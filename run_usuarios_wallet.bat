@echo off
echo ================================================
echo 🐳 Iniciando entorno Docker para usuarios-petfriendly...
echo ================================================

cd /d %~dp0

echo 🔻 Deteniendo y limpiando contenedor anterior...
docker-compose down -v

echo 🔄 Reconstruyendo imagen sin cache...
docker-compose build --no-cache

echo 🚀 Levantando contenedor...
start "" cmd /c "docker-compose up"

timeout /t 5 >nul

echo 🔎 Verificando contenedor...
for /f "tokens=1" %%i in ('docker ps -qf "name=usuarios-petfriendly"') do (
    echo ✅ Contenedor activo. Verificando wallet...
    docker exec -it usuarios-petfriendly ls /app/wallet
    goto fin
)

echo ❌ El contenedor no se está ejecutando. Mostrando últimos logs...
for /f "tokens=1" %%i in ('docker ps -aqf "name=usuarios-petfriendly"') do (
    docker logs %%i
)

:fin
echo ================================================
echo ✔️ Proceso completado.
echo ================================================
pause
