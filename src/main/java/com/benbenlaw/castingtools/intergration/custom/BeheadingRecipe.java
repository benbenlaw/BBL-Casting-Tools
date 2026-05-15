package com.benbenlaw.castingtools.intergration.custom;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStackTemplate;

public record BeheadingRecipe(EntityType<?> entity, ItemStackTemplate head) {
}
