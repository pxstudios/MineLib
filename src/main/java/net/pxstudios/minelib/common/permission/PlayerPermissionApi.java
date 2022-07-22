package net.pxstudios.minelib.common.permission;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.pxstudios.minelib.MineLibrary;
import net.pxstudios.minelib.event.EventsSubscriber;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissibleBase;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class PlayerPermissionApi {

    private final Map<Player, BukkitPermissibleObjectProvider> permissibleByPlayersMap = new HashMap<>();

    @Getter
    @Setter
    private PermissionDatabaseProvider permissionDatabaseProvider;

    @Getter
    @Setter
    private boolean enabled;

    @Getter
    @Setter
    private boolean enabledOperatorsSystem;

    public PlayerPermissionApi() {
        EventsSubscriber eventsSubscriber = MineLibrary.getLibrary().getEventsSubscriber();

        eventsSubscriber.subscribe(PlayerJoinEvent.class)
                .complete(event -> {

                    Player player = event.getPlayer();

                    if (isEnabled()) {
                        injectPermissibleBase(player, createPermissibleBase(player));
                    }
                });

        eventsSubscriber.subscribe(PlayerQuitEvent.class)
                .complete(event -> permissibleByPlayersMap.remove(event.getPlayer()));
    }

    public PermissibleBase createPermissibleBase(Player player) {
        BukkitPermissibleObjectProvider permissible = new BukkitPermissibleObjectProvider(this, player);
        permissibleByPlayersMap.put(player, permissible);

        return permissible;
    }

    @SneakyThrows
    public void injectPermissibleBase(Player player, PermissibleBase permissibleBase) {
        Field field = player.getClass().getDeclaredField("perm");

        field.setAccessible(true);
        field.set(player, permissibleBase);

        field.setAccessible(false);
    }

    private BukkitPermissibleObjectProvider getCachedPermissible(Player player) {
        return permissibleByPlayersMap.get(player);
    }

    public void applyPermissionsMore(Player player, Collection<String> permissions) {
        BukkitPermissibleObjectProvider permissible = getCachedPermissible(player);

        if (permissible != null) {
            permissible.applyPermissions(permissions);
        }
    }

    public void addPermission(Player player, String permission) {
        BukkitPermissibleObjectProvider permissible = getCachedPermissible(player);

        if (permissible != null) {
            permissible.addPermission(permission);
        }
    }

    public void removePermission(Player player, String permission) {
        BukkitPermissibleObjectProvider permissible = getCachedPermissible(player);

        if (permissible != null) {
            permissible.removePermission(permission);
        }
    }

    public boolean hasPermission(Player player, String permission) {
        return player.hasPermission(permission);
    }

}
