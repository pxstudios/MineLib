package net.pxstudios.minelib.common.board;

import lombok.Getter;
import lombok.NonNull;
import net.pxstudios.minelib.MineLibrary;
import net.pxstudios.minelib.event.EventsSubscriber;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings("UnusedReturnValue")
public class Board {

    private static final String UNKNOWN_PRESET_FORMAT = "PRESET(%s)";
    private static final String SMART_PRESET_LINE_PREFIX = "preset:";

    private final BoardApi boardApi;

    @Getter
    private final BoardObjective objective;

    private final Set<Player> playersViewSet = new HashSet<>();
    private final Set<BoardFlag> flagsSet = new HashSet<>();
    private final Set<String> disabledWorldsSet = new HashSet<>();

    public Board(BoardApi boardApi, DisplaySlot display, String name, String criteria) {
        this.boardApi = boardApi;
        this.objective = boardApi.createObjective(this, display, name, criteria);
    }

    public Board(BoardApi boardApi, String name, String criteria) {
        this(boardApi, DisplaySlot.SIDEBAR, name, criteria);
    }

    public Board(BoardApi boardApi, String name) {
        this(boardApi, DisplaySlot.SIDEBAR, name, BoardApi.DUMMY_CRITERIA);
    }

    public Board(BoardApi boardApi, DisplaySlot display, String name) {
        this(boardApi, display, name, BoardApi.DUMMY_CRITERIA);
    }

    public final void subscribePlayerEvents(EventsSubscriber eventsSubscriber) {
        eventsSubscriber.subscribe(PlayerJoinEvent.class, EventPriority.HIGHEST)
                .complete(event -> {

                    if (hasFlag(BoardFlag.VIEW_ON_PLAYER_JOIN)) {
                        addPlayerView(event.getPlayer());
                    }
                });

        eventsSubscriber.subscribe(PlayerQuitEvent.class, EventPriority.HIGHEST)
                .complete(event -> {

                    Player player = event.getPlayer();

                    if (isViewing(player) && hasFlag(BoardFlag.REMOVE_ON_PLAYER_JOIN)) {
                        removePlayerView(player);
                    }
                });

        eventsSubscriber.subscribe(PlayerChangedWorldEvent.class, EventPriority.HIGHEST)
                .complete(event -> {

                    Player player = event.getPlayer();

                    if (isViewing(player) && hasFlag(BoardFlag.USE_DISABLED_WORLD_SYSTEM)) {

                        World worldFrom = event.getFrom();
                        World worldTo = player.getWorld();

                        if (isDisabledWorld(worldTo)) {
                            sendRemoveStatus(player);
                        }
                        else if (isDisabledWorld(worldFrom)) {
                            sendCreateStatus(player);
                        }
                    }
                });
    }

    public final boolean addDisabledWorld(String disabledWorldName) {
        return disabledWorldsSet.add(disabledWorldName.toLowerCase());
    }

    public final boolean removeDisabledWorld(String disabledWorldName) {
        return disabledWorldsSet.remove(disabledWorldName.toLowerCase());
    }

    public final boolean isDisabledWorld(String worldName) {
        return disabledWorldsSet.contains(worldName.toLowerCase());
    }

    public final boolean isDisabledWorld(@NonNull World world) {
        return isDisabledWorld(world.getName());
    }

    public final Set<String> getDisabledWorldsNames() {
        return Collections.unmodifiableSet(disabledWorldsSet);
    }

    public final Set<World> getDisabledWorlds() {
        return Collections.unmodifiableSet(disabledWorldsSet.stream().map(Bukkit::getWorld).collect(Collectors.toSet()));
    }

    public final Board addFlag(@NonNull BoardFlag flag) {
        flagsSet.add(flag);

        return this;
    }

    public final Board removeFlag(@NonNull BoardFlag flag) {
        flagsSet.remove(flag);

        return this;
    }

    public final boolean hasFlag(@NonNull BoardFlag flag) {
        return flagsSet.contains(flag);
    }

    protected final void checkObjectiveNullable() {
        if (objective == null) {
            throw new NullPointerException("objective");
        }
    }

