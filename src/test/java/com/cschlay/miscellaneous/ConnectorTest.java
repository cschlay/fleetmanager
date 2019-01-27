package com.cschlay.miscellaneous;

import com.cschlay.database.Connector;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectorTest {

    @Test
    public void connectionTest() throws SQLException {
        Connector connector = new Connector();
        Connection connection = connector.connect();

        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM auto");

        while (result.next()) {
            System.out.println(result.getString("rekisterinumero"));
        }
        result.close();

        statement.close();
        connection.close();
    }
}
