package com.benbenlaw.castingtools.event.client;

import com.benbenlaw.castingtools.CastingTools;
import com.benbenlaw.castingtools.item.CTDataComponent;
import com.benbenlaw.castingtools.item.ModifierComponent;
import com.benbenlaw.castingtools.modifier.ModifierRegistry;
import com.benbenlaw.castingtools.utils.ModifierUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.CustomBlockOutlineRenderer;
import net.neoforged.neoforge.client.event.ExtractBlockOutlineRenderStateEvent;

import java.util.List;

@EventBusSubscriber(modid = CastingTools.MOD_ID, value = Dist.CLIENT)
public class RenderHighlightEvent {

    @SubscribeEvent
    public static void onExtractBlockOutlineRenderStateEvent(ExtractBlockOutlineRenderStateEvent event) {
        event.addCustomRenderer(new PreviewRender(event.getHitResult(), event.getCamera()));
    }

    private record PreviewRender(BlockHitResult target, Camera camera) implements CustomBlockOutlineRenderer {

        @Override
        public boolean render(BlockOutlineRenderState renderState, MultiBufferSource.BufferSource buffer,
                              PoseStack poseStack, boolean translucentPass, LevelRenderState levelRenderState) {

            Entity entity = camera.entity();
            if (!(entity instanceof Player player)) return false;
            float colorR = 0, colorG = 0, colorB = 0;

            ItemStack stack = player.getMainHandItem();
            if (stack.has(CTDataComponent.MODIFIER_COMPONENT.get())) {
                ModifierComponent comp = stack.get(CTDataComponent.MODIFIER_COMPONENT.get());
                if (comp != null && comp.modifiers().containsKey(ModifierRegistry.EXCAVATION.getId())) {
                    int level = comp.modifiers().get(ModifierRegistry.EXCAVATION.getId());

                    VertexConsumer lineBuilder = buffer.getBuffer(RenderTypes.lines());

                    List<BlockPos> willExcavate = ModifierUtils.getExcavationPlane(renderState.pos(), target.getDirection(), level);

                    double d0 = camera.position().x();
                    double d1 = camera.position().y();
                    double d2 = camera.position().z();

                    for (BlockPos block : willExcavate) {
                        AABB aabb = new AABB(block).move(-d0, -d1, -d2);
                        ShapeRenderer.renderShape(poseStack, lineBuilder, Shapes.create(aabb), 0, 0, 0,
                                ARGB.colorFromFloat(0.4F, colorR, colorG, colorB), 2F);
                    }
                }
            }
            return false;
        }
    }
}
