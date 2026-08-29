package com.benbenlaw.castingtools.event;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.item.CTDataComponent;
import com.benbenlaw.castingtools.item.ModifierComponent;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierRegistry;
import com.benbenlaw.castingtools.utils.TriConsumer;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.player.ArrowLooseEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;


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
    public static void onBowFired(ArrowLooseEvent event) {
        handleModifiers(event.getEntity(), (modifier, level) ->
                modifier.onBowFired(event, modifier.getData(), level));
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
    public static void onFallDamage(LivingFallEvent event) {
        if (event.getEntity() instanceof Player) {
            handleAllArmorModifiers((Player) event.getEntity(), (modifier, stack, level) -> modifier.onFalling(event, stack, modifier.getData()));
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        handleAllInventoryModifiers(player, (modifier, stack, level) -> modifier.onPlayerEveryTick(event, stack, modifier.getData(), level));

        if (player.level().getGameTime() % 20 == 0) {
            handleAllInventoryModifiers(player, (modifier, stack, level) -> modifier.onPlayerLimitedTick(event, stack, modifier.getData(), level));
        }
    }

    @SubscribeEvent
    public static void onEntityDrops(LivingDropsEvent event) {
        // Attacker Logic
        Entity killer = event.getEntity().getKillCredit();
        if (killer instanceof LivingEntity attacker) {
            handleModifiers(attacker, (modifier, level) -> modifier.onMobDrops(event, modifier.getData(), level));
        }

        // Player Inventory Logic
        if (event.getEntity() instanceof Player player) {
            List<ItemEntity> drops = new java.util.ArrayList<>(event.getDrops());

            for (ItemEntity itemEntity : drops) {
                ItemStack stack = itemEntity.getItem();

                processStack(stack, (modifier, itemStack, level) ->
                        modifier.onPlayerDrops(event, itemStack, modifier.getData(), level, -1));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            Inventory inventory = player.getInventory();
            for (int i = 0; i < inventory.getContainerSize(); i++) {
                ItemStack stack = inventory.getItem(i);
                if (!stack.isEmpty()) {
                    int slot = i;
                    processStack(stack, (modifier, itemStack, level) ->
                            modifier.onPlayerDeath(event, itemStack, modifier.getData(), level, slot));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBlockDrops(BlockDropsEvent event) {
        ItemStack stack = event.getTool();
        processStack(stack, (modifier, itemStack, level) ->
            modifier.onBlockDrops(event, modifier.getData(), level));
    }

    @SubscribeEvent
    public static void onBlockBreak(BreakBlockEvent event) {
        if (event.getLevel().isClientSide()) return;
        ItemStack stack = event.getPlayer().getWeaponItem();
        processStack(stack, (modifier, itemStack, level) ->
                modifier.onBlockBreak(event, modifier.getData(), level));
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;
        Player oldPlayer = event.getOriginal();
        for (int i = 0; i < oldPlayer.getInventory().getContainerSize(); i++) {
            ItemStack stack = oldPlayer.getInventory().getItem(i);
            processStack(stack, (modifier, itemStack, level) -> {
                modifier.onPlayerClone(event, modifier.getData(), level);
            });
        }
    }

    //Held Item Modifier Handling Helper
    private static void handleModifiers(LivingEntity entity, BiConsumer<Modifier, Integer> action) {
        ItemStack stack = entity.getMainHandItem();
        ModifierComponent comp = stack.get(CTDataComponent.MODIFIER_COMPONENT);
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

    private static void handleAllArmorModifiers(Player player, TriConsumer<Modifier, ItemStack, Integer> action) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                ItemStack stack = player.getItemBySlot(slot);
                processStack(stack, action);
            }
        }
    }

    private static void processStack(ItemStack stack, TriConsumer<Modifier, ItemStack, Integer> action) {
        if (stack.isEmpty()) return;

        ModifierComponent comp = stack.get(CTDataComponent.MODIFIER_COMPONENT);
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