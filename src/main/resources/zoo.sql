

CREATE TABLE IF NOT EXISTS `zookeeper` (
                                           `id` INT AUTO_INCREMENT,
                                           `fname` VARCHAR(40) NOT NULL,
    `lname` VARCHAR(40) NOT NULL,
    `username` VARCHAR(40) NOT NULL,
    `password` VARCHAR(40) NOT NULL,
    CONSTRAINT PK_ZOOKEEPER PRIMARY KEY (`id`)
    );
ALTER TABLE `zookeeper` ADD CONSTRAINT USERNAME UNIQUE (`username`);

CREATE TABLE IF NOT EXISTS `species` (
                                         `id` INT AUTO_INCREMENT,
    `subphylum` VARCHAR(25) NOT NULL,
    `class` VARCHAR(100) NOT NULL,
    CONSTRAINT PK_SPECIES PRIMARY KEY (`id`),
    CONSTRAINT SUBPHYLUM CHECK ((`subphylum` LIKE 'Vertebrates') OR (`subphylum` LIKE 'Invertebrates')),
    CONSTRAINT CLASS CHECK ((`class` LIKE 'Amphibians') OR (`class` LIKE 'Birds') OR (`class` LIKE 'Fish')
    OR (`class` LIKE 'Mammals') OR (`class` LIKE 'Reptiles') OR (`class` LIKE 'Arachnids')
    OR (`class` LIKE 'Crustaceans') OR (`class` LIKE 'Insects') OR (`class` LIKE 'Mollusks')
    OR (`class` LIKE 'Myriapoda') OR (`class` LIKE 'Echinoderms')
    )
    );
ALTER TABLE `species` ADD CONSTRAINT CLASS_IDX UNIQUE (`class`);

CREATE TABLE IF NOT EXISTS `diet` (
                                      `id` INT AUTO_INCREMENT,
                                      `type` VARCHAR(15) NOT NULL,
    CONSTRAINT PK_DIET PRIMARY KEY (`id`),
    CONSTRAINT DIET_TYPE CHECK (
(`type` LIKE 'Carnivores') OR (`type` LIKE 'Herbivores') OR (`type` LIKE 'Omnivores')
    )
    );
ALTER TABLE `diet` ADD CONSTRAINT DIET UNIQUE (`type`);

CREATE TABLE IF NOT EXISTS `habitat` (
                                         `id` INT AUTO_INCREMENT,
                                         `name` VARCHAR(50) NOT NULL,
    CONSTRAINT PK_HABITAT PRIMARY KEY (`id`)
    );
ALTER TABLE `habitat` ADD CONSTRAINT REGION_NAME UNIQUE (`name`);

