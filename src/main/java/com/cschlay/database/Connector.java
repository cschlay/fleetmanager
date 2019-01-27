package com.cschlay.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Yhdistin -luokka, jota käytetään tietokantaan yhdistämisen apuna.
 * Luokkaan abstraktoidaan yleisimmin käytetyt tietokantaoperaatiot.
 */
public class Connector {
    private final String USERNAME;
    private final String PASSWORD;
    private final String DATABASE;

    /**
     * Luo uuden yhdistimen oletusarvoilla.
     * Oletetaan, että tietokannan nimi on cschlay ja salasana koodikas sekä tietokantana on postgresql.
     */
    public Connector() {
        USERNAME = "cschlay";
        PASSWORD = "koodikas";
        DATABASE = "jdbc:postgresql:cschlay";
    }

    /**
     * Toisenlainen rakentaja mahdollistaa luokan käyttämisen yhdistettäessä muihinkin tietokantoihin.
     *
     * @param username tietokannan käyttäjänimi
     * @param password tietokannan salasana
     * @param database tietokannan osoite esim. muodossa 'jdbc:postgres:cschlay'
     */
    public Connector(String username, String password, String database) {
        USERNAME = username;
        PASSWORD = password;
        DATABASE = database;
    }

    /**
     * Yhdistää tietokantaan ja palauttaa Connection -tyyppisen olion.
     *
     * @return Connection
     */
    public Connection connect() {
        try {
            return DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
        }
        catch (SQLException e) {
            return null;
        }
    }
}
