#!/bin/bash

# Set JavaFX path
JAVAFX_PATH="lib/javafx-sdk-23.0.1/lib"

# Clean previous builds
rm -rf out
mkdir -p out

# Compile all Java files
echo "Compiling Java files..."
javac --module-path "$JAVAFX_PATH" \
      --add-modules javafx.controls,javafx.fxml \
      -d out \
      -cp src \
      src/**/*.java

if [ $? -eq 0 ]; then
    echo "Compilation successful!"

    # Copy FXML files to output directory
    echo "Copying FXML files..."
    mkdir -p out/gui
    cp src/gui/*.fxml out/gui/

    echo "Build complete!"
else
    echo "Compilation failed!"
    exit 1
fi