package com.benbenlaw.castingtools.block;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.block.entity.ModifierBlockEntity;
import com.benbenlaw.castingtools.block.entity.UpgraderBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CTBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, CastingTools.MOD_ID);

    public static final Supplier<BlockEntityType<ModifierBlockEntity>> MODIFIER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("modifier_block_entity", () ->
                    new BlockEntityType<>(ModifierBlockEntity::new, CTBlocks.MODIFIER.get()));

    public static final Supplier<BlockEntityType<UpgraderBlockEntity>> UPGRADER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("upgrader_block_entity", () ->
                    new BlockEntityType<>(UpgraderBlockEntity::new, CTBlocks.UPGRADER.get()));

}
