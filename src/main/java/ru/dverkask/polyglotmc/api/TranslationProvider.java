package ru.dverkask.polyglotmc.api;

import org.bukkit.Material;

import java.util.Optional;

public interface TranslationProvider {
    Optional<String> getTranslation(Material item);
}
