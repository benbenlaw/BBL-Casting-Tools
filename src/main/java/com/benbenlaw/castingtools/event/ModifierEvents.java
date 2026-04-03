package com.benbenlaw.castingtools.event;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.item.CastingToolsDataComponent;
import com.benbenlaw.castingtools.item.ModifierComponent;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import com.benbenlaw.castingtools.modifier.ModifierRegistry;
import com.benbenlaw.castingtools.utils.ModifierUtils;
import com.benbenlaw.castingtools.utils.QuadConsumer;
import com.benbenlaw.castingtools.utils.TriConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
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
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            handleModifiers(attacker, (modifier, level) ->
                    modifier.onPreHit(event, modifier.getData(), level));
        }

        LivingEntity victim = event.getEntity();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                ItemStack armorStack = victim.getItemBySlot(slot);
                if (!armorStack.isEmpty()) {
                    processStack(armorStack, (modifier, stack, level) ->
                            modifier.onPreHit(event, modifier.getData(), level));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamagePost(LivingDamageEvent.Post event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            handleModifiers(attacker, (modifier, level) ->
                    modifier.onPostHit(event, modifier.getData(), level));
        }
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getLevel().isClientSide()) return;
        handleModifiers(event.getEntity(), (modifier, level) ->
                modifier.onLeftClickBlock(event, modifier.getData(), level));
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        handleModifiers(event.getEntity(), (modifier, level) ->
                modifier.onBreakSpeed(event, modifier.getData(), level));
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        handleModifiers(event.getEntity(), (modifier, level) ->
                modifier.onRightClickBlock(event, modifier.getData(), level));
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        handleModifiers(event.getEntity(), (modifier, level) ->
                modifier.onRightClickItem(event, modifier.getData(), level));
    }


    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        if (player.level().getGameTime() % 20 == 0) {
            handleAllInventoryModifiers(player, (modifier, stack, level) -> {
                modifier.onPlayerTick(event, stack, modifier.getData(), level);
            });
        }
    }

    @SubscribeEvent
    public static void onEntityDrops(LivingDropsEvent event) {
        Entity killer = event.getEntity().getKillCredit();
        if (killer instanceof LivingEntity attacker) {
            handleModifiers(attacker, (modifier, level) -> {
                modifier.onMobDrops(event, modifier.getData(), level);
            });
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
            } else {
                ModifierUtils.breakBlockWithCasting(level, player, originPos, tool);
            }
            event.setCanceled(true);
        }
    }



    //Held Item Modifier Handling Helper
    private static void handleModifiers(LivingEntity entity, BiConsumer<Modifier, Integer> action) {
        ItemStack stack = entity.getMainHandItem();
        ModifierComponent comp = stack.get(CastingToolsDataComponent.MODIFIER_COMPONENT);
        if (comp == null) return;

        comp.modifiers().forEach((id, level) -> {
            Modifier logic = ModifierRegistry.MODIFIER_REGISTRY.getValue(id);

            if (logic != null) {
                action.accept(logic, level);
            }
        });
    }

    //Inventory-wide Modifier Handling Helper
    private static void handleAllInventoryModifiers(Player player, TriConsumer<Modifier, ItemStack, Integer> action) {
        Inventory inventory = player.getInventory();

        for (ItemStack stack : inventory.getNonEquipmentItems()) {
            processStack(stack, action);
        }

        for (int slotId : Inventory.EQUIPMENT_SLOT_MAPPING.keySet()) {
            ItemStack stack = inventory.getItem(slotId);
            processStack(stack, action);
        }
    }

    private static void processStack(ItemStack stack, TriConsumer<Modifier, ItemStack, Integer> action) {
        if (stack.isEmpty()) return;

        ModifierComponent comp = stack.get(CastingToolsDataComponent.MODIFIER_COMPONENT);
        if (comp != null) {
            comp.modifiers().forEach((id, level) -> {
                Modifier modifier = ModifierRegistry.MODIFIER_REGISTRY.getValue(id);
                if (modifier != null) {
                    action.accept(modifier, stack, level);
                }
            });
        }
    }

}