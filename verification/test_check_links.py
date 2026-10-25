import tempfile
from pathlib import Path
import unittest
import zipfile

from check_links import classify, collect, text_urls


class LinkChecks(unittest.TestCase):
    def test_markdown_punctuation_and_balanced_parentheses(self):
        self.assertEqual(list(text_urls('[source](https://example.org/a_(b)). <https://example.org/b>')), ['https://example.org/a_(b)', 'https://example.org/b'])

    def test_word_relationships_split_runs_headers_and_deduplication(self):
        with tempfile.TemporaryDirectory() as folder:
            root = Path(folder)
            (root / 'README.md').write_text('[Example](https://example.org/page#one)', encoding='utf-8')
            with zipfile.ZipFile(root / 'chapter.docx', 'w') as archive:
                archive.writestr('word/_rels/document.xml.rels', '<Relationships><Relationship TargetMode="External" Target="https://example.org/page#two"/></Relationships>')
                archive.writestr('word/header1.xml', '<w:hdr xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main"><w:p><w:r><w:t>https://example.</w:t></w:r><w:r><w:t>org/header</w:t></w:r></w:p></w:hdr>')
            links, errors = collect(root)
            self.assertFalse(errors)
            self.assertEqual(links['https://example.org/page'], {'README.md', 'chapter.docx'})
            self.assertEqual(links['https://example.org/header'], {'chapter.docx'})

    def test_access_blocks_are_not_missing_pages(self):
        for status in (401, 403, 407, 429, 451):
            self.assertEqual(classify(status), 'blocked')
        for status in (404, 410):
            self.assertEqual(classify(status), 'missing')
        self.assertEqual(classify(503), 'inconclusive')
        self.assertEqual(classify(200), 'ok')

    def test_invalid_word_file_is_reported(self):
        with tempfile.TemporaryDirectory() as folder:
            root = Path(folder)
            (root / 'broken.docx').write_bytes(b'not a zip')
            _, errors = collect(root)
            self.assertEqual(errors[0]['file'], 'broken.docx')


if __name__ == '__main__':
    unittest.main()
