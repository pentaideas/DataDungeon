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
                                  ("chapter02_queue", "QueueCompletion"),
                                  ("chapter03", "ProcessionCompletion"),
                                  ("chapter04", "DirectoryCompletion"),
                                  ("chapter05", "RegistryCompletion")]:
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
        output = Path(cls.temp.name) / "procession_starter"
        output.mkdir()
        subprocess.run(["javac", "--release", "11", "-d", str(output),
            *map(str, (ROOT / "starter/chapter03").glob("*.java"))],
            check=True, timeout=60)
        cls.classes["procession_starter"] = output
        output = Path(cls.temp.name) / "directory_starter"
        output.mkdir()
        subprocess.run(["javac", "--release", "11", "-d", str(output),
            *map(str, (ROOT / "starter/chapter04").glob("*.java"))],
            check=True, timeout=60)
        cls.classes["directory_starter"] = output
        output = Path(cls.temp.name) / "registry_starter"
        output.mkdir()
        subprocess.run(["javac", "--release", "11", "-d", str(output),
            *map(str, (ROOT / "starter/chapter05").glob("*.java"))],
            check=True, timeout=60)
        cls.classes["registry_starter"] = output

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

    def test_procession_requirements(self):
        self.assertIn("passed", self.run_java("chapter03", "ProcessionCompletion"))

    def test_directory_requirements(self):
        self.assertIn("passed", self.run_java("chapter04", "DirectoryCompletion"))

    def test_registry_requirements(self):
        self.assertIn("passed", self.run_java("chapter05", "RegistryCompletion"))

    def test_registry_starter(self):
        output = self.run_java("registry_starter", "RegistryGame",
            "find C\nfind Aa\nclaim BB\nregister D gate\nbuckets\nquit\n")
        for expected in ["Location: archive", "Not registered.",
                         "Cannot register.", "Size: 3",
                         "2: C=archive BB=forge Aa=well"]:
            self.assertIn(expected, output)

    def test_registry_complete_workshop(self):
        output = self.run_java("chapter05", "RegistryGame",
            "find BB\nclaim BB\nbuckets\nfind Aa\nregister Aa gate\n"
            "register BB sanctuary\nbuckets\nclaim BB\nclaim Aa\n"
            "claim C\nclaim C\nbuckets\nregister C gate\nfind C\nquit\n")
        for expected in ["Location: forge", "Claimed from: forge",
                         "2: C=archive Aa=well", "Location: well",
                         "Cannot register.", "2: BB=sanctuary C=archive Aa=well",
                         "Claimed from: sanctuary", "Claimed from: well",
                         "Claimed from: archive", "Size: 0", "Location: gate"]:
            self.assertIn(expected, output)

    def test_registry_only_permitted_method_bodies_changed(self):
        def mask(text):
            for signature in ["public String get(String id)",
                              "public boolean register(String id, String room)",
                              "public String remove(String id)"]:
                start = text.index("{", text.index(signature))
                depth, end = 1, start + 1
                while depth:
                    if text[end] == "{": depth += 1
                    if text[end] == "}": depth -= 1
                    end += 1
                text = text[:start] + "{}" + text[end:]
            return text
        self.assertEqual(mask((ROOT / "starter/chapter05/ArtifactRegistry.java").read_text()),
                         mask((ROOT / "instructor/chapter05/ArtifactRegistry.java").read_text()))

    def test_registry_edge_checks_detect_targeted_bugs(self):
        self.assertIn("passed", self.run_java("chapter05", "RegistryEdgeChecks"))
        correct = (ROOT / "instructor/chapter05/ArtifactRegistry.java").read_text()
        starter = (ROOT / "starter/chapter05/ArtifactRegistry.java").read_text()
        begin = correct.index("    public String get(")
        end = correct.index("    public boolean register(")
        starter_get = starter[starter.index("    public String get("):
                              starter.index("    public boolean register(")]
        mutants = {
            "head_only": (correct[:begin] + starter_get + correct[end:], "tail lookup"),
            "identity": (correct.replace("node.id.equals(id)", "node.id == id"),
                         "equal text in different objects"),
            "clear_bucket": (correct.replace("else previous.next = current.next;",
                                             "else buckets[index] = null;"),
                             "survivors after removal"),
        }
        for label, (source, message) in mutants.items():
            output = Path(self.temp.name) / label
            output.mkdir()
            java = output / "ArtifactRegistry.java"
            java.write_text(source)
            subprocess.run(["javac", "--release", "11", "-d", str(output), str(java),
                str(ROOT / "instructor/chapter05/RegistryEdgeChecks.java")],
                check=True, timeout=60)
            result = subprocess.run(["java", "-cp", str(output), "RegistryEdgeChecks"],
                capture_output=True, text=True, timeout=30)
            self.assertNotEqual(result.returncode, 0, label)
            self.assertIn(message, result.stderr, label)

    def test_directory_starter(self):
        output = self.run_java("directory_starter", "DirectoryGame",
            "ceiling 35\nexact 35\nadd 35 2 sanctuary\nlevels\nquit\n")
        for expected in ["40:archive", "Horizontal steps: 3", "No room.",
                         "Cannot add.", "Size: 6"]:
            self.assertIn(expected, output)

    def test_directory_complete_workshop(self):
        output = self.run_java("chapter04", "DirectoryGame",
            "ceiling 35\nexact 35\nadd 35 2 sanctuary\nceiling 31\n"
            "exact 35\nadd 35 3 replacement\nlevels\nceiling 61\n"
            "exact nope\nquit\n")
        for expected in ["40:archive", "Horizontal steps: 2", "Added.",
                         "35:sanctuary", "Cannot add.", "Size: 7",
                         "L2: 20 35 40", "No room.",
                         "Use integer numbers and heights."]:
            self.assertIn(expected, output)

    def test_directory_only_permitted_method_bodies_changed(self):
        def mask(text):
            for signature in ["private Node findBefore(int target)",
                              "public boolean add(int number, String name, int height)"]:
                start = text.index("{", text.index(signature))
                depth, end = 1, start + 1
                while depth:
                    if text[end] == "{": depth += 1
                    if text[end] == "}": depth -= 1
                    end += 1
                text = text[:start] + "{}" + text[end:]
            return text
        self.assertEqual(mask((ROOT / "starter/chapter04/RoomDirectory.java").read_text()),
                         mask((ROOT / "instructor/chapter04/RoomDirectory.java").read_text()))

    def test_procession_starter(self):
        output = self.run_java("procession_starter", "ProcessionGame",
            "list\nback\nprev\nnext\ninsert healer\nresolve\nquit\n")
        for expected in ["[scout, ogre, gate]", "[gate, ogre, scout]",
                         "No previous encounter.", "Cannot insert.",
                         "No encounter.", "Selected: ogre; size=3"]:
            self.assertIn(expected, output)

    def test_procession_complete_workshop(self):
        output = self.run_java("chapter03", "ProcessionGame",
            "next\ninsert healer\nlist\nresolve\nlist\nback\nresolve\n"
            "first\nresolve\nresolve\nresolve\ninsert torchbearer\n"
            "list\nback\nprev\nnext\nquit\n")
        for expected in ["[scout, healer, ogre, gate]", "[scout, healer, gate]",
                         "[gate, healer, scout]", "Selected: gate; size=3",
                         "Selected: healer; size=2", "Selected: null; size=0",
                         "No encounter.", "[torchbearer]",
                         "No previous encounter.", "No next encounter."]:
            self.assertIn(expected, output)
        self.assertNotIn("Cannot insert.", output)

    def test_procession_only_permitted_method_bodies_changed(self):
        starter = (ROOT / "starter/chapter03/EncounterList.java").read_text()
        solution = (ROOT / "instructor/chapter03/EncounterList.java").read_text()
        def mask(text):
            for signature in ["public boolean insertBefore(String name)",
                              "public String removeSelected()"]:
                start = text.index("{", text.index(signature))
                depth, end = 1, start + 1
                while depth:
                    if text[end] == "{": depth += 1
                    if text[end] == "}": depth -= 1
                    end += 1
                text = text[:start] + "{}" + text[end:]
            return text
        self.assertEqual(mask(starter), mask(solution))

    def test_permitted_edit_scope(self):
        for chapter, names in [("chapter01", ["Room.java", "Player.java", "Game.java"]),
                               ("chapter02", ["Item.java", "PackGame.java"]),
                               ("chapter02_queue", ["EncounterDesk.java"]),
                               ("chapter03", ["ProcessionGame.java"]),
                               ("chapter04", ["DirectoryGame.java"]),
                               ("chapter05", ["RegistryGame.java"])]:
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
                 ("chapter02_queue", "Data_Dungeon_Chapter_2_Circular_Queue_Workshop.docx"),
                 ("chapter03", "Data_Dungeon_Chapter_3_The_Shifting_Procession.docx"),
                 ("chapter04", "Data_Dungeon_Chapter_4_The_Ordered_Directory.docx"),
                 ("chapter05", "Data_Dungeon_Chapter_5_The_Keepers_Registry.docx")]
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
