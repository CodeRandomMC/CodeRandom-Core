package com.coderandom.core;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.plugin.Plugin;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages MySQL connections using HikariCP connection pool.
 * Singleton class for database operations.
 */
public final class MySQLManager {

    private static volatile MySQLManager instance;
    private static Plugin plugin;
    private static Logger LOGGER;
    private HikariDataSource dataSource;

    private MySQLManager() {
        LOGGER = plugin.getLogger();
    }

    /**
     * Initializes the MySQLManager singleton instance.
     *
     * @param pluginInstance the plugin instance
     * @throws IllegalStateException if the manager is already initialized
     */
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

    /**
     * Returns the singleton instance of MySQLManager.
     *
     * @return the MySQLManager instance
     * @throws IllegalStateException if the manager is not initialized
     */
    static synchronized MySQLManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("MySQLManager is not initialized. Call initialize() first.");
        }
        return instance;
    }

    /**
     * Connects to the MySQL database using HikariCP.
     *
     * @return true if connected successfully, false otherwise
     */
    public boolean connect() {
        try {
            if (dataSource != null && !dataSource.isClosed()) {
                return false;
            }
            initializeDataSource();
            LOGGER.log(Level.INFO, "Connected to MySQL database.");
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Could not connect to MySQL database!", e);
            return false;
        }
    }

    /**
     * Initializes the HikariCP data source with the configuration from the plugin.
     */
    private void initializeDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + plugin.getConfig().getString("MySQL.host", "localhost") +
                ":" + plugin.getConfig().getString("MySQL.port", "3306") +
                "/" + plugin.getConfig().getString("MySQL.database", "code_random") +
                "?useSSL=false");
        config.setUsername(plugin.getConfig().getString("MySQL.username", "root"));
        config.setPassword(plugin.getConfig().getString("MySQL.password", ""));
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setMaxLifetime(600000);
        config.setConnectionTimeout(30000);

        this.dataSource = new HikariDataSource(config);
        LOGGER.log(Level.INFO, "MySQL connection pool initialized.");
    }

    /**
     * Disconnects from the MySQL database by closing the HikariCP data source.
     */
    public void disconnect() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            LOGGER.log(Level.INFO, "MySQL connection pool closed.");
        }
    }

    /**
     * Retrieves a connection from the HikariCP data source.
     *
     * @return a SQL connection
     * @throws SQLException if a database access error occurs
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Executes a query with the provided parameters and returns the result set.
     *
     * @param query      the SQL query
     * @param parameters the parameters for the query
     * @return the result set of the query
     * @throws SQLException if a database access error occurs
     */
    public ResultSet executeQuery(String query, Object... parameters) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            setParameters(ps, parameters);
            return ps.executeQuery();
        }
    }

    /**
     * Executes an update with the provided parameters.
     *
     * @param query      the SQL query
     * @param parameters the parameters for the query
     * @throws SQLException if a database access error occurs
     */
    public void executeUpdate(String query, Object... parameters) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            setParameters(ps, parameters);
            ps.executeUpdate();
        }
    }

    /**
     * Executes a batch update with the provided parameters.
     *
     * @param query      the SQL query
     * @param parameters the batch parameters for the query
     * @throws SQLException if a database access error occurs
     */
    public void executeBatchUpdate(String query, Object[][] parameters) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            for (Object[] parameterSet : parameters) {
                setParameters(ps, parameterSet);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    /**
     * Sets the parameters for a prepared statement.
     *
     * @param ps         the prepared statement
     * @param parameters the parameters to set
     * @throws SQLException if a database access error occurs
     */
    private void setParameters(PreparedStatement ps, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            ps.setObject(i + 1, parameters[i]);
        }
    }

    /**
     * Executes a table creation query.
     *
     * @param tableCreationQuery the SQL query to create tables
     */
    public void createTables(String tableCreationQuery) {
        try {
            executeUpdate(tableCreationQuery);
            LOGGER.log(Level.INFO, "Table creation query executed.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not create table!", e);
        }
    }
}
