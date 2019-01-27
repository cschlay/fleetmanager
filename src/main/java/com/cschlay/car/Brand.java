package com.cschlay.car;

import com.cschlay.database.Connective;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Luokka automerkkien hallinointiin.
 */
public class Brand extends Connective {
    private String name;

    /**
     * Hakee tietokannasta automerkin id:n. Palauttaa arvon -1, jos ei löydy sellaista.
     *
     * @return automerkin id -tunnus
     */
    public int getId() {
        int id = -1;

        try {
            Connection connection = connector.connect();
            Statement query = connection.createStatement();
            ResultSet result = query.executeQuery(
                    String.format("SELECT id FROM merkki WHERE nimi = '%s'", this.name)
            );

            if (result.next())
                id = result.getInt("id");

            result.close();
            connection.close();
            query.close();
        } catch (SQLException e) {
            System.out.println(String.format("VIRHE: Automerkkiä %s ei ole olemassa.", this.name));
        }

        return id;
    }

    public String getName() { return name; }
    public void setName(String name) {
        this.name = name;
    }
}
