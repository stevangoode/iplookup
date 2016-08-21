package com.stevangoode.iplookup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Aliases {
    private Map <String, ArrayList<String>> aliases;

    public Aliases () {
        this.aliases = new HashMap<String, ArrayList<String>>();
    }

    public void addAlias(String ip_address, String alias) {
        ArrayList<String> aliases = new ArrayList<String>();
        if (this.aliases.containsKey(ip_address)) {
            aliases = this.aliases.get(ip_address);
        }
        if (!aliases.contains(alias)) {
            aliases.add(alias);
        }
        this.aliases.put(ip_address, aliases);
    }

    public ArrayList<String> getAliases(String ip_address) {
        return this.aliases.get(ip_address);
    }

    public Map <String, ArrayList<String>> getAliases() {
        return this.aliases;
    }

}
