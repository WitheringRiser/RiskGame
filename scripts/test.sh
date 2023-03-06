#!/bin/bash

./gradlew build || exit 1
./gradlew cloverGenerateReport || exit 1
scripts/coverage_summary.sh
cp client/build/reports/clover/html/* /coverage-out/client_clover.xml || exit 1
cp server/build/reports/clover/html/* /coverage-out/server_clover.xml || exit 1
cp shared/build/reports/clover/html/* /coverage-out/shared_clover.xml || exit 1

