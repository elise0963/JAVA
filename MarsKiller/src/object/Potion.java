package object;

import entity.Entity;

import main.GamePanel;

public class Potion extends Entity{
	
	public Potion(GamePanel gp) {
		
		super(gp);
	
		name = "Potion_red";
		down1 = setup("/objects/potion_red", gp.tileSize,gp.tileSize);
	}
}


