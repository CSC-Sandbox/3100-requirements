<img width="2172" height="724" alt="6D10B86A-3228-4A00-BA79-C505B6495A8F" src="https://github.com/user-attachments/assets/3693485d-618f-4564-83fc-23b28116f3f5" />


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

See [Assignment History](docs/ASSIGNMENTS.md) for A01 and A02 accepted Stories and student contributions.
