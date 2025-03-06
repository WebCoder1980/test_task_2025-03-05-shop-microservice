CREATE TABLE IF NOT EXISTS counter (
	"name" varchar(75) NOT NULL,
	currentid int8 NULL,
	CONSTRAINT counter_pkey PRIMARY KEY (name)
);

CREATE TABLE IF NOT EXISTS shop (
    id_ int8 NOT NULL,
    name varchar(250) NOT NULL,
    address text NOT NULL,
    CONSTRAINT shop_pkey PRIMARY KEY (id_)
);