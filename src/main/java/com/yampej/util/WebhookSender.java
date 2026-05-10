package com.yampej.util;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class WebhookSender {

    public static void sendSpawner(String webhookUrl, int x, int y, int z, String type, int totalNearby) {
        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("User-Agent", "YampejClient/1.0");
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            String json = "{" +
                "\"embeds\":[{" +
                "\"title\":\"\\uD83D\\uDC80 Spawner Tespit Edildi!\", " +
                "\"description\":\"DonutSMP'de yeni bir spawner bulundu.\", " +
                "\"color\":10038562," +
                "\"fields\":[" +
                "{\"name\":\"\\uD83E\\uDDDF T\\u00fcr\",\"value\":\"**" + escape(type) + " Spawner**\",\"inline\":true}," +
                "{\"name\":\"\\uD83D\\uDCCD Koordinatlar\",\"value\":\"X: **" + x + "** Y: **" + y + "** Z: **" + z + "**\",\"inline\":true}," +
                "{\"name\":\"\\uD83D\\uDCCA Yak\\u0131ndaki Toplam\",\"value\":\"**" + totalNearby + "** spawner\",\"inline\":true}" +
                "]," +
                "\"footer\":{\"text\":\"Yampej Client v1.0.0\"}" +
                "}]" +
                "}";

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int code = conn.getResponseCode();
            conn.disconnect();

            if (code < 200 || code >= 300) {
                System.err.println("[Yampej] Webhook gonderimi basarisiz: HTTP " + code);
            }
        } catch (Exception e) {
            System.err.println("[Yampej] Webhook hatasi: " + e.getMessage());
        }
    }

    private static String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
