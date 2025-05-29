@echo off
setlocal enabledelayedexpansion

REM 配置目标服务器地址和端口
set "host=10.0.0.137"
set "port=7443"

echo 正在生成套件列表...
REM 直接解析 openssl 输出（提取每行第一个单词）
for /F "tokens=1* delims= " %%a in ('openssl ciphers -v "ALL:COMPLEMENTOFALL"') do (
    echo 测试套件: %%a
    openssl s_client -connect %host%:%port% -cipher "%%a" < nul 2>&1 | findstr /C:"Cipher is (NONE)" > nul
    if errorlevel 1 (
        echo [支持] %%a
    ) else (
        echo [不支持] %%a
    )
)

endlocal
pause