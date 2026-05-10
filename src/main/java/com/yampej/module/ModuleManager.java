package com.yampej.module;

import com.yampej.module.modules.combat.*;
import com.yampej.module.modules.render.*;
import com.yampej.module.modules.misc.*;
import com.yampej.module.modules.hud.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {
    private static final List<Module> modules = new ArrayList<>();

    public static void init() {
        // Combat
        register(new Criticals());
        register(new AutoTotem());
        register(new AntiKnockback());
        register(new AutoArmor());
        register(new Trigger());

        // Render
        register(new ESP());
        register(new Tracers());
        register(new FullBright());
        register(new Xray());
        register(new HoleESP());
        register(new Nametag());

        // Misc
        register(new ChatSuffix());
        register(new DiscordRPC());
        register(new MiddleClickFriend());
        register(new NameProtect());
        register(new ServerSpoof());
        register(new PacketCanceller());

        // HUD
        register(new HUD());
        register(new Watermark());
        register(new ActiveModules());
        register(new CombatInfo());
        register(new Coordinates());
        register(new FPSModule());
        register(new PingModule());
    }

    private static void register(Module module) { modules.add(module); }

    public static List<Module> getAll() { return modules; }

    public static List<Module> getByCategory(Category cat) {
        return modules.stream().filter(m -> m.getCategory() == cat).collect(Collectors.toList());
    }

    public static List<Module> getEnabled() {
        return modules.stream().filter(Module::isEnabled).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public static <T extends Module> T get(Class<T> clazz) {
        return (T) modules.stream().filter(m -> m.getClass() == clazz).findFirst().orElse(null);
    }

    public static void onTick() {
        modules.stream().filter(Module::isEnabled).forEach(Module::onTick);
    }
}
