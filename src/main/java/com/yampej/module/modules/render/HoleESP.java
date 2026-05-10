package com.yampej.module.modules.render;

import com.yampej.module.Category;
import com.yampej.module.Module;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import org.joml.Matrix4f;

public class HoleESP extends Module {
    public HoleESP() {
        super("HoleESP", "Highlights safe holes", Category.RENDER);
        setEnabled(true);
    }

    public void render(MatrixStack matrices, VertexConsumerProvider consumers, float tickDelta) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null) return;

        BlockPos center = mc.player.getBlockPos();
        int range = 10;

        matrices.push();
        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                BlockPos pos = center.add(x, -1, z);
                if (!isSafeHole(pos)) continue;

                double px = pos.getX() - mc.getEntityRenderDispatcher().camera.getPos().x;
                double py = pos.getY() + 1.0 - mc.getEntityRenderDispatcher().camera.getPos().y;
                double pz = pos.getZ() - mc.getEntityRenderDispatcher().camera.getPos().z;

                matrices.push();
                matrices.translate(px, py, pz);
                VertexConsumer consumer = consumers.getBuffer(RenderLayer.getDebugFilledBox());
                Matrix4f matrix = matrices.peek().getPositionMatrix();
                fillBox(consumer, matrix, 0, 0, 0, 1, 0.05f, 1, 0.1f, 0.9f, 0.4f, 0.5f);
                matrices.pop();
            }
        }
        matrices.pop();
    }

    private boolean isSafeHole(BlockPos pos) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.world == null) return false;
        var block = mc.world.getBlockState(pos).getBlock();
        if (block != Blocks.OBSIDIAN && block != Blocks.BEDROCK) return false;
        for (int i = 1; i <= 3; i++) {
            if (!mc.world.getBlockState(pos.up(i)).isAir()) return false;
        }
        BlockPos[] sides = {pos.north(), pos.south(), pos.east(), pos.west()};
        for (BlockPos side : sides) {
            var sb = mc.world.getBlockState(side).getBlock();
            if (sb != Blocks.OBSIDIAN && sb != Blocks.BEDROCK) return false;
        }
        return true;
    }

    private void fillBox(VertexConsumer c, Matrix4f m, float x1, float y1, float z1, float x2, float y2, float z2, float r, float g, float b, float a) {
        c.vertex(m, x1, y1, z1).color(r, g, b, a);
        c.vertex(m, x2, y1, z1).color(r, g, b, a);
        c.vertex(m, x2, y1, z2).color(r, g, b, a);
        c.vertex(m, x1, y1, z2).color(r, g, b, a);
    }
}
