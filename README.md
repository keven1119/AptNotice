# AptNotice
数据更新通知,通过annotationprocessor生成通知代理，利用Uri实现跨module通讯

## Getting started
### 1.gradle 配置
#### 1>基础模块的build.gradle依赖AptNotice
```groovy
 compile "com.github.keven1119.AptNotice:noticefinder:${APTNOTICE_VERSION}"
 compile "com.github.keven1119.AptNotice:noticefinder-annotation:${APTNOTICE_VERSION}"
 compile "com.github.keven1119.AptNotice:noticefinder-impl:${APTNOTICE_VERSION}"
 apt "com.github.keven1119.AptNotice:noticefinder-compiler:${APTNOTICE_VERSION}"
```

#### 2>基础模块的build.gradle添加annotationprocessor插件
```groovy
apply plugin: 'com.neenbedankt.android-apt'
```

#### 3>根目录biuld.gradle添加jitpack仓库地址
```groovy
allprojects {
    repositories {
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}
```

#### 4>根目录biuld.gradle 添加annotationprocessor classpath
```groovy
dependencies {
        // apt需要的插件
        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
       }
```

### 2.使用
#### 1>对于要通知的类Class A 使用
```java
   NoticeFinder.inject(this);//进行绑定
```
```java
   NoticeFinder.unInject(this);//进行解绑
```

#### 2>使用@OnNotice 注解要通知的函数（暂不支持带参数函数，函数需要public）
```java
  @OnNotice()
    public void updata(){
        mTextView.setText(System.currentTimeMillis()+"");
    }
```

#### 2>通知调用
  ###### 方法1：通知相同模块或者子模块
```java
   new MainActivity$$ObjectNoticeInter().updata();
```
  ###### 方法2：跨模块通知
```java
   NoticeFinder.toNoticeMethod(Uri.parse(NoticeFinder.getNoticeHost()+"/"+类名+"/"+函数名));
   //NoticeFinder.toNoticeMethod(Uri.parse(NoticeFinder.getNoticeHost()+"/MainActivity/updata"));
```










