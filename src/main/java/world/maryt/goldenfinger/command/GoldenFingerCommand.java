package world.maryt.goldenfinger.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TranslatableComponent;

import net.minecraft.world.entity.player.Player;
import world.maryt.goldenfinger.config.Config;

import static world.maryt.goldenfinger.GoldenFinger.*;

public class GoldenFingerCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        int properPermission = Config.NON_OP_CAN_USE.get() ? 0 : 2;
        dispatcher.register(Commands.literal(MOD_ID).requires(source -> source.hasPermission(properPermission)).executes(GoldenFingerCommand::execute));
    }

    private static int execute(CommandContext<CommandSourceStack> context) {
        if (context.getSource().getEntity() instanceof Player player) {
            if (!player.getPersistentData().contains(MOD_ID)) player.getPersistentData().putBoolean(MOD_ID, true);
            else {player.getPersistentData().putBoolean(MOD_ID, !player.getPersistentData().getBoolean(MOD_ID));}
            if (player.getPersistentData().getBoolean(MOD_ID)) context.getSource().sendSuccess(new TranslatableComponent(MOD_ID + ".command_executed"), false);
            return 0;
        }
        LOGGER.warn("Golden Finger command must be executed by player.");
        return 1;
    }
}
