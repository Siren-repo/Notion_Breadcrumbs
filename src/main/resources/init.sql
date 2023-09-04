DROP TABLE IF EXISTS page;

CREATE TABLE page (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    parent_id BIGINT,
    title VARCHAR(255) NOT NULL,
    contents TEXT NOT NULL
);

-- Main Page
INSERT INTO page (parent_id, title, contents)
VALUES (null, 'A', 'This is Main Page');

-- Sub Pages under Main Page
INSERT INTO page (parent_id, title, contents)
VALUES (1, 'B', 'This is Sub Page 1-1'),
       (1, 'C', 'This is Sub Page 1-2'),
       (1, 'D', 'This is Sub Page 1-3');

-- Sub Pages under Sub Page
INSERT INTO page (parent_id, title, contents)
VALUES (4, 'E', 'This is Sub Page 1-3-1'),
       (4, 'F', 'This is Sub Page 1-3-2'),
       (4, 'G', 'This is Sub Page 1-3-2');

INSERT INTO page (parent_id, title, contents)
VALUES (5, 'H', 'This is Sub Page 1-3-1-1'),
       (5, 'I', 'This is Sub Page 1-3-1-1');

INSERT INTO page (parent_id, title, contents)
VALUES (7, 'J', 'This is Sub Page 1-3-2-1'),
       (7, 'K', 'This is Sub Page 1-3-2-1');

INSERT INTO page (parent_id, title, contents)
VALUES (8, 'L', 'This is Sub Page 1-3-1-1-1'),
       (8, 'M', 'This is Sub Page 1-3-1-1-1');

INSERT INTO page (parent_id, title, contents)
VALUES (11, 'N', 'This is Sub Page 1-3-2-1-1'),
       (11, 'O', 'This is Sub Page 1-3-2-1-2'),
       (12, 'P', 'This is Sub Page 1-3-1-1-1-1');

commit;