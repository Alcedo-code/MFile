# MFile 文件资源管理服务系统

* [版本清单](#Release)
* [编译说明](#build)
* [功能说明](#功能说明)
* [部署说明](#部署说明)

##Release

 -  V 1.0
 
##Build

 - JDK1.8
 -  IntelliJ IDEA 2021 x64
 -  apache-maven-3.6.3

  
##功能说明

##部署说明
  
  1. 依赖docker 、docker-compose、postgresql12、tomcat8.5以上版本(如没有docker可单独部署一个tomcat) 基础环境,
  2. 拷贝MFile.war文件到tomcat/webapps目录下；   
  3. 浏览器访问 http://ip:{port}/MFile/
  
* 依赖环境
    1. JDK1.8；
* JAR包部署(暂未提供)
    1. 安装JDK；
    2. 将JAR包拷贝到目标服务器的目标目录下；
    3. 将配置文件(bootstrap.properties)拷贝到JAR包同一目录下；
    4. 修改配置文件：
       <pre>
          启动后访问地址：http://ip:port/MFile/
       </pre>
    5. 安装minio地址：http://ip:9900 用于用户文件上传
    6. 执行上述命令后等待控制台启动日志，正常启动后可以打开接口文档验证是否启动成功，接口文档地址：
       <pre>http://ip:port/MFile/doc/index.html</pre>
* WAR包部署
    1. 安装JDK；
    2. 将war包拷贝到目标服务器的目标目录下(tomcat/webapps)；
    3. 将配置文件(bootstrap.properties)拷贝到war包同一目录下；
    4. 安装minio地址：http://ip:9900 用于用户图标上传
    5. 执行上述命令后等待控制台启动日志，正常启动后可以打开接口文档验证是否启动成功，接口文档地址：
       <pre>http://ip:port/MFile/doc/index.html</pre>
* Docker部署
    1. 安装Docker(推荐安装portainer)
    2. 上传镜像到目标服务器某个目下
    3. 使用命令(docker import)或portainer(推荐)导入镜像
    4. 使用命令(docker run)或portainer(推荐)运行一个该镜像的容器，运行参数包括：
        1. 映射端口(镜像暴露的端口是8080)
        2. [可选]通过"JVM_OPTIONS"环境变量设置JVM参数，默认是："-Xms1024m -Xmx1024m"
    5. 容器启动后，在浏览器中访问系统是否正常启动，地址如下：
       <pre>http://ip:port/MFile/</pre>
       其中ip和port分别为主机的ip和port。
    6. 系统接口页面地址：
       <pre>http://ip:port/MFile/doc/index.html</pre>
* 技术支持
* 联系作者 QQ:815657032

   

                 
