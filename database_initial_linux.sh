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

INSERT INTO malli (merkki, nimi) VALUES (1, 'X1');
INSERT INTO malli (merkki, nimi) VALUES (2, 'Y2');

INSERT INTO auto (malli, merkki, rekisterinumero, vuosimalli) VALUES (1, 1, 'TESTI-AXM-103', 1997);
INSERT INTO auto (malli, merkki, rekisterinumero, vuosimalli) VALUES (1, 3, 'TESTI-QLW-951', 2015);

INSERT INTO moottori (auto, koko, teho) VALUES (1, 1500, 13);
INSERT INTO moottori (auto, koko, teho) VALUES (2, 1800, 15);

INSERT INTO katsastus (auto, pvm) VALUES (1, '2001-10-1');
INSERT INTO katsastus (auto, pvm) VALUES (1, '2002-12-3');
INSERT INTO katsastus (auto, pvm) VALUES (2, '2019-01-10');

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public to cschlay;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO cschlay;
EOF