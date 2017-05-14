/*
 * Copyright 2017 Comportment | comportment@diax.me
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.diax.comportment.discordbridge;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Created by Comportment at 13:08 on 14/05/17
 * https://github.com/Comportment | comportment@diax.me
 *
 * @author Comportment
 */
public class ChatListener extends ListenerAdapter implements Listener {

    private final JDA jda;
    private final Logger logger;

    private final String prefix;
    private final String bridgeServer;
    private final String bridgeChannel;

    public ChatListener(Logger logger, BotConfiguration configuration, JDA jda) {
        this.jda = jda;
        this.logger = logger;
        prefix = configuration.getBridgePrefix();
        bridgeServer = configuration.getBridgeServer();
        bridgeChannel = configuration.getBridgeChannel();
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        if (!event.getMessage().startsWith(prefix)) return;
        String message = event.getMessage().replaceFirst(Pattern.quote(prefix), "");
        try {
            jda.getGuildById(bridgeServer).getTextChannelById(bridgeChannel).sendMessage(event.getPlayer().getDisplayName() + ": " + message).queue();
        } catch (NullPointerException e) {
            logger.log(Level.SEVERE, "Could not find the guild or the channel to bridge to! Please make sure you fill in the discordbridge.properties found in the your server's directory. [Not the plugins directory]");
        } catch (PermissionException e) {
            logger.log(Level.SEVERE, "Bot does not have the permission to send messages to that channel!");
        }
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String message = event.getMessage().getRawContent();
        if (!event.getGuild().getId().equals(bridgeServer) || !event.getChannel().getId().equals(bridgeChannel) || !message.startsWith(prefix))
            return;
        message = strip(message);
        Bukkit.getServer().broadcastMessage(makeName(event.getAuthor()) + ": " + message);
    }

    private String strip(String message) {
        return ChatColor.stripColor(message.replaceFirst(Pattern.quote(prefix), ""));
    }

    private String makeName(User user) {
        return String.format("%s#%s", user.getName(), user.getDiscriminator());
    }
}