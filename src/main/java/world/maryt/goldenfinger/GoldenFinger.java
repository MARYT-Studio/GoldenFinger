package world.maryt.goldenfinger;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import world.maryt.goldenfinger.command.GoldenFingerCommand;
import world.maryt.goldenfinger.config.Config;
import world.maryt.goldenfinger.event.LeftClickEvent;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Mod("golden_finger")
public class GoldenFinger {

    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "golden_finger";

    public static HashMap<ResourceLocation, ResourceLocation> ALL_CUSTOM_RULES = new HashMap<>();

    public GoldenFinger() {
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.CONFIG, MOD_ID + "-server.toml");

        MinecraftForge.EVENT_BUS.addListener(LeftClickEvent::onLeftClick);
        MinecraftForge.EVENT_BUS.addListener(GoldenFinger::onDataLoad);
        MinecraftForge.EVENT_BUS.addListener(GoldenFinger::onServerStarting);


        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {

    }

    private void processIMC(final InterModProcessEvent event) {

    }

    private static void onDataLoad(AddReloadListenerEvent event) {
        event.addListener(new SimpleJsonResourceReloadListener((new GsonBuilder()).create(), MOD_ID + "_rules") {
            @Override
            protected void apply(@NotNull Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
                ALL_CUSTOM_RULES.clear();
                ResourceLocation location = new ResourceLocation(MOD_ID, MOD_ID + "_rules" + "/custom_rules.json");
                try {
                    Resource resource = resourceManager.getResource(location);
                    InputStreamReader reader = new InputStreamReader(resource.getInputStream());
                    JsonElement jsonElement = JsonParser.parseReader(reader);
                    reader.close();

                    jsonElement.getAsJsonObject().entrySet().forEach(entry -> {
                        ResourceLocation item = ResourceLocation.tryParse(entry.getKey());
                        ResourceLocation lootTable = ResourceLocation.tryParse(entry.getValue().getAsString());
                        if (item != null && lootTable != null) {ALL_CUSTOM_RULES.put(item, lootTable);}
                    });

                    LOGGER.info("All custom rules are listed below:");
                    ALL_CUSTOM_RULES.forEach((item, lootTable) -> LOGGER.info("With item {} in hand, you can use loot table {}", item, lootTable));
                } catch (IllegalArgumentException | JsonParseException | IOException e) {
                        LOGGER.error("Parsing error loading custom rules. Message: {}", e.getMessage());
                    }
                }
        });
    }

    public static void onServerStarting(ServerStartingEvent event) {
        GoldenFingerCommand.register(event.getServer().getCommands().getDispatcher());
        LOGGER.info("GoldenFinger command registered");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

    }
}
