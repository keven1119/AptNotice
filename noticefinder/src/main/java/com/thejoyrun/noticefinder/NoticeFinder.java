package com.thejoyrun.noticefinder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by keven-liang on 2017/10/10.
 */

public class NoticeFinder {

    public static final Map<String, Object> FINDER_MAP = new HashMap<>();

    public static void inject(Object host) {
        String className = host.getClass().getName();
        try {
            Object finder = FINDER_MAP.get(className);
            if (finder == null) {
                FINDER_MAP.put(className,host);
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to inject for " + className, e);
        }
    }

    public static void unInject(Object host){
        FINDER_MAP.remove(host);
    }
}
