-- Categorías
INSERT IGNORE INTO category (id, title, meta_title, slug, content) VALUES
(1, 'Hogar', 'Productos para el hogar', 'hogar', 'Artículos para la casa'),
(2, 'Deportes', 'Productos deportivos', 'deportes', 'Artículos para entrenar'),
(3, 'Tecnología', 'Productos tecnológicos', 'tecnologia', 'Electrónica y gadgets');

-- Subcategorías
INSERT IGNORE INTO sub_category (id, description, categoryid) VALUES
(1, 'Dormitorio', 1),
(2, 'Cocina', 1),
(3, 'Fitness', 2),
(4, 'Periféricos', 3),
(5, 'Audio', 3);

-- Productos
INSERT IGNORE INTO product (id, title, summary, content, created_at, categoryid, subcategoryid) VALUES
(1, 'UltraSoft Pillow', 'Almohada de plumas', 'A luxurious pillow made with the finest down feathers.', NOW(), 1, 1),
(2, 'SteelChef Knife Set', 'Set de cuchillos', 'A set of stainless steel knives with ergonomic handles.', NOW(), 1, 2),
(3, 'PureBrew Coffee Maker', 'Cafetera automática', 'Automatic coffee maker with programmable settings.', NOW(), 1, 2),
(4, 'FlexiYoga Mat', 'Mat de yoga', 'Non-slip yoga mat made from eco-friendly materials.', NOW(), 2, 3),
(5, 'ProFit Resistance Bands', 'Bandas elásticas', 'Set of five resistance bands for a complete workout.', NOW(), 2, 3),
(6, 'ProGaming Mouse', 'Mouse gamer', 'Ergonomic gaming mouse with customizable RGB lighting.', NOW(), 3, 4),
(7, 'SoundWave Bluetooth Speaker', 'Parlante bluetooth', 'Portable speaker with high-definition sound and deep bass.', NOW(), 3, 5);