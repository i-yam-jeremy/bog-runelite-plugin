/*
 * Copyright (c) 2018
 * Jeremy Berchtold <https://github.com/i-yam-jeremy>
 * Job Feikens <job@vv32.nl>
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.bog;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GameState;
import net.runelite.api.GroundObject;
import net.runelite.api.TileObject;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GroundObjectSpawned;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

@PluginDescriptor(
	name = "Temple Trekking Bog",
	description = "Highlights walkable tiles for the bog encounter in the Temple Trekking minigame",
	tags = {"bog", "temple", "trekking"}
)
@Slf4j
public class BogPlugin extends Plugin
{
	private static final int WALKABLE_BOG_TILE_ID = 13838;

	@Getter
	final Set<TileObject> walkableBogTiles = new HashSet<>();

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private BogOverlay bogOverlay;

	@Override
	protected void startUp()
	{
		overlayManager.add(bogOverlay);
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(bogOverlay);
	}

	@Subscribe
	public void onGroundObjectSpawned(final GroundObjectSpawned event)
	{
		final GroundObject groundObject = event.getGroundObject();

		if (groundObject.getId() == WALKABLE_BOG_TILE_ID)
		{
			walkableBogTiles.add(groundObject);
		}
	}

	@Subscribe
	public void onGameStateChanged(final GameStateChanged event)
	{
		if (event.getGameState() == GameState.LOADING)
		{
			walkableBogTiles.clear();
		}
	}
}
