---
description: Production incident drill — you play the system, I diagnose
argument-hint: [scenario area, e.g. "PIN translation", "key rotation", "declined ARQC"]
---
Run an incident drill on: $ARGUMENTS (default: something from my current or completed phases).

Invent a realistic production incident in a payment switch. Give me only the initial alert /
symptom (as an on-call page would). Then play the system: answer ONLY what I ask — log lines
(masked properly), HSM responses, response codes, KCVs, config — nothing volunteered. Keep the
root cause internally consistent. When I declare a root cause, tell me if I'm right, then debrief:
what the fastest diagnostic path was, and what monitoring would have caught it earlier.
