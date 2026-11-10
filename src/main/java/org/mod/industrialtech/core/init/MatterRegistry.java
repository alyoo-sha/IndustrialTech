package org.mod.industrialtech.core.init;

import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import org.mod.industrialtech.Industrialtech;
import org.mod.industrialtech.common.matter.LongEnergy;


public final class MatterRegistry {
    private MatterRegistry() {}

    public static void register() {
        Industrialtech.modEventBus.addListener(MatterRegistry::onRegisterCapabilities);
    }

    private static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(LongEnergy.Handler.class);
    }
}