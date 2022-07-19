package net.pxstudios.minelib.registry.adapter.type;

import lombok.SneakyThrows;
import net.pxstudios.minelib.MineLibrary;
import net.pxstudios.minelib.command.CommandContext;
import net.pxstudios.minelib.command.CommandSettings;
import net.pxstudios.minelib.command.type.AbstractContextCommand;
import net.pxstudios.minelib.registry.BukkitRegistryObject;
import net.pxstudios.minelib.registry.adapter.BukkitRegistryObjectAdapter;
import net.pxstudios.minelib.registry.type.CommandRegistryObject;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CommandRegistryObjectAdapter extends BukkitRegistryObjectAdapter<AbstractContextCommand> {

    @Override
    public void fireRegister(Plugin plugin, AbstractContextCommand obj) {
        if (obj != null) {
            MineLibrary.getLibrary().getCommandRegistry().registerCommand(obj);
        }
    }

    @Override
    public AbstractContextCommand newObjectInstance(Plugin plugin, BukkitRegistryObject<AbstractContextCommand> bukkitRegistryObject) {
        Method processMethod = getProcessMethod(bukkitRegistryObject);

        if (processMethod == null) {
            return null;
        }

        Set<CommandSettings<?>> settings = getSettings(bukkitRegistryObject);
        Set<String> aliases = getAliases(bukkitRegistryObject);

        return newCommandObj(plugin, bukkitRegistryObject, aliases, settings, processMethod);
    }

    private Method getProcessMethod(BukkitRegistryObject<AbstractContextCommand> bukkitRegistryObject) {
        try {
            return bukkitRegistryObject.getClass().getMethod("process", CommandContext.class);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private Set<CommandSettings<?>> getSettings(BukkitRegistryObject<AbstractContextCommand> bukkitRegistryObject) {
        try {
            for (Method method : bukkitRegistryObject.getClass().getDeclaredMethods()) {
                if (method.getParameterCount() != 0 || !method.getReturnType().isAssignableFrom(Collection.class)) {
                    continue;
                }

                if (method.isAnnotationPresent(CommandRegistryObject.SettingsMethod.class)) {

                    Collection<CommandSettings<?>> collection = (Collection<CommandSettings<?>>) method.invoke(bukkitRegistryObject);
                    return new HashSet<>(collection);
                }
            }

            return null;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }

    private Set<String> getAliases(BukkitRegistryObject<AbstractContextCommand> bukkitRegistryObject) {
        try {
            CommandRegistryObject.MultipleAliases multipleAliases = bukkitRegistryObject.getClass().getDeclaredAnnotation(CommandRegistryObject.MultipleAliases.class);
            if (multipleAliases == null) {
                return null;
            }

            Set<String> aliases = new HashSet<>();

            for (CommandRegistryObject.Alias alias : multipleAliases.value()) {
                aliases.add(alias.value());
            }

            return aliases;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }

    private AbstractContextCommand newCommandObj(Plugin plugin, BukkitRegistryObject<AbstractContextCommand> bukkitRegistryObject,
                                                 Set<String> aliases, Set<CommandSettings<?>> settings,
                                                 Method invoker) {

        if (aliases == null || aliases.isEmpty()) {
            return null;
        }
        AbstractContextCommand commandInstance = new AbstractContextCommand(plugin) {

            @SneakyThrows
            @Override
            public void process(CommandContext context) {
                invoker.invoke(bukkitRegistryObject, context);
            }
        };

        if (settings != null && !settings.isEmpty()) {
            commandInstance.getSettings().addAll(settings);
        }

        commandInstance.getActiveLabels().addAll(aliases);
        return commandInstance;
    }
}
