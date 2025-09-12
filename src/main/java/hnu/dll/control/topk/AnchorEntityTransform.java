package hnu.dll.control.topk;

import hnu.dll.control.BasicFunctions;
import hnu.dll.entity.Entity;
import hnu.dll.structure.AnchorEntity;
import hnu.dll.structure.basic_structure.Anchor;

import java.util.List;
import java.util.Map;

public class AnchorEntityTransform implements AnchorEntityConvertor{
    Map<String, List<Anchor>> stringAnchorListMap;
    Map<String, Entity> entityMap;
    Map<Anchor, Entity> anchorEntityMap;

    public AnchorEntityTransform(Map<String, List<Anchor>> stringAnchorListMap, Map<String, Entity> entityMap) {
        this.stringAnchorListMap = stringAnchorListMap;
        this.entityMap = entityMap;
        this.anchorEntityMap = BasicFunctions.getAnchorEntityMap(this.stringAnchorListMap, this.entityMap);
    }

    @Override
    public AnchorEntity toAnchorEntity(Anchor anchor) {
        Entity entity = this.anchorEntityMap.get(anchor);
        return new AnchorEntity(anchor, entity);
    }
}
