package fr.raph.sethome.cmd;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.raph.sethome.main.Main;
import net.md_5.bungee.api.ChatColor;

public class CommandsPrincipal implements CommandExecutor {
	
	private Main plugin;
	
	private String prefixError = ChatColor.DARK_GREEN + "SethomeErreur : ";
	private String prefixPerm = ChatColor.DARK_GREEN + "SethomePerm : ";
	private String prefixGame = ChatColor.DARK_GREEN + "Sethome : ";

	public CommandsPrincipal(Main main) {
		this.plugin = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		
		if(sender instanceof Player) {
			
			Player p = (Player) sender;
			
			if(cmd.getName().equalsIgnoreCase("home")) {
				
				if(!p.hasPermission("sethome.home")) {
					p.sendMessage(prefixPerm + ChatColor.RED + "Vous n'avez pas les permissions suffisantes pour exécuter cette commande ! Veuillez vous diriger vers un membre du staff si vous pensez que vous"
							+ "devriez avoir accès à cette commande.");
					return false;
				}
				ArrayList<String> homes = new ArrayList<String>();
				for(String string : plugin.getHomesConfig().getConfigurationSection("Home." + p.getUniqueId().toString()).getKeys(false)) {
					homes.add(string);
				}
				if(args.length == 0) {

					p.sendMessage(prefixGame + ChatColor.RED + "Voici la liste de vos homes : " + ChatColor.AQUA + homes.toString());
					return false;
				}
				
				for(int i = 0; i <= homes.size()-1; i++) {
					
					if(homes.get(i).toString().equals(args[0].toString())) {
						
						Double x = plugin.getHomesConfig().getDouble("Home." + p.getUniqueId().toString() + "." + args[0].toString() + ".X");
						Double y = plugin.getHomesConfig().getDouble("Home." + p.getUniqueId().toString() + "." + args[0].toString() + ".Y");
						Double z = plugin.getHomesConfig().getDouble("Home." + p.getUniqueId().toString() + "." + args[0].toString() + ".Z");
						Float Yaw = (float) plugin.getHomesConfig().getLong("Home." + p.getUniqueId().toString() + "." + args[0].toString() + ".Yaw");
						Float Pitch = (float) plugin.getHomesConfig().getLong("Home." + p.getUniqueId().toString() + "." + args[0].toString() + ".Pitch");
						World world = Bukkit.getWorld(plugin.getHomesConfig().getString("Home." + p.getUniqueId().toString() + "." + args[0].toString() + ".World"));
						
						Location home = new Location(world, x, y, z, Yaw, Pitch);
						
						p.teleport(home);
						p.sendMessage(prefixGame + ChatColor.AQUA + "Vous avez été téléporter à votre home : " + args[0].toString());

						return true;
					}
				}
				
				p.sendMessage(prefixError + ChatColor.RED + "Le nom du home n'a pas été trouvé");
				return false;
				
			}else if(cmd.getName().equalsIgnoreCase("sethome")) {
				
				if(!p.hasPermission("sethome.sethome")) {
					p.sendMessage(prefixPerm + ChatColor.RED + "Vous n'avez pas les permissions suffisantes pour exécuter cette commande ! Veuillez vous diriger vers un membre du staff si vous pensez que vous"
							+ "devriez avoir accès à cette commande.");
					return false;
				}

				if(args.length == 0) {
					p.sendMessage(prefixError + ChatColor.RED + "La commande est /sethome <Nom du home> !");
					return false;
				}
				
				plugin.getHomesConfig().set("Home." + p.getUniqueId().toString() + "." + args[0].toString() + ".X", Double.valueOf(p.getLocation().getX()));
				plugin.getHomesConfig().set("Home." + p.getUniqueId().toString() + "." + args[0].toString() + ".Y", Double.valueOf(p.getLocation().getY()));
				plugin.getHomesConfig().set("Home." + p.getUniqueId().toString() + "." + args[0].toString() + ".Z", Double.valueOf(p.getLocation().getZ()));
				plugin.getHomesConfig().set("Home." + p.getUniqueId().toString() + "." + args[0].toString() + ".Yaw", Float.valueOf((long) p.getLocation().getYaw()));
				plugin.getHomesConfig().set("Home." + p.getUniqueId().toString() + "." + args[0].toString() + ".Pitch", Float.valueOf(p.getLocation().getPitch()));
				plugin.getHomesConfig().set("Home." + p.getUniqueId().toString() + "." + args[0].toString() + ".World", p.getWorld().getName());
				
				
				try {
					
					plugin.getHomesConfig().save(plugin.homesFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
				p.sendMessage("home create");
				
				
			}
			
			
			
			
		}else
			sender.sendMessage(prefixError + ChatColor.RED + "Cette commande ne peut être exécuter que par un joueur !");
		
		
		
		return false;
	}

}
