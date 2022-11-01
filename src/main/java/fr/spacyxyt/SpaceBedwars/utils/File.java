package fr.spacyxyt.SpaceBedwars.utils;

import fr.spacyxyt.SpaceBedwars.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

public class File {
    java.io.File file;
    String name;
    YamlConfiguration yamlConfiguration;
    Main main;

    public File(java.io.File file) {
        this.file = file;
        this.name = file.getName();
    }

    public File(String path, Main main) {
        java.io.File file = new java.io.File(path);

        this.main = main;
        this.file = file;
        this.name = file.getName();
    }

    public boolean createParentFile() {
        if(!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        if(generateFile()) {
            return true;
        }
        return false;
    }

    public boolean generateFile() {
        if(file.getParentFile().exists()) {
            if(!file.exists()) {
                try {
                    file.createNewFile();
                    read();
                    return true;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return false;
    }

    public void save() {
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void read() {
        yamlConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public void set(String path, String value) {
        yamlConfiguration.set(path, value);
        save();
    }

    public void set(String path, int value) {
        yamlConfiguration.set(path, value);
        save();
    }

    public void set(String path, double value) {
        yamlConfiguration.set(path, value);
        save();
    }

    public void set(String path, float value) {
        yamlConfiguration.set(path, value);
        save();
    }

    public String getString(String path) {
        yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        return yamlConfiguration.getString(path).replace("\"", "").replace("&", "§");
    }

    public int getInt(String path) {
        yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        return yamlConfiguration.getInt(path);
    }

    public String getName() {
        return name;
    }

    public YamlConfiguration getYamlConfiguration() {
        if (yamlConfiguration == null) {
            yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        }
        return yamlConfiguration;
    }
}
