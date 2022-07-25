package net.pxstudtios.minelib.test.board;

import lombok.NonNull;
import net.pxstudios.minelib.MineLibrary;
import net.pxstudios.minelib.board.Board;
import net.pxstudios.minelib.board.BoardApi;
import net.pxstudios.minelib.board.BoardFlag;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.concurrent.ThreadLocalRandom;

public class TestBoardImpl extends Board {

    private static final TestBoardImpl IMPL = new TestBoardImpl(MineLibrary.getLibrary());

    public static void setPlayer(@NonNull Player player) {
        // Show board to the player.
        IMPL.addPlayerView(player);
    }

    private TestBoardImpl(@NonNull MineLibrary mineLibrary) {
        super(mineLibrary.getBoardApi(), DisplaySlot.SIDEBAR, "hub", BoardApi.DUMMY_CRITERIA);

        addFlag(BoardFlag.REMOVE_ON_PLAYER_JOIN);
        addFlag(BoardFlag.REMOVE_ON_CHANGED);
        addFlag(BoardFlag.WITH_AUTOMATICALLY_STATIC_LINES_COLORIZE);
        addFlag(BoardFlag.USE_DISABLED_WORLD_SYSTEM);
        addFlag(BoardFlag.USE_GLOBAL_PRESETS);

        subscribePlayerEvents(mineLibrary.getEventsSubscriber());

        // Add disabled worlds.
        addDisabledWorld("survival");
        addDisabledWorld("bw_arena_1");

        // Set a display-name for objective of board.
        setDisplayName("HUB"); // can use presets (use: "preset:PRESET_KEY")

        // Set a lines for objective of board.
        setLineSmart(5);
        setLineSmart(4, "&7Your name:");
        setLineSmart(3, HumanEntity::getName);
        setLineSmart(2);
        setLineSmart(1, "preset:Website");

        // Add automatically updater to board.
        update(10, () -> {

            ChatColor[] chatColors = ChatColor.values();
            ChatColor randomColor = chatColors[ThreadLocalRandom.current().nextInt(chatColors.length)];

            setDisplayName(randomColor + ChatColor.stripColor(getObjective().getDisplayName()));

            return true; // if continue the next repeat of update-action?
        });
    }

}
