package com.yampej.module.modules.combat;

import com.yampej.module.Category;
import com.yampej.module.Module;
import com.yampej.module.setting.SliderSetting;

public class AntiKnockback extends Module {
    public final SliderSetting horizontal = addSetting(new SliderSetting("Horizontal", 1.0, 0.0, 1.0, 0.1));
    public final SliderSetting vertical   = addSetting(new SliderSetting("Vertical",   1.0, 0.0, 1.0, 0.1));

    public AntiKnockback() {
        super("AntiKnockback", "Reduces knockback taken", Category.COMBAT);
    }
}
