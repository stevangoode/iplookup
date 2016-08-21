package com.stevangoode.iplookup;

import java.net.*;
import java.io.*;

public class FetchInformation {
    public static String doLookup(String url) throws Exception {

        URL lookup = new URL(url);
        BufferedReader in = new BufferedReader(
            new InputStreamReader(lookup.openStream())
        );

        String output = "";
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            output = output.concat(inputLine);
        }
        in.close();

        return output;
    }
}
