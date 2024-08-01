package com.coderandom.core.listener;

import com.coderandom.core.BedrockUUID;
import com.coderandom.core.CodeRandomCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public final class OnBedrockPlayerJoinListener extends BaseListener{
    public OnBedrockPlayerJoinListener() {
        super(CodeRandomCore.getInstance());
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent e) {
        if (e.getPlayer().getName().startsWith(".")) {
            BedrockUUID.getInstance().saveUUID(e.getPlayer());
        }
    }
}
