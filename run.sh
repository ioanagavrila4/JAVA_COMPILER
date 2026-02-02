#!/bin/bash
# Compile and run the JavaFX application

echo "Compiling Java files..."
javac --module-path lib/javafx-sdk-23.0.1/lib \
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

    echo "Starting application..."
    java --module-path lib/javafx-sdk-23.0.1/lib \
         --add-modules javafx.controls,javafx.fxml \
         --enable-native-access=javafx.graphics \
         -cp out \
         gui.MainGUI
else
    echo "Compilation failed!"
fi