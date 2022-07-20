package net.pxstudios.minelib.common.chat;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.function.Predicate;

public final class ChatApi {

    private static final Server SERVER = Bukkit.getServer();

    public void sendMessage(ChatDirection direction, Player player, String message, Predicate<Player> predicate) {
        if (direction == null) {
            direction = ChatDirection.CHAT;
        }

        if (predicate != null && !predicate.test(player)) {
            return;
        }

        switch (direction) {
            case CHAT: {
                player.sendMessage(message);
                break;
            }

            case ACTIONBAR: {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
                break;
            }

            case TITLE: {
                player.sendTitle(message, "");
                break;
            }

            case SUBTITLE: {
                player.sendTitle("", message);
                break;
            }
        }
    }

    public void sendMessage(ChatDirection direction, Player player, String message) {
        sendMessage(direction, player, message, (Predicate<Player>) null);
    }

    public void sendMessage(Player player, String message) {
        sendMessage(ChatDirection.CHAT, player, message, (Predicate<Player>) null);
    }

    public void sendMessage(ChatDirection direction, Player player, String permission, String message) {
        if (player.hasPermission(permission)) {
            sendMessage(direction, player, message);
        }
    }

    public void sendMessage(Player player, String permission, String message) {
        sendMessage(ChatDirection.CHAT, player, permission, message);
    }

    public void broadcastMessage(boolean inConsole, ChatDirection direction, String message, Predicate<Player> predicate) {
        if (inConsole) {
            SERVER.getConsoleSender().sendMessage(message);
        }

        for (Player player : SERVER.getOnlinePlayers()) {

            if (predicate != null && predicate.test(player)) {
                sendMessage(direction, player, message);
            }
        }
    }

    public void broadcastMessage(ChatDirection direction, String message, Predicate<Player> predicate) {
        broadcastMessage(true, direction, message, predicate);
    }

    public void broadcastMessage(String message, Predicate<Player> predicate) {
        broadcastMessage(ChatDirection.CHAT, message, predicate);
    }

    public void broadcastMessage(ChatDirection direction, String message) {
        broadcastMessage(direction, message, (Predicate<Player>) null);
    }

    public void broadcastMessage(String message) {
        broadcastMessage(ChatDirection.CHAT, message);
    }

    public void broadcastMessage(ChatDirection direction, String permission, String message) {
        broadcastMessage(direction, message, player -> permission != null && player.hasPermission(permission));
    }

    public void broadcastMessage(String permission, String message) {
        broadcastMessage(ChatDirection.CHAT, permission, message);
    }

}
