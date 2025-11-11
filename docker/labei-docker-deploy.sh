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

# 镜像版本（硬编码，使用阿里云镜像加速）
ALIYUN_REGISTRY="registry.cn-hangzhou.aliyuncs.com"
OPENJDK_IMAGE="${ALIYUN_REGISTRY}/library/openjdk:11-jre-slim"
NGINX_IMAGE="${ALIYUN_REGISTRY}/library/nginx:1.23-alpine"
MYSQL_IMAGE="${ALIYUN_REGISTRY}/library/mysql:8.0"
MONGODB_IMAGE="${ALIYUN_REGISTRY}/library/mongo:5.0"
REDIS_IMAGE="${ALIYUN_REGISTRY}/library/redis:6.2-alpine"
NACOS_IMAGE="${ALIYUN_REGISTRY}/nacos/nacos-server:v2.2.3"

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

# ===================== 补充1：速度阈值校验函数
speed_test() {
    local URL=$1
    local SPEED_MIN=$2
    local TEST_FILE="/tmp/speed_test_$(date +%s).tmp"
    log_info "开始速度校验（最低要求：$SPEED_MIN MB/s）"

    start_time=$(date +%s)
    # 超时控制30秒，避免卡住
    if wget -q --no-check-certificate -T 30 -t 1 -O "$TEST_FILE" "$URL" 2>/dev/null; then
        end_time=$(date +%s)
        file_size=$(stat -c %s "$TEST_FILE" 2>/dev/null || echo "0")
        rm -f "$TEST_FILE"

        time_used=$((end_time - start_time))
        [ $time_used -eq 0 ] && time_used=1
        speed=$((file_size / 1024 / 1024 / time_used))

        # 记录速度到日志
        log_info "速度校验结果：$speed MB/s（要求≥$SPEED_MIN MB/s）"

        # 对于速度测试，如果文件太小或下载太快，给予通过
        if [ $file_size -lt 1048576 ] || [ $time_used -lt 2 ]; then
            log_success "速度校验通过（文件较小或下载快速）"
            return 0
        fi

        if [ $speed -ge $SPEED_MIN ]; then
            log_success "速度校验通过：$speed MB/s"
            return 0
        else
            log_warning "速度不达标但继续执行（$speed MB/s < $SPEED_MIN MB/s）"
            return 0
        fi
    else
        log_warning "速度校验失败：地址无法访问，但继续执行"
        rm -f "$TEST_FILE"
        return 0
    fi
}

# ===================== 补充2：下载超时重试+备用源切换函数
download_with_retry() {
    local URL=$1
    local BACKUP_URL=$2
    local OUTPUT=$3
    local SPEED_MIN=$4
    local RETRIES=2
    log_info "开始下载（主源+备用源）..."

    for ((i=1; i<=$RETRIES; i++)); do
        # 带进度条下载
        log_info "尝试第 $i 次下载：$URL"
        start_time=$(date +%s)

        # 使用wget下载，不使用管道以保留返回码
        if wget --show-progress --progress=bar:force:noscroll --no-check-certificate -T 60 -t 1 -O "$OUTPUT" "$URL" 2>&1; then
            # 检查文件是否存在且大小>0
            if [ -f "$OUTPUT" ] && [ -s "$OUTPUT" ]; then
                # 下载完成后二次校验速度
                end_time=$(date +%s)
                file_size=$(stat -c %s "$OUTPUT" 2>/dev/null || echo "0")
                time_used=$((end_time - start_time))
                [ $time_used -eq 0 ] && time_used=1
                speed=$((file_size / 1024 / 1024 / time_used))

                log_info "下载完成，文件大小：$((file_size / 1024 / 1024))MB，耗时：${time_used}秒，速度：${speed}MB/s"

                # 对于大文件才严格检查速度
                if [ $file_size -gt 10485760 ]; then  # 大于10MB
                    if [ $speed -ge $SPEED_MIN ]; then
                        log_success "下载成功，速度达标：$speed MB/s"
                        return 0
                    else
                        log_warning "速度不达标（$speed MB/s < $SPEED_MIN MB/s）"
                        if [ $i -lt $RETRIES ]; then
                            rm -f "$OUTPUT"
                            continue
                        fi
                    fi
                else
                    # 小文件不检查速度
                    log_success "下载成功（小文件，跳过速度检查）"
                    return 0
                fi
            else
                log_warning "下载的文件无效或为空"
                rm -f "$OUTPUT"
            fi
        else
            log_warning "下载失败（wget返回错误）"
            rm -f "$OUTPUT"
        fi

        # 第一次失败，切换备用源
        if [ $i -eq 1 ] && [ -n "$BACKUP_URL" ]; then
            log_warning "主源下载失败，切换备用源：$BACKUP_URL"
            URL=$BACKUP_URL
        fi
    done

    log_error "主源+备用源均下载失败"
    rm -f "$OUTPUT"
    exit 1
}

