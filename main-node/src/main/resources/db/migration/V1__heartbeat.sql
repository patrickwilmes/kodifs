CREATE TABLE IF NOT EXISTS heartbeat
(
    host               text NOT NULL,
    port               text NOT NULL,
    active_connections int,
    cpu_load           float,
    used_memory        bigint,
    total_memory       bigint,
    free_disk_space    bigint,
    last_hb            timestamp DEFAULT NOW(),
    healthy            boolean,

    PRIMARY KEY (host, port)
);
