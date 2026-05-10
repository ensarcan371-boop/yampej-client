package com.yampej.module.modules.misc;

import com.yampej.module.Category;
import com.yampej.module.Module;
import com.yampej.util.Config;
import com.yampej.util.WebhookSender;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;

import java.util.HashMap;
import java.util.Map;

public class SpawnerAlert extends Module {

    private final Map<BlockPos, String> reportedSpawners = new HashMap<>();
    private int tickTimer = 0;
    private static final int CHECK_INTERVAL = 60;
    private static final int SCAN_RADIUS = 6;

    public SpawnerAlert() {
        super("SpawnerAlert", "Spawner bulununca webhook gonder", Category.MISC);
    }

    @Override
    public void onEnable() {
        reportedSpawners.clear();
        tickTimer = 0;
    }

    @Override
    public void onDisable() {
        reportedSpawners.clear();
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null) return;

        tickTimer++;
        if (tickTimer % CHECK_INTERVAL != 0) return;

        String webhookUrl = Config.getWebhookUrl();
        if (webhookUrl.isEmpty()) return;

        ChunkPos center = mc.player.getChunkPos();
        Map<BlockPos, String> foundThisScan = new HashMap<>();

        for (int cx = center.x - SCAN_RADIUS; cx <= center.x + SCAN_RADIUS; cx++) {
            for (int cz = center.z - SCAN_RADIUS; cz <= center.z + SCAN_RADIUS; cz++) {
                WorldChunk chunk = mc.world.getChunkManager().getWorldChunk(cx, cz);
                if (chunk == null) continue;

                chunk.getBlockEntities().forEach((pos, be) -> {
                    if (!(be instanceof MobSpawnerBlockEntity spawner)) return;

                    String typeName = getSpawnerType(spawner, mc);
                    foundThisScan.put(pos.toImmutable(), typeName);
                });
            }
        }

        int totalNearby = foundThisScan.size();

        for (Map.Entry<BlockPos, String> entry : foundThisScan.entrySet()) {
            BlockPos pos = entry.getKey();
            String type = entry.getValue();

            if (reportedSpawners.containsKey(pos)) continue;

            reportedSpawners.put(pos, type);

            final int x = pos.getX(), y = pos.getY(), z = pos.getZ();
            final String finalType = type;
            final int total = totalNearby;

            new Thread(() -> WebhookSender.sendSpawner(webhookUrl, x, y, z, finalType, total),
                "Yampej-Webhook").start();
        }

        reportedSpawners.keySet().removeIf(pos -> !foundThisScan.containsKey(pos));
    }

    private String getSpawnerType(MobSpawnerBlockEntity spawner, MinecraftClient mc) {
        try {
            var logic = spawner.getLogic();
            if (mc.world == null) return "Unknown";

            var spawnEntry = logic.getSpawnEntry(mc.world, spawner.getPos());
            if (spawnEntry == null) return "Unknown";

            EntityType<?> entityType = spawnEntry.type();
            String id = Registries.ENTITY_TYPE.getId(entityType).getPath();

            return switch (id) {
                case "zombie"           -> "Zombie";
                case "skeleton"         -> "Iskelet";
                case "spider"           -> "Orumcek";
                case "cave_spider"      -> "Magara Orumcegi";
                case "blaze"            -> "Blaze";
                case "silverfish"       -> "Silverfish";
                case "creeper"          -> "Creeper";
                case "zombie_pigman", "zombified_piglin" -> "Zombi Piglin";
                case "enderman"         -> "Enderman";
                case "witch"            -> "Cadi";
                case "piglin"           -> "Piglin";
                case "husk"             -> "Husk";
                case "stray"            -> "Stray";
                case "drowned"          -> "Bogulmus";
                case "wither_skeleton"  -> "Wither Iskelet";
                case "guardian"         -> "Guardian";
                default -> Character.toUpperCase(id.charAt(0)) + id.substring(1).replace("_", " ");
            };
        } catch (Exception e) {
            return "Unknown";
        }
    }
}
