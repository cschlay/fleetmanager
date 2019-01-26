package com.cschlay.database;

/**
 * Luokka, jonka kaikki tietokantaan yhdistävät perivät.
 * Tarkoitus on helpottaa uusien luokkien kirjoittamista.
 */
public class Connective {
    protected static Connector connector;

    public Connective() {
        if (connector == null) {
            connector = new Connector();
        }
    }
}
