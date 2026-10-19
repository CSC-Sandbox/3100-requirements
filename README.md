<img width="2172" height="724" alt="6D10B86A-3228-4A00-BA79-C505B6495A8F" src="https://github.com/user-attachments/assets/3693485d-618f-4564-83fc-23b28116f3f5" />

# CSC 3100 — Introduction to Software Engineering

Shared Java 17/Maven repository for course work connecting **requirements, design, implementation, testing, documentation, and code review**. GitHub Issues define User Stories and Tasks; Pull Requests capture implementation and review history.

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

> [!WARNING]
> **Important Notes — Required for Every Submission**
>
> - **Mandatory UML:** Export the UML class diagram as **PNG (`.png`) or JPEG (`.jpg` / `.jpeg`)** in `docs/story-<number>-<name>/`. Do not submit an Astah `.asta` file instead of the image.
> - **Mandatory Story-linked PR:** Submit changes through a Pull Request, not directly to `main`. In its description, include exactly **one** Story reference under `## User Story` and at least **one** Task reference under `## Tasks`. Every Task must be a **native GitHub sub-issue** of the referenced Story.
> - **Automated validation:** The **Story and Task references** check fails for missing sections, missing or multiple Story references, nonexistent issues, or Tasks not linked as sub-issues. Read the GitHub Actions error, fix the PR description or issue relationship, and rerun the check.
> - **Build validation:** Compilation or test failures cause Maven checks to fail. Required checks and review must pass before merging.
> - **JavaDoc:** Document classes and keep UML consistent with the implementation. The Story/Task check does **not** automatically verify UML or JavaDoc.

## Build and Test

From the repository root, run:

```bash
mvn clean verify
```

This compiles the project and runs its configured tests and verification steps. Resolve any failures before submitting a Pull Request. Do not commit generated files such as `target/`, `.class`, or `.DS_Store`.

## Assignments

The tables identify GitHub accounts associated with work through Story assignments, Tasks, or related Pull Requests. They are not grades or an exhaustive measure of individual contributions.

### A01 — Requirements and Initial Implementation (Sprint 1)

Students worked from assigned User Stories, decomposed requirements into Tasks, implemented Java functionality, documented designs with UML and JavaDoc, and submitted changes for review through Pull Requests. Sprint 1 also introduced the Maven project structure.

<table width="100%">
<thead><tr><th>User Story</th><th>Student GitHub account(s)</th></tr></thead>
<tbody>
<tr><td><a href="https://github.com/CSC-Sandbox/3100-requirements/issues/6">#6 — Enter Message</a></td><td><a href="https://github.com/jnne-lbg">@jnne-lbg</a> (Tasks #59–#62)</td></tr>
<tr><td><a href="https://github.com/CSC-Sandbox/3100-requirements/issues/9">#9 — Detect Malformed Messages</a></td><td><a href="https://github.com/Omarc2007">@Omarc2007</a></td></tr>
<tr><td><a href="https://github.com/CSC-Sandbox/3100-requirements/issues/11">#11 — Display Real-Time Robot Pose</a></td><td><a href="https://github.com/srinityad">@srinityad</a></td></tr>
<tr><td><a href="https://github.com/CSC-Sandbox/3100-requirements/issues/13">#13 — Gather Robot Pose</a></td><td><a href="https://github.com/sipactivism">@sipactivism</a></td></tr>
<tr><td><a href="https://github.com/CSC-Sandbox/3100-requirements/issues/23">#23 — Display Message History</a></td><td><a href="https://github.com/matthewdaviii">@matthewdaviii</a> (related Tasks #80–#83)</td></tr>
<tr><td><a href="https://github.com/CSC-Sandbox/3100-requirements/issues/24">#24 — Encrypt Messages</a></td><td><a href="https://github.com/anthony-Soto">@anthony-Soto</a></td></tr>
<tr><td><a href="https://github.com/CSC-Sandbox/3100-requirements/issues/26">#26 — Storing Messages</a></td><td><a href="https://github.com/Landon-McCaslin">@Landon-McCaslin</a> (related <a href="https://github.com/CSC-Sandbox/3100-requirements/pull/72">PR #72</a> for Story #8)</td></tr>
<tr><td><a href="https://github.com/CSC-Sandbox/3100-requirements/issues/34">#34 — Gather Eye-Tracking Data from Device</a></td><td><a href="https://github.com/mars-rams">@mars-rams</a></td></tr>
<tr><td><a href="https://github.com/CSC-Sandbox/3100-requirements/issues/35">#35 — Display LiDAR Map</a></td><td><a href="https://github.com/MKHLimkilde">@MKHLimkilde</a></td></tr>
<tr><td><a href="https://github.com/CSC-Sandbox/3100-requirements/issues/36">#36 — Monitor Data Availability</a></td><td><a href="https://github.com/tphan56">@tphan56</a></td></tr>
<tr><td><a href="https://github.com/CSC-Sandbox/3100-requirements/issues/37">#37 — Record User Session with Stored Messages</a></td><td>See issue and associated PRs</td></tr>
<tr><td><a href="https://github.com/CSC-Sandbox/3100-requirements/issues/38">#38 — Display Real-Time Gaze Position</a></td><td><a href="https://github.com/dkguru27">@dkguru27</a></td></tr>
<tr><td><a href="https://github.com/CSC-Sandbox/3100-requirements/issues/40">#40 — Display Real-Time Affective State</a></td><td><a href="https://github.com/an-prata">@an-prata</a></td></tr>
<tr><td><a href="https://github.com/CSC-Sandbox/3100-requirements/issues/42">#42 — Display Recent Data Activity</a></td><td><a href="https://github.com/sp4msvwnz">@sp4msvwnz</a></td></tr>
</tbody>
</table>


