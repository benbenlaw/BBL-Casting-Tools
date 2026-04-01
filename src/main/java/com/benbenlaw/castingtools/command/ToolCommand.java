package com.benbenlaw.castingtools.command;

import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierRegistry;
import com.benbenlaw.castingtools.utils.ModifierUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public class ToolCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register(Commands.literal("tool")
                // Use the Vanilla helper for permission level 2 (Gamemaster/OP)
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.argument("item", ItemArgument.item(context))
                        .then(Commands.argument("modifier", IdentifierArgument.id())
                                .suggests((ctx, builder) -> {
                                    ModifierRegistry.REGISTRY.keySet().forEach(id -> builder.suggest(id.toString()));
                                    return builder.buildFuture();
                                })
                                .then(Commands.argument("level", IntegerArgumentType.integer(1))
                                        .executes(ToolCommand::execute)))));
    }

    private static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();

        // 1. Get the ItemInput (Vanilla uses ItemInput to create the stack)
        ItemInput input = ItemArgument.getItem(context, "item");
        ItemStack stack = input.createItemStack(1);

        // 2. Get Modifier
        Identifier modifierId = IdentifierArgument.getId(context, "modifier");
        Modifier modifier = ModifierRegistry.REGISTRY.getValue(modifierId);

        if (modifier == null) {
            source.sendFailure(Component.literal("Unknown modifier: " + modifierId));
            return 0;
        }

        // 3. Get Level
        int level = IntegerArgumentType.getInteger(context, "level");

        // 4. Application Logic
        if (!modifier.isValid(stack)) {
            source.sendFailure(Component.literal("Modifier cannot be applied to this item!"));
            return 0;
        }

        ModifierUtils.setModifierLevel(stack, modifier, level);

        // 5. Give to player (Vanilla style check)
        if (source.getPlayer() != null) {
            source.getPlayer().getInventory().add(stack);
            source.sendSuccess(() -> Component.literal("Gave modified tool!"), true);
        }

        return 1;
    }
}