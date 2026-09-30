---
description: Have the payments-reviewer subagent review my code
argument-hint: [path, defaults to recently changed files in projects/]
---
Use the payments-reviewer subagent to review: $ARGUMENTS
(If empty, review files changed since the last commit under `projects/` — check `git status`/`git diff`.)
Relay its findings to me. Don't fix anything yourself. After I've addressed the Must-fix items,
offer to run the review again.
