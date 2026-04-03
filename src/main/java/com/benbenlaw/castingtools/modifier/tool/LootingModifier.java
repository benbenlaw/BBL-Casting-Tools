package com.benbenlaw.castingtools.modifier.tool;

import com.benbenlaw.casting.Casting;
import com.benbenlaw.castingtools.modifier.Modifier;
import com.benbenlaw.castingtools.modifier.ModifierData;
import com.benbenlaw.castingtools.utils.CastingToolsTags;
import com.benbenlaw.core.item.CoreItemUtils;
import com.benbenlaw.core.util.FakePlayerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class LootingModifier extends Modifier {

    @Override
    public boolean overridesLootTable(ItemStack stack, ModifierData data, int toolLevel) {
        return true;
    }

    @Override
    public void onMobDrops(LivingDropsEvent event, ModifierData data, int toolLevel) {
        LivingEntity deadEntity = event.getEntity();
        Entity killer = event.getEntity().getKillCredit();
        DamageSource source = event.getSource();
        Level world = deadEntity.level();

        boolean bossLoot = deadEntity instanceof WitherBoss || deadEntity instanceof EnderDragon;

        if (!bossLoot) {
            event.setCanceled(true);

            ItemStack fakeStack = Objects.requireNonNull(event.getSource().getWeaponItem()).copy();
            fakeStack.enchant(world.holderLookup(Registries.ENCHANTMENT).getOrThrow(Enchantments.LOOTING), toolLevel);

            LootTable lootTable = Objects.requireNonNull(world.getServer()).reloadableRegistries()
                    .getLootTable(deadEntity.getLootTable().get());

            List<ItemStack> newLoot = getMobLootDrops(deadEntity, (Player) killer, source, fakeStack, lootTable, world);

            for (ItemStack stack : newLoot) {

                popOutTheItem(world, BlockPos.containing(deadEntity.blockPosition().getCenter()), stack);
            }
        }
    }

    public static void popOutTheItem(Level level, BlockPos blockPos, ItemStack itemStack) {

        Vec3 vec3 = Vec3.atLowerCornerWithOffset(blockPos, 0.5, 1.1, 0.5).offsetRandom(level.getRandom(), 0.7F);
        ItemStack itemstack1 = itemStack.copy();
        ItemEntity itementity = new ItemEntity(level, vec3.x(), vec3.y(), vec3.z(), itemstack1);
        itementity.setDefaultPickUpDelay();
        level.addFreshEntity(itementity);
    }

    public static List<ItemStack> getMobLootDrops(Entity deadEntity, Player player, DamageSource damageSource, ItemStack stack,
                                                  LootTable lootTable, Level level) {

        FakePlayer fakePlayer = FakePlayerUtil.createFakePlayer((ServerLevel) level, "FakePlayerForLooting");
        fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, stack);

        LootParams.Builder lootParams = new LootParams.Builder((ServerLevel) level)
                .withParameter(LootContextParams.THIS_ENTITY, deadEntity)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(deadEntity.getOnPos()))
                .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
                .withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player)
                .withParameter(LootContextParams.ATTACKING_ENTITY, fakePlayer);

        LootParams lootParamsFinal = lootParams.create(LootContextParamSets.ENTITY);
        return lootTable.getRandomItems(lootParamsFinal);
    }
}