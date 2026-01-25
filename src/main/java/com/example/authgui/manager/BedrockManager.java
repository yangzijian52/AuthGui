package com.example.authgui.manager;

import com.example.authgui.AuthGuiPlugin;
import fr.xephi.authme.api.v3.AuthMeApi;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BedrockManager {

    // 同样引入一个标记，防止逻辑冲突
    private static final Set<UUID> processingPlayers = new HashSet<>();

    public static void openAuthForm(Player player) {
        if (!player.isOnline()) return;

        AuthMeApi authMe = AuthMeApi.getInstance();
        boolean isRegistered = authMe.isRegistered(player.getName());

        if (isRegistered) {
            sendLoginForm(player);
        } else {
            sendRegisterForm(player);
        }
    }

    private static void handleClose(Player player) {
        // 如果正在处理逻辑（比如正在重开表单提示错误），就别管
        if (processingPlayers.contains(player.getUniqueId())) {
            processingPlayers.remove(player.getUniqueId());
            return;
        }

        Bukkit.getScheduler().runTaskLater(AuthGuiPlugin.getInstance(), () -> {
            if (player.isOnline() && !AuthMeApi.getInstance().isAuthenticated(player)) {
                openAuthForm(player);
            }
        }, 10L);
    }

    private static void sendLoginForm(Player player) {
        CustomForm form = CustomForm.builder()
                .title("服务器登录")
                .input("请输入密码", "密码")
                .validResultHandler(response -> {
                    String password = response.asInput(0);

                    // 标记正在处理，防止 CloseHandler 干扰
                    processingPlayers.add(player.getUniqueId());

                    if (password != null && !password.isEmpty()) {
                        Bukkit.getScheduler().runTask(AuthGuiPlugin.getInstance(), () -> {
                            player.chat("/login " + password);
                            // 这里移除标记，让防逃逸逻辑接管后续（如果密码错，它会负责弹回来）
                            processingPlayers.remove(player.getUniqueId());
                            handleClose(player);
                        });
                    } else {
                        // 密码为空，重开
                        Bukkit.getScheduler().runTask(AuthGuiPlugin.getInstance(), () -> sendLoginForm(player));
                    }
                })
                .closedOrInvalidResultHandler(response -> handleClose(player))
                .build();

        FloodgateApi.getInstance().sendForm(player.getUniqueId(), form);
    }

    private static void sendRegisterForm(Player player) {
        CustomForm form = CustomForm.builder()
                .title("服务器注册")
                .input("步骤1: 设置密码", "请输入密码")
                .input("步骤2: 确认密码", "请再次输入密码")
                .validResultHandler(response -> {
                    String pass1 = response.asInput(0);
                    String pass2 = response.asInput(1);

                    // 标记正在处理
                    processingPlayers.add(player.getUniqueId());

                    Bukkit.getScheduler().runTask(AuthGuiPlugin.getInstance(), () -> {
                        if (pass1 == null || pass2 == null || pass1.isEmpty() || pass2.isEmpty()) {
                            player.sendMessage("§c密码不能为空！");
                            sendRegisterForm(player); // 主动重开
                            return;
                        }

                        if (!pass1.equals(pass2)) {
                            player.sendMessage("§c两次输入的密码不一致，请重试！");
                            sendRegisterForm(player); // 主动重开
                            return;
                        }

                        // 校验通过，执行注册
                        player.chat("/reg " + pass1 + " " + pass2);

                        // 移除标记，因为注册成功后的登录检测属于新流程
                        processingPlayers.remove(player.getUniqueId());

                        // 注册后自动检测流程
                        Bukkit.getScheduler().runTaskLater(AuthGuiPlugin.getInstance(), () -> {
                            if (player.isOnline() && !AuthMeApi.getInstance().isAuthenticated(player)) {
                                player.sendMessage("§a[系统] §f注册成功！请登录。");
                                sendLoginForm(player);
                            }
                        }, 20L);
                    });
                })
                .closedOrInvalidResultHandler(response -> handleClose(player))
                .build();

        FloodgateApi.getInstance().sendForm(player.getUniqueId(), form);
    }
}
