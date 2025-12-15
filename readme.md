# 📝 在线考试系统（Exam System）

一个基于 Spring Boot 的分布式在线考试平台，支持考生登录、试卷生成、答题提交、自动阅卷与成绩查询等功能。采用微服务架构，便于扩展与维护。
# 🛠 技术栈
- 核心框架：Spring Boot 3.x + Spring Cloud Alibaba

- 注册与配置中心：Nacos

- 缓存：Redis（用于题目缓存等）

- 消息队列：RocketMQ（用于异步处理答题记录等）

- 数据库：MySQL 8.0

- 构建工具：Maven

- 部署方式：Jar 包 / Docker

✅ 当前版本已完成全部功能开发

# 🚀 快速开始（本地运行）
## 前置依赖
请确保本地已安装并启动以下服务：
- MySQL ≥ 8.0 创建数据库

- Redis ≥ 6.0 缓存，默认配置即可

- Nacos ≥ 2.2 用于服务注册与配置管理

- RocketMQ ≥ 5.0 消息队列，仅需启动 NameServer 和 Broker


> 💡提示：若仅验证核心流程，可暂时关闭 RocketMQ 相关功能（需注释相关代码或配置），但 Nacos 为必需组件。

## 步骤 1：初始化数据库

执行 SQL 脚本（位于各模块的sql目录下）完成表结构与初始数据导入。

## 步骤 2：启动微服务

在各模块目录下执行：

```bash
#1. 公共模块  
   mvn install  
#2. 启动 API 网关（入口）  
   cd exam-gateway && mvn spring-boot:run  
#3. 启动认证服务（用户登录）  
   cd exam-service && mvn spring-boot:run  
#4. 启动试卷服务  
   cd summary-service && mvn spring-boot:run  
#5. 启动答题提交服务  
   cd exam-service && mvn spring-boot:run  
#6. 启动用户服务  
   cd user-service && mvn spring-boot:run  
```
✅ 所有服务启动后，可在 Nacos 控制台查看注册状态

访问 [http://localhost:8080/index.html（默认账号/密码：nacos/nacos）](http://localhost:8080/index.html%EF%BC%88%E9%BB%98%E8%AE%A4%E8%B4%A6%E5%8F%B7/%E5%AF%86%E7%A0%81%EF%BC%9Anacos/nacos%EF%BC%89)

## 步骤 3：访问系统

**后端网关入口：http://localhost:8080**

# 📂 项目结构简览
exam-system/  
├── exam-gateway/ # API 网关  
├── user-service/ # 用户管理模块  
├── questions-service/ # 试卷管理模块  
├── exam-service/ # 考试管理模块  
├── summary-service/ # 成绩统计模块  
├── exam-system-common/ # 公共工具与 DTO└── README.md

# ℹ️  日志管理说明

本系统各微服务均采用 Logback 进行日志记录，默认日志级别为 `INFO`，日志文件按服务隔离并每日滚动，保存最近7天。

- 日志路径：`{服务目录}/logs/{服务名}.log`
- 错误日志单独输出至 `{服务名}-error.log`，便于快速定位异常
- 开发人员可通过调整 `application.yml` 中 `logging.level` 动态修改日志级别（如 `logging.level.com.example.exam=DEBUG`）

当前阶段暂未接入集中式日志系统，后续可根据运维需求集成 ELK 或类似方案

# 📌 注意事项

本地开发时，请确保各服务的 application.yaml 中 Nacos 地址为 localhost:8848

若跳过 RocketMQ，需在 application.yaml 中关闭相关监听器或设置 mq.enabled=false

# 🎯维护建议

详见项目报告“软件维护”模块