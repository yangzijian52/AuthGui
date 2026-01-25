package com.example.authgui.manager;

import com.example.authgui.AuthGuiPlugin;
import fr.xephi.authme.api.v3.AuthMeApi;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class JavaGuiManager {

    // 防止从步骤1跳到步骤2时，被判定为“关闭菜单”而强制弹回
    private static final Set<UUID> switchingPlayers = new HashSet<>();

    public static void openAuthGui(Player player) {
        if (!player.isOnline()) return;

        AuthMeApi authMe = AuthMeApi.getInstance();
        if (authMe.isRegistered(player.getName())) {
            openLoginGui(player);
        } else {
            openRegisterGuiStep1(player);
        }
    }

    // --- 通用关闭处理 ---
    private static void handleClose(Player player) {
        // 如果玩家在这个名单里，说明他是为了去下一步才关的，不仅不管，还要把他移出名单
        if (switchingPlayers.contains(player.getUniqueId())) {
            switchingPlayers.remove(player.getUniqueId());
            return;
        }

        // 正常防逃逸逻辑
        Bukkit.getScheduler().runTaskLater(AuthGuiPlugin.getInstance(), () -> {
            if (player.isOnline() && !AuthMeApi.getInstance().isAuthenticated(player)) {
                openAuthGui(player);
            }
        }, 10L);
    }

    // --- 登录界面 ---
    private static void openLoginGui(Player player) {
        new AnvilGUI.Builder()
                .plugin(AuthGuiPlugin.getInstance())
                .title("登录 - 输入密码")
                .text(" ") // 空格占位，玩家不需要删，直接输即可
                .itemLeft(createGuiItem(Material.PAPER, " "))
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }

                    String password = stateSnapshot.getText().trim();
                    if (password.isEmpty()) {
                        return Collections.singletonList(AnvilGUI.ResponseAction.replaceInputText("密码不能为空"));
                    }

                    // 登录不需要标记切换，因为登录成功窗口关闭是正常的
                    player.chat("/login " + password);
                    return Collections.singletonList(AnvilGUI.ResponseAction.close());
                })
                .onClose(stateSnapshot -> handleClose(stateSnapshot.getPlayer()))
                .open(player);
    }

    // --- 注册第一步 ---
    private static void openRegisterGuiStep1(Player player) {
        new AnvilGUI.Builder()
                .plugin(AuthGuiPlugin.getInstance())
                .title("注册(1/2) - 输入新密码")
                .text(" ")
                .itemLeft(createGuiItem(Material.NAME_TAG, "步骤1"))
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }

                    String pass1 = stateSnapshot.getText().trim();
                    if (pass1.isEmpty()) {
                        return Collections.singletonList(AnvilGUI.ResponseAction.replaceInputText("不能空白"));
                    }

                    // 【关键】标记该玩家正在切换界面，防止触发 onClose 的拦截
                    switchingPlayers.add(player.getUniqueId());

                    // 切换到第二步
                    AuthGuiPlugin.getInstance().getServer().getScheduler().runTask(AuthGuiPlugin.getInstance(), () -> {
                        openRegisterGuiStep2(player, pass1);
                    });

                    return Collections.singletonList(AnvilGUI.ResponseAction.close());
                })
                .onClose(stateSnapshot -> handleClose(stateSnapshot.getPlayer()))
                .open(player);
    }

    // --- 注册第二步 ---
    private static void openRegisterGuiStep2(Player player, String firstPass) {
        new AnvilGUI.Builder()
                .plugin(AuthGuiPlugin.getInstance())
                .title("注册(2/2) - 确认密码")
                .text(" ")
                .itemLeft(createGuiItem(Material.NAME_TAG, "步骤2"))
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }

                    String pass2 = stateSnapshot.getText().trim();

                    if (pass2.equals(firstPass)) {
                        player.chat("/reg " + firstPass + " " + pass2);

                        // 注册成功后，延迟检测登录状态
                        // 这里我们不标记 switching，因为窗口关闭后，如果没登录，正是我们需要 handleClose 介入的时候
                        // 它会自动检测：如果 AuthMe 自动登录了 -> 没事；如果没有 -> 自动弹登录窗
                        return Collections.singletonList(AnvilGUI.ResponseAction.close());
                    } else {
                        player.sendMessage("§c两次密码不一致，请重试。");

                        // 输错重来，也算是一种“切换”，防止被 handleClose 拦截导致逻辑混乱
                        switchingPlayers.add(player.getUniqueId());

                        AuthGuiPlugin.getInstance().getServer().getScheduler().runTask(AuthGuiPlugin.getInstance(), () -> {
                            openRegisterGuiStep1(player);
                        });
                        return Collections.singletonList(AnvilGUI.ResponseAction.close());
                    }
                })
                .onClose(stateSnapshot -> handleClose(stateSnapshot.getPlayer()))
                .open(player);
    }

    private static ItemStack createGuiItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§r" + name);
            item.setItemMeta(meta);
        }
        return item;
    }
}
