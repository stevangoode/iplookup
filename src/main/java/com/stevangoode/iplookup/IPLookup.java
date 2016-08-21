package com.stevangoode.iplookup;

import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.Text;

import java.util.Optional;

import org.spongepowered.api.text.format.TextColors;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;

@Plugin(
        id = "com.stevangoode.iplookup",
        name = "IP Lookup",
        version = "1.0-dev",
        description = "Looks up a client IP when joining the server"
)
public class IPLookup {
    private Aliases IPAliases;
    private Aliases UUIDAliases;
    private Config config;

    public IPLookup() {
        this.config = new Config();
        this.IPAliases = new Aliases();
        this.UUIDAliases = new Aliases();
    }

    public void sendMessage(Text message, Player player) {
        MessageChannel.permission("Staff").send(message);
        player.getMessageChannel().send(message);
    }

    @Listener
    public void onClientConnection(ClientConnectionEvent.Join event) {
        Cause cause = event.getCause();
        Optional<Player> playerOptional = cause.first(Player.class);
        if (!playerOptional.isPresent()) {
            return;
        }
        Player player = playerOptional.get();
        String ip = player.getConnection().getAddress().getAddress().getHostAddress();

        String lookupAddress = "http://ip-api.com/xml/";
        if (!ip.equals("127.0.0.1")) {
            lookupAddress = lookupAddress.concat(ip);
        }

        Text output;

        try {

            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            StringBuilder xmlStringBuilder = new StringBuilder();

            xmlStringBuilder.append(FetchInformation.doLookup(lookupAddress));

            ByteArrayInputStream input =  new ByteArrayInputStream(
                    xmlStringBuilder.toString().getBytes("UTF-8"));
            Document doc = builder.parse(input);

            Element root = doc.getDocumentElement();
            Element city = (Element)root.getElementsByTagName("city").item(0);
            Element regionName = (Element)root.getElementsByTagName("regionName").item(0);
            Element country = (Element)root.getElementsByTagName("country").item(0);

            // TODO : Change colours when config files are in
            output = Text.of(
                this.config.getPlayerColour(),
                player.getName(),
                TextColors.WHITE,
                " is connecting from ",
                this.config.getLocationColour(),
                city.getTextContent().trim(),
                ", ",
                regionName.getTextContent().trim(),
                ", ",
                country.getTextContent().trim()
            );
        } catch (Exception e) {
            System.out.println(e.getMessage());

            output = Text.of(
                "Error getting location of ",
                this.config.getPlayerColour(),
                player.getName()
            );
        }

        // TODO : Change this when config files are in
        this.sendMessage(output, player);

        // Aliases Time!
        this.checkAliases(
            this.IPAliases,
            ip,
            player,
            "Also connected from this IP: "
        );

        this.checkAliases(
            this.UUIDAliases,
            player.getUniqueId().toString(),
            player,
            "Previously known as: "
        );
    }

    public void checkAliases(Aliases alias, String key, Player player, String title) {
        alias.addAlias(key, player.getName());

        if (alias.size(key) > 1) {
            this.sendAliasMessage(
                alias.getAliasString(key),
                player,
                title
            );
        }
    }

    public void sendAliasMessage(String list, Player player, String title) {
        this.sendMessage(
            Text.of(
                this.config.getAliasTitleColour(),
                title,
                this.config.getAliasColour(),
                list
            ),
            player
        );
    }
}
