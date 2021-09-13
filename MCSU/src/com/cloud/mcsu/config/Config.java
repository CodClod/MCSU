package com.cloud.mcsu.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {

    private static File file;
    private static FileConfiguration configfile;

    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("MCSU").getDataFolder(), "config.yml");

        if(!file.exists()) {
            try {
                file.createNewFile();
                Config.setup();
                Config.get().set("Points.BluePoints","0");
                Config.get().set("Points.RedPoints","0");
                Config.get().set("Points.GreenPoints","0");
                Config.get().set("Points.YellowPoints","0");
                Config.get().set("Points.AquaPoints","0");
                Config.get().set("Points.PinkPoints","0");
                Config.get().set("Points.GreyPoints","0");
                Config.get().set("Points.WhitePoints","0");
                Config.get().options().copyDefaults(true);
                Config.save();
            } catch (IOException e) {
                // execption
            }
        }

        configfile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get() {
        return configfile;
    }

    public static void save() {
        try {
            configfile.save(file);
        } catch (IOException e) {
            System.out.println("Couldn't save file.");
        }
    }

    public static void reload() {
        configfile = YamlConfiguration.loadConfiguration(file);
    }
}
