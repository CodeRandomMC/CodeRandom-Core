package com.coderandom.core.listener;

import com.coderandom.core.BedrockUUID;
import com.coderandom.core.CodeRandomCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Listener for handling events when a Bedrock player joins the server.
 */
public final class OnBedrockPlayerJoinListener extends BaseListener {

    /**
     * Constructs a new OnBedrockPlayerJoinListener and registers it with the plugin.
     */
    public OnBedrockPlayerJoinListener() {
        super(CodeRandomCore.getInstance());
    }

    /**
     * Event handler for PlayerJoinEvent.
     * Saves the UUID of the player if their name starts with a period.
     *
     * @param event the PlayerJoinEvent
     */
    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().getName().startsWith(".")) {
            BedrockUUID.getInstance().saveUUID(event.getPlayer());
        }
    }
}
