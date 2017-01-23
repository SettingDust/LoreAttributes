package com.settingdust.loreattr.config;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;

import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConstructor;
import org.bukkit.configuration.file.YamlRepresenter;

import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

/**
 * Author: SettingDust
 * Date: 16-8-7
 * By IntelliJ IDEA
 */
public class YamlConfig extends YamlConfiguration {

    private final DumperOptions yamlOptions = new DumperOptions();
    private final Representer yamlRepresenter = new YamlRepresenter();
    private final Yaml yaml = new Yaml(new YamlConstructor(), this.yamlRepresenter, this.yamlOptions);

    public YamlConfig() {
        super();
    }

    /**
     * 保存config
     * <p/>
     *
     * @param file 文件
     * @throws IOException
     */
    @Override
    public void save(File file) throws IOException {
        this.createParentDirs(file);

        String data = saveToString();

        FileWriter writer = new FileWriter(file);
        try {
            writer.write(data);
        } finally {
            writer.close();
        }
    }

    /**
     * 保存config
     * <p/>
     *
     * @param file 文件
     * @throws IOException
     */
    @Override
    public void save(String file) throws IOException {
        save(new File(file));
    }

    /**
     * 载入
     * <p/>
     *
     * @param file 文件
     * @throws java.io.FileNotFoundException
     * @throws org.bukkit.configuration.InvalidConfigurationException
     */
    @Override
    public void load(File file) throws FileNotFoundException, IOException, InvalidConfigurationException {
        load(new FileInputStream(file));
    }

    /**
     * 载入
     * <p/>
     *
     * @param stream 读入内容
     * @throws IOException
     * @throws InvalidConfigurationException
     */
    @Override
    public void load(InputStream stream) throws IOException, InvalidConfigurationException {
        InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
        StringBuilder builder = new StringBuilder();
        BufferedReader input = new BufferedReader(reader);
        ArrayList<String> builders = new ArrayList<String>();
        try {
            String line;
            while ((line = input.readLine()) != null) {
                builders.add(line);
                builder.append(line);
                builder.append('\n');
            }
        } finally {
            input.close();
        }

        super.loadFromString(builder.toString());
    }

    /**
     * 载入 config
     * <p/>
     *
     * @param file 文件
     * @throws FileNotFoundException
     * @throws IOException
     * @throws InvalidConfigurationException
     */
    @Override
    public void load(String file) throws FileNotFoundException, IOException, InvalidConfigurationException {
        load(new File(file));
    }

    /**
     * 修复1.7.10不能为中文
     * <p/>
     *
     * @param contents 内容
     * @throws org.bukkit.configuration.InvalidConfigurationException
     */
    @Override
    public void loadFromString(String contents) throws InvalidConfigurationException {
        Map input;
        try {
            input = (Map) this.yaml.load(contents);
        } catch (YAMLException e) {
            throw new InvalidConfigurationException(e);
        } catch (ClassCastException e) {
            throw new InvalidConfigurationException("Top level is not a Map.");
        }

        String header = parseHeader(contents);
        if (header.length() > 0) {
            options().header(header);
        }

        if (input != null) {
            convertMapsToSections(input, this);
        }
    }

    /**
     * 修复1.7.10不能为中文
     * <p/>
     *
     * @return String
     */
    @Override
    public String saveToString() {
        this.yamlOptions.setIndent(options().indent());
        this.yamlOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        this.yamlRepresenter.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        String header = buildHeader();
        String dump = this.yaml.dump(getValues(false));

        if (dump.equals("{}\n")) {
            dump = "";
        }

        return header + dump;
    }

    /**
     * 创建父文件夹
     * <p/>
     *
     * @param file 文件
     * @throws IOException
     */
    private void createParentDirs(File file) throws IOException {
        File parent = file.getCanonicalFile().getParentFile();
        if (parent == null) {
            return;
        }

        parent.mkdirs();

        if (!parent.isDirectory()) {
            throw new IOException("Unable to create parent directories of " + file);
        }
    }

    public static YamlConfig loadConfiguration(File file) {
        Validate.notNull(file, "File cannot be null");
        YamlConfig config = new YamlConfig();

        try {
            config.load(file);
        } catch (IOException var4) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, var4);
        } catch (InvalidConfigurationException var5) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, var5);
        }

        return config;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public static YamlConfig loadConfiguration(InputStream stream) {
        Validate.notNull(stream, "Stream cannot be null");
        YamlConfig config = new YamlConfig();

        try {
            config.load(stream);
        } catch (IOException var3) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load configuration from stream", var3);
        } catch (InvalidConfigurationException var4) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load configuration from stream", var4);
        }

        return config;
    }

    public static YamlConfig loadConfiguration(Reader reader) {
        Validate.notNull(reader, "Stream cannot be null");
        YamlConfig config = new YamlConfig();

        try {
            config.load(reader);
        } catch (IOException var3) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load configuration from stream", var3);
        } catch (InvalidConfigurationException var4) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load configuration from stream", var4);
        }

        return config;
    }
}