
## 简介

&emsp; &emsp; MFile 是一款基于 Java 开发、以 Minio 对象存储为核心的专业化资源文件管理服务系统，核心定位是解决传统 Minio 资源管理的痛点。它无需用户登录 Minio 后台操作，通过优化后的统一入口，即可完成资源文件上传、下载、权限配置等全生命周期管控，大幅简化复杂流程；同时提供标准化 API 接口体系，开发者无需使用 Minio SDK，就能快速实现业务系统与 Minio 存储的集成。其依托 Java 跨平台、高稳定的特性，既能适配各类业务架构，又可根据需求扩展对接数据分析、可视化等工具，为企业提供从资源管控到数据价值挖掘的高效解决方案，广泛适用于企业文件管理、电商资源管控、政务文档归档等多场景。<br>
&emsp; &emsp;
MFile并非替代Minio对象存储，而是以Minio的稳定存储能力为基础，针对其原生管理方式的痛点进行精准优化。传统模式下，用户需登录Minio后台进行资源的上传、下载、权限配置等操作，流程繁琐且缺乏业务化的管控逻辑；开发者则需熟练掌握Minio SDK并进行定制化开发，才能实现与业务系统的集成。MFile恰好填补这一空白，成为连接用户操作与Minio底层存储的中间件，既简化了人工管控流程，又降低了系统集成门槛。

## 发行版本

#### &emsp; [下载链接](https://github.com/Alcedo-code/MFile.git "下载链接"): https://github.com/Alcedo-code/MFile.git <br>

## 功能概述

#### &emsp; 适用场景与价值体现

&emsp;&emsp; MFile广泛适用于各类需要基于Minio进行资源管理的场景，包括但不限于：企业级文件存储管理平台、电商平台的商品图片/视频资源管控、政务系统的文档归档与检索服务、研发团队的代码包及测试资源管理等。其核心价值在于：对运维人员，简化了Minio的日常管控流程，降低了运维成本；对开发者，提供了便捷的集成方式，提升了开发效率；对企业，实现了资源的规范化、精细化管理，保障了数据安全与业务连续性。<br>

