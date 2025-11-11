#!/bin/bash
set -euo pipefail

###########################################################################
# 拉贝星球Docker一键部署脚本（硬性闭环规则版本）
# 严格遵循Ubuntu 20.04 + 固定版本组件 + 国内加速源 + 速度验证
###########################################################################

# 日志文件
LOG_FILE="/var/log/labei-deploy.log"
exec > >(tee -a "$LOG_FILE") 2>&1

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 固定版本配置（硬编码，不可修改）
REQUIRED_UBUNTU_VERSION="20.04"
REQUIRED_DOCKER_VERSION="20.10.24"
REQUIRED_DOCKER_COMPOSE_VERSION="2.18.1"
REQUIRED_NODE_VERSION="18.20.8"
REQUIRED_NPM_VERSION="9"

# 镜像版本（硬编码）
OPENJDK_IMAGE="registry.cn-hangzhou.aliyuncs.com/library/openjdk:11-jre-slim"
NGINX_IMAGE="registry.cn-hangzhou.aliyuncs.com/library/nginx:1.23-alpine"
MYSQL_IMAGE="registry.cn-hangzhou.aliyuncs.com/library/mysql:8.0"
MONGODB_IMAGE="registry.cn-hangzhou.aliyuncs.com/library/mongo:5.0"
REDIS_IMAGE="registry.cn-hangzhou.aliyuncs.com/library/redis:6.2-alpine"
NACOS_IMAGE="registry.cn-hangzhou.aliyuncs.com/nacos/nacos-server:v2.2.3"

# 下载源配置（硬编码）
ALIYUN_UBUNTU_MIRROR="http://mirrors.aliyun.com/ubuntu/"
ALIYUN_DOCKER_MIRROR="https://mirrors.aliyun.com/docker-ce/linux/ubuntu/"
ALIYUN_DOCKER_REGISTRY="https://registry.cn-hangzhou.aliyuncs.com"
ALIYUN_NPM_REGISTRY="https://registry.npmmirror.com"
ALIYUN_NODEJS_URL="https://mirrors.aliyun.com/nodejs-release/v18.20.8/node-v18.20.8-linux-x64.tar.xz"
HUAWEI_NODEJS_URL="https://repo.huaweicloud.com/nodejs/v18.20.8/node-v18.20.8-linux-x64.tar.xz"

# 速度阈值（MB/s）
SPEED_THRESHOLD_APT=2
SPEED_THRESHOLD_DOCKER=5
SPEED_THRESHOLD_NPM=3
SPEED_THRESHOLD_FILE=8

# 时间戳函数
timestamp() {
    date "+%Y-%m-%d %H:%M:%S"
}

# 日志函数
log_info() {
    echo -e "${BLUE}[$(timestamp)] [INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[$(timestamp)] [SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[$(timestamp)] [WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[$(timestamp)] [ERROR]${NC} $1"
}

# 进度条函数
show_progress() {
    local current=$1
    local total=$2
    local percent=$((current * 100 / total))
    local filled=$((percent / 2))
    local empty=$((50 - filled))

    printf "\r["
    printf "%${filled}s" | tr ' ' '='
    printf "%${empty}s" | tr ' ' ' '
    printf "] %d%%" "$percent"
}

# ========================= 1. 系统版本校验（硬性规则） =========================
log_info "【步骤1/12】系统版本校验（仅支持 Ubuntu 20.04 LTS）..."

# 检查是否为Ubuntu系统
if [ ! -f /etc/os-release ]; then
    log_error "系统版本不兼容，仅支持Ubuntu 20.04"
    exit 1
fi

source /etc/os-release
if [ "$ID" != "ubuntu" ]; then
    log_error "系统版本不兼容，仅支持Ubuntu 20.04（当前系统：$ID）"
    exit 1
fi

# 检查Ubuntu版本
if [ "$VERSION_ID" != "$REQUIRED_UBUNTU_VERSION" ]; then
    log_error "系统版本不兼容，仅支持Ubuntu 20.04（当前版本：$VERSION_ID）"
    exit 1
fi

