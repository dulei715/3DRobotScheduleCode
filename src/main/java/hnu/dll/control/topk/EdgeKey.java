package hnu.dll.control.topk;

import hnu.dll.structure.basic_structure.Anchor;

import java.util.Objects;

public class EdgeKey {
    final Anchor u, v;
    EdgeKey(Anchor u, Anchor v) { this.u = u; this.v = v; }
    @Override
    public boolean equals(Object o) {
        return (o instanceof EdgeKey) && Objects.equals(u, ((EdgeKey)o).u) && Objects.equals(v, ((EdgeKey)o).v);
    }
    @Override
    public int hashCode() {
        return Objects.hash(u, v);
    }
}
