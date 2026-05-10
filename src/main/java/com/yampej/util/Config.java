package com.yampej.util;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;

public class Config {
    private static final Path WEBHOOK_FILE = FabricLoader.getInstance()
        .getConfigDir().resolve("yampej_webhook.txt");

    public static String getWebhookUrl() {
        try {
            if (!Files.exists(WEBHOOK_FILE)) {
                Files.writeString(WEBHOOK_FILE,
                    "# Yampej Client - Webhook Ayarlari\n" +
                    "# Discord webhook URL'ini asagiya yaz (# ile baslayan satirlar yorum)\n" +
                    "# Ornek: https://discord.com/api/webhooks/123456789/abcdef...\n\n"
                );
                return "";
            }
            return Files.readAllLines(WEBHOOK_FILE).stream()
                .filter(line -> !line.trim().startsWith("#") && !line.trim().isEmpty())
                .findFirst()
                .orElse("");
        } catch (Exception e) {
            System.err.println("[Yampej] Config okunamadi: " + e.getMessage());
            return "";
        }
    }
}
