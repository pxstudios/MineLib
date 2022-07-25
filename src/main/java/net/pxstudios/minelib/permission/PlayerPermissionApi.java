package net.pxstudios.minelib.permission;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.pxstudios.minelib.subscription.EventsSubscriber;
import net.pxstudios.minelib.plugin.MinecraftPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    public PlayerPermissionApi(MinecraftPlugin plugin) {
        EventsSubscriber eventsSubscriber = plugin.getMineLibrary().getEventsSubscriber();

        eventsSubscriber.subscribe(PlayerJoinEvent.class, EventPriority.HIGHEST)
                .complete(event -> {

                    Player player = event.getPlayer();

                    if (isEnabled()) {
                        injectPermissibleBase(player, createPermissibleBase(player));
                    }
                });

        eventsSubscriber.subscribe(PlayerQuitEvent.class, EventPriority.HIGHEST)
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
        return enabled ? permissibleByPlayersMap.get(player) : null;
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

    public Set<String> getPermissions(Player player) {
        BukkitPermissibleObjectProvider permissible = getCachedPermissible(player);

        if (permissible != null) {
            return permissible.getPermissions();
        }

        return player.getEffectivePermissions().stream().map(PermissionAttachmentInfo::getPermission).collect(Collectors.toSet());
    }

    public boolean hasPermission(Player player, String permission) {
        return player.hasPermission(permission);
    }

}
