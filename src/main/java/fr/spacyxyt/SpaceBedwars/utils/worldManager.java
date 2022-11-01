package fr.spacyxyt.SpaceBedwars.utils;

import fr.spacyxyt.SpaceBedwars.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

import java.io.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class worldManager {

    public static int create(Main main, String name, teamType type, int maxPlayers) {
        World world = new World(name, type, maxPlayers);
        main.getServers().add(world);
        if(isWorldFolderExisting(name)) {
            return 1;
        }
        WorldCreator wc = new WorldCreator(name);
        wc.type(WorldType.FLAT);
        wc.generateStructures(false);
        wc.generatorSettings("2;0;1;");
        wc.createWorld();
        world.setLoc(Bukkit.getWorld(name).getSpawnLocation());
        return 0;
    }

    public static void start(Main main) {
        for (java.io.File file : Bukkit.getWorldContainer().listFiles()) {
            if (file.getName().contains("tmp")) {
                unloadWorld(file.getName(), false);
                delete(file);
            }
        }

    }

    public static void deleteWorld(String name) {
        unloadWorld(name, false);
        delete(new java.io.File(Bukkit.getWorldContainer() + "/" + name));
    }

    public static void createTempWorld(Player p, teamType type) {
        ArrayList<String> worlds = new ArrayList<>();
        for (java.io.File file : Bukkit.getWorldContainer().listFiles()) {
            if (!file.getName().equalsIgnoreCase("world") && !file.getName().equalsIgnoreCase("world_nether") && !file.getName().equalsIgnoreCase("world_the_end")) {
                if (file.getName().contains(type.toString())) {
                    if (!file.getName().contains("_tmp")) {
                        worlds.add(file.getName());
                    }
                }
            }
        }
        int random = new Random().nextInt(worlds.size());
        String world = worlds.get(random);

        if (world.length() == 0) {
            return;
        }

        cloneWorld(new java.io.File(Bukkit.getWorldContainer() + "/" + world),
                new java.io.File(Bukkit.getWorldContainer() + "/" + world + "_tmp"));
        loadWorld(world + "_tmp");
        p.teleport(Bukkit.getWorld(world + "_tmp").getSpawnLocation());
    }



    public static org.bukkit.World loadWorld(String name)  {
        if(Bukkit.getWorld(name) != null) return Bukkit.getWorld(name);
        if(isWorldFolderExisting(name)){
            return new WorldCreator(name).createWorld();
        } else {
            System.out.println("Can't load world \""+name+"\" because world folder does not exists");
        }
        return null;
    }

    public static void unloadWorld(String name, boolean save){
        for (Player p : Bukkit.getOnlinePlayers()) {
            if(p.getWorld().getName().equalsIgnoreCase(name)){
                p.teleport(Bukkit.getWorlds().get(1).getSpawnLocation());
            }
        }
        if(Bukkit.getWorld(name) != null){
            Bukkit.unloadWorld(name, save);
        }
    }

    public static void cloneWorld(java.io.File source, java.io.File target) {
        try {
            ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat", "session.lock"));
            if(!ignore.contains(source.getName())) {
                if(source.isDirectory()) {
                    if(!target.exists())
                        target.mkdirs();
                    String files[] = source.list();
                    for (String file : files) {
                        java.io.File srcFile = new java.io.File(source, file);
                        java.io.File destFile = new java.io.File(target, file);
                        cloneWorld(srcFile, destFile);
                    }
                } else {
                    InputStream in = new FileInputStream(source);
                    OutputStream out = new FileOutputStream(target);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0)
                        out.write(buffer, 0, length);
                    in.close();
                    out.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isWorldFolderExisting(String worldName){
        boolean exists = false;
        for (java.io.File file : Arrays.asList(Bukkit.getWorldContainer().listFiles(java.io.File::isDirectory))) {
            if(file.getName().equals(worldName)){
                exists = true;
            }
        }
        return exists;
    }

    public static void delete(java.io.File f) {
        if(f.isDirectory()){
            //si le dossier est vide, supprimez-le
            if(f.list().length == 0){
                f.delete();
                System.out.println("Dossier est supprimé: "+ f.getAbsolutePath());
            }else{
                //lister le contenu du répertoire
                String files[] = f.list();

                for (String tmp : files) {
                    java.io.File file = new java.io.File(f, tmp);
                    //suppression récursive
                    delete(file);
                }
                //vérifiez à nouveau le dossier, s'il est vide, supprimez-le
                if(f.list().length == 0){
                    f.delete();
                    System.out.println("Dossier est supprimé: "+ f.getAbsolutePath());
                }
            }
        }else{
            f.delete();
            System.out.println("Fichier est supprimé: " + f.getAbsolutePath());
        }
    }

    public static World getWorld(String name) {
        for (World world : Main.getInstance().getServers()) {
            if(world.getName() == name) {
                return world;
            }
        }
        return null;
    }


}
