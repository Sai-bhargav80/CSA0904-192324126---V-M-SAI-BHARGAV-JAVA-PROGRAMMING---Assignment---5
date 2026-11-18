# Task A1 – Design a File Format
## A1.1 Record layouts
**Delimited text (CSV):** `passId,regNo,name,routeNo,boardingStop,validUntil,amountPaid`
- UTF-8, one record per line; CSV quoting handles commas and quotes. A field containing a comma or quote is enclosed in double quotes; an internal quote is doubled. Example: `P1001,192311001,"Arun, K",12,"Central ""A""",2027-03-31,4500.00`.
- Validate exactly seven decoded fields and required types.

**Binary DataOutputStream layout:** write fields in this order: `writeUTF(passId)`, `writeUTF(regNo)`, `writeUTF(name)`, `writeInt(routeNo)`, `writeUTF(boardingStop)`, `writeUTF(validUntil ISO date)`, `writeDouble(amountPaid)`. Strings are length-prefixed modified UTF-8; int uses 4 bytes and double 8 bytes. This is variable length, not fixed-size.

## A1.2 Operations – SIMATS-style pseudocode
```text
ALGORITHM AddPass(pass)
INPUT: one validated Pass record
OUTPUT: record appended or error reported
BEGIN
  OPEN BufferedWriter using Files.newBufferedWriter(path, UTF_8, CREATE, APPEND)
  ENCODE pass as one correctly escaped CSV record
  WRITE encoded record and line separator
  CLOSE writer using try-with-resources
END
```
```text
ALGORITHM FindPass(passId)
INPUT: pass ID
OUTPUT: matching record or NOT_FOUND
BEGIN
  IF file does not exist THEN RETURN NOT_FOUND
  OPEN BufferedReader using Files.newBufferedReader(path, UTF_8)
  WHILE a line can be read
    PARSE line as CSV
    IF field count is not 7 OR a required field is invalid THEN
      RECORD damaged line and CONTINUE
    ENDIF
    IF parsed pass ID equals passId THEN RETURN parsed record
  ENDWHILE
  RETURN NOT_FOUND
  CLOSE reader
END
```
```text
ALGORITHM ListByRoute(route)
INPUT: route number
OUTPUT: all valid passes on route
BEGIN
  OPEN BufferedReader using Files.newBufferedReader(path, UTF_8)
  WHILE a line can be read
    PARSE and VALIDATE line
    IF valid AND route number equals route THEN DISPLAY record
    ELSE IF invalid THEN LOG line number as damaged
    ENDIF
  ENDWHILE
  CLOSE reader
END
```
`BufferedReader`/`BufferedWriter` reduce small read/write calls and support line processing. `FileReader`/`FileWriter` are character-stream alternatives but use the platform default charset unless configured. `FileInputStream`/`FileOutputStream` are byte streams suited to binary data.

## A1.3 Damaged data and recovery
A damaged line is isolated by parsing and field/type validation; log its line number and skip it rather than terminating the scan. A missing file is treated as an empty store for add and as no records for find/list. To reduce incomplete-write risk, write a complete new file to a temporary file, flush/close it, then atomically replace the old file where supported; retain a backup and checksum or record count.

## Alternative: RandomAccessFile
With fixed-size records, encode each field into fixed-width bytes plus fixed-width numeric/date fields. Record i can be sought in O(1) when its index is known. Finding by ID still needs O(n) unless an ID-to-offset index is maintained. Padding wastes `field capacity − actual length` bytes per record; long values may be rejected or truncated. Sequential text search is O(n) and uses O(1) extra memory.
