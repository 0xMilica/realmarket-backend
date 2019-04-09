create type eauthstate as enum ('CONFIRM_REGISTRATION', 'INITIALIZE_2FA', 'ACTIVE');

alter type eauthstate owner to postgres;