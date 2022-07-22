package net.pxstudios.minelib.common.permission;

import org.bukkit.entity.Player;

import java.util.Collection;

public interface PermissionDatabaseProvider {

    Collection<String> providePermissions(Player player);

    void onPermissionAdd(Player player, String permission);

    void onPermissionDelete(Player player, String permission);
}
