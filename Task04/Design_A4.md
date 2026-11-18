# Task A4 – What Should Be Serialized?
## A4.1 Persisted object graph
Mark the live JDBC `Connection` field `transient`: it is a live external resource, not durable state. Password should not be serialized; preferably store a salted password hash in a dedicated credential store, not plaintext. The Hostel, rooms and residents reachable through non-transient serializable references are traversed recursively. Every reachable non-transient object must be serializable.

## A4.2 Versioning
Add `private static final long serialVersionUID = 1L;` to versioned serializable classes. If an old stream has the same UID, a newly added phone-number field is assigned its default (`null`) when absent. Migration code should supply a safe default. If UID differs, deserialization throws `InvalidClassException`. UID compatibility does not automatically migrate semantic changes.

## Alternative: CSV/JSON
Text formats are often larger than compact binary serialization, and parsing adds CPU work. Saving/loading n residents is O(n) time and O(n) memory if the whole collection is built in memory. A schema change is usually easier to evolve: missing JSON keys can default, and CSV can add a versioned column/header. Java serialization is convenient for Java object graphs but couples data to class definitions and is less portable.
