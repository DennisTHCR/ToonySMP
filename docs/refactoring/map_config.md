# Reasoning
The current configuration system is a pain to work with, and isn't extensible (enough).
# Solution
The new configuration will use the following layout:<br>
ConfigManager<br>
|- - ConfigWrapper<br>
|- - |- - RecipeConfig<br>
|<br>
|- - ConfigWrapper<br>
|- - |- - ModuleConfig<br>
|<br>
|- - ConfigWrapper<br>
|- - |- - RankConfig<br>
...<br>
All Configs will have their own Option Enum. These Enums all implement the Option interface:<br>
```kt
interface Option {
    val value: Any
    val type: Class<*>
}
```
All Config classes will then implement the Config interface. The config interface will define the following:<br>

```kt
// TODO: Add saving / loading functionality (100% safe this time)
import dev.jorel.commandapi.CommandAPICommand
import java.util.EnumMap

interface Config<T>
        where T : Enum<T>,
              T : Option {
    val configMap: EnumMap<T, Any>

    @Suppress("UNCHECKED_CAST")
    fun <V> get(configOption: T): V {
        return configMap[configOption] as V
    }

    fun set(configOption: T, value: Any) {
        configMap[configOption] = value
    }

    val configSubcommand: CommandAPICommand
    val name: String
}
```
The ConfigWrapper will then pass through the functions of the classes, as well as initializing them.<br>
The ConfigManager will first load the main config, then decide which configs need to be loaded.