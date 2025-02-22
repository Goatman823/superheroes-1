package me.xemor.superheroes.commands;

import me.xemor.superheroes.CooldownHandler;
import me.xemor.superheroes.Superheroes;
import me.xemor.superheroes.data.ConfigHandler;
import me.xemor.superheroes.data.HeroHandler;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Reroll implements SubCommand, Listener {

    private final HeroHandler heroHandler;
    private final ConfigHandler configHandler;
    private final boolean isEnabled;
    private final Component noPermission = MiniMessage.miniMessage().deserialize("<dark_red>You do not have permission to use this power!");
    private final CooldownHandler cooldownHandler = new CooldownHandler("", ChatMessageType.ACTION_BAR);

    public Reroll(HeroHandler heroHandler, ConfigHandler configHandler) {
        this.heroHandler = heroHandler;
        this.configHandler = configHandler;
        isEnabled = configHandler.isRerollEnabled();
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (isEnabled) {
                if (configHandler.getRerollItem().matches(item)) {
                    if (cooldownHandler.isCooldownOver(e.getPlayer().getUniqueId())) {
                        item.setAmount(item.getAmount() - 1);
                        heroHandler.setRandomHero(player);
                        cooldownHandler.startCooldown(configHandler.getRerollCooldown(), player.getUniqueId());
                    }
                }
            }
        }
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Audience audience = Superheroes.getBukkitAudiences().sender(sender);
        if (sender.hasPermission("superheroes.reroll")) {
            Player player;
            if (args.length >= 2) {
                player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    audience.sendMessage(MiniMessage.miniMessage().deserialize(configHandler.getInvalidPlayerMessage(), Placeholder.unparsed("player", sender.getName())));
                    return;
                }
            }
            else {
                if (sender instanceof Player) {
                    player = (Player) sender;
                }
                else {
                    return;
                }
            }
            heroHandler.setRandomHero(player);
        }
        else {
            audience.sendMessage(noPermission);
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
