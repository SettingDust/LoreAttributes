package com.settingdust.loreattr.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

/**
 * Author: SettingDust
 * Date: 16-8-7
 * By IntelliJ IDEA
 */
public class Config {
    private String fileName;
    private final JavaPlugin plugin;

    private File configFile;
    private YamlConfig fileConfiguration = new YamlConfig();

    /**
     * Init config
     *
     * @param fileName
     * @param plugin
     */
    public Config(String fileName, JavaPlugin plugin) {
        this.plugin = plugin;
        this.load(fileName, true);
    }

    /**
     * @param fileName
     * @param plugin
     * @param def
     */
    public Config(String fileName, JavaPlugin plugin, boolean def) {
        this.plugin = plugin;
        this.load(fileName, def);
    }

    /**
     * @param plugin
     */
    public Config(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * @param fileName
     * @param def
     */
    public void load(String fileName, boolean def) {
        this.fileName = fileName;
        this.configFile = new File(plugin.getDataFolder(), fileName);
        this.load(configFile, def);
    }

    /**
     * @param configFile
     * @param def
     */
    public void load(File configFile, boolean def) {
        try {
            if (!configFile.exists()) {
                configFile.getParentFile().mkdirs();
                configFile.createNewFile();
                if (def) {
                    InputStream inputStream = plugin.getResource(configFile.getName());
                    load(inputStream);
                } else {
                    this.load(new FileInputStream(configFile));
                }
                this.saveConfig();
            } else
                this.load(new FileInputStream(configFile));
        } catch (IOException e) {
            e.printStackTrace();
        } /*catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * Load file with InputStream
     *
     * @param inputStream
     */
    @Deprecated
    public void load(InputStream inputStream) {
        this.fileConfiguration = YamlConfig.loadConfiguration(inputStream);
    }

    /**
     * Return file name
     *
     * @return
     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * Return current config
     *
     * @return
     */
    public YamlConfig getConfig() {
        return fileConfiguration;
    }

    /**
     * Reload config
     */
    public void reloadConfig() {
        this.load(configFile, false);
    }

    public void saveConfig() {
        try {
            this.fileConfiguration.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String transfer2GBK(String s) {
        String utf8 = new String("");
        try {
            byte[] from = s.getBytes("UTF-8");
            byte[] temp = new String(from, "UTF-8").getBytes("gbk");
            utf8 = new String(temp, "gbk");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return utf8;
    }

    private String transferFile2GBK(InputStream inputStream)
            throws IOException {
        String utf8 = new String("");
        try {
            InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
            StringBuilder builder = new StringBuilder();
            BufferedReader input = new BufferedReader(reader);
            String builders = "";
            try {
                String line;
                while ((line = input.readLine()) != null) {
                    builders = builders + transfer2GBK(line) + '\n';
                    builder.append(transfer2GBK(line));
                    builder.append('\n');
                }
                utf8 = builders;
            } finally {
                input.close();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return utf8;
    }
}
