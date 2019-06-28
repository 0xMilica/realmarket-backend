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
INSERT INTO public.campaign_state (id, name) VALUES (NEXTVAL('campaign_state_seq'), 'FINANCE_PROPOSITION');
INSERT INTO public.campaign_state (id, name) VALUES (NEXTVAL('campaign_state_seq'), 'LEAD_INVESTMENT');
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
INSERT INTO public.investment_state (id, name) VALUES (NEXTVAL('investment_state_seq'), 'PAID');
INSERT INTO public.investment_state (id, name) VALUES (NEXTVAL('investment_state_seq'), 'REVOKED');
INSERT INTO public.investment_state (id, name) VALUES (NEXTVAL('investment_state_seq'), 'APPROVED');
INSERT INTO public.investment_state (id, name) VALUES (NEXTVAL('investment_state_seq'), 'REJECTED');

-- person
INSERT INTO public.person (id, address, city, country_for_taxation, country_of_residence, email, first_name, last_name, phone_number, profile_picture_url) VALUES (NEXTVAL('person_seq'), '12 Millbank, Westminster', 'London', 'BS', 'GB', 'mark.stevenson@crowdfunding.com', 'Mark', 'Stevenson', '0044123456789', null);
INSERT INTO public.person (id, address, city, country_for_taxation, country_of_residence, email, first_name, last_name, phone_number, profile_picture_url) VALUES (NEXTVAL('person_seq'), 'The Old Schools, Trinity Ln', 'Cambridge', null, 'GB', 'tom.britton@cambridgecrowdfundingunity.com', 'Tom', 'Britton', '0044987654321', null);
INSERT INTO public.person (id, address, city, country_for_taxation, country_of_residence, email, first_name, last_name, phone_number, profile_picture_url) VALUES (NEXTVAL('person_seq'), 'Marcelo Torcuato de Alvear 1705', 'Buenos Aires', 'CL', 'AR', 'pablo.garcia@crowdfunding.com', 'Pablo', 'Garcia', '0054123456789', null);
INSERT INTO public.person (id, address, city, country_for_taxation, country_of_residence, email, first_name, last_name, phone_number, profile_picture_url) VALUES (NEXTVAL('person_seq'), 'Via dei Monti Parioli, 20', 'Rome', 'MT', 'IT', 'vittorio.bonnavita@romanblockchain.com', 'Vittorio', 'Bonnavita', '0039123456789', null);
INSERT INTO public.person (id, address, city, country_for_taxation, country_of_residence, email, first_name, last_name, phone_number, profile_picture_url) VALUES (NEXTVAL('person_seq'), 'Leof. Vasilissis Sofias 106', 'Athens', null, 'GR', 'adonis.sotiropoulos@cypruscybersecurity.com', 'Adonis', 'Sotiropoulos', '00357123456789', null);
INSERT INTO public.person (id, address, city, country_for_taxation, country_of_residence, email, first_name, last_name, phone_number, profile_picture_url) VALUES (NEXTVAL('person_seq'), 'Nemanjina 11', 'Belgrade', 'BS', 'RS', 'radovan.turovic@realmarket.io', 'Radovan', 'Turovic', '00381123456789', null);
INSERT INTO public.person (id, address, city, country_for_taxation, country_of_residence, email, first_name, last_name, phone_number, profile_picture_url) VALUES (NEXTVAL('person_seq'), 'Modene 1', 'Novi Sad', null, 'RS', 'ognjen.vlajic@realmarket.io', 'Ognjen', 'Vlajic', '00381987654321', null);

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

