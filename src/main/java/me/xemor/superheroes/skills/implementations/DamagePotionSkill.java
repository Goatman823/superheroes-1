package me.xemor.superheroes.skills.implementations;

import me.xemor.superheroes.Superhero;
import me.xemor.superheroes.data.HeroHandler;
import me.xemor.superheroes.events.PlayerLostSuperheroEvent;
import me.xemor.superheroes.skills.Skill;
import me.xemor.superheroes.skills.skilldata.DamagePotionData;
import me.xemor.superheroes.skills.skilldata.DamageResistanceData;
import me.xemor.superheroes.skills.skilldata.SkillData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Collection;

public class DamagePotionSkill extends SkillImplementation {

    public DamagePotionSkill(HeroHandler heroHandler) {
        super(heroHandler);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) e.getEntity();
        Superhero superhero = heroHandler.getSuperhero(player);
        Collection<SkillData> skillDatas = superhero.getSkillData(Skill.getSkill("DAMAGEPOTION"));
        for (SkillData skillData : skillDatas) {
            DamagePotionData damagePotionData = (DamagePotionData) skillData;
            if (damagePotionData.getDamageCause() == null || damagePotionData.getDamageCause().contains(e.getCause())) {
                if (damagePotionData.getPotionEffect() != null) {
                    if (!player.hasPotionEffect(damagePotionData.getPotionEffect().getType())) {
                        if (damagePotionData.areConditionsTrue(player)) {
                            player.addPotionEffect(damagePotionData.getPotionEffect());
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPowerLoss(PlayerLostSuperheroEvent e) {
        Player player = e.getPlayer();
        Superhero superhero = heroHandler.getSuperhero(player);
        Collection<SkillData> skillDatas = superhero.getSkillData(Skill.getSkill("DAMAGERESISTANCE"));
        if (!skillDatas.isEmpty()) {
            for (SkillData skillData : skillDatas) {
                DamageResistanceData damageResistanceData = (DamageResistanceData) skillData;
                if (damageResistanceData.getPotionEffect() != null) {
                    player.removePotionEffect(damageResistanceData.getPotionEffect().getType());
                }
            }
        }
    }


}