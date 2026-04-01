package com.benbenlaw.castingtools.intergration.jei;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.casting.block.CastingBlocks;
import com.benbenlaw.casting.event.client.ClientRecipeCache;
import com.benbenlaw.casting.recipe.custom.SolidifierRecipe;
import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.block.CastingToolsBlockEntities;
import com.benbenlaw.castingtools.block.CastingToolsBlocks;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.core.util.MouseUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ModifierRecipeCategory implements IRecipeCategory<ModifierRecipe> {

    public final static Identifier TEXTURE = CastingTools.identifier("textures/gui/modifier_jei.png");
    public static final IRecipeType<ModifierRecipe> RECIPE_TYPE = IRecipeType.create(Casting.identifier("modifier"), ModifierRecipe.class);

    private final int width = 101;
    private final int height = 30;
    private final IDrawable icon;

    @Override
    public @Nullable Identifier getIdentifier(ModifierRecipe recipe) {
        return recipe.modifier().getId();
    }

    public ModifierRecipeCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CastingToolsBlocks.MODIFIER.get()));
    }

    @Override
    public @NotNull IRecipeType<ModifierRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("jei.castingtools.modifier");
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ModifierRecipe recipe, IFocusGroup focusGroup) {
        TagKey<Fluid> experienceTag = TagKey.create(Registries.FLUID, Identifier.fromNamespaceAndPath("c", "experience"));

        List<FluidStack> displayFluids = new ArrayList<>();
        displayFluids.add(BuiltInRegistries.FLUID.get(experienceTag).stream().map(
                holders -> new FluidStack(holders.get(0), 1000)
        ).toList().getFirst());

        builder.addSlot(RecipeIngredientRole.INPUT, 2, 12).add(displayFluids.getFirst().getFluid())
                .addRichTooltipCallback((slot, tooltip) ->
                        tooltip.add(Component.literal(recipe.modifier().getExperienceCost() + " mB").withStyle(ChatFormatting.GOLD)));;

        if (recipe.modifier().getFluidIngredient().isPresent()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 34, 12).add(recipe.modifier().getFluidIngredient().get().ingredient().display())
                    .addRichTooltipCallback((slot, tooltip) ->
                            tooltip.add(Component.literal(recipe.modifier().getFluidIngredient().get().amount() + " mB").withStyle(ChatFormatting.GOLD)));;
        }

        if (recipe.modifier().getIngredient().isPresent()) {
            SizedIngredient sizedIng = recipe.modifier().getIngredient().get();
            Ingredient vanillaIngredient = sizedIng.ingredient();
            List<ItemStack> displayStacks = vanillaIngredient.getValues().stream()
                    .map(holder -> {
                        ItemStack stack = new ItemStack(holder.getDelegate().value());
                        stack.setCount(sizedIng.count()); // Apply the count from SizedIngredient
                        return stack;
                    }).toList();

            builder.addSlot(RecipeIngredientRole.INPUT, 52, 12)
                    .addItemStacks(displayStacks);
        }

        List<ItemStack> displayStacks = new ArrayList<>();

        for (TagKey<Item> tag : recipe.modifier().getValidTags()) {
            for (Holder<Item> holder : Objects.requireNonNull(BuiltInRegistries.ITEM.get(tag).orElse(null))) {
                displayStacks.add(new ItemStack(holder.value()));
            }
        }

        displayStacks.addAll(recipe.modifier().getValidItems().stream().map(ItemStack::new).toList());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 83, 12).addItemStacks(displayStacks);
        builder.addSlot(RecipeIngredientRole.INPUT, 83, 12).addItemStacks(displayStacks);


    }

    @Override
    public void draw(ModifierRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, 0, 0, 0, 0, width, height, width, height);
        guiGraphics.text(Minecraft.getInstance().font, recipe.modifier().getDisplayName(),
                2,1 , 0xFF000000, false);
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, ModifierRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        Component displayName = recipe.modifier().getDisplayName();

        int textWidth = minecraft.font.width(displayName);
        int textHeight = minecraft.font.lineHeight;

        if (mouseX >= 2 && mouseX <= 2 + textWidth && mouseY >= 1 && mouseY <= 1 + textHeight) {

            tooltip.add(recipe.modifier().getDisplayName());
            tooltip.add(recipe.modifier().getDescription());

            if (!recipe.modifier().getIncompatibleModifiers().isEmpty()) {
                tooltip.add(Component.empty());
                tooltip.add(Component.translatable("modifier.castingtools.incompatible")
                        .withStyle(ChatFormatting.RED));

                for (Modifier conflict : recipe.modifier().getIncompatibleModifiers()) {
                    tooltip.add(Component.literal("- ").append(conflict.getDisplayName())
                            .withStyle(ChatFormatting.DARK_RED));
                }
            }

            tooltip.add(Component.translatable("modifier.castingtools.max_level", recipe.modifier().getMaxLevel())
                    .withStyle(ChatFormatting.AQUA));
        }
    }
}
