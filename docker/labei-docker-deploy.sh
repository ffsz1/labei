#!/bin/bash
###############################################################################
# 拉贝星球项目 - Docker一键部署脚本
# GitHub仓库: https://github.com/ffsz1/labei.git
###############################################################################

set -e

RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[1;33m'; BLUE='\033[0;34m'; NC='\033[0m'
log_info() { echo -e "${GREEN}[INFO]${NC} $1"; }
log_error() { echo -e "${RED}[ERROR]${NC} $1"; }
log_step() { echo -e "${BLUE}[STEP]${NC} $1"; }

MYSQL_PASSWORD="LaBei_MySQL_2025"
REDIS_PASSWORD="LaBei_Redis_2025"
MONGO_USER="labei_mongo"
MONGO_PASSWORD="LaBei_Mongo_2025"
GITHUB_REPO="https://github.com/ffsz1/labei.git"
PROJECT_DIR="labei"

log_step "步骤1: 检查系统依赖..."
for cmd in docker git node npm; do
    command -v $cmd &> /dev/null || { log_error "$cmd 未安装"; exit 1; }
done
docker compose version &> /dev/null && COMPOSE_CMD="docker compose" || COMPOSE_CMD="docker-compose"
log_info "✅ 依赖检查通过"

log_step "步骤2: 清理旧环境..."
[ -d "$PROJECT_DIR" ] && rm -rf "$PROJECT_DIR"

log_step "步骤3: 拉取GitHub源码..."
git clone "$GITHUB_REPO" "$PROJECT_DIR"
cd "$PROJECT_DIR"

log_step "步骤4-6: 创建配置文件..."
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
      - ./labei.sql:/docker-entrypoint-initdb.d/labei.sql
    command: --default-authentication-plugin=mysql_native_password --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci
    networks:
      - labei-network
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
  apiservice:
    build:
      context: ./apiservice
      dockerfile: Dockerfile
    container_name: labei-apiservice
    restart: always
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/labei?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useSSL=false
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: LaBei_MySQL_2025
      SPRING_REDIS_HOST: redis
      SPRING_REDIS_PORT: 6379
      SPRING_REDIS_PASSWORD: LaBei_Redis_2025
      TZ: Asia/Shanghai
    depends_on:
      - mysql
      - redis
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
      - mysql
      - redis
      - mongodb
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

cat > nginx.conf << 'NGINX_EOF'
user nginx;
worker_processes auto;
error_log /var/log/nginx/error.log warn;
pid /var/run/nginx.pid;
events {
    worker_connections 1024;
}
http {
    include /etc/nginx/mime.types;
    default_type application/octet-stream;
    sendfile on;
    keepalive_timeout 65;
    gzip on;
    server {
        listen 80;
        server_name _;
        location /h5 {
            alias /usr/share/nginx/html/h5;
            index index.html;
            try_files $uri $uri/ /h5/index.html;
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
    }
}
NGINX_EOF

cat > labei.sql << 'SQL_EOF'
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
CREATE DATABASE IF NOT EXISTS `labei` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `labei`;
CREATE TABLE IF NOT EXISTS `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` bigint(20) NOT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `nickname` varchar(50) DEFAULT NULL,
  `status` tinyint(1) DEFAULT '1',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_uid` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE IF NOT EXISTS `t_admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `status` tinyint(1) DEFAULT '1',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
INSERT INTO `t_admin` (`username`, `password`, `mobile`, `status`) VALUES
('13800138000', 'e10adc3949ba59abbe56e057f20f883e', '13800138000', 1)
ON DUPLICATE KEY UPDATE `username`=VALUES(`username`);
SET FOREIGN_KEY_CHECKS = 1;
SQL_EOF

log_step "步骤7-8: 创建Dockerfile..."
cat > apiservice/Dockerfile << 'API_EOF'
FROM maven:3.8-openjdk-8 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -Ptest -DskipTests -q
FROM openjdk:8-jre-alpine
RUN apk add --no-cache tzdata && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
WORKDIR /app
COPY --from=builder /app/xchat-web-api/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=test", "-jar", "app.jar"]
API_EOF

cat > imsvr/Dockerfile << 'IM_EOF'
FROM node:18-alpine
RUN npm install -g pm2
WORKDIR /app
COPY package*.json ./
RUN npm install --registry=https://registry.npmmirror.com --production
COPY . .
RUN sed -i "s/mysql密碼/LaBei_MySQL_2025/g" config/development.js && \
    sed -i "s/redis密碼/LaBei_Redis_2025/g" config/development.js && \
    sed -i "s/yingtao/labei/g" config/development.js
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
IM_EOF

log_step "步骤11: 启动Docker服务..."
$COMPOSE_CMD up -d --build

log_step "步骤12: 等待服务就绪..."
sleep 120

log_step "步骤13: 验证部署..."
echo "========================================="
echo "验证1: Docker服务状态"
echo "========================================="
$COMPOSE_CMD ps

echo ""
echo "========================================="
echo "验证2: MySQL数据库"
echo "========================================="
docker exec labei-mysql mysql -uroot -p$MYSQL_PASSWORD -e "USE labei; SHOW TABLES;" 2>/dev/null || echo "MySQL验证失败"

echo ""
echo "========================================="
echo "验证3: 数据库镜像版本"
echo "========================================="
docker images | grep -E "mysql|mongo|redis"

echo ""
echo "========================================="
echo "🎉 部署完成！"
echo "========================================="
echo "访问地址: http://localhost/h5"
echo "数据库信息:"
echo "  MySQL: root / $MYSQL_PASSWORD @ localhost:3306/labei"
echo "  Redis: $REDIS_PASSWORD @ localhost:6379"
echo "  MongoDB: $MONGO_USER / $MONGO_PASSWORD @ localhost:27017/artqiyi"
echo "========================================="
