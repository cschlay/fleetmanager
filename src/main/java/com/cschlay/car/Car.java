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
            engine.setId(car.getEngine());
            model.setName(car.getModelName());
            year = car.getYear();
        }
        catch (CarNotFoundException e) {
            e.printStackTrace();
        }
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
     * Tallentaa luodun auton tietokantaan.
     *
     * @return totuusarvo, onko lisäys onnistunut.
     */
    public boolean store() {
        boolean status = false;
        String sql = "INSERT INTO auto (malli, merkki, moottori, rekisterinumero, vuosimalli) VALUES (?, ?, ?, ?, ?)";

        if (brand.getId() > 0 && engine.getId() > 0 && model.getId() > 0 && registry!= null && year > 1900) {
            try {
                Connection connection = connector.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setInt(1, model.getId());
                preparedStatement.setInt(2, brand.getId());
                preparedStatement.setInt(3, engine.getId());
                preparedStatement.setString(4, registry);
                preparedStatement.setInt(5, year);

                preparedStatement.executeUpdate();
                status = true;

                connection.close();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return status;
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
        String sql = "UPDATE car SET malli = ?, merkki = ?, moottori = ?, vuosimalli = ? WHERE rekisterinumero = ?";

        try {
            Connection connection = connector.connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, model.getId());
            preparedStatement.setInt(2, brand.getId());
            preparedStatement.setInt(3, engine.getId());
            preparedStatement.setInt(4, year);
            preparedStatement.setString(5, registry);

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

    public int getBrand() { return brand.getId(); }
    public void setBrand(String name) { brand.setName(name); }
    public String getBrandName() { return brand.getName(); }
    public int getEngine() { return engine.getId(); }
    public void setEngine(int id) { engine.setId(id); }
    public int getModel() { return model.getId(); }
    public void setModel(String name) { model.setName(name); }
    public String getModelName() { return model.getName(); }
    public String getRegistry() { return registry; }
    public void setRegistry(String registry) { this.registry = registry; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
}
