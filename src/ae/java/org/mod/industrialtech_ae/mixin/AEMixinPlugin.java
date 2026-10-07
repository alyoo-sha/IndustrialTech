package org.mod.industrialtech_ae.mixin;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.forgespi.language.IModInfo;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class AEMixinPlugin implements IMixinConfigPlugin {
    private static final Object2ObjectMap<String, String> MOD_MIXINS = new Object2ObjectOpenHashMap();

    public void onLoad(String mixinPackage) {
    }

    public String getRefMapperConfig() {
        return null;
    }

    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return !MOD_MIXINS.containsKey(mixinClassName) || isModLoaded((String) MOD_MIXINS.get(mixinClassName));
    }

    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    public List<String> getMixins() {
        return null;
    }

    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    private static boolean isModLoaded(String modId) {
        return isModLoadedInternal("ae2") || isModLoadedInternal("appliedenergistics2");
    }

    private static boolean isModLoadedInternal(String modId) {
        if (ModList.get() == null) {
            Stream var10000 = LoadingModList.get().getMods().stream().map((modInfo) -> modInfo.getModId());
            Objects.requireNonNull(modId);
            return var10000.anyMatch(modId::equals);
        } else {
            return ModList.get().isLoaded(modId);
        }
    }

    static {
        MOD_MIXINS.put("org.mod.industrialtech_ae.mixin.MixinPatternEncodingTermMenu", "ae2");
        MOD_MIXINS.put("org.mod.industrialtech_ae.mixin.MixinRestrictedInputSlot", "ae2");
        MOD_MIXINS.put("org.mod.industrialtech_ae.mixin.MixinPatternEncodingTermMenu", "appliedenergistics2");
        MOD_MIXINS.put("org.mod.industrialtech_ae.mixin.MixinRestrictedInputSlot", "appliedenergistics2");
    }
}
