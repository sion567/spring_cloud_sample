@echo off
cd /d "%~dp0shop-parent"
echo Building...
call gradlew.bat clean bootJar -x test
if %errorlevel% neq 0 (
    echo Build failed!
    pause
    exit /b 1
)

set DIR=%cd%

powershell -Command "Start-Process wt -ArgumentList 'new-tab --title user-service --tabColor \"#0078D4\" cmd /c \"java -jar \"%DIR%\\user-service\\build\\libs\\user-service-1.0.0.jar\" --spring.profiles.active=dev\" ; new-tab --title product-service --tabColor \"#107C10\" cmd /c \"java -jar \"%DIR%\\product-service\\build\\libs\\product-service-1.0.0.jar\" --spring.profiles.active=dev\" ; new-tab --title order-service --tabColor \"#FF8C00\" cmd /c \"java -jar \"%DIR%\\order-service\\build\\libs\\order-service-1.0.0.jar\" --spring.profiles.active=dev\" ; new-tab --title gateway --tabColor \"#00BCF2\" cmd /c \"java -jar \"%DIR%\\gateway\\build\\libs\\gateway-1.0.0.jar\" --spring.profiles.active=dev\" ; new-tab --title email-service --tabColor \"#E81123\" cmd /c \"java -jar \"%DIR%\\email-service\\build\\libs\\email-service-1.0.0.jar\" --spring.profiles.active=dev\" ; new-tab --title marketing-service --tabColor \"#881798\" cmd /c \"java -jar \"%DIR%\\marketing-service\\build\\libs\\marketing-service-1.0.0.jar\" --spring.profiles.active=dev\" ; new-tab --title points-service --tabColor \"#00B294\" cmd /c \"java -jar \"%DIR%\\points-service\\build\\libs\\points-service-1.0.0.jar\" --spring.profiles.active=dev\"'"

exit
