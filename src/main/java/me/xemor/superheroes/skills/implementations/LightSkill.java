package me.xemor.superheroes.skills.implementations;

import me.xemor.superheroes.Superhero;
import me.xemor.superheroes.data.HeroHandler;
import me.xemor.superheroes.events.PlayerGainedSuperheroEvent;
import me.xemor.superheroes.events.PlayerLostSuperheroEvent;
import me.xemor.superheroes.skills.Skill;
import me.xemor.superheroes.skills.skilldata.LightSkillData;
import me.xemor.superheroes.skills.skilldata.SkillData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;

public class LightSkill extends SkillImplementation {
    public LightSkill(HeroHandler heroHandler) {
        super(heroHandler);
    }

    @EventHandler
    public void onGain(PlayerGainedSuperheroEvent e) {
        Superhero superhero = heroHandler.getSuperhero(e.getPlayer());
        if (superhero.hasSkill(Skill.getSkill("LIGHT"))) {
            runnable(e.getPlayer());
        }
    }

    @EventHandler
    public void onLost(PlayerLostSuperheroEvent e) {
        Superhero superhero = heroHandler.getSuperhero(e.getPlayer());
        Collection<SkillData> skillDatas = superhero.getSkillData(Skill.getSkill("LIGHT"));
        for (SkillData skillData : skillDatas) {
            LightSkillData lightData = (LightSkillData) skillData;
            e.getPlayer().removePotionEffect(lightData.getPotionEffect().getType());
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Superhero superhero = heroHandler.getSuperhero(e.getPlayer());
        if (superhero.hasSkill(Skill.getSkill("LIGHT"))) {
            runnable(e.getPlayer());
        }
    }

    public void runnable(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Superhero superhero1 = heroHandler.getSuperhero(player);
                Collection<SkillData> data = superhero1.getSkillData(Skill.getSkill("LIGHT"));
                if (data.isEmpty()) {
                    this.cancel();
                    return;
                }
                for (SkillData skillData : data) {
                    LightSkillData lightData = (LightSkillData) skillData;
                    if (player.getWorld().getBlockAt(player.getLocation()).getLightLevel() > 10) {
                        if (lightData.areConditionsTrue(player)) {
                            player.addPotionEffect(lightData.getPotionEffect());
                        }
                    }
                    else {
                        player.removePotionEffect(lightData.getPotionEffect().getType());
                    }
                }
            }
        }.runTaskTimer(heroHandler.getPlugin(), 0L, 20L);
    }


}
