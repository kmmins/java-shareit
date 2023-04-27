--booking status
CREATE TYPE booking_status AS ENUM
    ('WAITING', 'APPROVED', 'REJECTED', 'CANCELED');
--users
CREATE TABLE IF NOT EXISTS public.users
(
    id        bigint                 NOT NULL GENERATED ALWAYS AS IDENTITY,
    user_name character varying(255) NOT NULL,
    email     character varying(512) NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT users_email_key UNIQUE (email)
);
--requests
CREATE TABLE IF NOT EXISTS public.requests
(
    id              bigint                 NOT NULL GENERATED ALWAYS AS IDENTITY,
    description     character varying(512) NOT NULL,
    request_user_id bigint                 NOT NULL,
    CONSTRAINT requests_pkey PRIMARY KEY (id),
    CONSTRAINT requests_request_user_id_fkey FOREIGN KEY (request_user_id)
        REFERENCES public.users (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
--items
CREATE TABLE IF NOT EXISTS public.items
(
    id          bigint                 NOT NULL GENERATED ALWAYS AS IDENTITY,
    item_name   character varying(255) NOT NULL,
    description character varying(512) NOT NULL,
    available   boolean                NOT NULL,
    owner_id    bigint                 NOT NULL,
    request_id  bigint                 NOT NULL,
    CONSTRAINT items_pkey PRIMARY KEY (id),
    CONSTRAINT items_owner_id_fkey FOREIGN KEY (owner_id)
        REFERENCES public.users (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT items_request_id_fkey FOREIGN KEY (request_id)
        REFERENCES public.requests (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
--bookings
CREATE TABLE IF NOT EXISTS public.bookings
(
    id         bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    item_id    bigint NOT NULL,
    start_time timestamp without time zone,
    end_time   timestamp without time zone,
    booker_id  bigint NOT NULL,
    status     booking_status,
    CONSTRAINT bookings_pkey PRIMARY KEY (id),
    CONSTRAINT bookings_booker_id_fkey FOREIGN KEY (booker_id)
        REFERENCES public.users (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT bookings_item_id_fkey FOREIGN KEY (item_id)
        REFERENCES public.items (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
--comments
CREATE TABLE IF NOT EXISTS public.comments
(
    id           bigint                      NOT NULL GENERATED ALWAYS AS IDENTITY,
    comment_text character varying(512)      NOT NULL,
    item_id      bigint                      NOT NULL,
    author_id    bigint                      NOT NULL,
    created      timestamp without time zone NOT NULL,
    CONSTRAINT comments_pkey PRIMARY KEY (id),
    CONSTRAINT comments_author_id_fkey FOREIGN KEY (author_id)
        REFERENCES public.users (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT comments_item_id_fkey FOREIGN KEY (item_id)
        REFERENCES public.items (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
