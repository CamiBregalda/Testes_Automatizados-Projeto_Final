CREATE TABLE pessoas (
    id BIGINT NOT NULL,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) NOT NULL,
    endereco VARCHAR(255) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    inatived boolean not null,
    created_at timestamp(6) not null,
    updated_at timestamp(6),
    inatived_at timestamp(6),

    PRIMARY KEY(id)
);

CREATE TABLE contas (
    id SERIAL,
    agencia BIGINT not null,
    numero BIGINT not null,
    saldo DECIMAL(19,2) not null,
    tipo VARCHAR(50) not null,
    titular_id BIGINT NOT NULL UNIQUE,
    canceled boolean not null,
    created_at timestamp(6) not null,
    updated_at timestamp(6),
    deleted_at timestamp(6),

    PRIMARY KEY(id),
    FOREIGN KEY (titular_id) REFERENCES pessoas(id)
);