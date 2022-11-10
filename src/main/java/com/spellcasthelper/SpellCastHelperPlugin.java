package com.spellcasthelper;

import com.google.inject.Provides;
import javax.inject.Inject;

import joptsimple.internal.Strings;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.api.SpriteID;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;

import java.awt.image.BufferedImage;

@Slf4j
@PluginDescriptor(
	name = "Spell Cast Helper",
	description = "Show additional information about the casts remaining for the current spell being cast",
	tags = {"cast","spell","remaining","overlay","combat"}
)
public class SpellCastHelperPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private InfoBoxManager infoBoxManager;

	@Inject
	private ConfigManager configManager;

	@Inject
	private SpellCastHelperConfig config;

	@Inject
	private ItemManager itemManager;

	@Inject
	private ClientThread clientThread;

	@Getter(AccessLevel.PACKAGE)
	@Setter(AccessLevel.PACKAGE)
	private int casts;

	@Getter(AccessLevel.PACKAGE)
	@Setter(AccessLevel.PACKAGE)
	private String spellName;

	private CastCounter counter;

	@Override
	protected void startUp() throws Exception
	{
		if(config.showInfobox()){
			clientThread.invoke(this::addCounter);
		}
	}

	@Override
	protected void shutDown() throws Exception
	{
		removeCounter();
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		switch (gameStateChanged.getGameState())
		{
			case HOPPING:
			case LOGGING_IN:
				spellName = "";
				casts = 0;
				break;
			case LOGGED_IN:
				client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says ", null);
//				migrateConfig();
//				if (getIntProfileConfig(SlayerConfig.AMOUNT_KEY) != -1
//						&& !getStringProfileConfig(SlayerConfig.TASK_NAME_KEY).isEmpty()
//						&& loginFlag)
//				{
//					setTask(getStringProfileConfig(SlayerConfig.TASK_NAME_KEY),
//							getIntProfileConfig(SlayerConfig.AMOUNT_KEY),
//							getIntProfileConfig(SlayerConfig.INIT_AMOUNT_KEY),
//							getStringProfileConfig(SlayerConfig.TASK_LOC_KEY), false);
//					loginFlag = false;
//				}
				break;
		}
	}

	@Subscribe
	private void onConfigChanged(ConfigChanged event)
	{
		if (!event.getGroup().equals(SpellCastHelperConfig.GROUP_NAME))
		{
			return;
		}

		if (event.getKey().equals("infobox"))
		{
			if (config.showInfobox())
			{
				clientThread.invoke(this::addCounter);
			}
			else
			{
				removeCounter();
			}
		}
//		else
//		{
//			npcOverlayService.rebuild();
//		}
	}

	@Provides
	SpellCastHelperConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(SpellCastHelperConfig.class);
	}

	private void addCounter()
	{
		if (!config.showInfobox() || counter != null || Strings.isNullOrEmpty(spellName))
		{
			return;
		}

		Spell spell = Spell.getSpell(spellName);
		int spellSpriteId = SpriteID.SPELL_FIRE_SURGE_DISABLED;
		if (spell != null)
		{
			spellSpriteId = spell.getSpellSpriteId();
		}

		BufferedImage spellImage = itemManager.getImage(spellSpriteId);

		counter = new CastCounter(spellImage, this, casts);

		infoBoxManager.addInfoBox(counter);
	}

	private void removeCounter()
	{
		if (counter == null)
		{
			return;
		}

		infoBoxManager.removeInfoBox(counter);
		counter = null;
	}

}
