package hnu.dll.structure.basic_structure;

import java.util.Objects;

public class BasicPair <K, V> {
    private K key;
    private V value;

    public BasicPair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BasicPair<?, ?> basicPair = (BasicPair<?, ?>) o;
        return Objects.equals(key, basicPair.key) && Objects.equals(value, basicPair.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }
}
