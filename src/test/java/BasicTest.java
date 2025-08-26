import hnu.dll.entity.Entity;
import hnu.dll.entity.Robot;
import org.junit.Test;

import java.util.UUID;

public class BasicTest {
    @Test
    public void fun1() {
        String uuid = UUID.randomUUID().toString();
        System.out.println(uuid);
        String uuid2 = UUID.randomUUID().toString();
        System.out.println(uuid2);

    }

    @Test
    public void fun2() {
        Class clazz = Robot.class;
        String name = clazz.getName();
        String typeName = clazz.getTypeName();
        String simpleName = clazz.getSimpleName();
        System.out.println(name);
        System.out.println(typeName);
        System.out.println(simpleName);
    }

    @Test
    public void fun3() {
        Entity entity = new Robot("R1", "2-feet",
                0.5, 0.4, 10D);
        String name = entity.getClass().getName();
        System.out.println(name);
    }
}
