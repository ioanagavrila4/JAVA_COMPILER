#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}=== Java Toy Language Interpreter - Compile Only ===${NC}"
echo ""

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo -e "${RED}Error: Java is not installed or not in PATH${NC}"
    exit 1
fi

# Check if javac is installed
if ! command -v javac &> /dev/null; then
    echo -e "${RED}Error: Java compiler (javac) is not installed or not in PATH${NC}"
    exit 1
fi

# Set up directories
SRC_DIR="src"
BUILD_DIR="build"
LIB_DIR="lib"

# Create build directory if it doesn't exist
if [ ! -d "$BUILD_DIR" ]; then
    echo -e "${YELLOW}Creating build directory...${NC}"
    mkdir -p "$BUILD_DIR"
fi

# Clean previous build
echo -e "${YELLOW}Cleaning previous build...${NC}"
rm -rf "$BUILD_DIR"/*

# Find all Java files
echo -e "${YELLOW}Finding Java source files...${NC}"
JAVA_FILES=$(find "$SRC_DIR" -name "*.java")
FILE_COUNT=$(echo "$JAVA_FILES" | wc -l | tr -d ' ')
echo -e "${GREEN}Found $FILE_COUNT Java files${NC}"

# Set classpath (add JavaFX if available)
CLASSPATH="$BUILD_DIR"

# Check for JavaFX libraries
if [ -d "$LIB_DIR/javafx-sdk-23.0.1/lib" ]; then
    JAVAFX_PATH="$LIB_DIR/javafx-sdk-23.0.1/lib"
    JAVAFX_JARS=$(find "$JAVAFX_PATH" -name "*.jar" 2>/dev/null | tr '\n' ':')
    if [ ! -z "$JAVAFX_JARS" ]; then
        CLASSPATH="$CLASSPATH:$JAVAFX_JARS"
        echo -e "${GREEN}Found JavaFX libraries in $JAVAFX_PATH${NC}"
    fi
elif [ -d "$LIB_DIR" ]; then
    # Look for JavaFX in any subdirectory of lib
    JAVAFX_DIR=$(find "$LIB_DIR" -type d -name "lib" -path "*/javafx*/lib" 2>/dev/null | head -1)
    if [ ! -z "$JAVAFX_DIR" ]; then
        JAVAFX_PATH="$JAVAFX_DIR"
        JAVAFX_JARS=$(find "$JAVAFX_PATH" -name "*.jar" 2>/dev/null | tr '\n' ':')
        if [ ! -z "$JAVAFX_JARS" ]; then
            CLASSPATH="$CLASSPATH:$JAVAFX_JARS"
            echo -e "${GREEN}Found JavaFX libraries in $JAVAFX_PATH${NC}"
        fi
    fi
fi

# Compile Java files
echo -e "${YELLOW}Compiling Java files...${NC}"
javac -d "$BUILD_DIR" -cp "$CLASSPATH" $JAVA_FILES

if [ $? -eq 0 ]; then
    echo ""
    echo -e "${GREEN}✅ Compilation successful!${NC}"
    echo ""

    # Copy FXML files to build directory
    echo -e "${YELLOW}Copying FXML files...${NC}"
    mkdir -p "$BUILD_DIR/gui"
    find "$SRC_DIR" -name "*.fxml" -exec cp {} "$BUILD_DIR/gui/" \; 2>/dev/null
    echo -e "${GREEN}FXML files copied${NC}"

    echo ""
    echo -e "${GREEN}=== Build Complete ===${NC}"
    echo ""
    echo -e "To run the application:"
    echo -e "  ${YELLOW}GUI:${NC}  ./run.sh (then select option 1)"
    echo -e "  ${YELLOW}Text:${NC} ./run.sh (then select option 2)"
    echo ""
else
    echo ""
    echo -e "${RED}❌ Compilation failed!${NC}"
    echo -e "${YELLOW}Check the error messages above for details.${NC}"
    exit 1
fi