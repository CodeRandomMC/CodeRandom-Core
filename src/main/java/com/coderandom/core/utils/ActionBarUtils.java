package com.coderandom.core.utils;

import com.coderandom.core.CodeRandomCore;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public final class ActionBarUtils {
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
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message))
                );
                count++;
            }
        }.runTaskTimerAsynchronously(CodeRandomCore.getInstance(), 0L, 20L); // Run every second (20 ticks)
    }

    public static void actionBar(Player player, String message) {
        Bukkit.getScheduler().runTask(CodeRandomCore.getInstance(), () ->
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message))
        );
    }
}
