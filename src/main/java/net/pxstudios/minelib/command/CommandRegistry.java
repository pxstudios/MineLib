package net.pxstudios.minelib.command;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.pxstudios.minelib.command.type.AbstractContextCommand;
import net.pxstudios.minelib.plugin.MinecraftPlugin;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public final class CommandRegistry {

    private final MinecraftPlugin plugin;
    private CommandMap cachedCommandMap;

    @SneakyThrows
    private CommandMap injectCommandMap(@NonNull Server server) {
        if (cachedCommandMap == null) {
            cachedCommandMap = ((CommandMap) server.getClass().getDeclaredField("commandMap").get(server));
        }

        return cachedCommandMap;
    }

    private void registerCommandWrapper(@NonNull BukkitCommandExecuteWrapper commandExecuteWrapper) {
        CommandMap commandMap = injectCommandMap(plugin.getServer());
        String fallbackPrefix = plugin.getName();

        commandMap.register(commandExecuteWrapper.getName(), fallbackPrefix, commandExecuteWrapper);
    }

    public void registerCommand(AbstractContextCommand abstractContextCommand, String name, String... aliases) {
        registerCommandWrapper(new BukkitCommandExecuteWrapper(abstractContextCommand, name, aliases));
    }

    public void registerCommand(AbstractContextCommand abstractContextCommand) {
        Set<String> activeLabels = new HashSet<>(abstractContextCommand.getActiveLabels());

        if (activeLabels.isEmpty()) {
            throw new IllegalArgumentException("No command labels found");
        }

        String name = activeLabels.iterator().next();
        activeLabels.remove(name);

        registerCommand(abstractContextCommand, name, activeLabels.toArray(new String[0]));
    }
}
