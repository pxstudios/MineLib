package net.pxstudios.minelib.registry.adapter.type;

import net.pxstudios.minelib.MineLibrary;
import net.pxstudios.minelib.command.type.AbstractContextCommand;
import net.pxstudios.minelib.registry.BukkitRegistryObject;
import net.pxstudios.minelib.registry.adapter.BukkitRegistryObjectAdapter;
import org.bukkit.plugin.Plugin;

public class CommandRegistryObjectAdapter extends BukkitRegistryObjectAdapter<AbstractContextCommand> {

    @Override
    public void fireRegister(Plugin plugin, AbstractContextCommand obj) {
        MineLibrary.getLibrary().getCommandRegistry().registerCommand(obj);
    }

    @Override
    public AbstractContextCommand newObjectInstance(BukkitRegistryObject<AbstractContextCommand> bukkitRegistryObject) {
        return null;
    }

}
