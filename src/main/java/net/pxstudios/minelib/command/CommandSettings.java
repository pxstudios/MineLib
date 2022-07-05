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

    public static final CommandSettings<Long> USE_COOLDOWN_DELAY = new CommandSettings<>("USE_COOLDOWN_DELAY");

    public static final CommandSettings<String> EMPTY_ARGS_MESSAGE = new CommandSettings<>("EMPTY_ARGS_MESSAGE", ChatColor.RED + "No enough arguments!");
    public static final CommandSettings<String> CONSOLE_USE_MESSAGE = new CommandSettings<>("CONSOLE_USE_MESSAGE", ChatColor.RED + "That command can use only players!");

    public static final CommandSettings<?>[] DEFAULT_SETTINGS_ARRAY = {
            SENDER_TYPE,
            USE_COOLDOWN_DELAY,
            EMPTY_ARGS_MESSAGE,
            CONSOLE_USE_MESSAGE,
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
