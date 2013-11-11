/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk44.jfxgallery.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Properties;

/**
 *
 * @author sk
 */
public class Config {

	private static final String CONFIG_PATH = "config.properties";
	private static final String CONFIG_ENCODING = "UTF-8";
	private static final String KEY_STARTUP_PATH = "startup.path";
	private static final String KEY_VIEWER_MODE = "viewer.mode";
	// TODO
	private static final String KEY_WINDOW_WIDTH = "windiw.width";
	private static final String KEY_WINDOW_HEIGHT = "window.height";
	private static final String KEY_WINDOW_X = "window.x";
	private static final String KEY_WINDOW_Y = "window.y";

	public static Config load() {
		Properties props = readFromConfigFile();
		if (props == null) {
			return defaultConfig();
		}
		File startupPath = null;
		String path = props.getProperty(KEY_STARTUP_PATH, null);
		if (path != null) {
			File file = new File(path);
			if (file.exists()) {
				startupPath = file;
			}
		}
		ViewerMode mode = null;
		String modeId = props.getProperty(KEY_VIEWER_MODE, null);
		if (modeId != null && modeId.length() > 0) {
			mode = ViewerMode.modeOfId(modeId);
		}
		return new Config(
			startupPath == null ? defaultStartupPath() : startupPath,
			mode == null ? ViewerMode.defaultMode() : mode);
	}

	public static void update(File startupPath, ViewerMode viewerMode) {
		Properties props = new Properties();
		props.put(
			KEY_STARTUP_PATH,
			startupPath == null ? "" : startupPath.getAbsolutePath());
		props.put(KEY_VIEWER_MODE, viewerMode.getId());

		try (OutputStream os = new FileOutputStream(new File(CONFIG_PATH))) {
			props.store(os, CONFIG_ENCODING);
		} catch (IOException ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}

	static Properties readFromConfigFile() {

		File configFile = new File(CONFIG_PATH);
		if (configFile.exists() == false) {
			return null;
		}
		Properties p = new Properties();
		try (Reader reader = new InputStreamReader(new FileInputStream(configFile), CONFIG_ENCODING)) {
			p.load(reader);
			return p;
		} catch (IOException ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}

	static Config defaultConfig() {
		return new Config(defaultStartupPath(), ViewerMode.defaultMode());
	}

	private static File defaultStartupPath() {
		String home = System.getProperty("user.home");
		return new File(home);
	}

	private Config(File startupPath, ViewerMode viewerMode) {
		this.startupPath = startupPath;
		this.viewerMode = viewerMode;
	}

	private final File startupPath;
	private final ViewerMode viewerMode;

	public File getStartupPath() {
		return startupPath;
	}

	public ViewerMode getViewerMode() {
		return viewerMode;
	}

	@Override
	public String toString() {
		return "Config{" + "startupPath=" + startupPath + ", viewerMode=" + viewerMode + '}';
	}

}
