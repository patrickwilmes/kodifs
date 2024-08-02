CREATE TABLE IF NOT EXISTS heartbeat
(
    host    text NOT NULL,
    port    text NOT NULL,
    last_hb timestamp DEFAULT NOW(),
    healthy boolean,

    PRIMARY KEY (host, port)
);
