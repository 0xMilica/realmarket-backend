-- platform settings
INSERT INTO public.platform_settings (id, platform_min_investment) VALUES (1, 500);

-- person
INSERT INTO public.person (id, address, city, country_for_taxation, country_of_residence, email, first_name, last_name, phone_number, profile_picture_url) VALUES (NEXTVAL('person_seq'), 'Modene', 'Novi Sad', null, 'BB', 'marina.nenic@realmarket.io', 'Marina', 'Nenic', null, null);
INSERT INTO public.person (id, address, city, country_for_taxation, country_of_residence, email, first_name, last_name, phone_number, profile_picture_url) VALUES (NEXTVAL('person_seq'), 'Modene 1', 'Novi Sad', 'BS', 'AU', 'angelina.vujanovic@realmarket.io', 'Angelina', 'Vujanovic', null, null);
INSERT INTO public.person (id, address, city, country_for_taxation, country_of_residence, email, first_name, last_name, phone_number, profile_picture_url) VALUES (NEXTVAL('person_seq'), 'Modene', 'Novi Sad', null, 'BB', 'nikola.todorovic@realmarket.io', 'Nikola', 'Todorovic', null, null);
INSERT INTO public.person (id, address, city, country_for_taxation, country_of_residence, email, first_name, last_name, phone_number, profile_picture_url) VALUES (NEXTVAL('person_seq'), 'Modene 1', 'Novi Sad', 'BS', 'AU', 'vladimir.ivkovic@realmarket.io', 'Vladimir', 'Ivkovic', null, null);
INSERT INTO public.person (id, address, city, country_for_taxation, country_of_residence, email, first_name, last_name, phone_number, profile_picture_url) VALUES (NEXTVAL('person_seq'), 'Modene 1', 'Novi Sad', null, 'BB', 'alice.rm@mailinator.com', 'Alice', 'Entrepreneur', null, null);
INSERT INTO public.person (id, address, city, country_for_taxation, country_of_residence, email, first_name, last_name, phone_number, profile_picture_url) VALUES (NEXTVAL('person_seq'), 'Modene 1', 'Novi Sad', 'BS', 'AU', 'ginny.rm@mailinator.com', 'Ginny ', 'Investor', null, null);
INSERT INTO public.person (id, address, city, country_for_taxation, country_of_residence, email, first_name, last_name, phone_number, profile_picture_url) VALUES (NEXTVAL('person_seq'), 'Modene 1', 'Novi Sad', null, 'BB', 'ent.rm@mailinator.com', 'Entepreneur', 'Entrepreneur', null, null);

