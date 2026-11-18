# Task A3 – Discover the Data-Access Design Pattern
## A3.1 Common pattern
Repeated steps: obtain JDBC connection, prepare SQL, bind parameters, execute, iterate ResultSet, map columns to objects, handle SQLException and close resources. Differences: table/column names, entity type, SQL operations and mapping rules.

## A3.2 Generic DAO
```java
interface Dao<T, K> {
  int insert(T value) throws SQLException;
  Optional<T> findById(K id) throws SQLException;
  List<T> findAll() throws SQLException;
  int update(T value) throws SQLException;
  int delete(K id) throws SQLException;
}
```
`StudentDao implements Dao<Student,Integer>` owns student SQL and maps `id`, `name`, `email` to a Student. Service code depends on the interface, not SQL.

## A3.3 Object–table mapping
`Student.id` → `student.student_id` (PRIMARY KEY); `Student.name` → `student.name`; `Student.email` → `student.email`.

## Alternative: SQL in service/GUI
A column rename requires edits in every service or screen containing that SQL; with DAO it is localized to the DAO and mapper. Both have the same asymptotic database operation cost and similar result memory. DAO adds a small constant abstraction overhead but improves maintainability and testability.
