package org.naruto.framework.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ObjUtils {
    public static void copyMap2Obj(Map map, Object obj){
        Iterator<Map.Entry<String, Object>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            String key = (String)entry.getKey();
            Object value = entry.getValue();
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

    public static void copyProperties(Object src,Object target)  {
        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                Field sField = src.getClass().getDeclaredField(field.getName());
                sField.setAccessible(true);
                Object value = sField.get(src);

                field.setAccessible(true);
                field.set(target,value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static List transformerClass(List srcList, Class clazz)  {
        List list = new ArrayList();

        for (Object o : srcList) {
            try {
                Object instance = clazz.newInstance();
                copyProperties(o,instance);
                list.add(instance);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
