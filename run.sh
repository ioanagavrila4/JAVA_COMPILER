#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Set JavaFX path
JAVAFX_PATH="lib/javafx-sdk-23.0.1/lib"

echo -e "${YELLOW}======================================${NC}"
echo -e "${YELLOW}   Toy Language Interpreter - MAP${NC}"
echo -e "${YELLOW}======================================${NC}"
echo ""

# Check if JavaFX exists
if [ ! -d "$JAVAFX_PATH" ]; then
    echo -e "${RED}Error: JavaFX not found at $JAVAFX_PATH${NC}"
    echo "Please ensure JavaFX SDK is installed in the lib directory"
    exit 1
fi

# Clean previous builds
echo -e "${GREEN}[1/4] Cleaning previous builds...${NC}"
rm -rf out
mkdir -p out

# Compile all Java files
echo -e "${GREEN}[2/4] Compiling Java files...${NC}"
javac --module-path "$JAVAFX_PATH" \
      --add-modules javafx.controls,javafx.fxml \
      -d out \
      -cp src \
      src/**/*.java

if [ $? -ne 0 ]; then
    echo -e "${RED}Compilation failed!${NC}"
    exit 1
fi

# Copy FXML files to output directory
echo -e "${GREEN}[3/4] Copying FXML files...${NC}"
mkdir -p out/gui
cp src/gui/*.fxml out/gui/

# Create test.in file if it doesn't exist (needed for file operations example)
if [ ! -f "test.in" ]; then
    echo -e "${YELLOW}Creating test.in file for file operations example...${NC}"
    echo "15" > test.in
    echo "20" >> test.in
fi

echo -e "${GREEN}[4/4] Starting application...${NC}"
echo ""
echo -e "${YELLOW}Features available:${NC}"
echo "  • Switch Statement (Example 12)"
echo "  • Count Semaphore (Example 13)"
echo "  • Plus 11 other example programs"
echo ""

# Run the JavaFX application
java --module-path "$JAVAFX_PATH" \
     --add-modules javafx.controls,javafx.fxml \
     -cp out \
     gui.MainGUI