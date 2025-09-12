package hnu.dll.structure.match;

import cn.edu.dll.constant_values.ConstantValues;
import com.sun.tools.javac.util.StringUtils;
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
        this.maxCostTimeLength = 0;
    }

    public List<MatchElement> getMatchElementList() {
        return matchElementList;
    }

    public void add(MatchElement matchElement) {
        this.matchElementList.add(matchElement);
        this.maxCostTimeLength = Math.max(this.maxCostTimeLength, matchElement.getTimePointPath().getTimeLength());
    }


    public Integer getMaxCostTimeLength() {
        return maxCostTimeLength;
    }

    public void setMaxCostTimeLength(Integer maxCostTimeLength) {
        this.maxCostTimeLength = maxCostTimeLength;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("maxCostTime: ").append(maxCostTimeLength).append(ConstantValues.LINE_SPLIT);
        stringBuilder.append("{");
        for (MatchElement matchElement : matchElementList) {
            stringBuilder.append(matchElement).append(ConstantValues.LINE_SPLIT);
        }
        return stringBuilder.toString();
    }
}
