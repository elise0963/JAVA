package main;

import java.awt.event.KeyEvent;

import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

	public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, attackKeyPressed, shotKeyPressed;
	boolean checkDrawTime = false;
	public int selectedLevel = 0;
	GamePanel gp;

	public KeyHandler(GamePanel gp) {
		this.gp = gp;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

		int code = e.getKeyCode();
		// TITLE STATE
		if (gp.gameState == gp.titleState) {
			titleState(code);
			
			
		}
		// PLAY STATE
		else if (gp.gameState == gp.playState) {
			playState(code);
		}
		// PAUSE STATE
		else if (gp.gameState == gp.pauseState) {
			pauseState(code);
		}
		// DIALOGUE STATE
		else if (gp.gameState == gp.dialogueState) {
			dialogueState(code);
		}
		// GAMEOVER STATE
		else if (gp.gameState == gp.gameOverState) {
			gameOverState(code);
		}
		// CHARACTERSTATE STATE
		else if (gp.gameState == gp.characterState) {
			characterState(code);
		}
	}
	public void titleState(int code) {
		
		if (code == KeyEvent.VK_DOWN) {
			gp.ui.commandNum++;
			if (gp.ui.commandNum > 1) {
				gp.ui.commandNum = 0;
			}
		}
		if (code == KeyEvent.VK_UP) {
			gp.ui.commandNum--;
			if (gp.ui.commandNum < 0) {
				gp.ui.commandNum = 1;
			}
		}
		if (code == KeyEvent.VK_ENTER) {
			if (gp.ui.commandNum == 0) {
				gp.gameState = gp.playState;
			}
			if (gp.ui.commandNum == 1) {
				System.exit(0);
			}
		}
	}
	public void playState(int code) {
		if (code == KeyEvent.VK_DOWN) {
			downPressed = true;
		}
		if (code == KeyEvent.VK_UP) {
			upPressed = true;
		}
		if (code == KeyEvent.VK_LEFT) {
			leftPressed = true;
		}
		if (code == KeyEvent.VK_RIGHT) {
			rightPressed = true;
		}

		if (code == KeyEvent.VK_P) {
			gp.gameState = gp.pauseState;
		}
		if (code == KeyEvent.VK_C) {
			gp.gameState = gp.characterState;
		}
		if (code == KeyEvent.VK_ENTER) {
			enterPressed = true;
		}
		if (code == KeyEvent.VK_SPACE) { // shot fireball
			shotKeyPressed = true;
		}
		if (code == KeyEvent.VK_A) { // attack with sword
			attackKeyPressed = true;
		}

		// DEBUG
		if (code == KeyEvent.VK_T) {
			if (checkDrawTime == false) {
				checkDrawTime = true;
			} else if (checkDrawTime == true) {
				checkDrawTime = false;
			}
		}
	}
	public void pauseState(int code) {
		if (code == KeyEvent.VK_P) {
			gp.gameState = gp.playState;
		}	
	}
	public void dialogueState(int code) {
		if (code == KeyEvent.VK_ENTER) {
			gp.gameState = gp.playState;
		}
	}
	public void characterState(int code) {
		if (code == KeyEvent.VK_C) {
			gp.gameState = gp.playState;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int code = e.getKeyCode();

		if (code == KeyEvent.VK_DOWN) {
			downPressed = false;
		}
		if (code == KeyEvent.VK_UP) {
			upPressed = false;
		}
		if (code == KeyEvent.VK_LEFT) {
			leftPressed = false;
		}
		if (code == KeyEvent.VK_RIGHT) {
			rightPressed = false;
		}
		if (code == KeyEvent.VK_SPACE) { // shot fireball
			shotKeyPressed = false;
		}
		if (code == KeyEvent.VK_A) { // attack with sword
			attackKeyPressed = false;
		}

	}

	public int getSelectedLevel() {

		return selectedLevel;
	}

	public void setSelectedLevel(int level) {

		selectedLevel = level;
	}

	public void gameOverState(int code) {
		
		if (code == KeyEvent.VK_UP) {
			
			gp.ui.commandNum--;
			if (gp.ui.commandNum < 0) {
				gp.ui.commandNum = 1;
			}
		}
		if (code == KeyEvent.VK_DOWN) {
			gp.ui.commandNum++;
			if (gp.ui.commandNum > 1) {
				gp.ui.commandNum = 0;
			}
		}
		if (code == KeyEvent.VK_ENTER) {
			if (gp.ui.commandNum == 0) {
				gp.gameState = gp.playState;
				gp.retry();

			} else if (gp.ui.commandNum == 1) {
				selectedLevel = 1;
				gp.gameState = gp.titleState;
				setSelectedLevel(1);
				gp.restart();

			}
		}
	}

}
