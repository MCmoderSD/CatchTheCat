package de.MCmoderSD.utilities.database;

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
        connect();
    }

    // Control

    // Connect to MySQL
    public void connect() {
        if (gameID == null) return; // no gameID set

        try {
            if (isConnected()) return; // already connected
            connection = java.sql.DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password); // connect
            System.out.println("MySQL connected!");
        } catch (java.sql.SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    // Disconnect from MySQL
    public void disconnect() {
        try {
            if (!isConnected()) return; // already disconnected
            connection.close(); // disconnect
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
    public String pullFromMySQL() {
        String encodedData = null;
        try {
            if (!isConnected()) connect(); // connect if not connected

            String query = "SELECT encodedData FROM " + table + " WHERE GameID = ?"; // query
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, gameID); // search for data with gameID
            ResultSet resultSet = preparedStatement.executeQuery(); // execute query

            if (resultSet.next()) encodedData = resultSet.getString("encodedData"); // get encodedData

            // Close connections
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return encodedData; // return encodedData
    }

    // Insert data into MySQL
    public void pushToMySQL(String encodedData) {
        try {
            if (!isConnected()) return; // not connected

            // First, try to update the row
            String updateQuery = "UPDATE " + table + " SET encodedData = ? WHERE GameID = ?"; // query
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setString(1, encodedData); // set encodedData
            updateStatement.setString(2, gameID); // search for data with gameID

            int rowsUpdated = updateStatement.executeUpdate(); // execute query and get updated rows
            updateStatement.close();

            // If no rows were updated, the gameID doesn't exist, so insert a new row
            if (rowsUpdated == 0) {
                String insertQuery = "INSERT INTO " + table + " (GameID, encodedData) VALUES (?, ?)"; // query
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setString(1, gameID); // set gameID
                insertStatement.setString(2, encodedData); // set encodedData
                insertStatement.executeUpdate(); // execute query
                insertStatement.close();

                System.out.println("New Game created " + gameID);
            } else System.out.println("Encoded and Pushed");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    // Getter
    public boolean isConnected() {
        return connection != null;
    }

    // Getter Constants
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