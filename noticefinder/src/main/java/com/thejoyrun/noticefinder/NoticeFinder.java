package com.thejoyrun.noticefinder;

import android.net.Uri;

import com.thejoyrun.impl.ObjectNoticeInter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static com.thejoyrun.impl.ObjectNoticeInter.NOTICE_SUFFIX;

/**
 * Created by keven-liang on 2017/10/10.
 */

public class NoticeFinder {

    public static final Map<ObjectNoticeInter, List<Object>> OBJECT_MAP = new HashMap<>();//NOTICE 和 实例的对应表
    public static final Map<String, ObjectNoticeInter> NOTICE_MAP = new HashMap<>();//包名+类名 和 NOTICE的对应表
    public static final Map<String , List<String>> SIMPLE_FULL_NAME_MAP = new HashMap<>();//类名和 包名+类名的对应表

    private static String APTNOTICE_HOST = "http://www.aptNotice.com";

    public static void inject(Object host) {
        String className = host.getClass().getName();

        //保存NotceiInter对应的所有实例
        try {
            ObjectNoticeInter objectNotice = NOTICE_MAP.get(className);
            if(objectNotice == null) {

                String fullName = host.getClass().getName();

                Class noticeClass = Class.forName(fullName+NOTICE_SUFFIX);

                objectNotice = (ObjectNoticeInter) noticeClass.newInstance();
            }

            List<Object> objectNotice_objectList = OBJECT_MAP.get(objectNotice);
            if(objectNotice_objectList == null){
                objectNotice_objectList = new ArrayList<>();
            }

            if(!objectNotice_objectList.contains(host)) {
                objectNotice_objectList.add(host);
            }
            OBJECT_MAP.put(objectNotice,objectNotice_objectList);
            NOTICE_MAP.put(className,objectNotice);

            String simpleName = host.getClass().getSimpleName();
            List<String> fullNameList = SIMPLE_FULL_NAME_MAP.get(simpleName);
            if(fullNameList == null){
                fullNameList = new ArrayList<>();
            }
            fullNameList.add(className);
            SIMPLE_FULL_NAME_MAP.put(simpleName,fullNameList);

        } catch (Exception e) {
//            throw new RuntimeException("Unable to inject for " + className, e);
        }
    }

    public static void unInject(Object host){
        String className = host.getClass().getName();
        ObjectNoticeInter objectNoticeInter = NOTICE_MAP.get(className);
        if(objectNoticeInter != null){
            List<Object> objects = OBJECT_MAP.get(objectNoticeInter);

            if(objects != null){
                objects.remove(host);
                if(objects.size() == 0){
                    NOTICE_MAP.remove(objectNoticeInter);
                }
            }else {
                NOTICE_MAP.remove(objectNoticeInter);
            }
        }

        String simpleName = host.getClass().getSimpleName();
        List<String> strings = SIMPLE_FULL_NAME_MAP.get(simpleName);
        if(strings != null){
            strings.remove(className);
            if(strings.size() == 0){
                SIMPLE_FULL_NAME_MAP.remove(simpleName);
            }
        }else {
            SIMPLE_FULL_NAME_MAP.remove(simpleName);
        }
    }
    public static void toNoticeMethod(Uri uri){
        if(!uri.toString().startsWith(APTNOTICE_HOST)){
            return;
        }

        List<String> pathSegments = uri.getPathSegments();
        if(pathSegments == null || pathSegments.size()<2){
           return;
        }

        String className = pathSegments.get(0);
        String methodName = pathSegments.get(1);

        if(className == null || methodName == null) {
           return;
        }

        List<String> fullNameList = SIMPLE_FULL_NAME_MAP.get(className);
        if(fullNameList == null){
           return;
        }

        for (String fullName : fullNameList){
            ObjectNoticeInter objectNoticeInter = NOTICE_MAP.get(fullName);
            if(objectNoticeInter != null) {
                objectNoticeInter.invokeMethod(methodName);
            }
        }

    }

    public static void setNoticeHost(String host){
        APTNOTICE_HOST = host;
    }

    public static String getNoticeHost(){
        return APTNOTICE_HOST;
    }

}
