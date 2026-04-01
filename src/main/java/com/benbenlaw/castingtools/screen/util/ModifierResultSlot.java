package com.benbenlaw.castingtools.screen.util;

import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierRegistry;
import com.benbenlaw.castingtools.utils.ModifierUtils;
import com.benbenlaw.core.block.entity.handler.fluid.InputFluidHandler;
import com.benbenlaw.core.block.entity.handler.item.InputItemHandler;
import com.benbenlaw.core.block.entity.handler.item.OutputItemHandler;
import com.benbenlaw.core.screen.util.slot.ResultSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import javax.swing.*;

public class ModifierResultSlot extends ResultSlot {


    private final InputItemHandler inputHandler;
    private final InputFluidHandler fluidHandler;

    public ModifierResultSlot(InputItemHandler inputHandler, InputFluidHandler fluidHandler, OutputItemHandler outputItemHandler, IndexModifier<ItemResource> slotModifier, int index, int xPosition, int yPosition) {
        super(outputItemHandler, slotModifier, index, xPosition, yPosition);
        this.inputHandler = inputHandler;
        this.fluidHandler = fluidHandler;
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

        Modifier modifier = ModifierUtils.getMatchingModifier(toolStack, ingredientStack);

        if (modifier != null) {
            try (Transaction tx = Transaction.open(null)) {

                int expCost = modifier.getExperienceCost();
                if (expCost > 0) {
                    FluidStack expStack = FluidUtil.getStack(fluidHandler, 0);
                    fluidHandler.extractInternal(0, FluidResource.of(expStack), expCost, tx);
                }

                modifier.getFluidIngredient().ifPresent(fluidIng -> {
                    FluidStack recipeStack = FluidUtil.getStack(fluidHandler, 1);
                    fluidHandler.extractInternal(1, FluidResource.of(recipeStack), (int) fluidIng.amount(), tx);
                });

                modifier.getIngredient().ifPresent(ingredient -> {
                    inputHandler.extractInternal(0, ItemResource.of(ingredientStack), ingredient.count(), tx);
                });

                inputHandler.extractInternal(1, ItemResource.of(toolStack), 1, tx);

                tx.commit();
            }
        }

        setChanged();
        super.onTake(player, carried);
    }
}
