[![downloads](https://img.shields.io/badge/-⬇%20releases-brightgreen)](https://github.com/makamys/Satchels/releases)
[![CurseForge](https://shields.io/badge/CurseForge-555555?logo=curseforge)](https://www.curseforge.com/minecraft/mc-mods/satchels)

# Satchels

This mod adds **pouches** and **satchels**, which act as wearable inventory upgrades. Unlike your typical backpack mod, it's seamlessly integrated into the inventory screen instead of being on a separate screen.

![](https://raw.githubusercontent.com/makamys/Satchels/master/docs/satchels_gui.png)

*Pictured: an unupgraded pouch (the colored column on the left), a fully upgraded pouch (the colored column on the right) and a satchel (the colored row)*

## Pouches

A pouch adds 3 slots to your inventory, and can be worn on the left or the right side. You only need some leather and some string to craft it.

Its capacity can be increased with **pouch expansions**, which can only be found inside dungeon chests. Each expansion will add an additional slot to the pouch's capacity, with the maximum being 8 slots. To apply a pouch upgrade, right click a pouch to open its upgrade inventory where you can insert it.

You can wear up to 2 pouches at the same time.

## Satchel

A satchel adds an entire row (9 slots) of storage to your inventory. But its utility is matched by its cost: crafting it requires a full diamond block and some slime by default. You can only wear 1 satchel at a time.

## Models

The items you wear will show up as 3D models on the player. This can be disabled in the config.

![](https://raw.githubusercontent.com/makamys/Satchels/master/docs/satchels_model.png)

# Dependencies

This mod requires CodeChickenLib (installed by [CodeChickenCore](https://www.curseforge.com/minecraft/mc-mods/codechickencore)).

## Suggested mods

If you want more inventory expansion options in your earlygame, check out [D-Mod](https://github.com/makamys/DMod)'s bundle backport!

# Incompatibilities

* [Aether Legacy](https://www.curseforge.com/minecraft/mc-mods/the-aether): accessories button doesn't show up in inventory. [This is a bug on their side.](https://github.com/gildedgames/aether-issues/issues/742) Until it's fixed, use the keybind to open the equipment GUI.
* Various coremods (for example, Factorization) will crash during startup due to an incompatibility with Mixin. This is fixed by [Mixingasm](https://github.com/makamys/Mixingasm).

# License

This mod is licensed under the [MIT License](https://github.com/makamys/Satchels/blob/master/LICENSE).

# Contributing

When running in an IDE, add these program arguments
```
--tweakClass org.spongepowered.asm.launch.MixinTweaker --mixin satchels.mixin.json
```
