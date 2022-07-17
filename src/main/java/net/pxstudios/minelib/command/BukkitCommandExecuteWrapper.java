package net.pxstudios.minelib.command;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import net.pxstudios.minelib.command.type.AbstractContextCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class BukkitCommandExecuteWrapper extends Command implements CommandExecutor {

    private static final String DEF_DESCRIPTION = "Command created & registered by MineLib";

    @Getter(AccessLevel.PACKAGE)
    private final AbstractContextCommand handle;

    private Cache<CommandSender, Long> cooldownDelayCache;

    protected BukkitCommandExecuteWrapper(AbstractContextCommand handle, String name, String[] aliases) {
        super(name, DEF_DESCRIPTION, "", Arrays.asList(aliases));

        this.handle = handle;
    }

    @Override
    public boolean execute(CommandSender commandSender, String label, String[] args) {
        if (applySettings(commandSender, args)) {
            handle.process(new CommandContext(handle.getSettings(), commandSender, args, label));
        }
        return false;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        return execute(commandSender, label, args);
    }

    private Cache<CommandSender, Long> injectCooldownDelayCache(long cooldownDelayMillis) {
        if (cooldownDelayCache == null) {
            cooldownDelayCache = CacheBuilder.newBuilder().expireAfterAccess(cooldownDelayMillis, TimeUnit.MILLISECONDS).build();
        }
        else if (cooldownDelayCache.stats().totalLoadTime() != cooldownDelayMillis) {
            cooldownDelayCache = null;

            return injectCooldownDelayCache(cooldownDelayMillis);
        }

        return cooldownDelayCache;
    }

    private boolean applySettings(CommandSender commandSender, String[] args) {
        Set<CommandSettings<?>> settings = handle.getSettings();
        boolean resultFlag = true;

        // Check sender class type.
        if (settings.contains(CommandSettings.SENDER_TYPE)) {
            Class<? extends CommandSender> value = handle.getSettingValue(CommandSettings.SENDER_TYPE);

            resultFlag = commandSender.getClass().isAssignableFrom(value);

            // If sender type as console is disabled.
            if (!resultFlag && settings.contains(CommandSettings.CONSOLE_DISABLED_MESSAGE) && value.isAssignableFrom(ConsoleCommandSender.class)) {

                String message = handle.getSettingValue(CommandSettings.CONSOLE_DISABLED_MESSAGE);
                commandSender.sendMessage(message);
            }
        }

        // Check command argument's length.
        if (resultFlag && settings.contains(CommandSettings.EMPTY_ARGS_MESSAGE) && args.length == 0) {

            String message = handle.getSettingValue(CommandSettings.EMPTY_ARGS_MESSAGE);
            commandSender.sendMessage(message);
        }

        // Check sender general permissions.
        if (resultFlag && settings.contains(CommandSettings.USE_PERMISSION)) {

            String permission = handle.getSettingValue(CommandSettings.USE_PERMISSION);
            resultFlag = commandSender.hasPermission(permission);

            // If sender is no has permissions then check next setting & send value as message.
            if (!resultFlag && settings.contains(CommandSettings.NO_PERMISSION_MESSAGE)) {

                String message = handle.getSettingValue(CommandSettings.NO_PERMISSION_MESSAGE);
                commandSender.sendMessage(String.format(message, permission));
            }
        }

        // Check cooldown delay for command using.
        if (resultFlag && settings.contains(CommandSettings.USE_COOLDOWN_DELAY)) {

            long cooldownDelayMillis = handle.getSettingValue(CommandSettings.USE_COOLDOWN_DELAY);

            Cache<CommandSender, Long> cooldownDelayCache = injectCooldownDelayCache(cooldownDelayMillis);
            cooldownDelayCache.cleanUp();

            resultFlag = !cooldownDelayCache.asMap().containsKey(commandSender);

            // If sender is no has cooldown for command using then add he in caches.
            if (resultFlag) {
                cooldownDelayCache.put(commandSender, System.currentTimeMillis());
            }
            else if (settings.contains(CommandSettings.HAS_COOLDOWN_DELAY_MESSAGE)) {

                long cooldownLeftMillis = cooldownDelayMillis - (System.currentTimeMillis() - cooldownDelayCache.asMap().get(commandSender));
                long cooldownLeftSeconds = TimeUnit.MILLISECONDS.toSeconds(cooldownLeftMillis);

                // Validate maybe time logic error.
                if (cooldownLeftMillis > 0) {

                    String message = handle.getSettingValue(CommandSettings.HAS_COOLDOWN_DELAY_MESSAGE);
                    commandSender.sendMessage(String.format(message, cooldownLeftSeconds));
                }
                else {
                    resultFlag = true;
                }
            }
        }

        return resultFlag;
    }

}
