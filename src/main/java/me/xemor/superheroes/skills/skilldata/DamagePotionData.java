package me.xemor.superheroes.skills.skilldata;

import me.xemor.superheroes.skills.skilldata.configdata.PotionEffectData;
import me.xemor.superheroes.skills.skilldata.exceptions.InvalidConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class DamagePotionData extends SkillData {

    private PotionEffect potionEffect;
    private HashSet<EntityDamageEvent.DamageCause> damageCauses;

    public DamagePotionData(int skill, ConfigurationSection configurationSection) throws InvalidConfig {
        super(skill, configurationSection);
        List<String> damageCausesStr = configurationSection.getStringList("damageCause");
        if (damageCausesStr.contains("ALL")) {
            damageCauses = null;
        }
        damageCauses = damageCausesStr.stream().map(EntityDamageEvent.DamageCause::valueOf).collect(Collectors.toCollection(HashSet::new));
        PotionEffectType type = PotionEffectType.getByName(configurationSection.getString("type", ""));
        if (type == null) {
            throw new InvalidConfig("Invalid potion effect type specified in damage potion skill " + configurationSection.getCurrentPath());
        }
        potionEffect = new PotionEffectData(configurationSection, type , 0, 0).getPotionEffect();
    }

    public PotionEffect getPotionEffect() {
        return potionEffect;
    }

    public HashSet<EntityDamageEvent.DamageCause> getDamageCause() {
        return damageCauses;
    }
}