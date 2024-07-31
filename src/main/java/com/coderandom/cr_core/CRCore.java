package com.coderandom.cr_core;

import org.bukkit.plugin.java.JavaPlugin;

public final class CRCore extends JavaPlugin {
    private static volatile CRCore instance;
    private static boolean usingMySQL = false;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        if (getConfig().getBoolean("MySQL.enabled")) {
            MySQLManager.initialize(this);
            if (MySQLManager.getInstance().connect()) {
                usingMySQL = true;
            }
        }

        synchronized (CRCore.class) {
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

    public static boolean usingMySQL() {
        return usingMySQL;
    }

    public static MySQLManager getMySQLManager() {
        return MySQLManager.getInstance();
    }

    public static CRCore getInstance() {
        return instance;
    }
}
