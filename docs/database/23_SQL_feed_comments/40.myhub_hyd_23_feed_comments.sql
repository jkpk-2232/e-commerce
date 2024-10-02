CREATE TABLE vendor_feed_comments
(
    comment_id character varying(255) NOT NULL,
    post_id character varying(255) ,
    comment_content text  NOT NULL,
    user_id character varying(255)  NOT NULL,
    parent_comment character varying(255) ,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT vendor_feed_comments_pkey PRIMARY KEY (comment_id),
    CONSTRAINT vendor_feed_comments_post_id_fkey FOREIGN KEY (post_id)
        REFERENCES public.vendor_feeds (vendor_feed_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE INDEX idx_vendor_feed_comments_post_id ON vendor_feed_comments(post_id);
CREATE INDEX idx_vendor_feed_comments_user_id ON vendor_feed_comments(user_id);
CREATE INDEX idx_vendor_feed_comments_parent_comment ON vendor_feed_comments(parent_comment);
CREATE INDEX idx_vendor_feed_comments_created_at ON vendor_feed_comments(created_at);