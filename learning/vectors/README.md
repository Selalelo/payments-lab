# Vectors

One file per algorithm, copied from TEMPLATE.md. The vector-checker subagent only trusts files
here that name their source. Suggested files: `pinblock-f0.md`, `pinblock-f3.md`, `pinblock-f4.md`,
`kcv.md`, `dukpt-tdes.md`, `dukpt-aes.md`, `tr31.md`, `arqc.md`, `aes-nist.md`, `gcm-nist.md`.

Format 3 and Format 4 blocks contain random padding, so vectors for those check *decode* of a
published block, or encode with a fixed/injected random source.
