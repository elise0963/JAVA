
package object;

import entity.Entity;
import main.GamePanel;

public class Coin extends Entity{
	
	public Coin(GamePanel gp) {
		
		super(gp);
	
		name = "Coin";
		down1 = setup("/objects/coin", gp.tileSize,gp.tileSize);
		
	}
}

