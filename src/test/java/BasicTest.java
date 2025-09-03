import hnu.dll.entity.Entity;
import hnu.dll.entity.Robot;
import org.junit.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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

    @Test
    public void fun4() {
        Map<String, Integer> map = new HashMap<>();
        map.put("name", 1);
        map.put("age", 30);
        map.put("zipcode", 410082);
        System.out.println(map);
        Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> next = iterator.next();
            if (next.getValue().equals(30)) {
                iterator.remove();
            }
        }
        System.out.println(map);
    }
}
