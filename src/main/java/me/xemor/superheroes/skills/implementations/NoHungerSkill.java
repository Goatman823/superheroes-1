package me.xemor.superheroes.skills.implementations;

import me.xemor.superheroes.data.HeroHandler;
import me.xemor.superheroes.skills.Skill;
import me.xemor.superheroes.skills.skilldata.SkillData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import java.util.Collection;

public class NoHungerSkill extends SkillImplementation {
    public NoHungerSkill(HeroHandler heroHandler) {
        super(heroHandler);
    }

    @EventHandler
    public void onSaturation(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player player) {
            if (e.getFoodLevel() <= player.getFoodLevel()) {
                Collection<SkillData> skillDatas = heroHandler.getSuperhero(player).getSkillData(Skill.getSkill("NOHUNGER"));
                for (SkillData skillData : skillDatas) {
                    if (heroHandler.getSuperhero(player).hasSkill(Skill.getSkill("NOHUNGER"))) {
                        if (skillData.areConditionsTrue(player)) {
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
