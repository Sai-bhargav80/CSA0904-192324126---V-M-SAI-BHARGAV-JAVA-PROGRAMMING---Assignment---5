# CSA09 – Programming in Java – Assignment 5 (CO5)
| | |
|---|---|
| **Name** | V M Sai Bhargav |
| **Register number** | `<ENTER YOUR REGISTER NUMBER>` |
| **Course** | CSA09 – Programming in Java |
| **Assignment** | Assignment 5 – Data Persistence Engineering: Files and Databases |
| **Java version** | OpenJDK 17+ recommended |
| **Report** | `Report/Assignment5_Report.docx` and `.pdf` |

## Structure
- `Task01`–`Task05`: Part A design answers A1–A5 plus runnable Java demonstrations (text/binary files, buffered I/O, generic JDBC DAO, serialization and transactions).
- `Task06`: B1 CSV pass store and buffered/unbuffered line-count experiment.
- `Task07`: B2 SQL injection test, adversarial inputs and damaged-file samples.
- `Task08`: B3 JDBC batch loader, benchmark and measurement sheet.
- `Task09`: B4 migration and DatabaseMetaData inspection.
- `database/schema.sql`: local MySQL schema.
- `Report`: consolidated report.
- `scripts`: compile/run helpers.

## Compile and run
From the repository root:
```bash
bash scripts/run_local.sh
```
This compiles and runs the file-based programs. For individual tasks:
```bash
cd Task01 && javac PassFileFormatDemo.java && java PassFileFormatDemo
cd ../Task02 && javac BufferedReadDemo.java && java BufferedReadDemo
cd ../Task04 && javac HostelSerializationDemo.java && java HostelSerializationDemo
cd ../Task06 && javac PassStore.java LineCounter.java PassGrowthBenchmark.java && java PassStore && java LineCounter && java PassGrowthBenchmark
cd ../Task03 && javac StudentDaoDemo.java   # runtime needs MySQL + Connector/J
cd ../Task05 && javac WalletTransactionDemo.java   # runtime needs MySQL + Connector/J
cd ../Task07 && javac UnsafeVsSafeLogin.java   # runtime needs MySQL + Connector/J
cd ../Task08 && javac PassLoader.java BatchBenchmark.java JdbcComparison.java TransactionFailureDemo.java
cd ../Task09 && javac -cp ../Task08 MigrateAndInspect.java
```
JDBC tasks require MySQL 8+, MySQL Connector/J on the classpath, and environment variables `DB_URL`, `DB_USER`, `DB_PASSWORD`. Example:
```bash
export DB_URL='jdbc:mysql://localhost:3306/assignment5'
export DB_USER='root'
export DB_PASSWORD='your-password'
mysql -u root -p < database/schema.sql
```
Then add the Connector/J JAR to `-cp` when compiling/running JDBC classes. Do not commit database passwords.

## Task results
1. **A1** – CSV and DataOutputStream layouts, three file operations, damaged-data recovery and RandomAccessFile comparison.
2. **A2** – Predicts 52,428,800 byte reads for 50 MiB and redesigns with a 64 KiB buffer.
3. **A3** – Generic DAO interface and Student-to-table mapping; compares SQL placement.
4. **A4** – Marks live Connection transient, explains object graph and serialVersionUID compatibility.
5. **A5** – JDBC commit/rollback transfer and PreparedStatement injection prevention.
6. **B1** – PassStore adds/finds/lists and skips malformed lines; LineCounter records local measurements for 1, 10 and 50 MiB.
7. **B2** – Unsafe and parameterized login implementations plus adversarial inputs.
8. **B3** – Transactional PreparedStatement batch loader and benchmark harness for 1k/10k/100k.
9. **B4** – CSV-to-DB migration with rejected-row reporting and DatabaseMetaData inspection.

## Evidence and limitations
`output.txt` files are console transcripts from the local Java execution. `output.png` files are generated renderings of those transcripts, not photographs of a physical terminal. The MySQL experiments are provided as executable source and SQL, but require a configured local MySQL server and Connector/J. Their timings and actual database output are intentionally left for execution on your machine; fill `Task08/measurements.csv` and add your screenshots after running them.

## Before submission
1. Replace `<ENTER YOUR REGISTER NUMBER>` and rename the repository to `CSA09-<RegisterNumber>-Assignment5`.
2. Execute every program on your own machine and capture genuine output screenshots.
3. Run the MySQL tasks and complete measurement evidence.
4. Commit progressively (at least one commit per task) and share the GitHub repository with faculty. This ZIP does not create or publish a GitHub repository.

## Part A implementation source files
- A1: `Task01/PassFileFormatDemo.java`
- A2: `Task02/BufferedReadDemo.java`
- A3: `Task03/StudentDaoDemo.java`
- A4: `Task04/HostelSerializationDemo.java`
- A5: `Task05/WalletTransactionDemo.java`

Each task folder contains a short README with its compile/run command. JDBC tasks need MySQL Connector/J and the schema in `database/schema.sql`.
