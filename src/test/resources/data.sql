INSERT INTO categories (id, name, min_price, max_price)
VALUES ('34b1101f-835b-4057-af7a-0256200ae22c',	'Electronics', 50, 50000);

INSERT INTO products (id, name, description, price, quantity, created_at, category_id)
VALUES ('123e4567-e89b-12d3-a456-426614174000', 'existingName', 'Existing Description', 3000.0, 5, NOW(), '34b1101f-835b-4057-af7a-0256200ae22c');

INSERT INTO blocked_words (id, name)
VALUES ('e792f10d-c4e3-47b2-9123-1c32a15c16bc', 'blockedWord');