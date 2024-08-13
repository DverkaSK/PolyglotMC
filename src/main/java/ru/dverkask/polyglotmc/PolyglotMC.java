package ru.dverkask.polyglotmc;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import ru.dverkask.polyglotmc.api.Polyglot;
import ru.dverkask.polyglotmc.api.TranslationResult;
import ru.dverkask.polyglotmc.lang.SupportedLanguage;

public class PolyglotMC extends JavaPlugin implements Listener {

    @Override public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        Polyglot polyglotMC = new Polyglot.Builder()
                .withLanguages(
                        SupportedLanguage.RU_RU,
                        SupportedLanguage.EN_US,
                        SupportedLanguage.ZLM_ARAB
                )
                .withDefaultLanguage(SupportedLanguage.RU_RU)
                .build();

        TranslationResult diamondResultRU = polyglotMC.translate(Material.DIAMOND);
        player.sendMessage(Component.text(diamondResultRU.translatedName()));

        TranslationResult diamondResultEN = polyglotMC.translate(Material.DIAMOND, SupportedLanguage.EN_US);
        player.sendMessage(Component.text(diamondResultEN.translatedName()));

        TranslationResult diamondResultARAB = polyglotMC.translate(Material.DIAMOND, SupportedLanguage.ZLM_ARAB);
        player.sendMessage(Component.text(diamondResultARAB.translatedName()));
    }
}
