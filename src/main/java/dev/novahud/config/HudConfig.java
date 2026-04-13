package dev.novahud.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class HudConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = FabricLoader.getInstance()
        .getConfigDir().resolve("novahud.json").toFile();

    public Map<String, Boolean> enabled = new HashMap<>();
    public Map<String, int[]> positions = new HashMap<>();

    public HudConfig() {
        enabled.put("fps", true);
        enabled.put("ping", true);
        enabled.put("cps", true);
        enabled.put("wasd", true);
        enabled.put("coords", true);
        enabled.put("speed", true);
        enabled.put("time", true);
        enabled.put("armor", true);

        positions.put("fps",    new int[]{5, 5});
        positions.put("ping",   new int[]{5, 18});
        positions.put("cps",    new int[]{5, 31});
        positions.put("wasd",   new int[]{5, 44});
        positions.put("coords", new int[]{5, 80});
        positions.put("speed",  new int[]{5, 93});
        positions.put("time",   new int[]{5, 106});
        positions.put("armor",  new int[]{5, 119});
    }

    public boolean isEnabled(String module) {
        return enabled.getOrDefault(module, true);
    }

    public int[] getPos(String module) {
        return positions.getOrDefault(module, new int[]{5, 5});
    }

    public void setPos(String module, int x, int y) {
        positions.put(module, new int[]{x, y});
    }

    public void save() {
        try (Writer w = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(this, w);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HudConfig load() {
        if (!CONFIG_FILE.exists()) {
            HudConfig cfg = new HudConfig();
            cfg.save();
            return cfg;
        }
        try (Reader r = new FileReader(CONFIG_FILE)) {
            HudConfig cfg = GSON.fromJson(r, HudConfig.class);
            if (cfg == null) return new HudConfig();
            HudConfig def = new HudConfig();
            def.enabled.forEach(cfg.enabled::putIfAbsent);
            def.positions.forEach(cfg.positions::putIfAbsent);
            return cfg;
        } catch (IOException e) {
            return new HudConfig();
        }
    }
}
