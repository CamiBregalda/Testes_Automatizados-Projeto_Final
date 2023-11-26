CREATE TABLE transacoes (
    id BIGINT NOT NULL,
    conta_id BIGINT NOT NULL,
    tipo_transacao VARCHAR(50) NOT NULL,
    valor DECIMAL(19,2) NOT NULL,
    data timestamp(6) not null,

    PRIMARY KEY(id)
);