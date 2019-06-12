-- platform settings
INSERT INTO public.platform_settings (id, platform_min_investment) VALUES (NEXTVAL('platform_settings_seq'), 500);

-- auth state
INSERT INTO public.auth_state (id, name) VALUES (NEXTVAL('auth_state_seq'), 'CONFIRM_REGISTRATION');
INSERT INTO public.auth_state (id, name) VALUES (NEXTVAL('auth_state_seq'), 'INITIALIZE_2FA');
INSERT INTO public.auth_state (id, name) VALUES (NEXTVAL('auth_state_seq'), 'ACTIVE');

-- authorized action type
INSERT INTO public.authorized_action_type (id, name) VALUES (NEXTVAL('authorized_action_type_seq'), 'NEW_TOTP_SECRET');
INSERT INTO public.authorized_action_type (id, name) VALUES (NEXTVAL('authorized_action_type_seq'), 'NEW_PASSWORD');
INSERT INTO public.authorized_action_type (id, name) VALUES (NEXTVAL('authorized_action_type_seq'), 'NEW_EMAIL');

-- temporary token type
INSERT INTO public.temporary_token_type (id, name) VALUES (NEXTVAL('temporary_token_type_seq'), 'SETUP_2FA_TOKEN');
INSERT INTO public.temporary_token_type (id, name) VALUES (NEXTVAL('temporary_token_type_seq'), 'LOGIN_TOKEN');
INSERT INTO public.temporary_token_type (id, name) VALUES (NEXTVAL('temporary_token_type_seq'), 'REGISTRATION_TOKEN');
INSERT INTO public.temporary_token_type (id, name) VALUES (NEXTVAL('temporary_token_type_seq'), 'EMAIL_TOKEN');
INSERT INTO public.temporary_token_type (id, name) VALUES (NEXTVAL('temporary_token_type_seq'), 'RESET_PASSWORD_TOKEN');
INSERT INTO public.temporary_token_type (id, name) VALUES (NEXTVAL('temporary_token_type_seq'), 'EMAIL_CHANGE_TOKEN');
INSERT INTO public.temporary_token_type (id, name) VALUES (NEXTVAL('temporary_token_type_seq'), 'PASSWORD_VERIFIED_TOKEN');

-- user role
INSERT INTO public.user_role (id, name) VALUES (NEXTVAL('user_role_seq'), 'ROLE_ADMIN');
INSERT INTO public.user_role (id, name) VALUES (NEXTVAL('user_role_seq'), 'ROLE_INVESTOR');
INSERT INTO public.user_role (id, name) VALUES (NEXTVAL('user_role_seq'), 'ROLE_ENTREPRENEUR');

