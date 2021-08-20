
------------------------------
-- Users and users sessions --
------------------------------

CREATE TABLE public.users (
	user_id int4 NOT NULL,
	is_admin bool NULL,
	login varchar(255) NULL,
	user_name varchar(255) NULL,
	user_password varchar(255) NULL,
	registration_date varchar(255) NULL,
	salt varchar(255) NULL,
	CONSTRAINT uk_user_login UNIQUE (login),
	CONSTRAINT users_pkey PRIMARY KEY (user_id)
);

CREATE TABLE public.user_sessions (
	session_uuid varchar(255) NOT NULL,
	user_id int4 NULL,
	CONSTRAINT user_sessions_pkey PRIMARY KEY (session_uuid)
);

ALTER TABLE public.user_sessions ADD CONSTRAINT fk_users_sessions 
FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON DELETE CASCADE;


------------------------
-- Books and sections --
------------------------

CREATE TABLE public.sections (
	section_id int4 NOT NULL,
	section_name varchar(255) NULL,
	parent_section_id int4 NULL,
	CONSTRAINT sections_pkey PRIMARY KEY (section_id),
	CONSTRAINT uk_section_name UNIQUE (section_name),
	CONSTRAINT fk_parent_node_id FOREIGN KEY (parent_section_id) REFERENCES sections(section_id)
);


CREATE TABLE public.sections_sections (
	section_section_id int4 NOT NULL,
	children_section_id int4 NOT NULL,
	CONSTRAINT uk_uniq_child_id UNIQUE (children_section_id)
);

ALTER TABLE public.sections_sections ADD CONSTRAINT fk_child_section_id
FOREIGN KEY (children_section_id) REFERENCES public.sections(section_id);

ALTER TABLE public.sections_sections ADD CONSTRAINT fk_parent_section_id 
FOREIGN KEY (section_section_id) REFERENCES public.sections(section_id);


CREATE TABLE public.books (
	id int4 NOT NULL,
	book_description text NULL,
	file_name varchar(255) NULL,
	file_format varchar(255) NULL,
	book_name varchar(255) NULL,
	section_section_id int4 NULL,
	authors varchar(255) NULL,
	book_code varchar(255) NULL,
	magazine varchar(255) NULL,
	city varchar(255) NULL,
	publisher varchar(255) NULL,
	publication_year int4 NULL,
	CONSTRAINT books_pkey PRIMARY KEY (id)
);


ALTER TABLE public.books ADD CONSTRAINT fk_book_section_id 
FOREIGN KEY (section_section_id) REFERENCES public.sections(section_id);

---------------
-- Sequences --
---------------

CREATE sequence SEQ_USER
minvalue 0
start with 0
increment by 1
cache 5;

CREATE sequence SEQ_BOOK
minvalue 0
start with 0
increment by 1
cache 5;
