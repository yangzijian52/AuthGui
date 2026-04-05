package com.example.authgui.listener;

import com.example.authgui.AuthGuiPlugin;
import com.example.authgui.manager.BedrockManager;
import com.example.authgui.manager.JavaGuiManager;
import fr.xephi.authme.api.v3.AuthMeApi;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.geysermc.floodgate.api.FloodgateApi;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        AuthMeApi authMe = AuthMeApi.getInstance();

        Bukkit.getScheduler().runTaskLater(AuthGuiPlugin.getInstance(), () -> {
            if (authMe.isAuthenticated(player)) {
                return;
            }

            boolean isBedrock = false;
            if (Bukkit.getPluginManager().isPluginEnabled("floodgate")) {
                isBedrock = FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId());
            }

            if (isBedrock) {
                BedrockManager.openAuthForm(player);
            } else {
                JavaGuiManager.openAuthGui(player);
            }
        }, 10L);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (JavaGuiManager.handleChat(event.getPlayer(), event.getMessage())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        JavaGuiManager.clearSession(event.getPlayer().getUniqueId());
    }
}
