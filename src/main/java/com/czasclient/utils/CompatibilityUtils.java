package com.czasclient.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CompatibilityUtils {
    private static final Logger LOGGER = LogManager.getLogger("CzasClient/Compatibility");
    
    private static final String[] SUPPORTED_VERSIONS = {
        "1.18.2", "1.19", "1.19.1", "1.19.2", "1.19.3", "1.19.4",
        "1.20", "1.20.1", "1.20.2", "1.20.3", "1.20.4", "1.20.5", "1.20.6",
        "1.21", "1.21.1", "1.21.2", "1.21.3", "1.21.4"
    };

    public static boolean isVersionSupported() {
        String minecraftVersion = Minecraft.getInstance().getVersion();
        
        for (String supportedVersion : SUPPORTED_VERSIONS) {
            if (minecraftVersion.startsWith(supportedVersion)) {
                LOGGER.info("Minecraft version " + minecraftVersion + " is supported");
                return true;
            }
        }
        
        LOGGER.warn("Minecraft version " + minecraftVersion + " may not be fully supported");
        return false;
    }

    public static String getMinecraftVersion() {
        try {
            return Minecraft.getInstance().getVersion();
        } catch (Exception e) {
            LOGGER.error("Failed to get Minecraft version: " + e.getMessage());
            return "Unknown";
        }
    }

    public static void logCompatibilityInfo() {
        String version = getMinecraftVersion();
        boolean supported = isVersionSupported();
        
        LOGGER.info("=== Czas Client Compatibility Check ===");
        LOGGER.info("Minecraft Version: " + version);
        LOGGER.info("Supported: " + (supported ? "Yes" : "No"));
        LOGGER.info("Supported Versions: " + String.join(", ", SUPPORTED_VERSIONS));
        LOGGER.info("========================================");
    }

    public static boolean isCompatibleFeature(String feature) {
        String version = getMinecraftVersion();
        
        if (version.startsWith("1.18")) {
            return isCompatibleWith118(feature);
        } else if (version.startsWith("1.19")) {
            return isCompatibleWith119(feature);
        } else if (version.startsWith("1.20")) {
            return isCompatibleWith120(feature);
        } else if (version.startsWith("1.21")) {
            return isCompatibleWith121(feature);
        }
        
        return false;
    }

    private static boolean isCompatibleWith118(String feature) {
        switch (feature.toLowerCase()) {
            case "movement":
            case "combat":
            case "utilities":
            case "world":
            case "render":
                return true;
            default:
                return false;
        }
    }

    private static boolean isCompatibleWith119(String feature) {
        return isCompatibleWith118(feature);
    }

    private static boolean isCompatibleWith120(String feature) {
        return isCompatibleWith118(feature);
    }

    private static boolean isCompatibleWith121(String feature) {
        return isCompatibleWith118(feature);
    }

    public static String formatMessage(String message) {
        return TextFormatting.RED + "[Czas] " + TextFormatting.WHITE + message;
    }
}
