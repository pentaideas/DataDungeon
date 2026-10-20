"""Verify reference solutions, permitted edit scope, and assignment routes."""
from pathlib import Path
import subprocess
import tempfile
import unittest
import zipfile
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]


class CompletionChecks(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.temp = tempfile.TemporaryDirectory(prefix="dungeon-completion-")
        cls.addClassCleanup(cls.temp.cleanup)
        cls.classes = {}
        for chapter, harness in [("chapter01", "Chapter1Completion"),
                                  ("chapter02", "InventoryCompletion"),
                                  ("chapter02_queue", "QueueCompletion")]:
            output = Path(cls.temp.name) / chapter
            output.mkdir()
            sources = list((ROOT / "instructor" / chapter).glob("*.java"))
            subprocess.run(["javac", "--release", "11", "-d", str(output),
                *map(str, sources), str(ROOT / "verification" / (harness + ".java"))],
                check=True, timeout=60)
            cls.classes[chapter] = output
        output = Path(cls.temp.name) / "queue_starter"
        output.mkdir()
        subprocess.run(["javac", "--release", "11", "-d", str(output),
            *map(str, (ROOT / "starter/chapter02_queue").glob("*.java"))],
            check=True, timeout=60)
        cls.classes["queue_starter"] = output

    def run_java(self, project, main, commands=""):
        result = subprocess.run(["java", "-cp", str(self.classes[project]), main],
            input=commands, text=True, capture_output=True, timeout=30)
        self.assertEqual(result.returncode, 0, result.stdout + result.stderr)
        self.assertEqual(result.stderr, "")
        return result.stdout.replace("> ", "")

    def test_chapter1_requirements(self):
        self.assertIn("passed", self.run_java("chapter01", "Chapter1Completion"))

    def test_inventory_requirements(self):
        self.assertIn("passed", self.run_java("chapter02", "InventoryCompletion"))

    def test_queue_requirements(self):
        self.assertIn("passed", self.run_java("chapter02_queue", "QueueCompletion"))

    def test_permitted_edit_scope(self):
        for chapter, names in [("chapter01", ["Room.java", "Player.java", "Game.java"]),
                               ("chapter02", ["Item.java", "PackGame.java"]),
                               ("chapter02_queue", ["EncounterDesk.java"])]:
            for name in names:
                with self.subTest(chapter=chapter, file=name):
                    self.assertEqual((ROOT / "starter" / chapter / name).read_bytes(),
                                     (ROOT / "instructor" / chapter / name).read_bytes())

    def test_inventory_complete_adventure(self):
        output = self.run_java("chapter02", "PackGame",
            "go\ntake lantern\ntake rope\ntake key\ntake map\ntake relic\n"
            "drop key\nlook\ninventory\ntake key\ninventory\ngo\nquit\n")
        self.assertIn("On the floor: [key]", output)
        self.assertIn("lantern\nrope\nmap\nrelic\nSize 4, capacity 8", output)
        self.assertIn("lantern\nrope\nmap\nrelic\nkey\nSize 5, capacity 8", output)
        self.assertIn("Relic recovered! You may continue.", output)
        self.assertNotIn("Cannot", output)

    def test_queue_starter_limit_and_shifting(self):
        output = self.run_java("queue_starter", "EncounterDesk",
            "join A\njoin B\njoin C\njoin D\njoin E\nnext\nstate\nquit\n")
        self.assertIn("Cannot join.", output)
        self.assertIn("Encounter: A", output)
        self.assertIn("[B, C, D, null] front=0 size=3", output)

    def test_queue_complete_workshop(self):
        commands = "join A\njoin B\njoin C\njoin D\nnext\nnext\njoin E\njoin F\n"
        commands += "state\njoin G\nstate\npeek\n" + "next\n" * 6 + "quit\n"
        output = self.run_java("chapter02_queue", "EncounterDesk", commands)
        self.assertIn("[E, F, C, D] front=2 size=4", output)
        self.assertIn("[C, D, E, F, G, null, null, null] front=0 size=5", output)
        self.assertIn("Next: C", output)
        encounters = [line for line in output.splitlines() if line.startswith("Encounter:")]
        self.assertEqual(encounters, ["Encounter: " + x for x in "ABCDEFG"])
        self.assertIn("No encounters.", output)

    def test_word_starter_listings_match_tested_files(self):
        namespace = {"w": "http://schemas.openxmlformats.org/wordprocessingml/2006/main"}
        pairs = [("chapter01", "Data_Dungeon_Chapter_1_At_the_Threshold.docx"),
                 ("chapter02", "Data_Dungeon_Chapter_2_The_Adventurers_Pack.docx"),
                 ("chapter02_queue", "Data_Dungeon_Chapter_2_Circular_Queue_Workshop.docx")]
        for chapter, filename in pairs:
            with zipfile.ZipFile(ROOT / "docs" / filename) as archive:
                root = ET.fromstring(archive.read("word/document.xml"))
            code = []
            for paragraph in root.findall(".//w:body/w:p", namespace):
                for run in paragraph.findall("w:r", namespace):
                    font = run.find("w:rPr/w:rFonts", namespace)
                    if font is not None and "Mono" in font.get("{" + namespace["w"] + "}ascii", ""):
                        text = ""
                        for part in run:
                            if part.tag == "{" + namespace["w"] + "}t": text += part.text or ""
                            elif part.tag == "{" + namespace["w"] + "}br": text += "\n"
                            elif part.tag == "{" + namespace["w"] + "}tab": text += "\t"
                        code.append(text)
            combined = "\n".join(code)
            for source in (ROOT / "starter" / chapter).glob("*.java"):
                self.assertIn(source.read_text().strip(), combined,
                              f"Word listing differs: {chapter}/{source.name}")


if __name__ == "__main__":
    unittest.main(verbosity=2)
