#!/usr/bin/env python3
"""Check objective A03 PR requirements; semantic design review remains manual."""
import base64
import json
import os
import re
import sys
import urllib.request

ASSIGNMENTS = {
    232: ("MKHLimkilde", "docs/story-35-display-lidar-map/uml.png", "src/main/java/edu/calpoly/visualization/Lidar3DPanel.java"),
    233: ("sp4msvwnz", "docs/story-42-display-recent-data-activity/uml.png", "src/main/java/edu/calpoly/monitor/ActivityDisplay.java"),
    234: ("an-prata", "docs/story-141-classify-system-activity/activity-classifier.png", "src/main/java/edu/calpoly/monitor/ActivityClassifier.java"),
    235: ("tphan56", "docs/story-36-monitor-data-availability/uml.png", "src/main/java/edu/calpoly/MonitorAvailability.java"),
    236: ("ReeledWarrior14", "docs/story-147-manage-stored-messages/uml.png", "src/main/java/edu/calpoly/storage/RetrieveMessages.java"),
    237: ("Landon-McCaslin", "docs/story-26-storing-messages/uml.png", "src/main/java/edu/calpoly/messages/StoreMessages.java"),
    238: ("Omarc2007", "docs/story-9-message-validator/MessageValidatorUML.jpeg", "src/main/java/edu/calpoly/message-validator/MessageValidator.java"),
    239: ("anthony-Soto", "docs/story-24-encrypt-messages/uml.png", "src/main/java/edu/calpoly/messages/EncryptMessage.java"),
    240: ("dkguru27", "docs/story-34-gather-eye/uml.png", "src/main/java/edu/calpoly/eye/GatherEyeGUI.java"),
    241: ("mars-rams", "docs/story-160-analyze-gaze-data/uml.png", "src/main/java/edu/calpoly/eye/GazeStatistics.java"),
    242: ("matthewdaviii", "docs/story-166-improve-message-input-processing/uml-message-input.png", "src/main/java/edu/calpoly/messages/MessageInput.java"),
    243: ("srinityad", "docs/story-172-improve-robot-data-diagnostics/uml.png", "src/main/java/edu/calpoly/visualization/RobotDiagnostics.java"),
    244: ("sipactivism", "docs/story-13-gather-robot/uml.png", "src/main/java/edu/calpoly/visualization/GatherRobot.java"),
}


def api(path):
    request = urllib.request.Request(
        "https://api.github.com/" + path,
        headers={
            "Authorization": "Bearer " + os.environ["GH_TOKEN"],
            "Accept": "application/vnd.github+json",
            "User-Agent": "csc3100-a03-validator",
            "X-GitHub-Api-Version": "2022-11-28",
        },
    )
    with urllib.request.urlopen(request, timeout=25) as response:
        return json.load(response)


def section(body, title):
    match = re.search(
        r"^##\s+" + re.escape(title) + r"\s*$([\s\S]*?)(?=^##\s+|\Z)",
        body, re.I | re.M,
    )
    return match.group(1) if match else ""



def check_class_javadoc(source_text, filename):
    """Check class-level tags for each Java type declared in a changed source file."""
    problems = []
    declarations = list(re.finditer(
        r"(?m)^\s*(?:(?:public|protected|private|abstract|final|sealed|non-sealed|static|strictfp)\s+)*"
        r"(?:class|interface|enum|record|@interface)\s+([A-Za-z_$][\w$]*)\b",
        source_text,
    ))
    for match in declarations:
        name = match.group(1)
        before = source_text[:match.start()]
        doc = re.search(
            r"/\*\*([\s\S]*?)\*/\s*(?:@(?:[\w.]+)(?:\([^)]*\))?\s*)*$",
            before,
        )
        if not doc:
            problems.append(f"{filename}: {name} needs class-level JavaDoc immediately before its declaration.")
            continue
        comment = doc.group(1)
        for tag in ("author", "version"):
            if not re.search(r"(?m)^\s*\*\s*@" + tag + r"\s+\S+", comment):
                problems.append(f"{filename}: {name} needs a nonempty @{tag} tag in its class-level JavaDoc.")
    return problems


