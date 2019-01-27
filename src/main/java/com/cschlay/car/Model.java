package com.cschlay.car;

import com.cschlay.database.Connective;

import java.sql.*;

/**
 * Luokka automerkkien hallinointiin.
 */
public class Model extends Connective implements Identifiable {
    private String name;

    /**
     * Hakee tietokannasta automerkin id:n. Palauttaa arvon -1, jos ei löydy sellaista.
     *
     * @return automerkin id -tunnus
     */
    public int getId() {
        String sql = "SELECT id FROM malli WHERE nimi = ?";
        int id = -1;

        try {
            Connection connection = connector.connect();

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);

            ResultSet result = preparedStatement.executeQuery();

            if (result.next())
                id = result.getInt("id");

            result.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println(String.format("Mallia %s ei ole olemassa.", this.name));
        }

        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
