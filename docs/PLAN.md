# Payment Security Engineering: Accelerated Learning Plan

# Payment Security Engineering: Accelerated Learning Plan

**Duration:** ~32 weeks (8 months) at 10–15 hours/week
**Philosophy:** Build first, theory on demand. Every phase ends with working code verified against real test vectors, not notes.

---

## How This Plan Differs From the Original

- **Protocols before math.** You start with ISO 8583 and PIN blocks, and learn 3DES/AES exactly when you need them. The deep crypto theory comes later, when you have context for why it matters.
- **Corrected and updated content.** Includes ISO 9564 Format 4, AES DUKPT (X9.24-3), TR-31 (now ANSI X9.143), TR-34, network tokenization, 3-D Secure, and PCI DSS v4.0.1.
- **Projects in every phase.** Your portfolio at the end is the proof of skill.
- **AI as tutor and reviewer, never as the source of truth.** Test vectors are the source of truth.

---

## Part 1: Rules for Using AI

1. **Verify everything against test vectors or the spec.** AI will confidently invent field numbers, key variants, and derivation steps. If your output doesn't match a published test vector, the explanation was wrong, not the vector.
2. **Write the code yourself first.** Use AI to review, explain failures, and suggest edge cases. Having it write the implementation skips the learning.
3. **Never paste real keys, real PANs, or production logs.** Use test cards (e.g., `4761 7390 0101 0010`-style test PANs) and published test keys only. This is a PCI habit; build it now.
4. **Make AI quiz you.** Explaining back is where retention happens.
5. **Keep a "confusion log."** Every time something doesn't click, write it down. Bring the log to AI sessions.

### Prompt Templates

**Spec explainer**
```
I'm learning payment security. Below is a section of [EMV Book 2 / ISO 9564 / X9.24].
Explain it step by step with a worked numeric example. Then list the 3 things
people most often get wrong when implementing it. Flag anything you're unsure of.

[paste section]
```

**Socratic quiz**
```
Quiz me on [topic] one question at a time. Don't give the answer until I try.
After each answer, tell me what was right, what was missing, and ask a harder
follow-up. Start at intermediate level.
```

**Code review**
```
Review this [PIN block encoder / DUKPT implementation] like a senior payments
engineer. Focus on: correctness against the spec, key/PIN handling in memory,
logging of sensitive data, and edge cases. Don't rewrite it; point out issues.

[paste code]
```

**Debug partner**
```
My [output] doesn't match the test vector. Expected: [X]. Got: [Y].
Here's my code and my intermediate values. Help me find where they diverge.
Don't guess; reason from the intermediate values.
```

**Scenario drill**
```
Give me a realistic production incident in a payment switch involving [PIN
translation / key rotation / declined ARQC]. Play the role of the on-call
system; I'll ask diagnostic questions and you answer only what I ask.
```

---

## Part 2: Setup (Week 0)

- [ ] Java 21 + Maven or Gradle
- [ ] Python 3 (for quick crypto checks with the `cryptography` library)
- [ ] **jPOS** (jpos.org): open-source ISO 8583 framework; read the free *jPOS Programmer's Guide*
- [ ] **SoftHSM2**: free software HSM exposing PKCS#11
- [ ] **EFTlab BP-Tools**: free cryptographic calculator for payments (PIN blocks, DUKPT, KCVs, EMV). Your second verification source.
- [ ] Download the **EMV Books 1–4** (free from emvco.com)
- [ ] Download **PCI DSS v4.0.1** (free from pcisecuritystandards.org)
- [ ] A GitHub repo called `payments-lab` for all projects below

---

## Phase 1: Messages & PINs (Weeks 1–6)

**Goal:** Read and build a raw payment message by hand; build and parse PIN blocks.

### Learn
- ISO 8583 structure: MTI, primary/secondary bitmaps, data element formats (fixed, LLVAR, LLLVAR), encodings (ASCII, BCD, binary)
- Key data elements: DE 2 (PAN), 3 (processing code), 4 (amount), 11 (STAN), 22 (POS entry mode), 35 (Track 2), 39 (response code), 52 (PIN data), 55 (EMV data)
- Message flows: authorization (0100/0110), financial (0200/0210), reversal (0400/0420), network management (0800)
- 3DES and AES basics: block size, ECB vs CBC, why ECB is acceptable for single-block PIN encryption
- ISO 9564 PIN blocks:
  - **Format 0:** PIN field XOR PAN field (rightmost 12 PAN digits excluding check digit)
  - **Format 3:** like Format 0 but random padding nibbles (A–F)
  - **Format 4:** 16-byte AES-based block; the modern standard

