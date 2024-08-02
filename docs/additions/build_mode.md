# Reasoning
Building is a core concept of the SMP. Building something great in survival can be difficult,
which leads to many people using third party mods to have schematics displayed.
Adding the ability to prototype in creative mode, directly on the server, and then having it displayed in the world
would help create better builds faster.
# Solution
## Build Command
A new command, `/build <name>` will take you to the build world, in which all Block Place and Destroy events will be saved to a file, named `builds/<playerName>/<buildName>`.<br>
## Load Build Command
### Spawning the entities
Then, when a player use the `/loadbuild <name>` command, that file should be read, and BlockDisplay Entity spawn events should be sent to the player through nms, to visualize.
Make sure to compare the materials with the ones noted in the file, to make sure you don't spawn unnecessary entities. Also, make sure to keep the Entity IDs in a Map ((Player, Build) -> [Entity IDs])<br>
When blocks should be destroyed, spawn a BlockDisplay with Air as its material, change the team color to red and give it the glow effect.
### After entities were spawned
When a player places a block, update the BlockDisplay entity.<br>
Right Material -> Kill Entity<br>
Wrong Material -> Red Glow<br>
Destroyed Block that shouldn't be Air -> Spawn Entity<br>
Destroy Block that should be Air -> Kill Entity<br>
Furthermore, whenever an Entity is killed or spawned, update the Map keeping track of them.
## Unload Build Command
The `/unload <name>` should send the entity kill events to the Player for all the BlockDisplays that make up the build (get them from the Map).