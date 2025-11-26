CREATE TABLE IF NOT EXISTS filial (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome VARCHAR(255) NOT NULL,
    cnpj VARCHAR(18) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL,
    updated_at TIMESTAMP,
    updated_by VARCHAR(255)
);

CREATE INDEX IF NOT EXISTS idx_filial_cnpj ON filial(cnpj);

CREATE TABLE IF NOT EXISTS caminhao (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    placa VARCHAR(8) NOT NULL UNIQUE,
    tara DECIMAL(10, 3) NOT NULL ,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL,
    updated_at TIMESTAMP,
    updated_by VARCHAR(255)
);

CREATE INDEX IF NOT EXISTS idx_caminhao_placa ON caminhao(placa);

CREATE TABLE IF NOT EXISTS grao (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome VARCHAR(255) NOT NULL UNIQUE,
    preco_compra_por_tonelada DECIMAL(10, 2) NOT NULL ,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL,
    updated_at TIMESTAMP,
    updated_by VARCHAR(255)
);

CREATE INDEX IF NOT EXISTS idx_caminhao_placa ON caminhao(placa);

CREATE TABLE IF NOT EXISTS doca (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL,
    updated_at TIMESTAMP,
    updated_by VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS balanca (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_doca INTEGER NOT NULL,
    status VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL,
    updated_at TIMESTAMP,
    updated_by VARCHAR(255),

    FOREIGN KEY (id_doca) REFERENCES doca(id)
);

CREATE TABLE IF NOT EXISTS demanda_transporte (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_caminhao INTEGER NOT NULL,
    id_grao INTEGER NOT NULL,
    id_filial INTEGER NOT NULL,
    status VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL,
    updated_at TIMESTAMP,
    updated_by VARCHAR(255),

    FOREIGN KEY (id_caminhao) REFERENCES caminhao(id),
    FOREIGN KEY (id_grao) REFERENCES grao(id),
    FOREIGN KEY (id_filial) REFERENCES filial(id)
);

CREATE TABLE IF NOT EXISTS pesagem (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_demanda INTEGER NOT NULL,
    id_balanca INTEGER NOT NULL,
    id_caminhao INTEGER NOT NULL,
    id_grao INTEGER NOT NULL,
    id_filial INTEGER NOT NULL,
    id_doca INTEGER NOT NULL,
    peso_bruto DECIMAL(10,3) NOT NULL,
    tara DECIMAL(10,3) NOT NULL,
    peso_liquido DECIMAL(10,3) NOT NULL,
    custo DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL,

    FOREIGN KEY (id_caminhao) REFERENCES caminhao(id),
    FOREIGN KEY (id_grao) REFERENCES grao(id),
    FOREIGN KEY (id_filial) REFERENCES filial(id),
    FOREIGN KEY (id_doca) REFERENCES doca(id),
    FOREIGN KEY (id_balanca) REFERENCES balanca(id),
    FOREIGN KEY (id_demanda) REFERENCES demanda_transporte(id)
);

CREATE TABLE IF NOT EXISTS estoque_doca_grao (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_doca INTEGER NOT NULL,
    id_grao INTEGER NOT NULL,
    qtd_max DECIMAL(10, 3) NOT NULL,
    qtd_atual DECIMAL(10, 3) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL,
    updated_at TIMESTAMP,
    updated_by VARCHAR(255),

    FOREIGN KEY (id_grao) REFERENCES grao(id),
    FOREIGN KEY (id_doca) REFERENCES doca(id)
);

CREATE INDEX IF NOT EXISTS idx_estoque_doca_grao_doca ON estoque_doca_grao(id_doca);
CREATE INDEX IF NOT EXISTS idx_estoque_doca_grao_grao ON estoque_doca_grao(id_grao);
CREATE UNIQUE INDEX IF NOT EXISTS idx_estoque_doca_grao_unique ON estoque_doca_grao(id_doca, id_grao);