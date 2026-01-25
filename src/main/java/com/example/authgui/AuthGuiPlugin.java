package com.example.authgui;

import com.example.authgui.listener.JoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class AuthGuiPlugin extends JavaPlugin {

    private static AuthGuiPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        // 注册监听器
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);

        getLogger().info("AuthGui 插件已启动！兼容 Java (AnvilGUI) 和 Bedrock (Forms)");
    }

    public static AuthGuiPlugin getInstance() {
        return instance;
    }
}

