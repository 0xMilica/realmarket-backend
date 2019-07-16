-- authorized action type
INSERT INTO public.authorized_action_type (id, name) VALUES (NEXTVAL('authorized_action_type_seq'), 'NEW_TOTP_SECRET');
INSERT INTO public.authorized_action_type (id, name) VALUES (NEXTVAL('authorized_action_type_seq'), 'NEW_EMAIL');
INSERT INTO public.authorized_action_type (id, name) VALUES (NEXTVAL('authorized_action_type_seq'), 'NEW_PASSWORD');

-- auth state
INSERT INTO public.auth_state (id, name) VALUES (NEXTVAL('auth_state_seq'), 'CONFIRM_REGISTRATION');
INSERT INTO public.auth_state (id, name) VALUES (NEXTVAL('auth_state_seq'), 'INITIALIZE_2FA');
INSERT INTO public.auth_state (id, name) VALUES (NEXTVAL('auth_state_seq'), 'ACTIVE');

-- campaign document type
INSERT INTO public.campaign_document_type (id, name) VALUES (NEXTVAL('campaign_document_type_seq'), 'DOCTYPE_DUE_DILIGENCE');
INSERT INTO public.campaign_document_type (id, name) VALUES (NEXTVAL('campaign_document_type_seq'), 'DOCTYPE_LEGAL');

-- campaign state
INSERT INTO public.campaign_state (id, name) VALUES (NEXTVAL('campaign_state_seq'), 'INITIAL');
INSERT INTO public.campaign_state (id, name) VALUES (NEXTVAL('campaign_state_seq'), 'REVIEW_READY');
INSERT INTO public.campaign_state (id, name) VALUES (NEXTVAL('campaign_state_seq'), 'AUDIT');
INSERT INTO public.campaign_state (id, name) VALUES (NEXTVAL('campaign_state_seq'), 'LAUNCH_READY');
INSERT INTO public.campaign_state (id, name) VALUES (NEXTVAL('campaign_state_seq'), 'ACTIVE');
INSERT INTO public.campaign_state (id, name) VALUES (NEXTVAL('campaign_state_seq'), 'POST_CAMPAIGN');
INSERT INTO public.campaign_state (id, name) VALUES (NEXTVAL('campaign_state_seq'), 'DELETED');

-- campaign topic type
INSERT INTO public.campaign_topic_type (id, name) VALUES (NEXTVAL('campaign_topic_type_seq'), 'PROBLEM');
INSERT INTO public.campaign_topic_type (id, name) VALUES (NEXTVAL('campaign_topic_type_seq'), 'SOLUTION');
INSERT INTO public.campaign_topic_type (id, name) VALUES (NEXTVAL('campaign_topic_type_seq'), 'RISK_AND_COMPETITION');
INSERT INTO public.campaign_topic_type (id, name) VALUES (NEXTVAL('campaign_topic_type_seq'), 'CAPITAL_AND_EXIT');
INSERT INTO public.campaign_topic_type (id, name) VALUES (NEXTVAL('campaign_topic_type_seq'), 'UPDATES');
INSERT INTO public.campaign_topic_type (id, name) VALUES (NEXTVAL('campaign_topic_type_seq'), 'OVERVIEW');
INSERT INTO public.campaign_topic_type (id, name) VALUES (NEXTVAL('campaign_topic_type_seq'), 'MARKET');
INSERT INTO public.campaign_topic_type (id, name) VALUES (NEXTVAL('campaign_topic_type_seq'), 'TRACTION');

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

-- company document type
INSERT INTO public.company_document_type (id, name) VALUES (NEXTVAL('company_document_type_seq'), 'DOCTYPE_APR_PAPER');
INSERT INTO public.company_document_type (id, name) VALUES (NEXTVAL('company_document_type_seq'), 'DOCTYPE_BUSINESS_PLAN');
INSERT INTO public.company_document_type (id, name) VALUES (NEXTVAL('company_document_type_seq'), 'DOCTYPE_PITCH_DECK');
INSERT INTO public.company_document_type (id, name) VALUES (NEXTVAL('company_document_type_seq'), 'DOCTYPE_BANK');

-- document access level
INSERT INTO public.document_access_level (id, name) VALUES (NEXTVAL('document_access_level_seq'), 'PLATFORM_ADMINS');
INSERT INTO public.document_access_level (id, name) VALUES (NEXTVAL('document_access_level_seq'), 'INVESTORS');
INSERT INTO public.document_access_level (id, name) VALUES (NEXTVAL('document_access_level_seq'), 'PUBLIC');

-- investment state
INSERT INTO public.investment_state (id, name) VALUES (NEXTVAL('investment_state_seq'), 'INITIAL');
INSERT INTO public.investment_state (id, name) VALUES (NEXTVAL('investment_state_seq'), 'OWNER_APPROVED');
INSERT INTO public.investment_state (id, name) VALUES (NEXTVAL('investment_state_seq'), 'OWNER_REJECTED');
INSERT INTO public.investment_state (id, name) VALUES (NEXTVAL('investment_state_seq'), 'PAID');
INSERT INTO public.investment_state (id, name) VALUES (NEXTVAL('investment_state_seq'), 'REVOKED');
INSERT INTO public.investment_state (id, name) VALUES (NEXTVAL('investment_state_seq'), 'AUDIT_APPROVED');
INSERT INTO public.investment_state (id, name) VALUES (NEXTVAL('investment_state_seq'), 'AUDIT_REJECTED');

-- platform settings
INSERT INTO public.platform_settings (id, platform_min_investment) VALUES (NEXTVAL('platform_settings_seq'), 500);

-- request state
INSERT INTO public.request_state (id, name) VALUES (NEXTVAL('request_state_seq'), 'PENDING');
INSERT INTO public.request_state (id, name) VALUES (NEXTVAL('request_state_seq'), 'APPROVED');
INSERT INTO public.request_state (id, name) VALUES (NEXTVAL('request_state_seq'), 'DECLINED');

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
INSERT INTO public.user_role (id, name) VALUES (NEXTVAL('user_role_seq'), 'ROLE_AUDITOR');