-- auth
INSERT INTO public.auth (id, state, password, user_role, blocked, totp_secret, username, person_id) VALUES (NEXTVAL('auth_seq'), 'ACTIVE', '$2a$10$E6Pwa0HnLWOoZIBBuZVyV.TodJ88sb.iBin.U2e9YSp7Z0ZaPXck2', 'ROLE_ENTREPRENEUR', false, 'CYFMMB56QFIVKUFG', 'marina', 1);
INSERT INTO public.auth (id, state, password, user_role, blocked, totp_secret, username, person_id) VALUES (NEXTVAL('auth_seq'), 'ACTIVE', '$2a$10$Nd8uSgyw8fYxUqMNET.TruoaYI.te6D3f5YPVVIm0QqRhzJVCGgma', 'ROLE_ENTREPRENEUR', false, '7O2LOBXXCYHK4GJ4', 'angelina', 2);
INSERT INTO public.auth (id, state, password, user_role, blocked, totp_secret, username, person_id) VALUES (NEXTVAL('auth_seq'), 'ACTIVE', '$2a$10$Nd8uSgyw8fYxUqMNET.TruoaYI.te6D3f5YPVVIm0QqRhzJVCGgma', 'ROLE_ENTREPRENEUR', false, '7O2LOBXXCYHK4GJ4', 'nikola', 3);
INSERT INTO public.auth (id, state, password, user_role, blocked, totp_secret, username, person_id) VALUES (NEXTVAL('auth_seq'), 'ACTIVE', '$2a$10$E6Pwa0HnLWOoZIBBuZVyV.TodJ88sb.iBin.U2e9YSp7Z0ZaPXck2', 'ROLE_ENTREPRENEUR', false, 'CYFMMB56QFIVKUFG', 'vladimir', 4);
/*password is testPASS123 */
INSERT INTO public.auth (id, state, password, user_role, blocked, totp_secret, username, person_id) VALUES (NEXTVAL('auth_seq'), 'ACTIVE', '$2a$10$nTWQERaNUio88aEHzSVhrOmzsH6XRCfrftpJDxNqwsEdDjh8Y4PFi', 'ROLE_ENTREPRENEUR', false, 'secret', 'entrepreneur', 5);
INSERT INTO public.auth (id, state, password, user_role, blocked, totp_secret, username, person_id) VALUES (NEXTVAL('auth_seq'), 'ACTIVE', '$2a$10$nTWQERaNUio88aEHzSVhrOmzsH6XRCfrftpJDxNqwsEdDjh8Y4PFi', 'ROLE_INVESTOR', false, 'secret', 'investor', 6);
INSERT INTO public.auth (id, state, password, user_role, blocked, totp_secret, username, person_id) VALUES (NEXTVAL('auth_seq'), 'ACTIVE', '$2a$10$nTWQERaNUio88aEHzSVhrOmzsH6XRCfrftpJDxNqwsEdDjh8Y4PFi', 'ROLE_ENTREPRENEUR', false, 'secret', 'entrepreneur2', 7);

-- company category
INSERT INTO public.company_category (id, name) VALUES (1, 'Agriculture');
INSERT INTO public.company_category (id, name) VALUES (2, 'Big Data & AI');
INSERT INTO public.company_category (id, name) VALUES (3, 'Biotech and Medicine');
INSERT INTO public.company_category (id, name) VALUES (4, 'Blockchain & Cryptocurrencies');
INSERT INTO public.company_category (id, name) VALUES (5, 'Cyber Security');
INSERT INTO public.company_category (id, name) VALUES (6, 'Design & Graphics');
INSERT INTO public.company_category (id, name) VALUES (7, 'eCommerce');
INSERT INTO public.company_category (id, name) VALUES (8, 'Education');
INSERT INTO public.company_category (id, name) VALUES (9, 'Energy & Sustainability');
INSERT INTO public.company_category (id, name) VALUES (10, 'Fashion');
INSERT INTO public.company_category (id, name) VALUES (11, 'Financial Services');
INSERT INTO public.company_category (id, name) VALUES (12, 'Food & Drinks');
INSERT INTO public.company_category (id, name) VALUES (13, 'Gaming');
INSERT INTO public.company_category (id, name) VALUES (14, 'Hardware');
INSERT INTO public.company_category (id, name) VALUES (15, 'Health & Fitness');
INSERT INTO public.company_category (id, name) VALUES (16, 'Human Resources');
INSERT INTO public.company_category (id, name) VALUES (17, 'Insurance');
INSERT INTO public.company_category (id, name) VALUES (18, 'IoT (The Internet of Things)');
INSERT INTO public.company_category (id, name) VALUES (19, 'Legal Services');
INSERT INTO public.company_category (id, name) VALUES (20, 'Marketing & Advertising');
INSERT INTO public.company_category (id, name) VALUES (21, 'Media & Communication');
INSERT INTO public.company_category (id, name) VALUES (22, 'Mobility & Logistics');
INSERT INTO public.company_category (id, name) VALUES (23, 'Music');
INSERT INTO public.company_category (id, name) VALUES (24, 'Real Estate');
INSERT INTO public.company_category (id, name) VALUES (25, 'Sharing & Gig Economy (Rent Services to the Companies)');
INSERT INTO public.company_category (id, name) VALUES (26, 'Social Media & Events');
INSERT INTO public.company_category (id, name) VALUES (27, 'Travel');
INSERT INTO public.company_category (id, name) VALUES (28, 'Video & Photography');
INSERT INTO public.company_category (id, name) VALUES (29, 'Virtual & Augmented Reality');