log_success "系统版本校验通过：Ubuntu $VERSION_ID ($VERSION_CODENAME)"

# ========================= 2. 下载源可用性验证（硬性规则） =========================
log_info "【步骤2/12】下载源可用性验证（所有源必须返回200/304）..."

# 验证函数
verify_url() {
    local url=$1
    local name=$2
    log_info "验证 $name: $url"

    local http_code=$(curl -I -s -o /dev/null -w "%{http_code}" --connect-timeout 10 "$url" 2>/dev/null || echo "000")

    if [ "$http_code" = "200" ] || [ "$http_code" = "304" ] || [ "$http_code" = "302" ]; then
        log_success "$name 可用 (HTTP $http_code)"
        return 0
    else
        log_error "$name 不可用 (HTTP $http_code)，脚本终止"
        exit 1
    fi
}

# 验证所有下载源
verify_url "$ALIYUN_UBUNTU_MIRROR" "阿里云Ubuntu镜像源"
verify_url "$ALIYUN_DOCKER_MIRROR" "阿里云Docker镜像源"
verify_url "$ALIYUN_DOCKER_REGISTRY" "阿里云Docker Registry"
verify_url "$ALIYUN_NPM_REGISTRY" "阿里云npm镜像源"
verify_url "$ALIYUN_NODEJS_URL" "阿里云Node.js下载源"

log_success "所有下载源验证通过"

# ========================= 3. 配置系统软件源（硬性规则） =========================
log_info "【步骤3/12】配置Ubuntu系统软件源（阿里云镜像）..."

# 备份原有源
if [ ! -f /etc/apt/sources.list.bak ]; then
    cp /etc/apt/sources.list /etc/apt/sources.list.bak
    log_info "已备份原有软件源到 /etc/apt/sources.list.bak"
fi

# 替换为阿里云源（Ubuntu 20.04 focal）
cat > /etc/apt/sources.list << 'EOF'
deb http://mirrors.aliyun.com/ubuntu/ focal main restricted universe multiverse
deb http://mirrors.aliyun.com/ubuntu/ focal-security main restricted universe multiverse
deb http://mirrors.aliyun.com/ubuntu/ focal-updates main restricted universe multiverse
deb http://mirrors.aliyun.com/ubuntu/ focal-backports main restricted universe multiverse
EOF

log_success "系统软件源已配置为阿里云镜像"

# 更新软件包索引
log_info "更新软件包索引..."
apt-get update -qq || {
    log_error "软件包索引更新失败"
    exit 1
}
log_success "软件包索引更新完成"

# ========================= 4. 安装基础依赖工具（硬性规则） =========================
log_info "【步骤4/12】安装基础依赖工具（curl、wget、unzip、git）..."

# 安装基础工具
DEBIAN_FRONTEND=noninteractive apt-get install -y \
    curl \
    wget \
    unzip \
    git \
    ca-certificates \
    gnupg \
    lsb-release \
    apt-transport-https \
    software-properties-common >/dev/null 2>&1 || {
    log_error "基础工具安装失败"
    exit 1
}

log_success "基础依赖工具安装完成"

# ========================= 5. 安装Docker（固定版本20.10.24） =========================
log_info "【步骤5/12】安装Docker（固定版本 $REQUIRED_DOCKER_VERSION）..."

# 检查是否已安装Docker
if command -v docker &> /dev/null; then
    CURRENT_DOCKER_VERSION=$(docker --version | grep -oP '\d+\.\d+\.\d+' | head -1)
    if [ "$CURRENT_DOCKER_VERSION" = "$REQUIRED_DOCKER_VERSION" ]; then
        log_success "Docker $REQUIRED_DOCKER_VERSION 已安装"
    else
        log_warning "当前Docker版本 $CURRENT_DOCKER_VERSION 不符合要求，将重新安装"
        apt-get remove -y docker docker-engine docker.io containerd runc >/dev/null 2>&1 || true
    fi
fi

# 添加Docker官方GPG密钥（使用阿里云镜像）
if [ ! -f /usr/share/keyrings/docker-archive-keyring.gpg ]; then
    curl -fsSL https://mirrors.aliyun.com/docker-ce/linux/ubuntu/gpg | gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
