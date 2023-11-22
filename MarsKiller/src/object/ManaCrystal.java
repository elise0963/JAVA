package object;

import entity.Entity;
import main.GamePanel;

public class ManaCrystal extends Entity {
	GamePanel gp;
	public ManaCrystal(GamePanel gp) {
		super(gp);
		this.gp=gp;
		
		name = "Mana Cystal";
		image = setup("/objects/manacrystal_full", gp.tileSize,gp.tileSize);
		image2 = setup("/objects/manacrystal_blank", gp.tileSize,gp.tileSize);
	}

}
