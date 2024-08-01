package com.coderandom.core;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Utility class for fetching and generating UUIDs for players.
 */
public final class UUIDFetcher {

    private UUIDFetcher() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Fetches the online UUID of a player from the Mojang API.
     *
     * @param playerName the name of the player
     * @return the UUID of the player, or null if not found or an error occurs
     */
    public static UUID getOnlineUUID(String playerName) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            connection.addRequestProperty("User-Agent", "Mozilla/5.0");

            if (connection.getResponseCode() == 200) {
                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                String uuidString = jsonObject.get("id").getAsString();
                return UUID.fromString(uuidString.replaceFirst(
                        "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{12})",
                        "$1-$2-$3-$4-$5"
                ));
            }
        } catch (Exception e) {
            CodeRandomCore.getInstance().getLogger().log(Level.SEVERE, "Error fetching UUID for player: " + playerName, e);
        }
        return null;
    }

    /**
     * Generates a UUID for Bedrock players using Floodgate's method.
     *
     * @param playerName the name of the player
     * @return the generated UUID
     */
    public static UUID getFloodgateUUID(String playerName) {
        return UUID.nameUUIDFromBytes(("Floodgate:" + playerName).getBytes());
    }

    /**
     * Generates an offline UUID for a player based on their name.
     *
     * @param playerName the name of the player
     * @return the generated UUID
     */
    public static UUID getOfflineUUID(String playerName) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(("OfflinePlayer:" + playerName).getBytes());
            long msb = 0;
            long lsb = 0;
            for (int i = 0; i < 8; i++) {
                msb = (msb << 8) | (hash[i] & 0xff);
            }
            for (int i = 8; i < 16; i++) {
                lsb = (lsb << 8) | (hash[i] & 0xff);
            }
            msb = (msb & 0xFFFFFFFFFFFF0FFFL) | 0x0000000000003000L; // set the version to 3
            lsb = (lsb & 0x3FFFFFFFFFFFFFFFL) | 0x8000000000000000L; // set the variant to 2
            return new UUID(msb, lsb);
        } catch (NoSuchAlgorithmException e) {
            CodeRandomCore.getInstance().getLogger().log(Level.SEVERE, "Error generating offline UUID for player: " + playerName, e);
            return null;
        }
    }

    /**
     * Retrieves the UUID for the specified player name. It checks if the player is a Bedrock player first.
     * If not, it fetches the online UUID if the server is in online mode or generates an offline UUID otherwise.
     *
     * @param playerName the name of the player
     * @return the UUID of the player
     */
    public static UUID getUUID(String playerName) {
        if (playerName.startsWith(".")) {
            return BedrockUUID.getInstance().getUUID(playerName);
        }
        return Bukkit.getOnlineMode() ? getOnlineUUID(playerName) : getOfflineUUID(playerName);
    }
}
