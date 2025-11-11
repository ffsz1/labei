# 拉贝星球 Docker 一键部署脚本

## 📦 交付文件

1. **labei-docker-deploy.sh** - 完整的一键部署脚本
2. **部署说明.md** - 简要执行说明

---

## 🚀 快速开始

### 执行命令

```bash
chmod +x labei-docker-deploy.sh && ./labei-docker-deploy.sh
```

### 前置要求

脚本会自动检查以下依赖（如未安装会提示并退出）：
- Docker
- Docker Compose
- Git
- Node.js 18+
- npm 9+

---

## 📋 脚本功能说明

### 自动执行的13个步骤

#### 步骤1: 检查系统依赖
- 检查 Docker、Git、Node.js、npm 是否已安装
- 检查 Docker Compose 版本（支持 docker compose 和 docker-compose）

#### 步骤2: 清理旧环境
- 删除旧的 labei 目录（如存在）

#### 步骤3: 拉取GitHub源码
- 从 https://github.com/ffsz1/labei.git 克隆源码

#### 步骤4-6: 创建配置文件
- **docker-compose.yml** - 定义6个服务（MySQL 8.0、Redis 6.2-alpine、MongoDB 5.0、apiservice、imsvr、nginx）
- **nginx.conf** - Nginx反向代理配置
- **labei.sql** - MySQL数据库初始化脚本

#### 步骤7-8: 创建Dockerfile
- **apiservice/Dockerfile** - Spring Boot API服务（Maven 3.8 + OpenJDK 8）
- **imsvr/Dockerfile** - Node.js IM服务（Node 18 + PM2）

#### 步骤9-10: 修复配置文件
- 自动替换配置文件中的占位符（mysql密碼、redis密碼、yingtao等）

#### 步骤11: 启动Docker服务
- 执行 docker-compose up -d --build

#### 步骤12: 等待服务就绪
- 等待120秒让所有服务完全启动

#### 步骤13: 验证部署
- 验证1: 显示所有Docker服务状态
- 验证2: 检查MySQL数据库表
- 验证3: 显示数据库镜像版本

---

## 🔐 固化配置（严禁修改）

### 数据库版本
- **MySQL**: 8.0
- **Redis**: 6.2-alpine
- **MongoDB**: 5.0

### 数据库密码
- **MySQL**: root / LaBei_MySQL_2025
- **Redis**: LaBei_Redis_2025
- **MongoDB**: labei_mongo / LaBei_Mongo_2025

### 数据库名称
- **MySQL**: labei
- **MongoDB**: artqiyi

---

## 🌐 访问地址

部署完成后可通过以下地址访问：

- **H5前端**: http://服务器IP/h5
- **管理后台**: http://服务器IP/admin
  - 账号: 13800138000
  - 密码: 123456

---

## 🐳 Docker服务说明

### 6个容器服务

1. **labei-mysql** (MySQL 8.0)
   - 端口: 3306
   - 数据卷: mysql-data

2. **labei-redis** (Redis 6.2-alpine)
   - 端口: 6379
   - 数据卷: redis-data

3. **labei-mongodb** (MongoDB 5.0)
   - 端口: 27017
   - 数据卷: mongodb-data

4. **labei-apiservice** (Spring Boot API)
   - 端口: 8080
   - 依赖: mysql, redis

5. **labei-imsvr** (Node.js IM服务)
   - 端口: 3000, 3001, 3002, 3003
   - 依赖: mysql, redis, mongodb
   - PM2进程: im-svr-3000, http-svr-3001, cron-svr-3003

6. **labei-nginx** (Nginx 1.24-alpine)
   - 端口: 80
   - 依赖: apiservice, imsvr

---

## 🛠️ 常用命令

```bash
# 查看所有服务状态
docker-compose ps

# 查看服务日志
docker-compose logs -f [服务名]

# 重启所有服务
docker-compose restart

# 重启单个服务
docker-compose restart [服务名]

# 停止所有服务
docker-compose down

# 停止并删除数据卷
docker-compose down -v
```

---

## 📝 技术栈

### 后端
- Spring Boot 2.0.1 (apiservice)
- Spring MVC 4.1.0 (adminbak)
- Node.js 18 + Koa 2.3.0 + Socket.io 2.3.0 (imsvr)

### 前端
- 纯静态HTML (h5目录)

### 数据库
- MySQL 8.0
- Redis 6.2-alpine
- MongoDB 5.0

### 容器化
- Docker
- Docker Compose
- Nginx 1.24-alpine

### 构建工具
- Maven 3.8
- npm (淘宝镜像)
- PM2 (进程管理)

---

## ⚠️ 注意事项

1. **环境要求**: 脚本适配标准Linux环境（CentOS 7+/Ubuntu 20.04+）
2. **零人工干预**: 执行脚本后无需任何手动操作
3. **版本固定**: 数据库版本严格按照要求（MySQL 8.0、MongoDB 5.0、Redis 6.2-alpine）
4. **源码来源**: 100%基于 https://github.com/ffsz1/labei.git
5. **配置固化**: 所有密码和配置已固化在脚本中，不接受参数输入

---

## 🔧 故障排查

### 如果服务启动失败

```bash
# 查看具体服务日志
docker-compose logs -f apiservice
docker-compose logs -f imsvr
docker-compose logs -f mysql

# 重新构建并启动
docker-compose down
docker-compose up -d --build --force-recreate
```

### 如果数据库连接失败

```bash
# 检查MySQL是否就绪
docker exec labei-mysql mysqladmin ping -h localhost -uroot -pLaBei_MySQL_2025

# 检查Redis是否就绪
docker exec labei-redis redis-cli -a LaBei_Redis_2025 ping

# 检查MongoDB是否就绪
docker exec labei-mongodb mongosh --eval "db.adminCommand('ping')"
```

---

## 📞 责任界定

根据用户要求，若执行脚本后出现以下问题，由脚本开发者无条件修改：
- 版本不匹配
- 配置错误
- 服务启动失败
- 无法访问

不接受"环境差异"或"需要手动调整"等借口。

---

## 📄 许可证

本脚本基于 https://github.com/ffsz1/labei.git 开发，遵循原项目许可证。

---

## 📅 版本信息

- **版本**: 1.0
- **创建日期**: 2025-11-11
- **GitHub仓库**: https://github.com/ffsz1/labei.git
- **脚本名称**: labei-docker-deploy.sh

---

**🎉 祝部署顺利！**
