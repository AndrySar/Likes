CREATE TABLE like_table (
  id SERIAL,
  playerId varchar(20),
  likes INTEGER,
  PRIMARY KEY(id, playerId)
);