#!/bin/bash

# 稳定性测试脚本 - 支持自定义DNS解析

# 默认参数
DEFAULT_REQUESTS=100
DEFAULT_SLEEP=1
DEFAULT_TIMEOUT=5

# 显示帮助信息
show_help() {
    echo "使用: $0 [选项] <URL>"
    echo "选项:"
    echo "  -n <数字>    总请求次数 (默认: $DEFAULT_REQUESTS)"
    echo "  -s <秒>      请求间隔时间 (默认: $DEFAULT_SLEEP)"
    echo "  -t <秒>      超时时间 (默认: $DEFAULT_TIMEOUT)"
    echo "  -r <HOST:PORT:IP> 自定义DNS解析 (如: example.com:443:1.2.3.4)"
    echo "  -h           显示此帮助信息"
    echo
    echo "示例:"
    echo "  $0 -n 50 -s 0.5 https://www.example.com/health"
    echo "  $0 -r 'www.example.com:443:1.2.3.4' https://www.example.com/health"
    exit 0
}

# 初始化变量
TOTAL_REQUESTS=$DEFAULT_REQUESTS
SLEEP_TIME=$DEFAULT_SLEEP
TIMEOUT=$DEFAULT_TIMEOUT
RESOLVE_OPT=""

# 解析命令行参数
while getopts "n:s:t:r:h" opt; do
    case $opt in
        n) TOTAL_REQUESTS=$OPTARG ;;
        s) SLEEP_TIME=$OPTARG ;;
        t) TIMEOUT=$OPTARG ;;
        r) RESOLVE_OPT="--resolve $OPTARG" ;;
        h) show_help ;;
        *) echo "无效选项: -$OPTARG" >&2; exit 1 ;;
    esac
done
shift $((OPTIND-1))

# 检查URL参数
if [ -z "$1" ]; then
    echo "错误: 必须指定测试URL" >&2
    show_help
    exit 1
fi
URL="$1"

# 验证数字参数
validate_number() {
    local num="$1"
    local name="$2"

    if ! [[ "$num" =~ ^[0-9]+(\.[0-9]+)?$ ]] || (( $(echo "$num <= 0" | bc -l) )); then
        echo "错误: $name 必须是正数" >&2
        exit 1
    fi
}

validate_number "$TOTAL_REQUESTS" "请求次数"
validate_number "$SLEEP_TIME" "间隔时间"
validate_number "$TIMEOUT" "超时时间"

# 结果统计变量
success_count=0
fail_count=0
total_time=0
min_time=999
max_time=0

# 检查依赖
for cmd in curl bc; do
    if ! command -v "$cmd" &> /dev/null; then
        echo "错误: $cmd 未安装，请先安装" >&2
        exit 1
    fi
done

echo "目标URL: $URL"
[ -n "$RESOLVE_OPT" ] && echo "自定义解析: $RESOLVE_OPT"
echo "总请求数: $TOTAL_REQUESTS"
echo "请求间隔: $SLEEP_TIME 秒"
echo "超时设置: $TIMEOUT 秒"
echo "----------------------------------"

for ((i=1; i<=$TOTAL_REQUESTS; i++)); do
    # 执行curl命令并测量时间
    start_time=$(date +%s.%N)

    response=$(curl $RESOLVE_OPT -s -o /dev/null -w "%{http_code}" --connect-timeout $TIMEOUT "$URL")
    exit_code=$?
    end_time=$(date +%s.%N)

    # 计算耗时(毫秒)
    elapsed=$(echo "($end_time - $start_time)*1000" | bc | awk '{printf "%.2f", $0}')

    # 更新统计
    if [ $exit_code -eq 0 ] && [ "$response" -eq 200 ]; then
        status="成功"
        ((success_count++))
        total_time=$(echo "$total_time + $elapsed" | bc)

        # 更新最小/最大时间
        if (( $(echo "$elapsed < $min_time" | bc -l) )); then
            min_time=$elapsed
        fi
        if (( $(echo "$elapsed > $max_time" | bc -l) )); then
            max_time=$elapsed
        fi
    else
        status="失败"
        ((fail_count++))
    fi

    # 打印单次结果
    printf "请求 %3d/%d: 状态: %-4s 耗时: %6.2f ms | HTTP状态码: %s\n" \
           $i $TOTAL_REQUESTS "$status" $elapsed "${response:-无响应}"

    sleep $SLEEP_TIME
done

echo "----------------------------------"
echo "总请求数: $TOTAL_REQUESTS"
echo "成功: $success_count"
echo "失败: $fail_count"
success_rate=$(echo "scale=2; $success_count/$TOTAL_REQUESTS*100" | bc)
echo "成功率: $success_rate%"

if [ $success_count -gt 0 ]; then
    avg_time=$(echo "scale=2; $total_time/$success_count" | bc)
    echo "平均响应时间: $avg_time ms"
    echo "最小响应时间: $min_time ms"
    echo "最大响应时间: $max_time ms"
fi

# 退出状态
if [ $fail_count -eq 0 ]; then
    exit 0
else
    exit 1
fi