![操作](https://raw.githubusercontent.com/Alcedo-code/MFile/refs/heads/main/mfile-index.jpg "2021-07-03_09-43-50.gif")

## 系统目录

```
MFile/
├── doc/
│   ├── docker/
│   ├── docs/
│   └── example/
├── MFile/
│   └── src/
│       ├── main/
│       │   ├── java/com/angel/minio/
│       │   │   ├── config/
│       │   │   │   ├── minio/
│       │   │   │   └── mvc/
│       │   │   ├── controller/
│       │   │   ├── domain/
│       │   │   ├── mapper/
│       │   │   ├── service/
│       │   │   │   └── impl/
│       │   │   └── util/
│       │   │       ├── bean/
│       │   │       ├── constant/
│       │   │       ├── security/
│       │   │       ├── text/
│       │   │       ├── utils/
│       │   │       │   └── exception/
│       │   │       └── web/
│       │   │           ├── controller/
│       │   │           ├── domain/
│       │   │           └── page/
│       │   └── resources/
│       │       ├── mapper/
│       │       ├── static/
│       │       │   ├── doc/
│       │       │   ├── images/
│       │       │   │   └── map/
│       │       │   │       └── thumb/
│       │       │   └── static/
│       │       │       ├── css/
│       │       │       ├── fonts/
│       │       │       ├── img/
│       │       │       └── js/
│       │       └── templates/
│       └── test/
│           └── java/com/angel/minio/
└── MFile-ui/
    ├── MFile/
    │   ├── images/
    │   │   └── map/
    │   │       └── thumb/
    │   └── static/
    │       ├── css/
    │       ├── fonts/
    │       ├── img/
    │       └── js/
    ├── mock/
    ├── node_modules/
    └── src/
        ├── api/
        ├── assets/
        │   ├── 404_images/
        │   └── images/
        ├── components/
        │   ├── Breadcrumb/
        │   ├── Hamburger/
        │   └── SvgIcon/
        ├── icons/
        │   └── svg/
        ├── interface/
        ├── layout/
        │   ├── components/
        │   │   └── Sidebar/
        │   └── mixin/
        ├── router/
        ├── store/
        │   └── modules/
        ├── styles/
        ├── utils/
        └── views/
            ├── login/
            └── table/
```

## 核心技术

### 依赖

- [postgresql] 12以上版本
- [Jdk] 1.8

### 后端

- [Spring Boot2.3.5.RELEASE](https://spring.io/projects/spring-boot/): Spring Boot是一款开箱即用框架，让我们的Spring应用变的更轻量化、更快的入门。
  在主程序执行main函数就可以运行。你也可以打包你的应用为jar并通过使用java -jar来运行你的Web应用；
### 前端

- [npm](https://www.npmjs.com/)：node.js的包管理工具，用于统一管理我们前端项目中需要用到的包、插件、工具、命令等，便于开发和维护。
- [webpack](https://webpack.docschina.org/)：用于现代 JavaScript 应用程序的静态模块打包工具。
- [ES6](https://es6.ruanyifeng.com/)：JavaScript的新版本，ECMAScript6的简称。利用ES6我们可以简化我们的JS代码，同时利用其提供的强大功能来快速实现JS逻辑。
- [vue-cli](https://cli.vuejs.org/)：Vue的脚手架工具，用于自动生成Vue项目的目录及文件。
- [vue-router](https://router.vuejs.org/)： Vue提供的前端路由工具，利用其我们实现页面的路由控制，局部刷新及按需加载，构建单页应用，实现前后端分离。
- [element-ui](https://element.eleme.cn/#/zh-CN)：基于MVVM框架Vue开源出来的一套前端ui组件。
## 部署方式

### 前端发布

```
cd MFile\MFile-ui
npm run build
```

### 后端Java打包

简易步骤

```
下载最新发行版
配置完JAVA_HOME后，通过mvn clean package -Dmaven.test.skip=true打包
或者通过IDEA打包
```


在Linux上先准备好maven、node.js、jdk

- [Apache Maven] 3.6 <br>
- [Node.js] v14.19.1 <br>
- [Jdk] 1.8

## 谁在使用

感谢关注并使用MFile的朋友，如果贵公司、组织、学校等正在使用MFile，希望可以按照此[Issue]
登记并提供贵公司、组织、学校的logo，我们将把贵方的logo放在项目首页进行展示并优先进行技术支持 <br>

## 版本问题

已知以下版本存在兼容性问题，请不要使用

- Node.js V14及以上
- openJdk
- Jdk 1.8及以下/11及以上（jdk11部分版本可能有问题）


## 商业授权

MFile使用[Apache2.0开源协议](http://www.apache.org/licenses/LICENSE-2.0.html)
，允许商业使用，但务必保留类作者、Copyright信息。 <br>
个人和企业，在直接使用、二次开发后商业使用，需要遵守：

- 包含Apache LICENSE文件
- 如果修改了代码，需要在被修改的文件中说明
- 在修改和有源代码衍生的代码中，需要带有原来代码中的协议，商标
- 在二次开发后商业发布的产品中，使用了多个开源软件，则必须包含一个Notice文件，在Notice文件中需要带有Apache
  LICENSE。你可以在Notice中增加自己的许可，但不可以表现为对Apache
  LICENSE构成更改。

## 技术支持

**如有问题，请提交 [Issue] <br>**


个人微信：个人微信，不闲聊，加群、咨询请备注，备注格式不限但最好是：“项目名 + 问题” ，项目名可以是**MFile**、**Minio资源管理系统**，只要能一眼分辨就行 <br>
例1：minio资源管理系统 加群 <br>
例2：MFile 有个问题xxxx <br>
有问题，不管是技术问题还是使用问题，都尽量在群里问。加个人微信记得**一定要加备注 ！！！**<br>
