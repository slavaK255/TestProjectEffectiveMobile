# Система управления банковскими картами

REST API для управления банковскими картами с аутентификацией, авторизацией и операциями перевода.

##  Требования
- Java 17+
- Docker
- Docker Compose
  
##  Стек технологий
- **Java 17+**
- **Spring Boot**
- **Spring Data JPA**
- **Spring Security + JWT**
- **Lombok**
- **MapStruct**
- **LiquidBase**
- **Maven**
- **Swagger**
- **PostgreSQL (docker-compose)**

## ⚙️ Установка и запуск

### 1. Клонировать репозиторий
git clone https://github.com/slavaK255/TestProjectEffectiveMobile.git
cd TestProjectEffectiveMobile

### 2. Поднять базу данных через Docker
В корне проекта есть файл `docker-compose.yml`. Для запуска PostgreSQL выполните:
docker-compose up -d

### 3. Установить переменные окружения JWT
Для работы с авторизацией необходимо задать параметры для генерации токенов:

**Linux / MacOS (bash/zsh):**
export JWT_ACCESS_SECRET=your-actual-access-secret
export JWT_REFRESH_SECRET=your-actual-refresh-secret

### 4. Запустите после этого ваше приложение из того же терминала. 
