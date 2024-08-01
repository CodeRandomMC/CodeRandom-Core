package com.coderandom.core.utils;

import com.coderandom.core.CodeRandomCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class TitleUtils {
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
        Bukkit.getScheduler().runTask(CodeRandomCore.getInstance(), () -> player.sendTitle(title, subTitle, fadeIn, stay, fadeOut));
    }
}
