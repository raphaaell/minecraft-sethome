package fr.raph.sethome.main;

import java.io.File;
import java.io.IOException;

import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import fr.raph.sethome.cmd.CommandsAdmin;
import fr.raph.sethome.cmd.CommandsPrincipal;

public class Main extends JavaPlugin {
	
	public File homesFile;
	private FileConfiguration homesConfig;

	@Override
	public void onEnable() {
		createHomesConfig();
		createClass();
	}
	
	private void createClass() {
		CommandExecutor cmdP = new CommandsPrincipal(this);
		getCommand("home").setExecutor(cmdP);
		getCommand("sethome").setExecutor(cmdP);
		getCommand("delhome").setExecutor(cmdP);
		CommandExecutor cmdA = new CommandsAdmin();
		getCommand("homes").setExecutor(cmdA);
		getCommand("home-tp").setExecutor(cmdA);
		getCommand("delhomes").setExecutor(cmdA);
	}

	
	public FileConfiguration getHomesConfig() {
		return this.homesConfig;
	}
	
	private void createHomesConfig() {
		homesFile = new File(getDataFolder(), "homes.yml");
		if(!homesFile.exists()) {
			homesFile.getParentFile().mkdirs();
			saveResource("homes.yml", true);
		}
		
		homesConfig = new YamlConfiguration();
		
		try {
			homesConfig.load(homesFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	

}
