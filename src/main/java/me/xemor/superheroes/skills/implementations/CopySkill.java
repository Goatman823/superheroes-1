package me.xemor.superheroes.skills.implementations;

import me.xemor.superheroes.data.HeroHandler;

public class CopySkill extends SkillImplementation {
    public CopySkill(HeroHandler heroHandler) {
        super(heroHandler);
    }
/*
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player copier = (Player) e.getDamager();
            heroHandler.getSuperhero(copier);
        }
    }
 */
}
