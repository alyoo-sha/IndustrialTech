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
    private static final Object2ObjectMap<String, String> MOD_MIXINS = new Object2ObjectOpenHashMap<>(new String[]{"org.mod.industrialtech_ae.mixin.MixinItemDefinition", "org.mod.industrialtech_ae.mixin.MixinPatternEncodingTermMenu", "org.mod.industrialtech_ae.mixin.MixinRestrictedInputSlot"}, new String[]{"infinitypattern", "infinitypattern", "infinitypattern"}, 0.75F);

    public AEMixinPlugin() {
    }

    public void onLoad(String s) {
    }

    public String getRefMapperConfig() {
        return null;
    }

    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return !MOD_MIXINS.containsKey(mixinClassName) || isModLoaded(MOD_MIXINS.get(mixinClassName));
    }

    public void acceptTargets(Set<String> set, Set<String> set1) {
    }

    public List<String> getMixins() {
        return null;
    }

    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {
    }

    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {
    }

    private static boolean isModLoaded(String modId) {
        if (ModList.get() == null) {
            Stream<String> var10000 = LoadingModList.get().getMods().stream().map(IModInfo::getModId);
            Objects.requireNonNull(modId);
            return var10000.anyMatch(modId::equals);
        } else {
            return ModList.get().isLoaded(modId);
        }
    }
}
