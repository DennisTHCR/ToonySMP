# Reasoning
Although JSON is nice for things like configuration files, it isn't the optimal solution for data storage, like for an [economy](currency.md) or a [custom whitelist implementation](whitelist.md).
# Solution
Setting up a Database isn't hard. However, it needs to be considered when designing the plugin.
This file mainly exists to explicitly tell you to NOT host the database directly inside the plugin.