# B1 – File store and measurements
Run `javac *.java && java PassStore && java LineCounter && java PassGrowthBenchmark`.
- `output.txt`: add/find/list and malformed row test.
- `line_counter_output.txt`: actual local 1/10/50 MiB buffered vs unbuffered timings.
- `growth_output.txt`: actual local find timings for 1k–100k records.
Timings depend on the machine, filesystem cache and JVM; repeat several times for a defensible report.
