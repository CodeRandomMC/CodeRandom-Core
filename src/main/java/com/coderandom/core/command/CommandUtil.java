package com.coderandom.core.command;

import com.coderandom.core.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class CommandUtil {
    public static boolean checkArgsLength(String[] args, int expectedLength) {
        return args.length == expectedLength;
    }

    public static boolean checkPermission(CommandSender sender, String permission) {
        if (!sender.hasPermission(permission)) {
            noPermission(sender, permission);
            return false;
        }
        return true;
    }

    public static void noPermission(CommandSender sender, String permission) {
        MessageUtils.formattedMessage(sender, "You don't have the permission: " + permission);
    }

    public static Double parseDouble(String doubleString) {
        try {
            return Double.parseDouble(doubleString);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Integer parseInt(String intString) {
        try {
            return Integer.parseInt(intString);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static List<String> tabCompleteFilter(String partialArg, String... options) {
        return Arrays.stream(options)
                .filter(option -> option.startsWith(partialArg.toLowerCase()))
                .collect(Collectors.toList());
    }

    public static List<String> tabCompleteOnlinePlayers(String partialName) {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(name -> name.toLowerCase().startsWith(partialName.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Deprecated //TODO: run performance testing.
    public static List<String> tabCompleteOfflinePlayers(String partialName) {
        return Arrays.stream(Bukkit.getOfflinePlayers())
                .map(OfflinePlayer::getName).filter(Objects::nonNull)
                .filter(name -> name.toLowerCase().startsWith(partialName.toLowerCase()))
                .collect(Collectors.toList());
    }
}