-- company category
INSERT INTO public.company_category (id, name) VALUES (NEXTVAL('company_category_seq'), 'Agriculture');
INSERT INTO public.company_category (id, name) VALUES (NEXTVAL('company_category_seq'), 'Big Data & AI');
INSERT INTO public.company_category (id, name) VALUES (NEXTVAL('company_category_seq'), 'Biotech and Medicine');
INSERT INTO public.company_category (id, name) VALUES (NEXTVAL('company_category_seq'), 'Blockchain & Cryptocurrencies');
INSERT INTO public.company_category (id, name) VALUES (NEXTVAL('company_category_seq'), 'Cyber Security');
INSERT INTO public.company_category (id, name) VALUES (NEXTVAL('company_category_seq'), 'Design & Graphics');
INSERT INTO public.company_category (id, name) VALUES (NEXTVAL('company_category_seq'), 'eCommerce');
INSERT INTO public.company_category (id, name) VALUES (NEXTVAL('company_category_seq'), 'Education');
INSERT INTO public.company_category (id, name) VALUES (NEXTVAL('company_category_seq'), 'Energy & Sustainability');
INSERT INTO public.company_category (id, name) VALUES (NEXTVAL('company_category_seq'), 'Fashion');
INSERT INTO public.company_category (id, name) VALUES (NEXTVAL('company_category_seq'), 'Financial Services');
INSERT INTO public.company_category (id, name) VALUES (NEXTVAL('company_category_seq'), 'Food & Drinks');
INSERT INTO public.company_category (id, name) VALUES (NEXTVAL('company_category_seq'), 'Gaming');
INSERT INTO public.company_category (id, name) VALUES (NEXTVAL('company_category_seq'), 'Hardware');
INSERT INTO public.company_category (id, name) VALUES (NEXTVAL('company_category_seq'), 'Health & Fitness');
INSERT INTO public.company_category (id, name) VALUES (NEXTVAL('company_category_seq'), 'Human Resources');
INSERT INTO public.company_category (id, name) VALUES (NEXTVAL('company_category_seq'), 'Insurance');
INSERT INTO public.company_category (id, name) VALUES (NEXTVAL('company_category_seq'), 'IoT (The Internet of Things)');
INSERT INTO public.company_category (id, name) VALUES (NEXTVAL('company_category_seq'), 'Legal Services');
INSERT INTO public.company_category (id, name) VALUES (NEXTVAL('company_category_seq'), 'Marketing & Advertising');
INSERT INTO public.company_category (id, name) VALUES (NEXTVAL('company_category_seq'), 'Media & Communication');
INSERT INTO public.company_category (id, name) VALUES (NEXTVAL('company_category_seq'), 'Mobility & Logistics');
INSERT INTO public.company_category (id, name) VALUES (NEXTVAL('company_category_seq'), 'Music');
INSERT INTO public.company_category (id, name) VALUES (NEXTVAL('company_category_seq'), 'Real Estate');
INSERT INTO public.company_category (id, name) VALUES (NEXTVAL('company_category_seq'), 'Sharing & Gig Economy (Rent Services to the Companies)');
INSERT INTO public.company_category (id, name) VALUES (NEXTVAL('company_category_seq'), 'Social Media & Events');
INSERT INTO public.company_category (id, name) VALUES (NEXTVAL('company_category_seq'), 'Travel');
INSERT INTO public.company_category (id, name) VALUES (NEXTVAL('company_category_seq'), 'Video & Photography');
INSERT INTO public.company_category (id, name) VALUES (NEXTVAL('company_category_seq'), 'Virtual & Augmented Reality');

-- campaign state
INSERT INTO public.campaign_state (id, name) VALUES (NEXTVAL('campaign_state_seq'), 'INITIAL');
INSERT INTO public.campaign_state (id, name) VALUES (NEXTVAL('campaign_state_seq'), 'REVIEW_READY');
INSERT INTO public.campaign_state (id, name) VALUES (NEXTVAL('campaign_state_seq'), 'AUDIT');
INSERT INTO public.campaign_state (id, name) VALUES (NEXTVAL('campaign_state_seq'), 'FINANCE_PROPOSITION');
INSERT INTO public.campaign_state (id, name) VALUES (NEXTVAL('campaign_state_seq'), 'LEAD_INVESTMENT');
INSERT INTO public.campaign_state (id, name) VALUES (NEXTVAL('campaign_state_seq'), 'ACTIVE');
INSERT INTO public.campaign_state (id, name) VALUES (NEXTVAL('campaign_state_seq'), 'POST_CAMPAIGN');
INSERT INTO public.campaign_state (id, name) VALUES (NEXTVAL('campaign_state_seq'), 'DELETED');

-- request state
INSERT INTO public.request_state (id, name) VALUES (NEXTVAL('request_state_seq'), 'PENDING');
INSERT INTO public.request_state (id, name) VALUES (NEXTVAL('request_state_seq'), 'APPROVED');
INSERT INTO public.request_state (id, name) VALUES (NEXTVAL('request_state_seq'), 'DECLINED');

