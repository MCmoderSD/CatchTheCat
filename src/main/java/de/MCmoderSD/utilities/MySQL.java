package de.MCmoderSD.utilities;

import java.sql.Connection;

@SuppressWarnings("unused")
public class MySQL {
    private final String host;
    private final String port;
    private final String database;
    private final String username;
    private final String password;
    private Connection connection;

    public MySQL(String host, String port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public boolean isConnected() {
        return connection != null;
    }

    public void connect() {
        try {
            if (isConnected()) return;
            connection = java.sql.DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", username, password);
            System.out.println("MySQL connected!");
        } catch (java.sql.SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void disconnect() {
        try {
            if (!isConnected()) return;
            connection.close();
            System.out.println("MySQL disconnected!");
        } catch (java.sql.SQLException e) {
            System.err.println(e.getMessage());
        }
    }


    // Getter Constants
    public Connection getConnection() {
        return connection;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
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
