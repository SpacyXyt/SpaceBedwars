package fr.spacyxyt.SpaceBedwars;

import fr.spacyxyt.SpaceBedwars.commands.sbw;
import fr.spacyxyt.SpaceBedwars.utils.File;
import fr.spacyxyt.SpaceBedwars.utils.World;
import fr.spacyxyt.SpaceBedwars.utils.teamType;
import fr.spacyxyt.SpaceBedwars.utils.worldManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;

public final class Main extends JavaPlugin {

    private static Main instance;
    private File configFile = new File(getDataFolder() + "/config.yml", this);
    private File messageFile = new File(getDataFolder() + "/message.yml", this);
    private File worldsFile = new File(getDataFolder() + "/worlds/worlds.yml", this);
    private ArrayList<World> worlds = new ArrayList<>();

    @Override
    public void onEnable() {
        System.out.print("------------< SpaceBedwars >---< Initialisation >---------");
        instance = this;
        getCommand("sbw").setExecutor(new sbw(this));
        System.out.print("Generating file...");
        if (configFile.createParentFile()) {
            System.out.print("config file create with success.");
        }
        if(messageFile.generateFile()) {
            System.out.print("message config file created with success.");
        }
        if(worldsFile.createParentFile()) {
            System.out.print("worlds config file created with success.");
        }
        System.out.print("file generated.");
        System.out.print("Recuperation des mondes...");
        for (org.bukkit.World world : Bukkit.getWorlds()) {
            if (world.getName().contains("tmp") || world.getName().equalsIgnoreCase("world") || world.getName().equalsIgnoreCase("world_nether") || world.getName().equalsIgnoreCase("world_the_end")) {
                continue;
            }
            System.out.print("Importing " + world.getName() + "...");
            if(world.getName().contains("solo")) {
                World world1 = new World(world.getName(), teamType.solo, 8);
                worlds.add(world1);
            } else if (world.getName().contains("duo")) {
                World world1 = new World(world.getName(), teamType.duo, 8);
                worlds.add(world1);
            } else if (world.getName().contains("trio")) {
                World world1 = new World(world.getName(), teamType.trio, 12);
                worlds.add(world1);
            } else if (world.getName().contains("quad")) {
                World world1 = new World(world.getName(), teamType.quad, 16);
                worlds.add(world1);
            }
            for (org.bukkit.World w : Bukkit.getWorlds()) {
                if (!w.getName().contains("tmp")) {
                    continue;
                }
                for (Player p : w.getPlayers()) {
                    p.teleport(Bukkit.getWorlds().get(1).getSpawnLocation());
                }
            }
            System.out.print(world.getName() + " imported.");
        }
        System.out.print("Monde recuperer.");
        System.out.print("------------< SpaceBedwars >---< Initialisation finished >---------");
    }

    @Override
    public void onDisable() {
        for (World world : worlds) {
            worldsFile.getYamlConfiguration().set("worlds." + world.getName() + ".name", world.getName());
            worldsFile.getYamlConfiguration().set("worlds." + world.getName() + ".type", world.getType().toString());
            worldsFile.getYamlConfiguration().set("worlds." + world.getName() + ".maxPlayers", world.getMaxPlayers());
            worldsFile.getYamlConfiguration().set("worlds." + world.getName() + ".loc.World", world.getName());
            worldsFile.getYamlConfiguration().set("worlds." + world.getName() + ".loc.X", world.getLoc().getX());
            worldsFile.getYamlConfiguration().set("worlds." + world.getName() + ".loc.Y", world.getLoc().getY());
            worldsFile.getYamlConfiguration().set("worlds." + world.getName() + ".loc.Z", world.getLoc().getZ());
            worldsFile.getYamlConfiguration().set("worlds." + world.getName() + ".loc.Yaw", world.getLoc().getYaw());
            worldsFile.getYamlConfiguration().set("worlds." + world.getName() + ".loc.Pitch", world.getLoc().getPitch());
            try {
                worldsFile.getYamlConfiguration().save(new java.io.File(getDataFolder() + "/worlds/worlds.yml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLoad() {
        System.out.println("Stopping unutilised worlds...");
        worldManager.start(this);
        System.out.println("worlds unutilised stopped.");
    }

    public static Main getInstance() {
        return instance;
    }

    public File getConfigFile() {
        return configFile;
    }

    public File getMessageFile() {
        return messageFile;
    }

    public File getWorldsFile() {
        return worldsFile;
    }

    public ArrayList<World> getServers() {
        return worlds;
    }
}
