package me.xemor.superheroes2;

import de.themoep.minedown.adventure.MineDown;
import me.xemor.superheroes2.skills.skilldata.configdata.Cooldown;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.UUID;

public class SkillCooldownHandler {

    private final HashMap<Cooldown, HashMap<UUID, Long>> cooldownMap = new HashMap<>();

    public void startCooldown(Cooldown skillData, double cooldown, UUID uuid) {
        HashMap<UUID, Long> hashMap = cooldownMap.getOrDefault(skillData, new HashMap<>());
        hashMap.put(uuid, (long) (cooldown * 1000) + System.currentTimeMillis());
        cooldownMap.put(skillData, hashMap);
    }

    public void startCooldown(Cooldown skillData, UUID uuid) {
        startCooldown(skillData, skillData.getCooldown(), uuid);
    }

    public boolean isCooldownOver(Cooldown skillData, UUID uuid) {
        HashMap<UUID, Long> hashMap = cooldownMap.getOrDefault(skillData, new HashMap<>());
        if (hashMap.containsKey(uuid)) {
            if (hashMap.get(uuid) <= System.currentTimeMillis()) {
                return true;
            }
            long seconds = ((hashMap.get(uuid) - System.currentTimeMillis()) / 1000);
            Component cooldownMessage = new MineDown(skillData.getCooldownMessage()).replace("currentcooldown", String.valueOf(seconds)).toComponent();
            Superheroes2.getBukkitAudiences().player(Bukkit.getPlayer(uuid)).sendActionBar(cooldownMessage);
            return false;
        }
        else {
            return true;
        }
    }
}
