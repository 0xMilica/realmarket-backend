insert into public.campaign_state (id, name) values (nextval('campaign_state_seq'), 'initial');
insert into public.campaign_state (id, name) values (nextval('campaign_state_seq'), 'readyForAudit');
insert into public.campaign_state (id, name) values (nextval('campaign_state_seq'), 'auditing');
insert into public.campaign_state (id, name) values (nextval('campaign_state_seq'), 'auditingCompleted');
insert into public.campaign_state (id, name) values (nextval('campaign_state_seq'), 'leadInvestment');
insert into public.campaign_state (id, name) values (nextval('campaign_state_seq'), 'crowdfunding');
insert into public.campaign_state (id, name) values (nextval('campaign_state_seq'), 'postCampaign');