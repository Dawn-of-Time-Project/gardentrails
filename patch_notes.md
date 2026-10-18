## 🌱 Garden Trails Patch Notes v0.1.14

![new blocks](https://i.imgur.com/PnG9gNy.jpeg)

► Full patchnote here: https://www.curseforge.com/minecraft/mc-mods/garden-trails/files/9183592

### New Content
- Added the Rotten Oak Sapling and its flower pot, it grows an oak shaped tree made of Rotten Oak Logs that can be brushed again to collect more mold
- Added the Flower Planter, a wider flower pot made of bricks placed against a wall on the lower or upper half of a block, it holds any plant a flower pot can hold, from this mod, vanilla or other mods, shown with the same model as in the pot
- Added the Rotten Arrow, it deals less damage than a regular arrow but applies Poison and Hunger for 5 seconds on hit
- Added a socials button on the Garden Trails creative tab, it opens the socials links from the DoT project
- Added the Japanese Light Armor boots recipe (Armor of the Ages) still using string instead of silk
- Mold can now be found in the chests of simple dungeons (5%) and of strongholds, in the library, corridors and crossings (5%)
- Colored arrows (25%) and colored feathers (50%) can now be found in the chests of jungle temples, the color is random
- New config options generateMold, generateArrows and generateFeathers to turn these chest additions off
### Fixed
- Fixed the planter turning around when placed on the lower part of a block
- Fixed the crash at launch on server with Fabric
- Plants can now be placed anywhere on a pergola in survival (no more dirt or axis requirement) and consume the item
- Pergolas with plants now drop the pergola when broken, plus the plant when it is mature
- Fixed the oxidized copper lattice recipe
- Fixed the sake vat selection overlay and the missing faces around the liquid
- Fixed the chest loot (silk, grape, maize, rice, mulberry in shipwrecks and village houses) that never appeared with Forge, and its chance being rolled twice
### Changed
- Wild rice now spawns twice as often
- Potted plants now use the vanilla flower pot: put a plant on an empty flower pot to get the potted version, an empty hand gives the plant back
- Potted plants are no longer craftable nor in the creative tab, and the potted items in inventories are removed, placed pots in the world are kept
- Plants that fit in a flower pot now show it in their tooltip
- The geranium planters are replaced by the Flower Planter: put a geranium in it to get the planter with the hanging leaves, placed geranium planters in the world are removed
- Plants now spawn in the biomes of Terralith, Biomes O' Plenty, Regions Unexplored, Oh The Biomes We've Gone, Atmospheric and Nature's Spirit, so they keep spawning in modpacks that replace the vanilla biomes
- The mulberry is now in the forge:fruits/mulberry tag, so other mods that use this tag (like Upgrade Aquatic) recognize it as a mulberry
- The tea leaves are now in the farmersrespite:tea_leaves tag, so they can be used to brew dandelion tea in the Farmer's Respite kettle
- The red and orange maple trees now also spawn in the Biomes O' Plenty Maple Woods
### Other
- Removed unused code and made a ton of internal code changes