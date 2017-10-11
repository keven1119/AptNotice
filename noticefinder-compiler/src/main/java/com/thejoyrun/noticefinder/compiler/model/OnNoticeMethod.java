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
    private String value;

    public OnNoticeMethod(Element element){
        if (element.getKind() != ElementKind.METHOD) {
            throw new IllegalArgumentException(
                    String.format("Only methods can be annotated with @%s", OnNotice.class.getSimpleName()));
        }
        this.methodElement = (ExecutableElement) element;
        this.value = methodElement.getAnnotation(OnNotice.class).value();

        if (value == null && value.equals("")) {
            throw new IllegalArgumentException(String.format("Must set valid ids for @%s", OnNotice.class.getSimpleName()));
        }

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

    public String getValue() {
        return value;
    }
}
