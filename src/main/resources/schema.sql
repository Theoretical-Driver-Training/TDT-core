CREATE TABLE IF NOT EXISTS users
(
    id                        BIGSERIAL PRIMARY KEY,
    username                  VARCHAR(255) UNIQUE NOT NULL,
    password                  VARCHAR(255)        NOT NULL,
    last_password_change_date TIMESTAMP,
    first_name                VARCHAR(255),
    last_name                 VARCHAR(255),
    middle_name               VARCHAR(255),
    weight                    FLOAT,
    height                    FLOAT,
    birth_date                DATE,
    role                      VARCHAR(50)         NOT NULL
);


CREATE TABLE IF NOT EXISTS tokens
(
    id       BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    token    VARCHAR(255)        NOT NULL
);

CREATE TABLE IF NOT EXISTS password_change_tokens
(
    id          BIGSERIAL PRIMARY KEY,
    token       VARCHAR(255) NOT NULL,
    expiry_date TIMESTAMP WITH TIME ZONE,
    user_id     BIGINT       NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS blacklist_token
(
    id          BIGSERIAL PRIMARY KEY,
    token       VARCHAR(255) NOT NULL,
    expiry_date TIMESTAMP WITH TIME ZONE
);

-- Создание таблицы тестов
CREATE TABLE IF NOT EXISTS tests
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT         NOT NULL
);

-- Создание таблицы вопросов
CREATE TABLE IF NOT EXISTS questions
(
    id      BIGSERIAL PRIMARY KEY,
    test_id BIGINT NOT NULL,
    number  INT    NOT NULL,
    content TEXT   NOT NULL,
    FOREIGN KEY (test_id) REFERENCES tests (id) ON DELETE CASCADE
);

-- Создание таблицы возможных ответов
CREATE TABLE IF NOT EXISTS possible_answers
(
    id          BIGSERIAL PRIMARY KEY,
    question_id BIGINT NOT NULL,
    number      INT    NOT NULL,
    value       INT    NOT NULL,
    content     TEXT   NOT NULL,
    FOREIGN KEY (question_id) REFERENCES questions (id) ON DELETE CASCADE
);

-- Создание таблицы результатов
CREATE TABLE IF NOT EXISTS results
(
    id          BIGSERIAL PRIMARY KEY,
    test_id     BIGINT       NOT NULL,
    name        VARCHAR(255) NOT NULL,
    description TEXT         NOT NULL,
    FOREIGN KEY (test_id) REFERENCES tests (id) ON DELETE CASCADE
);

-- Создание таблицы возможных результатов
CREATE TABLE IF NOT EXISTS possible_results
(
    id          BIGSERIAL PRIMARY KEY,
    result_id   BIGINT NOT NULL,
    start_value INT    NOT NULL,
    end_value   INT    NOT NULL,
    content     TEXT   NOT NULL,
    FOREIGN KEY (result_id) REFERENCES results (id) ON DELETE CASCADE
);


-- Создание таблицы результатов
CREATE TABLE IF NOT EXISTS results
(
    id          BIGSERIAL PRIMARY KEY,
    test_id     BIGINT       NOT NULL,
    name        VARCHAR(255) NOT NULL,
    description TEXT         NOT NULL,
    FOREIGN KEY (test_id) REFERENCES tests (id) ON DELETE CASCADE
);

-- Создание таблицы возможных результатов
CREATE TABLE IF NOT EXISTS possible_results
(
    id          BIGSERIAL PRIMARY KEY,
    result_id   BIGINT NOT NULL,
    start_value INT    NOT NULL,
    end_value   INT    NOT NULL,
    content     TEXT   NOT NULL,
    FOREIGN KEY (result_id) REFERENCES results (id) ON DELETE CASCADE
);

-- Создание таблицы статусов
CREATE TABLE IF NOT EXISTS test_status
(
    id          SERIAL PRIMARY KEY,
    status_name VARCHAR(50) NOT NULL UNIQUE
);


-- Создание таблицы истории тестов
CREATE TABLE IF NOT EXISTS test_history
(
    id                  BIGSERIAL PRIMARY KEY,
    user_id             BIGINT   NOT NULL,
    test_id             BIGINT   NOT NULL,
    status              SMALLINT NOT NULL,
    start_time          TIMESTAMP,
    end_time            TIMESTAMP,
    current_test_cursor BIGINT,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (test_id) REFERENCES tests (id)
);

-- Создание таблицы списка ответов
CREATE TABLE IF NOT EXISTS test_history_answers
(
    id              BIGSERIAL PRIMARY KEY,
    test_history_id BIGINT NOT NULL,
    question_id     BIGINT NOT NULL,
    answer_id       BIGINT NOT NULL,
    FOREIGN KEY (test_history_id) REFERENCES test_history (id),
    FOREIGN KEY (question_id) REFERENCES questions (id),
    FOREIGN KEY (answer_id) REFERENCES possible_answers (id)
);

-- Создание таблицы списка результатов
CREATE TABLE IF NOT EXISTS test_history_results
(
    id              BIGSERIAL PRIMARY KEY,
    test_history_id BIGINT       NOT NULL,
    name            VARCHAR(255) NOT NULL,
    value           INT          NOT NULL,
    description     TEXT         NOT NULL,
    FOREIGN KEY (test_history_id) REFERENCES test_history (id)
);
