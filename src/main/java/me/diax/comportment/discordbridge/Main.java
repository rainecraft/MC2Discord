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

import com.knockturnmc.api.util.ConfigurationUtils;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.ShutdownEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Comportment at 22:40 on 13/05/17
 * https://github.com/Comportment | comportment@diax.me
 *
 * @author Comportment
 */
public final class Main extends JavaPlugin {

    private final Logger logger = this.getLogger();
    private final BotConfiguration configuration;
    private JDA jda;

    public Main() {
        configuration = ConfigurationUtils.loadConfiguration(getClassLoader(), "discordbridge.properties", new File(System.getProperty("user.dir")), BotConfiguration.class);
    }

    @Override
    public void onEnable() {
        logger.log(Level.INFO, "Enabling plugin...");
        try {
            jda = new JDABuilder(AccountType.BOT).setToken(configuration.getBotToken()).addEventListener(new ListenerAdapter() {
                @Override
                public void onReady(ReadyEvent event) {
                    logger.log(Level.INFO, "The bot has started.");
                }

                @Override
                public void onShutdown(ShutdownEvent event) {
                    logger.log(Level.INFO, "The bot has shutdown.");
                }
            }).buildBlocking();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not start the bot!\n" + e.getMessage());
        }
        this.getServer().getPluginManager().registerEvents(new ChatListener(logger, configuration, jda), this);
        logger.log(Level.INFO, "Plugin has been enabled.");
    }

    @Override
    public void onDisable() {
        logger.log(Level.INFO, "Disabling plugin...");
        if (jda != null) jda.shutdown(false);
        logger.log(Level.INFO, "Plugin has been disabled.");
    }
}