package com.therealspoljo.antiautosoup.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import com.therealspoljo.antiautosoup.utilities.TempStorage;

public class EntityDamage implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
	if (!(event.getEntity() instanceof Player)) {
	    return;
	}

	if (event.getCause() == EntityDamageEvent.DamageCause.SUICIDE) {
	    return;
	}

	TempStorage.setLastAttackTime(((Player) event.getEntity()).getUniqueId());
    }
}
