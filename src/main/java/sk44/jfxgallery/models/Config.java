package sk44.jfxgallery.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author sk
 */
public class Config {

    @FunctionalInterface
    public interface UpdateHandler {

        void onUpdate();
    }

    private static Config INSTANCE;
    private static final String CONFIG_PATH = "config.properties";
    private static final String CONFIG_ENCODING = "UTF-8";
    private static final String KEY_STARTUP_PATH = "startup.path";
    private static final String KEY_VIEWER_MODE = "viewer.mode";
    private static final String KEY_BACKGROUND_IMAGE = "background.image";
    private static final String KEY_WINDOW_WIDTH = "window.width";
    private static final String KEY_WINDOW_HEIGHT = "window.height";
    private static final String KEY_WINDOW_X = "window.x";
    private static final String KEY_WINDOW_Y = "window.y";
    private static final String KEY_WINDOW_FULLSCREEN = "window.fullscreen";
    private static final double DEFAULT_WINDOW_WIDTH = 800.0;
    private static final double DEFAULT_WINDOW_HEIGHT = 600.0;
    private static final double DEFAULT_WINDOW_X = 10.0;
    private static final double DEFAULT_WINDOW_Y = 30.0;

    public static Config load() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        Properties props = createPropertiesFromConfigFile();
        if (props == null) {
            return defaultConfig();
        }
        Config config = defaultConfig();
        String path = props.getProperty(KEY_STARTUP_PATH, null);
        if (path != null) {
            File file = new File(path);
            if (file.exists()) {
                config.startupPath = file;
            }
        }

        String modeId = props.getProperty(KEY_VIEWER_MODE, null);
        if (modeId != null && modeId.length() > 0) {
            config.viewerMode = ViewerMode.modeOfId(modeId);
        }
        String backgroundImagePath = props.getProperty(KEY_BACKGROUND_IMAGE, null);
        if (backgroundImagePath != null && backgroundImagePath.length() > 0) {
            File file = new File(backgroundImagePath);
            if (file.exists()) {
                config.backgroundImage = file;
            }
        }
        config.windowHeight = getDoubleProperty(props, KEY_WINDOW_HEIGHT, DEFAULT_WINDOW_HEIGHT);
        config.windowWidth = getDoubleProperty(props, KEY_WINDOW_WIDTH, DEFAULT_WINDOW_WIDTH);
        config.windowX = getDoubleProperty(props, KEY_WINDOW_X, DEFAULT_WINDOW_X);
        config.windowY = getDoubleProperty(props, KEY_WINDOW_Y, DEFAULT_WINDOW_Y);
        config.fullScreen = getBooleanProperty(props, KEY_WINDOW_FULLSCREEN, false);

        INSTANCE = config;
        return INSTANCE;
    }

    private static double getDoubleProperty(Properties props, String key, double defaultValue) {
        String value = props.getProperty(key, null);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    private static boolean getBooleanProperty(Properties props, String key, boolean defaultValue) {
        String value = props.getProperty(key, null);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Boolean.parseBoolean(value);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    static Properties createPropertiesFromConfigFile() {

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
        Config result = new Config();
        result.startupPath = defaultStartupPath();
        result.viewerMode = ViewerMode.defaultMode();
        return result;
    }

    private static File defaultStartupPath() {
        String home = System.getProperty("user.home");
        return new File(home);
    }

    private Config() {
        updateHandlers = new ArrayList<>();
    }

    private final List<UpdateHandler> updateHandlers;
    private File startupPath;
    private ViewerMode viewerMode;
    private File backgroundImage;
    private double windowWidth = 0.0;
    private double windowHeight = 0.0;
    private double windowX = 0.0;
    private double windowY = 0.0;
    private boolean fullScreen = false;

    private void save() {
        Properties props = new Properties();
        props.put(
            KEY_STARTUP_PATH,
            startupPath == null
                ? ""
                : startupPath.getAbsolutePath());
        props.put(KEY_VIEWER_MODE,
            viewerMode == null
                ? ViewerMode.defaultMode().getId()
                : viewerMode.getId());
        props.put(KEY_BACKGROUND_IMAGE,
            backgroundImage == null || backgroundImage.length() == 0
            ? ""
            : backgroundImage.getAbsolutePath());
        props.put(KEY_WINDOW_FULLSCREEN, String.valueOf(isFullScreen()));
        props.put(KEY_WINDOW_HEIGHT, String.valueOf(getWindowHeight()));
        props.put(KEY_WINDOW_WIDTH, String.valueOf(getWindowWidth()));
        props.put(KEY_WINDOW_X, String.valueOf(getWindowX()));
        props.put(KEY_WINDOW_Y, String.valueOf(getWindowY()));

        try (OutputStream os = new FileOutputStream(new File(CONFIG_PATH))) {
            props.store(os, CONFIG_ENCODING);
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
        updateHandlers.stream().forEach((handler) -> {
            handler.onUpdate();
        });

    }

    public void updateWindowSettings(double windowHeight, double windowWidth, double windowX, double windowY, boolean fullScreen) {
        this.windowHeight = windowHeight;
        this.windowWidth = windowWidth;
        this.windowX = windowX;
        this.windowY = windowY;
        this.fullScreen = fullScreen;
        save();
    }

    public void updateUserSettings(File startupPath, ViewerMode viewerMode, File backgroundImage) {
        this.startupPath = startupPath;
        this.viewerMode = viewerMode;
        this.backgroundImage = backgroundImage;
        save();
    }

    public void updateBackgroundImage(Path backgroundImagePath) {
        this.backgroundImage = backgroundImagePath.toFile();
        save();
    }

    public File getStartupPath() {
        return startupPath;
    }

    public Path getBackgroundImagePath() {
        if (backgroundImage == null || backgroundImage.exists() == false) {
            return null;
        }
        return backgroundImage.toPath().toAbsolutePath();
    }

    public ViewerMode getViewerMode() {
        return viewerMode;
    }

    public double getWindowWidth() {
        return windowWidth;
    }

    public double getWindowHeight() {
        return windowHeight;
    }

    public boolean isFullScreen() {
        return fullScreen;
    }

    public double getWindowX() {
        return windowX;
    }

    public double getWindowY() {
        return windowY;
    }

    public void registerUpdateHandler(UpdateHandler handler) {
        updateHandlers.add(handler);
    }

}
