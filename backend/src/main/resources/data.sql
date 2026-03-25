INSERT INTO animais (grupo, nome)
SELECT 1, 'Avestruz' WHERE NOT EXISTS (SELECT 1 FROM animais WHERE grupo = 1);

INSERT INTO animais (grupo, nome)
SELECT 2, 'Águia' WHERE NOT EXISTS (SELECT 1 FROM animais WHERE grupo = 2);

INSERT INTO animais (grupo, nome)
SELECT 3, 'Burro' WHERE NOT EXISTS (SELECT 1 FROM animais WHERE grupo = 3);

INSERT INTO animais (grupo, nome)
SELECT 4, 'Borboleta' WHERE NOT EXISTS (SELECT 1 FROM animais WHERE grupo = 4);

INSERT INTO animais (grupo, nome)
SELECT 5, 'Cachorro' WHERE NOT EXISTS (SELECT 1 FROM animais WHERE grupo = 5);

INSERT INTO animais (grupo, nome)
SELECT 6, 'Cabra' WHERE NOT EXISTS (SELECT 1 FROM animais WHERE grupo = 6);

INSERT INTO animais (grupo, nome)
SELECT 7, 'Carneiro' WHERE NOT EXISTS (SELECT 1 FROM animais WHERE grupo = 7);

INSERT INTO animais (grupo, nome)
SELECT 8, 'Camelo' WHERE NOT EXISTS (SELECT 1 FROM animais WHERE grupo = 8);

INSERT INTO animais (grupo, nome)
SELECT 9, 'Cobra' WHERE NOT EXISTS (SELECT 1 FROM animais WHERE grupo = 9);

INSERT INTO animais (grupo, nome)
SELECT 10, 'Coelho' WHERE NOT EXISTS (SELECT 1 FROM animais WHERE grupo = 10);

INSERT INTO animais (grupo, nome)
SELECT 11, 'Cavalo' WHERE NOT EXISTS (SELECT 1 FROM animais WHERE grupo = 11);

INSERT INTO animais (grupo, nome)
SELECT 12, 'Elefante' WHERE NOT EXISTS (SELECT 1 FROM animais WHERE grupo = 12);

INSERT INTO animais (grupo, nome)
SELECT 13, 'Galo' WHERE NOT EXISTS (SELECT 1 FROM animais WHERE grupo = 13);

INSERT INTO animais (grupo, nome)
SELECT 14, 'Gato' WHERE NOT EXISTS (SELECT 1 FROM animais WHERE grupo = 14);

INSERT INTO animais (grupo, nome)
SELECT 15, 'Jacaré' WHERE NOT EXISTS (SELECT 1 FROM animais WHERE grupo = 15);

INSERT INTO animais (grupo, nome)
SELECT 16, 'Leão' WHERE NOT EXISTS (SELECT 1 FROM animais WHERE grupo = 16);

INSERT INTO animais (grupo, nome)
SELECT 17, 'Macaco' WHERE NOT EXISTS (SELECT 1 FROM animais WHERE grupo = 17);

INSERT INTO animais (grupo, nome)
SELECT 18, 'Porco' WHERE NOT EXISTS (SELECT 1 FROM animais WHERE grupo = 18);

INSERT INTO animais (grupo, nome)
SELECT 19, 'Pavão' WHERE NOT EXISTS (SELECT 1 FROM animais WHERE grupo = 19);

INSERT INTO animais (grupo, nome)
SELECT 20, 'Peru' WHERE NOT EXISTS (SELECT 1 FROM animais WHERE grupo = 20);

INSERT INTO animais (grupo, nome)
SELECT 21, 'Touro' WHERE NOT EXISTS (SELECT 1 FROM animais WHERE grupo = 21);

INSERT INTO animais (grupo, nome)
SELECT 22, 'Tigre' WHERE NOT EXISTS (SELECT 1 FROM animais WHERE grupo = 22);

INSERT INTO animais (grupo, nome)
SELECT 23, 'Urso' WHERE NOT EXISTS (SELECT 1 FROM animais WHERE grupo = 23);

INSERT INTO animais (grupo, nome)
SELECT 24, 'Veado' WHERE NOT EXISTS (SELECT 1 FROM animais WHERE grupo = 24);

INSERT INTO animais (grupo, nome)
SELECT 25, 'Vaca' WHERE NOT EXISTS (SELECT 1 FROM animais WHERE grupo = 25);