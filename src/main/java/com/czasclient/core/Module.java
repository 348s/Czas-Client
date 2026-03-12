package com.czasclient.core;

import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public abstract class Module {
    protected final Minecraft mc = Minecraft.getInstance();
    protected String name;
    protected String description;
    protected int key;
    protected boolean enabled;
    protected Category category;

    public Module(String name, String description, int key, Category category) {
        this.name = name;
        this.description = description;
        this.key = key;
        this.category = category;
        this.enabled = false;
    }

    public abstract void onEnable();
    public abstract void onDisable();
    public abstract void onTick();

    public void toggle() {
        enabled = !enabled;
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getKey() { return key; }
    public boolean isEnabled() { return enabled; }
    public Category getCategory() { return category; }

    public enum Category {
        MOVEMENT("Movement"),
        COMBAT("Combat"),
        UTILITIES("Utilities"),
        WORLD("World"),
        RENDER("Render");

        private final String displayName;

        Category(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