-- auth
INSERT INTO public.auth (id, state_id, password, user_role_id, blocked, totp_secret, username, person_id) VALUES (NEXTVAL('auth_seq'), 3, '$2a$10$E6Pwa0HnLWOoZIBBuZVyV.TodJ88sb.iBin.U2e9YSp7Z0ZaPXck2', 2, false, 'CYFMMB56QFIVKUFG', 'mark', 1);
INSERT INTO public.auth (id, state_id, password, user_role_id, blocked, totp_secret, username, person_id) VALUES (NEXTVAL('auth_seq'), 3, '$2a$10$Nd8uSgyw8fYxUqMNET.TruoaYI.te6D3f5YPVVIm0QqRhzJVCGgma', 3, false, '7O2LOBXXCYHK4GJ4', 'tom', 2);
INSERT INTO public.auth (id, state_id, password, user_role_id, blocked, totp_secret, username, person_id) VALUES (NEXTVAL('auth_seq'), 3, '$2a$10$Nd8uSgyw8fYxUqMNET.TruoaYI.te6D3f5YPVVIm0QqRhzJVCGgma', 2, false, '7O2LOBXXCYHK4GJ4', 'pablo', 3);
INSERT INTO public.auth (id, state_id, password, user_role_id, blocked, totp_secret, username, person_id) VALUES (NEXTVAL('auth_seq'), 3, '$2a$10$E6Pwa0HnLWOoZIBBuZVyV.TodJ88sb.iBin.U2e9YSp7Z0ZaPXck2', 3, false, 'CYFMMB56QFIVKUFG', 'vittorio', 4);
/*password is testPASS123 */
INSERT INTO public.auth (id, state_id, password, user_role_id, blocked, totp_secret, username, person_id) VALUES (NEXTVAL('auth_seq'), 3, '$2a$10$nTWQERaNUio88aEHzSVhrOmzsH6XRCfrftpJDxNqwsEdDjh8Y4PFi', 3, false, 'secret', 'adonis', 5);
INSERT INTO public.auth (id, state_id, password, user_role_id, blocked, totp_secret, username, person_id) VALUES (NEXTVAL('auth_seq'), 3, '$2a$10$nTWQERaNUio88aEHzSVhrOmzsH6XRCfrftpJDxNqwsEdDjh8Y4PFi', 2, false, 'secret', 'radovan', 6);
INSERT INTO public.auth (id, state_id, password, user_role_id, blocked, totp_secret, username, person_id) VALUES (NEXTVAL('auth_seq'), 3, '$2a$10$nTWQERaNUio88aEHzSVhrOmzsH6XRCfrftpJDxNqwsEdDjh8Y4PFi', 3, false, 'secret', 'ognjen', 7);

-- remember me cookie
INSERT INTO public.remember_me_cookie (id, expiration_time, value, auth_id) VALUES (1, TO_DATE('01/05/2022', 'DD/MM/YYYY'), '1234', 5);

-- company
INSERT INTO public.company(id, address, bank_account, city, county, featured_image_url, logo_url, name, tax_identifier, website, linkedin_url, twitter_url, facebook_url, custom_url, auth_id, company_category_id) VALUES (NEXTVAL('company_seq'), 'The Old Schools, Trinity Ln', '00-123456789', 'Cambridge','United Kingdom', null, null, 'Cambridge Crowdfunding Unity', '6482796831', 'www.cambridgecrowdfundingunity.uk', 'www.linkedin.com/cambridgecrowdfundingunity', 'www.twitter.com/cambridgecrowdfundingunity', 'www.facebook.com/cambridgecrowdfundingunity', null, 2, 11);
INSERT INTO public.company(id, address, bank_account, city, county, featured_image_url, logo_url, name, tax_identifier, website, linkedin_url, twitter_url, facebook_url, custom_url, auth_id, company_category_id) VALUES (NEXTVAL('company_seq'), 'Via dei Monti Parioli, 20', '00-123456789', 'Rome', 'Italy', null, null, 'Roman Blockchain', '13545698564', 'www.romanblockchain.it', 'www.linkedin.com/romanblockchain', 'www.twitter.com/romanblockchain', 'www.facebook.com/romanblockchain', null, 4, 4);
INSERT INTO public.company(id, address, bank_account, city, county, featured_image_url, logo_url, name, tax_identifier, website, linkedin_url, twitter_url, facebook_url, custom_url, auth_id, company_category_id) VALUES (NEXTVAL('company_seq'), 'Leof. Vasilissis Sofias 106', '00-123456789', 'Athens','Greece', null, null, 'Cyprus Cyber Security', '2158564699', 'www.cypruscybersecurity.cy', 'www.linkedin.com/cypruscybersecurity', 'www.twitter.com/cypruscybersecurity', 'www.facebook.com/cypruscybersecurity', null, 5, 10);
INSERT INTO public.company(id, address, bank_account, city, county, featured_image_url, logo_url, name, tax_identifier, website, linkedin_url, twitter_url, facebook_url, custom_url, auth_id, company_category_id) VALUES (NEXTVAL('company_seq'), 'Nemanjina 11','00-123456789', 'Belgrade', 'Serbia', null, null, 'Радован Туровић ПР Агенција за рачунарско програмирање Т и Т7 Врбас', '9652163215', 'www.tit7vrbas.rs', 'www.linkedin.com/tit7vrbas', 'www.twitter.com/tit7vrbas', 'www.facebook.com/tit7vrbas', null, 6, 2);

