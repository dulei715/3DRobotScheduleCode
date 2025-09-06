package hnu.dll.structure.match;

import hnu.dll.structure.basic_structure.BasicPair;

import java.util.ArrayList;
import java.util.List;

public class Match {
    private List<MatchElement> matchElementList;
    private Integer maxCostTimeLength;

    public Match(List<MatchElement> matchElementList, Integer maxCostTimeLength) {
        this.matchElementList = matchElementList;
        this.maxCostTimeLength = maxCostTimeLength;
    }

    public Match() {
        this.matchElementList = new ArrayList<>();
//        this.maxCostTimeLength = 0;
    }

    public List<MatchElement> getMatchElementList() {
        return matchElementList;
    }

    public void add(MatchElement matchElement) {
        this.matchElementList.add(matchElement);
    }


    public Integer getMaxCostTimeLength() {
        return maxCostTimeLength;
    }

    public void setMaxCostTimeLength(Integer maxCostTimeLength) {
        this.maxCostTimeLength = maxCostTimeLength;
    }
}
