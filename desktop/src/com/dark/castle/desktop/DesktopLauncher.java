package com.dark.castle.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dark.castle.DarkCastle;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280 / 2;
		config.height = 720 / 2;
		new LwjglApplication(new DarkCastle(), config);

	}
}
