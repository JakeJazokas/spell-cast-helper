package com.spellcasthelper;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(SpellCastHelperConfig.GROUP_NAME)
public interface SpellCastHelperConfig extends Config
{
	String GROUP_NAME = "spellcasthelper";

	String SPELL_NAME_KEY = "spellName";
	String CASTS_KEY = "casts";

	@ConfigItem(
			position = 1,
			keyName = "infobox",
			name = "Cast InfoBox",
			description = "Display cast information in an InfoBox"
	)
	default boolean showInfobox()
	{
		return true;
	}


}