-- shareholder
INSERT INTO public.shareholder(id, custom_profile_url, description, facebook_url, invested_amount, is_anonymous, linkedin_url, location, name, order_number, photo_url, twitter_url, company_id) VALUES (NEXTVAL('shareholder_seq'), null, 'Elon Musk is a technology entrepreneur, investor, and engineer.', 'www.facebook.com/elonmusk', 1000000, false, 'www.linkedin.com/elonmusk', 'Cape Town', 'Elon Musk', 1, null, 'www.twitter.com/elonmusk', 1);
INSERT INTO public.shareholder(id, custom_profile_url, description, facebook_url, invested_amount, is_anonymous, linkedin_url, location, name, order_number, photo_url, twitter_url, company_id) VALUES (NEXTVAL('shareholder_seq'), null, 'Arthur D. Levinson is an American businessman and is the current Chairman of Apple Inc. and CEO of Calico. He is the former chief executive officer and chairman of Genentech.', 'www.facebook.com/arthurlevinson', 2000000, false, 'www.linkedin.com/arthurlevinson', 'Seattle', 'Arthur Levinson', 1, null, 'www.twitter.com/arthurlevinson', 2);
INSERT INTO public.shareholder(id, custom_profile_url, description, facebook_url, invested_amount, is_anonymous, linkedin_url, location, name, order_number, photo_url, twitter_url, company_id) VALUES (NEXTVAL('shareholder_seq'), null, null, 'www.facebook.com/mirkobordjoski', 100000, true, 'www.linkedin.com/mirkobordjoski', 'Becej', 'Mirko Bordjoski', 2, null, 'www.twitter.com/mirkobordjoski', 2);

