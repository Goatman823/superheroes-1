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

public class DamageResistanceData extends SkillData {

    private double damageMultiplier;
    private PotionEffect potionEffect;
    private HashSet<EntityDamageEvent.DamageCause> damageCauses;

    public DamageResistanceData(int skill, ConfigurationSection configurationSection) throws InvalidConfig {
        super(skill, configurationSection);
        damageMultiplier = configurationSection.getDouble("damageMultiplier", 0);
        List<String> damageCausesStr = configurationSection.getStringList("damageCause");
        if (damageCausesStr.contains("ALL")) {
            damageCauses = null;
        }
        damageCauses = damageCausesStr.stream().map(EntityDamageEvent.DamageCause::valueOf).collect(Collectors.toCollection(HashSet::new));
        if (configurationSection.contains("type")) {
            PotionEffectType type = PotionEffectType.getByName(configurationSection.getString("type", ""));
            if (type == null) {
                throw new InvalidConfig("Invalid potion effect type specified in damage resistance skill " + configurationSection.getCurrentPath());
            }
            potionEffect = new PotionEffectData(configurationSection, type, 0, 0).getPotionEffect();
        }
    }

    public double getDamageMultiplier() {
        return damageMultiplier;
    }

    public PotionEffect getPotionEffect() {
        return potionEffect;
    }

    public HashSet<EntityDamageEvent.DamageCause> getDamageCause() {
        return damageCauses;
    }
}
