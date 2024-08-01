package com.coderandom.core.utils;

import com.coderandom.core.CodeRandomCore;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Utility class for sending action bar messages to players.
 */
public final class ActionBarUtils {

    private ActionBarUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Sends an action bar message to a player for a specified duration.
     *
     * @param player  the player to send the message to
     * @param message the message to send
     * @param stay    the duration in ticks for the message to stay (20 ticks = 1 second)
     */
    public static void actionBar(Player player, String message, int stay) {
        new BukkitRunnable() {
            int count = 0;
            final int repetitions = stay / 20; // stay duration in ticks divided by interval (20 ticks = 1 second)

            @Override
            public void run() {
                if (count >= repetitions) {
                    cancel();
                    return;
                }
                Bukkit.getScheduler().runTask(CodeRandomCore.getInstance(), () ->
                        actionBar(player, message)
                );
                count++;
            }
        }.runTaskTimerAsynchronously(CodeRandomCore.getInstance(), 0L, 20L); // Run every second (20 ticks)
    }

    /**
     * Sends a single action bar message to a player.
     *
     * @param player  the player to send the message to
     * @param message the message to send
     */
    public static void actionBar(Player player, String message) {
        Bukkit.getScheduler().runTask(CodeRandomCore.getInstance(), () ->
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message))
        );
    }
}
