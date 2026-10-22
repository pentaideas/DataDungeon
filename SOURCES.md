# Sources and adaptation map

Data Dungeon: The Linked Labyrinth is a teaching companion to **Pat Morin, Open Data Structures, Java edition 0.1G**, <https://opendatastructures.org/ods-java/>. The textbook and accompanying source code are available under **Creative Commons Attribution 2.5 Canada**, <https://creativecommons.org/licenses/by/2.5/ca/>. The license is linked by the author's site, <https://opendatastructures.org/>. Sources checked on 22 October 2026.

These credits identify the source topics and teaching adaptations; they do not claim that Morin wrote the dungeon programs, tests, or instructor guidance. Any reproduced or adapted Morin expression remains subject to his license. Original project contributions remain under CC0 1.0. This is not a choice to redistribute Morin material under CC0. See [NOTICE.md](NOTICE.md) and [LICENSE](LICENSE).

## Complete chapter scope

Chapters 1–6 and 10–12, plus the Chapter 2 circular-queue workshop, are the complete planned collection. No additional chapters are planned. Numbering follows Morin; it is not a claim to cover every topic in those chapters.

The folders below occur in both `starter/` and `instructor/`. Each is an independent project. Its student chapter and instructor guide in `docs/` cover the same adaptation; the Chapter 2 guide covers both activities.

| Folder | Morin reading | Teaching adaptation and differences | Main class |
|---|---|---|---|
| `chapter01` | [Chapter 1](https://opendatastructures.org/ods-java/1_Introduction.html), especially interfaces and implementations | Java object-reference preparation through rooms and directed passages; not a full implementation of Chapter 1 interfaces. | `Game` |
| `chapter02` | [§2.1 ArrayStack](https://opendatastructures.org/ods-java/2_1_ArrayStack_Fast_Stack_O.html) | `Inventory`: Item references, append-only growth by doubling, identifier search and ordered removal; no shrinking or full List interface. | `PackGame` |
| `chapter02_queue` | [§2.3 ArrayQueue](https://opendatastructures.org/ods-java/2_3_ArrayQueue_Array_Based_.html) | `EncounterQueue`: String encounters, explicit physical-state display, doubling without shrinking, advanced front retained after draining. | `EncounterDesk` |
| `chapter03` | [§3.2 DLList](https://opendatastructures.org/ods-java/3_2_DLList_Doubly_Linked_Li.html) | `EncounterList`: circular dummy node, cursor-based insertion/removal, explicit detachment; no indexed List implementation. | `ProcessionGame` |
| `chapter04` | [§4.1 structure](https://opendatastructures.org/ods-java/4_1_Basic_Structure.html) and [§4.2 SkiplistSSet](https://opendatastructures.org/ods-java/4_2_SkiplistSSet_Efficient_.html) | `RoomDirectory`: integer room keys, exact and ceiling queries, explicit heights 0–3 and measured rightward moves; no random heights, deletion, or general logarithmic guarantee. | `DirectoryGame` |
| `chapter05` | [§5.1 ChainedHashTable](https://opendatastructures.org/ods-java/5_1_ChainedHashTable_Hashin.html) | `ArtifactRegistry`: identifier-to-room pairs, five fixed buckets, explicit linked chains and floorMod; no resizing or randomized hashing. | `RegistryGame` |
| `chapter06` | [§6.1 terminology](https://opendatastructures.org/ods-java/6_1_BinaryTree_Basic_Binary.html) and [§6.2 BinarySearchTree](https://opendatastructures.org/ods-java/6_2_BinarySearchTree_Unbala.html) | `DoorArchive`: exact integer-key lookup and leaf insertion; local parent tracking; no deletion, successor lookup, or balancing. | `ArchiveGame` |
| `chapter10` | [§10.1 BinaryHeap](https://opendatastructures.org/ods-java/10_1_BinaryHeap_Implicit_Bi.html) | `TrapQueue`: event time plus registration sequence; supplied doubling and simulation; no shrinking, cancellation, or melding. | `TrapGame` |
| `chapter11` | [§11.1.1 merge sort](https://opendatastructures.org/ods-java/11_1_Comparison_Based_Sorti.html) | `CatalogueSort`: half-open ranges, shared buffer, left-on-equality stable merge, stable insertion-sort baseline and comparison counts. | `CatalogueGame` |
| `chapter12` | [§12.2 adjacency lists](https://opendatastructures.org/ods-java/12_2_AdjacencyLists_Graph_a.html) and [§12.3.1 BFS](https://opendatastructures.org/ods-java/12_3_Graph_Traversal.html) | `MazeGraph` and `RouteFinder`: fixed copied adjacency arrays, ArrayDeque frontier, distances, predecessor routes and invalid-index results; no DFS or weighted paths. | `LabyrinthGame` |

The project adds the dungeon setting, console drivers, sample fixtures, deliberate starter defects, implementation contracts, assessment guidance, AI/no-AI exercises, and verification harnesses. This map describes observable relationships; it is not a reconstruction of unrecorded AI generation history. The maintainer should retain any original source notices or additional provenance records supplied with copied material.

## Redistributing extracts

Keep Morin's name, title, source link, applicable license, and adaptation credit with extracted teaching material or code. Preserve any upstream copyright and warranty notices. The Java files carry a portable source notice; Word documents carry their own publication notice. The Word code listings omit the Java files' shared notice header to avoid repeating it on each listing; their executable code is checked verbatim.

For example: “Teaching adaptation of selected topics from Pat Morin, Open Data Structures (Java), https://opendatastructures.org/ods-java/, CC BY 2.5 Canada, https://creativecommons.org/licenses/by/2.5/ca/. Data Dungeon adds dungeon examples and simplified exercise contracts; see the chapter map for changes. No endorsement is implied.”

## Other credits

The existing project records Michael Penta's *Companion Materials for OpenStax: Introduction to Python Programming*, Chapter 5 (2025), as a supplied CC0 format reference. That source document is not included here; its license/provenance should be retained by the maintainer. Decorative artwork is disclosed as generated using OpenAI image generation. Exact ChatGPT model versions were not recorded. Neither AI disclosure nor source attribution implies a completed classroom pilot.
