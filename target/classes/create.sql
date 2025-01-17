DROP TABLE IF EXISTS documents;

CREATE TABLE documents (
    Id INTEGER PRIMARY KEY,
    PostTypeId INTEGER,
    AcceptedAnswerId INTEGER,
    ParentId INTEGER,
    CreationDate DATE,
    Score INTEGER,
    ViewCount INTEGER,
    Body TEXT,
    LastEditDate DATE,
    LastActivityDate Date,
    Title TEXT,
    Tags TEXT,
    AnswerCount INTEGER,
    CommentCount INTEGER,
    FavoriteCount INTEGER
);

DROP TABLE IF EXISTS answers;

CREATE TABLE answers(
    Id INTEGER PRIMARY KEY,
    ParentId INTEGER,
    Score INTEGER,
    Body TEXT
);