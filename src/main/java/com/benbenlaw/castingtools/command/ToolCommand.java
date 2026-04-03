package com.benbenlaw.castingtools.command;

import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierRegistry;
import com.benbenlaw.castingtools.utils.ModifierUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ToolCommand {

    private static final int MAX_MODIFIERS = 8;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("tool")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.argument("item", IdentifierArgument.id())
                        .then(modifierChain(0))
                )
        );
    }

    private static ArgumentBuilder<CommandSourceStack, ?> modifierChain(int depth) {

        var modifierArg = Commands.argument("modifier_" + depth, IdentifierArgument.id())
                .suggests((ctx, builder) -> {
                    for (var entry : ModifierRegistry.MODIFIER_REGISTRY.entrySet()) {
                        builder.suggest(entry.getKey().identifier().toString());
                    }
                    return builder.buildFuture();
                });

        var levelArg = Commands.argument("level_" + depth, IntegerArgumentType.integer(1))
                .executes(ToolCommand::execute);

        if (depth < MAX_MODIFIERS) {
            levelArg.then(modifierChain(depth + 1));
        }

        modifierArg.then(levelArg);

        return modifierArg;
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) {

        try {
            ServerPlayer player = ctx.getSource().getPlayerOrException();

            Identifier itemId = IdentifierArgument.getId(ctx, "item");
            Item item = BuiltInRegistries.ITEM.getValue(itemId);

            if (item == null) {
                ctx.getSource().sendFailure(Component.literal("Invalid item"));
                return 0;
            }

            ItemStack stack = new ItemStack(item);
            List<ModifierEntry> modifiers = extractModifiers(ctx);

            for (ModifierEntry entry : modifiers) {
                Modifier modifier = ModifierRegistry.MODIFIER_REGISTRY.getValue(entry.id);

                if (modifier == null) continue;

                if (!modifier.isValid(stack)) continue;

                int level = Math.min(entry.level, modifier.getMaxLevel());

                ModifierUtils.setModifierLevel(stack, modifier, level);
            }

            player.addItem(stack);
            return 1;

        } catch (Exception e) {
            ctx.getSource().sendFailure(Component.literal("Error executing command"));
            return 0;
        }
    }

    private static List<ModifierEntry> extractModifiers(CommandContext<CommandSourceStack> ctx) {

        List<ModifierEntry> list = new ArrayList<>();

        for (int i = 0; i < MAX_MODIFIERS; i++) {

            try {
                Identifier id = IdentifierArgument.getId(ctx, "modifier_" + i);
                int level = IntegerArgumentType.getInteger(ctx, "level_" + i);

                list.add(new ModifierEntry(id, level));

            } catch (Exception ignored) {
                break;
            }
        }

        return list;
    }

    private static class ModifierEntry {
        Identifier id;
        int level;

        ModifierEntry(Identifier id, int level) {
            this.id = id;
            this.level = level;
        }
    }
}