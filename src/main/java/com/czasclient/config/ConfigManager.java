package com.czasclient.config;

import com.czasclient.core.Module;
import com.czasclient.core.ModuleManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigManager {
    private static ConfigManager instance;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final File configDir;
    private final File configFile;

    public ConfigManager() {
        instance = this;
        configDir = new File("czasclient");
        if (!configDir.exists()) {
            configDir.mkdirs();
        }
        configFile = new File(configDir, "config.json");
    }

    public static ConfigManager getInstance() {
        return instance;
    }

    public void saveConfig() {
        try {
            JsonObject config = new JsonObject();
            
            for (Module module : ModuleManager.getInstance().getModules()) {
                JsonObject moduleConfig = new JsonObject();
                moduleConfig.addProperty("enabled", module.isEnabled());
                moduleConfig.addProperty("key", module.getKey());
                
                JsonObject settings = getModuleSettings(module);
                if (settings.size() > 0) {
                    moduleConfig.add("settings", settings);
                }
                
                config.add(module.getName().toLowerCase().replace(" ", "_"), moduleConfig);
            }

            JsonObject guiConfig = new JsonObject();
            guiConfig.addProperty("show_watermark", true);
            guiConfig.addProperty("theme", "red_black");
            guiConfig.addProperty("language", "english");
            config.add("gui", guiConfig);

            try (FileWriter writer = new FileWriter(configFile)) {
                gson.toJson(config, writer);
            }

            CzasClient.LOGGER.info("Configuration saved successfully");
        } catch (IOException e) {
            CzasClient.LOGGER.error("Failed to save configuration: " + e.getMessage());
        }
    }

    public void loadConfig() {
        if (!configFile.exists()) {
            CzasClient.LOGGER.info("No configuration file found, using defaults");
            return;
        }

        try (FileReader reader = new FileReader(configFile)) {
            JsonObject config = gson.fromJson(reader, JsonObject.class);
            
            if (config == null) return;

            for (Module module : ModuleManager.getInstance().getModules()) {
                String moduleName = module.getName().toLowerCase().replace(" ", "_");
                if (config.has(moduleName)) {
                    JsonObject moduleConfig = config.getAsJsonObject(moduleName);
                    
                    if (moduleConfig.has("enabled")) {
                        boolean enabled = moduleConfig.get("enabled").getAsBoolean();
                        if (enabled && !module.isEnabled()) {
                            module.toggle();
                        } else if (!enabled && module.isEnabled()) {
                            module.toggle();
                        }
                    }
                    
                    if (moduleConfig.has("key")) {
                        module.setKey(moduleConfig.get("key").getAsInt());
                    }
                    
                    if (moduleConfig.has("settings")) {
                        applyModuleSettings(module, moduleConfig.getAsJsonObject("settings"));
                    }
                }
            }

            if (config.has("gui")) {
                JsonObject guiConfig = config.getAsJsonObject("gui");
                loadGuiSettings(guiConfig);
            }

            CzasClient.LOGGER.info("Configuration loaded successfully");
        } catch (IOException e) {
            CzasClient.LOGGER.error("Failed to load configuration: " + e.getMessage());
        }
    }

    private JsonObject getModuleSettings(Module module) {
        JsonObject settings = new JsonObject();
        
        if (module instanceof com.czasclient.modules.movement.WhileSpeed) {
            com.czasclient.modules.movement.WhileSpeed speed = (com.czasclient.modules.movement.WhileSpeed) module;
            settings.addProperty("speed", speed.getSpeed());
            settings.addProperty("air_strafe", speed.isAirStrafe());
        } else if (module instanceof com.czasclient.modules.combat.AimAssist) {
            com.czasclient.modules.combat.AimAssist aim = (com.czasclient.modules.combat.AimAssist) module;
            settings.addProperty("range", aim.getRange());
            settings.addProperty("speed", aim.getSpeed());
            settings.addProperty("only_players", aim.isOnlyPlayers());
            settings.addProperty("through_walls", aim.isThroughWalls());
        }
        
        return settings;
    }

    private void applyModuleSettings(Module module, JsonObject settings) {
        if (module instanceof com.czasclient.modules.movement.WhileSpeed) {
            com.czasclient.modules.movement.WhileSpeed speed = (com.czasclient.modules.movement.WhileSpeed) module;
            if (settings.has("speed")) speed.setSpeed(settings.get("speed").getAsDouble());
            if (settings.has("air_strafe")) speed.setAirStrafe(settings.get("air_strafe").getAsBoolean());
        } else if (module instanceof com.czasclient.modules.combat.AimAssist) {
            com.czasclient.modules.combat.AimAssist aim = (com.czasclient.modules.combat.AimAssist) module;
            if (settings.has("range")) aim.setRange(settings.get("range").getAsDouble());
            if (settings.has("speed")) aim.setSpeed(settings.get("speed").getAsFloat());
            if (settings.has("only_players")) aim.setOnlyPlayers(settings.get("only_players").getAsBoolean());
            if (settings.has("through_walls")) aim.setThroughWalls(settings.get("through_walls").getAsBoolean());
        }
    }

    private void loadGuiSettings(JsonObject guiConfig) {
        if (guiConfig.has("theme")) {
            String theme = guiConfig.get("theme").getAsString();
            applyTheme(theme);
        }
        
        if (guiConfig.has("language")) {
            String language = guiConfig.get("language").getAsString();
            setLanguage(language);
        }
    }

    private void applyTheme(String theme) {
        switch (theme.toLowerCase()) {
            case "red_black":
                ThemeManager.setTheme(ThemeManager.Theme.RED_BLACK);
                break;
            case "blue":
                ThemeManager.setTheme(ThemeManager.Theme.BLUE);
                break;
            case "green":
                ThemeManager.setTheme(ThemeManager.Theme.GREEN);
                break;
        }
    }

    private void setLanguage(String language) {
        LanguageManager.setLanguage(language);
    }

    public File getConfigDir() {
        return configDir;
    }

    public void resetConfig() {
        if (configFile.exists()) {
            configFile.delete();
            CzasClient.LOGGER.info("Configuration reset");
        }
    }
}
