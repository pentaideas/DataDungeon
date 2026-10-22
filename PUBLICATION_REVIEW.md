# Publication readiness review

Review date: 22 October 2026. Baseline: `9e5d7ba072368c11c012a30cfcf631075d8546c5`.

**Assessment: ready for focused human release review.** The existing collection is complete in scope. No additional chapters are needed. This review found no additional algorithmic defect in the reference solutions within the documented teaching assumptions. Automated tests and editorial review do not establish classroom effectiveness, universal accessibility, or ownership of every source contribution.

## Scope and evidence

Reviewed the 20 Word files (10 student activities, nine instructor guides, and the book plan), the ten independent starter/reference project pairs, verification harnesses, README, and licensing notices. Chapters 1–6 and 10–12, including the Chapter 2 circular queue, are the complete collection. The numbering follows Morin and intentionally skips other chapters.

- Compared explanations, contracts, expected traces, complexity claims, and teaching adaptations with the code and the relevant portions of Pat Morin's Java edition 0.1G. See [SOURCES.md](SOURCES.md) for exact readings and differences.
- Confirmed the baseline GitHub Actions run [39759338000](https://github.com/pentaideas/DataDungeon/actions/runs/39759338000): all 47 tests passed on Java 11 and Java 21 (nine starter checks and 38 completion checks per Java version).
- Reran the nine non-compilation completion checks after editing: permitted method boundaries, protected files, and Word/source correspondence passed. Java executable statements were not changed. The local review environment lacked `javac`; a GitHub Actions run after upload is the gate for the edited revision. The original review was delivered as a ZIP. Its subsequent GitHub import and verification are recorded below.
- Rendered all 20 revised Word files, totaling 162 pages. Reviewed page contact sheets and inspected selected changed pages at full resolution; no obvious clipping or overlap was found. This is not a page-by-page Microsoft Word compatibility certification.
- The structural document accessibility audit reported no high, medium, or low findings across the 20 files. It checks document structure, not actual screen-reader usability or conformance of future exports.
- Confirmed the linked Morin readings and license sources. This was not an exhaustive external-link crawl or a reconstruction of the original AI generation history.

## Corrections made

1. Strengthened Morin attribution and the CC BY 2.5 Canada exception in every Word file, LICENSE, NOTICE, and README. Added portable notices to all 55 starter/reference Java files and a chapter-specific source/adaptation map. Original contributions retain their existing CC0 dedication; Morin material is not relicensed as CC0.
2. Replaced obsolete future-chapter roadmap language with the complete agreed scope. Made the plan's graph activity specifically BFS, matching the supplied final chapter.
3. Corrected instructor advice suggesting that course-only distribution could keep already-public GitHub solutions private.
4. Clarified JDK setup and use of repository starter files, particularly in Chapters 1 and 2.
5. Qualified string-dependent complexity statements in the Chapter 2 and 3 instructor guides.
6. Updated the Word/source check to require the exact attribution header in Java and the license credit in the document, while still comparing executable listings verbatim.

Intentional starter defects remain part of the assignments. The test suite is a maintainer verification suite, not a complete grader for arbitrary student implementations.

## Chapter review record

| Activity | Content inspected | Human pilot focus |
|---|---|---|
| 1: Threshold | Object references, directed passages, navigation and allowed edits | Can novices distinguish a room object from a reference and follow the setup unaided? |
| 2: Pack | Capacity versus size, doubling, ordered removal, stale references, amortized cost | Have students explain one costly append versus a sequence of appends. |
| 2: Circular queue | Physical versus logical order, modulo indexing, wrapped growth, empty reuse | Observe a learner tracing growth after wraparound without resetting the wrong index. |
| 3: Procession | Dummy node, reciprocal links, cursor identity, insertion/removal | Check whether diagrams distinguish identity from equal names. |
| 4: Directory | Top-down search, predecessor array, exact/ceiling queries, explicit heights | Confirm learners understand that four fixed levels do not imply general logarithmic performance. |
| 5: Registry | Collisions, equality, chain survival, duplicate rejection, fixed-bucket costs | Require checks that surviving entries remain reachable after deletion. |
| 6: Archive | Whole-subtree ordering, lookup direction, leaf insertion, height | Ask learners to explain why sorted output does not demonstrate balance. |
| 10: Traps | Heap order, time/sequence tie-breaking, growth, child choice, event simulation | Observe right-child/one-child removal reasoning and equal-time ordering. |
| 11: Catalogue | Stable insertion/merge sorts, half-open ranges, counters, ties | Confirm learners can justify left-on-equality merging and interpret comparison counts. |
| 12: Labyrinth | Directed adjacency, BFS discovery, predecessor routes, unreachable vertices | Check discovery-time marking and shortest-path reasoning with cycles and ties. |

The limited structures are described as teaching adaptations: the skiplist has fixed explicit heights, hashing has five fixed buckets, the BST is unbalanced, and BFS addresses unweighted paths. The stable merge's tie handling differs intentionally from the cited textbook listing. These differences are not missing chapters or incomplete implementations of the assigned contracts.

## Human issues to address

These are open tasks, not claims that a problem has already been observed in a classroom.

| Priority | Issue and location | Owner and action | Evidence needed to close |
|---|---|---|---|
| Before final release | Source ownership and provenance: NOTICE, SOURCES, all publication notices | Author/maintainer: confirm authority to dedicate original contributions under CC0. Retain the supplied Michael Penta format source and its license; it was unavailable for independent review. Check any directly copied Morin expression for original notices that must travel with it. | A dated provenance record, including any third-party exceptions and retained notices. |
| Before final release | Existing “human-verified” claim on covers and README | Named subject reviewer: sign off on the actual release's explanations, answers, code, and traces. If the prior claim cannot be substantiated, revise it to accurately describe the review performed. An AI review cannot supply human sign-off. | Reviewer name, date, release commit, scope, and resolved corrections. |
| Before final release | Assistive-technology usability of Word documents | Accessibility reviewer and representative users: test heading navigation, reading order, table headers, code punctuation, image treatment, keyboard access, and zoom in the intended reader. | Recorded tools/versions, tasks, findings, and fixes. Audit any offered PDF/HTML exports separately. |
| Before classroom adoption | Timings and prerequisites are estimates | Instructor: pilot all ten activities with intended learners, including initial JDK setup. Record completion time, help requests, ambiguous instructions, and errors at the chapter-specific checkpoints above. | Pilot notes and revised estimates/instructions where evidence warrants. |
| Before assessed use | Reference solutions and instructor answers are public | Instructor: choose open-exemplar assessment or create fresh assessment variants. Explain permitted consultation and grade reasoning and evidence consistently. | Course assessment policy and any private variants; moving files to an LMS does not remove public copies. |
| Before classroom adoption | AI assistance and equivalent non-AI route | Instructor: apply local AI/privacy policy, try each prompt with an allowed tool, and also complete the supplied no-AI alternative. Confirm equivalent learning evidence and that no paid account is necessary. | A tested activity route for both access conditions and clear student instructions. |
| At release | Version and support route | Maintainer: record the release commit, date, reviewer sign-off and known limitations. Provide an error-reporting route and keep review-draft wording until its replacement is justified. | A versioned release note and working support link. |
| Optional improvement | Cross-platform usability and reading formats | Try repository download, folder selection, compilation and launch on the platforms students use. Consider accessible web/PDF reading copies while retaining editable originals. | Successful novice setup observations; separately verified exports if added. |

Use [GitHub Issues](https://github.com/pentaideas/DataDungeon/issues) to report errors with the chapter, page or filename, expected result, actual result, and release version. Do not include private student information.

## Release decision

Keep this as an instructor-review draft until the release-critical rows have documented closure and the revised branch's Java 11/21 CI passes. A classroom pilot can follow a clearly labeled public review release; do not describe unpiloted material as classroom-validated. No new chapters are part of this checklist.

## GitHub integration verification

On 22 October 2026, the uploaded publication-review package was imported into `main` as commit `289683bc80ff4ee82412e53d98aab559d6197dba`. Every package file was verified against the resulting Git tree. The import changes 82 files, including the 20 Word documents and portable notices on 55 Java files.

The follow-up review confirmed that stripping only the exact shared attribution header restores all 55 Java files byte-for-byte to their previously tested versions. All nine non-compilation consistency checks passed again, including allowed edit boundaries and Word/source correspondence. Fixed submission requirements and numeric grading allocations remain absent. All 20 documents rendered successfully to 162 pages; page overviews and selected revised pages were inspected.

The exact imported revision's [Java 11 and Java 21 workflow](https://github.com/pentaideas/DataDungeon/actions/runs/39815292642) was queued at the time this record was written. This record does not claim a passing result for that run. The earlier 47-test baseline remains the last confirmed full Java run until the linked workflow completes. This automated review is not human release sign-off.

## Licensing sources

- [Pat Morin's Open Data Structures site](https://opendatastructures.org/) and [Java edition](https://opendatastructures.org/ods-java/).
- [CC BY 2.5 Canada legal code](https://creativecommons.org/licenses/by/2.5/ca/legalcode.en), including attribution and redistribution conditions in section 4.
- [CC0 1.0](https://creativecommons.org/publicdomain/zero/1.0/) for original project contributions.

The edits make attribution and license scope explicit. Source-history confirmation remains necessary; this review does not certify rights to material whose provenance was not supplied.
