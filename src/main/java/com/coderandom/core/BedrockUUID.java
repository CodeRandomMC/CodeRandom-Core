package com.coderandom.core;

import com.coderandom.core.storage.JsonFileManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Singleton class to manage Bedrock player UUIDs.
 * Loads and saves UUIDs from/to a JSON file.
 */
public final class BedrockUUID {

    private static volatile BedrockUUID instance;
    private static JsonFileManager bedrockUUIDManager;
    private static ConcurrentMap<String, UUID> bedrockUUIDMap;

    private BedrockUUID() {
        bedrockUUIDManager = new JsonFileManager(CodeRandomCore.getInstance(), "DATA", "bedrockUUID");
        bedrockUUIDMap = new ConcurrentHashMap<>();
        loadUUIDData();
    }

    /**
     * Returns the singleton instance of BedrockUUID.
     *
     * @return the BedrockUUID instance
     */
    public static BedrockUUID getInstance() {
        if (instance == null) {
            synchronized (BedrockUUID.class) {
                if (instance == null) {
                    instance = new BedrockUUID();
                }
            }
        }
        return instance;
    }

    /**
     * Loads UUID data from the JSON file asynchronously.
     */
    private void loadUUIDData() {
        CompletableFuture<JsonElement> future = bedrockUUIDManager.getAsync();
        future.thenAccept(jsonElement -> {
            if (jsonElement != null && jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                jsonObject.keySet().forEach(key -> bedrockUUIDMap.put(key, UUID.fromString(jsonObject.get(key).getAsString())));
            }
        }).join();  // Wait for the async task to complete
    }

    /**
     * Saves the UUID of a player to the map and updates the JSON file.
     *
     * @param player the player whose UUID is to be saved
     */
    public void saveUUID(Player player) {
        bedrockUUIDMap.put(player.getName(), player.getUniqueId());
        saveToFile();
    }

    /**
     * Retrieves the UUID for the specified player name. If the UUID is not in the map,
     * it attempts to find the player on the server, add their UUID to the map, and save it.
     *
     * @param playerName the name of the player
     * @return the UUID of the player, or null if not found
     */
    public UUID getUUID(String playerName) {
        UUID uuid = bedrockUUIDMap.get(playerName);
        if (uuid == null) {
            Player player = Bukkit.getServer().getPlayer(playerName);
            if (player != null) {
                uuid = player.getUniqueId();
                bedrockUUIDMap.put(playerName, uuid);
                saveToFile();
            }
        }
        return uuid;
    }

    /**
     * Saves the current UUID map to the JSON file asynchronously.
     */
    private void saveToFile() {
        JsonObject jsonObject = new JsonObject();
        bedrockUUIDMap.forEach((key, value) -> jsonObject.addProperty(key, value.toString()));
        bedrockUUIDManager.setAsync(jsonObject).join(); // Ensure data is saved
    }
}