-- campaign topic type
INSERT INTO public.campaign_topic_type (id, name) VALUES (NEXTVAL('campaign_topic_type_seq'), 'PROBLEM');
INSERT INTO public.campaign_topic_type (id, name) VALUES (NEXTVAL('campaign_topic_type_seq'), 'SOLUTION');
INSERT INTO public.campaign_topic_type (id, name) VALUES (NEXTVAL('campaign_topic_type_seq'), 'RISK_AND_COMPETITION');
INSERT INTO public.campaign_topic_type (id, name) VALUES (NEXTVAL('campaign_topic_type_seq'), 'CAPITAL_AND_EXIT');
INSERT INTO public.campaign_topic_type (id, name) VALUES (NEXTVAL('campaign_topic_type_seq'), 'UPDATES');
INSERT INTO public.campaign_topic_type (id, name) VALUES (NEXTVAL('campaign_topic_type_seq'), 'OVERVIEW');
INSERT INTO public.campaign_topic_type (id, name) VALUES (NEXTVAL('campaign_topic_type_seq'), 'MARKET');
INSERT INTO public.campaign_topic_type (id, name) VALUES (NEXTVAL('campaign_topic_type_seq'), 'TRACTION');

-- campaign document access level
INSERT INTO public.campaign_document_access_level (id, name) VALUES (NEXTVAL('campaign_document_access_level_seq'), 'PLATFORM_ADMINS');
INSERT INTO public.campaign_document_access_level (id, name) VALUES (NEXTVAL('campaign_document_access_level_seq'), 'INVESTORS');
INSERT INTO public.campaign_document_access_level (id, name) VALUES (NEXTVAL('campaign_document_access_level_seq'), 'PUBLIC');

-- campaign document type
INSERT INTO public.campaign_document_type (id, name) VALUES (NEXTVAL('campaign_document_type_seq'), 'DOCTYPE_APR_PAPER');
INSERT INTO public.campaign_document_type (id, name) VALUES (NEXTVAL('campaign_document_type_seq'), 'DOCTYPE_BUSINESS_PLAN');
INSERT INTO public.campaign_document_type (id, name) VALUES (NEXTVAL('campaign_document_type_seq'), 'DOCTYPE_PITCH_DECK');
INSERT INTO public.campaign_document_type (id, name) VALUES (NEXTVAL('campaign_document_type_seq'), 'DOCTYPE_DUE_DILIGENCE_OTHER');

-- person
INSERT INTO public.person (id, address, city, country_for_taxation, country_of_residence, email, first_name, last_name, phone_number, profile_picture_url) VALUES (NEXTVAL('person_seq'), 'Modene', 'Novi Sad', null, 'BB', 'marina.nenic@realmarket.io', 'Marina', 'Nenic', null, null);
INSERT INTO public.person (id, address, city, country_for_taxation, country_of_residence, email, first_name, last_name, phone_number, profile_picture_url) VALUES (NEXTVAL('person_seq'), 'Modene 1', 'Novi Sad', 'BS', 'AU', 'angelina.vujanovic@realmarket.io', 'Angelina', 'Vujanovic', null, null);
INSERT INTO public.person (id, address, city, country_for_taxation, country_of_residence, email, first_name, last_name, phone_number, profile_picture_url) VALUES (NEXTVAL('person_seq'), 'Modene', 'Novi Sad', null, 'BB', 'nikola.todorovic@realmarket.io', 'Nikola', 'Todorovic', null, null);
INSERT INTO public.person (id, address, city, country_for_taxation, country_of_residence, email, first_name, last_name, phone_number, profile_picture_url) VALUES (NEXTVAL('person_seq'), 'Modene 1', 'Novi Sad', 'BS', 'AU', 'vladimir.ivkovic@realmarket.io', 'Vladimir', 'Ivkovic', null, null);
INSERT INTO public.person (id, address, city, country_for_taxation, country_of_residence, email, first_name, last_name, phone_number, profile_picture_url) VALUES (NEXTVAL('person_seq'), 'Modene 1', 'Novi Sad', null, 'BB', 'alice.rm@mailinator.com', 'Alice', 'Entrepreneur', null, null);
INSERT INTO public.person (id, address, city, country_for_taxation, country_of_residence, email, first_name, last_name, phone_number, profile_picture_url) VALUES (NEXTVAL('person_seq'), 'Modene 1', 'Novi Sad', 'BS', 'AU', 'ognjen.vlajic@realmarket.io', 'Ginny ', 'Investor', null, null);
INSERT INTO public.person (id, address, city, country_for_taxation, country_of_residence, email, first_name, last_name, phone_number, profile_picture_url) VALUES (NEXTVAL('person_seq'), 'Modene 1', 'Novi Sad', null, 'BB', 'ent.rm@mailinator.com', 'Entepreneur', 'Entrepreneur', null, null);

