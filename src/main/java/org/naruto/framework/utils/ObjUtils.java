package org.naruto.framework.utils;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

public class ObjUtils {
    public static void copyMap2Obj(Map map, Object obj){
        Iterator<Map.Entry<String, Object>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            String key = (String)entry.getKey();
            Object value = entry.getValue();
            System.out.println("Key = " + key + ", Value = " + value);
            try {
                Field f = obj.getClass().getDeclaredField(key);
                f.setAccessible(true);
                f.set(obj, value);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
