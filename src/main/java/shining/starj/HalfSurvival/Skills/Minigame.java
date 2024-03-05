package shining.starj.HalfSurvival.Skills;

import shining.starj.HalfSurvival.Skills.Farming.FarmingMinigame;
import shining.starj.HalfSurvival.Skills.Fishing.FishingMinigame;
import shining.starj.HalfSurvival.Skills.Hunting.HuntingMinigame;
import shining.starj.HalfSurvival.Skills.Mining.MiningMinigame;
import org.bukkit.entity.Player;

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