    public final boolean isViewing(Player player) {
        return playersViewSet.contains(player);
    }

    public final boolean addPlayerView(Player player) {
        Board previous = boardApi.getActiveBoard(player);

        if (previous != null && previous.hasFlag(BoardFlag.REMOVE_ON_CHANGED)) {
            previous.removePlayerView(player);
        }

        boolean add = playersViewSet.add(player);

        if (add) {
            sendCreateStatus(player);
        }

        return add;
    }

    public final boolean removePlayerView(Player player) {
        boolean remove = playersViewSet.remove(player);

        if (remove) {
            sendRemoveStatus(player);
        }

        return remove;
    }

    public final boolean update(Player player) {
        boolean contains = (player != null) && playersViewSet.contains(player);

        if (contains) {
            sendUpdateStatus(player);
        }

        return contains;
    }

    public final Set<Player> getPlayersViews() {
        return Collections.unmodifiableSet(playersViewSet);
    }

    void sendUpdateStatus(Player player) {
        checkObjectiveNullable();

        objective.sendUpdateStatus(player);
    }

    void sendCreateStatus(Player player) {
        checkObjectiveNullable();

        objective.sendCreateStatus(player);
    }

    void sendRemoveStatus(Player player) {
        checkObjectiveNullable();

        objective.sendRemoveStatus(player);
    }

    public final void setDisplayName(@NonNull String displayName) {
        checkObjectiveNullable();

        boolean preset = displayName.toLowerCase().startsWith(SMART_PRESET_LINE_PREFIX);

        if (preset) {
            displayName = displayName.substring(SMART_PRESET_LINE_PREFIX.length());

            String presetString = boardApi.getGlobalPresetsManager().getPresetAsString(displayName);

            if (presetString == null) {
                displayName = String.format(UNKNOWN_PRESET_FORMAT, displayName);
            }
        }

        objective.setDisplayName(displayName);
    }

    private String getBlackLineText(int position) {
        char[] charsArray = BoardApi.BLANK_LINE_CHARS_BY_POSITION;

        char selectedChar = charsArray[position >= charsArray.length || position < 0 ? 0 : position];

        return ChatColor.getByChar(selectedChar).toString();
    }

    public final Board setStaticLine(int position, String text) {
        checkObjectiveNullable();

        objective.setStaticField(position, text);

        return this;
    }

    public final Board setModifiableLine(int position, Function<Player, String> textGetter) {
        checkObjectiveNullable();

        objective.setModifiableField(position, textGetter);

        return this;
    }

    public final Board setBlankLine(int position) {
        return setStaticLine(position, getBlackLineText(position));
    }

    public final Board setPresetLine(int position, String presetKey) {
        String staticText = String.format(UNKNOWN_PRESET_FORMAT, presetKey);

        if (hasFlag(BoardFlag.USE_GLOBAL_PRESETS)) {
            String presetString = boardApi.getGlobalPresetsManager().getPresetAsString(presetKey);

            if (presetString != null) {
                staticText = presetString;
            }
        }

        return setStaticLine(position, staticText);
    }

    public final Board setLineSmart(int position) {
        return setBlankLine(position);
    }

    public final Board setLineSmart(int position, String text) {
        boolean preset = text.toLowerCase().startsWith(SMART_PRESET_LINE_PREFIX);
        return !preset ? setStaticLine(position, text) : setPresetLine(position, text.substring(SMART_PRESET_LINE_PREFIX.length()));
    }

    public final Board setLineSmart(int position, Function<Player, String> textGetter) {
        return setModifiableLine(position, textGetter);
    }

    public final void update(int ticks, Supplier<Boolean> actionOnUpdate) {
        MineLibrary.getLibrary().getBeater().runCancellableTimer(ticks, ticks, new BukkitRunnable() {

            @Override
            public void run() {
                boolean canTaskContinue = actionOnUpdate.get();

                playersViewSet.forEach(player -> {

                    if (player.isOnline()) {
                        update(player);
                    }
                });

                if (!canTaskContinue) {
                    cancel();
                }
            }
        });
    }

    public final void update(int ticks) {
        update(ticks, () -> true);
    }

}
