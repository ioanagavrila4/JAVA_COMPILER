package model.state;

import model.value.StringValue;

import java.io.BufferedReader;
import java.util.Map;

public interface FileTable {
    void openFile(StringValue fileName, BufferedReader fileDescriptor);

    BufferedReader getFileDescriptor(StringValue fileName);

    boolean isDefined(StringValue fileName);

    void closeFile(StringValue fileName);

    Map<StringValue, BufferedReader> getContent(); //s-a adaugat nou pt a obt lista de fisiere
}