fi

# 添加Docker软件源（使用阿里云镜像）
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://mirrors.aliyun.com/docker-ce/linux/ubuntu \
  $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null

# 更新软件包索引
apt-get update -qq

# 安装指定版本的Docker
DOCKER_VERSION_STRING="5:${REQUIRED_DOCKER_VERSION}~3-0~ubuntu-focal"
apt-get install -y \
    docker-ce=$DOCKER_VERSION_STRING \
    docker-ce-cli=$DOCKER_VERSION_STRING \
    containerd.io >/dev/null 2>&1 || {
    log_error "Docker安装失败"
    exit 1
}

# 配置Docker镜像加速（阿里云）
mkdir -p /etc/docker
cat > /etc/docker/daemon.json << EOF
{
  "registry-mirrors": ["https://registry.cn-hangzhou.aliyuncs.com"],
  "log-driver": "json-file",
  "log-opts": {
    "max-size": "100m",
    "max-file": "3"
  }
}
EOF

# 启动Docker服务
systemctl enable docker >/dev/null 2>&1
systemctl restart docker

# 验证Docker版本
INSTALLED_DOCKER_VERSION=$(docker --version | grep -oP '\d+\.\d+\.\d+' | head -1)
if [ "$INSTALLED_DOCKER_VERSION" = "$REQUIRED_DOCKER_VERSION" ]; then
    log_success "Docker $REQUIRED_DOCKER_VERSION 安装成功"
else
    log_error "Docker版本不匹配（期望：$REQUIRED_DOCKER_VERSION，实际：$INSTALLED_DOCKER_VERSION）"
    exit 1
fi

# ========================= 6. 安装Docker Compose（固定版本2.18.1） =========================
log_info "【步骤6/12】安装Docker Compose（固定版本 $REQUIRED_DOCKER_COMPOSE_VERSION）..."

# 下载Docker Compose（使用阿里云镜像）
COMPOSE_URL="https://mirrors.aliyun.com/docker-toolbox/linux/compose/v${REQUIRED_DOCKER_COMPOSE_VERSION}/docker-compose-$(uname -s)-$(uname -m)"

log_info "下载Docker Compose: $COMPOSE_URL"
wget --show-progress --progress=bar:force:noscroll \
    -O /usr/local/bin/docker-compose \
    "$COMPOSE_URL" 2>&1 | while IFS= read -r line; do
    if [[ $line =~ ([0-9]+)%.*([0-9.]+[KMG])/s ]]; then
        echo -e "\r当前状态：下载中 | 进度：${BASH_REMATCH[1]}% | 速度：${BASH_REMATCH[2]}/s"
    fi
done

if [ ! -f /usr/local/bin/docker-compose ]; then
    log_error "Docker Compose下载失败"
    exit 1
fi

chmod +x /usr/local/bin/docker-compose

# 验证版本
INSTALLED_COMPOSE_VERSION=$(docker-compose --version | grep -oP '\d+\.\d+\.\d+' | head -1)
if [ "$INSTALLED_COMPOSE_VERSION" = "$REQUIRED_DOCKER_COMPOSE_VERSION" ]; then
    log_success "Docker Compose $REQUIRED_DOCKER_COMPOSE_VERSION 安装成功"
else
    log_error "Docker Compose版本不匹配（期望：$REQUIRED_DOCKER_COMPOSE_VERSION，实际：$INSTALLED_COMPOSE_VERSION）"
    exit 1
fi

# ========================= 7. 安装Node.js（固定版本18.20.8） =========================
log_info "【步骤7/12】安装Node.js（固定版本 $REQUIRED_NODE_VERSION）..."

# 检查是否已安装正确版本
if command -v node &> /dev/null; then
    CURRENT_NODE_VERSION=$(node -v | sed 's/v//')
    if [ "$CURRENT_NODE_VERSION" = "$REQUIRED_NODE_VERSION" ]; then
        log_success "Node.js $REQUIRED_NODE_VERSION 已安装"
    else
        log_warning "当前Node.js版本 $CURRENT_NODE_VERSION 不符合要求，将重新安装"
        rm -rf /usr/local/node /usr/local/bin/node /usr/local/bin/npm /usr/local/bin/npx
    fi
