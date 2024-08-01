package com.coderandom.core;

import com.coderandom.core.listener.OnBedrockPlayerJoinListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class CodeRandomCore extends JavaPlugin {
    private static volatile CodeRandomCore instance;
    private static boolean usingMySQL = false;
    private Economy economy;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        initializeMySQL();

        if (dependencyCheck("Floodgate")) {
            BedrockUUID.getInstance();
            new OnBedrockPlayerJoinListener();
        }

        synchronized (CodeRandomCore.class) {
            if (instance == null) {
                instance = this;
            }
        }
    }

    @Override
    public void onDisable() {
        if (usingMySQL) {
            MySQLManager.getInstance().disconnect();
        }
    }

    private void initializeMySQL() {
        if (getConfig().getBoolean("MySQL.enabled")) {
            MySQLManager.initialize(this);
            if (MySQLManager.getInstance().connect()) {
                usingMySQL = true;
            }
        }
    }

    public static boolean usingMySQL() {
        return usingMySQL;
    }

    public static MySQLManager getMySQLManager() {
        return MySQLManager.getInstance();
    }

    public static CodeRandomCore getInstance() {
        return instance;
    }

    public Economy getEconomy() {
        if (economy == null && dependencyCheck("Vault")) {
            RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp != null) {
                economy = rsp.getProvider();
            }
        }
        return economy;
    }

    // Checks if the specified plugin is present
    public boolean dependencyCheck(String pluginName) {
        return getServer().getPluginManager().getPlugin(pluginName) != null;
    }

    // Handles the dependency requirement check and disables the plugin if the dependency is missing
    public boolean dependencyRequirement(Plugin plugin, String dependencyName) {
        if (!dependencyCheck(dependencyName)) {
            plugin.getLogger().log(Level.SEVERE, dependencyName + " plugin not found! Disabling " + plugin.getName() + "...");
            getServer().getPluginManager().disablePlugin(plugin);
            return false;
        }
        return true;
    }
}
