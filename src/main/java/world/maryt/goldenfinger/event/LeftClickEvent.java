package world.maryt.goldenfinger.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;

import java.util.concurrent.ThreadLocalRandom;

import static world.maryt.goldenfinger.GoldenFinger.*;

public class LeftClickEvent {
    public static void onLeftClick(LeftClickBlock event) {
        if (event.getPlayer() == null || !event.getPlayer().getPersistentData().getBoolean(MOD_ID)) return;
        if (!event.getWorld().isClientSide && event.getWorld().getServer() != null && event.getWorld().getBlockEntity(event.getPos()) instanceof ChestBlockEntity chest) {
            LootContext.Builder builder = (new LootContext.Builder((ServerLevel) event.getWorld())).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(event.getPos())).withOptionalRandomSeed(ThreadLocalRandom.current().nextLong());
            builder.withLuck(event.getPlayer().getLuck()).withParameter(LootContextParams.THIS_ENTITY, event.getPlayer());
            if (event.getHand() == InteractionHand.MAIN_HAND) {
                if (ALL_CUSTOM_RULES.containsKey(event.getItemStack().getItem().getRegistryName())) {
                    event.getWorld().getServer().getLootTables().get(ALL_CUSTOM_RULES.get(event.getItemStack().getItem().getRegistryName())).fill(chest, builder.create(LootContextParamSets.CHEST));
                }
            }
        }
    }
}
