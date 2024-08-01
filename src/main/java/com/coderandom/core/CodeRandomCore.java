package com.coderandom.core;

import com.coderandom.core.listener.OnBedrockPlayerJoinListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/**
 * Main class for the CodeRandomCore plugin.
 * Handles initialization, dependency checks, and MySQL management.
 */
public final class CodeRandomCore extends JavaPlugin {

    private static volatile CodeRandomCore instance;
    private static boolean usingMySQL = false;
    private Economy economy;

    /**
     * Called when the plugin is enabled.
     * Initializes configuration, MySQL, and necessary listeners.
     */
    @Override
    public void onEnable() {
        saveDefaultConfig();
        initializeMySQL();
        setupBedrockListener();
        setInstance();
    }

    /**
     * Called when the plugin is disabled.
     * Disconnects from MySQL if it is being used.
     */
    @Override
    public void onDisable() {
        if (usingMySQL) {
            MySQLManager.getInstance().disconnect();
        }
    }

    /**
     * Initializes MySQL connection if enabled in the configuration.
     */
    private void initializeMySQL() {
        if (getConfig().getBoolean("MySQL.enabled")) {
            MySQLManager.initialize(this);
            if (MySQLManager.getInstance().connect()) {
                usingMySQL = true;
            }
        }
    }

    /**
     * Sets up the listener for Bedrock player joins if the Floodgate plugin is present.
     */
    private void setupBedrockListener() {
        if (dependencyCheck("Floodgate")) {
            BedrockUUID.getInstance();
            new OnBedrockPlayerJoinListener();
        }
    }

    /**
     * Sets the instance of this plugin.
     */
    private void setInstance() {
        synchronized (CodeRandomCore.class) {
            if (instance == null) {
                instance = this;
            }
        }
    }

    /**
     * Checks if the specified plugin is present.
     *
     * @param pluginName the name of the plugin to check
     * @return true if the plugin is present, false otherwise
     */
    public boolean dependencyCheck(String pluginName) {
        return getServer().getPluginManager().getPlugin(pluginName) != null;
    }

    /**
     * Handles the dependency requirement check and disables the plugin if the dependency is missing.
     *
     * @param plugin the plugin requiring the dependency
     * @param dependencyName the name of the required dependency
     * @return true if the dependency is present, false otherwise
     */
    public boolean dependencyRequirement(Plugin plugin, String dependencyName) {
        if (!dependencyCheck(dependencyName)) {
            plugin.getLogger().log(Level.SEVERE, dependencyName + " plugin not found! Disabling " + plugin.getName() + "...");
            getServer().getPluginManager().disablePlugin(plugin);
            return false;
        }
        return true;
    }

    /**
     * Retrieves the MySQL manager instance.
     *
     * @return the MySQL manager instance
     */
    public static MySQLManager getMySQLManager() {
        return MySQLManager.getInstance();
    }

    /**
     * Retrieves the plugin instance.
     *
     * @return the plugin instance
     */
    public static CodeRandomCore getInstance() {
        return instance;
    }

    /**
     * Checks if MySQL is being used.
     *
     * @return true if MySQL is being used, false otherwise
     */
    public static boolean usingMySQL() {
        return usingMySQL;
    }

    /**
     * Retrieves the Economy provider if Vault is present.
     *
     * @return the Economy provider, or null if not available
     */
    public Economy getEconomy() {
        if (economy == null && dependencyCheck("Vault")) {
            RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp != null) {
                economy = rsp.getProvider();
            }
        }
        return economy;
    }
}
