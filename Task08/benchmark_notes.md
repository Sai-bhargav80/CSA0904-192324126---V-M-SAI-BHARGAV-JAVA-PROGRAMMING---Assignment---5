# B3 – JDBC experiment record
`BatchBenchmark.java` performs transactional prepared-statement batch inserts at 1,000, 10,000 and 100,000 rows. The code records elapsed milliseconds on the machine where it is run. This ZIP does not contain a MySQL server or Connector/J driver, so no database timings are fabricated. Run after setting `DB_URL`, `DB_USER`, `DB_PASSWORD`, and adding MySQL Connector/J to the classpath.

For the full required comparison, repeat with (a) Statement, (b) PreparedStatement one row at a time, and (c) this batch loader. Record the actual console timings in `measurements.csv`. Run once with auto-commit on and once in a single transaction; force a constraint violation midway and query row count. Auto-commit can retain earlier inserts; transaction rollback should leave zero rows from that attempted batch.

Additional executable comparisons:
- `JdbcComparison.java` measures Statement, PreparedStatement row-by-row and PreparedStatement batch at all three sizes.
- `TransactionFailureDemo.java` inserts rows in one transaction, deliberately violates the non-negative amount constraint, rolls back and prints the remaining row count.
Run these with the Connector/J JAR on the classpath after executing `database/schema.sql`.
