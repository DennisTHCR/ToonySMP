# Reasoning
With the current whitelisting system through Twitch, sometimes the `/whitelist` command fails. Making our own whitelist system could resolve this issue.
# Solution
Save whitelisted players' UUID in the database. Try to get their UUID from the Minecraft API. If it can't find the name, refund the channel points. If it can, mark the channel point redemption as completed.