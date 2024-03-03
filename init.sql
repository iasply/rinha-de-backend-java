CREATE SCHEMA rinha;
-- Cria a tabela cliente
CREATE TABLE rinha.cliente
(
  id     SERIAL PRIMARY KEY,
  limite BIGINT,
  saldo  BIGINT
);

-- Cria a tabela transacao
CREATE TABLE rinha.transacao
(
  id         SERIAL PRIMARY KEY,
  cliente_id INT,
  valor      BIGINT      NOT NULL,
  tipo       CHAR(1)     NOT NULL,
  descricao  VARCHAR(10) NOT NULL,
  data       VARCHAR(35) NOT NULL,
  FOREIGN KEY (cliente_id) REFERENCES cliente (id)
);

-- Insere os dados na tabela cliente
INSERT INTO rinha.cliente (limite, saldo)
VALUES (100000, 0),
       (80000, 0),
       (1000000, 0),
       (10000000, 0),
       (500000, 0);
