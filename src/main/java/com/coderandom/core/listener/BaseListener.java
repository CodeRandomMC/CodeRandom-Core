package com.coderandom.core.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

/**
 * Abstract base class for event listeners in the plugin.
 * Automatically registers the listener with the Bukkit event system.
 */
public abstract class BaseListener implements Listener {

    /**
     * Constructs a new BaseListener and registers it with the plugin.
     *
     * @param plugin the plugin instance to register the listener with
     */
    public BaseListener(Plugin plugin) {
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }
}
