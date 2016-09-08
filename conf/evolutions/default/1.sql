# TEST schema

# --- !Ups

CREATE TABLE USER (
    ID bigint(20) NOT NULL AUTO_INCREMENT,
    FULLNAME varchar(255) NOT NULL,
    PASSWORD varchar(255) NOT NULL,
    EMAIL varchar(255) NOT NULL,
    ISADMIN boolean NOT NULL,
    PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE USER;