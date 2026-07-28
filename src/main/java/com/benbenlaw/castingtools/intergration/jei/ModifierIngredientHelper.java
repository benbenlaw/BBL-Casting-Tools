package com.benbenlaw.castingtools.intergration.jei;

import com.benbenlaw.castingtools.modifier.Modifier;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.Identifier;

import javax.annotation.Nullable;

public class ModifierIngredientHelper implements IIngredientHelper<Modifier> {

    @Override
    public IIngredientType<Modifier> getIngredientType() {
        return ModifierIngredientType.INSTANCE;
    }

    @Override
    public String getDisplayName(Modifier ingredient) {
        return ingredient.getDisplayName().getString();
    }

    @Override
    public Object getUid(Modifier ingredient, UidContext context) {
        return ingredient.getId();
    }

    @Override
    public Identifier getIdentifier(Modifier ingredient) {
        return ingredient.getId();
    }

    @Override
    public Modifier copyIngredient(Modifier ingredient) {
        return ingredient;
    }

    @Override
    public String getErrorInfo(@Nullable Modifier ingredient) {
        return ingredient == null ? "null modifier" : String.valueOf(ingredient.getId());
    }
}