package net.pxstudios.minelib.event.bukkit.player;

import lombok.Getter;
import lombok.Setter;
import net.pxstudios.minelib.MineLibrary;
import net.pxstudios.minelib.event.player.MLPlayerEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

@Getter
public class MLPlayerShootBowEvent extends MLPlayerEvent implements Cancellable {

    private final ItemStack bow;

    @Setter
    private Entity projectile;

    private final float force;

    @Getter
    @Setter
    private boolean cancelled;

    public MLPlayerShootBowEvent(MineLibrary mineLibrary, Player player, ItemStack bow, Entity projectile, float force) {
        super(mineLibrary, player);

        this.bow = bow;
        this.projectile = projectile;
        this.force = force;
    }

}