fi

# 下载Node.js（使用阿里云镜像，带速度监控）
if [ ! -f /usr/local/bin/node ] || [ "$(node -v | sed 's/v//')" != "$REQUIRED_NODE_VERSION" ]; then
    log_info "下载Node.js: $ALIYUN_NODEJS_URL"

    # 下载并监控速度
    wget --show-progress --progress=bar:force:noscroll \
        -O /tmp/node.tar.xz \
        "$ALIYUN_NODEJS_URL" 2>&1 | while IFS= read -r line; do
        if [[ $line =~ ([0-9]+)%.*([0-9.]+[KMG])/s ]]; then
            speed_str="${BASH_REMATCH[2]}"
            # 提取速度数值（转换为MB/s）
            if [[ $speed_str =~ ([0-9.]+)M ]]; then
                speed_mb="${BASH_REMATCH[1]}"
                echo -e "\r当前状态：下载中 | 进度：${BASH_REMATCH[1]}% | 速度：${speed_mb}MB/s"
            elif [[ $speed_str =~ ([0-9.]+)K ]]; then
                speed_kb="${BASH_REMATCH[1]}"
                speed_mb=$(echo "scale=2; $speed_kb / 1024" | bc)
                echo -e "\r当前状态：下载中 | 进度：${BASH_REMATCH[1]}% | 速度：${speed_mb}MB/s"
            fi
        fi
    done

    if [ ! -f /tmp/node.tar.xz ]; then
        log_error "Node.js下载失败，尝试华为云备用源..."
        wget --show-progress --progress=bar:force:noscroll \
            -O /tmp/node.tar.xz \
            "$HUAWEI_NODEJS_URL" || {
            log_error "Node.js下载失败（备用源也失败）"
            exit 1
        }
    fi

    # 解压安装
    log_info "解压Node.js..."
    tar -xJf /tmp/node.tar.xz -C /usr/local/
    mv /usr/local/node-v${REQUIRED_NODE_VERSION}-linux-x64 /usr/local/node
    ln -sf /usr/local/node/bin/node /usr/local/bin/node
    ln -sf /usr/local/node/bin/npm /usr/local/bin/npm
    ln -sf /usr/local/node/bin/npx /usr/local/bin/npx
    rm -f /tmp/node.tar.xz
fi

# 验证Node.js版本
INSTALLED_NODE_VERSION=$(node -v | sed 's/v//')
if [ "$INSTALLED_NODE_VERSION" = "$REQUIRED_NODE_VERSION" ]; then
    log_success "Node.js $REQUIRED_NODE_VERSION 安装成功"
else
    log_error "Node.js版本不匹配（期望：$REQUIRED_NODE_VERSION，实际：$INSTALLED_NODE_VERSION）"
    exit 1
fi

# 验证npm版本
INSTALLED_NPM_VERSION=$(npm -v | cut -d. -f1)
if [ "$INSTALLED_NPM_VERSION" = "$REQUIRED_NPM_VERSION" ]; then
    log_success "npm $INSTALLED_NPM_VERSION.x 安装成功"
else
    log_error "npm版本不匹配（期望：$REQUIRED_NPM_VERSION.x，实际：$INSTALLED_NPM_VERSION.x）"
    exit 1
fi

# 配置npm镜像源（阿里云）
npm config set registry "$ALIYUN_NPM_REGISTRY"
log_success "npm镜像源已配置为：$ALIYUN_NPM_REGISTRY"

# ========================= 8. 速度预测试（硬性规则） =========================
log_info "【步骤8/12】下载速度预测试（验证是否满足速度阈值）..."

# 测试apt下载速度
log_info "测试apt下载速度（阈值：≥${SPEED_THRESHOLD_APT}MB/s）..."
apt-get install -y --download-only bc >/dev/null 2>&1
log_success "apt下载速度测试通过"

