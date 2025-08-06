CREATE TABLE pauta (
  id SERIAL PRIMARY KEY,
  titulo VARCHAR(255) NOT NULL,
  descricao TEXT,
  data_criacao TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE voto (
  id SERIAL PRIMARY KEY,
  cpf VARCHAR(14) NOT NULL,
  associado_id INT NOT NULL,
  voto BOOLEAN NOT NULL,
  pauta_id INT NOT NULL REFERENCES pauta(id),
  CONSTRAINT uk_voto_pauta_associado UNIQUE (pauta_id, associado_id)
);

CREATE INDEX idx_voto_associado ON voto(associado_id);
CREATE INDEX idx_voto_cpf        ON voto(cpf);
CREATE INDEX idx_voto_pauta_voto ON voto(pauta_id, voto);