### Build
- [ ] **P1: ISO 8583 parser/builder** from scratch (no jPOS). Handle bitmaps, variable-length fields, and a secondary bitmap. Then compare with jPOS's output.
- [ ] **P2: PIN block encoder/decoder** for Formats 0, 3, and 4. Verify every case in BP-Tools.

### Checkpoint
- Hand-decode a hex dump of a 0200 message on paper, including the bitmap
- Explain why Format 0 ties the PIN block to the PAN, and why Format 4 replaced it

---

## Phase 2: Key Management & HSMs (Weeks 7–12)

**Goal:** Understand how keys are created, protected, exchanged, and derived. This is the core of the job.

### Learn
- Key hierarchy: LMK (HSM master key), ZMK (zone master key), ZPK (zone PIN key), TPK/TMK (terminal keys), BDK (base derivation key)
- Key check values (KCVs) and why they exist
- Key components, split knowledge, and dual control
- **TR-31 / ANSI X9.143 key blocks:** header, key usage, mode of use, exportability, MAC binding
- **DUKPT:** TDES version (X9.24-1) and AES version (X9.24-3); KSN structure, IPEK, future keys, counter exhaustion
- **TR-34:** asymmetric remote key loading (concepts)
- Typical HSM operations: generate key, import/export under ZMK, PIN translate (e.g., DUKPT → ZPK), PIN verify, MAC generate/verify
- **PKCS#11:** sessions, object handles, mechanisms; Java's `SunPKCS11` provider

### Build
- [ ] **P3: KCV calculator** for TDES and AES keys
- [ ] **P4: DUKPT implementation** (TDES first, then AES). Must match the X9.24 published test vectors exactly.
- [ ] **P5: TR-31 key block wrap/unwrap** (Version D, AES)
- [ ] **P6: Java + SoftHSM via PKCS#11:** generate an AES key inside the HSM, encrypt/decrypt with it, and prove the key is non-extractable
- [ ] **P7: Mini PIN translation service:** accept a DUKPT-encrypted Format 0 PIN block, translate to Format 4 under a ZPK. Simulate the HSM boundary in its own module.

### Checkpoint
- Draw the full key hierarchy from terminal to issuer from memory
- Explain what happens when a DUKPT counter is exhausted
- Explain why a raw ZPK should never exist outside the HSM in clear

---

## Phase 3: EMV, Tokenization & 3-D Secure (Weeks 13–18)

**Goal:** Understand what happens between card and terminal, and how card-not-present is secured.

### Learn
- BER-TLV encoding and common EMV tags (9F26, 9F27, 9F10, 9F36, 95, 9A, 9C, 5F2A, 82)
- Transaction flow: application selection (PSE/PPSE, AIDs), GPO, read records, offline data authentication, processing restrictions, cardholder verification, terminal risk management, terminal/card action analysis
- **Offline data authentication:** SDA/DDA/CDA using an **RSA** certificate chain (CA → Issuer → ICC)
- **ARQC/ARPC:** session key derivation from the ICC master key, cryptogram generation, issuer verification
- Contactless kernels and why they differ from contact
- **Network tokenization** (EMVCo Payment Tokenisation spec): token requestors, token vaults, domain restrictions, cryptograms; how mobile wallets actually work
- **Merchant/vault tokenization:** random tokens vs. format-preserving encryption, and the tradeoffs
- **3-D Secure 2.x:** frictionless vs. challenge flows, risk-based authentication

### Build
- [ ] **P8: EMV TLV parser** that decodes DE 55 from an ISO 8583 message into named tags
- [ ] **P9: ARQC generator/verifier** using published test keys; verify with BP-Tools
- [ ] **P10: RSA certificate chain recovery** for EMV ODA (recover issuer public key from a test issuer certificate)

### Checkpoint
- Walk through a full contactless tap from PPSE select to issuer response, naming the tags involved
- Explain why a replayed ARQC fails at the issuer

---

## Phase 4: Cryptography in Depth (Weeks 19–24)

