package net.pxstudios.minelib.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class CommandContext {

    private final Set<CommandSettings<?>> settings;

    private final CommandSender commandSender;

    @Getter
    private final String[] arguments;

    @Getter
    private final String label;

    @SuppressWarnings("unchecked")
    public <R> CommandSettings<R> getSetting(String name) {
        for (CommandSettings<?> commandSettings : settings) {

            if (commandSettings.getName().equals(name)) {
                return (CommandSettings<R>) commandSettings;
            }
        }

        return null;
    }

    public void withArgumentsOrFail(int requireSize, Runnable process, Runnable onFail) {
        if (arguments.length < requireSize) {

            onFail.run();
            return;
        }

        process.run();
    }

    public void withArguments(int requireSize, Runnable process) {
        withArgumentsOrFail(requireSize, process, () -> {

            CommandSettings<String> message = getSetting("EMPTY_ARGS_MESSAGE");
            commandSender.sendMessage(message.value());
        });
    }

    public int argumentsSize() {
        return arguments.length;
    }

    public Argument argument(int index) {
        return new Argument(index - 1);
    }

    public <T> Optional<T> argument(int index, Function<String, T> parser) {
        return argument(index).as(parser);
    }

    public String argumentStr(int index) {
        return argument(index).toString();
    }

    public CommandSender sender() {
        return commandSender;
    }

    public Player senderAsPlayer() {
        return ((Player) commandSender);
    }

    @RequiredArgsConstructor
    public class Argument {

        private final int index;

        @Override
        public String toString() {
            return arguments[index];
        }

        public <T> T asOrFail(Function<String, T> parseFunction, Supplier<T> onFail) {
            if (onFail == null) {
                onFail = () -> {
                    commandSender.sendMessage(ChatColor.RED + String.format("Unknown argument (%d) assertion type!", index));

                    return null;
                };
            }

            try {
                T value = parseFunction.apply(toString());

                if (value == null) {
                    return onFail.get();
                }

                return value;
            }
            catch (Throwable throwable) {
                return onFail.get();
            }
        }

        public <T> Optional<T> asOrFail(Function<String, T> parseFunction, Runnable onFail) {
            return Optional.ofNullable(asOrFail(parseFunction, () -> {

                if (onFail != null) {
                    onFail.run();
                }

                return null;
            }));
        }

        public <T> Optional<T> as(Function<String, T> parseFunction) {
            return asOrFail(parseFunction, (Runnable) null);
        }

        public Optional<Player> asOnlinePlayerOrFail(Runnable onFail) {
            return asOrFail(Bukkit::getPlayer, onFail);
        }

        public Optional<Player> asOnlinePlayer() {
            return asOnlinePlayerOrFail(null);
        }

        public Optional<OfflinePlayer> asOfflinePlayerOrFail(Runnable onFail) {
            return asOrFail(Bukkit::getOfflinePlayer, onFail);
        }

        public Optional<OfflinePlayer> asOfflinePlayer() {
            return asOfflinePlayerOrFail(null);
        }

        public Optional<UUID> asUUID(Runnable onFail) {
            return asOrFail(UUID::fromString, onFail);
        }

        public Optional<UUID> asUUID() {
            return asUUID(null);
        }

        public Optional<UUID> asBytesUUID(Runnable onFail) {
            return asOrFail(str -> UUID.nameUUIDFromBytes(str.getBytes()), onFail);
        }

        public Optional<UUID> asBytesUUID() {
            return asBytesUUID(null);
        }

        public Optional<Integer> asIntOrFail(Runnable onFail) {
            return asOrFail(Integer::parseInt, onFail);
        }

        public Optional<Integer> asInt() {
            return asIntOrFail(null);
        }

        public Optional<Long> asLongOrFail(Runnable onFail) {
            return asOrFail(Long::parseLong, onFail);
        }

        public Optional<Long> asLong() {
            return asLongOrFail(null);
        }

        public Optional<Double> asDoubleOrFail(Runnable onFail) {
            return asOrFail(Double::parseDouble, onFail);
        }

        public Optional<Double> asDouble() {
            return asDoubleOrFail(null);
        }
    }

}
