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

import com.knockturnmc.api.util.NamedProperties;
import com.knockturnmc.api.util.Property;

/**
 * Created by Comportment at 11:55 on 14/05/17
 * https://github.com/Comportment | comportment@diax.me
 *
 * @author Comportment
 */
public class BotConfiguration extends NamedProperties {

    @Property("botToken")
    private String botToken;

    @Property("bridgePrefix")
    private String bridgePrefix;

    @Property("bridgeServer")
    private String bridgeServer;

    @Property("bridgeChannel")
    private String bridgeChannel;

    public String getBotToken() {
        return botToken;
    }

    public String getBridgePrefix() {
        return bridgePrefix;
    }

    public String getBridgeServer() {
        return bridgeServer;
    }

    public String getBridgeChannel() {
        return bridgeChannel;
    }
}