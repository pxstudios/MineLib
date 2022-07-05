package net.pxstudios.minelib;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import net.pxstudios.minelib.asynccatcher.AsyncCatcherBypass;
import net.pxstudios.minelib.command.CommandRegistry;
import org.bukkit.plugin.Plugin;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MineLibrary {

    private static final MineLibrary instance = new MineLibrary();

    public static MineLibrary getLibrary() {
        return instance;
    }

    @Getter
    private CommandRegistry commandRegistry;

    @Getter
    private AsyncCatcherBypass asyncCatcherBypass;

    void init(@NonNull Plugin plugin) {
        commandRegistry = new CommandRegistry(plugin);
        asyncCatcherBypass = new AsyncCatcherBypass(plugin);
        // ...
    }

}
