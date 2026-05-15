package com.benbenlaw.castingtools.event.client;

import com.benbenlaw.castingtools.intergration.custom.BeheadingRecipe;
import com.benbenlaw.castingtools.intergration.custom.PulverizingRecipe;
import com.benbenlaw.castingtools.intergration.custom.TreasureRecipe;
import net.minecraft.resources.Identifier;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ClientRecipeCache {

    //Beheading
    public static Map<Identifier, BeheadingRecipe> beheadingRecipes = new HashMap<>();

    public static void setCachedBeheadingRecipes(Map<Identifier, BeheadingRecipe> recipes) {
        beheadingRecipes = recipes;
    }

    public static Collection<BeheadingRecipe> getCachedBeheadingRecipes() {
        return beheadingRecipes.values();
    }

    //Pulverizing
    public static Map<Identifier, PulverizingRecipe> pulverizingRecipes = new HashMap<>();

    public static void setCachedPulverizingRecipes(Map<Identifier, PulverizingRecipe> recipes) {
        pulverizingRecipes = recipes;
    }

    public static Collection<PulverizingRecipe> getCachedPulverizingRecipes() {
        return pulverizingRecipes.values();
    }

    //Treasure
    public static Map<Identifier, TreasureRecipe> treasureRecipes = new HashMap<>();

    public static void setCachedTreasureRecipes(Map<Identifier, TreasureRecipe> recipes) {
        treasureRecipes = recipes;
    }

    public static Collection<TreasureRecipe> getCachedTreasureRecipes() {
        return treasureRecipes.values();
    }

}
