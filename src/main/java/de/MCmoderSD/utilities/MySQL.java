package de.MCmoderSD.utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class MySQL {

    // Constants
    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private final String table;

    // Attributes
    private Connection connection;
    private String gameID;

    // Constructors
    public MySQL(String host, int port, String database, String username, String password, String table, String gameID) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.table = table;
        this.gameID = gameID;
    }

    // Control

    // Connect to MySQL
    public void connect() {
        if (gameID == null) return;
        try {
            if (isConnected()) return;
            connection = java.sql.DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
            System.out.println("MySQL connected!");
        } catch (java.sql.SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    // Disconnect from MySQL
    public void disconnect() {
        try {
            if (!isConnected()) return;
            connection.close();
            System.out.println("MySQL disconnected!");
        } catch (java.sql.SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    // Set gameID
    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    // Get encoded data from MySQL
    public String getEncodedData() {
        String encodedData = null;
        try {
            if (!isConnected()) connect();

            String query = "SELECT encodedData FROM " + table + " WHERE GameID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, gameID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) encodedData = resultSet.getString("encodedData");

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        //System.out.println("Data Pulled");
        return encodedData;
    }

    // Insert data into MySQL
    public void updateEncodedData(String encodedData) {
        try {
            if (!isConnected()) connect();

            // First, try to update the row
            String updateQuery = "UPDATE " + table + " SET encodedData = ? WHERE GameID = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setString(1, encodedData);
            updateStatement.setString(2, gameID);

            int rowsUpdated = updateStatement.executeUpdate();
            updateStatement.close();

            // If no rows were updated, the gameID doesn't exist, so insert a new row
            if (rowsUpdated == 0) {
                String insertQuery = "INSERT INTO " + table + " (GameID, encodedData) VALUES (?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setString(1, gameID);
                insertStatement.setString(2, encodedData);
                insertStatement.executeUpdate();
                insertStatement.close();

                System.out.println("New Game created " + gameID);
            } else System.out.println("Data Pushed");

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    // Getter Constants
    public boolean isConnected() {
        return connection != null;
    }

    public Connection getConnection() {
        return connection;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
