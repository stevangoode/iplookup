package com.stevangoode.iplookup;

import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

public class Config {
    private TextColor playerColour;
    private TextColor locationColour;
    private TextColor aliasColour;
    private TextColor aliasTitleColour;

    public Config() {
        this.setPlayerColour(TextColors.GOLD);
        this.setLocationColour(TextColors.AQUA);
        this.setAliasColour(TextColors.RED);
        this.setAliasTitleColour(TextColors.BLUE);
    }

    public TextColor getPlayerColour() {
        return this.playerColour;
    }

    public TextColor getLocationColour() {
        return this.locationColour;
    }

    public TextColor getAliasColour() {
        return this.aliasColour;
    }

    public TextColor getAliasTitleColour() {
        return this.aliasTitleColour;
    }

    public Config setPlayerColour(TextColor newColour) {
        this.playerColour = newColour;
        return this;
    }

    public Config setLocationColour(TextColor newColour) {
        this.locationColour = newColour;
        return this;
    }

    public Config setAliasColour(TextColor newColour) {
        this.aliasColour = newColour;
        return this;
    }

    public Config setAliasTitleColour(TextColor newColour) {
        this.aliasTitleColour = newColour;
        return this;
    }
}
