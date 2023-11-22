package main;

import java.util.Random;

import entity.Entity;
import entity.Magicien;
import monster.Alien;
import object.Boots;

import object.Coin;
import object.Diamond;

import object.Potion;

public class AssetSetter {

	GamePanel gp;

	public AssetSetter(GamePanel gp) {
		this.gp = gp;

	}

	public void setNPC() {
		Random random = new Random();
		gp.tileM.noiseMap[23][21] = 5;
		int numberOfNPCs = random.nextInt(1) + 2; // Random number between 2 and 5

		for (int i = 0; i < numberOfNPCs; i++) {
			gp.npc[i] = new Magicien(gp);

			// Find a random position close to the snow position (23, 21)
			int snowX = 23;
			int snowY = 21;

			int xOffset, yOffset, newX, newY;

			do {
				xOffset = random.nextInt(5) - 2; // Random number between -2 and 2
				yOffset = random.nextInt(5) - 2; // Random number between -2 and 2

				newX = Math.max(0, Math.min(49, snowX + xOffset)); // Ensure newX is within bounds
				newY = Math.max(0, Math.min(49, snowY + yOffset)); // Ensure newY is within bounds

			} while (gp.tileM.noiseMap[newX][newY] == 1 || gp.tileM.noiseMap[newX][newY] == 2
					|| gp.tileM.noiseMap[newX][newY] == 3 | gp.tileM.noiseMap[newX][newY] == 4
					|| gp.tileM.noiseMap[newX][newY] == 7 || isPositionOccupied(newX, newY));

			gp.npc[i].worldX = gp.tileSize * newX;
			gp.npc[i].worldY = gp.tileSize * newY;
		}
	}

	public void setMonster() {
		Random random = new Random();
		int gameLevel = gp.player.level;
		System.out.println("Game Level: " + gameLevel);
		gp.tileM.noiseMap[23][21] = 5;

		int baseNumberOfMonsters = 5; // Adjust the base number of monsters as needed

		int numberOfMonsters = baseNumberOfMonsters + (5 * gameLevel); // Increase by 5 at each game level

		for (int i = 0; i < numberOfMonsters; i++) {
			gp.monster[i] = new Alien(gp);

			// Find a random position close to the snow position (23, 21)
			int snowX = 23;
			int snowY = 21;

			int xOffset, yOffset, newX, newY;

			do {
				/*
				 * xOffset = random.nextInt(5) - 2; // Random number between -2 and 2 yOffset =
				 * random.nextInt(5) - 2; // Random number between -2 and 2
				 * 
				 * newX = Math.max(0, Math.min(49, snowX + xOffset)); // Ensure newX is within
				 * bounds newY = Math.max(0, Math.min(49, snowY + yOffset)); // Ensure newY is
				 * within bounds
				 */
				newX = random.nextInt(50);
				newY = random.nextInt(50);
			} while (gp.tileM.noiseMap[newX][newY] == 1 || gp.tileM.noiseMap[newX][newY] == 2
					|| gp.tileM.noiseMap[newX][newY] == 3 | gp.tileM.noiseMap[newX][newY] == 4
					|| gp.tileM.noiseMap[newX][newY] == 7 || isPositionOccupied(newX, newY));

			gp.monster[i].worldX = gp.tileSize * newX;
			gp.monster[i].worldY = gp.tileSize * newY;
		}
	}

	// Check if the position is occupied by NPC or Monster
	public boolean isPositionOccupied(int x, int y) {
		for (Entity npc : gp.npc) {
			if (npc != null && npc.worldX / gp.tileSize == x && npc.worldY / gp.tileSize == y) {
				return true; // Position is occupied by NPC
			}
		}

		for (Entity monster : gp.monster) {
			if (monster != null && monster.worldX / gp.tileSize == x && monster.worldY / gp.tileSize == y) {
				return true; // Position is occupied by Monster
			}
		}

		if (gp.player.worldX / gp.tileSize == x && gp.player.worldY / gp.tileSize == y) {
			return true; // Position is occupied by Monster
		}

		for (Entity obj : gp.obj) {
			if (obj != null && obj.worldX / gp.tileSize == x && obj.worldY / gp.tileSize == y) {
				return true; // Position is occupied by object
			}
		}

		return false; // Position is not occupied
	}

	public void setObject() {

		Random random = new Random();

		int nbdiamond = 5 * gp.player.level;

		int nbpotion = 3 * gp.player.level;

		int nbboots = 2;

		int x, y;

		for (int i = 0; i < nbdiamond; i++) {

			do {

				x = random.nextInt(50);

				y = random.nextInt(50);

			} while (gp.tileM.noiseMap[x][y] == 1 || gp.tileM.noiseMap[x][y] == 2

					|| gp.tileM.noiseMap[x][y] == 3 | gp.tileM.noiseMap[x][y] == 4

					|| gp.tileM.noiseMap[x][y] == 7 || isPositionOccupied(x, y));

			gp.obj[i] = new Diamond(gp);

			gp.obj[i].worldX = x * gp.tileSize;

			gp.obj[i].worldY = y * gp.tileSize;

		}

		for (int i = 0; i < nbpotion; i++) {

			do {

				x = random.nextInt(50);
				y = random.nextInt(50);

			} while (gp.tileM.noiseMap[x][y] == 1 || gp.tileM.noiseMap[x][y] == 2

					|| gp.tileM.noiseMap[x][y] == 3 | gp.tileM.noiseMap[x][y] == 4

					|| gp.tileM.noiseMap[x][y] == 7 || isPositionOccupied(x, y));

			gp.obj[i] = new Potion(gp);

			gp.obj[i].worldX = x * gp.tileSize;

			gp.obj[i].worldY = y * gp.tileSize;

		}

		for (int i = 0; i < nbboots; i++) {

			do {

				x = random.nextInt(50);

				y = random.nextInt(50);

			} while (gp.tileM.noiseMap[x][y] == 1 || gp.tileM.noiseMap[x][y] == 2

					|| gp.tileM.noiseMap[x][y] == 3 | gp.tileM.noiseMap[x][y] == 4

					|| gp.tileM.noiseMap[x][y] == 7 || isPositionOccupied(x, y));

			gp.obj[i] = new Boots(gp);

			gp.obj[i].worldX = x * gp.tileSize;

			gp.obj[i].worldY = y * gp.tileSize;

		}

	}

}
