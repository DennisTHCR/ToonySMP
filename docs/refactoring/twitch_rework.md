# Reasoning
Currently, the connection with Twitch can be a bit wonky. This is due to the handling of the Twitch API not being complete.<br>
Of course, we can't have this instability.
# Solution
The whole Webserver and TwitchAPI parsing aspect will be offloaded to a separate program.
The Minecraft Server will host a REST API, that will be used for communication between the two systems.