def pr_file_content(repo, head_sha, filename):
    """Read the proposed PR-head file, not the main-branch version."""
    from urllib.parse import quote
    data = api(f"repos/{repo}/contents/{quote(filename, safe='/')}?ref={head_sha}")
    if data.get("encoding") != "base64":
        raise ValueError(f"Unexpected content encoding for {filename}")
    return base64.b64decode(data["content"]).decode("utf-8")


def main():
    repo = os.environ["REPO"]
    pr = api(f"repos/{repo}/pulls/{os.environ['PR_NUMBER']}")
    if any(label["name"] == "instructor-maintenance" for label in pr["labels"]):
        print("Instructor maintenance PR: A03 student validation exempt.")
        return 0

    story_refs = re.findall(r"(?<![\w/])#(\d+)\b", section(pr.get("body") or "", "User Story"))
    a03_refs = [int(ref) for ref in story_refs if int(ref) in ASSIGNMENTS]
    if not a03_refs:
        print("Not an A03 Story PR: A03-specific checks do not apply.")
        return 0

    errors = []
    if len(story_refs) != 1:
        errors.append("A03 PR must reference exactly one Story in ## User Story.")
    story = a03_refs[0]
    expected_user, uml, source = ASSIGNMENTS[story]
    if pr["user"]["login"].lower() != expected_user.lower():
        errors.append(f"Story #{story} is assigned to @{expected_user}; PR author is @{pr['user']['login']}. Ask instructor if working from a shared account.")

    story_issue = api(f"repos/{repo}/issues/{story}")
    if not any(label["name"] == "Sprint 3" for label in story_issue["labels"]):
        errors.append(f"Story #{story} must have the Sprint 3 label.")
    if expected_user.lower() not in [u["login"].lower() for u in story_issue.get("assignees", [])]:
        errors.append(f"Story #{story} must be assigned to @{expected_user}.")

    files = []
    page = 1
    while True:
        batch = api(f"repos/{repo}/pulls/{pr['number']}/files?per_page=100&page={page}")
        files.extend(batch)
        if len(batch) < 100:
            break
        page += 1
    changed = {f["filename"]: f for f in files}
    if uml not in changed or changed[uml]["status"] == "removed":
        errors.append(f"Update the existing UML image IN PLACE: {uml}. Do not submit a separate diagram.")
    extra_images = [p for p, f in changed.items()
                    if p.startswith("docs/") and re.search(r"\.(png|jpe?g)$", p, re.I)
                    and p != uml and f["status"] == "added"]
    if extra_images:
        errors.append("Do not add new UML images; update the assigned existing image: " + ", ".join(extra_images))
    if source not in changed or changed[source]["status"] == "removed":
        errors.append(f"Refactor the assigned existing Java component: {source}.")
    tests = [p for p in changed if p.startswith("src/test/java/") and p.endswith("Test.java")]
    if not tests:
        errors.append("Add or update a JUnit test under src/test/java/ (filename ending Test.java) demonstrating behavior.")
    forbidden = [p for p in changed if p.startswith("src/main/java/edu/calpoly/provided/")
                 or p.startswith("src/test/java/edu/calpoly/provided/")]
    if forbidden:
        errors.append("Do not modify instructor-provided code or fixtures: " + ", ".join(forbidden))

    # Validate actual Java source at the PR head, not just changed diff lines.
    # This also checks unchanged class headers when students modify methods.
    java_files = [f for f in files if f["filename"].startswith("src/main/java/")
                  and f["filename"].endswith(".java") and f["status"] != "removed"]
    for file in java_files:
        filename = file["filename"]
        try:
            source_text = pr_file_content(repo, pr["head"]["sha"], filename)
            errors.extend(check_class_javadoc(source_text, filename))
        except (ValueError, UnicodeDecodeError, KeyError) as exc:
            errors.append(f"Could not validate JavaDoc in {filename}: {exc}")


    if errors:
        for error in errors:
            print("::error::" + error)
        return 1
    print(f"A03 Story #{story}: author, assigned source, existing UML, JUnit test, and class-level JavaDoc tags verified.")
    print("Behavior, UML accuracy, and JavaDoc quality still require review; Maven Build verifies execution.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
