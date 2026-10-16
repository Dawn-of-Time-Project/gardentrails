## 🌱 Garden Trails Patch Notes: 1.21.11 v0.3.0

![new blocks](https://i.imgur.com/PnG9gNy.jpeg)

Portage of the 1.21.1 to 1.21.11

### Fixes
- Fixed the silk worms not growing on the Stick Bundle: the growth code ran in `tick` instead of `randomTick`. Since 1.21 `randomTick` no longer calls `tick`, so the random ticks never reached it
- Fixed the Ivy not growing or spreading in the wild (it only grew on a lattice), same cause
- Fixed the Maple Sapling never growing into a tree, same cause