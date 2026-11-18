#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
(cd "$ROOT/Task06" && javac PassStore.java LineCounter.java PassGrowthBenchmark.java && java PassStore | tee output.txt && java LineCounter | tee line_counter_output.txt && java PassGrowthBenchmark | tee growth_output.txt)
(cd "$ROOT/Task07" && javac UnsafeVsSafeLogin.java)
(cd "$ROOT/Task08" && javac PassLoader.java BatchBenchmark.java)
(cd "$ROOT/Task09" && javac -cp "$ROOT/Task08" MigrateAndInspect.java)
echo "Local compilation complete. JDBC execution requires MySQL and Connector/J."