-- campaign
INSERT INTO public.campaign(id, url_friendly_name, funding_goals, collected_amount, market_image_url, max_equity_offered, min_equity_offered, name, time_to_raise_funds, min_investment, tag_line, activation_date, modified_date, company_id, campaign_state_id) VALUES (NEXTVAL('campaign_seq'), 'friendlyName', 4260004, 4427511, null, 6.60, 3.30, 'Paysend', 60, 500, 'Money for the future. Pay, hold & send money instantly and across borders', '2019-05-07 16:41:05.57465', null, 2, 6);
INSERT INTO public.campaign(id, url_friendly_name, funding_goals, collected_amount, market_image_url, max_equity_offered, min_equity_offered, name, time_to_raise_funds, min_investment, tag_line, activation_date, modified_date, company_id, campaign_state_id) VALUES (NEXTVAL('campaign_seq'), 'friendlyName', 500000, 548760, null, 11, 5.56, 'Young Platform', 90, 150, 'The intuitive crypto-exchange for a new generation of investors with an innovative acquisition tool.', '2019-04-22 07:12:56.65983', null, 2, 7);
INSERT INTO public.campaign(id, url_friendly_name, funding_goals, collected_amount, market_image_url, max_equity_offered, min_equity_offered, name, time_to_raise_funds, min_investment, tag_line, activation_date, modified_date, company_id, campaign_state_id) VALUES (NEXTVAL('campaign_seq'), 'friendlyName', 30000, 153306, null, 5, 3.61, 'Glu', 35, 150, 'Where consumers discover cryptocurrencies.', '2019-06-27 16:41:45.15652', null, 3, 6);
INSERT INTO public.campaign(id, url_friendly_name, funding_goals, collected_amount, market_image_url, max_equity_offered, min_equity_offered, name, time_to_raise_funds, min_investment, tag_line, activation_date, modified_date, company_id, campaign_state_id) VALUES (NEXTVAL('campaign_seq'), 'friendlyName', 500005, 127281, null, 5, 4.76, 'Yielders', 80, 200, 'We lower the barriers of entry to a traditionally inaccessible asset class: real estate.', '2019-01-27 16:41:45.15652', null, 1, 7);
INSERT INTO public.campaign(id, url_friendly_name, funding_goals, collected_amount, market_image_url, max_equity_offered, min_equity_offered, name, time_to_raise_funds, min_investment, tag_line, activation_date, modified_date, company_id, campaign_state_id) VALUES (NEXTVAL('campaign_seq'), 'friendlyName', 600002, 0, null, 10, 8.45, 'CreditLadder', 60, 200, 'Build your Experian credit history, by paying your rent on time. Backed by HM Treasury.', null, null, 1, 1);
INSERT INTO public.campaign(id, url_friendly_name, funding_goals, collected_amount, market_image_url, max_equity_offered, min_equity_offered, name, time_to_raise_funds, min_investment, tag_line, activation_date, modified_date, company_id, campaign_state_id) VALUES (NEXTVAL('campaign_seq'), 'friendlyName', 400002, 110841, null, 15, 14.77, 'Deciwatt', 90, 140, 'Deciwatt creates renewable energy products that allow users to make light and power. Instantly.', null, null, 4, 2);
INSERT INTO public.campaign(id, url_friendly_name, funding_goals, collected_amount, market_image_url, max_equity_offered, min_equity_offered, name, time_to_raise_funds, min_investment, tag_line, activation_date, modified_date, company_id, campaign_state_id) VALUES (NEXTVAL('campaign_seq'), 'friendlyName', 1000000, 0, null, 20, 18, 'Airsorted', 30, 500, 'Airsorted makes hosting hassle-free on platforms like Airbnb, HomeAway & Expedia.', null, null, 4, 6);

-- campaign team member
INSERT INTO public.campaign_team_member(id, custom_profile_url, description, facebook_url, linkedin_url, name, order_number, photo_url, title, twitter_url, campaign_id) VALUES (NEXTVAL('campaign_team_member_seq'), null, 'Leading the development of the company''s short- and long-term strategy. Creating and implementing the company or organization''s vision and mission.', 'www.facebook.com/tombritton', 'www.linkedin.com/tombritton', 'Tom Britton', 1, null, 'CEO', 'www.twitter.com/tombritton', 4);
INSERT INTO public.campaign_team_member(id, custom_profile_url, description, facebook_url, linkedin_url, name, order_number, photo_url, title, twitter_url, campaign_id) VALUES (NEXTVAL('campaign_team_member_seq'), null, 'Design, plan and implement business strategies, plans and procedures. Set comprehensive goals for business growth and success. Establish policies and procedures that promote company culture and vision.', 'www.facebook.com/nemanjamandic', 'www.linkedin.com/nemanjamandic', 'Nemanja Mandic', 2, null, 'COO', 'www.twitter.com/nemanjamandic', 4);
INSERT INTO public.campaign_team_member(id, custom_profile_url, description, facebook_url, linkedin_url, name, order_number, photo_url, title, twitter_url, campaign_id) VALUES (NEXTVAL('campaign_team_member_seq'), null, 'Responsible for handling all aspects of planned publicity campaigns and PR activities.', 'www.facebook.com/draganrakita', 'www.linkedin.com/draganrakita', 'Dragan Rakita', 3, null, 'PR executive', 'www.twitter.com/draganrakita', 4);

