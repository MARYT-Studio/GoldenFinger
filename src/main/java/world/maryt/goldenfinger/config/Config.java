package world.maryt.goldenfinger.config;
import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec CONFIG;

    public static ForgeConfigSpec.BooleanValue NON_OP_CAN_USE;

    static {
        BUILDER.push("general");

        NON_OP_CAN_USE = BUILDER
                .comment("Define if non-OP players can use Golden Finger.")
                        .define("nonOpCanUse", false);

        BUILDER.pop();
        CONFIG = BUILDER.build();
    }
}
