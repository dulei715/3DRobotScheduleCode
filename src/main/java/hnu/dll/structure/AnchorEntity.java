package hnu.dll.structure;

import hnu.dll.entity.Entity;
import hnu.dll.structure.basic_structure.Anchor;

public class AnchorEntity implements Comparable<AnchorEntity>{
    private Anchor anchor;
    private Entity entity;

    public AnchorEntity(Anchor anchor, Entity entity)  {
        this.anchor = anchor;
        this.entity = entity;
    }

    public Anchor getAnchor() {
        return anchor;
    }

    public void setAnchor(Anchor anchor) {
        this.anchor = anchor;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    /**
     * 只比较Anchor
     */
    public int compareTo(AnchorEntity that) {
        return this.anchor.compareTo(that.anchor);
    }
}
