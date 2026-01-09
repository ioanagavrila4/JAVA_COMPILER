package model.state;

import model.value.StringValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapFileTable implements FileTable {
    private final Map<StringValue, BufferedReader> fileTable = new HashMap<>();

    @Override
    public void openFile(StringValue fileName, BufferedReader fileDescriptor) {
        fileTable.put(fileName, fileDescriptor);
    }

    @Override
    public BufferedReader getFileDescriptor(StringValue fileName) {
        return fileTable.get(fileName);
    }

    @Override
    public boolean isDefined(StringValue fileName) {
        return fileTable.containsKey(fileName);
    }

    @Override
    public void closeFile(StringValue fileName) {
        BufferedReader reader = fileTable.get(fileName);
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException("Error closing file: " + fileName.getVal());
            }
            fileTable.remove(fileName);
        }
    }

    @Override
    public Map<StringValue, BufferedReader> getContent() {
        return new HashMap<>(fileTable);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (StringValue fileName : fileTable.keySet()) {
            sb.append(fileName.getVal()).append("\n");
        }
        return sb.toString();
    }
}
