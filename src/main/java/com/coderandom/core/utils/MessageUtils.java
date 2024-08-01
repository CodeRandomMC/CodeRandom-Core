package com.coderandom.core.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Utility class for sending formatted messages to command senders.
 */
public final class MessageUtils {

    private MessageUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Sends a message with a title to the recipient.
     *
     * @param recipient the recipient of the message
     * @param title     the title of the message
     * @param lines     the lines of the message
     */
    public static void messageWithTitle(CommandSender recipient, String title, String... lines) {
        recipient.sendMessage(formatMessageWithTitle(title, lines));
    }

    /**
     * Sends a formatted message to the recipient.
     *
     * @param recipient the recipient of the message
     * @param message   the message to send
     */
    public static void formattedMessage(CommandSender recipient, String message) {
        recipient.sendMessage(formatMessage(message));
    }

    /**
     * Sends a formatted error message to the recipient.
     *
     * @param recipient the recipient of the message
     * @param message   the error message to send
     */
    public static void formattedErrorMessage(CommandSender recipient, String message) {
        recipient.sendMessage(formatErrorMessage(message));
    }

    /**
     * Sends a message with a border to the recipient.
     *
     * @param recipient the recipient of the message
     * @param lines     the lines of the message
     */
    public static void messageWithBorder(CommandSender recipient, String... lines) {
        recipient.sendMessage(formatWithBorder(lines));
    }

    /**
     * Formats a message with a border.
     *
     * @param lines the lines of the message
     * @return the formatted message
     */
    private static String formatWithBorder(String... lines) {
        StringBuilder message = new StringBuilder();
        int length = 12; // Default length

        // Determine the border length and add each line
        for (String line : lines) {
            if (line.length() > length) {
                length = Math.min(60, line.length()); // Maximum length of 60
            }
            message.append(line).append("\n");
        }

        // Create the border
        String border = "=".repeat(length);

        // Add the top border
        message.insert(0, border + '\n');

        // Add the bottom border
        message.append(border);

        // Format the entire message
        return formatMessage(message.toString());
    }

    /**
     * Formats a message with a title.
     *
     * @param title the title of the message
     * @param lines the lines of the message
     * @return the formatted message
     */
    private static String formatMessageWithTitle(String title, String... lines) {
        StringBuilder message = new StringBuilder();

        // Format and add the title
        message.append("========[ ").append(title).append(" ]========").append("\n");
        int titleLength = message.length();
        int spaceCount = 0;

        // Count spaces in the title
        for (char c : message.toString().toCharArray()) {
            if (Character.isWhitespace(c) || c == "'".toCharArray()[0]) {
                spaceCount++;
            }
        }

        // Format and add each line
        for (String line : lines) {
            if (line.length() < titleLength) {
                int startSpace = (titleLength - line.length()) / 2;
                message.append(" ".repeat(startSpace + 1));
            }
            message.append(line).append("\n");
        }

        // Add the closing line
        message.append("=".repeat(Math.max(0, titleLength - spaceCount)));

        return formatMessage(message.toString());
    }

    /**
     * Formats a message with color codes.
     *
     * @param message the message to format
     * @return the formatted message
     */
    private static String formatMessage(String message) {
        StringBuilder formattedMessage = new StringBuilder(message.length() * 2); // Pre-allocate enough space

        // Append initial green color code
        formattedMessage.append(ChatColor.GREEN);

        // Iterate through each character in the message
        for (char c : message.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c)) {
                // Append the dark purple color code and the special character
                formattedMessage.append(ChatColor.DARK_PURPLE).append(c).append(ChatColor.GREEN);
            } else {
                // Append the regular character
                formattedMessage.append(c);
            }
        }

        return formattedMessage.toString();
    }

    /**
     * Formats an error message with color codes.
     *
     * @param message the message to format
     * @return the formatted error message
     */
    private static String formatErrorMessage(String message) {
        StringBuilder formattedMessage = new StringBuilder(message.length() * 2); // Pre-allocate enough space

        // Append initial red color code
        formattedMessage.append(ChatColor.RED);

        // Iterate through each character in the message
        for (char c : message.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c)) {
                // Append the dark purple color code and the special character
                formattedMessage.append(ChatColor.DARK_PURPLE).append(c).append(ChatColor.RED);
            } else {
                // Append the regular character
                formattedMessage.append(c);
            }
        }

        return formattedMessage.toString();
    }
}
