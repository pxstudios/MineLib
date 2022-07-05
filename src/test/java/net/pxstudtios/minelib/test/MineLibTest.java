package net.pxstudtios.minelib.test;

import net.pxstudios.minelib.MineLibrary;
import net.pxstudtios.minelib.test.command.TestAbstractBukkitCommand;
import net.pxstudtios.minelib.test.command.TestAbstractContextCommand;
import net.pxstudtios.minelib.test.command.TestAbstractPlayerBukkitCommand;
import net.pxstudtios.minelib.test.item.TestBukkitItemListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class MineLibTest extends JavaPlugin {

    private final MineLibrary mineLibrary = MineLibrary.getLibrary();

    private void registerTestCommands() {
        mineLibrary.getCommandRegistry().registerCommand(new TestAbstractBukkitCommand(this), "bukkittest", "btest");
        // -> that command is not contains internal labels.

        mineLibrary.getCommandRegistry().registerCommand(new TestAbstractPlayerBukkitCommand(this));
        mineLibrary.getCommandRegistry().registerCommand(new TestAbstractContextCommand(this));
    }

    private void registerItemListener() {
        getServer().getPluginManager().registerEvents(new TestBukkitItemListener(), this);
    }

    @Override
    public void onEnable() {
        registerTestCommands();

        registerItemListener();
    }

}