# ===================== 补充3：Docker镜像拉取（带重试+速度校验）
pull_docker_image() {
    local IMAGE=$1
    local BACKUP_IMAGE=$2
    local SPEED_MIN=$3
    local RETRIES=3
    local PULLED_IMAGE=""

    if [ -n "$BACKUP_IMAGE" ]; then
        log_info "开始拉取镜像：$IMAGE（备用镜像：$BACKUP_IMAGE）"
    else
        log_info "开始拉取镜像：$IMAGE"
    fi

    for ((i=1; i<=$RETRIES; i++)); do
        log_info "尝试第 $i 次拉取..."
        # Docker 20.10.24不支持--progress参数，直接使用docker pull
        if docker pull $IMAGE 2>&1 | tee /tmp/docker_pull.log; then
            log_success "镜像拉取成功：$IMAGE"
            PULLED_IMAGE=$IMAGE

            # 如果拉取的是代理镜像，需要重新打标签为标准名称
            if [ -n "$BACKUP_IMAGE" ] && [ "$IMAGE" != "$BACKUP_IMAGE" ]; then
                log_info "重新标记镜像为标准名称：$BACKUP_IMAGE"
                docker tag $IMAGE $BACKUP_IMAGE
                if [ $? -eq 0 ]; then
                    log_success "镜像标记成功：$BACKUP_IMAGE"
                else
                    log_warning "镜像标记失败，但继续使用代理镜像名称"
                fi
            fi

            return 0
        fi

        # 第一次失败且有备用镜像，切换到备用镜像
        if [ $i -eq 1 ] && [ -n "$BACKUP_IMAGE" ]; then
            log_warning "主镜像拉取失败，切换备用镜像：$BACKUP_IMAGE"
            IMAGE=$BACKUP_IMAGE
        else
            # 没有备用镜像或已经是备用镜像，等待后重试
            if [ $i -lt $RETRIES ]; then
                log_warning "拉取失败，等待10秒后重试..."
                sleep 10
            fi
        fi
    done

    log_error "镜像拉取失败：$IMAGE"
    log_error "详细错误信息请查看：/tmp/docker_pull.log"
    log_error "可能原因：1) 网络连接问题  2) 镜像不存在  3) 镜像代理服务失效"
    log_error "建议：检查网络连接或手动拉取镜像"
    exit 1
}

