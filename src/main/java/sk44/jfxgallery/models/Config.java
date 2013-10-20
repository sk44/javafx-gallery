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

	public static Config load() {
		Properties props = readFromConfigFile();
		if (props == null) {
			return defaultConfig();
		}
		String path = props.getProperty(KEY_STARTUP_PATH, null);
		if (path == null) {
			return defaultConfig();
		}
		File file = new File(path);
		if (file.exists() == false) {
			return defaultConfig();
		}
		return new Config(file);
	}

	public static void update(File startupPath) {
		Properties props = new Properties();
		props.put(
			KEY_STARTUP_PATH,
			startupPath == null ? "" : startupPath.getAbsolutePath());

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
		String home = System.getProperty("user.home");
		return new Config(new File(home));
	}

	private Config(File startupPath) {
		this.startupPath = startupPath;
	}

	private File startupPath;

	public File getStartupPath() {
		return startupPath;
	}

	public void setStartupPath(File startupPath) {
		this.startupPath = startupPath;
	}

	@Override
	public String toString() {
		return "Config{" + "startupPath=" + startupPath + '}';
	}

}
