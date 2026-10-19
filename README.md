<img width="2172" height="724" alt="6D10B86A-3228-4A00-BA79-C505B6495A8F" src="https://github.com/user-attachments/assets/3693485d-618f-4564-83fc-23b28116f3f5" />

# CSC 3100 — Introduction to Software Engineering

Shared Java 17/Maven repository for course work connecting **requirements, design, implementation, testing, documentation, and code review**. GitHub Issues define User Stories and Tasks; Pull Requests capture implementation and review history.

## Assignments

### A01 — Requirements and Initial Implementation (Sprint 1)

Students worked from assigned User Stories, decomposed requirements into Tasks, implemented Java functionality, documented designs with UML and JavaDoc, and submitted changes for review through Pull Requests. Sprint 1 also introduced the Maven project structure.

| User Story | GitHub assignee(s) |
| --- | --- |
| [#6 — Enter Message](https://github.com/CSC-Sandbox/3100-requirements/issues/6) | See issue and associated PRs |
| [#9 — Detect Malformed Messages](https://github.com/CSC-Sandbox/3100-requirements/issues/9) | [@Omarc2007](https://github.com/Omarc2007) |
| [#11 — Display Real-Time Robot Pose](https://github.com/CSC-Sandbox/3100-requirements/issues/11) | [@srinityad](https://github.com/srinityad) |
| [#13 — Gather Robot Pose](https://github.com/CSC-Sandbox/3100-requirements/issues/13) | [@sipactivism](https://github.com/sipactivism) |
| [#23 — Display Message History](https://github.com/CSC-Sandbox/3100-requirements/issues/23) | See issue and associated PRs |
| [#24 — Encrypt Messages](https://github.com/CSC-Sandbox/3100-requirements/issues/24) | [@anthony-Soto](https://github.com/anthony-Soto) |
| [#26 — Storing Messages](https://github.com/CSC-Sandbox/3100-requirements/issues/26) | See issue and associated PRs |
| [#34 — Gather Eye-Tracking Data from Device](https://github.com/CSC-Sandbox/3100-requirements/issues/34) | [@mars-rams](https://github.com/mars-rams) |
| [#35 — Display LiDAR Map](https://github.com/CSC-Sandbox/3100-requirements/issues/35) | [@MKHLimkilde](https://github.com/MKHLimkilde) |
| [#36 — Monitor Data Availability](https://github.com/CSC-Sandbox/3100-requirements/issues/36) | [@tphan56](https://github.com/tphan56) |
| [#37 — Record User Session with Stored Messages](https://github.com/CSC-Sandbox/3100-requirements/issues/37) | See issue and associated PRs |
| [#40 — Display Real-Time Affective State](https://github.com/CSC-Sandbox/3100-requirements/issues/40) | [@an-prata](https://github.com/an-prata) |
| [#42 — Display Recent Data Activity](https://github.com/CSC-Sandbox/3100-requirements/issues/42) | [@sp4msvwnz](https://github.com/sp4msvwnz) |

### A02 — Design, Libraries, and Integration (Sprint 2)

Students extended existing functionality with external Java libraries, Maven dependencies, integration, tests, and updated UML documentation. The table reflects **GitHub issue assignees**, not an exhaustive list of all contributors; see the linked issues and Pull Requests for contribution history.

| User Story | GitHub assignee(s) |
| --- | --- |
| [#135 — Visualize LiDAR Data in 3D](https://github.com/CSC-Sandbox/3100-requirements/issues/135) | [@sp4msvwnz](https://github.com/sp4msvwnz), [@MKHLimkilde](https://github.com/MKHLimkilde) |
| [#141 — Classify System Activity](https://github.com/CSC-Sandbox/3100-requirements/issues/141) | [@an-prata](https://github.com/an-prata), [@tphan56](https://github.com/tphan56) |
| [#147 — Manage Stored Messages as CSV](https://github.com/CSC-Sandbox/3100-requirements/issues/147) | [@ReeledWarrior14](https://github.com/ReeledWarrior14), [@Landon-McCaslin](https://github.com/Landon-McCaslin) |
| [#154 — Enhance Message Security Processing](https://github.com/CSC-Sandbox/3100-requirements/issues/154) | [@Omarc2007](https://github.com/Omarc2007), [@anthony-Soto](https://github.com/anthony-Soto) |
| [#160 — Analyze Gaze Data](https://github.com/CSC-Sandbox/3100-requirements/issues/160) | [@dkguru27](https://github.com/dkguru27), [@mars-rams](https://github.com/mars-rams) |
| [#166 — Improve Message Input Processing](https://github.com/CSC-Sandbox/3100-requirements/issues/166) | [@matthewdaviii](https://github.com/matthewdaviii) |
| [#172 — Improve Robot Data Diagnostics](https://github.com/CSC-Sandbox/3100-requirements/issues/172) | [@srinityad](https://github.com/srinityad), [@sipactivism](https://github.com/sipactivism) |

## Important Notes — Required for Every Submission

 **Mandatory UML diagram:** Include a UML class diagram exported as **PNG (`.png`) or JPEG (`.jpg` / `.jpeg`)** in the appropriate `docs/story-<number>-<name>/` folder. Do not submit an Astah `.asta` file in place of the image. Use `uml.png` or `uml.jpeg` when possible.

 **Mandatory Story-linked Pull Request:** Submit changes through a Pull Request, never directly to `main`. In the PR description, fill out **`## User Story`** with **exactly one** Story issue reference (for example, `#135`) and **`## Tasks`** with **at least one** Task issue reference (for example, `#183`). Every listed Task must be a **native GitHub sub-issue** of that Story. Merely mentioning a Task in text or linking an unrelated issue does not establish the required relationship.

 **Automated validation:** The **Story and Task references** check fails if the Story or Tasks sections are missing, if there are zero or multiple Story references, if an issue number does not exist, or if a referenced Task is not a sub-issue of the Story. GitHub Actions reports the specific error in the check logs. Fix the PR description or the issue relationship and rerun the check.

 **Build validation:** The Maven check may fail for compilation or test errors. Run `mvn clean verify` locally before submitting. A PR with failing required checks or missing required review cannot be merged into `main`.

 **JavaDoc and documentation:** Document your classes and keep UML consistent with the implementation. UML/image and JavaDoc requirements are course submission rules; the Story/Task workflow check does **not** automatically verify that the diagram or JavaDoc is present.

## Development Workflow

**User Story → Sub-Tasks → Branch → Code and Tests → Documentation → Pull Request → Review → Merge**

1. Read the assigned [User Story](https://github.com/CSC-Sandbox/3100-requirements/issues) and its acceptance criteria.
2. Work from Tasks linked as **GitHub sub-issues** of that Story.
3. Implement on a feature branch; do not commit directly to `main`.
4. Keep Java classes in separate `.java` files; document classes with JavaDoc and update UML diagrams when required.
5. Open a Pull Request using the repository template. Reference exactly one Story and at least one Task that is its native sub-issue.
6. Ensure automated Maven and Story/Task checks pass; obtain the required review before merging.

## Repository Structure

```text
src/main/java/                   Application source code
src/test/java/                   Java test programs
src/test/resources/sprint2/      Sprint 2 test datasets
docs/story-<number>-<name>/      Story-specific documentation and diagrams
.github/workflows/              Maven and Story/Task validation
pom.xml                         Java 17 Maven configuration
```

Documentation folders use **Story numbers**. Include `README.md` for notes and `uml.png` for UML diagrams when available; existing JPEG images are also retained. Do not commit Astah `.asta` files.

## Build and Submission

From the repository root:

```bash
mvn clean verify
```

Before submitting, confirm that the acceptance criteria are satisfied, tests pass, JavaDoc and required UML are included, and generated files such as `target/`, `.class`, and `.DS_Store` are not committed. Keep each Pull Request focused on its linked Story and Task(s).
