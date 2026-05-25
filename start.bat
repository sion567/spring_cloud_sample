@echo off
cd /d "%~dp0shop-parent"
echo Building...
call gradlew.bat bootJar -x test
if %errorlevel% neq 0 (
    echo Build failed!
    pause
    exit /b 1
)
echo.
set DIR=%cd%
echo [1] user-service
start "user-service" cmd /k "java -jar %DIR%\user-service\build\libs\user-service-1.0.0.jar"
timeout /t 5 > nul
echo [2] product-service
start "product-service" cmd /k "java -jar %DIR%\product-service\build\libs\product-service-1.0.0.jar"
timeout /t 5 > nul
echo [3] order-service
start "order-service" cmd /k "java -jar %DIR%\order-service\build\libs\order-service-1.0.0.jar"
timeout /t 8 > nul
echo [4] gateway
start "gateway" cmd /k "java -jar %DIR%\gateway\build\libs\gateway-1.0.0.jar"
timeout /t 5 > nul
echo [5] email-service
start "email-service" cmd /k "java -jar %DIR%\email-service\build\libs\email-service-1.0.0.jar"
timeout /t 5 > nul
echo [6] marketing-service
start "marketing-service" cmd /k "java -jar %DIR%\marketing-service\build\libs\marketing-service-1.0.0.jar"
timeout /t 5 > nul
echo [7] points-service
start "points-service" cmd /k "java -jar %DIR%\points-service\build\libs\points-service-1.0.0.jar"
pause