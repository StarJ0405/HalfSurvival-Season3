package com.StarJ.HS.Skills;

import org.bukkit.entity.Player;

import com.StarJ.HS.Skills.Farming.FarmingMinigame;
import com.StarJ.HS.Skills.Fishing.FishingMinigame;
import com.StarJ.HS.Skills.Hunting.HuntingMinigame;
import com.StarJ.HS.Skills.Mining.MiningMinigame;

public class Minigame {
	public final MiningMinigame miningMinigame;
	public final FarmingMinigame farmingMinigame;
	public final HuntingMinigame huntingMinigame;
	public final FishingMinigame fishingMinigame;

	public Minigame(Player player) {
		this.miningMinigame = new MiningMinigame(player);
		this.farmingMinigame = new FarmingMinigame(player);
		this.huntingMinigame = new HuntingMinigame(player);
		this.fishingMinigame = new FishingMinigame(player);
	}

	public boolean isRunning() {
		return this.miningMinigame.isRunning() || this.farmingMinigame.isRunning() || this.huntingMinigame.isRunning()
				|| this.fishingMinigame.isRunning();
	}
}
