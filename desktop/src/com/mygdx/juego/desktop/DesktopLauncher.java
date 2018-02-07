package com.mygdx.juego.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.juego.Juego;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL", "true"); 
		config.title = "Juego VI";
		config.width = 1400;
		config.height = 1024; 
		config.allowSoftwareMode = true;
		config.foregroundFPS = 20;
		config.samples = 0;
		
		new LwjglApplication(new Juego(), config);
	}
}
