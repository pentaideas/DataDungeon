"""Maintainer smoke tests for the supplied starters, not student solutions.

Run from a checkout with Python 3 and a JDK on PATH:
    python3 verification/verify_starters.py
Chapter 2 intentionally has a two-item limit and an unfinished remove method.
"""
from pathlib import Path
import subprocess
import tempfile
import unittest


ROOT = Path(__file__).resolve().parents[1]


class StarterChecks(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.temp = tempfile.TemporaryDirectory(prefix="data-dungeon-check-")
        cls.addClassCleanup(cls.temp.cleanup)
        cls.classes = {}
        for chapter in ("chapter01", "chapter02"):
            target = Path(cls.temp.name) / chapter
            target.mkdir()
            sources = sorted((ROOT / "starter" / chapter).glob("*.java"))
            if not sources:
                raise RuntimeError(f"Missing sources for {chapter}")
            subprocess.run(
                ["javac", "--release", "11", "-d", str(target),
                 *map(str, sources)], check=True, timeout=60,
            )
            cls.classes[chapter] = target
            print(f"Compiled {chapter} for Java 11", flush=True)

    def play(self, chapter, commands):
        main = "Game" if chapter == "chapter01" else "PackGame"
        result = subprocess.run(
            ["java", "-cp", str(self.classes[chapter]), main],
            input=commands, text=True, capture_output=True, timeout=10,
        )
        self.assertEqual(result.returncode, 0, result.stderr)
        self.assertEqual(result.stderr, "")
        lines = result.stdout.replace("> ", "").splitlines()
        return lines[1 if chapter == "chapter01" else 2:]

    def test_navigation_cycle_and_shared_destination(self):
        self.assertEqual(self.play("chapter01",
            "go 0\ngo 1\ngo 0\ngo 1\ngo 0\nquit\n"), [
                "Gallery", "0: Entrance", "1: Archive",
                "Archive", "0: Entrance",
                "Entrance", "0: Gallery", "1: Well",
                "Well", "0: Archive", "Archive", "0: Entrance",
            ])

    def test_invalid_moves_preserve_location(self):
        self.assertEqual(self.play("chapter01",
            "go 2\ngo -1\ngo 99\ngo two\nlook\nquit\n"), [
                "No passage there.", "No passage there.",
                "No passage there.", "Use an integer index.",
                "Entrance", "0: Gallery", "1: Well",
            ])

    def test_navigation_eof_and_unknown_command(self):
        self.assertEqual(self.play("chapter01", ""), [])
        self.assertEqual(self.play("chapter01", "help\n"),
                         ["Use look, go <index>, or quit."])

    def test_empty_pack_and_missing_items(self):
        self.assertEqual(self.play("chapter02",
            "inventory\ntake ghost\ndrop ghost\nlook\nquit\n"), [
                "Size 0, capacity 2", "Cannot take that.",
                "Cannot drop that.", "Entrance", "On the floor: []",
            ])

    def test_capacity_limit_preserves_room_and_pack(self):
        self.assertEqual(self.play("chapter02",
            "go\ntake lantern\ntake rope\ntake key\n"
            "take lantern\nlook\ninventory\nquit\n"), [
                "Supply chamber", "Collected lantern", "Collected rope",
                "Cannot take that.", "Cannot take that.",
                "Supply chamber", "On the floor: [key, map, relic]",
                "lantern", "rope", "Size 2, capacity 2",
            ])

    def test_unfinished_drop_preserves_item(self):
        self.assertEqual(self.play("chapter02",
            "go\ntake rope\ndrop rope\nlook\ninventory\nquit\n"), [
                "Supply chamber", "Collected rope", "Cannot drop that.",
                "Supply chamber", "On the floor: [lantern, key, map, relic]",
                "rope", "Size 1, capacity 2",
            ])

    def test_relic_return_and_no_premature_success(self):
        self.assertEqual(self.play("chapter02",
            "go\ntake relic\ngo\nquit\n"), [
                "Supply chamber", "Collected relic", "Entrance",
                "Relic recovered! You may continue.",
            ])

    def test_pack_eof_and_unknown_command(self):
        self.assertEqual(self.play("chapter02", ""), [])
        self.assertEqual(self.play("chapter02", "nonsense\n"),
                         ["Unknown command."])


if __name__ == "__main__":
    unittest.main(verbosity=2)