-- company
INSERT INTO public.company (id, name, tax_identifier, bank_account, county, city, address, website, logo_url, featured_image_url, linkedin_url, twitter_url, facebook_url, custom_url, company_category_id, auth_id) VALUES (NEXTVAL('company_seq'), 'company', 'tax_id_1', '123456789', 'Serbia', 'Novi Sad', 'Modene 1', 'www.company.com', null, null, null, null, null, null, 1, 7);

-- campaign state
INSERT INTO public.campaign_state (id, name) VALUES (NEXTVAL('campaign_state_seq'), 'initial');
INSERT INTO public.campaign_state (id, name) VALUES (NEXTVAL('campaign_state_seq'), 'readyForAudit');
INSERT INTO public.campaign_state (id, name) VALUES (NEXTVAL('campaign_state_seq'), 'auditing');
INSERT INTO public.campaign_state (id, name) VALUES (NEXTVAL('campaign_state_seq'), 'auditingCompleted');
INSERT INTO public.campaign_state (id, name) VALUES (NEXTVAL('campaign_state_seq'), 'leadInvestment');
INSERT INTO public.campaign_state (id, name) VALUES (NEXTVAL('campaign_state_seq'), 'crowdfunding');
INSERT INTO public.campaign_state (id, name) VALUES (NEXTVAL('campaign_state_seq'), 'postCampaign');

-- campaign topic type
INSERT INTO public.campaign_topic_type (id, name) VALUES (1, 'PROBLEM');
INSERT INTO public.campaign_topic_type (id, name) VALUES (2, 'SOLUTION');
INSERT INTO public.campaign_topic_type (id, name) VALUES (3, 'RISK_AND_COMPETITION');
INSERT INTO public.campaign_topic_type (id, name) VALUES (4, 'CAPITAL_AND_EXIT');
INSERT INTO public.campaign_topic_type (id, name) VALUES (5, 'UPDATES');
INSERT INTO public.campaign_topic_type (id, name) VALUES (6, 'OVERVIEW');
INSERT INTO public.campaign_topic_type (id, name) VALUES (7, 'MARKET');
INSERT INTO public.campaign_topic_type (id, name) VALUES (8, 'TRACTION');

-- campaign
INSERT INTO public.campaign (id, name, url_friendly_name, funding_goals, time_to_raise_funds, min_equity_offered, max_equity_offered, min_investment, market_image_url, active, company_id) VALUES (NEXTVAL('campaign_seq'), 'campaign 1', 'campaign1_url', 120000, 60, 3.2, 8.1, 2000, null, false, 1);
INSERT INTO public.campaign (id, name, url_friendly_name, funding_goals, time_to_raise_funds, min_equity_offered, max_equity_offered, min_investment, market_image_url, active, company_id) VALUES (NEXTVAL('campaign_seq'), 'campaign 2', 'campaign2_url', 81000, 60, 1.2, 7.4, 3500, null, true, 1);

-- campaign topic
INSERT INTO public.campaign_topic (id, content, campaign_topic_type_id, campaign_id) VALUES (NEXTVAL('campaign_topic_seq'), 'topic 1', 1, 1);
INSERT INTO public.campaign_topic (id, content, campaign_topic_type_id, campaign_id) VALUES (NEXTVAL('campaign_topic_seq'), 'topic 2', 2, 2);

-- campaign topic image
INSERT INTO public.campaign_topic_image (id, url, campaign_topic_id)  VALUES (NEXTVAL('campaign_topic_image_seq'), null, 1);
INSERT INTO public.campaign_topic_image (id, url, campaign_topic_id)  VALUES (NEXTVAL('campaign_topic_image_seq'), null, 2);

