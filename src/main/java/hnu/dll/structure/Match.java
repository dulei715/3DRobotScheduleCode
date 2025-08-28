package hnu.dll.structure;

import hnu.dll.structure.basic_structure.BasicPair;

import java.util.ArrayList;
import java.util.List;

public class Match<K, V> {
    private List<BasicPair<K,V>> matchData;

    public Match() {
        this.matchData = new ArrayList<>();
    }

    public void addElement(K partA, V partB) {
        this.matchData.add(new BasicPair<>(partA, partB));
    }

    public void addElement(BasicPair<K, V> element) {
        this.matchData.add(element);
    }

    public BasicPair<K, V> getElement(int index) {
        return this.matchData.get(index);
    }

}
