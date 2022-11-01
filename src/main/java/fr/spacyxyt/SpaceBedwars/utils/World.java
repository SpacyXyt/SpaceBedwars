package fr.spacyxyt.SpaceBedwars.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class World {

    private String name;
    private teamType type;
    private int maxPlayers;
    private boolean isFull = false;
    private int Players;
    private Location loc;

    public World(String name, teamType type, int maxPlayers) {
        this.name = name;
        this.type = type;
        this.maxPlayers = maxPlayers;
        loc = new Location(Bukkit.getWorld(name), 0, 0, 0, 0, 0);
    }

    public int addPlayer() {
        Players++;
        return Players;
    }

    public int removePlayer() {
        Players--;
        return Players;
    }

    public void getPlayer() {
        Players++;
    }

    public void setPlayers(int players) {
        Players = players;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public teamType getType() {
        return type;
    }

    public void setType(teamType type) {
        this.type = type;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean isFull) {
        this.isFull = isFull;
    }

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }
}
