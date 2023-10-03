CREATE TABLE IF NOT EXISTS avatars (
        id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
        url varchar(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
        id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
        username varchar(20) NOT NULL,
        email varchar(100) NOT NULL UNIQUE,
        password varchar(100) NOT NULL,
        avatar_id BIGINT NOT NULL,
        role varchar(20) NOT NULL,
        created_on timestamp NOT NULL,
        status varchar(25) NOT NULL,
        removed_on timestamp,
        removed_by BIGINT,
        CONSTRAINT fk_avatar FOREIGN KEY (avatar_id) REFERENCES avatars (id),
        CONSTRAINT fk_removed_by FOREIGN KEY (removed_by) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS bank_accounts (
        id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
        owner_id BIGINT NOT NULL,
        balance BIGINT NOT NULL,
        status varchar(30) NOT NULL,
        type varchar(25) NOT NULL,
        created_on timestamp NOT NULL,
        CONSTRAINT fk_owner FOREIGN KEY (owner_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS games (
        id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
        game_name varchar(100) NOT NULL,
        game_comment varchar(255),
        created_on timestamp NOT NULL,
        created_by BIGINT NOT NULL,
        status varchar(25) NOT NULL,
        start_balance BIGINT NOT NULL,
        CONSTRAINT fk_creator FOREIGN KEY (created_by) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS payments (
        id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
        amount BIGINT NOT NULL,
        payment_on timestamp NOT NULL,
        account_id_from BIGINT NOT NULL,
        account_id_to BIGINT NOT NULL,
        message varchar(200),
        game_id BIGINT NOT NULL,
        CONSTRAINT fk_account_from FOREIGN KEY (account_id_from) REFERENCES bank_accounts(id),
        CONSTRAINT fk_account_to FOREIGN KEY (account_id_to) REFERENCES bank_accounts(id),
        CONSTRAINT fk_game FOREIGN KEY (game_id) REFERENCES games(id)
);

CREATE TABLE IF NOT EXISTS participants (
        user_id BIGINT NOT NULL,
        game_id BIGINT NOT NULL,
        PRIMARY KEY (user_id, game_id),
        CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id),
        CONSTRAINT fk_game_participation FOREIGN KEY (game_id) REFERENCES games(id)
);

CREATE TABLE IF NOT EXISTS password_reset_token (
        id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
        user_id BIGINT NOT NULL,
        token varchar(255) NOT NULL,
        expiry_date timestamp NOT NULL,
        CONSTRAINT fk_user_token FOREIGN KEY (user_id) REFERENCES users(id)
);
