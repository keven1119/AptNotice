package com.thejoyrun.noticefinder.compiler.model;

import com.thejoyrun.noticefinder.annotation.OnNotice;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;

/**
 * Created by keven-liang on 2017/10/10.
 */

public class OnNoticeMethod {

    private ExecutableElement methodElement;
    private Name mClassName;
    private Name mMethodName;

    public OnNoticeMethod(Element element){
        if (element.getKind() != ElementKind.METHOD) {
            throw new IllegalArgumentException(
                    String.format("Only methods can be annotated with @%s", OnNotice.class.getSimpleName()));
        }
        this.methodElement = (ExecutableElement) element;
        this.mMethodName = methodElement.getSimpleName();
        // method params count must equals 0
        List<? extends VariableElement> parameters = methodElement.getParameters();
        if (parameters.size() > 0) {
            throw new IllegalArgumentException(
                    String.format("The method annotated with @%s must have no parameters", OnNotice.class.getSimpleName()));
        }
    }

    public Name getmMethodName() {
        return mMethodName;
    }

}
