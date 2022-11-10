package com.spellcasthelper;

import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.Counter;

import java.awt.image.BufferedImage;

public class CastCounter extends Counter {

    public CastCounter(BufferedImage image, Plugin plugin, int count) {
        super(image, plugin, count);
    }
}