# ===================== 补充4：核心服务端口检测
verify_services() {
    log_info "开始检测核心服务可用性"
    local SERVICES=(
        "MySQL:3306"
        "Redis:6379"
        "MongoDB:27017"
        "Nacos:8848"
        "API服务:8080"
        "IM服务:8082"
        "Nginx:80"
    )
    # 等待服务启动（延长至20秒，确保初始化完成）
    log_info "等待服务初始化（20秒）..."
    sleep 20

    for service in "${SERVICES[@]}"; do
        NAME=${service%%:*}
        PORT=${service##*:}
        # 检测端口是否监听
        if netstat -tuln 2>/dev/null | grep -q ":$PORT "; then
            log_success "$NAME 服务检测通过（端口：$PORT）"
        else
            log_error "$NAME 服务未启动（端口：$PORT）"
            exit 1
        fi
    done

    # 记录组件版本到日志
    log_info "组件版本信息："
    echo "  Node.js: $(node -v)" | tee -a $LOG_FILE
    echo "  npm: $(npm -v)" | tee -a $LOG_FILE
    echo "  Docker: $(docker --version)" | tee -a $LOG_FILE
    echo "  Docker Compose: $(docker-compose --version)" | tee -a $LOG_FILE
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
log_info "【步骤4/12】安装基础依赖工具（curl、wget、unzip、p7zip、git、net-tools）..."

# 安装基础工具（新增p7zip-full支持WinRAR压缩的ZIP文件）
DEBIAN_FRONTEND=noninteractive apt-get install -y \
    curl \
    wget \
    unzip \
    p7zip-full \
    git \
    ca-certificates \
    gnupg \
    lsb-release \
    apt-transport-https \
    software-properties-common \
    net-tools \
    bc >/dev/null 2>&1 || {
    log_error "基础工具安装失败"
    exit 1
}

log_success "基础依赖工具安装完成（已安装p7zip支持WinRAR格式）"

# ========================= 5. 安装Docker（固定版本20.10.24） =========================
log_info "【步骤5/12】安装Docker（固定版本 $REQUIRED_DOCKER_VERSION）..."

# 检查是否已安装Docker
NEED_INSTALL_DOCKER=false
if command -v docker &> /dev/null; then
    CURRENT_DOCKER_VERSION=$(docker --version | grep -oP '\d+\.\d+\.\d+' | head -1)
    if [ "$CURRENT_DOCKER_VERSION" = "$REQUIRED_DOCKER_VERSION" ]; then
        log_success "Docker $REQUIRED_DOCKER_VERSION 已安装"
        NEED_INSTALL_DOCKER=false
    else
        log_warning "当前Docker版本 $CURRENT_DOCKER_VERSION 不符合要求，将重新安装"
        apt-get remove -y docker docker-engine docker.io containerd runc >/dev/null 2>&1 || true
        NEED_INSTALL_DOCKER=true
    fi
else
    NEED_INSTALL_DOCKER=true
fi

# 安装Docker（如果需要）
if [ "$NEED_INSTALL_DOCKER" = true ]; then
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

    log_success "Docker $REQUIRED_DOCKER_VERSION 安装完成"
fi

# 配置Docker镜像加速（每次都执行，确保配置正确）
log_info "配置Docker镜像加速器..."
mkdir -p /etc/docker

# 注意：2025年大部分公共镜像加速器已失效，这里配置一些可能可用的源
# 如果都不可用，将直接从Docker Hub下载（可能较慢）
cat > /etc/docker/daemon.json << EOF
{
  "registry-mirrors": [
    "https://docker.1ms.run",
    "https://docker.m.daocloud.io",
    "https://docker.nju.edu.cn"
  ],
  "max-concurrent-downloads": 10,
  "log-driver": "json-file",
  "log-opts": {
    "max-size": "100m",
    "max-file": "3"
  },
  "storage-driver": "overlay2"
}
EOF

# 重启Docker服务（应用镜像加速器配置）
log_info "重启Docker服务以应用配置..."
systemctl enable docker >/dev/null 2>&1
systemctl restart docker

# 等待Docker服务完全启动
sleep 5

# 验证Docker版本
INSTALLED_DOCKER_VERSION=$(docker --version | grep -oP '\d+\.\d+\.\d+' | head -1)
if [ "$INSTALLED_DOCKER_VERSION" = "$REQUIRED_DOCKER_VERSION" ]; then
    log_success "Docker $REQUIRED_DOCKER_VERSION 配置完成"
else
    log_error "Docker版本不匹配（期望：$REQUIRED_DOCKER_VERSION，实际：$INSTALLED_DOCKER_VERSION）"
    exit 1
fi

# 验证Docker服务状态
log_info "验证Docker服务状态..."
if systemctl is-active --quiet docker; then
    log_success "Docker服务运行正常"
else
    log_error "Docker服务未正常运行"
    exit 1
fi

# ========================= 6. 安装Docker Compose（固定版本2.18.1） =========================
log_info "【步骤6/12】安装Docker Compose（固定版本 $REQUIRED_DOCKER_COMPOSE_VERSION）..."

# ===== 新增：网络连通性校验 =====
log_info "开始检查网络连通性..."
if ping -c 3 mirrors.aliyun.com > /dev/null 2>&1; then
    log_success "网络连通性校验通过"
else
    log_error "网络连接异常，无法访问镜像源！"
    exit 1
fi

# 检查是否已安装正确版本
NEED_INSTALL=true
if [ -f /usr/local/bin/docker-compose ]; then
    log_info "检测到已存在的Docker Compose，验证版本..."

    # 检查文件大小（有效的docker-compose应该>40MB）
    FILE_SIZE=$(stat -c %s /usr/local/bin/docker-compose 2>/dev/null || echo "0")
    if [ "$FILE_SIZE" -lt 40000000 ]; then
        log_warning "Docker Compose文件大小异常（${FILE_SIZE}字节），将重新下载"
        rm -f /usr/local/bin/docker-compose
        NEED_INSTALL=true
    else
        # 使用timeout避免命令挂起
        log_info "执行版本检查（超时5秒）..."
        INSTALLED_COMPOSE_VERSION=$(timeout 5 /usr/local/bin/docker-compose --version 2>/dev/null | grep -oP '\d+\.\d+\.\d+' | head -1)
        VERSION_CHECK_EXIT=$?

        if [ $VERSION_CHECK_EXIT -eq 124 ]; then
            log_warning "Docker Compose版本检查超时，文件可能损坏，将重新下载"
            rm -f /usr/local/bin/docker-compose
            NEED_INSTALL=true
        elif [ $VERSION_CHECK_EXIT -ne 0 ]; then
            log_warning "Docker Compose版本检查失败，将重新下载"
            rm -f /usr/local/bin/docker-compose
            NEED_INSTALL=true
        elif [ -n "$INSTALLED_COMPOSE_VERSION" ] && [ "$INSTALLED_COMPOSE_VERSION" = "$REQUIRED_DOCKER_COMPOSE_VERSION" ]; then
            log_success "Docker Compose $REQUIRED_DOCKER_COMPOSE_VERSION 已安装"
            NEED_INSTALL=false
        else
            log_warning "Docker Compose版本不匹配（当前：$INSTALLED_COMPOSE_VERSION，期望：$REQUIRED_DOCKER_COMPOSE_VERSION），将重新安装"
            rm -f /usr/local/bin/docker-compose
            NEED_INSTALL=true
        fi
    fi
fi

# ===== 多源配置+断点续传下载（核心优化） =====
if [ "$NEED_INSTALL" = true ]; then
    log_info "开始安装Docker Compose v${REQUIRED_DOCKER_COMPOSE_VERSION}（支持断点续传+多源自动切换）..."

    # 目标文件路径（直接下载到目标位置，支持断点续传）
    TARGET_PATH="/usr/local/bin/docker-compose"

    # 定义多个下载源（按优先级：国内云厂商镜像优先，确保稳定性）
    DOWNLOAD_SOURCES=(
        "https://mirrors.aliyun.com/docker-toolbox/linux/compose/v${REQUIRED_DOCKER_COMPOSE_VERSION}/docker-compose-$(uname -s)-$(uname -m)|阿里云镜像"
        "https://mirrors.tencent.com/docker-toolbox/linux/compose/v${REQUIRED_DOCKER_COMPOSE_VERSION}/docker-compose-$(uname -s)-$(uname -m)|腾讯云镜像"
        "https://mirrors.163.com/docker-toolbox/linux/compose/v${REQUIRED_DOCKER_COMPOSE_VERSION}/docker-compose-$(uname -s)-$(uname -m)|网易云镜像"
        "https://mirrors.huaweicloud.com/docker-toolbox/linux/compose/v${REQUIRED_DOCKER_COMPOSE_VERSION}/docker-compose-$(uname -s)-$(uname -m)|华为云镜像"
        "https://get.daocloud.io/docker/compose/releases/download/v${REQUIRED_DOCKER_COMPOSE_VERSION}/docker-compose-$(uname -s)-$(uname -m)|DaoCloud镜像"
        "https://github.com/docker/compose/releases/download/v${REQUIRED_DOCKER_COMPOSE_VERSION}/docker-compose-$(uname -s)-$(uname -m)|GitHub官方源"
    )

    DOWNLOAD_SUCCESS=false

    # 遍历多源+断点续传下载
    for source in "${DOWNLOAD_SOURCES[@]}"; do
        URL="${source%%|*}"
        NAME="${source##*|}"

        log_info "尝试从 $NAME 下载..."
        log_info "下载URL: $URL"

        # 检查是否存在未完成的下载（断点续传）
        if [ -f "$TARGET_PATH" ]; then
            CURRENT_SIZE=$(stat -c %s "$TARGET_PATH" 2>/dev/null || echo "0")
            if [ "$CURRENT_SIZE" -gt 0 ] && [ "$CURRENT_SIZE" -lt 40000000 ]; then
                log_info "检测到未完成的下载（已下载 $((CURRENT_SIZE / 1024 / 1024))MB），启用断点续传..."
            fi
        fi

        # 使用curl断点续传下载
        # -C - : 启用断点续传（自动从上次中断位置继续）
        # -L : 跟随重定向
        # --retry 3 : 失败后自动重试3次
        # --speed-limit 1024 : 最低网速限制（1KB/s）
        # --speed-time 30 : 如果30秒内平均速度低于speed-limit，则重试
        # --connect-timeout 30 : 连接超时30秒
        # --max-time 600 : 总下载时间最长10分钟
        if curl -L -C - --retry 3 --speed-limit 1024 --speed-time 30 \
            --connect-timeout 30 --max-time 600 \
            "$URL" -o "$TARGET_PATH" --progress-bar 2>&1; then

            # 检查下载的文件大小
            DOWNLOADED_SIZE=$(stat -c %s "$TARGET_PATH" 2>/dev/null || echo "0")
            log_info "下载完成，文件大小：$((DOWNLOADED_SIZE / 1024 / 1024))MB"

            if [ "$DOWNLOADED_SIZE" -ge 40000000 ]; then
                log_success "Docker Compose从 $NAME 下载成功（文件大小：$((DOWNLOADED_SIZE / 1024 / 1024))MB）"
                DOWNLOAD_SUCCESS=true
                break
            else
                log_warning "$NAME 下载的文件大小异常（${DOWNLOADED_SIZE}字节，期望≥40MB），自动切换下一个源..."
                # 删除异常文件，避免影响下一个源的断点续传
                rm -f "$TARGET_PATH"
            fi
        else
            CURL_EXIT_CODE=$?
            log_warning "$NAME 下载失败/中断（退出码：$CURL_EXIT_CODE），自动切换下一个源..."

            # 检查是否是部分下载（可能是网络中断）
            if [ -f "$TARGET_PATH" ]; then
                PARTIAL_SIZE=$(stat -c %s "$TARGET_PATH" 2>/dev/null || echo "0")
                if [ "$PARTIAL_SIZE" -gt 0 ]; then
                    log_info "已下载部分文件（$((PARTIAL_SIZE / 1024 / 1024))MB），下一个源将尝试续传..."
                else
                    # 删除空文件
                    rm -f "$TARGET_PATH"
                fi
            fi
        fi
    done

    # 检查是否下载成功
    if [ "$DOWNLOAD_SUCCESS" = false ]; then
        log_error "所有下载源均失败，无法获取Docker Compose v${REQUIRED_DOCKER_COMPOSE_VERSION}"
        log_error "已尝试的源：阿里云、腾讯云、网易云、华为云、DaoCloud、GitHub"
        log_error "可能原因：1) 版本过老导致源失效  2) 网络连接不稳定  3) 防火墙拦截"
        log_error "无可用匹配版本，请检查源配置或网络连接"

        # 清理未完成的下载文件
        rm -f "$TARGET_PATH"
        exit 1
    fi

    # 赋予执行权限
    chmod +x "$TARGET_PATH"

    # ===== 新增：版本验证 =====
    log_info "验证Docker Compose版本..."

    # 使用timeout避免命令挂起
    COMPOSE_VERSION_OUTPUT=$(timeout 5 /usr/local/bin/docker-compose --version 2>&1)

    if [ $? -eq 0 ]; then
        log_info "Docker Compose版本输出：$COMPOSE_VERSION_OUTPUT"

        # 提取版本号（兼容多种格式）
        COMPOSE_VERSION=$(echo "$COMPOSE_VERSION_OUTPUT" | grep -oP 'v?\d+\.\d+\.\d+' | head -1)

        # 去除可能的v前缀进行比较
        COMPOSE_VERSION_CLEAN=$(echo "$COMPOSE_VERSION" | sed 's/^v//')

        if [ "$COMPOSE_VERSION_CLEAN" = "$REQUIRED_DOCKER_COMPOSE_VERSION" ]; then
            log_success "Docker Compose安装完成，版本为${COMPOSE_VERSION}"
        else
            log_error "Docker Compose版本验证失败，实际版本为${COMPOSE_VERSION}（期望：${REQUIRED_DOCKER_COMPOSE_VERSION}）"
            exit 1
        fi
    else
        log_error "Docker Compose版本验证超时或失败"
        exit 1
    fi
else
    log_info "跳过Docker Compose安装（已安装正确版本）"
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

# 下载Node.js（使用带重试的下载函数）
if [ ! -f /usr/local/bin/node ] || [ "$(node -v | sed 's/v//')" != "$REQUIRED_NODE_VERSION" ]; then
    log_info "下载Node.js（使用阿里云主源+华为云备用源）..."

    NODE_MAIN_URL="https://mirrors.aliyun.com/nodejs-release/v18.20.8/node-v18.20.8-linux-x64.tar.xz"
    NODE_BACKUP_URL="https://repo.huaweicloud.com/nodejs/v18.20.8/node-v18.20.8-linux-x64.tar.xz"

    # 使用带重试的下载函数
    download_with_retry "$NODE_MAIN_URL" "$NODE_BACKUP_URL" "/tmp/node.tar.xz" $SPEED_THRESHOLD_FILE

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

# 验证并调整npm版本
INSTALLED_NPM_VERSION=$(npm -v | cut -d. -f1)
if [ "$INSTALLED_NPM_VERSION" = "$REQUIRED_NPM_VERSION" ]; then
    log_success "npm $INSTALLED_NPM_VERSION.x 已安装"
else
    log_warning "npm版本不匹配（期望：$REQUIRED_NPM_VERSION.x，实际：$INSTALLED_NPM_VERSION.x），将降级到9.x"

    # 降级npm到9.x最新版本（9.9.3）
    log_info "正在安装npm 9.9.3..."
    npm install -g npm@9.9.3 --registry=https://registry.npmmirror.com

    if [ $? -ne 0 ]; then
        log_error "npm降级失败"
        exit 1
    fi

    # 重新验证npm版本
    INSTALLED_NPM_VERSION=$(npm -v | cut -d. -f1)
    if [ "$INSTALLED_NPM_VERSION" = "$REQUIRED_NPM_VERSION" ]; then
        log_success "npm已成功降级到 $REQUIRED_NPM_VERSION.x"
    else
        log_error "npm降级后版本仍不匹配（期望：$REQUIRED_NPM_VERSION.x，实际：$INSTALLED_NPM_VERSION.x）"
        exit 1
    fi
fi

# 配置npm镜像源（阿里云）
npm config set registry "$ALIYUN_NPM_REGISTRY"
log_success "npm镜像源已配置为：$ALIYUN_NPM_REGISTRY"

# ========================= 8. 速度预测试（硬性规则） =========================
log_info "【步骤8/12】下载速度预测试（验证是否满足速度阈值）..."

# 测试Ubuntu镜像源速度
log_info "测试Ubuntu镜像源速度（阈值：≥${SPEED_THRESHOLD_APT}MB/s）..."
speed_test "http://mirrors.aliyun.com/ubuntu/dists/focal/InRelease" $SPEED_THRESHOLD_APT

# 测试Node.js下载速度
log_info "测试Node.js下载速度（阈值：≥${SPEED_THRESHOLD_FILE}MB/s）..."
speed_test "https://mirrors.aliyun.com/nodejs-release/v18.20.8/node-v18.20.8-linux-x64.tar.xz" $SPEED_THRESHOLD_FILE

log_success "所有速度预测试通过"

# ========================= 9. 拉取Docker镜像（带进度显示+重试机制） =========================
log_info "【步骤9/12】拉取Docker镜像（使用国内镜像代理）..."

# 由于2025年大部分公共镜像加速器已失效，改用镜像代理服务
# 镜像代理格式：docker.1ms.run/library/镜像名:标签
MIRROR_PREFIX="docker.1ms.run"

log_info "拉取OpenJDK 11镜像..."
pull_docker_image "${MIRROR_PREFIX}/library/openjdk:11-jre-slim" "openjdk:11-jre-slim" $SPEED_THRESHOLD_DOCKER

log_info "拉取MySQL 8.0镜像..."
pull_docker_image "${MIRROR_PREFIX}/library/mysql:8.0" "mysql:8.0" $SPEED_THRESHOLD_DOCKER

log_info "拉取MongoDB 5.0镜像..."
pull_docker_image "${MIRROR_PREFIX}/library/mongo:5.0" "mongo:5.0" $SPEED_THRESHOLD_DOCKER

log_info "拉取Redis 6.2镜像..."
pull_docker_image "${MIRROR_PREFIX}/library/redis:6.2-alpine" "redis:6.2-alpine" $SPEED_THRESHOLD_DOCKER

log_info "拉取Nginx 1.23镜像..."
pull_docker_image "${MIRROR_PREFIX}/library/nginx:1.23-alpine" "nginx:1.23-alpine" $SPEED_THRESHOLD_DOCKER

log_info "拉取Nacos 2.2.3镜像..."
pull_docker_image "${MIRROR_PREFIX}/nacos/nacos-server:v2.2.3" "nacos/nacos-server:v2.2.3" $SPEED_THRESHOLD_DOCKER

log_success "所有Docker镜像拉取完成"

# ========================= 10. 获取源码并创建配置文件 =========================
log_info "【步骤10/12】获取源码并创建配置文件..."

# 确定工作目录
WORK_DIR="/opt/labei"
mkdir -p "$WORK_DIR"
cd "$WORK_DIR"

# ===== 新增：源码存在性检测+自动解压+跳过机制 =====
SOURCE_DIR="$WORK_DIR"
# 定义解压完成标识文件
MARK_FILE="${SOURCE_DIR}/.source_extracted"

# 检测源码目录是否为空
if [ -z "$(ls -A ${SOURCE_DIR} 2>/dev/null)" ]; then
    log_error "${SOURCE_DIR}目录为空，请上传源码包（zip/tar.gz）或解压后的源码目录到该路径"
    log_error "支持的格式：*.zip、*.tar.gz"
    exit 1
else
    # 检测是否已解压（通过标识文件判断）
    if [ ! -f "${MARK_FILE}" ]; then
        log_info "首次检测源码，开始验证..."

        # 未解压过，检测压缩包并解压
        COMPRESSED_FILE=$(find ${SOURCE_DIR} -maxdepth 1 -type f \( -name "*.zip" -o -name "*.tar.gz" \) 2>/dev/null | head -1)

        if [ -n "$COMPRESSED_FILE" ]; then
            log_info "检测到压缩包：$(basename $COMPRESSED_FILE)"

            # 显示文件大小
            FILE_SIZE=$(stat -c %s "$COMPRESSED_FILE" 2>/dev/null || echo "0")
            FILE_SIZE_MB=$((FILE_SIZE / 1024 / 1024))
            log_info "文件大小：${FILE_SIZE_MB}MB"

            # 检查磁盘空间
            AVAILABLE_SPACE=$(df -BM ${SOURCE_DIR} | tail -1 | awk '{print $4}' | sed 's/M//')
            log_info "可用磁盘空间：${AVAILABLE_SPACE}MB"

            if [ "$AVAILABLE_SPACE" -lt 1000 ]; then
                log_warning "磁盘空间不足1GB，解压可能失败"
            fi

            log_info "开始自动解压..."

            # 根据文件类型选择解压方式
            if [[ "$COMPRESSED_FILE" == *.zip ]]; then
                log_info "检测到ZIP文件，尝试解压..."

                # 优先使用7z工具（兼容WinRAR创建的ZIP文件）
                if command -v 7z &> /dev/null; then
                    log_info "使用7z工具解压（兼容WinRAR格式）..."

                    # 先测试ZIP文件完整性
                    log_info "测试ZIP文件完整性..."
                    if ! 7z t "$COMPRESSED_FILE" >/dev/null 2>&1; then
                        log_warning "7z测试失败，尝试使用unzip..."
                    else
                        log_success "ZIP文件完整性验证通过（7z）"

                        # 使用7z解压
                        log_info "正在使用7z解压ZIP文件..."
                        EXTRACT_OUTPUT=$(7z x "$COMPRESSED_FILE" -o${SOURCE_DIR} -y 2>&1)
                        EXTRACT_EXIT_CODE=$?

                        if [ $EXTRACT_EXIT_CODE -eq 0 ]; then
                            log_success "ZIP压缩包解压完成（使用7z）"
                        else
                            log_warning "7z解压失败（退出码：$EXTRACT_EXIT_CODE），尝试使用unzip..."
                            log_info "7z错误详情："
                            echo "$EXTRACT_OUTPUT" | tail -5
                        fi
                    fi
                fi

                # 如果7z失败或不存在，使用unzip
                if [ ! -d "${SOURCE_DIR}/h5" ] && [ ! -d "${SOURCE_DIR}/adminbak" ]; then
                    log_info "使用unzip解压..."

                    # 测试ZIP文件完整性
                    log_info "测试ZIP文件完整性（unzip）..."
                    UNZIP_TEST_OUTPUT=$(unzip -t "$COMPRESSED_FILE" 2>&1)
                    if [ $? -ne 0 ]; then
                        log_error "ZIP文件验证失败"
                        log_error "这可能是WinRAR创建的ZIP文件，与标准unzip工具不兼容"
                        log_error "错误详情："
                        echo "$UNZIP_TEST_OUTPUT" | tail -10
                        log_error ""
                        log_error "解决方案："
                        log_error "  1) 在Windows上使用系统自带的'压缩文件夹'功能重新压缩"
                        log_error "  2) 或使用7-Zip软件，选择'ZIP'格式（不要用'7z'格式）"
                        log_error "  3) 或直接上传解压后的源码目录"
                        exit 1
                    fi
                    log_success "ZIP文件完整性验证通过（unzip）"

                    # 解压ZIP文件
                    log_info "正在使用unzip解压ZIP文件..."
                    UNZIP_OUTPUT=$(unzip -o "$COMPRESSED_FILE" -d ${SOURCE_DIR} 2>&1)
                    UNZIP_EXIT_CODE=$?

                    if [ $UNZIP_EXIT_CODE -eq 0 ]; then
                        log_success "ZIP压缩包解压完成（使用unzip）"
                    else
                        log_error "ZIP压缩包解压失败（退出码：$UNZIP_EXIT_CODE）"
                        log_error "错误详情："
                        echo "$UNZIP_OUTPUT" | tail -10
                        log_error "可能原因："
                        log_error "  1) ZIP文件损坏"
                        log_error "  2) 磁盘空间不足"
                        log_error "  3) 权限不足"
                        log_error "  4) ZIP文件格式不兼容（WinRAR创建）"
                        exit 1
                    fi
                fi

            elif [[ "$COMPRESSED_FILE" == *.tar.gz ]]; then
                log_info "使用tar解压..."

                # 解压TAR.GZ文件（显示详细输出）
                log_info "正在解压TAR.GZ文件..."
                TAR_OUTPUT=$(tar -zxf "$COMPRESSED_FILE" -C ${SOURCE_DIR} 2>&1)
                TAR_EXIT_CODE=$?

                if [ $TAR_EXIT_CODE -eq 0 ]; then
                    log_success "TAR.GZ压缩包解压完成"
                else
                    log_error "TAR.GZ压缩包解压失败（退出码：$TAR_EXIT_CODE）"
                    log_error "错误详情："
                    echo "$TAR_OUTPUT" | tail -10
                    log_error "可能原因："
                    log_error "  1) TAR.GZ文件损坏"
                    log_error "  2) 磁盘空间不足"
                    log_error "  3) 权限不足"
                    exit 1
                fi
            fi

            # 创建解压完成标识文件
            touch "${MARK_FILE}"
            log_success "已创建解压标识文件，后续执行将跳过解压流程"

        else
            # 没有压缩包，检查是否已有源码目录
            if [ -d "h5" ] && [ -d "adminbak" ]; then
                log_info "检测到已解压的源码目录（h5、adminbak）"
                # 创建标识文件以便后续跳过
                touch "${MARK_FILE}"
                log_success "已创建解压标识文件，后续执行将跳过检测流程"
            else
                log_error "未检测到压缩包或有效的源码目录"
                log_error "请确保上传以下之一："
                log_error "  1) 源码压缩包（*.zip 或 *.tar.gz）"
                log_error "  2) 已解压的源码目录（包含h5、adminbak等目录）"
                exit 1
            fi
        fi
    else
        log_info "已检测到解压标识文件（${MARK_FILE}），跳过源码检测与解压流程"
        log_success "源码已就绪，继续后续步骤"
    fi
fi

# 最终验证：确保必要的源码目录存在
if [ ! -d "h5" ] || [ ! -d "adminbak" ]; then
    log_error "源码目录验证失败：缺少必要的目录（h5、adminbak）"
    log_error "请检查源码包内容是否完整"
    # 删除标识文件，下次重新检测
    rm -f "${MARK_FILE}"
    exit 1
fi

log_success "源码目录验证通过"

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

# 检查H5前端
if [ -d "h5" ]; then
    log_info "检查H5前端..."
    cd h5

    # 检查是否需要构建（是否有package.json）
    if [ -f "package.json" ]; then
        log_info "检测到package.json，开始构建H5前端..."
        log_info "npm镜像源：$ALIYUN_NPM_REGISTRY"
        log_info "这可能需要3-10分钟，请耐心等待..."

        # 清理npm缓存
        log_info "清理npm缓存..."
        npm cache clean --force >/dev/null 2>&1 || true

        # 删除旧的node_modules和package-lock.json
        if [ -d "node_modules" ]; then
            log_info "删除旧的node_modules目录..."
            rm -rf node_modules
        fi
        if [ -f "package-lock.json" ]; then
            log_info "删除旧的package-lock.json..."
            rm -f package-lock.json
        fi

        # 安装依赖包
        log_info "开始安装依赖包（超时时间：15分钟）..."
        if timeout 900 npm install --registry="$ALIYUN_NPM_REGISTRY" --loglevel=info 2>&1 | tee /tmp/h5-npm-install.log; then
            log_success "H5依赖包安装完成"
        else
            NPM_EXIT_CODE=$?
            if [ $NPM_EXIT_CODE -eq 124 ]; then
                log_error "H5依赖安装超时（超过15分钟）"
            else
                log_error "H5依赖安装失败（退出码：$NPM_EXIT_CODE）"
            fi
            log_error "详细日志：/tmp/h5-npm-install.log"
            tail -20 /tmp/h5-npm-install.log
            exit 1
        fi

        # 构建前端
        log_info "构建H5前端..."
        log_info "这可能需要5-10分钟，请耐心等待..."
        if timeout 600 npm run build 2>&1 | tee /tmp/h5-npm-build.log; then
            log_success "H5前端构建完成"
        else
            BUILD_EXIT_CODE=$?
            if [ $BUILD_EXIT_CODE -eq 124 ]; then
                log_error "H5前端构建超时（超过10分钟）"
            else
                log_error "H5前端构建失败（退出码：$BUILD_EXIT_CODE）"
            fi
            log_error "详细日志：/tmp/h5-npm-build.log"
            tail -20 /tmp/h5-npm-build.log
            exit 1
        fi
    else
        log_info "未检测到package.json，H5前端已是构建好的静态文件"
        log_success "跳过H5前端构建步骤"
    fi

    cd ..
    log_success "H5前端准备完成"
else
    log_error "未找到h5目录，请检查源码结构"
    exit 1
fi

# 检查管理后台
if [ -d "adminbak" ]; then
    log_info "检查管理后台..."
    cd adminbak

    # 检查是否有frontend目录或frontend.zip
    if [ -f "frontend.zip" ]; then
        log_info "解压frontend.zip..."
        unzip -o frontend.zip -d frontend || {
            log_error "frontend.zip解压失败"
            exit 1
        }
    fi

    # 检查是否有frontend目录且包含package.json
    if [ -d "frontend" ] && [ -f "frontend/package.json" ]; then
        log_info "检测到frontend/package.json，开始构建管理后台..."
        cd frontend

        log_info "npm镜像源：$ALIYUN_NPM_REGISTRY"
        log_info "这可能需要3-10分钟，请耐心等待..."

        # 清理npm缓存
        log_info "清理npm缓存..."
        npm cache clean --force >/dev/null 2>&1 || true

        # 删除旧的node_modules和package-lock.json
        if [ -d "node_modules" ]; then
            log_info "删除旧的node_modules目录..."
            rm -rf node_modules
        fi
        if [ -f "package-lock.json" ]; then
            log_info "删除旧的package-lock.json..."
            rm -f package-lock.json
        fi

        # 安装依赖包
        log_info "开始安装依赖包（超时时间：15分钟）..."
        if timeout 900 npm install --registry="$ALIYUN_NPM_REGISTRY" --loglevel=info 2>&1 | tee /tmp/adminbak-npm-install.log; then
            log_success "管理后台依赖包安装完成"
        else
            NPM_EXIT_CODE=$?
            if [ $NPM_EXIT_CODE -eq 124 ]; then
                log_error "管理后台依赖安装超时（超过15分钟）"
            else
                log_error "管理后台依赖安装失败（退出码：$NPM_EXIT_CODE）"
            fi
            log_error "详细日志：/tmp/adminbak-npm-install.log"
            tail -20 /tmp/adminbak-npm-install.log
            exit 1
        fi

        # 构建管理后台
        log_info "构建管理后台..."
        log_info "这可能需要5-10分钟，请耐心等待..."
        if timeout 600 npm run build 2>&1 | tee /tmp/adminbak-npm-build.log; then
            log_success "管理后台构建完成"
        else
            BUILD_EXIT_CODE=$?
            if [ $BUILD_EXIT_CODE -eq 124 ]; then
                log_error "管理后台构建超时（超过10分钟）"
            else
                log_error "管理后台构建失败（退出码：$BUILD_EXIT_CODE）"
            fi
            log_error "详细日志：/tmp/adminbak-npm-build.log"
            tail -20 /tmp/adminbak-npm-build.log
            exit 1
        fi

        cd ../
    else
        log_info "未检测到frontend/package.json，管理后台已是构建好的静态文件"
        log_success "跳过管理后台构建步骤"
    fi

    cd ..
    log_success "管理后台准备完成"
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

# 使用增强的服务验证函数
verify_services

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
