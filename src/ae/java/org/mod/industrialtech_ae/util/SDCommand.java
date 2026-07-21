package org.mod.industrialtech_ae.util;

import appeng.api.config.Actionable;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import org.mod.industrialtech_ae.ae2.GenericAeKeyStorage;
import org.mod.industrialtech_ae.entity.BaseBlockEntity;
import org.mod.industrialtech_ae.entity.BulkFluidTankBlockEntity;
import org.mod.industrialtech_ae.entity.BulkItemChestBlockEntity;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.registries.ForgeRegistries;

public class SDCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext ctx) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder) Commands.literal("sd").requires((source) -> source.hasPermission(2))).then(Commands.literal("i").executes(org.mod.industrialtech_ae.util.SDCommand::showInfo))).then(Commands.literal("sa").then(Commands.argument("amount", LongArgumentType.longArg(0L)).executes(org.mod.industrialtech_ae.util.SDCommand::setAmount)))).then(((LiteralArgumentBuilder)Commands.literal("sf").then(Commands.literal("i").then(Commands.argument("item", ItemArgument.item(ctx)).executes(org.mod.industrialtech_ae.util.SDCommand::setItemFilter)))).then(Commands.literal("f").then(Commands.argument("fluid", ResourceLocationArgument.id()).executes(org.mod.industrialtech_ae.util.SDCommand::setFluidFilter)))));
    }

    private static int showInfo(CommandContext<CommandSourceStack> ctx) {
        BaseBlockEntity<?> be = getLookedAtTank(ctx);
        if (be == null) {
            return 0;
        } else {
            GenericAeKeyStorage<?> storage = be.getStorage();
            ((CommandSourceStack)ctx.getSource()).sendSuccess(() -> Component.literal("§6--- Storage Info ---"), false);
            ((CommandSourceStack)ctx.getSource()).sendSuccess(() -> {
                String var10000 = storage.getFilterKey() != null ? storage.getFilterKey().getDisplayName().getString() : "None";
                return Component.literal("Filter: §e" + var10000);
            }, false);
            ((CommandSourceStack)ctx.getSource()).sendSuccess(() -> {
                String var10000 = storage.getStoredKey() != null ? storage.getStoredKey().getDisplayName().getString() : "None";
                return Component.literal("Stored: §e" + var10000);
            }, false);
            ((CommandSourceStack)ctx.getSource()).sendSuccess(() -> Component.literal("Amount: §b" + storage.getAmount()), false);
            ((CommandSourceStack)ctx.getSource()).sendSuccess(() -> Component.literal("Online: " + (be.isOnline() ? "§aYES" : "§cNO")), false);
            return 1;
        }
    }

    private static int setAmount(CommandContext<CommandSourceStack> ctx) {
        BaseBlockEntity<?> be = getLookedAtTank(ctx);
        if (be == null) {
            return 0;
        } else {
            GenericAeKeyStorage<?> storage = be.getStorage();
            long targetAmount = LongArgumentType.getLong(ctx, "amount");
            AEKey filter = storage.getFilterKey();
            if (filter == null) {
                ((CommandSourceStack)ctx.getSource()).sendFailure(Component.literal("Cannot set amount: No filter set on tank!"));
                return 0;
            } else {
                storage.load(new CompoundTag());
                if (targetAmount > 0L) {
                    long inserted = storage.insert(filter, targetAmount, Actionable.MODULATE);
                    ((CommandSourceStack)ctx.getSource()).sendSuccess(() -> Component.literal(String.format("§aSet amount to §b%d§a of §e%s", inserted, filter.getDisplayName().getString())), true);
                } else {
                    ((CommandSourceStack)ctx.getSource()).sendSuccess(() -> Component.literal("§aTank cleared."), true);
                }

                be.onStorageChanged();
                return 1;
            }
        }
    }

    private static int setItemFilter(CommandContext<CommandSourceStack> ctx) {
        BaseBlockEntity<?> be = getLookedAtTank(ctx);
        if (be instanceof BulkItemChestBlockEntity itemTank) {
            try {
                Item item = ItemArgument.getItem(ctx, "item").getItem();
                AEItemKey key = AEItemKey.of(item);
                itemTank.getStorage().setFilter(key);
                itemTank.onStorageChanged();
                ((CommandSourceStack)ctx.getSource()).sendSuccess(() -> Component.literal("§aItem filter set to: " + key.getDisplayName().getString()), true);
            } catch (Exception e) {
                ((CommandSourceStack)ctx.getSource()).sendFailure(Component.literal("Error: " + e.getMessage()));
            }

            return 1;
        } else {
            ((CommandSourceStack)ctx.getSource()).sendFailure(Component.literal("Not an Item Chest!"));
            return 0;
        }
    }

    private static int setFluidFilter(CommandContext<CommandSourceStack> ctx) {
        BaseBlockEntity<?> be = getLookedAtTank(ctx);
        if (be instanceof BulkFluidTankBlockEntity fluidTank) {
            ResourceLocation id = ResourceLocationArgument.getId(ctx, "fluid");
            Fluid fluid = (Fluid) ForgeRegistries.FLUIDS.getValue(id);
            if (fluid != null && !fluid.isSame(Fluids.EMPTY)) {
                AEFluidKey key = AEFluidKey.of(fluid);
                fluidTank.getStorage().setFilter(key);
                fluidTank.onStorageChanged();
                ((CommandSourceStack)ctx.getSource()).sendSuccess(() -> Component.literal("§aFluid filter set to: " + key.getDisplayName().getString()), true);
                return 1;
            } else {
                ((CommandSourceStack)ctx.getSource()).sendFailure(Component.literal("Fluid not found: " + String.valueOf(id)));
                return 0;
            }
        } else {
            ((CommandSourceStack)ctx.getSource()).sendFailure(Component.literal("Not a Fluid Tank!"));
            return 0;
        }
    }

    private static BaseBlockEntity<?> getLookedAtTank(CommandContext<CommandSourceStack> ctx) {
        ServerPlayer player = ((CommandSourceStack)ctx.getSource()).getPlayer();
        if (player == null) {
            return null;
        } else {
            HitResult hit = player.pick((double)5.0F, 0.0F, false);
            if (hit.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = ((BlockHitResult)hit).getBlockPos();
                BlockEntity be = player.level().getBlockEntity(pos);
                if (be instanceof BaseBlockEntity) {
                    BaseBlockEntity<?> tank = (BaseBlockEntity)be;
                    return tank;
                }
            }

            ((CommandSourceStack)ctx.getSource()).sendFailure(Component.literal("You must be looking at a Bulk Tank!"));
            return null;
        }
    }
}
