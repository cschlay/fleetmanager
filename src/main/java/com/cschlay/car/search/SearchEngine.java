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
            Connection connection = connector.connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, registry);

            Car car = new Car();
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                car.setBrand(result.getString("merkki"));
                car.setModel(result.getString("malli"));
                car.setYear(result.getInt("vuosimalli"));
                car.setRegistry(registry);

                result.close();
                preparedStatement.close();
                connection.close();

                getEngineInfo(car);
                return car;
            }
            else {
                result.close();
                preparedStatement.close();
                connection.close();
                throw new SQLException();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new CarNotFoundException();
        }
    }

    /**
     * Hakee rajausten perusteella tietokannasta autoja.
     *
     * @return lista autoista
     */
    public List<Car> carListing() {
        List<Car> list = new Vector<>();
        String sql = "SELECT rekisterinumero FROM auto WHERE ";

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
                    e.printStackTrace();
                }
            }

            result.close();
            preparedStatement.close();
            connection.close();
        }
        catch (SQLException e) { e.printStackTrace(); }

        return list;
    }

    /**
     * Hakee moottorin tiedot Car -oliolle.
     * @param car auto oliona
     */
    private void getEngineInfo(Car car) {
        String sql = "SELECT koko, teho FROM moottori WHERE auto = ?";

        try {
            Connection connection = connector.connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, car.getId());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                car.setDisplacement(resultSet.getInt("koko"));
                car.setPower(resultSet.getInt("teho"));
            }

            preparedStatement.close();
            connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
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
