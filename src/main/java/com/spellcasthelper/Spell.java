package com.spellcasthelper;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import net.runelite.api.SpriteID;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
enum Spell {

    WIND_STRIKE("Wind Strike", SpriteID.SPELL_WIND_STRIKE),
    WATER_STRIKE("Water Strike", SpriteID.SPELL_WATER_STRIKE),
    EARTH_STRIKE("Earth Strike", SpriteID.SPELL_EARTH_STRIKE),
    FIRE_STRIKE("Fire Strike", SpriteID.SPELL_FIRE_STRIKE),
    WIND_BOLT("Wind Bolt", SpriteID.SPELL_WIND_BOLT),
    WATER_BOLT("Water Bolt", SpriteID.SPELL_WATER_BOLT),
    EARTH_BOLT("Earth Bolt", SpriteID.SPELL_EARTH_BOLT),
    FIRE_BOLT("Fire Bolt", SpriteID.SPELL_FIRE_BOLT),
    ;

    private static final Map<String, Spell> spells;
    private final String name;
    private final int spellSpriteId;

    static {
        ImmutableMap.Builder<String, Spell> builder = new ImmutableMap.Builder<>();
        for(Spell spell : values()){
            builder.put(spell.getName().toLowerCase(), spell);
        }
        spells = builder.build();
    }

    Spell(String name, int spellSpriteId) {
        Preconditions.checkArgument(spellSpriteId >= 0);
        this.name = name;
        this.spellSpriteId = spellSpriteId;

    }

    static Spell getSpell(String spellName){
        return spells.get(spellName.toLowerCase());
    }
}
