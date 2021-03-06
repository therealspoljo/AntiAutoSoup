package com.therealspoljo.antiautosoup.utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.therealspoljo.antiautosoup.Main;
import com.therealspoljo.antiautosoup.enums.Lang;

public class Utils {

    public static String colorize(String string) {
	return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static void notifyStaff(Player player, int violationLevel, long time) {
	Violation violation = TempStorage.getViolation(player.getUniqueId());

	violation.updateNotify();
	TempStorage.violations.put(player.getUniqueId(), violation);

	String message = Lang.NOTIFICATION.toString();

	message = message.replaceAll("%name", player.getName());
	message = message.replaceAll("%diff", String.valueOf(time));
	message = message.replaceAll("%vLevel", String.valueOf(violationLevel));

	for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
	    if (staff.hasPermission("aas.notifications")) {
		staff.sendMessage(message);
	    }
	}

	Bukkit.getServer().getConsoleSender().sendMessage(message);
    }

    public static void performAction(Player player) {
	int violationLevel = TempStorage.getViolation(player.getUniqueId()).getViolationLevel();
	int maxAllowedViolationLevel = ConfigUtils.getMaxAllowedViolationLevel();

	if (maxAllowedViolationLevel == 0) {
	    return;
	}

	if (violationLevel < maxAllowedViolationLevel) {
	    return;
	}

	List<String> commandsRun = ConfigUtils.getCommandsRun();

	if (commandsRun.isEmpty()) {
	    return;
	}

	for (String command : commandsRun) {
	    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player", player.getName()));
	}
    }

    public static void performLogging(String message) {
	if (!ConfigUtils.isLoggingEnabled()) {
	    return;
	}

	Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
	    public void run() {
		try {
		    PrintWriter writer = new PrintWriter(new FileWriter(Main.getInstance().getDataFolder() + File.separator + "log.log", true), true);

		    if (message.isEmpty()) {
			writer.write(System.getProperty("line.separator"));
		    } else {
			String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			writer.write(time + " = " + message);
			writer.write(System.getProperty("line.separator"));
		    }

		    writer.close();
		} catch (Exception ex) {
		    Main.getInstance().getLogger().warning("There was an error while logging violation levels to the file.");
		    ex.printStackTrace();
		}
	    }
	});
    }
}
