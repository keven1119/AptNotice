package com.thejoyrun.noticefinder.compiler.model;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.thejoyrun.impl.ObjectNoticeInter;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import static com.thejoyrun.impl.ObjectNoticeInter.NOTICE_SUFFIX;

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

    //生成在对应的moduel
    public JavaFile generateFinder() {

        List<MethodSpec> methodSpecList = new ArrayList<>();

        String fullClassName = getFullClassName();
        int dotIndex = fullClassName.lastIndexOf(".");
        String packageName = fullClassName.substring(0, dotIndex);
        String className = fullClassName.substring(dotIndex+1, fullClassName.length());
        ClassName hoverboard = ClassName.get(packageName, className);

        //创建invokeMethod接收方法
        MethodSpec.Builder invokeMethodBuilder = MethodSpec.methodBuilder("invokeMethod")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(String.class, "methodName");


        //创建对应通知的方法
        for (int i =0; i<mOnNoticeMethods.size() ;i ++){
            OnNoticeMethod method = mOnNoticeMethods.get(i);
            String methodName = method.getmMethodName().toString();

            invokeMethodBuilder.addStatement("\r\nif(methodName.equals($S)){"+
                    "\r\n$N();"+
                    "\r\n}",
                    methodName,
                    methodName);


            // method inject(final T host, Object source, Provider provider)
            MethodSpec.Builder injectMethodBuilder = MethodSpec.methodBuilder(method.getmMethodName().toString())
                    .addModifiers(Modifier.PUBLIC);

            injectMethodBuilder.addStatement("Object noticeObject = com.thejoyrun.noticefinder.NoticeFinder.NOTICE_MAP.get($L);"+
                            "\r\nif(noticeObject != null){"+
                            "Object object =com.thejoyrun.noticefinder.NoticeFinder.OBJECT_MAP.get(noticeObject);" +
                            "\r\nif(object != null ){" +
                            "\r\njava.util.List<Object> list = (java.util.List)object;"+
                            "\r\nfor(Object model :list){"+
                            "\r\n(($T)model).$N();" +
                            "\r\n}"+
                            "\r\n}"+
                            "\r\n}",
                            "\""+getFullClassName()+"\"",
                    hoverboard,
                    method.getmMethodName());

            MethodSpec build = injectMethodBuilder.build();
            methodSpecList.add(build);

            System.out.println(build.toString());
        }

        methodSpecList.add(invokeMethodBuilder.build());

        String simpleName = mClassElement.getSimpleName() + NOTICE_SUFFIX;

        // generate whole class
        TypeSpec finderClass = TypeSpec.classBuilder(mClassElement.getSimpleName() + NOTICE_SUFFIX)
                .addModifiers(Modifier.PUBLIC)
                .addMethods(methodSpecList)
                .superclass(ObjectNoticeInter.class)
                .build();

        return JavaFile.builder(packageName, finderClass).build();
    }



}