-- campaign document
INSERT INTO public.campaign_document (id, title, access_level, type, url, upload_date, campaign_id) VALUES (NEXTVAL('campaign_document_seq'), 'document 1', 'PLATFORM_ADMINS', 'DOCTYPE_APR_PAPER', 'url', TO_DATE('17/05/2019', 'DD/MM/YYYY'), 1);
INSERT INTO public.campaign_document (id, title, access_level, type, url, upload_date, campaign_id) VALUES (NEXTVAL('campaign_document_seq'), 'document 2', 'INVESTORS', 'DOCTYPE_BUSINESS_PLAN', 'url', TO_DATE('17/05/2019', 'DD/MM/YYYY'), 1);
INSERT INTO public.campaign_document (id, title, access_level, type, url, upload_date, campaign_id) VALUES (NEXTVAL('campaign_document_seq'), 'document 3', 'PUBLIC', 'DOCTYPE_PITCH_DECK', 'url', TO_DATE('17/05/2019', 'DD/MM/YYYY'), 1);
INSERT INTO public.campaign_document (id, title, access_level, type, url, upload_date, campaign_id) VALUES (NEXTVAL('campaign_document_seq'), 'document 4', 'PUBLIC', 'DOCTYPE_DUE_DILIGENCE_OTHER', 'url', TO_DATE('17/05/2019', 'DD/MM/YYYY'), 1);
INSERT INTO public.campaign_document (id, title, access_level, type, url, upload_date, campaign_id) VALUES (NEXTVAL('campaign_document_seq'), 'document 5', 'INVESTORS', 'DOCTYPE_APR_PAPER', 'url', TO_DATE('17/05/2019', 'DD/MM/YYYY'), 2);
INSERT INTO public.campaign_document (id, title, access_level, type, url, upload_date, campaign_id) VALUES (NEXTVAL('campaign_document_seq'), 'document 6', 'INVESTORS', 'DOCTYPE_BUSINESS_PLAN', 'url', TO_DATE('17/05/2019', 'DD/MM/YYYY'), 2);
INSERT INTO public.campaign_document (id, title, access_level, type, url, upload_date, campaign_id) VALUES (NEXTVAL('campaign_document_seq'), 'document 7', 'PUBLIC', 'DOCTYPE_PITCH_DECK', 'url', TO_DATE('17/05/2019', 'DD/MM/YYYY'), 2);

-- campaign team member
INSERT INTO public.campaign_team_member (id, name, title, description, photo_url, linkedin_url, twitter_url, facebook_url, custom_profile_url, order_number, campaign_id) VALUES (NEXTVAL('campaign_team_member_seq'), 'team member 1', 'team1', 'description for team 1', null, null, null, null, null, 1, 1);
INSERT INTO public.campaign_team_member (id, name, title, description, photo_url, linkedin_url, twitter_url, facebook_url, custom_profile_url, order_number, campaign_id) VALUES (NEXTVAL('campaign_team_member_seq'), 'team member 2', 'team2', 'description for team 2', null, null, null, null, null, 1, 2);

-- campaign investor
INSERT INTO public.campaign_investor (id, is_anonymous, name, location, invested_amount, description, photo_url, linkedin_url, twitter_url, facebook_url, custom_profile_url, order_number, campaign_id) VALUES (NEXTVAL('campaign_investor_seq'), false, 'investor 1', 'Novi Sad', 10000, 'investor description', null, null, null, null, null, 2, 1);
INSERT INTO public.campaign_investor (id, is_anonymous, name, location, invested_amount, description, photo_url, linkedin_url, twitter_url, facebook_url, custom_profile_url, order_number, campaign_id) VALUES (NEXTVAL('campaign_investor_seq'), true, 'investor 2', 'Novi Sad', 10000, 'investor description', null, null, null, null, null, 3, 1);
INSERT INTO public.campaign_investor (id, is_anonymous, name, location, invested_amount, description, photo_url, linkedin_url, twitter_url, facebook_url, custom_profile_url, order_number, campaign_id) VALUES (NEXTVAL('campaign_investor_seq'), true, 'investor 2', 'Novi Sad', 10000, 'investor description', null, null, null, null, null, 2, 2);