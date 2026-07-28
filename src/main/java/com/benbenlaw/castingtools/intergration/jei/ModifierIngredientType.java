package com.benbenlaw.castingtools.intergration.jei;

import com.benbenlaw.castingtools.modifier.Modifier;
import mezz.jei.api.ingredients.IIngredientType;
import org.jspecify.annotations.NonNull;

public class ModifierIngredientType implements IIngredientType<Modifier> {

    public static final ModifierIngredientType INSTANCE = new ModifierIngredientType();

    private ModifierIngredientType() {}

    @Override
    public @NonNull Class<? extends Modifier> getIngredientClass() {
        return Modifier.class;
    }
}