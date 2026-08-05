package com.benbenlaw.castingtools.item;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.SmithingTemplateItem;

import java.util.List;

public class OmnithiumSmithingTemplateItem extends SmithingTemplateItem {

    public OmnithiumSmithingTemplateItem(Component appliesTo, Component ingredients, Component baseSlotDescription, Component additionsSlotDescription, List<Identifier> baseSlotEmptyIcons, List<Identifier> additionalSlotEmptyIcons, Properties properties) {
        super(appliesTo, ingredients, baseSlotDescription, additionsSlotDescription, baseSlotEmptyIcons, additionalSlotEmptyIcons, properties);
    }

    public  static OmnithiumSmithingTemplateItem createOmnithiumUpgradeTemplate(Properties properties) {
        return new OmnithiumSmithingTemplateItem(
                Component.translatable("item.castingtools.omnithium_upgrade_smithing_template.applies_to"),
                Component.translatable("item.castingtools.omnithium_upgrade_smithing_template.ingredients"),
                Component.translatable("item.castingtools.omnithium_upgrade_smithing_template.base_slot_description"),
                Component.translatable("item.castingtools.omnithium_upgrade_smithing_template.additions_slot_description"),
                createEmptyIconsList(),
                List.of(Identifier.withDefaultNamespace("container/slot/ingot")),
                properties
        );
    }

    private static List<Identifier> createEmptyIconsList() {
        return List.of(
                Identifier.withDefaultNamespace("container/slot/helmet"),
                Identifier.withDefaultNamespace("container/slot/chestplate"),
                Identifier.withDefaultNamespace("container/slot/leggings"),
                Identifier.withDefaultNamespace("container/slot/boots"),
                Identifier.withDefaultNamespace("container/slot/hoe"),
                Identifier.withDefaultNamespace("container/slot/axe"),
                Identifier.withDefaultNamespace("container/slot/sword"),
                Identifier.withDefaultNamespace("container/slot/shovel"),
                Identifier.withDefaultNamespace("container/slot/spear"),
                Identifier.withDefaultNamespace("container/slot/pickaxe")
        );
    }

}
