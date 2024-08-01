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

/**
 * Utility class for command-related functionalities.
 */
public final class CommandUtil {

    private CommandUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Checks if the length of arguments matches the expected length.
     *
     * @param args           the arguments to check
     * @param expectedLength the expected length of arguments
     * @return true if the length matches, false otherwise
     */
    public static boolean checkArgsLength(String[] args, int expectedLength) {
        return args.length == expectedLength;
    }

    /**
     * Checks if the sender has the required permission.
     *
     * @param sender     the sender to check
     * @param permission the required permission
     * @return true if the sender has the permission, false otherwise
     */
    public static boolean checkPermission(CommandSender sender, String permission) {
        if (!sender.hasPermission(permission)) {
            noPermission(sender, permission);
            return false;
        }
        return true;
    }

    /**
     * Sends a no-permission message to the sender.
     *
     * @param sender     the sender to notify
     * @param permission the permission that is missing
     */
    public static void noPermission(CommandSender sender, String permission) {
        MessageUtils.formattedMessage(sender, "You don't have the permission: " + permission);
    }

    /**
     * Parses a string to a double.
     *
     * @param doubleString the string to parse
     * @return the parsed double, or null if parsing fails
     */
    public static Double parseDouble(String doubleString) {
        try {
            return Double.parseDouble(doubleString);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Parses a string to an integer.
     *
     * @param intString the string to parse
     * @return the parsed integer, or null if parsing fails
     */
    public static Integer parseInt(String intString) {
        try {
            return Integer.parseInt(intString);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Filters a list of options based on a partial argument.
     *
     * @param partialArg the partial argument to filter by
     * @param options    the list of options to filter
     * @return a list of options that start with the partial argument
     */
    public static List<String> tabCompleteFilter(String partialArg, String... options) {
        return Arrays.stream(options)
                .filter(option -> option.startsWith(partialArg.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Provides tab completion suggestions for online player names.
     *
     * @param partialName the partial player name to filter by
     * @return a list of online player names that start with the partial name
     */
    public static List<String> tabCompleteOnlinePlayers(String partialName) {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(name -> name.toLowerCase().startsWith(partialName.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Provides tab completion suggestions for offline player names.
     *
     * @param partialName the partial player name to filter by
     * @return a list of offline player names that start with the partial name
     * @deprecated This method may have performance issues. Run performance testing before using.
     */
    @Deprecated //TODO: run performance testing.
    public static List<String> tabCompleteOfflinePlayers(String partialName) {
        return Arrays.stream(Bukkit.getOfflinePlayers())
                .map(OfflinePlayer::getName).filter(Objects::nonNull)
                .filter(name -> name.toLowerCase().startsWith(partialName.toLowerCase()))
                .collect(Collectors.toList());
    }
}
