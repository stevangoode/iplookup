package com.stevangoode.iplookup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Aliases {
    private Map<String, ArrayList<String>> aliases;

    // Initialise the alias map
    public Aliases() { this.aliases = new HashMap(); }

    public void addAlias(String key, String alias) {
        // Create a new list by default
        ArrayList<String> aliases = new ArrayList<String>();

        // If we already have an entry matching this key grab it to add to it
        if (this.aliases.containsKey(key)) {
            aliases = this.aliases.get(key);
        }

        if (!aliases.contains(alias)) {
            // If the alias isn't already in the list, add it in
            aliases.add(alias);

            // Save the list back to the map
            this.aliases.put(key, aliases);
        }
    }

    public ArrayList<String> getAliases(String key) { return this.aliases.get(key); }
    public int size(String key) { return this.getAliases(key).size(); }
    public String getAliasString(String key) { return this.getAliasString(key, ", "); }

    public String getAliasString(String key, String separator) {
        if (!this.aliases.containsKey(key)) {
            return "";
        }

        String outputString = "";
        ArrayList<String> aliases = this.getAliases(key);

        for (String s : aliases) {
            if (outputString.length() > 0) {
                outputString = outputString.concat(separator);
            }
            outputString = outputString.concat(s);
        }

        return outputString;
    }
}
