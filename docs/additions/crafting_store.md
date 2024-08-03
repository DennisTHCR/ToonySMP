# Reasoning
The CraftingStore plugin has lots of functionality we don't need, thus bloating the plugin.
Furthermore, CraftingStore requires Vault, which we want to remove.
# Solution
The CraftingStore API will need to be integrated into the base plugin, or handled by an external application.
The plugin will then need to react to incoming changes, adding / removing groups as needed.