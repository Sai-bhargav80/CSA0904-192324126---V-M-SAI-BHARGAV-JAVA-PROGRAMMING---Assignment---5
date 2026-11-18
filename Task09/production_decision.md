# B4 – Final Engineering Decision
**Decision: use a relational database as the authoritative store.** At 100 times current volume across 12 campuses, multiple counters need concurrent writes, indexed search, constraints, transactions, backup and centralized reporting. A text file is easy to inspect but has weak concurrent-update and indexing support. Java serialization is Java-specific and not suited to concurrent multi-user updates.

Migration reads and validates each input row, records rejected line numbers, and inserts valid rows with PreparedStatement batches inside one transaction. DatabaseMetaData reports product, driver and tables so deployment differences can be diagnosed.

**Alternative: one serialized file per campus.** A local search could be O(n/12) on average if campus is known, but a global search requires scanning all 12 files, O(n) total. Space is O(n) across all files plus duplicated metadata. File locks or a single-writer service would be needed to coordinate concurrent counters at each campus; cross-campus reporting and atomic global updates are difficult. A shared database provides transaction isolation and indexes.
