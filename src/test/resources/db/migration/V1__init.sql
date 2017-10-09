CREATE TABLE like_table (
  id SERIAL,
  playerId INTEGER,
  likes INTEGER,
  PRIMARY KEY(id, playerId)
);