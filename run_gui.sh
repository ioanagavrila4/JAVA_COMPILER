#!/bin/bash

# Set JavaFX path
JAVAFX_PATH="lib/javafx-sdk-23.0.1/lib"

# Check if compiled classes exist
if [ ! -d "out" ]; then
    echo "Please compile first using ./compile_gui.sh"
    exit 1
fi

# Create test.in file if it doesn't exist (needed for file operations example)
if [ ! -f "test.in" ]; then
    echo "15" > test.in
    echo "20" >> test.in
fi

echo "Running JavaFX application..."
java --module-path "$JAVAFX_PATH" \
     --add-modules javafx.controls,javafx.fxml \
     -cp out \
     gui.MainGUI