package object;

import entity.Entity;
import main.GamePanel;

public class ShieldWood extends Entity {

	public ShieldWood(GamePanel gp) {
		super(gp);
		name = "Wood Shield";
		down1 = setup("/objects/shield_wood", gp.tileSize, gp.tileSize);
		defenseValue = 1;
	}

}
