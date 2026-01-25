package com.example.authgui.listener;

import com.example.authgui.AuthGuiPlugin;
import com.example.authgui.manager.BedrockManager;
import com.example.authgui.manager.JavaGuiManager;
import fr.xephi.authme.api.v3.AuthMeApi;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.geysermc.floodgate.api.FloodgateApi;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        AuthMeApi authMe = AuthMeApi.getInstance();

        // 延迟 10 ticks 执行，确保 AuthMe 数据已加载且玩家完全连接
        Bukkit.getScheduler().runTaskLater(AuthGuiPlugin.getInstance(), () -> {
            if (authMe.isAuthenticated(player)) {
                return; // 已经登录，无需操作
            }

            // 判断是否是基岩版玩家 (需要 Floodgate)
            boolean isBedrock = false;
            if (Bukkit.getPluginManager().isPluginEnabled("floodgate")) {
                isBedrock = FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId());
            }

            if (isBedrock) {
                // 处理基岩版逻辑
                BedrockManager.openAuthForm(player);
            } else {
                // 处理 Java 版逻辑
                JavaGuiManager.openAuthGui(player);
            }
        }, 10L);
    }
}
