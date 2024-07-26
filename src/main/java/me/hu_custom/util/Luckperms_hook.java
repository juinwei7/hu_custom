package me.hu_custom.util;

import lombok.Builder;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.context.DefaultContextKeys;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.N;

import java.time.Duration;

public class Luckperms_hook {
    public static void addperr(Player player, String permission) {
        net.luckperms.api.LuckPerms luckPerms = LuckPermsProvider.get();

        // 获取用户对象
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());

        // 添加权限节点
        user.data().add(Node.builder(permission).build());

        // 保存用户数据
        luckPerms.getUserManager().saveUser(user);
    }
    public static void addperr(Player player, String permission,long time_hr) {
        net.luckperms.api.LuckPerms luckPerms = LuckPermsProvider.get();

        // 获取用户对象
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());

        // 添加权限节点
        user.data().add(
                Node.builder(permission)
                        .value(true)
                        .expiry(Duration.ofHours(time_hr))
                        .withContext(DefaultContextKeys.SERVER_KEY,"ads01")
                        .withContext(DefaultContextKeys.SERVER_KEY,"ads02")
                        .withContext(DefaultContextKeys.SERVER_KEY,"ads03")
                        .withContext(DefaultContextKeys.SERVER_KEY,"server5")
                        .build()
        );

        // 保存用户数据
        luckPerms.getUserManager().saveUser(user);
    }

    public static void removeperr(Player player, String permission) {
        net.luckperms.api.LuckPerms luckPerms = LuckPermsProvider.get();

        // 获取用户对象
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());

        // 添加权限节点
        user.data().remove(Node.builder(permission).build());

        // 保存用户数据
        luckPerms.getUserManager().saveUser(user);
    }
}
