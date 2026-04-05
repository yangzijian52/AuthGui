package com.example.authgui.manager;

import com.example.authgui.AuthGuiPlugin;
import fr.xephi.authme.api.v3.AuthMeApi;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class JavaGuiManager {

    // TODO: This is a temporary fallback for Paper 26.1.x.
    // Restore a proper Java GUI flow after finding a stable GUI-compatible solution.

    private enum Step {
        LOGIN,
        REGISTER_PASSWORD,
        REGISTER_CONFIRM
    }

    private record Session(Step step, String firstPassword) {}

    private static final Map<UUID, Session> SESSIONS = new ConcurrentHashMap<>();

    public static void openAuthGui(Player player) {
        if (!player.isOnline()) {
            return;
        }

        AuthMeApi authMe = AuthMeApi.getInstance();
        if (authMe.isAuthenticated(player)) {
            clearSession(player.getUniqueId());
            return;
        }

        if (authMe.isRegistered(player.getName())) {
            startLogin(player);
        } else {
            startRegister(player);
        }
    }

    public static boolean handleChat(Player player, String message) {
        Session session = SESSIONS.get(player.getUniqueId());
        if (session == null) {
            return false;
        }

        String input = message == null ? "" : message.trim();
        Bukkit.getScheduler().runTask(AuthGuiPlugin.getInstance(), () -> processInput(player, session, input));
        return true;
    }

    public static void clearSession(UUID uuid) {
        SESSIONS.remove(uuid);
    }

    private static void processInput(Player player, Session session, String input) {
        if (!player.isOnline()) {
            clearSession(player.getUniqueId());
            return;
        }

        if (AuthMeApi.getInstance().isAuthenticated(player)) {
            clearSession(player.getUniqueId());
            return;
        }

        switch (session.step()) {
            case LOGIN -> handleLoginInput(player, input);
            case REGISTER_PASSWORD -> handleRegisterPassword(player, input);
            case REGISTER_CONFIRM -> handleRegisterConfirm(player, session.firstPassword(), input);
        }
    }

    private static void startLogin(Player player) {
        SESSIONS.put(player.getUniqueId(), new Session(Step.LOGIN, null));
        player.sendMessage(ChatColor.GOLD + "[AuthGui] " + ChatColor.YELLOW + "请直接在聊天栏输入你的登录密码。");
        player.sendMessage(ChatColor.GRAY + "输入内容不会公开显示。");
    }

    private static void startRegister(Player player) {
        SESSIONS.put(player.getUniqueId(), new Session(Step.REGISTER_PASSWORD, null));
        player.sendMessage(ChatColor.GOLD + "[AuthGui] " + ChatColor.YELLOW + "请直接在聊天栏输入你要注册的密码。");
        player.sendMessage(ChatColor.GRAY + "输入内容不会公开显示。");
    }

    private static void handleLoginInput(Player player, String input) {
        if (input.isEmpty()) {
            player.sendMessage(ChatColor.RED + "密码不能为空，请重新输入。");
            startLogin(player);
            return;
        }

        player.chat("/login " + input);
        Bukkit.getScheduler().runTaskLater(AuthGuiPlugin.getInstance(), () -> {
            if (player.isOnline() && !AuthMeApi.getInstance().isAuthenticated(player)) {
                player.sendMessage(ChatColor.RED + "登录失败，请重新输入密码。");
                startLogin(player);
            } else {
                clearSession(player.getUniqueId());
            }
        }, 20L);
    }

    private static void handleRegisterPassword(Player player, String input) {
        if (input.isEmpty()) {
            player.sendMessage(ChatColor.RED + "密码不能为空，请重新输入。");
            startRegister(player);
            return;
        }

        SESSIONS.put(player.getUniqueId(), new Session(Step.REGISTER_CONFIRM, input));
        player.sendMessage(ChatColor.GOLD + "[AuthGui] " + ChatColor.YELLOW + "请再次输入密码以确认注册。");
    }

    private static void handleRegisterConfirm(Player player, String firstPassword, String input) {
        if (input.isEmpty()) {
            player.sendMessage(ChatColor.RED + "确认密码不能为空，请重新注册。");
            startRegister(player);
            return;
        }

        if (!input.equals(firstPassword)) {
            player.sendMessage(ChatColor.RED + "两次密码不一致，请重新注册。");
            startRegister(player);
            return;
        }

        player.chat("/reg " + firstPassword + " " + input);
        Bukkit.getScheduler().runTaskLater(AuthGuiPlugin.getInstance(), () -> {
            if (!player.isOnline()) {
                clearSession(player.getUniqueId());
                return;
            }

            if (!AuthMeApi.getInstance().isAuthenticated(player)) {
                player.sendMessage(ChatColor.GREEN + "注册成功，请继续输入密码完成登录。");
                startLogin(player);
            } else {
                clearSession(player.getUniqueId());
            }
        }, 20L);
    }
}
