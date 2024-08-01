package com.coderandom.core;

import org.bukkit.plugin.Plugin;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class MySQLManager {
    private static volatile MySQLManager instance;
    private static Plugin plugin;
    private static Logger LOGGER;
    private final String host;
    private final String port;
    private final String database;
    private final String username;
    private final String password;
    private Connection connection;

    private MySQLManager() {
        this.host = plugin.getConfig().getString("MySQL.host", "localhost");
        this.port = plugin.getConfig().getString("MySQL.port", "3306");
        this.database = plugin.getConfig().getString("MySQL.database", "code_random");
        this.username = plugin.getConfig().getString("MySQL.username", "root");
        this.password = plugin.getConfig().getString("MySQL.password", "");

        LOGGER = plugin.getLogger();
    }

    // Package-private to limit access to this method
    static synchronized void initialize(Plugin pluginInstance) {
        if (plugin == null) {
            plugin = pluginInstance;
        }
        if (instance == null) {
            instance = new MySQLManager();
        } else {
            throw new IllegalStateException("MySQLManager has already been initialized.");
        }
    }

    static synchronized MySQLManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("MySQLManager is not initialized. Call initialize() first.");
        }
        return instance;
    }

    boolean connect() {
        try {
            if (connection != null && !connection.isClosed()) {
                return false;
            }
            String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false";
            connection = DriverManager.getConnection(url, username, password);
            LOGGER.log(Level.INFO, "Connected to MySQL database.");
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not connect to MySQL database!", e);
            return false;
        }
    }

    void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                LOGGER.log(Level.INFO, "MySQL Disconnected!");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not disconnect from MySQL database!", e);
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not reconnect to MySQL database!", e);
        }
        return connection;
    }

    public ResultSet executeQuery(String query, Object... parameters) throws SQLException {
        validateConnection();
        PreparedStatement ps = connection.prepareStatement(query);
        setParameters(ps, parameters);
        return ps.executeQuery();
    }

    public void executeUpdate(String query, Object... parameters) throws SQLException {
        validateConnection();
        PreparedStatement ps = connection.prepareStatement(query);
        setParameters(ps, parameters);
        ps.executeUpdate();
        ps.close();
    }

    private void validateConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connect();
        }
    }

    private void setParameters(PreparedStatement ps, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            ps.setObject(i + 1, parameters[i]);
        }
    }

    public void createTables(String tableCreationQuery) {
        try {
            executeUpdate(tableCreationQuery);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not create table!", e);
        }
    }
}
