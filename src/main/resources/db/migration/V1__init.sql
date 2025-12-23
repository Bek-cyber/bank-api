create table accounts (
                          id uuid primary key,
                          balance numeric(19,2) not null,
                          status varchar(32) not null,
                          version bigint not null,
                          created_at timestamptz not null
);

create table transactions (
                              id uuid primary key,
                              account_id uuid not null,
                              amount numeric(19,2) not null,
                              type varchar(32) not null,
                              created_at timestamptz not null,
                              constraint fk_transactions_account
                                  foreign key (account_id) references accounts(id)
);

create table idempotency_keys (
                                  id uuid primary key,
                                  key_value varchar(100) not null,
                                  endpoint varchar(255) not null,
                                  request_hash varchar(64) not null,
                                  response_body text,
                                  response_status int not null,
                                  created_at timestamptz not null,
                                  constraint uk_idempotency unique (key_value, endpoint)
);
