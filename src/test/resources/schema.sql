CREATE TABLE categories
(
    id        UUID PRIMARY KEY,
    name      VARCHAR(255) NOT NULL,
    min_price DOUBLE       NOT NULL,
    max_price DOUBLE       NOT NULL,
    CONSTRAINT category_pkey PRIMARY KEY (id)
);

CREATE TABLE products
(
    id          UUID PRIMARY KEY,
    name        VARCHAR(255)             NOT NULL,
    description VARCHAR(255),
    price       DOUBLE                   NOT NULL,
    quantity    INT                      NOT NULL,
    created_at  timestamp with time zone NOT NULL,
    category_id UUID                     NOT NULL,
    CONSTRAINT category_fkey FOREIGN KEY (category_id)
        REFERENCES categories (id)
);

CREATE TABLE blocked_words
(
    id   UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS public.products_history
(
    id         UUID PRIMARY KEY,
    product_id UUID                        NOT NULL,
    field_name VARCHAR(255)                NOT NULL,
    new_value  VARCHAR(255)                NOT NULL,
    old_value  VARCHAR(255)                NOT NULL,
    created_at timestamp without time zone NOT NULL,
    CONSTRAINT products_history_fkey FOREIGN KEY (product_id)
        REFERENCES products (id)
);