-- auth
INSERT INTO public.auth (id, state_id, password, user_role_id, blocked, totp_secret, username, person_id) VALUES (NEXTVAL('auth_seq'), 3, '$2a$10$E6Pwa0HnLWOoZIBBuZVyV.TodJ88sb.iBin.U2e9YSp7Z0ZaPXck2', 3, false, 'CYFMMB56QFIVKUFG', 'marina', 1);
INSERT INTO public.auth (id, state_id, password, user_role_id, blocked, totp_secret, username, person_id) VALUES (NEXTVAL('auth_seq'), 3, '$2a$10$Nd8uSgyw8fYxUqMNET.TruoaYI.te6D3f5YPVVIm0QqRhzJVCGgma', 3, false, '7O2LOBXXCYHK4GJ4', 'angelina', 2);
INSERT INTO public.auth (id, state_id, password, user_role_id, blocked, totp_secret, username, person_id) VALUES (NEXTVAL('auth_seq'), 3, '$2a$10$Nd8uSgyw8fYxUqMNET.TruoaYI.te6D3f5YPVVIm0QqRhzJVCGgma', 3, false, '7O2LOBXXCYHK4GJ4', 'nikola', 3);
INSERT INTO public.auth (id, state_id, password, user_role_id, blocked, totp_secret, username, person_id) VALUES (NEXTVAL('auth_seq'), 3, '$2a$10$E6Pwa0HnLWOoZIBBuZVyV.TodJ88sb.iBin.U2e9YSp7Z0ZaPXck2', 3, false, 'CYFMMB56QFIVKUFG', 'vladimir', 4);
/*password is testPASS123 */
INSERT INTO public.auth (id, state_id, password, user_role_id, blocked, totp_secret, username, person_id) VALUES (NEXTVAL('auth_seq'), 3, '$2a$10$nTWQERaNUio88aEHzSVhrOmzsH6XRCfrftpJDxNqwsEdDjh8Y4PFi', 3, false, 'secret', 'entrepreneur', 5);
INSERT INTO public.auth (id, state_id, password, user_role_id, blocked, totp_secret, username, person_id) VALUES (NEXTVAL('auth_seq'), 3, '$2a$10$nTWQERaNUio88aEHzSVhrOmzsH6XRCfrftpJDxNqwsEdDjh8Y4PFi', 2, false, 'secret', 'investor', 6);
INSERT INTO public.auth (id, state_id, password, user_role_id, blocked, totp_secret, username, person_id) VALUES (NEXTVAL('auth_seq'), 3, '$2a$10$nTWQERaNUio88aEHzSVhrOmzsH6XRCfrftpJDxNqwsEdDjh8Y4PFi', 3, false, 'secret', 'entrepreneur2', 7);

INSERT INTO public.remember_me_cookie (id, expiration_time, value, auth_id) VALUES (1, TO_DATE('01/05/2022', 'DD/MM/YYYY'), '1234', 5);