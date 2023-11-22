package tile;


import java.awt.Graphics2D;


import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class TileManager {
	
	
	GamePanel gp;
	public Tile [] tile;
	
	public int noiseMap[][];
	boolean drawPath = true;
	int mapTile[][];
	
	public TileManager(GamePanel gp) {
		this.gp = gp;
		
		tile = new Tile[10];
		
		
		noiseMap = PerlinNoise.generateNoise(gp.maxWorldCol, gp.maxWorldCol, 4, 0.5);
		defaultPos(noiseMap);
		
		getTileImage();
		
	}
	
	public void defaultPos(int noiseMap[][]) {
		if (noiseMap[23][21]!=9) {
			noiseMap[23][21]=9;
		}
	}
	public void getTileImage() {
		setup (5, "grass", false);
		setup (1, "wall", true);
		setup (7, "water", true);
		setup (0, "earth", false);
		setup (4, "tree", true);
		setup (6, "road", false);
		setup (9, "sand", false);
		setup (3, "tree_2", true);
		setup (8, "grass_2", false);
		setup (2, "hut", true);
		
	}
	
	public void setup(int index, String imageName,boolean collision) {
		
		UtilityTool uTool = new UtilityTool();
		
		try {
			
			tile[index] = new Tile();
			tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/"+ imageName+".png"));
			tile[index].image=uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
			tile[index].collision = collision;
		}catch (Exception e) {
			e.printStackTrace();

		}
	}
	
	
	//dessiner la carte 
	public void draw(Graphics2D g2) {
		
		int worldCol = 0;
		int worldRow = 0 ;
		
		while (worldCol< gp.maxWorldCol && worldRow < gp.maxWorldRow) {
			int tileNum = noiseMap[worldCol][worldRow];
			int worldX = worldCol * gp.tileSize;
			int worldY = worldRow * gp.tileSize;
			int screenX = worldX - gp.player.worldX + gp.player.screenX ;
			int screenY = worldY - gp.player.worldY + gp.player.screenY ;
			
			if (worldX +gp.tileSize > gp.player.worldX - gp.player.screenX && 
					worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
					worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
					worldY - gp.tileSize< gp.player.worldY + gp.player.screenY) {
				
				g2.drawImage(tile[tileNum].image, screenX, screenY,null);
			}
			
			worldCol++;
			
			if (worldCol == gp.maxWorldCol) {
				worldCol = 0;
				worldRow++;
				
				
			}
		}
	}	

}
