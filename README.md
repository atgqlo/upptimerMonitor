# 🌐 Uptime Monitor Service

Микросервис мониторинга доступности веб-ресурсов в режиме реального времени. Сервис периодически проверяет зарегистрированные URL-адреса, отслеживает их статус и отправляет события об инцидентах в Apache Kafka с защитой от спама через Redis.

---

## 🚀 Основной функционал

- **Управление сайтами для мониторинга:** REST API для регистрации URL, выбора канала связи и получателя.
- **Высокопроизводительный пинг сайтов:** Проверка доступности веб-ресурсов с использованием легковесных потоков (**Java 21 Virtual Threads** / Project Loom) и современного `RestClient`.
- **Защита от спам-уведомлений (Cooldown):** Кеширование в **Redis** с временным интервалом (TTL 10 минут), чтобы не отправлять повторные алерты при постоянном сбое сервиса.
- **Событийно-ориентированная интеграция:** Асинхронная отправка структурированных сообщений (`AlertMessage`) в топик **Apache Kafka** для обработки сервисом нотификаций.
- **Миграции БД:** Автоматическое управление схемой данных с помощью **Flyway**.

---

## 🛠 Технологический стек

- **Язык программирования:** Java 21
- **Фреймворк:** Spring Boot 3.x (Spring Web, Spring Data JPA, Spring Kafka, Spring Data Redis)
- **База данных:** PostgreSQL 15
- **Миграции БД:** Flyway
- **Брокер сообщений:** Apache Kafka (в режиме KRaft)
- **Кеш / Дедупликация:** Redis 7
- **Сборка и контейнеризация:** Maven, Docker, Docker Compose
- **Утилиты:** Lombok

---

## 🏛 Архитектура сервиса

```
                  +---------------------------+
                  |    Client / Frontend      |
                  +-------------+-------------+
                                | REST API
                                v
                   +-------------------------+
                   |  UrlMonitorController   |
                   +------------+------------+
                                |
                   +------------v------------+
                   |    UrlMonitorService    |
                   +------------+------------+
                                |
             +------------------+------------------+
             |                                     |
             v                                     v
+--------------------------+          +-------------------------+
| MonitoredUrlRepository   |          |      UrlPingWorker      |
|      (PostgreSQL)        |          | (Scheduled + VirtualTh) |
+--------------------------+          +------------+------------+
                                                   |
                        +--------------------------+--------------------------+
                        |                                                     |
                        v                                                     v
          +---------------------------+                         +---------------------------+
          |   AlertCooldownService    |                         |   AlertProducerService    |
          |          (Redis)          |                         |       (Apache Kafka)      |
          +---------------------------+                         +-------------+-------------+
                                                                              |
                                                                              v
                                                                     Topic: `url_alerts`
```

---

## ⚙️ Переменные окружения

| Переменная | Описание | Значение по умолчанию |
|---|---|---|
| `DB_HOST` | Хост базы данных PostgreSQL | `localhost` |
| `DB_PORT` | Порт PostgreSQL | `5432` |
| `DB_NAME` | Название базы данных | `monitordb` |
| `DB_USER` | Пользователь базы данных | `postgres` |
| `DB_PASSWORD` | Пароль базы данных | `password` |
| `REDIS_HOST` | Хост Redis | `localhost` |
| `REDIS_PORT` | Порт Redis | `6379` |
| `SPRING_KAFKA_BOOTSTRAP_SERVERS` | Брокер Kafka | `localhost:9092` |

---

## 🔌 REST API Endpoints

### 1. Добавить URL на мониторинг
`POST /api/v1/urls`

**Тело запроса (JSON):**
```json
{
  "url": "https://google.com",
  "recipient": "admin@example.com",
  "channel": "EMAIL"
}
```
*Поддерживаемые каналы (`channel`):* `EMAIL`, `TELEGRAM` (для Telegram в `recipient` передается `chat_id`).

**Ответ (201 Created):**
```json
{
  "id": 1,
  "url": "https://google.com",
  "recipient": "admin@example.com",
  "channel": "EMAIL",
  "status": "UNKNOWN",
  "createdAt": "2026-10-20T12:00:00",
  "lastChecked": null
}
```

### 2. Получить список всех мониторимых сайтов (с пагинацией)
`GET /api/v1/urls?page=0&size=10`

---

## 🐳 Запуск через Docker Compose

Для совместного развертывания с брокером Kafka:

```bash
docker compose up -d --build
```
Сервис будет доступен по адресу: `http://localhost:8081`