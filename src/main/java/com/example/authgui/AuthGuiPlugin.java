package com.example.authgui;

import com.example.authgui.listener.JoinListener;
import com.example.authgui.manager.JavaGuiManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AuthGuiPlugin extends JavaPlugin {

    private static AuthGuiPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
        getCommand("openlogin").setExecutor(this);

        getLogger().info("AuthGui 插件已启动！兼容 Java（聊天引导）和 Bedrock（Forms）");
    }

    public static AuthGuiPlugin getInstance() {
        return instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("该命令只能由玩家执行。");
            return true;
        }

        JavaGuiManager.openAuthGui(player);
        return true;
    }
}
