DROP TABLE IF EXISTS documents;

CREATE TABLE documents (
    Id INTEGER PRIMARY KEY,
    PostTypeId INTEGER,
    AcceptedAnswerId INTEGER,
    ParentId INTEGER,
    CreationDate DATE,
    score INTEGER,
    ViewCount INTEGER,
    Body VARCHAR,
    LastEditDate DATE,
    LastActivity Date,
    Title VARCHAR,
    Tags VARCHAR,
    AnswerCount INTEGER,
    CommentCount INTEGER,
    FavoriteCount INTEGER
);