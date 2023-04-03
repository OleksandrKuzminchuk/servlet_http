CREATE TABLE my_db.user (
                           id BIGINT NOT NULL AUTO_INCREMENT,
                           name VARCHAR(100),
                           PRIMARY KEY (id)
);

CREATE TABLE my_db.file (
                             id BIGINT NOT NULL AUTO_INCREMENT,
                             name VARCHAR(255),
                             file_path VARCHAR(255),
                             PRIMARY KEY (id)
);

CREATE TABLE my_db.event (
                           id BIGINT NOT NULL AUTO_INCREMENT,
                           user_id BIGINT,
                           file_id BIGINT,
                           PRIMARY KEY (id),
                           FOREIGN KEY (user_id) REFERENCES my_db.user(id),
                           FOREIGN KEY (file_id) REFERENCES my_db.file(id)
);
