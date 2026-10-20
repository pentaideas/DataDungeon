# Data Dungeon: The Linked Labyrinth

Ancillary resources for Pat Morin’s Open Data Structures in Java.

This project contains editable Word student chapters, a book plan, decorative AI-generated artwork, and chapter-specific Java starter code. Assignments combine code investigation, implementation, testing, and purposeful AI learning practice.

## Materials

- `docs/Data_Dungeon_Book_Plan.docx`
- `docs/Data_Dungeon_Chapter_1_At_the_Threshold.docx`
- `docs/Data_Dungeon_Chapter_2_The_Adventurers_Pack.docx`
- `docs/Data_Dungeon_Chapter_2_Circular_Queue_Workshop.docx`
- `docs/Data_Dungeon_Chapter_1_Instructor_Guide.docx`
- `docs/Data_Dungeon_Chapter_2_Instructor_Guide.docx`
- `starter/chapter01/` — navigation foundation
- `starter/chapter02/` — inventory activity with deliberate student tasks
- `starter/chapter02_queue/` — independent circular-queue workshop
- `instructor/chapter01/`, `instructor/chapter02/`, `instructor/chapter02_queue/` — complete runnable reference solutions

## Status

Instructor-review drafts. Chapter 2 starter code deliberately refuses growth and leaves removal unfinished. All three activity projects have tested completion paths within their permitted edit scopes. The original starter checks and new completion checks total 17 tests, passing on Java 11 and Java 21. [Verified run](https://github.com/pentaideas/DataDungeon/actions/runs/39473271023). Word layouts have been reviewed. Publication licensing remains to be finalized.

The chapter format follows the supplied Python Adventure companion by Michael Penta. Textbook: https://opendatastructures.org/ods-java/

ChatGPT assisted with drafting, instructional design, and code. Decorative artwork was created with OpenAI image generation. Human review is required before classroom release.

## Verification

GitHub Actions checks changes to starter code, solutions, verification files, and Word documents on pushes to main and pull requests. Run the same checks locally with a JDK and Python 3:

```sh
python3 verification/verify_starters.py
python3 verification/verify_completion.py
```

The starter checks preserve the deliberate two-item inventory limit and unfinished removal. Completion checks compile the reference projects, exercise the assigned routes and boundary cases, verify permitted edit scope, and compare the Word starter listings with tested source files. Queue verification includes wrapped growth and 1,000 deterministic mixed operations compared with ArrayDeque. These are maintainer checks, not a general grading suite for arbitrary student solutions.

Instructor solutions are in this public repository and are visible to students. Compile each project folder separately.

## Completion standard for each chapter

Before advancing a chapter, include the student Word assignment, complete starter project, instructor Word guide, runnable reference solution, expected traces, assessment criteria, and teaching notes. Demonstrate each required feature using only permitted changes; test boundary behavior and verify that the Word listings match the Java sources. AI tasks must specify a purpose, permitted assistance, a concrete verification method, and an equivalent access alternative. Student timing and live AI interactions still require classroom observation.
