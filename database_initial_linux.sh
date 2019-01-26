#!/bin/bash

# Tämä skripti alustaa tietokannan luomalla tauluja ja lisäämällä dataa.

# Oletetaan, että järjestelmä sisältää tietokannan ja käyttäjän nimellä 'cschlay'.

echo "Yhdistetään tietokantaan nimellä cschlay ja lisätään dataa."
psql cschlay <<EOF

CREATE TABLE IF NOT EXISTS merkki (id INTEGER PRIMARY KEY, nimi VARCHAR (50) UNIQUE NOT NULL);
CREATE TABLE IF NOT EXISTS moottori (id INTEGER PRIMARY KEY, koko INTEGER, teho INTEGER);

CREATE TABLE IF NOT EXISTS malli (
    id INTEGER,
    merkki INTEGER NOT NULL,
    nimi VARCHAR (50) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (merkki) REFERENCES merkki(id)
);

CREATE TABLE IF NOT EXISTS auto (
    id INTEGER,
    merkki INTEGER,
    moottori INTEGER,
    rekisterinumero VARCHAR (15) UNIQUE NOT NULL,
    vuosimalli INTEGER NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (merkki) REFERENCES merkki(id),
    FOREIGN KEY (moottori) REFERENCES moottori(id)
);

CREATE TABLE IF NOT EXISTS katsastukset (
    id INTEGER,
    auto INTEGER NOT NULL,
    pvm DATE NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (auto) REFERENCES auto(id)
);
EOF