-- 1. ユーザーの初期登録（最優先で実行）
INSERT INTO users (email, password) 
VALUES ('test@example.com', 'password123') 
ON CONFLICT (email) DO NOTHING;

-- 2. スポットの初期登録（重複がない場合のみ挿入）
INSERT INTO spots (name, description, address, latitude, longitude)
SELECT '門司港レトロ', '歴史的な建造物が並ぶ観光スポット', '福岡県北九州市門司区', 33.9486, 130.9619
WHERE NOT EXISTS (SELECT 1 FROM spots WHERE name = '門司港レトロ');

INSERT INTO spots (name, description, address, latitude, longitude)
SELECT '小倉城', '北九州市のシンボル城郭', '福岡県北九州市小倉北区', 33.8833, 130.8753
WHERE NOT EXISTS (SELECT 1 FROM spots WHERE name = '小倉城');