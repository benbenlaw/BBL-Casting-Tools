package com.benbenlaw.castingtools.data;

import com.benbenlaw.casting.block.custom.CastingBlock;
import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.block.CastingToolsBlocks;
import com.benbenlaw.castingtools.fluids.CTFluids;
import com.benbenlaw.castingtools.item.CastingToolsItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.model.item.DynamicFluidContainerModel;
import net.neoforged.neoforge.common.NeoForgeMod;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static net.minecraft.client.data.models.BlockModelGenerators.*;

public class CTModelProvider extends ModelProvider {

    public CTModelProvider(PackOutput output) {
        super(output, CastingTools.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {

        //Blocks
        createMachineBlock(CastingToolsBlocks.MODIFIER.get(), blockModels.blockStateOutput, blockModels.modelOutput);
        blockModels.createTrivialCube(CastingToolsBlocks.OMNITHIUM_BLOCK.get());

        //Items
        itemModels.generateFlatItem(CastingToolsItems.OMNITHIUM_INGOT.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(CastingToolsItems.OMNITHIUM_NUGGET.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(CastingToolsItems.OMNITHIUM_PICKAXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(CastingToolsItems.OMNITHIUM_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(CastingToolsItems.OMNITHIUM_SHOVEL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(CastingToolsItems.OMNITHIUM_HOE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(CastingToolsItems.OMNITHIUM_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateSpear(CastingToolsItems.OMNITHIUM_SPEAR.get());
        itemModels.generateFlatItem(CastingToolsItems.OMNITHIUM_CHESTPLATE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(CastingToolsItems.OMNITHIUM_LEGGINGS.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(CastingToolsItems.OMNITHIUM_HELMET.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(CastingToolsItems.OMNITHIUM_BOOTS.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

        bucketItem(itemModels, CTFluids.MOLTEN_OMNITHIUM.getBucket(), CTFluids.MOLTEN_OMNITHIUM.getBucket().content, false, false);

        //Fluids
        blockModels.createNonTemplateModelBlock(CTFluids.MOLTEN_OMNITHIUM.getBlock());

    }

    public void createMachineBlock(Block block, Consumer<BlockModelDefinitionGenerator> blockStateOutput, BiConsumer<Identifier, ModelInstance> modelOutput) {
        TextureMapping idleTextureMapping = (new TextureMapping()).put(TextureSlot.TOP, new Material(CastingTools.identifier("block/modifier_top"))).put(TextureSlot.SIDE, new Material(CastingTools.identifier("block/modifier_side"))).put(TextureSlot.FRONT, TextureMapping.getBlockTexture(block, "_front"));
        TextureMapping workingTextureMapping = (new TextureMapping()).put(TextureSlot.TOP, new Material(CastingTools.identifier("block/modifier_top"))).put(TextureSlot.SIDE, new Material(CastingTools.identifier("block/modifier_side"))).put(TextureSlot.FRONT, TextureMapping.getBlockTexture(block, "_front_working"));

        MultiVariant multivariant = plainVariant(ModelTemplates.CUBE_ORIENTABLE.create(block, idleTextureMapping, modelOutput));
        MultiVariant multivariant1 = plainVariant(ModelTemplates.CUBE_ORIENTABLE_VERTICAL.create(block, idleTextureMapping, modelOutput));

        MultiVariant workingVariant = plainVariant(ModelTemplates.CUBE_ORIENTABLE.createWithSuffix(block, "_working", workingTextureMapping, modelOutput));
        MultiVariant workingVariant1 = plainVariant(ModelTemplates.CUBE_ORIENTABLE_VERTICAL.createWithSuffix(block, "_working", workingTextureMapping, modelOutput));

        blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(PropertyDispatch.initial(BlockStateProperties.FACING, CastingBlock.WORKING)
                                .select(Direction.DOWN, false, multivariant1.with(X_ROT_180))
                                .select(Direction.UP, false, multivariant1)
                                .select(Direction.NORTH, false, multivariant)
                                .select(Direction.EAST, false, multivariant.with(Y_ROT_90))
                                .select(Direction.SOUTH,false, multivariant.with(Y_ROT_180))
                                .select(Direction.WEST,false, multivariant.with(Y_ROT_270))
                                .select(Direction.DOWN, true, workingVariant1.with(X_ROT_180))
                                .select(Direction.UP, true, workingVariant1)
                                .select(Direction.NORTH, true, workingVariant)
                                .select(Direction.EAST, true, workingVariant.with(Y_ROT_90))
                                .select(Direction.SOUTH,true, workingVariant.with(Y_ROT_180))
                                .select(Direction.WEST,true, workingVariant.with(Y_ROT_270))));

    }

    public void bucketItem(ItemModelGenerators itemModelGenerators, BucketItem item, Fluid fluid, boolean flipGas, boolean applyFluidLuminosity) {
        Material drip = new Material(Identifier.fromNamespaceAndPath(NeoForgeMod.MOD_ID, "item/mask/bucket_fluid_drip"));
        Material bucket = new Material(Identifier.withDefaultNamespace("item/bucket"));
        DynamicFluidContainerModel.Textures textures = new DynamicFluidContainerModel.Textures(Optional.empty(), Optional.of(bucket), Optional.of(drip), Optional.empty());
        itemModelGenerators.itemModelOutput.accept(item, new DynamicFluidContainerModel.Unbaked(textures, fluid, flipGas, false, applyFluidLuminosity));
    }
}
