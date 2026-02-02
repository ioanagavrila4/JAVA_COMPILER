#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}=== Java Toy Language Interpreter ===${NC}"
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

# Print Java version
echo -e "${GREEN}Java version:${NC}"
java -version
echo ""

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

# Set classpath (add JavaFX if available)
CLASSPATH="$BUILD_DIR"

# Check for JavaFX libraries
JAVAFX_PATH=""
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

# Try to find JavaFX in common locations if not in lib
if [ -z "$JAVAFX_JARS" ]; then
    # Common JavaFX locations on macOS
    POSSIBLE_JAVAFX_PATHS=(
        "/Library/Java/JavaVirtualMachines/javafx-sdk-*/lib"
        "$HOME/javafx-sdk-*/lib"
        "/usr/local/javafx-sdk-*/lib"
        "/opt/javafx-sdk-*/lib"
    )

    for pattern in "${POSSIBLE_JAVAFX_PATHS[@]}"; do
        for path in $pattern; do
            if [ -d "$path" ]; then
                JAVAFX_PATH="$path"
                echo -e "${GREEN}Found JavaFX at: $JAVAFX_PATH${NC}"
                JAVAFX_JARS=$(find "$JAVAFX_PATH" -name "*.jar" | tr '\n' ':')
                CLASSPATH="$CLASSPATH:$JAVAFX_JARS"
                break 2
            fi
        done
    done
fi

# Compile Java files
echo -e "${YELLOW}Compiling Java files...${NC}"
javac -d "$BUILD_DIR" -cp "$CLASSPATH" $JAVA_FILES

if [ $? -eq 0 ]; then
    echo -e "${GREEN}Compilation successful!${NC}"
    echo ""

    # Copy FXML files to build directory
    echo -e "${YELLOW}Copying FXML files...${NC}"
    find "$SRC_DIR" -name "*.fxml" -exec cp {} "$BUILD_DIR/gui/" \; 2>/dev/null

    # Ask user which interface to run
    echo -e "${YELLOW}Select interface to run:${NC}"
    echo "1) GUI Interface (JavaFX)"
    echo "2) Text Menu Interface"
    echo -n "Enter choice (1 or 2): "
    read choice

    case $choice in
        1)
            echo ""
            echo -e "${YELLOW}Starting GUI interface...${NC}"
            if [ ! -z "$JAVAFX_PATH" ]; then
                # Run with JavaFX module path
                java --module-path "$JAVAFX_PATH" \
                     --add-modules javafx.controls,javafx.fxml \
                     -cp "$CLASSPATH" \
                     gui.MainGUI
            else
                # Try running without module path (if JavaFX is in classpath)
                java -cp "$CLASSPATH" gui.MainGUI
            fi
            ;;
        2)
            echo ""
            echo -e "${YELLOW}Starting text menu interface...${NC}"
            java -cp "$BUILD_DIR" Main
            ;;
        *)
            echo -e "${RED}Invalid choice. Exiting.${NC}"
            exit 1
            ;;
    esac
else
    echo -e "${RED}Compilation failed!${NC}"
    echo -e "${YELLOW}Check the error messages above for details.${NC}"
    exit 1
fi