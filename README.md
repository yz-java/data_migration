# data_migration 数据迁移工具

支持mysql、sqlite、postgresql、sqlserver四种数据库之间数据相互迁移。

### 两种方式实现数据迁移：

![image.png](https://upload-images.jianshu.io/upload_images/3057341-a1b8410eb5bb5d04.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### ①页面交互
   
数据库连接成功后，通过点击按钮查询所有表显示当前数据库所有表名然后点击表名显示该表的所有字段，只需要勾选需要操作的字段。
  
**这种方式只支持源数据库select，目标数据库insert操作**

#### ②SQL输入
这种方式比较简单，在页面中输入将在两边数据库执行的sql语句然后点击执行按钮即可

### 安装、部署

**JDK环境：>=1.8**

#### 源码部署：

项目源码：https://github.com/yz-java/data_migration
maven打包：mvn clean package -Dmaven.test.skip=true

#### realase下载：

[Download](https://github.com/yz-java/data_migration/releases)

#### 运行
java -jar 安装包


