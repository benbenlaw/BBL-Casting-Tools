package com.benbenlaw.castingtools.screen.util;

import com.benbenlaw.castingtools.config.CTServerConfig;
import com.benbenlaw.castingtools.item.CTDataComponent;
import com.benbenlaw.castingtools.item.ModifierComponent;
import com.benbenlaw.castingtools.screen.UpgraderMenu;
import com.benbenlaw.core.block.entity.handler.fluid.SyncableFluidHandler;
import com.benbenlaw.core.block.entity.handler.item.SyncableItemHandler;
import com.benbenlaw.core.screen.util.slot.ResultSlot;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.Optional;

public class UpgraderResultSlot extends ResultSlot {

    private final SyncableItemHandler inputHandler;
    private final SyncableFluidHandler fluidHandler;
    private final Level level;

    public UpgraderResultSlot(SyncableItemHandler inputHandler, SyncableFluidHandler fluidHandler, SyncableItemHandler outputItemHandler, IndexModifier<ItemResource> slotModifier, int index, int xPosition, int yPosition, Level level) {
        super(outputItemHandler, slotModifier, index, xPosition, yPosition);
        this.inputHandler = inputHandler;
        this.fluidHandler = fluidHandler;
        this.level = level;
    }

    @Override
    public boolean mayPickup(Player player) {
        ItemStack stack = this.getItem();
        if (stack.is(Items.BARRIER)) {
            return false;
        }
        return super.mayPickup(player);
    }

    @Override
    public void onTake(Player player, ItemStack carried) {
        ItemStack ingredientStack = ItemUtil.getStack(inputHandler, 0);
        ItemStack toolStack = ItemUtil.getStack(inputHandler, 1);
        FluidStack tankFluid = FluidUtil.getStack(fluidHandler, 1);
        FluidStack expFluid = FluidUtil.getStack(fluidHandler, 0);

        UpgraderMenu.UpgradeResult upgradeResult = UpgraderMenu.findUpgradeMatch(level, toolStack, ingredientStack, tankFluid, expFluid);

        if (upgradeResult.status() == UpgraderMenu.UpgradeStatus.SUCCESS) {
            UpgraderMenu.UpgradeMatch match = upgradeResult.match().get();

            try (Transaction tx = Transaction.openRoot()) {

                int expCost = CTServerConfig.costForUpgrader.get();
                fluidHandler.runInternal(() -> {
                    fluidHandler.extract(0, FluidResource.of(expFluid), expCost, tx);
                });

                if (match.requiredItemCount() > 0) {
                    inputHandler.runInternal(() -> {
                        inputHandler.extract(0, ItemResource.of(ingredientStack), match.requiredItemCount(), tx);
                    });
                }

                if (match.requiredFluidAmount() > 0) {
                    fluidHandler.runInternal(() -> {
                        fluidHandler.extract(1, FluidResource.of(tankFluid), match.requiredFluidAmount(), tx);
                    });
                }

                inputHandler.runInternal(() -> {
                    inputHandler.extract(1, ItemResource.of(toolStack), 1, tx);
                });

                tx.commit();
            }
        }

        setChanged();
        super.onTake(player, carried);
    }
}