-- campaign topic
INSERT INTO public.campaign_topic(id, content, campaign_id, campaign_topic_type_id) VALUES (NEXTVAL('campaign_topic_seq'), 'At Paysend we have a clear vision of how to deliver these needs in an instant, freemium, borderless way and designed for ultimate customer satisfactionWe have introduced a global card to card transfer to connect 12 billion cards issued by international systems: Mastercard, Visa, China Union Pay, and local cardsWe launched Paysend Link, where money is sent as a text message with fixed fees. We created a multi-currency global account with a proxy card technology that allows customers to avoid cross border fees and easily manage their money. In June 2019, we plan to launch a new global stablecoin on the Stellar network that will provide a consistent store of value that users can hold or seamlessly transact in real time. Paysend is money FOR the future.', 1, 4);
INSERT INTO public.campaign_topic(id, content, campaign_id, campaign_topic_type_id) VALUES (NEXTVAL('campaign_topic_seq'), '{"blocks":[{"key":"gkav","text":"Digital assets are becoming increasingly traded: Daily crypto trading volume went from $20B in Jan ''''19 to $100B in May ''''19 with a general increase of 25% in prices for key digital assets. A new wave of investors is approaching the market and we are building the Young Ecosystem to help them invest in an easy and intuitive way. With the Young Ecosystem you can:","type":"unstyled","depth":0,"inlineStyleRanges":[],"entityRanges":[],"data":{}},{"key":"cg1bh","text":"1) Earn our token Young (YNG) with Stepdrop App by simply walking;.","type":"unstyled","depth":0,"inlineStyleRanges":[],"entityRanges":[],"data":{}},{"key":"5ao2s","text":"2) Easily buy the most trusted cryptocurrencies in less than 30 seconds using Young Platform;.","type":"unstyled","depth":0,"inlineStyleRanges":[],"entityRanges":[],"data":{}},{"key":"cl1qe","text":"3) Trade with the advanced features and competitive fees on Young Platform Pro.","type":"unstyled","depth":0,"inlineStyleRanges":[],"entityRanges":[],"data":{}},{"key":"d3mfs","text":"","type":"unstyled","depth":0,"inlineStyleRanges":[],"entityRanges":[],"data":{}},{"key":"7e2h6","text":"Through its Stepdrop App Young platform has achieved:","type":"unstyled","depth":0,"inlineStyleRanges":[],"entityRanges":[],"data":{}},{"key":"76ibp","text":"- Over 50k downloads in 4 months","type":"unstyled","depth":0,"inlineStyleRanges":[],"entityRanges":[],"data":{}},{"key":"10qi8","text":"- An average of 23k active users per month","type":"unstyled","depth":0,"inlineStyleRanges":[],"entityRanges":[],"data":{}},{"key":"cnm19","text":"- Client acquisition cost of €0,10.","type":"unstyled","depth":0,"inlineStyleRanges":[],"entityRanges":[],"data":{}},{"key":"5rn4h","text":"","type":"unstyled","depth":0,"inlineStyleRanges":[],"entityRanges":[],"data":{}},{"key":"4l9u7","text":"Functional, secure and with all the features required by today’s users, we believe Young Platform is what the market was waiting for to become mainstream.","type":"unstyled","depth":0,"inlineStyleRanges":[],"entityRanges":[],"data":{}}],"entityMap":{}}', 2, 6);
INSERT INTO public.campaign_topic(id, content, campaign_id, campaign_topic_type_id) VALUES (NEXTVAL('campaign_topic_seq'), 'Many of us are aware of the crypto revolution that is taking place. In the UK, a survey showed that 77% of people have heard of blockchain and yet only 9% have invested in any crypto assets so far. It can be a world full of jargon, mistrust and disjointed information. This is where Glu comes in. Think of us as ''TripAdvisor'' for cryptocurrencies. We are here to help customers discover the world of cryptocurrencies and become more knowledgeable. We believe this will help them make smarter investments in this exciting, but often confusing world. Our free-to-use service will provide information & user-generated reviews on thousands of different crypto platforms and products, with more being added every week. We aim to be the most trusted entry point into the world of cryptocurrencies. This is a market capable of generating significant growth over the next few years. In our view, this is similar to the rapid growth that the internet experienced in the ''90s and Glu is perfectly placed to take advantage. Please note that the company is incorporated in Luxembourg however all of Glu''s business operations are in the UK.', 3, 1);
INSERT INTO public.campaign_topic(id, content, campaign_id, campaign_topic_type_id) VALUES (NEXTVAL('campaign_topic_seq'), 'Yielders'' award-winning real-estate investment platform is the UK''s first Islamic finance-friendly FinTech company, regulated by the FCA. The rise of FinTech across the world is providing everyday people access to alternative financial opportunities. Our platform aims to deliver consistent returns for the conscientious, modern investor. We aim to serve a global demand for alternative and ethical investing - our business model has provided our users with regular rental income consistently on the 1st of every month for 36 months (and counting!) as well as competitive returns on development projects. We provide hassle-free property investments, with predefined lease agreements which offer users a steady income. Assets are expertly sourced in regions where there is strong projected capital appreciation, allowing investors to benefit from all aspects of a real-estate investment without any of the headaches of being a landlord.', 4, 3);
INSERT INTO public.campaign_topic(id, content, campaign_id, campaign_topic_type_id) VALUES (NEXTVAL('campaign_topic_seq'), 'CreditLadder aims to deliver financial fairness to tenants by ensuring their on time rent payment each month builds their Experian credit history. An improved credit history should allow tenants to save money, and ultimately may also help them on to the housing ladder.', 5, 3);
INSERT INTO public.campaign_topic(id, content, campaign_id, campaign_topic_type_id) VALUES (NEXTVAL('campaign_topic_seq'), 'Deciwatt develop renewable energy solutions and our new product, NowLight creates instant light and power from the pull of a cord. Just one minute of pulling on NowLight’s cord will generate up to two hours of light and when fully charged NowLight will provide up to 50 hours of light.', 5, 6);
INSERT INTO public.campaign_topic(id, content, campaign_id, campaign_topic_type_id) VALUES (NEXTVAL('campaign_topic_seq'), '{"blocks":[{"key":"fih7a","text":"In April 2018 we ran a crowdfunding campaign on Seedrs, beating our target in a matter of days. Since then we''''ve put these funds to good use. In the last year, we’ve...","type":"unstyled","depth":0,"inlineStyleRanges":[],"entityRanges":[],"data":{}},{"key":"dutfe","text":"","type":"unstyled","depth":0,"inlineStyleRanges":[],"entityRanges":[],"data":{}},{"key":"asvdl","text":":white_check_mark: Grown revenue 2.5x*.","type":"unstyled","depth":0,"inlineStyleRanges":[],"entityRanges":[],"data":{}},{"key":"cssr5","text":":white_check_mark: Launched in 12 new cities.","type":"unstyled","depth":0,"inlineStyleRanges":[],"entityRanges":[],"data":{}},{"key":"646qg","text":":white_check_mark: Grown the number of homes under management from 1,800 to more than 4,000.","type":"unstyled","depth":0,"inlineStyleRanges":[],"entityRanges":[],"data":{}},{"key":"bjgla","text":"","type":"unstyled","depth":0,"inlineStyleRanges":[],"entityRanges":[],"data":{}},{"key":"cv800","text":"Our mission is to make hosting hassle-free for everyone. Renting out your home can be a hassle – from dealing with guest inquiries to cleaning and arranging key exchanges – especially if you are away or out of the country yourself. We market our hosts’ homes for short-lets, then look after guests from start to finish.","type":"unstyled","depth":0,"inlineStyleRanges":[],"entityRanges":[],"data":{}},{"key":"8hbcf","text":"","type":"unstyled","depth":0,"inlineStyleRanges":[],"entityRanges":[],"data":{}},{"key":"2tgg8","text":"Home sharing is not accessible for many homeowners who don’t have the time or expertise – we’re here to solve that. We host and our clients earn – that''''s Airsorted. The extra income & freedom enables our hosts to do more of what they love.","type":"unstyled","depth":0,"inlineStyleRanges":[],"entityRanges":[],"data":{}}],"entityMap":{}}', 6, 7);

