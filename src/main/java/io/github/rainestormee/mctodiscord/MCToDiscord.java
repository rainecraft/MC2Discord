package io.github.rainestormee.mctodiscord;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.BroadcastMessageEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class MCToDiscord extends JavaPlugin implements Listener {

    private WebhookClient client;

    @Override
    public void onEnable() {
        // Plugin startup logic
        FileConfiguration configuration = this.createCustomConfig();
        try {
            client = new WebhookClientBuilder(configuration.getString("webhook-url")).build();
            this.getServer().getPluginManager().registerEvents(this, this);
        } catch (IllegalArgumentException | NullPointerException e) {
            this.getLogger().severe("Invalid web-hook URL! Please edit the config.yml file. See https://github.com/rainestormee/MC2Discord/wiki/Setup for more information.");
            this.getPluginLoader().disablePlugin(this);
        }
    }

    private YamlConfiguration createCustomConfig() {
        File customConfigFile = new File(getDataFolder(), "config.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }

        YamlConfiguration customConfig = new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
            return customConfig;
        } catch (IOException | InvalidConfigurationException e) {
            return null;
        }
    }

    @Override
    public void onDisable() {
        if (client != null) {
            client.close();
        }
    }

    @EventHandler
    public void onBroadCast(BroadcastMessageEvent event) {
        sendMessage(null, event.getMessage());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        sendMessage(event.getPlayer(), event.getMessage());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        sendMessage(null, event.getJoinMessage());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        sendMessage(event.getQuitMessage());
    }

    private void sendMessage(String message) {
        sendMessage(null, message);
    }

    private void sendMessage(Player player, String message) {
        WebhookMessageBuilder messageBuilder = new WebhookMessageBuilder();
        if (player != null) {
            messageBuilder.setUsername(player.getDisplayName()).setAvatarUrl("https://crafatar.com/renders/head/" + player.getUniqueId() + ".png");
        }
        messageBuilder.setContent(ChatColor.stripColor(message).replace("@everyone", "@" + "\u200B" + "everyone").replace("@here", "@" + "\u200B" + "here"));
        try {
            client.send(messageBuilder.build()).get();
        } catch (Exception e) {
            this.getLogger().severe("Failed to send message to Discord. Check your config.");
            e.printStackTrace();
        }
    }
}
