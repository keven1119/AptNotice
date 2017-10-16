package com.thejoyrun.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by keven-liang on 2017/10/16.
 */

public abstract class ObjectNoticeInter {
    public static final String NOTICE_SUFFIX = "$$ObjectNoticeInter";
    public static final Map<String,List<String>> SIMPLE_FULL_NAME_MAP = new HashMap<>();
    public abstract void  invokeMethod(String methodName);
}
