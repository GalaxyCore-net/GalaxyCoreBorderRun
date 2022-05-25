package net.galaxycore.borderrun.components

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent

class NoHealthModificationComponent : Listener {
    @EventHandler fun onDamage(event: EntityDamageEvent) {
        event.isCancelled = true
    }

    @EventHandler fun onHungerChange(event: FoodLevelChangeEvent) {
        event.isCancelled = true
    }
}