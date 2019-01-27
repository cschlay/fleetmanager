package com.cschlay.car.search;

import com.cschlay.car.Brand;
import com.cschlay.car.Car;
import com.cschlay.car.Model;
import com.cschlay.database.Connective;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

/**
 * Pieni hakukone, joka on optimoitu autojen hakemiseen.
 */
public class SearchEngine extends Connective {
    // Rajausehtoja.
    // Kaikkia ei pidä alustaa, sillä hakutoiminnon kyselyt hyödyntävät oletusarvoja.
    private int minYear;
    private int maxYear;
    private Brand brand;
    private Model model;

    public SearchEngine() {
        brand = new Brand();
        model = new Model();
    }

    /**
     * Hakee rajausten perusteella tietokannasta autoja.
     * Muodostaa näistä listan ja palauttaa sen.
     *
     * @return lista autoista
     */
    public List<Car> carListing() {
        String sql = "SELECT rekisterinumero FROM auto WHERE ";
        List<Car> list = new Vector<>();

        boolean hasMinYear = minYear != 0;
        boolean hasMaxYear = maxYear != 0;
        boolean hasBrand = brand.getName() != null;
        boolean hasModel = model.getName() != null;

        // Rajoituksien lisäämistä.
        sql += (hasMinYear ? "vuosimalli >= ? " : "vuosimalli >= 1900 ");
        sql += (hasMaxYear ? "AND vuosimalli <= ? " : "AND vuosimalli <= 2100 ");

        if (hasBrand)
            sql += "AND merkki = ? ";

        if (hasModel)
            sql += "AND malli = ?";

        try {
            Connection connection = connector.connect();

            int i = 1;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            if (hasMinYear)
                preparedStatement.setInt(i++, minYear);

            if (hasMaxYear)
                preparedStatement.setInt(i++, maxYear);

            if (hasBrand)
                preparedStatement.setInt(i++, brand.getId());

            if (hasModel)
                preparedStatement.setInt(i, model.getId());

            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                String registry = result.getString("rekisterinumero");

                try {
                    list.add(searchCar(registry));
                }
                catch (CarNotFoundException e) {
                    System.out.println("Yksittäistä autoa ei voitu hakea.");
                }
            }

            result.close();
            preparedStatement.close();
            connection.close();
        }
        catch (SQLException e) {
            System.out.println("Autojen haku ei suju.");
        }

        return list;
    }

    /**
     * Hakee auton rekisterinumeron perusteella.
     *
     * @param registry haettavan auton rekisterinumero
     * @return Car -olio alustettuna tietokansta haetuilla arvoilla
     * @throws CarNotFoundException jos autoa löydy tietokannasta.
     */
    public Car searchCar(String registry) throws CarNotFoundException {
        String sql = "SELECT malli.nimi AS malli, merkki.nimi AS merkki, rekisterinumero, vuosimalli " +
                "FROM auto LEFT OUTER JOIN merkki " +
                "ON auto.merkki = merkki.id " +
                "LEFT OUTER JOIN malli " +
                "ON auto.malli = malli.id " +
                "WHERE rekisterinumero = ?";

        try {
            Car car = new Car();

            Connection connection = connector.connect();

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, registry);

            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                // Asetetaan auto -oliolle tietoja.
                car.setBrand(result.getString("merkki"));
                car.setModel(result.getString("malli"));
                car.setYear(result.getInt("vuosimalli"));
                car.setRegistry(registry);

                result.close();
                preparedStatement.close();
                connection.close();

                // Haetaan moottorin tiedot.
                getEngineInfo(car);

                // Haetaan seuraava katsastus.
                getInspectionInfo(car);

                return car;
            }
            // Heitetään virhe, jos autoa ei löydy.
            else {
                result.close();
                preparedStatement.close();
                connection.close();
                throw new SQLException();
            }
        }
        catch (SQLException e) {
            throw new CarNotFoundException();
        }
    }

    /**
     * Hakee moottorin tiedot Car -oliolle.
     *
     * @param car auto oliona
     */
    private void getEngineInfo(Car car) {
        String sql = "SELECT koko, teho FROM moottori WHERE auto = ?";

        try {
            Connection connection = connector.connect();

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, car.getId());

            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                car.setDisplacement(result.getInt("koko"));
                car.setPower(result.getInt("teho"));
            }

            result.close();
            preparedStatement.close();
            connection.close();
        }
        catch (SQLException e) {
            System.out.println("Moottorin tiedoissa on vikaa.");
        }
    }

    /**
     * Hakee katsastuspäivämäärän.
     *
     * @param car auto oliona
     */
    public void getInspectionInfo(Car car) {
        String sql = "SELECT pvm FROM katsastus WHERE auto = ?";

        try {
            Connection connection = connector.connect();

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, car.getId());

            ResultSet result = preparedStatement.executeQuery();

            if (result.next())
                car.setInspectionDate(result.getDate("pvm"));

            result.close();
            preparedStatement.close();
            connection.close();
        }
        catch (SQLException e) {
            System.out.println("Katsastuspäivämäärää ei löytynyt.");
        }
    }

    public int getMinYear() {
        return minYear;
    }

    public void setMinYear(int year) {
        minYear = year;
    }

    public int getMaxYear() {
        return maxYear;
    }

    public void setMaxYear(int year) {
        maxYear = year;
    }

    public String getBrand() {
        return brand.getName();
    }

    public void setBrand(String brand) {
        this.brand.setName(brand);
    }

    public String getModel() {
        return model.getName();
    }

    public void setModel(String model) {
        this.model.setName(model);
    }
}
