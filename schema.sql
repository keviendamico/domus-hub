CREATE TABLE IF NOT EXISTS light_event_log (
    id bigint generated always as identity primary key,
    room varchar(100) not null,
    chat_id varchar(100) not null,
    action varchar(50) not null,
    created_at timestamp not null
);

CREATE TABLE IF NOT EXISTS light_session (
    id bigint generated always as identity primary key,
    room varchar(100) not null,
    started_at timestamp not null,
    ended_at timestamp,
    duration_seconds bigint
);
