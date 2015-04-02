# iTranxCraft

[![Build Status](https://drone.io/github.com/TranxCraft/iTranxCraft/status.png)](https://drone.io/github.com/TranxCraft/iTranxCraft/latest)

### Description
iTranxCraft is the improved version of TranxCraft, the custom plugin that we run on the server to add lots of cool features that we hope will improve the experience for both players and admins on our server.

### Why bother rewriting the plugin?
We opted to rewrite the plugin over improving what was already done, as the previous plugin needed a lot of core changes that would be cumbersome to implement. This meant that a rewrite would be simpler to implement than changing the existing plugin. This also gave us scope to switch to a different API, like [Sponge](https://spongepowered.org). However for now at least we have decided to stick with Craftbukkit/Bukkit as it's what we're familiar with and we can test our code.

### Can I use this plugin on my server?
You can, yes, as long as you comply with the [license](https://github.com/TranxCraft/License/blob/master/LICENSE.md). You will need to compile the source code yourself and make sure you have everything setup as needed. This plugin has a **_requirement_** on MySQL. Without it, the plugin will disable itself and you won't be able to use it. If you don't have all of the configuration entries set in the config table, a lot of the plugin will not work. So if you're interested in using this plugin, please be ready for the amount of work you'll need to put in - we will not help you with any issues unless they're to do with the plugin.

### Am I allowed to rename the plugin to use on my server?
There's nothing stopping you from doing so, however we highly discourage it. You should give credit back to where you found the plugin. You wouldn't rename Essentials, WorldEdit, etc. on your server would you? This plugin isn't any different.

### I'm interested in contributing to the plugin, how?
If you would like to contribute to this plugin, please see our contributing guide [here](https://github.com/TranxCraft/iTranxCraft/blob/master/CONTRIBUTING.md).
