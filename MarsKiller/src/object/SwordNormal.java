package object;

import entity.Entity;
import main.GamePanel;

public class SwordNormal extends Entity{

	public SwordNormal(GamePanel gp) {
		super(gp);
		name = "Normal Sword";
		down1 = setup("/objects/sword_normal", gp.tileSize,gp.tileSize);
		attackValue = 4;
	}

}
