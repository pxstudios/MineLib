package net.pxstudios.minelib.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@AllArgsConstructor
@RequiredArgsConstructor
public class CommandSettings<R> {

// ------------------------------------------------------------------------------------------------------------------ //

    public static final CommandSettings<Class<? extends CommandSender>> SENDER_TYPE = new CommandSettings<>("SENDER_TYPE", CommandSender.class);

    public static final CommandSettings<Long> USE_COOLDOWN_DELAY = new CommandSettings<>("USE_COOLDOWN_DELAY", 100L);

    public static final CommandSettings<String> USE_PERMISSION = new CommandSettings<>("USE_PERMISSION");

    public static final CommandSettings<String> NO_PERMISSION_MESSAGE = new CommandSettings<>("NO_PERMISSION_MESSAGE", ChatColor.RED + "You don`t have permissions for use this command!");
    public static final CommandSettings<String> EMPTY_ARGS_MESSAGE = new CommandSettings<>("EMPTY_ARGS_MESSAGE", ChatColor.RED + "No enough arguments!");
    public static final CommandSettings<String> CONSOLE_DISABLED_MESSAGE = new CommandSettings<>("CONSOLE_USE_MESSAGE", ChatColor.RED + "That command can use only players!");

    public static final CommandSettings<?>[] DEFAULT_SETTINGS_ARRAY = {
            SENDER_TYPE,
            USE_COOLDOWN_DELAY,
            USE_PERMISSION,
            NO_PERMISSION_MESSAGE,
            EMPTY_ARGS_MESSAGE,
            CONSOLE_DISABLED_MESSAGE,
    };

// ------------------------------------------------------------------------------------------------------------------ //

    @Getter
    private final String name;

    private R value;

    public CommandSettings<R> set(R value) {
        this.value = value;
        return this;
    }

    public R value() {
        return value;
    }
}
