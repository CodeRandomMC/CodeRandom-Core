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

public final class BedrockUUID {
    private static volatile BedrockUUID instance;
    private static JsonFileManager bedrockUUIDManager;
    private static ConcurrentMap<String, UUID> bedrockUUIDMap;

    private BedrockUUID() {
        bedrockUUIDManager = new JsonFileManager(CodeRandomCore.getInstance(), "DATA", "bedrockUUID");
        bedrockUUIDMap = new ConcurrentHashMap<>();
        loadUUIDData();
    }

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

    private void loadUUIDData() {
        CompletableFuture<JsonElement> future = bedrockUUIDManager.getAsync();
        future.thenAccept(jsonElement -> {
            if (jsonElement != null && jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                for (String key : jsonObject.keySet()) {
                    bedrockUUIDMap.put(key, UUID.fromString(jsonObject.get(key).getAsString()));
                }
            }
        }).join();  // Wait for the async task to complete
    }

    public void saveUUID(Player player) {
        bedrockUUIDMap.put(player.getName(), player.getUniqueId());
        saveToFile();
    }

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

    private void saveToFile() {
        JsonObject jsonObject = new JsonObject();
        for (String key : bedrockUUIDMap.keySet()) {
            jsonObject.addProperty(key, bedrockUUIDMap.get(key).toString());
        }
        bedrockUUIDManager.setAsync(jsonObject).join(); // Ensure data is saved
    }
}
