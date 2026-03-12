package com.czasclient;

import com.czasclient.core.ModuleManager;
import com.czasclient.core.EventManager;
import com.czasclient.gui.GuiManager;
import com.czasclient.config.ConfigManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(CzasClient.MOD_ID)
public class CzasClient {
    public static final String MOD_ID = "czasclient";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public CzasClient() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        CzasClient.LOGGER.info("Czas Client by 348 s - Initializing...");
        new ModuleManager();
        new GuiManager();
        new ConfigManager().loadConfig();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        CzasClient.LOGGER.info("Czas Client - Client setup complete");
        MinecraftForge.EVENT_BUS.register(new EventManager());
        MinecraftForge.EVENT_BUS.register(new GuiManager());
    }
}
