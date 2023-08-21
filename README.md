# shiro-rbac-server

### 介绍

基于Shiro，JWT，RBAC的简单服务端安全框架

本项目实现了基础的身份认证功能与动态权限控制，并包含简单业务系统所需的过滤器和数据封装，适合初学者快速上手和二次开发。

### 主要功能

- 身份认证：基于JWT实现身份认证
- 权限控制：基于RBAC的权限控制
- 多方式登录：默认添加了密码登录，可自定义添加其他登录方式
- 基础业务系统框架：统一响应数据包装，统一控制器异常处理等功能

### 技术选型

- Java版本：Java 8
- Web框架：SSM
- 持久层框架：MyBatis-plus
- 数据库：Mysql
- 缓存：Redis
- 安全控制：Shiro + JWT + RBAC
- IDE推荐：JetBrains IDEA

### 目录结构

```bash
blog-server/src/main

/java/com.zjc.shiro
├─  config                      # 配置类
├─  controller                  # 接口控制器
├─  dto                         # 响应数据类
├─  entity                      # 数据模型
├─  enums                       # 项目常量
├─  exception                   # 自定义异常类型
├─  handler                     # 相关全局处理器
├─  mapper                      # mybatis-plus 数据映射接口
├─  security                    # 系统安全控制相关
    ├─  filter                  # 认证与授权过滤器
    ├─  realm                   # 认证域
    └─  token                   # 自定义认证令牌
├─  service                     # 业务类
├─  utils                       # 常用工具包
├─  vo                          # 接收参数类
└─  ShiroRbacApplication        # 项目入口文件

/resources
├─  mapper                      # mybatis sql映射文件            
├─  application.yml             # 项目配置文件（由模板复制）
├─  application.yml.template    # 项目配置文件模板
└─  model.sql                   # sql初始化文件
```

### 安装使用

1. 克隆项目到本地

2. 初始化IDEA项目

3. 复制application.yml.template 为 application.yml，将配置改成自己的

4. 新建mysql数据库，执行model.sql文件

5. 打开IDEA，点击运行


### 功能测试

1. 访问 /login 完成登录，获取token，默认管理员账号 13800000000，密码 123

2. 访问授权接口，需要携带 token，存放于 header.Authorization = Bearer token

3. 如果 token 失效或非法，认证过滤器将拒绝；如果没有访问权限，授权过滤器将拒绝

### 二次开发

1. 访问 /user/register 注册不同用户，默认绑定 user 角色

2. 在 AuthController 中可添加更多登录方式，通过认证后为其分配 token 即可

3. 自定义路径的权限，完成 permission-role-user 三级绑定，实现RBAC权限控制

4. 对于可匿名访问的接口，可在 ShiroConfig 处配置白名单，也可在数据库中配置开启匿名访问

5. 实现自定义业务模块，在数据库中配置相关权限，即可为业务模块添加安全控制
