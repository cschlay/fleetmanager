#!/bin/bash

# Tämä skripti alustaa tietokannan luomalla tauluja ja lisäämällä dataa.

# Oletetaan, että järjestelmä sisältää tietokannan ja käyttäjän nimellä 'cschlay'.

echo "Yhdistetään tietokantaan nimellä cschlay ja lisätään dataa."
psql cschlay <<EOF

CREATE TABLE IF NOT EXISTS merkki (id SERIAL PRIMARY KEY, nimi VARCHAR (50) UNIQUE NOT NULL);

CREATE TABLE IF NOT EXISTS malli (
    id SERIAL,
    merkki INTEGER NOT NULL,
    nimi VARCHAR (50) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (merkki) REFERENCES merkki(id)
);

CREATE TABLE IF NOT EXISTS auto (
    id SERIAL,
    malli INTEGER,
    merkki INTEGER,
    rekisterinumero VARCHAR (15) UNIQUE NOT NULL,
    vuosimalli INTEGER NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (malli) REFERENCES malli(id),
    FOREIGN KEY (merkki) REFERENCES merkki(id)
);

CREATE TABLE IF NOT EXISTS moottori (
    id SERIAL,
    auto INTEGER UNIQUE,
    koko INTEGER,
    teho INTEGER,
    PRIMARY KEY (id),
    FOREIGN KEY (auto) REFERENCES auto(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS katsastus (
    id SERIAL,
    auto INTEGER NOT NULL,
    pvm DATE NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (auto) REFERENCES auto(id) ON DELETE CASCADE
);

INSERT INTO merkki (nimi) VALUES ('Clay');
INSERT INTO merkki (nimi) VALUES ('Alcy');
INSERT INTO merkki (nimi) VALUES ('Yalc');
INSERT INTO merkki (nimi) VALUES ('Caly');


INSERT INTO malli (merkki, nimi) VALUES (1, 'X1');
INSERT INTO malli (merkki, nimi) VALUES (1, 'X2');
INSERT INTO malli (merkki, nimi) VALUES (2, 'Y1');
INSERT INTO malli (merkki, nimi) VALUES (3, 'S');
INSERT INTO malli (merkki, nimi) VALUES (4, 'L');

INSERT INTO auto (malli, merkki, rekisterinumero, vuosimalli) VALUES (1, 1, 'TESTI-1', 1997);
INSERT INTO auto (malli, merkki, rekisterinumero, vuosimalli) VALUES (2, 2, 'TESTI-2', 2006);
INSERT INTO auto (malli, merkki, rekisterinumero, vuosimalli) VALUES (3, 3, 'TESTI-3', 2015);
INSERT INTO auto (malli, merkki, rekisterinumero, vuosimalli) VALUES (4, 4, 'TESTI-4', 2019);

INSERT INTO moottori (auto, koko, teho) VALUES (1, 1500, 13);
INSERT INTO moottori (auto, koko, teho) VALUES (2, 1700, 20);
INSERT INTO moottori (auto, koko, teho) VALUES (3, 1400, 15);
INSERT INTO moottori (auto, koko, teho) VALUES (4, 1800, 12);


INSERT INTO katsastus (auto, pvm) VALUES (1, '2023-10-01');
INSERT INTO katsastus (auto, pvm) VALUES (2, '2022-12-03');
INSERT INTO katsastus (auto, pvm) VALUES (3, '2021-10-05');
INSERT INTO katsastus (auto, pvm) VALUES (4, '2020-12-03');

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public to cschlay;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO cschlay;
EOF