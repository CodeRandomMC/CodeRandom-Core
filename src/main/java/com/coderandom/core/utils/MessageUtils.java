package com.coderandom.core.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class MessageUtils {
    public static void messageWithTitle(CommandSender recipient, String title, String... lines) {
        recipient.sendMessage(formatMessageWithTitle(title, lines));
    }

    public static void formattedMessage(CommandSender recipient, String message) {
        recipient.sendMessage(formatMessage(message));
    }

    public static void formattedErrorMessage(CommandSender recipient, String message) {
        recipient.sendMessage(formatErrorMessage(message));
    }

    public static void messageWithBorder(CommandSender recipient, String... lines) {
        recipient.sendMessage(formatWithBorder(lines));
    }

    private static String formatWithBorder(String... lines) {
        StringBuilder message = new StringBuilder();
        int length = 12; // Default length

        // Work out border length amd add each line
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

    private static String formatMessageWithTitle(String title, String... lines) {
        // Use StringBuilder for efficient string manipulation
        StringBuilder message = new StringBuilder();

        // Format and add the title
        message.append("=====[ ").append(title).append(" ]=====").append("\n");
        int titleLength = message.length();
        int spaceCount = 0;

        // Iterate through each character in the message
        for (char c : message.toString().toCharArray()) {
            // Check if the character is a space
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

    private static String formatMessage(String message) {
        // Use StringBuilder for efficient string manipulation
        StringBuilder formattedMessage = new StringBuilder(message.length() * 2); // Pre-allocate enough space

        // Append initial green color code
        formattedMessage.append(ChatColor.GREEN);

        // Iterate through each character in the message
        for (char c : message.toCharArray()) {
            // Check if the character is a special character
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

    private static String formatErrorMessage(String message) {
        // Use StringBuilder for efficient string manipulation
        StringBuilder formattedMessage = new StringBuilder(message.length() * 2); // Pre-allocate enough space

        // Append initial green color code
        formattedMessage.append(ChatColor.RED);

        // Iterate through each character in the message
        for (char c : message.toCharArray()) {
            // Check if the character is a special character
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
