package com.benbenlaw.castingtools.block.entity;

import com.benbenlaw.casting.block.CastingBlockEntities;
import com.benbenlaw.casting.block.custom.CastingBlock;
import com.benbenlaw.casting.block.custom.SolidifierBlock;
import com.benbenlaw.casting.block.entity.FluidAccepting;
import com.benbenlaw.casting.block.entity.SolidifierBlockEntity;
import com.benbenlaw.casting.block.entity.TankBlockEntity;
import com.benbenlaw.casting.config.CastingConfig;
import com.benbenlaw.casting.item.CastingDataComponents;
import com.benbenlaw.casting.item.util.FluidListComponent;
import com.benbenlaw.casting.recipe.custom.SolidifierRecipe;
import com.benbenlaw.casting.screen.SolidifierMenu;
import com.benbenlaw.castingtools.block.CastingToolsBlockEntities;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierRegistry;
import com.benbenlaw.castingtools.screen.ModifierMenu;
import com.benbenlaw.castingtools.utils.ModifierUtils;
import com.benbenlaw.core.block.entity.SyncableBlockEntity;
import com.benbenlaw.core.block.entity.handler.fluid.FilterFluidHandler;
import com.benbenlaw.core.block.entity.handler.fluid.InputFluidHandler;
import com.benbenlaw.core.block.entity.handler.item.CombinedItemHandler;
import com.benbenlaw.core.block.entity.handler.item.InputItemHandler;
import com.benbenlaw.core.block.entity.handler.item.OutputItemHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.Optional;
import java.util.OptionalInt;

public class ModifierBlockEntity extends SyncableBlockEntity implements MenuProvider, FluidAccepting {

    private final InputItemHandler inputHandler = new InputItemHandler(this, 2, (i, stack) -> true);
    private final InputFluidHandler inputFluidHandler = new InputFluidHandler(this, 2, 16000, (i, stack) -> {
        if (i == 0) {
            TagKey<Fluid> experienceTag = TagKey.create(Registries.FLUID, Identifier.fromNamespaceAndPath("c", "experience"));
            return stack.is(experienceTag);
        }
        return i == 1;
    });

    private final OutputItemHandler outputHandler = new OutputItemHandler(this, 1, i -> i == 0);

    public ModifierBlockEntity(BlockPos pos, BlockState state) {
        super(CastingToolsBlockEntities.MODIFIER_BLOCK_ENTITY.get(), pos, state);
    }

    public void tick() {
    }

    public boolean onPlayerUse(Player player, InteractionHand hand) {
        return FluidUtil.interactWithFluidHandler(player, hand, this.worldPosition, inputFluidHandler);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        inputHandler.serialize(output.child("input"));
        inputFluidHandler.serialize(output.child("inputFluid"));
        outputHandler.serialize(output.child("output"));

        super.saveAdditional(output);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        inputHandler.deserialize(input.childOrEmpty("input"));
        inputFluidHandler.deserialize(input.childOrEmpty("inputFluid"));
        outputHandler.deserialize(input.childOrEmpty("output"));

        super.loadAdditional(input);
    }

    public InputItemHandler getInputHandler() { return inputHandler; }
    public InputFluidHandler getInputFluidHandler() { return inputFluidHandler; }
    public OutputItemHandler getOutputHandler() { return outputHandler; }
    public ResourceHandler<ItemResource> getItemCapability() { return inputHandler; }
    public ResourceHandler<FluidResource> getFluidCapability() { return inputFluidHandler; }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int container, @NonNull Inventory inventory, @NonNull Player player) {
        return new ModifierMenu(container, inventory, this.worldPosition, new SimpleContainerData(0));
    }

    @Override
    public @NonNull Component getDisplayName() {
        return Component.translatable("block.castingtools.modifier");
    }

    @Override
    public void preRemoveSideEffects(@NonNull BlockPos pos, @NonNull BlockState state) {
        dropInventoryContents(inputHandler);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.@NonNull Builder builder) {
        super.collectImplicitComponents(builder);
        builder.set(CastingDataComponents.FLUIDS.get(), FluidListComponent.fromHandlers(inputFluidHandler));
    }

    @Override
    protected void applyImplicitComponents(@NonNull DataComponentGetter components) {
        super.applyImplicitComponents(components);
        FluidListComponent component = components.get(CastingDataComponents.FLUIDS.get());
        if (component != null) {
            component.applyToHandlers(inputFluidHandler);
        }
    }

    @Override
    public InputFluidHandler receivingHandler() {
        return inputFluidHandler;
    }
}