-- campaign update
INSERT INTO public.campaign_update(id, title, campaign_id, post_date, content) VALUES (NEXTVAL('campaign_update_seq'), 'Campaign Update, June 2019', 1, null, 'In preparation for the 2019 High-Level Event onStatelessness, two regional preparatory meetings took place. From 12 to 14 November, a Regional Meeting on Statelessness was held in Saly, Senegal, organized by UNHCR and ECOWAS. The discussions focused on the implementation of the Banjul Plan of Action on the Eradication of Statelessness in West Africa and the road towards the 2019 High-Level Event on Statelessness. ');
INSERT INTO public.campaign_update(id, title, campaign_id, post_date, content) VALUES (NEXTVAL('campaign_update_seq'), '2019 Summer Update', 2, null, 'Sisters Maha and Souad Mamo were granted Brazilian nationality, with Maha receiving hers during a surprise ceremony that took place at a side event during the 69th session of the UNCHR Executive Committee meeting in Geneva on 4 October. In June, the two young women had been the first to have their statelessness status confirmed under Brazil’s new migration law, which grants protection to stateless persons and provides for residence and simplified naturalization procedures.');
INSERT INTO public.campaign_update(id, title, campaign_id, post_date, content) VALUES (NEXTVAL('campaign_update_seq'), 'Campaign soon to be completed!', 3, null, 'Thanks for all of your amazing support so far. Here’s a quick update on our end. Our team is preparing for our demo-day next week. Here’s a picture of our team test-driving our prototype. If we select your work, we’ll post it on our social channels and send you an extra ticket to our premiere. We know you’re smart because you backed our campaign. We’d love to hear what you like and what we can do better! Please let us know in the comments section on our campaign.');

