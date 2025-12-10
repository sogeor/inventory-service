# inventory-service

Содержит микросервис для работы с запасами продуктов.

## Запуск приложения

### Сетевые порты

| Номер | Описание             |
|-------|----------------------|
| 8080  | Порт основного API.  |
| 9090  | Порт служебного API. |

### Существующие переменные окружения

| Имя                             | Описание                                                                   | Значение по умолчанию |
|---------------------------------|----------------------------------------------------------------------------|-----------------------|
| HTTP_BASE_PATH                  | Базовый путь основного API.                                                | /v1/inventory         |
| HTTP_MAX_HEADER_SIZE            | Максимально допустимый размер заголовка запроса.                           | 8KB                   |
| HTTP_MANAGEMENT_BASE_PATH       | Базовый путь служебного API.                                               | /v1/inventory         |
| DATASOURCE_URL                  | Ссылка на базу данных PostgreSQL.                                          |                       |
| DATASOURCE_USERNAME             | Имя пользователя, необходимое для подключения к базе данных PostgreSQL.    |                       |
| DATASOURCE_PASSWORD             | Пароль пользователя, необходимый для подключения к базе данных PostgreSQL. |                       |
| MONGODB_HOST                    | Хост базы данных MongoDB.                                                  |                       |
| MONGODB_PORT                    | Порт базы данных MongoDB.                                                  | 27017                 |
| MONGODB_USERNAME                | Имя пользователя, необходимое для подключения к базе данных MongoDB.       |                       |
| MONGODB_PASSWORD                | Пароль пользователя, необходимый для подключения к базе данных MongoDB.    |                       |
| MONGODB_AUTHENTICATION_DATABASE | Имя базы данных MongoDB для аутентификации.                                | admin                 |
| MONGODB_DATABASE                | Имя базы данных MongoDB.                                                   | inventory             |
| KAFKA_BOOTSTRAP_SERVERS         | Список URL для подключения к Apache Kafka.                                 |                       |
| KAFKA_CONSUMER_GROUP            | Имя группы потребителей в Apache Kafka.                                    | inventory-service     |

## Работа с приложением

### Чтение документации API

Документация API должна быть доступна по адресу: https://api.sogeor.com/v1/inventory/actuator/swagger-ui
