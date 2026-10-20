# Data Dungeon: The Linked Labyrinth

Ancillary resources for Pat Morin’s Open Data Structures in Java.

This project contains editable Word student chapters, a book plan, decorative AI-generated artwork, and chapter-specific Java starter code. Assignments combine code investigation, implementation, testing, and purposeful AI learning practice.

## Materials

- `docs/Data_Dungeon_Book_Plan.docx`
- `docs/Data_Dungeon_Chapter_1_At_the_Threshold.docx`
- `docs/Data_Dungeon_Chapter_2_The_Adventurers_Pack.docx`
- `starter/chapter01/` — navigation foundation
- `starter/chapter02/` — inventory activity with deliberate student tasks

## Status

Instructor-review drafts. Chapter 2 starter code deliberately refuses growth and leaves removal unfinished. Both starter projects compile for Java 11 and pass all eight behavioral smoke tests on Java 11 and Java 21. [Verified run](https://github.com/pentaideas/DataDungeon/actions/runs/39470099906). Word layouts have been reviewed. Publication licensing remains to be finalized.

The chapter format follows the supplied Python Adventure companion by Michael Penta. Textbook: https://opendatastructures.org/ods-java/

ChatGPT assisted with drafting, instructional design, and code. Decorative artwork was created with OpenAI image generation. Human review is required before classroom release.

## Verification

GitHub Actions checks starter changes on pushes to main and pull requests. Run the same checks locally with a JDK and Python 3:

```sh
python3 verification/verify_starters.py
```

These are maintainer checks for the supplied starting behavior, not a grading suite for completed student solutions. Chapter 2 tests deliberately expect the two-item limit and unfinished removal. They cover navigation, invalid input, end-of-input handling, item transfer, capacity rejection, and returning with the relic.
