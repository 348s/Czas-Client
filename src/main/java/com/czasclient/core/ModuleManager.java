package com.czasclient.core;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private static ModuleManager instance;
    private final List<Module> modules;

    public ModuleManager() {
        this.modules = new ArrayList<>();
        instance = this;
        initModules();
    }

    public static ModuleManager getInstance() {
        return instance;
    }

    private void initModules() {
        modules.add(new modules.movement.WhileSpeed());
        modules.add(new modules.movement.FakeCreative());
        modules.add(new modules.combat.Nuker());
        modules.add(new modules.combat.AimAssist());
        modules.add(new modules.movement.Spider());
        modules.add(new modules.movement.LongJump());
        modules.add(new modules.movement.NoFall());
        modules.add(new modules.utilities.AutoRegen());
        modules.add(new modules.utilities.InventoryWalk());
        modules.add(new modules.world.FastBreak());
        modules.add(new modules.world.Timer());
        modules.add(new modules.world.Scaffold());
        modules.add(new modules.utilities.AutoArmor());
        modules.add(new modules.combat.NoSlow());
        modules.add(new modules.render.FullBright());
        modules.add(new modules.combat.AutoClicker());
    }

    public List<Module> getModules() {
        return modules;
    }

    public Module getModule(String name) {
        for (Module module : modules) {
            if (module.getName().equalsIgnoreCase(name)) {
                return module;
            }
        }
        return null;
    }

    public List<Module> getModulesByCategory(Module.Category category) {
        List<Module> categoryModules = new ArrayList<>();
        for (Module module : modules) {
            if (module.getCategory() == category) {
                categoryModules.add(module);
            }
        }
        return categoryModules;
    }

    public void onTick() {
        for (Module module : modules) {
            if (module.isEnabled()) {
                try {
                    module.onTick();
                } catch (Exception e) {
                    CzasClient.LOGGER.error("Error in module " + module.getName() + ": " + e.getMessage());
                    module.toggle();
                }
            }
        }
    }
}
