package com.tatadesmod.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private static final File CONFIG_FILE = new File("config", "tatadesmod.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static ConfigData data = new ConfigData();

    public static void init() {
        try {
            if (!CONFIG_FILE.getParentFile().exists()) CONFIG_FILE.getParentFile().mkdirs();
            if (CONFIG_FILE.exists()) {
                try (FileReader r = new FileReader(CONFIG_FILE)) {
                    data = GSON.fromJson(r, ConfigData.class);
                    if (data == null) data = new ConfigData();
                }
            } else {
                save();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ConfigData get() { return data; }

    public static void save() {
        try (FileWriter w = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(data, w);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class ConfigData {
        public Map<String, Object> values = new HashMap<>();
    }
}
