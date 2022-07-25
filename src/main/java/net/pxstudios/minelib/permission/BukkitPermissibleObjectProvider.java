package net.pxstudios.minelib.permission;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class BukkitPermissibleObjectProvider extends PermissibleBase {

    private final PlayerPermissionApi permissionApi;
    private final Player player;

    private final Set<String> permissionsSet = new HashSet<>();

    BukkitPermissibleObjectProvider(PlayerPermissionApi permissionApi, Player player) {
        super(player);

        this.permissionApi = permissionApi;
        this.player = player;

        if (permissionApi.getPermissionDatabaseProvider() != null) {
            applyPermissions(permissionApi.getPermissionDatabaseProvider().providePermissions(player));
        }
    }

    public Set<String> getPermissions() {
        return Collections.unmodifiableSet(permissionsSet);
    }

    public void addPermission(String permission) {
        if (permissionsSet.add(permission) && permissionApi.getPermissionDatabaseProvider() != null) {
            permissionApi.getPermissionDatabaseProvider().onPermissionAdd(player, permission);
        }
    }

    public void removePermission(String permission) {
        if (permissionsSet.remove(permission) && permissionApi.getPermissionDatabaseProvider() != null) {
            permissionApi.getPermissionDatabaseProvider().onPermissionDelete(player, permission);
        }
    }

    public void clearPermissions() {
        permissionsSet.clear();
    }

    public void applyPermissions(Collection<String> permissions) {
        if (permissions != null) {
            permissionsSet.addAll(permissions);
        }
    }

    public boolean hasOwnPermission() {
        return permissionsSet.contains("*");
    }

    private boolean calculateAndApplyPermission(String permission) {
        String[] parts = permission.split("\\.");

        StringBuilder partsBuilder = new StringBuilder();

        for (int in = 0; in < parts.length; in++) {
            partsBuilder.append(parts[0]).append(".");

            if (permissionsSet.contains(partsBuilder + "*")) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isOp() {
        return permissionApi.isEnabledOperatorsSystem() && super.isOp();
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return hasPermission(perm.getName());
    }

    @Override
    public boolean hasPermission(String permission) {
        if (hasOwnPermission() || permissionsSet.contains(permission)) {
            return true;
        }

        return calculateAndApplyPermission(permission);
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return hasPermission(perm);
    }

    @Override
    public boolean isPermissionSet(String name) {
        return hasPermission(name);
    }

}
