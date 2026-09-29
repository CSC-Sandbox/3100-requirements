
Shared repository for **CSC 3100: Software Engineering**.

We use GitHub as our development hub to connect **requirements, implementation, testing, documentation, and code review**.

## Development Workflow

**User Story → Tasks → Code → Test → Pull Request → Review → Merge**

1. **User Story** — Start with your assigned User Story and review its Acceptance Criteria.
2. **Tasks** — Break the Story into Tasks before implementation.
3. **Branch** — Develop on your assigned/feature branch. Do not work directly on `main`.
4. **Implementation** — Follow the Maven project structure under `src/main/java`.
5. **Testing** — Run the required tests under `src/test/java`.
6. **Documentation** — Include JavaDoc and update the UML diagram when required.
7. **Pull Request** — Submit your work through a PR and address review comments.

## Project Structure

```text
src/main/java/    Application source code
src/test/java/    Tests
docs/             UML and supporting documentation
pom.xml           Maven configuration and dependencies
```

The project uses **Java 17 and Maven**.

```bash
mvn clean test
```

## Before Submitting

Make sure:

- Acceptance Criteria are satisfied.
- Required tests pass.
- JavaDoc is included.
- UML is updated and exported as an image when required.
- `target/`, `.class`, `.DS_Store`, and other generated files are not committed.
- Your Pull Request contains only the files needed for your work.
