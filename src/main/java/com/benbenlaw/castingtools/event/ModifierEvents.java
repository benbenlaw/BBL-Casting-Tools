package com.benbenlaw.castingtools.event;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.item.CastingToolsDataComponent;
import com.benbenlaw.castingtools.item.ModifierComponent;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierRegistry;
import com.benbenlaw.castingtools.modifier.weapon.SharpnessModifier;
import com.benbenlaw.castingtools.utils.ModifierUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

import static com.benbenlaw.castingtools.modifier.ModifierRegistry.EXCAVATION;

@EventBusSubscriber(modid = CastingTools.MOD_ID)
public class ModifierEvents {

    public static final Map<UUID, Direction> lastHitDirectionMap = new HashMap<>();

    @SubscribeEvent
    public static void onLivingDamagePre(LivingDamageEvent.Pre event) {
        handleModifiers(event.getSource().getEntity(), (modifier, level) -> {
            if (modifier instanceof SharpnessModifier sharp) {
                sharp.onPreHit(event, level);
            }
        });
    }

    @SubscribeEvent
    public static void onLivingDamagePost(LivingDamageEvent.Post event) {
        handleModifiers(event.getSource().getEntity(), (modifier, level) -> {
            modifier.onPostHit(event, level);
        });
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        ItemStack tool = player.getMainHandItem();
        Level level = player.level();
        Direction face = event.getFace();
        ModifierComponent comp = tool.get(CastingToolsDataComponent.MODIFIER_COMPONENT);

        if (level.isClientSide()) return;
        if (comp != null) {
            if (comp.modifiers().containsKey(EXCAVATION.get().getId())) {
                lastHitDirectionMap.put(player.getUUID(), face);
            }
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getLevel().isClientSide()) return;

        Player player = event.getPlayer();
        Level level = (Level) event.getLevel();
        BlockPos originPos = event.getPos();
        BlockState originState = level.getBlockState(originPos);

        Direction face = lastHitDirectionMap.getOrDefault(player.getUUID(), Direction.DOWN);
        ItemStack tool = player.getMainHandItem();
        ModifierComponent comp = tool.get(CastingToolsDataComponent.MODIFIER_COMPONENT);

        if (comp != null) {
            Integer excavationLevel = comp.modifiers().get(EXCAVATION.get().getId());

            if (excavationLevel != null && excavationLevel > 0) {
                List<BlockPos> area = ModifierUtils.getExcavationPlane(originPos, face, excavationLevel);

                for (BlockPos targetPos : area) {
                    if (targetPos.equals(originPos)) continue;

                    BlockState targetState = level.getBlockState(targetPos);
                    if (tool.getDestroySpeed(targetState) <= 1.0f) continue;

                    float originHardness = originState.getDestroySpeed(level, originPos);
                    float targetHardness = targetState.getDestroySpeed(level, targetPos);

                    if (targetHardness < 0 || targetHardness > originHardness * 1.5f) continue;

                    ModifierUtils.breakBlockWithCasting(level, player, targetPos, tool);
                }

                ModifierUtils.breakBlockWithCasting(level, player, originPos, tool);
                event.setCanceled(true);
            } else {
                ModifierUtils.breakBlockWithCasting(level, player, originPos, tool);
            }
        }
    }

    private static void handleModifiers(Entity attacker, BiConsumer<Modifier, Integer> action) {
        if (!(attacker instanceof LivingEntity livingAttacker)) return;

        ItemStack stack = livingAttacker.getMainHandItem();
        ModifierComponent comp = stack.get(CastingToolsDataComponent.MODIFIER_COMPONENT);
        if (comp == null) return;

        comp.modifiers().forEach((id, level) -> {
            Modifier modifier = ModifierRegistry.REGISTRY.getValue(id);
            if (modifier != null) {
                action.accept(modifier, level);
            }
        });
    }

}