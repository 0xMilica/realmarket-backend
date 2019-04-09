create type etemporarytokentype as enum ('SETUP_2FA_TOKEN', 'LOGIN_TOKEN', 'REGISTRATION_TOKEN', 'EMAIL_TOKEN', 'RESET_PASSWORD_TOKEN', 'EMAIL_CHANGE_TOKEN', 'PASSWORD_VERIFIED_TOKEN');

alter type etemporarytokentype owner to postgres;