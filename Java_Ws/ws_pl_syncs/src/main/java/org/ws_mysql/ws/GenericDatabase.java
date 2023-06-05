package org.ws_mysql.ws;
import java.util.ArrayList;
import java.util.List;

public class GenericDatabase<T> {
    private List<T> data;

    public GenericDatabase() {
        data = new ArrayList<>();
    }

    public void save(T item) {
        data.add(item);
    }

    public void delete(T item) {
        data.remove(item);
    }

    public boolean contains(T item) {
        return data.contains(item);
    }

    public List<T> getAll() {
        return new ArrayList<>(data);
    }
}
