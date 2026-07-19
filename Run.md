# Запуск проекта testBankRest

## Требования

- Установлен и запущен **Docker Desktop**
- Установлен **Postman** (или его веб-версия)
- Свободные порты `8080` (приложение) и `5433` (Postgres)

## 1. Запустить Docker

Открой **Docker Desktop** и дождись, пока он полностью запустится (иконка кита в трее должна стать активной, без анимации загрузки).

Проверить, что Docker работает, можно в терминале:

```bash
docker --version
docker ps
```

Если команды отработали без ошибок - Docker готов.

## 2. Запустить docker-compose.yml

Из корня проекта (там, где лежат `docker-compose.yml` и `.env`):

```bash
cd testBankRest
docker compose up --build
```

Что произойдёт:

- поднимется контейнер `bankDb` с Postgres (порт `5433` на хосте -> `5432` внутри контейнера);
- соберётся образ приложения по `Dockerfile` и запустится контейнер `application` (порт `8080`);
- Liquibase автоматически применит миграции и создаст таблицы `users` и `cards`.

Дождись в логах строки о старте Tomcat/Spring Boot, например:

```
Started TestBankRestApplication in ... seconds
```

Проверить, что всё поднялось:

```bash
docker compose ps
```

Оба сервиса (`bankDb`, `application`) должны быть в статусе `Up` / `healthy`.

Остановить всё:

```bash
docker compose down
```

Остановить и заодно удалить данные Postgres (полный сброс БД):

```bash
docker compose down -v
```

## 3. Работать через Postman

Базовый адрес API: `http://localhost:8080`

### 3.1. Регистрация пользователя

```
POST http://localhost:8080/api/auth/reg
Content-Type: application/json

{
  "username": "john",
  "password": "pass123",
  "name": "John",
  "surname": "Doe",
  "email": "john@mail.com"
}
```

В ответе придёт `token` - JWT для дальнейших запросов.

### 3.2. Логин

```
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "john",
  "password": "pass123"
}
```

### 3.3. Авторизация в Postman

Полученный `token` нужно передавать в каждом защищённом запросе в заголовке:

```
Authorization: Bearer <token>
```

Удобно настроить один раз на уровне коллекции Postman:

1. Открой коллекцию -> вкладка **Authorization**
2. Тип: **Bearer Token**
3. В поле Token подставь `{{token}}`
4. После логина сохраняй значение `token` из ответа в переменную окружения Postman `token` (можно автоматически через Tests-скрипт в запросе логина):

```javascript
const data = pm.response.json();
pm.environment.set("token", data.token);
```

### 3.4. Основные эндпоинты

**Пользователь (роль USER)**

| Метод | URL | Описание |
|---|---|---|
| GET | `/api/cards` | список своих карт |
| GET | `/api/cards/{id}` | карта по id |
| GET | `/api/cards/{id}/balance` | баланс карты |
| POST | `/api/cards/{id}/block` | запрос на блокировку своей карты |
| POST | `/api/cards/transfer` | перевод между картами |

Пример тела для перевода:

```json
{
  "fromCardId": 1,
  "toCardId": 2,
  "amount": 100.00
}
```

**Администрирование карт (роль ADMIN)**

| Метод | URL | Описание |
|---|---|---|
| POST | `/api/admin/cards/create` | создать карту |
| GET | `/api/admin/cards` | список всех карт |
| POST | `/api/admin/cards/{id}/block` | заблокировать карту |
| POST | `/api/admin/cards/{id}/acive` | активировать карту |
| DELETE | `/api/admin/cards/{id}/delete` | удалить карту |

Пример тела для создания карты:

```json
{
  "ownerUsername": "john",
  "cardNumber": "1234567812341234",
  "expirationDate": "2030-01-01",
  "startedBalance": 1000.00
}
```

**Администрирование пользователей (роль ADMIN)**

| Метод | URL | Описание |
|---|---|---|
| GET | `/api/admin/users` | список пользователей |
| POST | `/api/admin/users/{username}/role` | сменить роль |
| POST | `/api/admin/users/{username}/enable` | включить пользователя |
| POST | `/api/admin/users/{username}/disable` | отключить пользователя |
| DELETE | `/api/admin/users/{username}/delete` | удалить пользователя |

> Чтобы протестировать admin-эндпоинты, зарегистрируй пользователя, а затем вручную смени ему роль на `ADMIN` через БД или через `/api/admin/users/{username}/role`, авторизовавшись уже существующим ADMIN'ом. Для самого первого администратора роль придётся выставить напрямую в базе (`UPDATE users SET role = 'ADMIN' WHERE username = '...'`).

## Возможные проблемы

- **Порт 8080 или 5433 занят** — останови процесс, который его использует, либо поменяй порт в `docker-compose.yml` (`"8081:8080"` и т.п.).
- **Контейнер `application` падает при старте** — смотри логи: `docker compose logs app`.
- **401 Unauthorized в Postman** — проверь, что заголовок `Authorization: Bearer <token>` реально уходит с запросом, и что токен не истёк (`JWT_EXPIRATION` в `.env`, по умолчанию 1 час).