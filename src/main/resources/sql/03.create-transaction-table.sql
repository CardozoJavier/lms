CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TYPE TRANSACTION_TYPE AS ENUM ('BORROW', 'RETURN');

CREATE TABLE transaction (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    type TRANSACTION_TYPE NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    customer_id UUID NOT NULL,
    book_id UUID NOT NULL,
    FOREIGN KEY (book_id) REFERENCES book(id)
);