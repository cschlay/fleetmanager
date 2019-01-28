package com.cschlay.car;

import com.cschlay.car.search.CarNotFoundException;
import com.cschlay.database.Connective;

import java.sql.*;

/**
 * Luokka esittää autoa, sisältää tiedot autosta.
 */
public class Car extends Connective implements Identifiable {
    private Brand brand;
    private Engine engine;
    private Model model;
    private Date inspectionDate;
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

            // Tallennetaan moottorin tiedot ja katsastus.
            int id = getId();
            engine.add(id);
            addInspection(id);

            response.setMessage(String.format("Auto %s lisättiin tietokantaan.", registry));
            response.setSuccess(true);
        }
        catch (SQLException e) {
            response.setMessage(String.format("Autoa %s ei voitu lisätä.", registry));
            response.setSuccess(false);
        }

        return response;
    }

    // Lisää katsastuksen tietokantaan.
    private void addInspection(int id) {
        String sql = "INSERT INTO katsastus (auto, pvm) VALUES (?, ?)";

        try {
            Connection connection = connector.connect();

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.setDate(2, inspectionDate);
            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();
        }
        catch (SQLException e) {
            System.out.println("Katsastusta ei voitu lisätä.");
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

                int deleted = preparedStatement.executeUpdate();

                preparedStatement.close();
                connection.close();

                if (deleted == 0)
                    throw new CarNotFoundException();
                else {
                    response.setMessage(String.format("Auto %s poistettiin tietokannasta.", registry));
                    response.setSuccess(true);
                }
            }
            else
                throw new CarNotFoundException();
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
    public CarResponse modify() {
        String sql = "UPDATE auto SET malli = ?, merkki = ?, vuosimalli = ? WHERE rekisterinumero = ?";
        CarResponse response = new CarResponse();

        try {
            Connection connection = connector.connect();

            // Muutetaan auton tietoja.
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, model.getId());
            preparedStatement.setInt(2, brand.getId());
            preparedStatement.setInt(3, year);
            preparedStatement.setString(4, registry);
            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();

            // Muutetaan moottorin tietoja ja katsastuksen tietoja.
            int id = getId();
            engine.modify(id);
            modifyInspectionDate(id);

            response.setMessage(String.format("Auton %s tietoja muokattiin.", registry));
            response.setSuccess(true);
        }
        catch (SQLException e) {
            response.setMessage(String.format("Auton %s tietoja ei voitu muokata.", registry));
            response.setSuccess(false);
        }

        return response;
    }

    // Muokkaa katsauspäiväärää
    private void modifyInspectionDate(int id) {
        String sql = "UPDATE moottori SET pvm = ? WHERE auto = ?";

        try {
            Connection connection = connector.connect();

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDate(1, inspectionDate);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();
        }
        catch (SQLException e) {
            System.out.println("Katsauspäivämäärää ei voitu muuttaa.");
        }
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
            System.out.println(String.format("Rekisterinumerolla %s ei löydy merkintää.", registry));
        }

        return id;
    }

    public Date getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(Date date) {
        inspectionDate = date;
    }
    public void setInspectionDate(String date) {
        inspectionDate = Date.valueOf(date);
    }

    public int getModel() {
        return model.getId();
    }

    public void setModel(String name) {
        model.setName(name);
    }

    public String getModelName() {
        return model.getName();
    }

    public int getPower() {
        return engine.getPower();
    }

    public void setPower(int value) {
        engine.setPower(value);
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
