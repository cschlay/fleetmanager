package com.cschlay.car;

import com.cschlay.database.Connective;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Luokka esittää autoa.
 */
public class Car extends Connective {
    private Brand brand;


    /**
     * Luo uuden auton.
     */
    public Car() {
        super();
    }

    public void setBrand(String name) {
        brand = new Brand(name);
    }

    /**
     * Tallentaa luodun auton tietokantaan.
     *
     * @return totuusarvo, onko lisäys onnistunut.
     */
    public boolean store() {
        boolean status = false;
        String sql = "INSERT INTO auto (merkki, moottori, rekisterinumero, vuosimalli) VALUES (?, ?, ?, ?)";

        try {
            Connection connection = connector.connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, brand.getId());

            connection.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return status;
    }
}
