package net.pxstudios.minelib.test.permission;

import net.pxstudios.minelib.MineLibrary;
import net.pxstudios.minelib.common.config.type.YamlPluginConfig;
import net.pxstudios.minelib.permission.PermissionDatabaseProvider;
import net.pxstudios.minelib.test.MineLibTest;
import org.bukkit.entity.Player;

import java.nio.file.Path;
import java.util.Collection;

public class TestPermissionDatabaseProvider implements PermissionDatabaseProvider {

    private final YamlPluginConfig permissionsConfig;

    public TestPermissionDatabaseProvider(String resourceName, MineLibTest mineLibTest) {
        Path configPath = mineLibTest.getDataFolder().toPath().resolve(resourceName);

        permissionsConfig = MineLibrary.getLibrary().getConfigManager().createYamlConfig(configPath.toFile());
        permissionsConfig.copyResource(mineLibTest, resourceName);
    }

    private String getPath(Player player) {
        return ("players.") + player.getName();
    }

    @Override
    public Collection<String> providePermissions(Player player) {
        return permissionsConfig.getStringList(getPath(player));
    }

    @Override
    public void onPermissionAdd(Player player, String permission) {
        Collection<String> savedPermissionsList = providePermissions(player);

        savedPermissionsList.add(permission);

        permissionsConfig.set(getPath(player), savedPermissionsList);
        permissionsConfig.save();
    }

    @Override
    public void onPermissionDelete(Player player, String permission) {
        Collection<String> savedPermissionsList = providePermissions(player);

        savedPermissionsList.remove(permission);

        permissionsConfig.set(getPath(player), savedPermissionsList);
        permissionsConfig.save();
    }

}
