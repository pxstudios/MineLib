package net.pxstudios.minelib.board;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.pxstudios.minelib.plugin.MinecraftPlugin;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public final class BoardApi {

    public static final int MAX_NAME_LENGTH         = 16;

    public static final String DUMMY_CRITERIA = "dummy";

    public static final char[] BLANK_LINE_CHARS_BY_POSITION =
            {
                    '0', '1', '2', '3', '4',
                    '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e',
                    'f', 'r',
            };

    @Getter
    private final MinecraftPlugin plugin;
    private final Map<String, Board> createdBoardsMap = new HashMap<>();

    @Getter
    private final BoardPresetsManager globalPresetsManager = new BoardPresetsManager();

    private String substringNameLength(String name) {
        return name.length() > MAX_NAME_LENGTH ? name.substring(0, MAX_NAME_LENGTH) : name;
    }

    public BoardPresetsManager createPresetsManager() {
        return new BoardPresetsManager();
    }

    public Board createOrGetBoard(DisplaySlot display, String name, String criteria) {
        return createdBoardsMap.computeIfAbsent(substringNameLength(name), (f) -> new Board(this, display, substringNameLength(name), criteria));
    }

    public Board createOrGetBoard(String name) {
        return createOrGetBoard(DisplaySlot.SIDEBAR, name, DUMMY_CRITERIA);
    }

    public Board createOrGetBoard(DisplaySlot display, String name) {
        return createOrGetBoard(display, name, DUMMY_CRITERIA);
    }

    public Board createOrGetBoard(String name, String criteria) {
        return createOrGetBoard(DisplaySlot.SIDEBAR, name, criteria);
    }

    public BoardObjective createObjective(Board board, DisplaySlot display, String name, String criteria) {
        return new BoardObjective(board, display, name, criteria);
    }

    public BoardObjective createObjective(Board board, String name, String criteria) {
        return createObjective(board, DisplaySlot.SIDEBAR, name, criteria);
    }

    public BoardObjective createObjective(Board board, String name) {
        return createObjective(board, DisplaySlot.SIDEBAR, name, DUMMY_CRITERIA);
    }

    public BoardObjective createObjective(Board board, DisplaySlot display, String name) {
        return createObjective(board, display, name, DUMMY_CRITERIA);
    }

    public Board getActiveBoard(Player player) {
        for (Board board : createdBoardsMap.values()) {

            if (board.isViewing(player)) {
                return board;
            }
        }

        return null;
    }

    public void removeBoard(@NonNull String name) {
        Board board = createdBoardsMap.remove(substringNameLength(name));
        
        if (board != null) {
            board.getPlayersViews().forEach(board::removePlayerView);
        }
    }

}
