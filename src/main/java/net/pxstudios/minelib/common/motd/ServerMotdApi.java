package net.pxstudios.minelib.common.motd;

import lombok.NonNull;
import lombok.SneakyThrows;
import net.pxstudios.minelib.MineLibrary;
import org.bukkit.Server;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.util.CachedServerIcon;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

public final class ServerMotdApi {

    private Server server;

    private CachedServerIcon serverIcon;

    private String motd;

    private int maxPlayers;

    public void initDefaults(@NonNull Server server) {
        this.server = server;

        this.motd = server.getMotd();
        this.maxPlayers = server.getMaxPlayers();
        this.serverIcon = server.getServerIcon();
    }

    private void checkServerNullable() {
        if (server == null) {
            throw new NullPointerException("server");
        }
    }

    public void enableApiEvents() {
        MineLibrary.getLibrary().getEventsSubscriber()
                .subscribe(ServerListPingEvent.class, EventPriority.HIGHEST)

                .withPredication(event -> server != null)
                .complete(event -> {

                    event.setServerIcon(serverIcon);
                    event.setMotd(motd);
                    event.setMaxPlayers(maxPlayers);
                });
    }

    public void setServerIcon(@NonNull BufferedImage bufferedImage) {
        checkServerNullable();

        try {
            this.serverIcon = server.loadServerIcon(bufferedImage);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @SneakyThrows
    public void setServerIcon(@NonNull File file) {
        setServerIcon(ImageIO.read(file));
    }

    @SneakyThrows
    public void setServerIcon(@NonNull URL url) {
        setServerIcon(ImageIO.read(url));
    }

    @SneakyThrows
    public void setServerIcon(@NonNull InputStream inputStream) {
        setServerIcon(ImageIO.read(inputStream));
    }

    public void setMotd(@NonNull String motd) {
        checkServerNullable();

        this.motd = motd;
    }

    public void setMotd(@NonNull Iterable<String> motd) {
        setMotd(String.join("\n", motd));
    }

    public void setMaxPlayers(int maxPlayers) {
        checkServerNullable();

        this.maxPlayers = maxPlayers;
    }

}