### A02 — Design, Libraries, and Integration (Sprint 2)

Students extended existing functionality with external Java libraries, Maven dependencies, integration, tests, and updated UML documentation. The table reflects **GitHub issue assignees**, not an exhaustive list of all contributors; see the linked issues and Pull Requests for contribution history.

<table width="100%">
<thead><tr><th>User Story</th><th>Student GitHub account(s)</th></tr></thead>
<tbody>
<tr><td><a href="https://github.com/CSC-Sandbox/3100-requirements/issues/135">#135 — Visualize LiDAR Data in 3D</a></td><td><a href="https://github.com/sp4msvwnz">@sp4msvwnz</a>, <a href="https://github.com/MKHLimkilde">@MKHLimkilde</a></td></tr>
<tr><td><a href="https://github.com/CSC-Sandbox/3100-requirements/issues/141">#141 — Classify System Activity</a></td><td><a href="https://github.com/an-prata">@an-prata</a>, <a href="https://github.com/tphan56">@tphan56</a></td></tr>
<tr><td><a href="https://github.com/CSC-Sandbox/3100-requirements/issues/147">#147 — Manage Stored Messages as CSV</a></td><td><a href="https://github.com/ReeledWarrior14">@ReeledWarrior14</a>, <a href="https://github.com/Landon-McCaslin">@Landon-McCaslin</a></td></tr>
<tr><td><a href="https://github.com/CSC-Sandbox/3100-requirements/issues/154">#154 — Enhance Message Security Processing</a></td><td><a href="https://github.com/Omarc2007">@Omarc2007</a>, <a href="https://github.com/anthony-Soto">@anthony-Soto</a></td></tr>
<tr><td><a href="https://github.com/CSC-Sandbox/3100-requirements/issues/160">#160 — Analyze Gaze Data</a></td><td><a href="https://github.com/dkguru27">@dkguru27</a>, <a href="https://github.com/mars-rams">@mars-rams</a></td></tr>
<tr><td><a href="https://github.com/CSC-Sandbox/3100-requirements/issues/166">#166 — Improve Message Input Processing</a></td><td><a href="https://github.com/matthewdaviii">@matthewdaviii</a></td></tr>
<tr><td><a href="https://github.com/CSC-Sandbox/3100-requirements/issues/172">#172 — Improve Robot Data Diagnostics</a></td><td><a href="https://github.com/srinityad">@srinityad</a>, <a href="https://github.com/sipactivism">@sipactivism</a></td></tr>
</tbody>
</table>


