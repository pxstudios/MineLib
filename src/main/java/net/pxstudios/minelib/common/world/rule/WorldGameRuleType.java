package net.pxstudios.minelib.common.world.rule;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WorldGameRuleType {
    
    DO_FIRE_TICK("doFireTick", "true"),
    MOB_GRIEF("mobGriefing", "true"),
    KEEP_INVENTORY("keepInventory", "false"),
    DO_MOB_SPAWNING("doMobSpawning", "true"),
    DO_MOB_LOOT("doMobLoot", "true"),
    DO_TILE_DROPS("doTileDrops", "true"),
    DO_ENTITY_DROPS("doEntityDrops", "true"),
    COMMAND_BLOCK_OUTPUT("commandBlockOutput", "true"),
    NATURAL_REGENERATION("naturalRegeneration", "true"),
    DO_DAYLIGHT_CYCLE("doDaylightCycle", "true"),
    LOG_ADMIN_COMMANDS("logAdminCommands", "true"),
    SHOW_DEATH_MESSAGES("showDeathMessages", "true"),
    RANDOM_TICK_SPEED("randomTickSpeed", "3"),
    SEND_COMMAND_FEEDBACK("sendCommandFeedback", "true"),
    REDUCED_DEBUG_INFO("reducedDebugInfo", "false"),
    SPECTATORS_GENERATE_CHUNKS("spectatorsGenerateChunks", "true"),
    SPAWN_RADIUS("spawnRadius", "10"),
    DISABLE_ELYTRA_MOVEMENT_CHECK("disableElytraMovementCheck", "false"),
    MAX_ENTITY_CRAMMING("maxEntityCramming", "24"),
    DO_WEATHER_CYCLE("doWeatherCycle", "true"),
    DO_LIMITED_CRAFTING("doLimitedCrafting", "false"),
    MAX_COMMAND_CHAIN_LENGTH("maxCommandChainLength", "65536"),
    ANNOUNCE_ADVANCEMENTS("announceAdvancements", "true"),
    GAME_LOOP_FUNCTION("gameLoopFunction", "-"),
    ;
    
    private final String name;

    private final String defaultValue;
}
