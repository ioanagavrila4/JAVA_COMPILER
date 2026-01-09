package model.state;

import model.value.Value;
import java.util.List;

public interface Out {
    void add(Value value);
    List<Value> getContent(); //pt gui
}
