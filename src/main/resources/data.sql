MERGE INTO spots (name, description, address, latitude, longitude) KEY (name)
VALUES ('門司港レトロ', '歴史的な建造物が並ぶ観光スポット', '福岡県北九州市門司区', 33.9486, 130.9619);

MERGE INTO spots (name, description, address, latitude, longitude) KEY (name)
VALUES ('小倉城', '北九州市のシンボル城郭', '福岡県北九州市小倉北区', 33.8833, 130.8753);

MERGE INTO users (email, password) KEY (email) VALUES ('test@example.com', 'password123');