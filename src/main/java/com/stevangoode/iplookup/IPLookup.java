package com.stevangoode.iplookup;

import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
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
    private Aliases aliases;

    public IPLookup() {
        this.aliases = new Aliases();
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
                TextColors.GOLD,
                player.getName(),
                TextColors.WHITE,
                " is connecting from ",
                TextColors.AQUA,
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
                TextColors.GOLD,
                player.getName()
            );
        }

        // TODO : Change this when config files are in
        MessageChannel.permission("Staff").send(output);

        // Alias time!
        this.aliases.addAlias(ip, player.getName());
        ArrayList<String> aliasList = this.aliases.getAliases(ip);
        if (aliasList.size() > 1) {
            String outputAliases = "";

            for (String s : aliasList) {
                if (outputAliases.length() > 0) {
                    outputAliases = outputAliases.concat(", ");
                }
                outputAliases = outputAliases.concat(s);
            }

            MessageChannel.permission("Staff").send(
                    Text.of(TextColors.BLUE, "Aliases: ", TextColors.RED, outputAliases)
            );
        }
    }
}
