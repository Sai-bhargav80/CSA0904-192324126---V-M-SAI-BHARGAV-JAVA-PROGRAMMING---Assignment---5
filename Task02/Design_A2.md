# Task A2 – Don’t Read What You Don’t Need
## A2.1 Predict the cost
A 50 MiB file contains 52,428,800 bytes. A loop calling `FileInputStream.read()` once per byte makes about 52.4 million Java read calls. Each call has method/loop overhead; implementations may buffer internally, but byte-at-a-time calls still incur substantial Java-level overhead.

## A2.2 Buffered redesign
Use `BufferedInputStream` with a 64 KiB buffer and count `\n` bytes. Approximately 800 buffer fills are needed for 50 MiB instead of 52.4 million single-byte calls. Time remains O(N); auxiliary memory is O(B), where B is buffer size.
```text
ALGORITHM CountLines(path)
  count ← 0
  OPEN BufferedInputStream(path, 65536)
  WHILE byte ← READ next byte
    IF byte = line-feed THEN count ← count + 1
  CLOSE stream
  RETURN count
```

## A2.3 When buffering helps less
If the source is already an in-memory byte array, or the underlying API returns large blocks efficiently, another buffer may add copying without reducing I/O calls.

## Alternative: Files.readAllLines()
It is concise and processes all lines in memory. Time O(N), memory O(N) plus per-line object overhead; for 50 MiB heap use can substantially exceed 50 MiB. Buffered streaming is O(B) memory and preferred for large files. `readAllLines` suits small bounded files where all lines are needed together.