CREATE TABLE IF NOT EXISTS `animaldetails` (
                                               `id` INT AUTO_INCREMENT,
                                               `info` longtext NOT NULL,
                                               `age` VARCHAR(50) NOT NULL,
    `weight` VARCHAR(30) NOT NULL,
    `food` VARCHAR(100) NOT NULL,
    `diet_id` INT NOT NULL,
    `habitat_id` INT NOT NULL,
    CONSTRAINT PK_ANIMAL_DETAILS PRIMARY KEY (`id`),
    CONSTRAINT fk_animaldetails_diet_id FOREIGN KEY (`diet_id`) REFERENCES `diet`(`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_animaldetails_habitat_id FOREIGN KEY (`habitat_id`) REFERENCES `habitat`(`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
    );

CREATE TABLE IF NOT EXISTS `animal` (
                                        `id` INT AUTO_INCREMENT,
                                        `name` VARCHAR(50) NOT NULL,
    `photo` VARCHAR(500) NOT NULL,
    `zookeeper_id` INT NOT NULL,
    `species_id` INT NOT NULL,
    `details_id` INT NOT NULL,
    CONSTRAINT PK_ANIMAL PRIMARY KEY (`id`),
    CONSTRAINT fk_animal_zookeeper_id FOREIGN KEY (`zookeeper_id`) REFERENCES `zookeeper`(`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_animal_species_id FOREIGN KEY (`species_id`) REFERENCES `species`(`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_animal_details_id FOREIGN KEY (`details_id`) REFERENCES `animaldetails`(`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
    );
ALTER TABLE `animal` ADD CONSTRAINT ANIMAL_NAME UNIQUE (`name`);

INSERT INTO `diet` (`type`) VALUES ('Carnivores'), ('Herbivores'), ('Omnivores');

INSERT INTO `species`
(`class`, `subphylum`)
VALUES
('Amphibians', 'Vertebrates'), /*1*/
('Birds', 'Vertebrates'), /*2*/
('Fish', 'Vertebrates'), /*3*/
('Mammals', 'Vertebrates'), /*4*/
('Reptiles', 'Vertebrates'); /*5*/

INSERT INTO `species`
(`class`, `subphylum`)
VALUES
('Arachnids', 'Invertebrates'), /*6*/
('Crustaceans', 'Invertebrates'), /*7*/
('Echinoderms', 'Invertebrates'), /*8*/
('Insects', 'Invertebrates'), /*9*/
('Mollusks', 'Invertebrates'), /*10*/
('Myriapoda', 'Invertebrates'); /*11*/

INSERT INTO
    `zookeeper` (`fname`,`lname`,`username`,`password`)
VALUES      ('Peter', 'Parker', 'admin', '37870d71a17c91e5219bd07f3bdb6c6ea070b0ff');

INSERT INTO
    `habitat` (`name`)
VALUES
('Africa'), /*1*/
('Asia'), /*2*/
('Europe'), /*3*/
('North America'), /*4*/
('South America'), /*5*/
('Australia'),/*6*/
('Arctic Circle'), /*7*/
('Arctic'); /*8*/


INSERT INTO `animaldetails` (`info`, `age`, `weight`, `food`, `diet_id`, `habitat_id`)
VALUES
("Lorem ipsum dolor sit amet, consectetur adipiscing elit..", '3 years', '154', 'browse, nuts, fruits', '2', '3'), /*deer, 1*/
("The polar bear (Ursus maritimus) is a mammal that inhabits only the northern Arctic coasts and the islands of
Eurasia and North America. Not found in Antarctica. It is the largest terrestrial predator.", 'up to 25 years', '350–700 kg', 'Seals, Muskox, Reindeer, birds, eggs, rodents, crabs, fish', '1', '7'), /*polar bear, 2*/
("Lorem ipsum dolor sit amet, consectetur adipiscing elit..", '5 months', '3', 'seeds, nuts, berries', '2', '2'), /*macaw, 3*/
("Lorem ipsum dolor sit amet, consectetur adipiscing elit..", '5 months', '20', 'small mammals and reptile', '1', '4'), /*green python, 4*/
("Lorem ipsum dolor sit amet, consectetur adipiscing elit..", '1 month', '0.254', 'insects', '1', '5'), /*green tree frog, 5*/
("Lorem ipsum dolor sit amet, consectetur adipiscing elit..", '1 week', '0.254', ' zooplankton', '3', '5'), /*clownfish, 6*/
("Snowy owls are native to the Arctic regions. One of the largest species of owl, it is the only owl with largely white plumage. Most owls sleep during the day and hunt at night, but the snowy owl is often active during the day, especially in the summertime.Male snowy owls have been known to measure from 52.5 to 64 cm in total length. In wingspan, males may range from 116 to 165.6 cm. The snowy owl is a nomadic bird, rarely breeding at the same locations or with the same mates on an annual basis and often not breeding at all if prey is unavailable. A largely migratory bird, snowy owls can wander almost anywhere close to the Arctic, sometimes unpredictably irrupting to the south in large numbers. Given the difficulty of surveying such an unpredictable bird, there was little in depth knowledge historically about the snowy owl\'s status. However, recent data suggests the species is declining precipitously.", 'up to 28 years', '1300-2500g', 'lemmings, voles, and other small rodents', '1', '8');/*snowy owl, 7*/


INSERT INTO
    `animal` (`name`,`photo`,`zookeeper_id`,`species_id`,`details_id`)
VALUES
(
    'Deer', 'https://images.unsplash.com/photo-1602366250877-ac5e42ce9002?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1076&q=80',
    '1', '4', '1'
),
(
    'Polar Bear', 'https://images.unsplash.com/photo-1589656966895-2f33e7653819?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80',
    '1', '7', '2'
),
(
    'Macaw', 'https://images.unsplash.com/photo-1470081766425-a75c92adff0b?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1134&q=80',
    '1', '2', '3'
),
(
    'Green python', 'https://images.unsplash.com/photo-1533567767427-38bb7cbc0409?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1034&q=80',
    '1', '5', '4'
),
(
    'Dainty green tree frog', 'https://images.unsplash.com/photo-1547511778-db69b35bde3e?ixlib=rb-1.2.1&ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&auto=format&fit=crop&w=1027&q=80',
    '1', '1', '5'
),
(
    'Clownfish', 'https://images.unsplash.com/photo-1535591273668-578e31182c4f?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=2100&q=80',
    '1', '3', '6'
),
(
    'Snowy Owl', 'photo/Vertebrates/Birds/Snowy owl.jpeg',
    '1', '2', '7'
);