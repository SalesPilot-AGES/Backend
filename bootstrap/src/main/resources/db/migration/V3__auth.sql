CREATE SCHEMA IF NOT EXISTS auth;

ALTER TABLE collaborators
    ADD COLUMN IF NOT EXISTS password_hash VARCHAR;

CREATE TABLE IF NOT EXISTS auth.refresh_tokens (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES collaborators(id),
    token_hash VARCHAR NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    revoked_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS refresh_tokens_user_id_idx ON auth.refresh_tokens (user_id);
CREATE UNIQUE INDEX IF NOT EXISTS refresh_tokens_token_hash_idx ON auth.refresh_tokens (token_hash);

