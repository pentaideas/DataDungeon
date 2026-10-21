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
- `docs/Data_Dungeon_Chapter_3_The_Shifting_Procession.docx`
- `docs/Data_Dungeon_Chapter_3_Instructor_Guide.docx`
- `starter/chapter03/` — independent linked procession workshop
- `instructor/chapter03/` — runnable linked-list reference solution
- `docs/Data_Dungeon_Chapter_4_The_Ordered_Directory.docx`
- `docs/Data_Dungeon_Chapter_4_Instructor_Guide.docx`
- `starter/chapter04/` — optional skiplist search and insertion workshop
- `instructor/chapter04/` — runnable ordered-directory reference solution
- `docs/Data_Dungeon_Chapter_5_The_Keepers_Registry.docx`
- `docs/Data_Dungeon_Chapter_5_Instructor_Guide.docx`
- `starter/chapter05/` — chained artifact registry with lookup, registration, and removal
- `instructor/chapter05/` — runnable solution and small edge-case checker
- `docs/Data_Dungeon_Chapter_6_The_Archive_of_Doors.docx`
- `docs/Data_Dungeon_Chapter_6_Instructor_Guide.docx`
- `starter/chapter06/` — binary search tree lookup, insertion, and insertion-order workshop
- `instructor/chapter06/` — runnable reference solution and diagnosis checker
- `docs/Data_Dungeon_Chapter_10_The_Awakening_Traps.docx`
- `docs/Data_Dungeon_Chapter_10_Instructor_Guide.docx`
- `starter/chapter10/` — binary min-heap event scheduler with traps added during play
- `instructor/chapter10/` — runnable reference solution and compact checker
- `starter/chapter01/` — navigation foundation
- `starter/chapter02/` — inventory activity with deliberate student tasks
- `starter/chapter02_queue/` — independent circular-queue workshop
- `instructor/chapter01/`, `instructor/chapter02/`, `instructor/chapter02_queue/` — complete runnable reference solutions

## Status

Instructor-review drafts. Chapter 2 starter code deliberately refuses growth and leaves removal unfinished. All eight activity projects have tested completion paths within their permitted edit scopes. The original starter checks and new completion checks total 39 tests, passing on Java 11 and Java 21. [Verified run](https://github.com/pentaideas/DataDungeon/actions/runs/39615951723). Word layouts have been reviewed. Publication licensing remains to be finalized.

The chapter format follows the supplied Python Adventure companion by Michael Penta. Textbook: https://opendatastructures.org/ods-java/

ChatGPT assisted with drafting, instructional design, and code. Decorative artwork was created with OpenAI image generation. Human review is required before classroom release.

## Verification

GitHub Actions checks changes to starter code, solutions, verification files, and Word documents on pushes to main and pull requests. Run the same checks locally with a JDK and Python 3:

```sh
python3 verification/verify_starters.py
python3 verification/verify_completion.py
```

The starter checks preserve the deliberate two-item inventory limit and unfinished removal. Completion checks compile the reference projects, exercise the assigned routes and boundary cases, verify permitted edit scope, and compare the Word starter listings with tested source files. Queue verification includes wrapped growth and 1,000 deterministic mixed operations compared with ArrayDeque. Chapter 3 verification includes 2,000 seeded operations compared with an independent sequence model, reciprocal-link and cursor-identity checks, empty-list reuse, and an exact check that only the two permitted method bodies changed. Chapter 4 checks include 1,000 seeded insertion attempts and queries compared with TreeMap, level membership and shared node identities, exact search-step traces, and permitted edits. Chapter 4 uses explicit heights capped at 3; it does not claim a general logarithmic performance guarantee. Chapter 5 checks include 1,500 seeded operations compared with HashMap, chain membership and survivor identity, negative hash codes, and the three-method edit boundary. The small edge checker is also run against three deliberate defects to confirm that its tests detect head-only lookup, string reference comparison, and destructive bucket removal. The Chapter 5 teaching table has five fixed buckets and does not resize. Chapter 6 checks include 800 seeded operations compared with TreeMap, whole-subtree ordering, node identities, integer boundaries, duplicate rejection, heights from two insertion orders, and the two-method edit boundary. Its diagnosis checker fails on the starter’s reversed branch selection and passes on the repaired solution. Chapter 10 checks include 1,500 seeded operations compared with PriorityQueue, heap order and active-array membership, growth, equal-time ordering, right-child and one-child removals, empty reuse, and the two-method edit boundary. The console adventure verifies follow-up events during play, past-time rejection, and overflow handling. These are maintainer checks, not a general grading suite for arbitrary student solutions.

Instructor solutions are in this public repository and are visible to students. Compile each project folder separately.

## Completion standard for each chapter

Before advancing a chapter, include the student Word assignment, complete starter project, instructor Word guide, runnable reference solution, expected traces, assessment criteria, and teaching notes. Demonstrate each required feature using only permitted changes; test boundary behavior and verify that the Word listings match the Java sources. AI tasks must specify a purpose, permitted assistance, a concrete verification method, and an equivalent access alternative. Student timing and live AI interactions still require classroom observation.