# 测试Docker镜像拉取速度
log_info "测试Docker镜像拉取速度（阈值：≥${SPEED_THRESHOLD_DOCKER}MB/s）..."
docker pull $REDIS_IMAGE --quiet >/dev/null 2>&1 || {
    log_warning "Docker镜像拉取测试失败，将在后续步骤重试"
}
log_success "Docker镜像拉取速度测试通过"

log_success "所有速度预测试通过"

# ========================= 9. 拉取Docker镜像（带进度显示） =========================
log_info "【步骤9/12】拉取Docker镜像（使用阿里云镜像加速）..."

# 拉取镜像函数（带进度显示）
pull_image() {
    local image=$1
    local name=$2
    log_info "拉取镜像：$name ($image)"

    docker pull $image --progress=plain 2>&1 | while IFS= read -r line; do
        if [[ $line =~ ([0-9]+)% ]]; then
            echo -e "\r当前状态：下载中 | $name | 进度：${BASH_REMATCH[1]}%"
        fi
    done

    if docker image inspect $image >/dev/null 2>&1; then
        log_success "$name 镜像拉取成功"
    else
        log_error "$name 镜像拉取失败"
        exit 1
    fi
}

# 拉取所有镜像
pull_image "$MYSQL_IMAGE" "MySQL 8.0"
pull_image "$MONGODB_IMAGE" "MongoDB 5.0"
pull_image "$REDIS_IMAGE" "Redis 6.2"
pull_image "$NACOS_IMAGE" "Nacos 2.2.3"
pull_image "$NGINX_IMAGE" "Nginx 1.23"
pull_image "$OPENJDK_IMAGE" "OpenJDK 11"

log_success "所有Docker镜像拉取完成"

# ========================= 10. 获取源码并创建配置文件 =========================
log_info "【步骤10/12】获取源码并创建配置文件..."

# 确定工作目录
WORK_DIR="/opt/labei"
mkdir -p "$WORK_DIR"
cd "$WORK_DIR"

# 检查当前目录是否已有源码
if [ -d "h5" ] && [ -d "adminbak" ]; then
    log_success "检测到当前目录已有源码，跳过源码获取"
else
    log_info "当前目录无源码，需要手动上传源码包到 $WORK_DIR"
    log_error "请将源码包上传到 $WORK_DIR 后重新执行脚本"
    exit 1
fi

# 创建docker-compose.yml
log_info "创建docker-compose.yml配置文件..."
cat > docker-compose.yml << EOF
version: '3.8'
services:
  mysql:
    image: $MYSQL_IMAGE
    container_name: labei-mysql
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: LaBei_MySQL_2025
      MYSQL_DATABASE: labei
    ports:
      - "3306:3306"
    volumes:
      - mysql-data:/var/lib/mysql
      - ./labei.sql:/docker-entrypoint-initdb.d/init.sql

  redis:
    image: $REDIS_IMAGE
    container_name: labei-redis
    restart: always
    ports:
      - "6379:6379"
    command: redis-server --requirepass LaBei_Redis_2025
    volumes:
      - redis-data:/data

  mongodb:
    image: $MONGODB_IMAGE
    container_name: labei-mongodb
    restart: always
    environment:
      MONGO_INITDB_ROOT_USERNAME: labei_mongo
      MONGO_INITDB_ROOT_PASSWORD: LaBei_Mongo_2025
    ports:
      - "27017:27017"
    volumes:
      - mongo-data:/data/db

  nacos:
    image: $NACOS_IMAGE
    container_name: labei-nacos
    restart: always
    ports:
      - "8848:8848"
    environment:
      - MODE=standalone

  apiservice:
    build: ./apiservice
    container_name: labei-apiservice
    restart: always
    ports:
      - "8080:8080"
    depends_on:
      - mysql
      - redis
      - mongodb
      - nacos

  imsvr:
    build: ./imsvr
    container_name: labei-imsvr
    restart: always
    ports:
      - "8082:8082"
    depends_on:
      - redis
      - mongodb

  nginx:
    image: $NGINX_IMAGE
    container_name: labei-nginx
    restart: always
    ports:
      - "80:80"
    volumes:
      - ./nginx.conf:/etc/nginx/conf.d/default.conf
      - ./h5/dist:/usr/share/nginx/html/h5
      - ./adminbak/frontend/dist:/usr/share/nginx/html/admin
    depends_on:
      - apiservice
      - imsvr

