package object;
import entity.Entity;
import main.GamePanel;

public class Diamond extends Entity{
	
	public Diamond(GamePanel gp) {
		
		super(gp);
	
		name = "Diamond";
		down1 = setup("/objects/blueheart", gp.tileSize,gp.tileSize);
	}
}
