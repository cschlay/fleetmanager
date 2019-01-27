package com.cschlay.car;

import com.cschlay.database.Connective;

/**
 * Moottorille ei ole toteutettu tuotekoodia merkkijonona, joten käytämme kuvitteellista id -tunnusta.
 */
public class Engine extends Connective implements Identifiable {
    private int id;
    private int power;
    private int displacement;   // https://en.wikipedia.org/wiki/Engine_displacement


    public int getDisplacement() {
        return displacement;
    }

    public void setDisplacement(int displacement) {
        this.displacement = displacement;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }
}