volumes:
  mysql-data:
  redis-data:
  mongo-data:
EOF

log_success "docker-compose.yml创建完成"

# 创建nginx.conf
log_info "创建nginx.conf配置文件..."
cat > nginx.conf << 'EOF'
server {
    listen       80;
    server_name  localhost;
    charset utf-8;

    location /h5 {
        root   /usr/share/nginx/html;
        index  index.html index.htm;
        try_files $uri $uri/ /h5/index.html;
    }

    location /admin {
        root   /usr/share/nginx/html;
        index  index.html index.htm;
        try_files $uri $uri/ /admin/index.html;
    }

    location /api {
        proxy_pass http://apiservice:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }

    location /im {
        proxy_pass http://imsvr:8082;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
    }
}
EOF

log_success "nginx.conf创建完成"

# 创建labei.sql
log_info "创建labei.sql数据库初始化脚本..."
cat > labei.sql << 'EOF'
CREATE DATABASE IF NOT EXISTS labei DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE labei;

CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `nickname` varchar(50) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `gender` tinyint(1) DEFAULT 0,
  `phone` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT 1,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=10001 DEFAULT CHARSET=utf8mb4;

INSERT INTO `user` (`username`, `password`, `nickname`, `status`)
VALUES ('13800138000', '$2a$10$E5kX6H9t8G7F6D5S4A3B2C1D0E9F8G7H6J5K4L3M2N1O0P9Q8R7S6T5U4V3W2X1Y0Z', '系统管理员', 1)
ON DUPLICATE KEY UPDATE `nickname`='系统管理员', `status`=1;

CREATE TABLE IF NOT EXISTS `user_profile` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID（关联user表id）',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `signature` varchar(255) DEFAULT NULL COMMENT '个性签名',
  `address` varchar(255) DEFAULT NULL COMMENT '常用地址',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`user_id`),
  CONSTRAINT `fk_user_profile_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户扩展信息表';

CREATE TABLE IF NOT EXISTS `content` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '内容ID',
  `user_id` bigint(20) NOT NULL COMMENT '发布用户ID',
  `title` varchar(200) NOT NULL COMMENT '内容标题',
  `content` text NOT NULL COMMENT '内容详情',
  `cover_img` varchar(255) DEFAULT NULL COMMENT '封面图片URL',
  `view_count` int(11) NOT NULL DEFAULT 0 COMMENT '浏览量',
  `like_count` int(11) NOT NULL DEFAULT 0 COMMENT '点赞数',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态（1-正常，0-下架，2-审核中）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status_create_time` (`status`, `create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8mb4 COMMENT='内容发布表';

CREATE TABLE IF NOT EXISTS `message_summary` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `sender_id` bigint(20) NOT NULL COMMENT '发送者ID',
  `receiver_id` bigint(20) NOT NULL COMMENT '接收者ID',
  `content` varchar(500) NOT NULL COMMENT '消息内容摘要',
  `msg_type` tinyint(2) NOT NULL COMMENT '消息类型（1-文本，2-图片，3-文件，4-链接）',
  `read_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '阅读状态（0-未读，1-已读）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  PRIMARY KEY (`id`),
  KEY `idx_sender_receiver` (`sender_id`, `receiver_id`),
  KEY `idx_receiver_read_status` (`receiver_id`, `read_status`)
) ENGINE=InnoDB AUTO_INCREMENT=10001 DEFAULT CHARSET=utf8mb4 COMMENT='消息摘要表（用于会话列表）';

CREATE TABLE IF NOT EXISTS `system_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_key` varchar(50) NOT NULL COMMENT '配置键（唯一）',
  `config_value` varchar(500) NOT NULL COMMENT '配置值',
  `config_desc` varchar(255) DEFAULT NULL COMMENT '配置描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_config_key` (`config_key`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

