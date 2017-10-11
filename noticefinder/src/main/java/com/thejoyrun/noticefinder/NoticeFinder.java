package com.thejoyrun.noticefinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by keven-liang on 2017/10/10.
 */

public class NoticeFinder {

    public static final Map<String, List<Object>> FINDER_MAP = new HashMap<>();

    public static void inject(Object host) {
        String className = host.getClass().getName();
        try {
            List<Object> finder = FINDER_MAP.get(className);
            if (finder == null) {
                finder = new ArrayList<>();
            }
            finder.add(host);
            FINDER_MAP.put(className,finder);
        } catch (Exception e) {
            throw new RuntimeException("Unable to inject for " + className, e);
        }
    }

    public static void unInject(Object host){
        String className = host.getClass().getName();
        List<Object> objects = FINDER_MAP.get(className);
        if(objects != null){
            objects.remove(host);
        }
    }
}
