# Progress

**Start date:** 2026/09/30  <!-- fill this in; used to check schedule -->
**Current phase:** 0 (Setup)
**Current week:** 0

## Phase 0: Setup (Week 0)
- [x] Java 21 + Maven or Gradle (2026-09-30: staying on JDK 25.0.2 LTS by choice + Maven 3.9.16)
- [x] Python 3 + `cryptography` (2026-09-30: Python 3.11.9, cryptography 50.0.1)
- [x] jPOS + read jPOS Programmer's Guide intro (2026-09-30: jPOS 3.0.0 resolves via Maven in projects/lab; guide intro read)
- [x] SoftHSM2 installed, token initialised (2026-09-30: SoftHSM2 2.5.0 via choco portable; config C:\softhsm-lab\softhsm2.conf; token "lab-token")
- [ ] EFTlab BP-Tools installed (2026-09-30: no longer freely downloadable; contact EFTlab via eftlab.com.au/contact-us. Need an independent verifier before P2)
- [ ] EMV Books 1–4 downloaded
- [ ] PCI DSS v4.0.1 downloaded
- [ ] Repo pushed to GitHub

## Phase 1: Messages & PINs (Weeks 1–6)
Learn:
- [ ] ISO 8583 structure (MTI, bitmaps, fixed/LLVAR/LLLVAR, encodings)
- [ ] Key data elements (2, 3, 4, 11, 22, 35, 39, 52, 55)
- [ ] Message flows (0100/0110, 0200/0210, 0400/0420, 0800)
- [ ] 3DES and AES basics, ECB vs CBC
- [ ] ISO 9564 Formats 0, 3, 4
Build:
- [ ] P1: ISO 8583 parser/builder (then compare with jPOS)
- [ ] P2: PIN block encoder/decoder, Formats 0/3/4, verified in BP-Tools
Checkpoint:
- [ ] Hand-decode a 0200 hex dump incl. bitmap
- [ ] Explain Format 0 PAN binding and why Format 4 replaced it

## Phase 2: Key Management & HSMs (Weeks 7–12)
Learn:
- [ ] Key hierarchy (LMK, ZMK, ZPK, TPK/TMK, BDK)
- [ ] KCVs
- [ ] Components, split knowledge, dual control
- [ ] TR-31 / X9.143 key blocks
- [ ] DUKPT TDES (X9.24-1) and AES (X9.24-3)
- [ ] TR-34 concepts
- [ ] HSM operations
- [ ] PKCS#11 + SunPKCS11
Build:
- [ ] P3: KCV calculator
- [ ] P4: DUKPT TDES, matching X9.24-1 vectors
- [ ] P4b: DUKPT AES, matching X9.24-3 vectors
- [ ] P5: TR-31 wrap/unwrap (Version D, AES)
- [ ] P6: Java + SoftHSM via PKCS#11, non-extractable key proven
- [ ] P7: Mini PIN translation service
Checkpoint:
- [ ] Draw key hierarchy terminal → issuer from memory
- [ ] Explain DUKPT counter exhaustion
- [ ] Explain why a clear ZPK must never leave the HSM

## Phase 3: EMV, Tokenization & 3-D Secure (Weeks 13–18)
Learn:
- [ ] BER-TLV + common tags
- [ ] EMV transaction flow
- [ ] ODA: SDA/DDA/CDA, RSA cert chain
- [ ] ARQC/ARPC + session keys
- [ ] Contactless kernels
- [ ] Network tokenization
- [ ] Merchant/vault tokenization vs FPE
- [ ] 3-D Secure 2.x
Build:
- [ ] P8: EMV TLV parser for DE 55
- [ ] P9: ARQC generator/verifier, verified in BP-Tools
- [ ] P10: RSA issuer public key recovery
Checkpoint:
- [ ] Walk a contactless tap PPSE → issuer response, naming tags
- [ ] Explain why a replayed ARQC fails

## Phase 4: Cryptography in Depth (Weeks 19–24)
Learn:
- [ ] Modular arithmetic, RSA, padding
- [ ] Elliptic curves, ECDSA, nonce reuse
- [ ] AES (SPN) and DES (Feistel) internals
- [ ] AES-GCM and nonce uniqueness
- [ ] HMAC, CMAC, HKDF
- [ ] Side-channels, constant-time comparison
Build:
- [ ] P11: Toy RSA + break by factoring
- [ ] P12: AES-GCM nonce reuse demo
- [ ] P13: Timing-safe vs unsafe MAC verification
Checkpoint:
- [ ] Explain ECDSA nonce reuse → private key
- [ ] Explain TDES → AES migration

## Phase 5: Systems, Compliance & Capstone (Weeks 25–32)
Learn:
- [ ] JMM, volatile, CAS, safe publication
- [ ] Low-latency basics
- [ ] Secrets in memory
- [ ] PCI DSS v4.0.1 Req 3, 4, 6, 8, 10
- [ ] PCI PIN Security (skim)
- [ ] OWASP API Security Top 10
Build:
- [ ] P14: Mini payment switch (ISO 8583/TCP, PIN translate, ARQC verify, tokenization, masked logs, load test, threat model)
- [ ] P15: PCI self-review of capstone
Checkpoint:
- [ ] Defend capstone in mock hostile architecture review
