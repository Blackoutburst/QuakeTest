package com.blackoutburst.quake.main;

import com.blackoutburst.quake.commands.CommandManager;
import com.blackoutburst.quake.core.Core;
import com.blackoutburst.quake.core.QuakePlayer;
import com.blackoutburst.quake.core.SkullLoader;
import com.blackoutburst.quake.core.Utils;
import com.blackoutburst.quake.event.EventListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Main extends JavaPlugin {

	public static List<QuakePlayer> players = new ArrayList<>();
	public static boolean gameRunning = false;
	public static int gameTime = 0;
	public static List<Location> respawns = new ArrayList<>();
	public static boolean allowMapSelector;
	public static Location spawn;

	public static World gameWorld;

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new EventListener(), this);
		new Core().cooldownTimer();
		new Core().gameTimer();
		new File("./plugins/Quake/player data/").mkdirs();
		new File("./plugins/Quake/config/").mkdirs();
		SkullLoader.load();
		Utils.spawnParticlesScheduler();

		gameWorld = Bukkit.getWorlds().get(0);

		File f = new File("plugins/Quake/config/config.yml");
		YamlConfiguration file = YamlConfiguration.loadConfiguration(f);
		if (!f.exists()) {
			allowMapSelector = false;
			spawn = new Location(Bukkit.getWorlds().get(0), 0.5, 5, 0.5, 0, 0);

			file.set("allow-map-selector", false);
			file.set("spawn", null);
			file.set("spawn.world", Bukkit.getWorlds().get(0).getName());
			file.set("spawn.x", 0.5);
			file.set("spawn.y", 5);
			file.set("spawn.z", 0.5);
			file.set("spawn.yaw", 0);
			file.set("spawn.pitch", 0);

			try {
				file.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			allowMapSelector = file.getBoolean("allow-map-selector");
			World world = Bukkit.getWorld(file.getString("spawn.world"));
			double x = file.getDouble("spawn.x");
			double y = file.getDouble("spawn.y");
			double z = file.getDouble("spawn.z");
			float yaw = (float) file.getDouble("spawn.yaw");
			float pitch = (float) file.getDouble("spawn.pitch");
			spawn = new Location(world, x, y, z, yaw, pitch);
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandManager.execute(sender, command, args);
		return true;
	}
}
