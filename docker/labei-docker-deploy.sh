#!/bin/bash
###############################################################################
# 拉贝星球项目 - Docker一键部署脚本（完整版 v2.0）
# GitHub仓库: https://github.com/ffsz1/labei.git
# 版本: 2.0 - 100%零人工干预 + 完整配置内置
# 创建日期: 2025-11-11
###############################################################################

set -e

# 颜色定义
RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[1;33m'; BLUE='\033[0;34m'; CYAN='\033[0;36m'; NC='\033[0m'

# 日志函数
log_info() { echo -e "${GREEN}[INFO]${NC} $1"; }
log_error() { echo -e "${RED}[ERROR]${NC} $1"; exit 1; }
log_step() { echo -e "${BLUE}[STEP]${NC} $1"; }
log_success() { echo -e "${CYAN}[SUCCESS]${NC} $1"; }
log_warn() { echo -e "${YELLOW}[WARN]${NC} $1"; }

# 固化配置（严禁修改）
readonly MYSQL_VERSION="8.0"
readonly REDIS_VERSION="6.2-alpine"
readonly MONGO_VERSION="5.0"
readonly MYSQL_PASSWORD="LaBei_MySQL_2025"
readonly REDIS_PASSWORD="LaBei_Redis_2025"
readonly MONGO_USER="labei_mongo"
readonly MONGO_PASSWORD="LaBei_Mongo_2025"
readonly MYSQL_DATABASE="labei"
readonly MONGO_DATABASE="artqiyi"
readonly GITHUB_REPO="https://github.com/ffsz1/labei.git"
readonly PROJECT_DIR="labei"
readonly ADMIN_USERNAME="13800138000"

echo "========================================="
echo "  拉贝星球 Docker 一键部署脚本 v2.0"
echo "  GitHub: ${GITHUB_REPO}"
echo "========================================="
echo ""

###############################################################################
# 步骤1/13: 检查系统依赖
###############################################################################
log_step "步骤1/13: 检查系统依赖..."
command -v docker &> /dev/null || log_error "Docker 未安装"
docker compose version &> /dev/null 2>&1 && COMPOSE_CMD="docker compose" || COMPOSE_CMD="docker-compose"
command -v $COMPOSE_CMD &> /dev/null || log_error "Docker Compose 未安装"
command -v git &> /dev/null || log_error "Git 未安装"
command -v node &> /dev/null || log_error "Node.js 未安装"
command -v npm &> /dev/null || log_error "npm 未安装"
log_success "所有依赖检查通过！"
echo ""

###############################################################################
# 步骤2/13: 清理旧环境
###############################################################################
log_step "步骤2/13: 清理旧环境..."
if [ -d "$PROJECT_DIR" ]; then
    cd "$PROJECT_DIR" 2>/dev/null && $COMPOSE_CMD down -v 2>/dev/null || true
    cd ..
    rm -rf "$PROJECT_DIR"
    log_info "✅ 旧环境已清理"
fi
echo ""

###############################################################################
# 步骤3/13: 拉取GitHub源码
###############################################################################
log_step "步骤3/13: 拉取GitHub源码..."
git clone "$GITHUB_REPO" "$PROJECT_DIR" || log_error "源码克隆失败"
cd "$PROJECT_DIR" || log_error "无法进入项目目录"
log_success "源码拉取成功！"
echo ""
###############################################################################
# 步骤4/13: 创建 docker-compose.yml
###############################################################################
log_step "步骤4/13: 创建 docker-compose.yml..."
cat > docker-compose.yml << 'COMPOSE_EOF'
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    container_name: labei-mysql
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: LaBei_MySQL_2025
      MYSQL_DATABASE: labei
      TZ: Asia/Shanghai
    ports:
      - "3306:3306"
    volumes:
      - mysql-data:/var/lib/mysql
      - ./labei.sql:/docker-entrypoint-initdb.d/labei.sql:ro
    command: --default-authentication-plugin=mysql_native_password --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci
    networks:
      - labei-network
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost", "-uroot", "-pLaBei_MySQL_2025"]
      interval: 10s
      timeout: 5s
      retries: 10
      start_period: 30s
  redis:
    image: redis:6.2-alpine
    container_name: labei-redis
    restart: always
    command: redis-server --requirepass LaBei_Redis_2025 --appendonly yes
    ports:
      - "6379:6379"
    volumes:
      - redis-data:/data
    networks:
      - labei-network
    healthcheck:
      test: ["CMD", "redis-cli", "-a", "LaBei_Redis_2025", "ping"]
      interval: 10s
      timeout: 5s
      retries: 5
  mongodb:
    image: mongo:5.0
    container_name: labei-mongodb
    restart: always
    environment:
      MONGO_INITDB_ROOT_USERNAME: labei_mongo
      MONGO_INITDB_ROOT_PASSWORD: LaBei_Mongo_2025
      MONGO_INITDB_DATABASE: artqiyi
      TZ: Asia/Shanghai
    ports:
      - "27017:27017"
    volumes:
      - mongodb-data:/data/db
    networks:
      - labei-network
    healthcheck:
      test: ["CMD", "mongosh", "--quiet", "--eval", "db.adminCommand('ping').ok"]
      interval: 10s
      timeout: 5s
      retries: 5
      start_period: 20s
  apiservice:
    build:
      context: ./apiservice
      dockerfile: Dockerfile
    container_name: labei-apiservice
    restart: always
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/labei?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useSSL=false&serverTimezone=Asia/Shanghai
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: LaBei_MySQL_2025
      SPRING_REDIS_HOST: redis
      SPRING_REDIS_PORT: 6379
      SPRING_REDIS_PASSWORD: LaBei_Redis_2025
      TZ: Asia/Shanghai
    depends_on:
      mysql:
        condition: service_healthy
      redis:
        condition: service_healthy
    networks:
      - labei-network
  imsvr:
    build:
      context: ./imsvr
      dockerfile: Dockerfile
    container_name: labei-imsvr
    restart: always
    ports:
      - "3000:3000"
      - "3001:3001"
      - "3002:3002"
      - "3003:3003"
    environment:
      MYSQL_HOST: mysql
      MYSQL_PASSWORD: LaBei_MySQL_2025
      MYSQL_DATABASE: labei
      REDIS_HOST: redis
      REDIS_PASSWORD: LaBei_Redis_2025
      MONGO_HOST: mongodb
      MONGO_USER: labei_mongo
      MONGO_PASSWORD: LaBei_Mongo_2025
      MONGO_DATABASE: artqiyi
      NODE_ENV: production
      TZ: Asia/Shanghai
    depends_on:
      mysql:
        condition: service_healthy
      redis:
        condition: service_healthy
      mongodb:
        condition: service_healthy
    networks:
      - labei-network
  nginx:
    image: nginx:1.24-alpine
    container_name: labei-nginx
    restart: always
    ports:
      - "80:80"
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf:ro
      - ./h5:/usr/share/nginx/html/h5:ro
      - ./adminbak:/usr/share/nginx/html/admin:ro
    depends_on:
      - apiservice
      - imsvr
    networks:
      - labei-network
volumes:
  mysql-data:
  redis-data:
  mongodb-data:
networks:
  labei-network:
    driver: bridge
COMPOSE_EOF
log_success "docker-compose.yml 创建成功！"
echo ""
###############################################################################
# 步骤5/13: 创建 nginx.conf
###############################################################################
log_step "步骤5/13: 创建 nginx.conf..."
cat > nginx.conf << 'NGINX_EOF'
user nginx;
worker_processes auto;
error_log /var/log/nginx/error.log warn;
pid /var/run/nginx.pid;
events {
    worker_connections 2048;
}
http {
    include /etc/nginx/mime.types;
    default_type application/octet-stream;
    sendfile on;
    keepalive_timeout 65;
    client_max_body_size 100M;
    gzip on;
    server {
        listen 80;
        server_name _;
        location /h5 {
            alias /usr/share/nginx/html/h5;
            index index.html;
            try_files $uri $uri/ /h5/index.html;
        }
        location /admin {
            alias /usr/share/nginx/html/admin;
            index index.html;
            try_files $uri $uri/ /admin/index.html;
        }
        location /api/ {
            proxy_pass http://apiservice:8080/;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
        }
        location /im/ {
            proxy_pass http://imsvr:3000/;
            proxy_http_version 1.1;
            proxy_set_header Upgrade $http_upgrade;
            proxy_set_header Connection "upgrade";
        }
        location /socket.io/ {
            proxy_pass http://imsvr:3002/socket.io/;
            proxy_http_version 1.1;
            proxy_set_header Upgrade $http_upgrade;
            proxy_set_header Connection "upgrade";
        }
        location /health {
            return 200 "healthy\n";
        }
    }
}
NGINX_EOF
log_success "nginx.conf 创建成功！"
echo ""

###############################################################################
# 步骤6/13: 创建 labei.sql（6张核心表）
###############################################################################
log_step "步骤6/13: 创建 labei.sql（6张核心表）..."
cat > labei.sql << 'SQL_EOF'
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
CREATE DATABASE IF NOT EXISTS `labei` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `labei`;

DROP TABLE IF EXISTS `t_admin`;
CREATE TABLE `t_admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `status` tinyint(1) DEFAULT '1',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';
INSERT INTO `t_admin` (`username`, `password`, `mobile`, `status`) VALUES ('13800138000', 'e10adc3949ba59abbe56e057f20f883e', '13800138000', 1);

DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` bigint(20) NOT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `nickname` varchar(50) DEFAULT NULL,
  `status` tinyint(1) DEFAULT '1',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_uid` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

DROP TABLE IF EXISTS `t_room`;
CREATE TABLE `t_room` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `room_id` bigint(20) NOT NULL,
  `room_name` varchar(100) NOT NULL,
  `owner_uid` bigint(20) NOT NULL,
  `status` tinyint(1) DEFAULT '1',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_room_id` (`room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='房间表';

DROP TABLE IF EXISTS `t_gift`;
CREATE TABLE `t_gift` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gift_name` varchar(50) NOT NULL,
  `gift_price` decimal(10,2) NOT NULL,
  `status` tinyint(1) DEFAULT '1',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='礼物表';

DROP TABLE IF EXISTS `t_message`;
CREATE TABLE `t_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `from_uid` bigint(20) NOT NULL,
  `to_uid` bigint(20) NOT NULL,
  `content` text,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息表';

DROP TABLE IF EXISTS `t_config`;
CREATE TABLE `t_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `config_key` varchar(100) NOT NULL,
  `config_value` text,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='配置表';
INSERT INTO `t_config` (`config_key`, `config_value`) VALUES ('site_name', '拉贝星球'), ('site_status', '1');

SET FOREIGN_KEY_CHECKS = 1;
SQL_EOF
log_success "labei.sql 创建成功！（6张核心表）"
echo ""

###############################################################################
# 步骤7/13: 创建 apiservice/Dockerfile
###############################################################################
log_step "步骤7/13: 创建 apiservice/Dockerfile..."
cat > apiservice/Dockerfile << 'API_DOCKERFILE_EOF'
FROM maven:3.8-openjdk-8 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -Ptest -DskipTests -q

FROM openjdk:8-jre-alpine
RUN apk add --no-cache tzdata curl && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
WORKDIR /app
COPY --from=builder /app/xchat-web-api/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=test", "-jar", "app.jar"]
API_DOCKERFILE_EOF
log_success "apiservice/Dockerfile 创建成功！"
echo ""

###############################################################################
# 步骤8/13: 创建 imsvr/Dockerfile
###############################################################################
log_step "步骤8/13: 创建 imsvr/Dockerfile..."
cat > imsvr/Dockerfile << 'IM_DOCKERFILE_EOF'
FROM node:18-alpine
RUN apk add --no-cache tzdata && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN npm install -g pm2@latest --registry=https://registry.npmmirror.com
WORKDIR /app
COPY package*.json ./
RUN npm install --production --registry=https://registry.npmmirror.com
COPY . .
RUN sed -i 's/mysql密碼/LaBei_MySQL_2025/g' config/development.js && \
    sed -i 's/redis密碼/LaBei_Redis_2025/g' config/development.js && \
    sed -i 's/yingtao/labei/g' config/development.js
RUN cat > ecosystem.config.js << 'PM2_EOF'
module.exports = {
  apps: [
    { name: 'im-svr-3000', script: 'app.js', instances: 1, env: { NODE_ENV: 'production', PORT: 3000 } },
    { name: 'http-svr-3001', script: 'http.js', instances: 1, env: { NODE_ENV: 'production', PORT: 3001 } },
    { name: 'cron-svr-3003', script: 'cron.js', instances: 1, env: { NODE_ENV: 'production', PORT: 3003 } }
  ]
};
PM2_EOF
EXPOSE 3000 3001 3002 3003
CMD ["pm2-runtime", "start", "ecosystem.config.js"]
IM_DOCKERFILE_EOF
log_success "imsvr/Dockerfile 创建成功！"
echo ""
###############################################################################
# 步骤9/13: 修复配置文件占位符
###############################################################################
log_step "步骤9/13: 修复配置文件占位符..."
[ -f "imsvr/config/development.js" ] && sed -i.bak "s/mysql密碼/${MYSQL_PASSWORD}/g; s/redis密碼/${REDIS_PASSWORD}/g; s/yingtao/${MYSQL_DATABASE}/g" imsvr/config/development.js && log_info "✅ imsvr配置已修复"
[ -f "apiservice/pom.xml" ] && sed -i.bak "s/mysqlµØÖ·/127.0.0.1/g; s/mysqlÃÜÂë/${MYSQL_PASSWORD}/g" apiservice/pom.xml && log_info "✅ apiservice配置已修复"
log_success "配置文件修复完成！"
echo ""

###############################################################################
# 步骤10/13: 检查源码完整性
###############################################################################
log_step "步骤10/13: 检查源码完整性..."
for dir in h5 apiservice imsvr; do
    [ -d "$dir" ] && log_info "✅ $dir" || log_error "❌ 缺少 $dir"
done
log_success "源码完整性检查通过！"
echo ""

###############################################################################
# 步骤11/13: 启动Docker服务
###############################################################################
log_step "步骤11/13: 启动Docker服务（构建+启动）..."
log_info "正在构建镜像并启动服务，预计需要10-15分钟..."
$COMPOSE_CMD up -d --build --force-recreate || log_error "Docker服务启动失败"
log_success "Docker服务启动成功！"
echo ""

###############################################################################
# 步骤12/13: 等待服务就绪
###############################################################################
log_step "步骤12/13: 等待服务就绪（120秒）..."
for i in {1..120}; do
    echo -ne "\r等待进度: $i/120 秒 "
    sleep 1
done
echo ""
log_success "等待完成！"
echo ""

###############################################################################
# 步骤13/13: 验证部署结果
###############################################################################
log_step "步骤13/13: 验证部署结果..."
echo ""
echo "========================================="
echo "  验证1: Docker服务状态"
echo "========================================="
$COMPOSE_CMD ps
echo ""

echo "========================================="
echo "  验证2: 数据库镜像版本"
echo "========================================="
echo "MySQL 版本（要求: ${MYSQL_VERSION}）:"
docker images | grep mysql | grep "${MYSQL_VERSION}" || log_warn "⚠️  MySQL版本不匹配"
echo "Redis 版本（要求: ${REDIS_VERSION}）:"
docker images | grep redis | grep "${REDIS_VERSION}" || log_warn "⚠️  Redis版本不匹配"
echo "MongoDB 版本（要求: ${MONGO_VERSION}）:"
docker images | grep mongo | grep "${MONGO_VERSION}" || log_warn "⚠️  MongoDB版本不匹配"
echo ""

echo "========================================="
echo "  验证3: MySQL数据库表检查"
echo "========================================="
if docker exec labei-mysql mysql -uroot -p${MYSQL_PASSWORD} -e "USE ${MYSQL_DATABASE}; SHOW TABLES;" 2>/dev/null; then
    log_success "✅ 数据库表创建成功！"
    TABLE_COUNT=$(docker exec labei-mysql mysql -uroot -p${MYSQL_PASSWORD} -e "USE ${MYSQL_DATABASE}; SHOW TABLES;" 2>/dev/null | wc -l)
    TABLE_COUNT=$((TABLE_COUNT - 1))
    log_info "数据库表数量: ${TABLE_COUNT} 张（预期: 6张）"
    echo ""
    docker exec labei-mysql mysql -uroot -p${MYSQL_PASSWORD} -e "USE ${MYSQL_DATABASE}; SHOW TABLES;" 2>/dev/null
else
    log_warn "⚠️  数据库表检查失败"
fi
echo ""

echo "========================================="
echo "  验证4: 服务健康检查"
echo "========================================="
docker exec labei-mysql mysqladmin ping -h localhost -uroot -p${MYSQL_PASSWORD} 2>/dev/null | grep -q "alive" && log_success "✅ MySQL 正常" || log_warn "⚠️  MySQL 异常"
docker exec labei-redis redis-cli -a ${REDIS_PASSWORD} ping 2>/dev/null | grep -q "PONG" && log_success "✅ Redis 正常" || log_warn "⚠️  Redis 异常"
docker exec labei-mongodb mongosh --quiet --eval "db.adminCommand('ping').ok" 2>/dev/null | grep -q "1" && log_success "✅ MongoDB 正常" || log_warn "⚠️  MongoDB 异常"
echo ""

###############################################################################
# 部署完成总结
###############################################################################
echo ""
echo "========================================="
echo "  🎉 部署完成！"
echo "========================================="
echo ""
echo "📊 服务信息:"
echo "  - MySQL ${MYSQL_VERSION}:    localhost:3306"
echo "  - Redis ${REDIS_VERSION}:  localhost:6379"
echo "  - MongoDB ${MONGO_VERSION}:  localhost:27017"
echo "  - API服务:       localhost:8080"
echo "  - IM服务:        localhost:3000-3003"
echo "  - Nginx:         localhost:80"
echo ""
echo "🔐 数据库账号密码:"
echo "  - MySQL:   root / ${MYSQL_PASSWORD}"
echo "  - Redis:   ${REDIS_PASSWORD}"
echo "  - MongoDB: ${MONGO_USER} / ${MONGO_PASSWORD}"
echo ""
echo "🌐 访问地址:"
echo "  - H5前端:     http://localhost/h5"
echo "  - 管理后台:   http://localhost/admin"
echo ""
echo "👤 管理后台登录:"
echo "  - 账号: ${ADMIN_USERNAME}"
echo "  - 密码: 123456"
echo ""
echo "📝 常用命令:"
echo "  - 查看服务状态:   $COMPOSE_CMD ps"
echo "  - 查看服务日志:   $COMPOSE_CMD logs -f [服务名]"
echo "  - 重启服务:       $COMPOSE_CMD restart"
echo "  - 停止服务:       $COMPOSE_CMD down"
echo "========================================="
echo ""

RUNNING_CONTAINERS=$($COMPOSE_CMD ps --services --filter "status=running" | wc -l)
TOTAL_CONTAINERS=$($COMPOSE_CMD ps --services | wc -l)

if [ "$RUNNING_CONTAINERS" -eq "$TOTAL_CONTAINERS" ]; then
    log_success "✅ 所有服务 ($RUNNING_CONTAINERS/$TOTAL_CONTAINERS) 已成功启动！"
    exit 0
else
    log_warn "⚠️  部分服务未启动 ($RUNNING_CONTAINERS/$TOTAL_CONTAINERS)，请检查日志"
    exit 1
fi