package com.tatadesmod.module;

import com.tatadesmod.module.impl.AutoAimModule;
import com.tatadesmod.module.impl.AutoClickerModule;
import com.tatadesmod.module.impl.ESPModule;
import com.tatadesmod.module.impl.FreecamModule;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private static ModuleManager instance;
    private final List<Module> modules = new ArrayList<>();

    private ModuleManager() {
        // register modules
        modules.add(new AutoClickerModule());
        modules.add(new AutoAimModule());
        modules.add(new ESPModule());
        modules.add(new FreecamModule());
    }

    public static ModuleManager get() {
        if (instance == null) instance = new ModuleManager();
        return instance;
    }

    public List<Module> getModules() { return modules; }

    public <T extends Module> T getModule(Class<T> cls) {
        for (Module m : modules) if (cls.isInstance(m)) return cls.cast(m);
        return null;
    }

    public void tickAll() {
        for (Module m : modules) if (m.isEnabled()) m.tick();
    }
}
