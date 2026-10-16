package org.mod.industrialtech;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Industrialtech.MOD_ID)
public class Config {


//    @SubscribeEvent
//    public static void onLivingUpdate(TickEvent.PlayerTickEvent event) {
//        if (event.phase != TickEvent.Phase.END || !event.player.level().isClientSide)
//            return;
//
//        LocalPlayer player = (LocalPlayer) event.player;
//
//        boolean jumping = (player.getDeltaMovement().y > 0 || player.input.jumping) // for 2 block gaps where the player hits the ceiling
//                && !player.onGround();
//        if (lastJumping != jumping) {
//            lastJumping = jumping;
//            if (jumping) {
//                tryTeleport(player, Direction.UP);
//            }
//        }
//
//        boolean crouching = player.isCrouching();
//        if (lastSneaking != crouching) {
//            lastSneaking = crouching;
//            if (crouching) {
//                tryTeleport(player, Direction.DOWN);
//            }
//        }
//    }

    // Config specification for Forge
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC = BUILDER.build();
}
