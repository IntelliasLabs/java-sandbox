CREATE TABLE IF NOT EXISTS books (
  id       BIGSERIAL PRIMARY KEY,
  title    varchar(255) NOT NULL,
  author   varchar(100) NOT NULL,
  isbn     varchar(20) NOT NULL
)