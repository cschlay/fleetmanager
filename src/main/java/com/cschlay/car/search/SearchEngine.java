package com.cschlay.car.search;

import com.cschlay.car.Car;
import com.cschlay.database.Connective;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchEngine extends Connective {

    /**
     * Hakee auton rekisterinumeron perusteella.
     *
     * @param registry haettavan auton rekisterinumero
     * @return Car -olio
     * @throws CarNotFoundException Jos autoa löydy tietokannasta.
     */
    public Car searchCar(String registry) throws CarNotFoundException {
        String sql = "SELECT malli.nimi AS malli, merkki.nimi AS merkki, moottori, rekisterinumero, vuosimalli " +
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
                car.setEngine(result.getInt("moottori"));
                car.setYear(result.getInt("vuosimalli"));
                car.setRegistry(registry);

                result.close();
                preparedStatement.close();
                connection.close();
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
}
