<div align=center><img src="https://github.com/MARYT-Studio/GoldenFinger/blob/master/logo.png" /></div>

# GoldenFinger

## Overview

Simple Forge mod for Minecraft 1.18.2.

It only do one thing:

Fill the chest you clicked on with the loot table of your choice.

Highly customizable for debugging loot tables, or some *special* gameplay if you want.

## Features

1. GoldenFinger determines whether a player can use its feature or not by the boolean value of this player's NBT tag `ForgeData\golden_finger`.

    Player may control it with command `/golden_finger`, while you may determine this command is accessible for non-OP players or OP only via config file `golden_finger-server.toml`.

    As you can see, it's a server config. If you are using it in a modpack, consider put the config file in your `defaultconfig` directory. 

   Or, if you are using other modpack tweaking mods (CraftTweaker, KubeJS or something like), you may also control that NBT tag's value directly if you like.
2. GoldenFinger let you to use a JSON in its datapack(provided in the [Release](https://github.com/MARYT-Studio/GoldenFinger/releases/tag/Datapack)) to customize it. Read the link for more detailed instructions.
