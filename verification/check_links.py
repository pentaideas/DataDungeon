"""Check external HTTP(S) links in Markdown and Word, using Python's stdlib.

Exit 1 for missing pages or unreadable source files. Blocked and inconclusive
requests remain visible warnings, not claims that a page is missing.
"""
import argparse
from collections import defaultdict
from concurrent.futures import ThreadPoolExecutor
import html
import json
from pathlib import Path
import re
import time
import urllib.error
import urllib.parse
import urllib.request
import xml.etree.ElementTree as ET
import zipfile


URL = re.compile(r"https?://[^\s<>\"`\[\]]+")
W = "{http://schemas.openxmlformats.org/wordprocessingml/2006/main}"


def text_urls(text):
    for match in URL.finditer(html.unescape(text)):
        url = match.group().rstrip(".,;:!?")
        while url.endswith(")") and url.count(")") > url.count("("):
            url = url[:-1]
        yield url


def document_urls(path):
    if path.suffix.lower() == ".md":
        yield from text_urls(path.read_text(encoding="utf-8"))
        return
    with zipfile.ZipFile(path) as archive:
        for name in archive.namelist():
            if name.endswith(".rels"):
                for rel in ET.fromstring(archive.read(name)):
                    target = rel.get("Target", "")
                    if rel.get("TargetMode") == "External" and target.startswith(("http://", "https://")):
                        yield target
            elif name.startswith("word/") and name.endswith(".xml"):
                root = ET.fromstring(archive.read(name))
                # Join runs inside paragraphs: Word often splits a visible URL.
                for paragraph in root.iter(W + "p"):
                    yield from text_urls("".join(t.text or "" for t in paragraph.iter(W + "t")))
                for field in root.iter(W + "instrText"):
                    yield from text_urls(field.text or "")


def collect(root):
    links = defaultdict(set)
    errors = []
    for path in sorted(root.rglob("*")):
        if any(p.startswith(".") for p in path.relative_to(root).parts):
            continue
        if path.suffix.lower() not in {".md", ".docx"} or not path.is_file():
            continue
        try:
            for url in document_urls(path):
                # Fetch the page once; this checker does not validate anchors.
                url = urllib.parse.urldefrag(url)[0]
                links[url].add(path.relative_to(root).as_posix())
        except (OSError, ValueError, ET.ParseError, zipfile.BadZipFile) as exc:
            errors.append({"file": str(path.relative_to(root)), "error": str(exc)})
    return links, errors


def classify(status):
    if 200 <= status < 300:
        return "ok"
    if status in (404, 410):
        return "missing"
    if status in (401, 403, 407, 429, 451):
        return "blocked"
    return "inconclusive"


def check(url):
    result = {"url": url}
    for attempt in range(2):
        try:
            request = urllib.request.Request(url, headers={"User-Agent": "DataDungeon-LinkChecker/1.0", "Accept": "text/html,*/*;q=0.8"})
            # GET avoids false failures from servers that reject HEAD. Do not
            # download bodies; redirects are followed by urllib.
            with urllib.request.urlopen(request, timeout=20) as response:
                status = response.status
                result.update(status=status, final_url=response.url)
        except urllib.error.HTTPError as exc:
            status = exc.code
            result.update(status=status, final_url=exc.url)
            exc.close()
        except (urllib.error.URLError, TimeoutError, OSError, ValueError) as exc:
            result.update(category="inconclusive", detail=str(exc))
            if attempt == 0:
                time.sleep(1)
                continue
            return result
        result["category"] = classify(status)
        if (status >= 500 or status in (404, 410, 429)) and attempt == 0:
            time.sleep(1)
            continue
        return result
    return result


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--root", type=Path, default=Path(__file__).resolve().parents[1])
    parser.add_argument("--report", type=Path, default=Path("link-report.json"))
    args = parser.parse_args()
    links, errors = collect(args.root)
    results = []
    with ThreadPoolExecutor(max_workers=4) as pool:
        for result in pool.map(check, sorted(links)):
            result["sources"] = sorted(links[result["url"]])
            results.append(result)
            print(f"{result['category'].upper()}: {result['url']} ({result.get('status', result.get('detail', ''))})")
    args.report.write_text(json.dumps({"links": results, "source_errors": errors}, indent=2) + "\n", encoding="utf-8")
    counts = {category: sum(r["category"] == category for r in results) for category in ("ok", "missing", "blocked", "inconclusive")}
    print(f"Summary: {counts}; source errors: {len(errors)}")
    for error in errors:
        print(f"SOURCE ERROR: {error['file']}: {error['error']}")
    # GitHub annotations contain counts only, never untrusted remote text.
    if counts["blocked"] or counts["inconclusive"]:
        print("::warning::Some links could not be verified; inspect the link report for blocked or inconclusive requests.")
    return int(bool(counts["missing"] or errors))


if __name__ == "__main__":
    raise SystemExit(main())
