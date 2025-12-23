-- Индекс для истории транзакций по счету
CREATE INDEX idx_transactions_account_created_at
    ON transactions (account_id, created_at DESC);

-- Уникальный индекс для idempotency-key
CREATE UNIQUE INDEX idx_idempotency_keys_key_endpoint
    ON idempotency_keys (key_value, endpoint);