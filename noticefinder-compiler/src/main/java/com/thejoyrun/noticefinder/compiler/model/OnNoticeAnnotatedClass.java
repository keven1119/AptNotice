package com.thejoyrun.noticefinder.compiler.model;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by keven-liang on 2017/10/10.
 */

public class OnNoticeAnnotatedClass {
    public TypeElement mClassElement;
    public List<OnNoticeMethod> mOnNoticeMethods;
    public Elements mElementUtils;

    public OnNoticeAnnotatedClass(TypeElement classElement, Elements elementUtils) {
        this.mClassElement = classElement;
        this.mOnNoticeMethods = new ArrayList<>();
        this.mElementUtils = elementUtils;
    }

    public String getFullClassName() {
        return mClassElement.getQualifiedName().toString();
    }

    public void addOnNoticeMethod(OnNoticeMethod noticeMethod){
        mOnNoticeMethods.add(noticeMethod);
    }

    public JavaFile generateFinder() {

        List<MethodSpec> methodSpecList = new ArrayList<>();

        for (OnNoticeMethod method: mOnNoticeMethods){
            System.out.println(method.getmMethodName());
            // method inject(final T host, Object source, Provider provider)
            MethodSpec.Builder injectMethodBuilder = MethodSpec.methodBuilder(method.getValue())
                    .addModifiers(Modifier.PUBLIC);

            String fullClassName = getFullClassName();
            int i = fullClassName.lastIndexOf(".");

            String packageName = fullClassName.substring(0, i);

            String className = fullClassName.substring(i+1, fullClassName.length());

            ClassName hoverboard = ClassName.get(packageName, className);

            injectMethodBuilder.addStatement("Object object =com.thejoyrun.noticefinder.NoticeFinder.FINDER_MAP.get($L);" +
                    "\r\nif(object != null ){" +
                    "\r\njava.util.List<Object> list = (java.util.List)object;"+
                    "\r\nfor(Object model :list){"+
                    "\r\n(($T)model).$N();" +
                    "\r\n}"+
                    "\r\n}",
                    "\""+getFullClassName()+"\"",
                    hoverboard,
                    method.getmMethodName());
            MethodSpec build = injectMethodBuilder.build();
            methodSpecList.add(build);

            System.out.println(build.toString());
        }

        // generate whole class
        TypeSpec finderClass = TypeSpec.classBuilder(mClassElement.getSimpleName() + "$$NoticeFinder")
                .addModifiers(Modifier.PUBLIC)
                .addMethods(methodSpecList)
                .build();

        String packageName = mElementUtils.getPackageOf(mClassElement).getQualifiedName().toString();

        return JavaFile.builder(packageName, finderClass).build();
    }
}
