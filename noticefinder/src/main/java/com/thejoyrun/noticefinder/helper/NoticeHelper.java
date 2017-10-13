package com.thejoyrun.noticefinder.helper;

import com.thejoyrun.noticefinder.event.AptNoticeEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by keven-liang on 2017/10/13.
 */

public class NoticeHelper {

    /**
     * 暂不支持执行带参数的函数
     * 执行要某个类的某个方法，例如   MainActivity.updataMsg
     * @param classMethod
     */
    public void toNotice(String classMethod){
        AptNoticeEvent aptNoticeEvent = new AptNoticeEvent();
        aptNoticeEvent.eventName = classMethod;
        EventBus.getDefault().post(aptNoticeEvent);
    }
}
