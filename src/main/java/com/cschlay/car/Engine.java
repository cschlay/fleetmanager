package com.cschlay.car;

import com.cschlay.database.Connective;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Moottorille ei ole toteutettu tuotekoodia merkkijonona, joten käytämme kuvitteellista id -tunnusta.
 */
public class Engine extends Connective implements Identifiable {
    private int id;
    private int power;
    private int displacement;   // https://en.wikipedia.org/wiki/Engine_displacement

    // Lisää moottorin tiedot tietokantaan.
    public void add(int id) {
        String sql = "INSERT INTO moottori (auto, koko, teho) VALUES (?, ?, ?)";

        try {
            Connection connection = connector.connect();

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, displacement);
            preparedStatement.setInt(3, power);
            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();
        }
        catch (SQLException e) {
            System.out.println("Moottorin tietoja ei voitu lisätä.");
        }
    }

    // Muokkaa moottorin tietoja.
    public void modify(int id) {
        String sql = "UPDATE moottori SET koko = ?, teho = ? WHERE auto = ?";

        try {
            Connection connection = connector.connect();

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, displacement);
            preparedStatement.setInt(2, power);
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();
        }
        catch (SQLException e) {
            System.out.println("Moottorin tietoja ei voitu muokata.");
        }
    }

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
