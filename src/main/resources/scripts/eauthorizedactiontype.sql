create type eauthorizedactiontype as enum ('NEW_TOTP_SECRET', 'NEW_PASSWORD', 'NEW_EMAIL');

alter type eauthorizedactiontype owner to postgres;

