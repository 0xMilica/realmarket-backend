create type ecampaigndocumentaccesslevel as enum ('PLATFORM_ADMINS', 'INVESTORS', 'PUBLIC');

alter type ecampaigndocumentaccesslevel owner to postgres;