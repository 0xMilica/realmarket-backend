create type euserrole as enum ('ROLE_ADMIN', 'ROLE_INVESTOR', 'ROLE_ENTREPRENEUR');

alter type euserrole owner to postgres;