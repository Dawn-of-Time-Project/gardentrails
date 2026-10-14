<div align="center">

# Garden Trails

**Find wild plants, grow your own garden, and turn its harvest into silk, sake and more.**

[![CurseForge](https://img.shields.io/badge/CurseForge-Download-orange?style=flat-square&logo=curseforge&labelColor=0d1117)](https://www.curseforge.com/minecraft/mc-mods/garden-trails)
[![Modrinth](https://img.shields.io/badge/Modrinth-Download-1bd96a?style=flat-square&logo=modrinth&labelColor=0d1117)](https://modrinth.com/mod/garden-trails)
[![Discord](https://img.shields.io/discord/303974483591692289?color=informational&label=Discord&logo=discord&style=flat-square&logoColor=white&labelColor=0d1117)](https://discord.gg/GfPPxR7eg3)
[![Patreon](https://img.shields.io/badge/Support-Patreon-f96854?style=flat-square&logo=patreon&labelColor=0d1117)](https://www.patreon.com/dawnoftimemod)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow?style=flat-square&labelColor=0d1117)](LICENSE.md)

</div>

<div align="center"><img src="https://i.imgur.com/yhha6Zo.png"></div>

## 🌿 What is Garden Trails?

**Garden Trails** is an exploration and crafting mod about plants. You travel the world to **find wild plants**, **harvest them**, and bring them home to build **your own garden**: flower pots, planters, pergolas, trees. Every plant lives next to the others and together they form a small ecosystem that you design.

Then you put that harvest to work. **Dry it, ferment it, feed it to the creatures that live around you**, and you get silk, tea, sake and even a bird companion that makes your garden grow faster.

No required dependency, and everything blends into vanilla. It also plays nicely with [Dawn of Time](https://www.curseforge.com/minecraft/mc-mods/dawn-of-time) and [Armor of the Ages](https://www.curseforge.com/minecraft/mc-mods/armor-of-the-ages) (see [Compatibility](#compatibility)).

<div align="center"><img src="https://i.imgur.com/yhha6Zo.png"></div>

## Plants

### Wild plants

| Plant | Where to find it | What you get                                        |
|---|---|-----------------------------------------------------|
| **Boxwood Bush** | Forests | Edges (short and tall) to shape your garden         |
| **Camellia** | Jungles, savannas, taigas | Leaves that dry into **tea**                        |
| **Commelina** | Swamps, flower forests | A blue flower that dries into **blue petals**       |
| **Cypress** | Savannas | A tall decorative tree-like plant                   |
| **Geraniums** | Flower forests, meadows, birch forests | Six colors, dye, planters                           |
| **Ivy** | Forests, dark forests | A climbing plant                            |
| **Mulberry** | Taigas | **Mulberry leaves** (silk) and **mulberry juice** (sake) |
| **Rice** | Swamps | **Rice** for cooking and sake                       |
| **Wild Grape** | Forests, dark forests | **Grapes** and **grape seeds**                      |
| **Wild Maize** | Savannas, plains | **Maize**                                           |
| **Maple trees** | Birch forests, dark forests | Red, orange and green autumn trees                  |

> Every biome list is editable in the [config](#configuration).

<div align="center"><img src="https://i.imgur.com/yhha6Zo.png"></div>

## Crafting systems

### Drying tray

Available in **nine wood types**. Place items on the tray, wait, and they turn into something new. Up to 4 items at a time.

| Put on the tray | You get |
|---|---|
| Camellia leaves | **Tea leaves** |
| Commelina | **Blue petals** |
| Grapes | **Dried grapes** |
| Maize | **Dried maize** |
| Moldy rice | **Fermented rice** |
| Mulberry juice | **Fermented mulberry juice** |
| Silk worm hatchery | **Silk worms** |

### Silk

1. **Plant a mulberry.** It attracts **Silkmoths**. Put a light nearby to keep them around.
2. **Wait for the eggs.** A Silkmoth drops about three **silk worm eggs** during its short life. Killing it gives a smaller chance of eggs.
3. **Craft a hatchery.** Silk worm eggs + 2 mulberry leaves.
4. **Dry the hatchery** on a drying tray to get **silk worms**.
5. **Place the worms on a stick bundle** (sticks and string) and wait for them to grow, then harvest the **cocoons**.
6. **Craft 3 cocoons** into **silk**.

Silk is used in recipes of other mods (see [Compatibility](#compatibility)). You can also craft **silk worm eggs** back from a cocoon, to start again.

### Sake

Sake is brewed in the **Sake Vat**. Each batch needs **three ingredients**, poured one by one, then the vat closes and ferments. When it is ready, right click it with **glass bottles** to collect your sake.

**Step 1, prepare the base: fermented rice**

1. **Wash** rice by right clicking a **water cauldron** with it. You get *washed rice*.
2. **Cook** the washed rice in a furnace. You get *cooked rice*.
3. Mix cooked rice with **mold** to get *moldy rice*.
4. **Dry** moldy rice on a drying tray. You get **fermented rice**.

> **Where does mold come from?** Look for the **rotten tree** in swamps and **brush** the rotten oak log with a vanilla brush.

**Step 2, fill the vat**

Every sake uses the same base: **a bottle of water + fermented rice + one flavor**.

| Flavor | Result | Boost | Price to pay |
|---|---|---|---|
| Cooked rice | **Sake** | Saturation | Slowness |
| Mulberry juice (fermented) | **Mulberry sake** | Regeneration | Mining fatigue |
| Crushed grape | **Grape sake** | Speed | Hunger |
| Crushed maize | **Maize sake** | Strength | Damage |
| Tea leaves | **Tea sake** | Haste | Weakness |

Each sake gives a **1 minute boost** followed by a short penalty. Drink several in a row and the effects stack. Drink at your own risk.

### Rotten wood

Found in the **rotten tree** structure in swamps. Rotten oak logs can be crafted into a full set of decorative blocks (planks, stairs, slab, edge, plate, fence, fence gate, trapdoor).

<div align="center"><img src="https://i.imgur.com/yhha6Zo.png"></div>

## Entities

### Silkmoth
A small flying insect that spawns around **mulberry plants**. It lives about 20 minutes and drops silk worm eggs while it flies. It is the starting point of the [silk chain](#-silk).

### Quetzal
A colorful bird that lives in **jungles**.

| Action | Result |
|---|---|
| **Wait and watch** | It drops blue, green, red and regular feathers while flying |
| **Craft a Quetzal Offering** and give it to the bird | Tames it (1 chance in 3 per offering) |
| **Right click** your tamed Quetzal | Cycles between **follow**, **sit** and **garden mode** |
| **Follow mode** | It can land on your shoulder and gives you **Quetzal Trust**, a speed bonus |
| **Garden mode** | It stays where you left it and **speeds up the growth of plants around it** (crops, saplings, pergola plants, mulberry, camellia and more) |

<div align="center"><img src="https://i.imgur.com/yhha6Zo.png"></div>

## Compatibility

| Mod | What changes |
|---|---|
| [Dawn of Time](https://www.curseforge.com/minecraft/mc-mods/dawn-of-time) | Some futon, tatami, chair, flag and cushion recipes use **silk** |
| [Armor of the Ages](https://www.curseforge.com/minecraft/mc-mods/armor-of-the-ages) | Several armor recipes use **silk**. The Quetzalcoatl armor uses **Quetzal feathers**. Wearing the Quetzalcoatl chestplate with a Quetzal on your shoulder gives **Reunited Brothers**: Quetzal Trust speed plus reduced damage |
| [Critters and Companions](https://www.curseforge.com/minecraft/mc-mods/critters-and-companions) | Silk, silk lead and grappling hook recipes |
| [Let's Do Vinery](https://www.curseforge.com/minecraft/mc-mods/lets-do-vinery) | Garden Trails grapes can be crafted into a red grape bag |
| [Farmer's Delight](https://www.curseforge.com/minecraft/mc-mods/farmers-delight) | Rice can be crafted into a bag of rice, and works on its rich soil |
| Aether, Immersive Weathering | Plants can grow on their farmland |
| **JEI** | Dedicated **Drying** and **Fermenting** recipe categories |

Everything is **datadriven**. The tags `gardentrails:quetzal_boosted`, `gardentrails:tameable_birds` and `gardentrails:quetzal_bond_chest` let modpack authors add their own plants, birds and chestplates. The `gardentrails:fermenting` and `gardentrails:dryer` recipe types can be used in datapacks.

<div align="center"><img src="https://i.imgur.com/yhha6Zo.png"></div>

## Configuration

The config file is `config/gardentrails-config.txt`, generated on first launch. A restart is required after any change.

| Option | Effect |
|---|---|
| `generateChestLoot` | Adds Garden Trails items to vanilla chest loot |
| `generateSilk`, `generateGrapes`, `generateMaize`, `generateRice`, `generateMulberry` | Toggles the matching loot additions |
| `dawnOfTimeUseSilk` | Enables the silk recipes for Dawn of Time |
| `armorOfTheAgesUseSilk` | Enables the silk recipes for Armor of the Ages |
| `armorOfTheAgesUseQuetzalFeathers` | Makes the Quetzalcoatl armor recipes use Garden Trails feathers |
| `<plant>_biomes` | Comma separated list of biome IDs where the plant spawns |

Biome IDs use the full form `namespace:biome_name`, so biomes from other mods can be added (for example `byg:jacaranda_forest`). IDs from mods that are not installed are ignored. To stop a plant from spawning, leave the list empty:

```
boxwood_bush_biomes=
```

<div align="center"><img src="https://i.imgur.com/yhha6Zo.png"></div>

## Credits

- **Development, models and textures** by [Poulpinou](https://github.com/PierreChag) & [TheGoldenWorld](https://github.com/TheGoldenWorld).
- **Maple tree** by *Knoxxturre*.
- **Quetzal bird** by *Hadrim*
- **Our community:** thank you all for constantly helping us improve the mod. ❤️

Garden Trails is released under the [MIT License](LICENSE.md).

<div align="center"><img src="https://i.imgur.com/yhha6Zo.png"></div>

## Socials

<a href="https://linktr.ee/TheGoldenWorld"><img src="https://i.imgur.com/Hpz5KXl.png" alt="Socials"></a>
<br><br>