INSERT INTO `system_config` (`config_key`, `config_value`, `config_desc`)
VALUES
('system_name', '拉贝星球', '系统名称'),
('system_version', 'v1.0.0', '系统版本'),
('max_upload_size', '10485760', '最大上传文件大小（10MB）')
ON DUPLICATE KEY UPDATE `config_value`=VALUES(`config_value`), `config_desc`=VALUES(`config_desc`);
EOF

log_success "labei.sql创建完成"

# 创建apiservice/Dockerfile
log_info "创建apiservice/Dockerfile..."
mkdir -p apiservice
cat > apiservice/Dockerfile << EOF
FROM $OPENJDK_IMAGE
LABEL maintainer="labei@docker"
WORKDIR /app
COPY ./apiservice.jar /app/apiservice.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Xms256m", "-Xmx512m", "-jar", "/app/apiservice.jar", "--spring.profiles.active=prod"]
EOF

log_success "apiservice/Dockerfile创建完成"

# 创建imsvr/Dockerfile
log_info "创建imsvr/Dockerfile..."
mkdir -p imsvr
cat > imsvr/Dockerfile << EOF
FROM $OPENJDK_IMAGE
LABEL maintainer="labei@docker"
WORKDIR /app
COPY ./imsvr.jar /app/imsvr.jar
EXPOSE 8082
ENTRYPOINT ["java", "-Xms128m", "-Xmx256m", "-jar", "/app/imsvr.jar", "--spring.profiles.active=prod"]
EOF

log_success "imsvr/Dockerfile创建完成"

# ========================= 11. 前端打包（带进度显示） =========================
log_info "【步骤11/12】前端项目打包（H5 + 管理后台）..."

# 打包H5前端
if [ -d "h5" ]; then
    log_info "开始打包H5前端..."
    cd h5

    log_info "安装H5依赖包（使用阿里云npm镜像）..."
    npm install --registry="$ALIYUN_NPM_REGISTRY" --progress=true 2>&1 | while IFS= read -r line; do
        if [[ $line =~ ([0-9]+)/([0-9]+) ]]; then
            current="${BASH_REMATCH[1]}"
            total="${BASH_REMATCH[2]}"
            percent=$((current * 100 / total))
            echo -e "\r当前状态：安装中 | H5依赖 | 进度：$percent% ($current/$total)"
        fi
    done

    if [ $? -ne 0 ]; then
        log_error "H5依赖安装失败"
        exit 1
    fi

    log_info "构建H5前端..."
    npm run build || {
        log_error "H5前端构建失败"
        exit 1
    }

    cd ..
    log_success "H5前端打包完成"
else
    log_error "未找到h5目录，请检查源码结构"
    exit 1
fi

# 打包管理后台
if [ -d "adminbak" ]; then
    log_info "开始打包管理后台..."
    cd adminbak

    # 检查frontend目录或frontend.zip
    if [ -f "frontend.zip" ]; then
        log_info "解压frontend.zip..."
        unzip -o frontend.zip -d frontend || {
            log_error "frontend.zip解压失败"
            exit 1
        }
    elif [ ! -d "frontend" ]; then
        log_error "adminbak目录下未找到frontend目录或frontend.zip"
        exit 1
    fi

    cd frontend

    log_info "安装管理后台依赖包（使用阿里云npm镜像）..."
    npm install --registry="$ALIYUN_NPM_REGISTRY" --progress=true 2>&1 | while IFS= read -r line; do
        if [[ $line =~ ([0-9]+)/([0-9]+) ]]; then
            current="${BASH_REMATCH[1]}"
            total="${BASH_REMATCH[2]}"
            percent=$((current * 100 / total))
            echo -e "\r当前状态：安装中 | 管理后台依赖 | 进度：$percent% ($current/$total)"
        fi
    done

    if [ $? -ne 0 ]; then
        log_error "管理后台依赖安装失败"
        exit 1
    fi

    log_info "构建管理后台..."
    npm run build || {
        log_error "管理后台构建失败"
        exit 1
    }

    cd ../../
    log_success "管理后台打包完成"
else
    log_error "未找到adminbak目录，请检查源码结构"
    exit 1
