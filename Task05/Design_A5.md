# Task A5 – Design for Correctness and Safety
## A5.1 Atomic wallet transfer
```text
ALGORITHM Transfer(fromId, toId, amount)
BEGIN
  GET JDBC connection
  SET autoCommit FALSE
  TRY
    debit ← UPDATE wallet SET balance = balance - amount
            WHERE wallet_id = fromId AND balance >= amount
    IF debit != 1 THEN RAISE insufficient funds
    credit ← UPDATE wallet SET balance = balance + amount
             WHERE wallet_id = toId
    IF credit != 1 THEN RAISE missing destination
    COMMIT
  CATCH any error
    ROLLBACK
    REPORT failure
  FINALLY restore previous autoCommit and close connection
END
```
Both updates commit together or neither does. Validate amount > 0 and use DECIMAL for currency.

## A5.2 SQL injection
Concatenating input lets a crafted ID such as `' OR '1'='1` change the WHERE condition; a comment suffix may neutralize the password predicate. Use `SELECT ... WHERE id = ? AND pwd_hash = ?` with `PreparedStatement`, bind values, and store password hashes. Parameterization treats input as data, not SQL syntax.

## Alternative: two auto-commit statements
If debit commits and the program fails before credit, money disappears from the application-level transfer. A transaction has coordination/logging overhead but protects the invariant. Two independent statements may be slightly cheaper in a trivial case, but correctness risk is unacceptable.
