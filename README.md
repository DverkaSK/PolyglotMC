<p align="center">
  <img src="./logo.png" alt="PolyglotMC Logo" width=""/>
</p>

<h1 align="center">POLYGLOT MC</h1>

<p align="center">
  <b>Version-aware Java library for translating Minecraft item names into multiple languages.</b>
</p>

<p align="center">
  <i>Because Minecraft items deserved to speak every language.</i>
</p>

---

## Overview

**PolyglotMC** is a lightweight Java library that provides **multilingual translations for Minecraft item names** across a wide range of **languages** and **Minecraft versions**.

The library fetches official Minecraft language assets, caches them efficiently, and exposes a clean API for translating names into human-readable localized strings.

---

## Features

- Supports **100+ Minecraft languages**
- Works across **many Minecraft versions**
- Automatic translation loading and caching

---

## Basic Usage

### Creating a Polyglot instance

```java
Polyglot polyglot = new Polyglot.Builder()
        .withVersion(SupportedVersion.Release.V1_20_4)
        .withLanguages(
                SupportedLanguage.EN_US,
                SupportedLanguage.RU_RU,
                SupportedLanguage.JA_JP
        )
        .withDefaultLanguage(SupportedLanguage.EN_US)
        .build();
```

### Translating game objects

Anything that implements Adventure's `Translatable` can be translated:
`Material`, `ItemStack`, `EntityType`, `Enchantment`, `PotionEffectType`, `Biome`, `Attribute`, `GameRule` and more.

```java
polyglot.translate(Material.DIAMOND_SWORD, SupportedLanguage.RU_RU).translatedName();  // Алмазный меч
polyglot.translate(Enchantment.SHARPNESS, SupportedLanguage.RU_RU).translatedName();    // Острота
polyglot.translate(EntityType.CREEPER, SupportedLanguage.RU_RU).translatedName();       // Крипер
polyglot.translate(PotionEffectType.SPEED, SupportedLanguage.RU_RU).translatedName();   // Скорость
```

### Translating raw keys

```java
polyglot.translate("gamerule.keepInventory", SupportedLanguage.RU_RU).translatedName(); // Сохранение инвентаря при смерти
```

If a key is missing in the requested language, the default language translation is returned
(or the key itself), and `TranslationResult#isTranslated()` is `false`.

### Versions

Any version from `SupportedVersion` can be used, as well as versions that are not listed yet:

```java
new Polyglot.Builder().withVersion(Version.of("26.1.2"));
```

Only Minecraft **1.13 and newer** are supported (older versions use a different language file format).

> Language files are downloaded from GitHub when `build()` is called (or on first use with dynamic loading),
> so consider building `Polyglot` asynchronously.

### Dynamic Language Loading

```java
Polyglot polyglot = new Polyglot.Builder()
        .withVersion(SupportedVersion.Release.V1_21)
        .withLanguages(SupportedLanguage.EN_US)
        .withDynamicLanguageLoading(true)
        .build();
```

---