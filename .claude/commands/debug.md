---
description: Debug a mismatch against a test vector by reasoning from intermediate values
argument-hint: [expected vs got, or a description]
---
My output doesn't match. $ARGUMENTS

Don't guess. Ask me for (or read from my code/logs) the intermediate values at each step of
the algorithm. Walk through the algorithm step by step and find the first step where my
intermediate value diverges from what the spec says it should be. Then ask me a question that
leads me to the bug rather than telling me the fix outright. Only state the fix if I've tried twice.
