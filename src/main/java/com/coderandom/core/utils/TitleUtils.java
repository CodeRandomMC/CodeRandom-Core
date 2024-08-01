package com.coderandom.core.utils;

import com.coderandom.core.CodeRandomCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Utility class for sending titles to players.
 */
public final class TitleUtils {

    private TitleUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Sends a title to a player with default subtitle and timings.
     *
     * @param player the player to send the title to
     * @param title  the title text
     */
    public static void title(Player player, String title) {
        title(player, title, "");
    }

    /**
     * Sends a title and subtitle to a player with default timings.
     *
     * @param player   the player to send the title to
     * @param title    the title text
     * @param subTitle the subtitle text
     */
    public static void title(Player player, String title, String subTitle) {
        title(player, title, subTitle, 20, 100, 20);
    }

    /**
     * Sends a title to a player with specified timings and no subtitle.
     *
     * @param player  the player to send the title to
     * @param title   the title text
     * @param fadeIn  the fade-in duration in ticks
     * @param stay    the stay duration in ticks
     * @param fadeOut the fade-out duration in ticks
     */
    public static void title(Player player, String title, int fadeIn, int stay, int fadeOut) {
        title(player, title, "", fadeIn, stay, fadeOut);
    }

    /**
     * Sends a title and subtitle to a player with specified timings.
     *
     * @param player   the player to send the title to
     * @param title    the title text
     * @param subTitle the subtitle text
     * @param fadeIn   the fade-in duration in ticks
     * @param stay     the stay duration in ticks
     * @param fadeOut  the fade-out duration in ticks
     */
    public static void title(Player player, String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        Bukkit.getScheduler().runTask(CodeRandomCore.getInstance(), () ->
                player.sendTitle(title, subTitle, fadeIn, stay, fadeOut)
        );
    }
}
