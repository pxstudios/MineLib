package net.pxstudios.minelib.board;

import gnu.trove.TCollections;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.function.Function;

@RequiredArgsConstructor
public class BoardObjective {

    private final Board board;
    private final TIntObjectMap<BoardField> fieldsMap = TCollections.synchronizedMap(new TIntObjectHashMap<>());

    @Getter
    private final DisplaySlot display;

    @Getter
    private final String name;
    @Getter
    private final String criteria;

    @Setter
    @Getter
    private String displayName;

    public final TIntObjectMap<BoardField> getScores() {
        return TCollections.unmodifiableMap(fieldsMap);
    }

    private BoardField createField0(int position) {
        return new BoardField(board, position);
    }

    public final BoardField createStaticField(int position, String text) {

        if (board.hasFlag(BoardFlag.WITH_AUTOMATICALLY_STATIC_LINES_COLORIZE)) {
            text = ChatColor.translateAlternateColorCodes('&', text);
        }

        return createField0(position).setStaticText(text);
    }

    public final BoardField createModifiableField(int position, Function<Player, String> textGetter) {
        return createField0(position).setModifiableText(textGetter);
    }

    public final void setField(BoardField field) {
        if (field == null) {
            throw new NullPointerException("score");
        }

        if (fieldsMap.put(field.getPosition(), field) == null) {

            for (Player player : board.getPlayersViews()) {
                field.sendAction(player, BoardField.Action.CREATE);
            }
        }
        else {
            for (Player player : board.getPlayersViews()) {
                field.sendAction(player, BoardField.Action.UPDATE);
            }
        }
    }

    public void setStaticField(int position, String text) {
        BoardField field = createStaticField(position, text);
        setField(field);
    }

    public void setModifiableField(int position, Function<Player, String> textGetter) {
        BoardField field = createModifiableField(position, textGetter);
        setField(field);
    }

    public final void removeField(int position) {
        BoardField field = fieldsMap.remove(position);

        if (field == null) {
            throw new IllegalArgumentException("score position (" + position + ")");
        }

        for (Player player : board.getPlayersViews()) {
            field.sendAction(player, BoardField.Action.REMOVE);
        }
    }

    public final BoardField getField(int position) {
        return fieldsMap.get(position);
    }

    private void sendFieldsAction(Player player, BoardField.Action action) {
        fieldsMap.forEachValue(boardField -> {

            boardField.sendAction(player, action);
            return true;
        });
    }

    void sendUpdateStatus(Player player) {
        sendFieldsAction(player, BoardField.Action.UPDATE);

        // todo
    }

    void sendCreateStatus(Player player) {
        sendFieldsAction(player, BoardField.Action.CREATE);

        // todo
    }

    void sendRemoveStatus(Player player) {
        sendFieldsAction(player, BoardField.Action.REMOVE);

        // todo
    }

}
