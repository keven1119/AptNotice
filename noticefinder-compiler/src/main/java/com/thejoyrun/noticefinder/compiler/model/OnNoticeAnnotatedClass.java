package com.thejoyrun.noticefinder.compiler.model;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.thejoyrun.noticefinder.event.AptNoticeEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
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

    //生成在对应的moduel
    public JavaFile generateFinder() {

        List<MethodSpec> methodSpecList = new ArrayList<>();

        String fullClassName = getFullClassName();
        int dotIndex = fullClassName.lastIndexOf(".");
        String packageName = fullClassName.substring(0, dotIndex);
        String className = fullClassName.substring(dotIndex+1, fullClassName.length());
        ClassName hoverboard = ClassName.get(packageName, className);

        AnnotationSpec.Builder eventBusAnnatiton = AnnotationSpec.builder(Subscribe.class)
                .addMember("threadMode", "$T.$L",ThreadMode.class,ThreadMode.MAIN.name());

        //创建EventBus接收方法
        MethodSpec.Builder eventBusMethodBuilder = MethodSpec.methodBuilder("onEventMainThread")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(eventBusAnnatiton.build())
                .addParameter(AptNoticeEvent.class, "event")
                .addStatement("if(event.eventName.startsWith($S)){"+
                        "\r\nString[] nameSplit = event.eventName.split(\".\")",
                        className);


        //创建对应通知的方法
        for (int i =0; i<mOnNoticeMethods.size() ;i ++){
            OnNoticeMethod method = mOnNoticeMethods.get(i);
            String methodName = method.getmMethodName().toString();

            eventBusMethodBuilder.addStatement("\r\nif(nameSplit[1].equals($S)){"+
                    "\r\n$N();"+
                    "\r\n}",
                    methodName,
                    methodName);

            if(i == mOnNoticeMethods.size() -1){
                eventBusMethodBuilder.addStatement("}");
            }


            // method inject(final T host, Object source, Provider provider)
            MethodSpec.Builder injectMethodBuilder = MethodSpec.methodBuilder(method.getmMethodName().toString())
                    .addModifiers(Modifier.PUBLIC);

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

        methodSpecList.add(eventBusMethodBuilder.build());

        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("if(!$T.getDefault().isRegistered(this)){"+
                        "\r\n$T.getDefault().register(this);"+
                        "\r\n}",
                        EventBus.class,
                        EventBus.class)
                .build();

        // generate whole class
        TypeSpec finderClass = TypeSpec.classBuilder(mClassElement.getSimpleName() + "$$NoticeFinder")
                .addModifiers(Modifier.PUBLIC)
                .addMethods(methodSpecList)
                .addMethod(constructor)
                .build();

//        String packageName = mElementUtils.getPackageOf(mClassElement).getQualifiedName().toString();

        return JavaFile.builder(packageName, finderClass).build();
    }



}