-- investment
INSERT INTO public.investment(id, invested_amount, auth_id, campaign_id, investment_state_id, creation_date, payment_date) VALUES (NEXTVAL('investment_seq'), 105000, 1, 1, 2, '2019-06-26 16:41:45.15652', '2019-06-27 16:41:45.15652');
INSERT INTO public.investment(id, invested_amount, auth_id, campaign_id, investment_state_id, creation_date, payment_date) VALUES (NEXTVAL('investment_seq'), 105000, 1, 2, 1, '2019-06-26 16:41:45.15652', null);
INSERT INTO public.investment(id, invested_amount, auth_id, campaign_id, investment_state_id, creation_date, payment_date) VALUES (NEXTVAL('investment_seq'), 105000, 1, 3, 4, '2019-06-26 16:41:45.15652', '2019-06-27 16:41:45.15652');
INSERT INTO public.investment(id, invested_amount, auth_id, campaign_id, investment_state_id, creation_date, payment_date) VALUES (NEXTVAL('investment_seq'), 105000, 1, 3, 3, '2019-06-26 16:41:45.15652', null);
INSERT INTO public.investment(id, invested_amount, auth_id, campaign_id, investment_state_id, creation_date, payment_date) VALUES (NEXTVAL('investment_seq'), 105000, 1, 3, 5, '2019-06-26 16:41:45.15652', '2019-06-27 16:41:45.15652');
INSERT INTO public.investment(id, invested_amount, auth_id, campaign_id, investment_state_id, creation_date, payment_date) VALUES (NEXTVAL('investment_seq'), 15000, 3, 2, 4, '2019-06-26 13:41:45.15652', '2019-06-27 13:41:45.15652');
INSERT INTO public.investment(id, invested_amount, auth_id, campaign_id, investment_state_id, creation_date, payment_date) VALUES (NEXTVAL('investment_seq'), 1000, 6, 7, 4, '2019-06-26 13:41:45.15652', '2019-06-27 13:41:45.15652');