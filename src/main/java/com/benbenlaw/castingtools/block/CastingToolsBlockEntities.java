package com.benbenlaw.castingtools.block;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.block.entity.ModifierBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CastingToolsBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, CastingTools.MOD_ID);

    public static final Supplier<BlockEntityType<ModifierBlockEntity>> MODIFIER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("modifier_block_entity", () ->
                    new BlockEntityType<>(ModifierBlockEntity::new, CastingToolsBlocks.MODIFIER.get()));

}
