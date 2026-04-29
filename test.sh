#!/usr/bin/env bash

# ~/code/java/library-system/test.sh
# compile and run the library system
# example:  `bash ./test.sh`

# Configuration
SRC_DIR="src"
OUT_DIR="out-cli"
LIB_JAR="src/assets/mysql-connector-j-9.6.0.jar"
MAIN_CLASS="Main"

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo "Starting compilation..."

# Ensure output directory exists
mkdir -p "$OUT_DIR"

# Copy resources (like .properties files) to output directory
cp "$SRC_DIR/database.properties" "$OUT_DIR/" 2>/dev/null || :

# Compile
if javac -cp "$LIB_JAR" -d "$OUT_DIR" $(find "$SRC_DIR" -name '*.java'); then
    echo -e "${GREEN}Compilation successful.${NC}"
    echo "Running program..."
    # Run
    java -cp "$OUT_DIR:$LIB_JAR" "$MAIN_CLASS"
else
    echo -e "${RED}Compilation failed.${NC}"
    exit 1
fi
