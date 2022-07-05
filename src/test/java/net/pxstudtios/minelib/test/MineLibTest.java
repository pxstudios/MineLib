package net.pxstudtios.minelib.test;

import net.pxstudios.minelib.MineLibrary;
import net.pxstudios.minelib.asynccatcher.AsyncCatcherBypass;
import net.pxstudtios.minelib.test.command.TestAbstractBukkitCommand;
import net.pxstudtios.minelib.test.command.TestAbstractContextCommand;
import net.pxstudtios.minelib.test.command.TestAbstractPlayerBukkitCommand;
import net.pxstudtios.minelib.test.item.TestBukkitItemListener;
import org.bukkit.entity.Entity;
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

    private void testAsyncCatcherBypass() {
        AsyncCatcherBypass asyncCatcherBypass = mineLibrary.getAsyncCatcherBypass();
        asyncCatcherBypass.enableSpigotBypass();

        getServer().getScheduler().runTaskAsynchronously(this, () -> {

            for (Entity entity : getServer().getWorld("world").getEntities()) {
                asyncCatcherBypass.sync(entity::remove);
            }
        });
    }

    @Override
    public void onEnable() {
        testAsyncCatcherBypass();

        registerTestCommands();

        registerItemListener();
    }

}