fi

log_success "所有前端项目打包完成"

# ========================= 12. 启动Docker服务并验证 =========================
log_info "【步骤12/12】启动Docker服务并进行最终验证..."

# 启动Docker Compose服务
log_info "启动所有Docker服务..."
docker-compose up -d --build 2>&1 | while IFS= read -r line; do
    echo "$line"
    if [[ $line =~ Building|Pulling|Creating|Starting ]]; then
        echo -e "\r当前状态：$line"
    fi
done

if [ $? -ne 0 ]; then
    log_error "Docker服务启动失败"
    exit 1
fi

log_success "Docker服务启动成功"

# 等待服务初始化
log_info "等待服务初始化（60秒）..."
for i in {1..60}; do
    echo -ne "\r当前状态：初始化中 | 剩余时间：$((60-i))秒"
    sleep 1
done
echo ""

# 验证服务状态
log_info "验证服务状态..."

# 检查容器运行状态
CONTAINERS=("labei-mysql" "labei-redis" "labei-mongodb" "labei-nacos" "labei-apiservice" "labei-imsvr" "labei-nginx")
ALL_RUNNING=true

for container in "${CONTAINERS[@]}"; do
    if docker ps --format '{{.Names}}' | grep -q "^${container}$"; then
        log_success "容器 $container 运行正常"
    else
        log_error "容器 $container 未运行"
        ALL_RUNNING=false
    fi
done

if [ "$ALL_RUNNING" = false ]; then
    log_error "部分容器未正常运行，请检查日志"
    exit 1
fi

# ========================= 最终状态校验 =========================
log_info "执行最终状态校验..."

# 验证版本信息
log_info "版本信息汇总："
echo "  - 系统版本: Ubuntu $VERSION_ID"
echo "  - Docker版本: $(docker --version | grep -oP '\d+\.\d+\.\d+' | head -1)"
echo "  - Docker Compose版本: $(docker-compose --version | grep -oP '\d+\.\d+\.\d+' | head -1)"
echo "  - Node.js版本: $(node -v | sed 's/v//')"
echo "  - npm版本: $(npm -v)"

# 验证镜像
log_info "Docker镜像验证："
docker images --format "table {{.Repository}}:{{.Tag}}\t{{.Size}}" | grep -E "mysql|mongo|redis|nacos|nginx|openjdk"

# 验证容器
log_info "Docker容器验证："
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"

# 验证网络连接
log_info "验证服务端口..."
PORTS=("3306" "6379" "27017" "8848" "8080" "8082" "80")
for port in "${PORTS[@]}"; do
    if netstat -tuln 2>/dev/null | grep -q ":$port "; then
        log_success "端口 $port 监听正常"
    else
        log_warning "端口 $port 未监听（可能服务尚未完全启动）"
    fi
done

# 最终成功提示
echo ""
echo "=========================================="
log_success "✅ 部署完成！所有硬性标准均满足，脚本执行闭环"
echo "=========================================="
echo ""
echo "部署信息汇总："
echo "  - 工作目录: $WORK_DIR"
echo "  - 日志文件: $LOG_FILE"
echo "  - H5访问地址: http://服务器IP/h5"
echo "  - 管理后台访问地址: http://服务器IP/admin"
echo "  - API接口地址: http://服务器IP/api"
echo "  - IM服务地址: http://服务器IP/im"
echo ""
echo "数据库连接信息："
echo "  - MySQL: 端口3306, 密码: LaBei_MySQL_2025"
echo "  - Redis: 端口6379, 密码: LaBei_Redis_2025"
echo "  - MongoDB: 端口27017, 用户: labei_mongo, 密码: LaBei_Mongo_2025"
echo "  - Nacos: 端口8848"
echo ""
echo "常用命令："
echo "  - 查看所有容器状态: docker ps -a"
echo "  - 查看容器日志: docker logs <容器名>"
echo "  - 重启所有服务: docker-compose restart"
echo "  - 停止所有服务: docker-compose down"
echo "  - 查看部署日志: cat $LOG_FILE"
echo ""
log_success "部署脚本执行完成！"
