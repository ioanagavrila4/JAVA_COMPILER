#!/bin/bash

# Script pentru compilare și rulare automată
# Usage: ./run.sh

# Culori pentru output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Set JavaFX path
JAVAFX_PATH="lib/javafx-sdk-23.0.1/lib"

echo -e "${YELLOW}=== MAP Practical Exam - Toy Language Interpreter ===${NC}"
echo ""

# Step 1: Clean previous builds
echo -e "${GREEN}[1/4] Cleaning previous builds...${NC}"
rm -rf out
mkdir -p out

# Step 2: Compile all Java files
echo -e "${GREEN}[2/4] Compiling Java files...${NC}"
javac --module-path "$JAVAFX_PATH" \
      --add-modules javafx.controls,javafx.fxml \
      -d out \
      -cp src \
      src/**/*.java 2>&1 | tee compile.log

if [ ${PIPESTATUS[0]} -eq 0 ]; then
    echo -e "${GREEN}✓ Compilation successful!${NC}"

    # Step 3: Copy FXML files to output directory
    echo -e "${GREEN}[3/4] Copying FXML files...${NC}"
    mkdir -p out/gui
    cp src/gui/*.fxml out/gui/ 2>/dev/null

    # Create test.in file if it doesn't exist (needed for file operations example)
    if [ ! -f "test.in" ]; then
        echo "15" > test.in
        echo "20" >> test.in
        echo -e "${YELLOW}Created test.in file for file operations${NC}"
    fi

    echo -e "${GREEN}✓ Build complete!${NC}"
    echo ""

    # Step 4: Run the application
    echo -e "${GREEN}[4/4] Starting Toy Language Interpreter...${NC}"
    echo "----------------------------------------"

    java --module-path "$JAVAFX_PATH" \
         --add-modules javafx.controls,javafx.fxml \
         -cp out \
         gui.MainGUI

    EXIT_CODE=$?

    if [ $EXIT_CODE -ne 0 ]; then
        echo ""
        echo -e "${RED}✗ Application exited with error code: $EXIT_CODE${NC}"
        echo -e "${YELLOW}Check the error messages above for details${NC}"
        exit $EXIT_CODE
    fi
else
    echo ""
    echo -e "${RED}✗ Compilation failed!${NC}"
    echo -e "${YELLOW}Check compile.log for detailed error messages${NC}"
    echo ""
    echo "Common issues:"
    echo "  1. Missing JavaFX library - check if lib/javafx-sdk-23.0.1/ exists"
    echo "  2. Syntax errors in Java files"
    echo "  3. Missing dependencies"
    echo ""
    echo -e "${YELLOW}Tip: Run 'cat compile.log | grep error' to see only error messages${NC}"
    exit 1
fi