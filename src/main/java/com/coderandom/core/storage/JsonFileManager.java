package com.coderandom.core.storage;

import com.google.gson.*;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

/**
 * Manages JSON file operations asynchronously and synchronously for a Bukkit plugin.
 */
public final class JsonFileManager {

    private final Logger LOGGER;
    private final File file;
    private final Gson gson;

    /**
     * Constructs a JsonFileManager for handling JSON file operations.
     *
     * @param plugin   the plugin instance
     * @param path     the directory path relative to the plugin's data folder
     * @param fileName the name of the JSON file
     */
    public JsonFileManager(Plugin plugin, String path, String fileName) {
        File directory;
        if (path == null || path.isEmpty()) {
            directory = plugin.getDataFolder();
        } else {
            directory = new File(plugin.getDataFolder(), path);
        }

        this.LOGGER = plugin.getLogger();
        this.file = new File(directory, fileName + ".json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();

        // Check if the file exists in the plugin data folder; if not, try to create it from JAR resources
        if (!file.exists()) {
            try {
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                if (copyFileFromJar(path, fileName)) {
                    LOGGER.info("Copied default file from JAR: " + fileName + ".json");
                } else {
                    file.createNewFile();
                    LOGGER.info("Created new file: " + fileName + ".json");
                }
            } catch (IOException e) {
                LOGGER.severe("Problem creating or copying file: " + file.getName());
            }
        }
    }

    /**
     * Copies a file from the JAR resources to the plugin data folder.
     *
     * @param path     the directory path relative to the JAR root
     * @param fileName the name of the file
     * @return true if the file was successfully copied, false otherwise
     */
    private boolean copyFileFromJar(String path, String fileName) {
        String resourcePath = path == null || path.isEmpty() ? "/" + fileName + ".json" : "/" + path + "/" + fileName + ".json";
        InputStream resourceStream = getClass().getResourceAsStream(resourcePath);
        if (resourceStream == null) {
            return false;
        }

        try {
            Files.copy(resourceStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            LOGGER.severe("Error copying file from JAR: " + e.getMessage());
            return false;
        } finally {
            try {
                resourceStream.close();
            } catch (IOException e) {
                LOGGER.severe("Error closing resource stream: " + e.getMessage());
            }
        }
    }

    /**
     * Asynchronously reads the JSON content from the file.
     *
     * @return a CompletableFuture containing the JSON content
     */
    public CompletableFuture<JsonElement> getAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try (Reader reader = new FileReader(file, StandardCharsets.UTF_8)) {
                return JsonParser.parseReader(reader);
            } catch (FileNotFoundException e) {
                LOGGER.severe("File not found: " + e.getMessage());
                return null;
            } catch (IOException | JsonSyntaxException e) {
                LOGGER.severe("Error reading JSON from file: " + e.getMessage());
                return null;
            }
        });
    }

    /**
     * Asynchronously writes the JSON content to the file.
     *
     * @param jsonElement the JSON content to write
     * @return a CompletableFuture representing the completion of the write operation
     */
    public CompletableFuture<Void> setAsync(JsonElement jsonElement) {
        return CompletableFuture.runAsync(() -> {
            try (Writer writer = new FileWriter(file, StandardCharsets.UTF_8, false)) {
                gson.toJson(jsonElement, writer);
            } catch (IOException e) {
                LOGGER.severe("Error writing JSON to file: " + e.getMessage());
            }
        });
    }

    /**
     * Synchronously reads the JSON content from the file.
     *
     * @return the JSON content
     */
    public JsonElement getSync() {
        try (Reader reader = new FileReader(file, StandardCharsets.UTF_8)) {
            return JsonParser.parseReader(reader);
        } catch (FileNotFoundException e) {
            LOGGER.severe("File not found: " + e.getMessage());
            return null;
        } catch (IOException | JsonSyntaxException e) {
            LOGGER.severe("Error reading JSON from file: " + e.getMessage());
            return null;
        }
    }

    /**
     * Deletes the JSON file.
     */
    public void deleteFile() {
        if (file.exists()) {
            if (file.delete()) {
                LOGGER.info("File deleted successfully: " + file.getName());
            } else {
                LOGGER.severe("Failed to delete the file: " + file.getName());
            }
        }
    }
}
