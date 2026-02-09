# Items Service

Backend-сервис для управления таблицей `items` с REST API и экспортом в Excel.

## Технологии

- Kotlin 2.3 + Spring Boot 3.5.9
- PostgreSQL 16
- Spring Data JPA (Hibernate)
- Flyway (миграции БД)
- Apache POI (генерация Excel)
- Gradle 9.3.1 (wrapper включён в проект)

## Требования

- JDK 25 (или 21+)
- Docker и Docker Compose (для PostgreSQL)

## Запуск

### 1. Запустить PostgreSQL

```bash
docker-compose up -d
```

### 2. Запустить приложение

```bash
# Linux/Mac
./gradlew bootRun

# Windows
gradlew.bat bootRun
```

При старте Flyway автоматически применит миграции:

- `V1__create_items_table.sql` — создание таблицы `items` (id, name)
- `V2__add_extra_columns.sql` — добавление столбцов `extra_number` и `extra_text` (nullable, существующие данные сохраняются)

Приложение будет доступно на `http://127.0.0.1:8080`.

> **Примечание:** PostgreSQL в Docker запускается на порту **5433** (чтобы не конфликтовать с локальной установкой PostgreSQL). Если порт 5432 свободен, можно изменить маппинг в `docker-compose.yml` и `application.yml`.

## API

### Получить все записи

```bash
curl http://127.0.0.1:8080/api/items
```

### Получить запись по ID

```bash
curl http://127.0.0.1:8080/api/items/1
```

### Создать запись

```bash
curl -X POST http://127.0.0.1:8080/api/items \
  -H "Content-Type: application/json" \
  -d '{"name": "Test Item", "extraNumber": 42, "extraText": "hello"}'
```

### Обновить запись

```bash
curl -X PUT http://127.0.0.1:8080/api/items/1 \
  -H "Content-Type: application/json" \
  -d '{"name": "Updated Item", "extraNumber": 99, "extraText": "world"}'
```

### Удалить запись

```bash
curl -X DELETE http://127.0.0.1:8080/api/items/1
```

### Экспорт в Excel

```bash
curl -X POST http://127.0.0.1:8080/api/items/export \
  -H "Content-Type: application/json" \
  -d '{"columns": ["id", "name", "extraNumber", "extraText"]}' \
  --output items.xlsx
```

Пользователь передаёт список столбцов для экспорта. Допустимые столбцы: `id`, `name`, `extraNumber`, `extraText`. В Excel-файл попадают только выбранные столбцы, первая строка — заголовки.

## Миграции БД

Миграции расположены в `src/main/resources/db/migration/` и применяются автоматически при запуске приложения через Flyway.

Новые столбцы добавлены как nullable, поэтому существующие данные не теряются.

## Валидация

- `name` — обязательное, не пустое, максимум 255 символов
- `extraNumber` — целое число, необязательное
- `extraText` — строка, необязательная, максимум 500 символов
- `columns` (в запросе экспорта) — не пустой список допустимых имён столбцов
