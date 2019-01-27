package com.cschlay.car;

import com.cschlay.car.search.CarNotFoundException;
import com.cschlay.car.search.SearchEngine;
import com.cschlay.database.Connective;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Luokka esittää autoa.
 */
public class Car extends Connective {
    private Brand brand;
    private Engine engine;
    private Model model;
    private String registry;
    private int year;

    /**
     * Luo uuden auton. Ei alusta tietoja millään tavalla.
     */
    public Car() {
        super();
        brand = new Brand();
        engine = new Engine();
        model = new Model();
    }

    /**
     * Hakee auton tietokannasta ja alustaa olion tiedot niillä tiedoilla.
     * Mikäli autoa ei löydy, arvot jää alustamatta.
     * Tilanne on silloin identtinen rakentajan Car() kanssa.

     * @param registry auton rekisterinumero
     */
    public Car(String registry) {
        super();
        brand = new Brand();
        engine = new Engine();
        model = new Model();
        this.registry = registry;

        try {
            SearchEngine search = new SearchEngine();
            Car car = search.searchCar(registry);
            brand.setName(car.getBrandName());
            model.setName(car.getModelName());
            year = car.getYear();
        }
        catch (CarNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tallentaa luodun auton tietokantaan.
     *
     * @return totuusarvo, onko lisäys onnistunut.
     */
    public CarResponse add() {
        CarResponse response = new CarResponse();

        try {
            Connection connection = connector.connect();

            // Tallennetaan ensin auto tietokantaan.
            String sql = "INSERT INTO auto (malli, merkki, rekisterinumero, vuosimalli) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, model.getId());
            preparedStatement.setInt(2, brand.getId());
            preparedStatement.setString(3, registry);
            preparedStatement.setInt(4, year);
            preparedStatement.executeUpdate();
            preparedStatement.close();

            // Haetaan auton id.
            int id = getId();

            // Tallennetaan moottorin tiedot.
            sql = "INSERT INTO moottori (auto, koko, teho) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, engine.getDisplacement());
            preparedStatement.setInt(3, engine.getPower());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();

            response.setMessage(String.format("Auto %s lisättiin tietokantaan.", registry));
            response.setSuccess(true);
        } catch (SQLException e) {
            e.printStackTrace();
            response.setMessage(String.format("Autoa %s ei voitu lisätä.", registry));
            response.setSuccess(false);
        }

        return response;
    }

    /**
     * Poistaa auton tietokannasta.
     * Rekisterinumeron tulee olla asetettu ennen tämän funktion kutsua.
     *
     * @return CarResponse -olio, joka sisältää tiedon onnistumisesta
     */
    public CarResponse delete() {
        CarResponse response = new CarResponse();

        String sql = "DELETE FROM auto WHERE rekisterinumero = ?";

        try {
            if (registry != null) {
                Connection connection = connector.connect();

                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, registry);
                int success = preparedStatement.executeUpdate();

                preparedStatement.close();
                connection.close();

                if (success == 0)
                    throw new CarNotFoundException();
                else {
                    response.setMessage(String.format("Auto %s poistettiin tietokannasta.", registry));
                    response.setSuccess(true);
                }
            }
            else
                throw new SQLException();
        }
        catch (SQLException | CarNotFoundException e) {
            response.setMessage(String.format("Autoa %s ei voitu poistaa.", registry));
            response.setSuccess(false);
        }

        return response;
    }

    /**
     * Muokkaa auton tietoja rekisterinumeron perusteella.
     * Virheelliset rekisterinumeroita ei käsitellä.
     * Oletetaan, että käyttäjällä on muita menetelmiä löytää virheellisesti syötetty rekisteri esimerkiksi käyttäjän
     * perusteella.
     *
     * @return totuusarvo onnistuiko muokkaus.
     */
    public boolean modify() {
        boolean status = false;
        String sql = "UPDATE car SET malli = ?, merkki = ?, vuosimalli = ? WHERE rekisterinumero = ?";

        try {
            Connection connection = connector.connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, model.getId());
            preparedStatement.setInt(2, brand.getId());
            preparedStatement.setInt(3, year);
            preparedStatement.setString(4, registry);

            preparedStatement.executeUpdate();
            status = true;

            preparedStatement.close();
            connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return status;
    }

    public int getBrand() {
        return brand.getId();
    }

    public void setBrand(String name) {
        brand.setName(name);
    }

    public String getBrandName() {
        return brand.getName();
    }

    public int getDisplacement() {
        return engine.getDisplacement();
    }

    public void setDisplacement(int value) {
        engine.setDisplacement(value);
    }

    /**
     * @return auton tietokannassa käytettävä id-tunnus, jos olemassa muuten -1
     */
    public int getId() {
        int id = -1;
        String sql = "SELECT id FROM auto WHERE rekisterinumero = ?";

        try {
            Connection connection = connector.connect();

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, registry);

            ResultSet result =  preparedStatement.executeQuery();

            if (result.next())
                id = result.getInt("id");

            result.close();
            preparedStatement.close();
            connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }

    public int getModel() {
        return model.getId();
    }

    public void setModel(String name) {
        model.setName(name);
    }

    public int getPower() {
        return engine.getPower();
    }

    public void setPower(int value) {
        engine.setPower(value);
    }

    public String getModelName() {
        return model.getName();
    }

    public String getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