**Goal:** Now that you've used the primitives, understand why they work and how they fail.

### Learn
- Modular arithmetic, Euler's totient, RSA key generation, and padding (why raw RSA is insecure)
- Elliptic curves: the Weierstrass form, point addition, ECDSA, and nonce reuse attacks
- AES internals (SPN) and DES internals (Feistel), at an understanding level, not implementation
- Authenticated encryption: AES-GCM, nonce uniqueness, and what happens when it's violated
- HMAC, CMAC, HKDF
- Side-channel and timing attacks; constant-time comparison

### Resources
- *Serious Cryptography* (2nd edition) by Jean-Philippe Aumasson
- Ross Anderson, *Security Engineering*: the banking chapter (real attacks on PIN and HSM systems)

### Build
- [ ] **P11: Toy RSA** with tiny primes, then break it by factoring
- [ ] **P12: Demonstrate AES-GCM nonce reuse** leaking the XOR of plaintexts
- [ ] **P13: Timing-safe MAC verification** in Java, and show the unsafe version's timing leak

### Checkpoint
- Explain why ECDSA nonce reuse reveals the private key
- Explain why the industry moved from TDES to AES in PIN and key management

---

## Phase 5: Systems, Compliance & Capstone (Weeks 25–32)

**Goal:** Build production-grade services and think like an auditor and an attacker.

### Learn
- *Java Concurrency in Practice* (Goetz): JMM, `volatile`, CAS, safe publication
- Low-latency basics (*Optimizing Java*, Evans): allocation pressure, `ByteBuffer`, object pooling. Learn it; apply it only where measurement says you need it.
- Handling secrets in memory: `char[]`/`byte[]` over `String`, zeroing buffers, never logging sensitive fields
- **PCI DSS v4.0.1:** focus on Requirements 3 (stored account data), 4 (transmission), 6 (secure development), 8 (authentication), 10 (logging)
- **PCI PIN Security** requirements (skim; know what they cover)
- **OWASP API Security Top 10:** BOLA, broken authentication, excessive data exposure

### Build: Capstone
- [ ] **P14: Mini payment switch**
  - Accepts ISO 8583 over TCP (can use jPOS now)
  - Translates PINs via your HSM module (SoftHSM-backed)
  - Verifies ARQCs
  - Tokenizes PANs into a vault; nothing downstream sees a clear PAN
  - Masks sensitive fields in logs
  - Includes a load test and a written threat model
- [ ] **P15: PCI self-review** of your own capstone against Requirements 3, 6, and 10. Write up findings as if you were the auditor.

### Checkpoint
- Defend your capstone's design in a mock architecture review (use the AI scenario drill prompt, playing a hostile reviewer)

---

## Weekly Rhythm

| Day | Activity | Time |
|---|---|---|
| Mon | Read spec/book section + AI explainer session | 2h |
| Tue | Build | 2h |
| Wed | Build + verify against test vectors | 2h |
| Thu | AI Socratic quiz on the week's topic | 1h |
| Fri | Code review with AI, fix issues | 1.5h |
| Sat | Longer build session or incident drill | 3h |
| Sun | Write a short summary in your repo's README (teaching = retention) | 1h |

---

## Verification Sources (Your Source of Truth)

- **ANSI X9.24-1 / X9.24-3** published DUKPT test vectors
- **EFTlab BP-Tools** for PIN blocks, KCVs, DUKPT, EMV cryptograms
- **EMV Books 1–4** (emvco.com)
- **jPOS** for ISO 8583 behaviour comparison
- **Python `cryptography`** for quick primitive checks
- **NIST** test vectors for AES, HMAC, and GCM

When AI and a test vector disagree, the test vector wins.

---

## Portfolio Checklist

By the end you should have a public repo with:

- [ ] ISO 8583 parser/builder
- [ ] PIN block library (Formats 0, 3, 4)
- [ ] DUKPT (TDES + AES), matching published vectors
- [ ] TR-31 key block wrap/unwrap
- [ ] PKCS#11 SoftHSM integration in Java
- [ ] EMV TLV parser + ARQC verifier
- [ ] Crypto attack demos (RSA, GCM nonce reuse, timing)
- [ ] Mini payment switch capstone with threat model and PCI self-review

This portfolio is what earns respect in interviews: something that runs and can be interrogated.

