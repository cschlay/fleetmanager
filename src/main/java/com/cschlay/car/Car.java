package com.cschlay.car;

import com.cschlay.database.Connective;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Luokka esittää autoa.
 */
public class Car extends Connective {
    private Model model;
    private Brand brand;
    private Engine engine;
    private String registry;
    private int year;

    /**
     * Luo uuden auton.
     */
    public Car() {
        super();
        brand = new Brand();
        engine = new Engine();
        model = new Model();
    }


    public void setBrand(String name) { brand.setName(name); }
    public void setEngine(int id) { engine.setId(id); }
    public void setModel(String name) { model.setName(name); }
    public void setRegistry(String registry) { this.registry = registry; }
    public void setYear(int year) { this.year = year; }

    /**
     * Tallentaa luodun auton tietokantaan.
     *
     * @return totuusarvo, onko lisäys onnistunut.
     */
    public boolean store() {
        boolean status = false;
        String sql = "INSERT INTO auto (malli, merkki, moottori, rekisterinumero, vuosimalli) VALUES (?, ?, ?, ?, ?)";

        try {
            Connection connection = connector.connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, model.getId());
            preparedStatement.setInt(2, brand.getId());
            preparedStatement.setInt(3, engine.getId());
            preparedStatement.setString(4, registry);
            preparedStatement.setInt(5, year);

            preparedStatement.executeUpdate();

            connection.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return status;
    }
}
