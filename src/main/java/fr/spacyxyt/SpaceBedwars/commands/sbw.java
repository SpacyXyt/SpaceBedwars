package fr.spacyxyt.SpaceBedwars.commands;

import fr.spacyxyt.SpaceBedwars.Main;
import fr.spacyxyt.SpaceBedwars.utils.World;
import fr.spacyxyt.SpaceBedwars.utils.teamType;
import fr.spacyxyt.SpaceBedwars.utils.worldManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class sbw implements CommandExecutor {

    private Main main;

    public sbw(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length < 1) {
            return false;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only the player can use this command!");
        }

        Player p = (Player) sender;

        switch (args[0]) {
            case "join":
                if (args.length < 2) {
                    return false;
                }
                if (!sender.hasPermission("spacebedwars.join")) {
                    sender.sendMessage(main.getInstance().getConfig().getString("language") + ".non-permission.join");
                }
                boolean found = false;
                for (World world : main.getInstance().getServers()) {
                    System.out.println(world.getName());
                    if (!world.isFull()) {
                        System.out.println("World " + world.getName() + " is not full.");
                        if (world.getType().toString().equalsIgnoreCase(args[1])) {
                            System.out.println(world.getName());
                            if (world.getName().contains("tmp")) {
                                System.out.println("World is a tempory world.");
                                p.teleport(world.getLoc());
                                found = true;
                                return true;
                            }
                        }
                    }
                }
                if (found == false) {
                    p.sendMessage("Aucun monde trouvé, creation d'un nouveaux monde...");
                    switch (args[1]) {
                        case "solo":
                            worldManager.createTempWorld(p, teamType.solo);
                            break;
                        case "duo":
                            worldManager.createTempWorld(p, teamType.duo);
                            break;
                        case "trio":
                            worldManager.createTempWorld(p, teamType.trio);
                            break;
                        case "quad":
                            worldManager.createTempWorld(p, teamType.quad);
                            break;
                        default:
                            break;
                    }
                    return true;
                }
                break;
            case "create":
                if (!sender.hasPermission("spacebedwars.create")) {
                    sender.sendMessage(main.getInstance().getConfig().getString("language") + ".non-permission.create");
                }
                if (args.length < 3) {
                    return false;
                }
                switch (args[2]) {
                    case "solo":
                    case "duo":
                        if(worldManager.create(main, args[1] + "_" + args[2], teamType.solo, 8) == 1) {
                            p.sendMessage("Ce nom existe déjà, veuillez en trouver un autre.");
                            return true;
                        }
                        p.sendMessage("World " + args[1] + " created with success.");
                        p.teleport(new Location(Bukkit.getWorld(args[1] + "_" + args[2]), 0.5, 5, 0.5));
                        main.getServer().dispatchCommand(sender, "setblock 0.5 1 0.5 stone");
                        return true;
                    case "trio":
                        if(worldManager.create(main, args[1] + "_" + args[2], teamType.solo, 12) == 1) {
                            p.sendMessage("Ce nom existe déjà, veuillez en trouver un autre.");
                            return true;
                        }
                        p.sendMessage("World " + args[1] + " created with success.");
                        p.teleport(new Location(Bukkit.getWorld(args[1] + "_" + args[2]), 0.5, 5, 0.5));
                        main.getServer().dispatchCommand(sender, "setblock 0.5 1 0.5 stone");
                        return true;
                    case "quad":
                        if(worldManager.create(main, args[1] + "_" + args[2], teamType.solo, 16) == 1) {
                            p.sendMessage("Ce nom existe déjà, veuillez en trouver un autre.");
                            return true;
                        }
                        p.sendMessage("World " + args[1] + " created with success.");
                        p.teleport(new Location(Bukkit.getWorld(args[1] + "_" + args[2]), 0.5, 5, 0.5));
                        main.getServer().dispatchCommand(sender, "setblock 0.5 1 0.5 stone");
                        return true;
                    default:
                        sender.sendMessage("Precise a type of game for create them.");
                        return true;
                }
            case "world":
                if (args.length < 2) {
                    sender.sendMessage("La commande est /world <tp [world_name]/list>");
                    return true;
                }
                switch (args[1]) {
                    case "tp":
                        if (args.length < 3) {
                            sender.sendMessage("La commande est /world <tp [world_name]/list>");
                        }
                        boolean exist = false;

                        if (worldManager.isWorldFolderExisting(args[2])) {
                            exist = true;
                            p.teleport(Bukkit.getWorld(args[2]).getSpawnLocation());
                            p.setAllowFlight(true);
                            p.setFlying(true);
                        }

                        if (!exist) {
                            p.sendMessage("This world does not exist.");
                        }
                        return true;
                    case "list":
                        String worlds = "";
                        for (org.bukkit.World world : Bukkit.getWorlds()) {
                            if(p.getWorld() == world) {
                                worlds += "§a" + world.getName() + "§f, ";
                                continue;
                            }
                            worlds += world.getName() + ", ";
                        }
                        p.sendMessage(worlds);
                        return true;
                    case "delete":
                        if (args.length < 3) {
                            sender.sendMessage("La commande est /sbw world delete [world name]");
                            return true;
                        }
                        worldManager.deleteWorld(args[2]);
                        p.sendMessage("world " + args[2] + " deleted with success.");
                        return true;
                    case "load":
                        if(args.length < 3) {
                            sender.sendMessage("La commande est /sbw world load [world name]");
                            return true;
                        }
                        worldManager.loadWorld(args[2]);
                        p.sendMessage("world " + args[2] + " loaded with success.");
                        return true;
                }
        }

        return false;
    }
}
