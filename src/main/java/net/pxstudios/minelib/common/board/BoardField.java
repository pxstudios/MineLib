package net.pxstudios.minelib.common.board;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.function.Function;

@RequiredArgsConstructor
public class BoardField {

    private final Board board;

    private Function<Player, String> textGetter;

    @Getter
    private final int position;

    @Getter
    private boolean isStatic;

    enum Action {

        UPDATE,
        CREATE,
        REMOVE,
    }

    public final BoardField setModifiableText(Function<Player, String> textGetter) {
        this.isStatic = false;
        this.textGetter = textGetter;

        return this;
    }

    public final BoardField setStaticText(String text) {
        this.isStatic = true;

        return setModifiableText((player) -> text);
    }

    public final String getStaticText() {
        return isStatic ? textGetter.apply(null) : null;
    }

    public final String getModifiableText(Player player) {
        return !isStatic ? textGetter.apply(player) : null;
    }

    private void sendUpdate(Player player) {
        // todo
    }

    private void sendCreate(Player player) {
        // todo
    }

    private void sendRemove(Player player) {
        // todo
    }

    void sendAction(Player player, Action action) {
        switch (action) {

            case UPDATE: sendUpdate(player); break;
            case CREATE: sendCreate(player); break;
            case REMOVE: sendRemove(player); break;
        }
    }
}
