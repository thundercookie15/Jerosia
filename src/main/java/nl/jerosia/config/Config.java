package nl.jerosia.config;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

public class Config {
    private final Plugin plugin;
    private final File configFile;
    private FileConfiguration fileConfiguration;

    public Config(boolean isMain, String folderName, String fileName, boolean hasDefault, Plugin plugin) {
        String folderPath;

        if (plugin == null) throw new IllegalArgumentException("Plugin cannot be null!");

        this.plugin = plugin;
        if (fileName == null || fileName.isEmpty()) throw new IllegalArgumentException("Name cannot be null");
        if (!plugin.getDataFolder().exists())
            this.plugin.getLogger().log(Level.INFO, "Making Plugin Data Folder (1): " + plugin.getDataFolder().mkdir());

        if (isMain || folderName.isEmpty()) {
            folderPath = plugin.getDataFolder().getPath();
        } else {
            folderPath = plugin.getDataFolder().getPath() + File.separator + folderName;
        }

        File dataFolder = new File(folderPath);
        this.configFile = new File(dataFolder, fileName);
        if (!dataFolder.exists()) this.plugin.getLogger().log(Level.INFO, "Making Config (2): " + dataFolder.mkdir());
        saveDefaultYaml(fileName, hasDefault);
    }

    public void reloadYaml() {
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);

        final InputStream defConfigStream = plugin.getResource(configFile.getPath());
        if (defConfigStream == null) return;
        final YamlConfiguration defConfig;
        final byte[] contents;
        defConfig = new YamlConfiguration();
        try {
            contents = ByteStreams.toByteArray(defConfigStream);
        } catch (IOException exception) {
            this.plugin.getLogger().log(Level.SEVERE, "Unexpected failure reading config.yml", exception);
            return;
        }

        final String text = new String(contents, Charset.defaultCharset());
        if (!text.equals(new String(contents, Charsets.UTF_8)))
            this.plugin.getLogger().log(Level.WARNING, "Default system encoding may have misread config.yml from plugin jar.");

        try {
            defConfig.loadFromString(text);
        } catch (InvalidConfigurationException exception) {
            this.plugin.getLogger().log(Level.SEVERE, "Cannot load configuration from jar", exception);
        }
        fileConfiguration.setDefaults(defConfig);
    }

    public FileConfiguration getYaml() {
        if (fileConfiguration == null) this.reloadYaml();
        return fileConfiguration;
    }

    private void saveYaml() {
        if (fileConfiguration != null && configFile != null) {
            try {
                getYaml().save(configFile);
            } catch (IOException exception) {
                this.plugin.getLogger().log(Level.SEVERE, "Could not save config to %s".formatted(configFile), exception);
            }
        }
    }

    public void saveDefaultYaml(String fileName, boolean hasDefault) {
        if (!configFile.exists()) {
            Path file = configFile.toPath();
            try {
                Files.createFile(file);
                if (hasDefault) copyDefaults(fileName);
            } catch (FileAlreadyExistsException exception) {
                this.plugin.getLogger().log(Level.INFO, "Tried to create a new Config, but it already existed! ");
            } catch (IOException exception) {
                this.plugin.getLogger().log(Level.SEVERE, "An error occurred while attempting to create file: %s".formatted(this.configFile.getName()), exception);
            }
        }
    }

    private void copyDefaults(String fileName) {
        try {
            Reader defConfigStream = new InputStreamReader(Config.class.getResourceAsStream(File.separator + fileName), StandardCharsets.UTF_8);
            PrintWriter writer = new PrintWriter(configFile, StandardCharsets.UTF_8);
            writer.println(this.read(defConfigStream));
            writer.close();
        } catch (IOException exception) {
            this.plugin.getLogger().log(Level.SEVERE, "An error occurred copying the default yml file!", exception);
        }
    }

    private String read(Reader r) throws IOException {
        char[] arr = new char[8192];
        StringBuilder buffer = new StringBuilder();

        int numCharsRead;
        while ((numCharsRead = r.read(arr, 0, arr.length)) != -1) buffer.append(arr, 0, numCharsRead);

        r.close();
        return buffer.toString();
    }

    public void modifyYaml() {
        saveYaml();
        reloadYaml();
    }
}
