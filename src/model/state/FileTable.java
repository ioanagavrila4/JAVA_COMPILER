package model.state;

import model.value.StringValue;

import java.io.BufferedReader;

public interface FileTable {
    void openFile(StringValue fileName, BufferedReader fileDescriptor);

    BufferedReader getFileDescriptor(StringValue fileName);

    boolean isDefined(StringValue fileName);

    void closeFile(StringValue fileName);
}
