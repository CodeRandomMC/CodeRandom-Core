package com.coderandom.cr_core;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MessageUtils {

    public static void title(Player player, String title) {
        title(player, title, "");
    }

    public static void title(Player player, String title, String subTitle) {
        title(player, title, subTitle, 20, 100, 20);
    }

    public static void title(Player player, String title, int fadeIn, int stay, int fadeOut) {
        title(player, title, "", fadeIn, stay, fadeOut);
    }

    public static void title(Player player, String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        Bukkit.getScheduler().runTask(CRCore.getInstance(), () -> player.sendTitle(title, subTitle, fadeIn, stay, fadeOut));
    }

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
                Bukkit.getScheduler().runTask(CRCore.getInstance(), () ->
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message))
                );
                count++;
            }
        }.runTaskTimerAsynchronously(CRCore.getInstance(), 0L, 20L); // Run every second (20 ticks)
    }

    public static void actionBar(Player player, String message) {
        Bukkit.getScheduler().runTask(CRCore.getInstance(), () ->
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message))
        );
    }
}
