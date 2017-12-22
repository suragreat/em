package com.suragreat.base.listener;


import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.StringUtils;

public class LocationsSettingConfigFileApplicationListener
        implements ApplicationListener<ApplicationEnvironmentPreparedEvent>, Ordered
{
    private static Logger logger = LoggerFactory.getLogger(LocationsSettingConfigFileApplicationListener.class);
    private static boolean hasError = false;
    private static String errMsg = "GlobalConfig is not initialized";
    private static final String APP_CONFIG_FILE_NAME = "application.properties";
    private static final String PROPERTY_FILE_EXTENSION = "properties";
    private static final String YAML_FILE_EXTENSION = "yml";
    private static final String DEFAULT_CONFIG_DIR = "/opt/config";

    public int getOrder()
    {
        return -2147483639;
    }

    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event)
    {
        SpringApplication app = event.getSpringApplication();
        for (ApplicationListener<?> listener : app.getListeners()) {
            if ((listener instanceof ConfigFileApplicationListener))
            {
                ConfigFileApplicationListener cfal = (ConfigFileApplicationListener)listener;

                String fileStr = findFiles();
                if (!StringUtils.isEmpty(fileStr)) {
                    cfal.setSearchLocations(fileStr);
                }
            }
        }
    }

    private String findFiles()
    {
        List<FileSystemResource> configFiles = Lists.newArrayList();

        InputStream appConfigInputStream = LocationsSettingConfigFileApplicationListener.class.getResourceAsStream("/application.properties");
        Properties appProperties = new Properties();
        if (appConfigInputStream != null)
        {
            try
            {
                appProperties.load(appConfigInputStream);
            }
            catch (IOException e)
            {
                hasError = true;
                errMsg = e.getMessage();
                logger.error(e.getMessage(), e);
            }
        }
        else
        {
            hasError = true;
            errMsg = "can't find application.properties in classpath";
        }
        String appCode = appProperties.getProperty("APP_CODE");
        String configCode = appProperties.getProperty("CONFIG_CODE");
        if ((!hasError) && (StringUtils.isEmpty(appCode)))
        {
            hasError = true;
            errMsg = "APP_CODE can't be empty";
        }
        if (!hasError)
        {
            String additionalConfigDirPath = System.getProperty("additional.config.dir");
            if (!StringUtils.isEmpty(additionalConfigDirPath))
            {
                File zfdir = new File(additionalConfigDirPath);
                addConfigFileFromDir(configFiles, zfdir);
            }
            if (!StringUtils.isEmpty(appCode))
            {
                File zfdir = new File(DEFAULT_CONFIG_DIR, appCode);
                addConfigFileFromDir(configFiles, zfdir);
            }
            if ((!StringUtils.isEmpty(configCode)) &&
                    (!configCode.equals(appCode)))
            {
                File zfdir = new File(DEFAULT_CONFIG_DIR, configCode);
                addConfigFileFromDir(configFiles, zfdir);
            }
        }
        if (hasError) {
            logger.error("GlobalConfig init failed . " + errMsg);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < configFiles.size(); i++)
        {
            try
            {
                sb.append(((FileSystemResource)configFiles.get(i)).getURI().toString());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            if (i < configFiles.size() - 1) {
                sb.append(",");
            }
        }
        logger.info("Load config files from {}", sb.toString());
        return sb.toString();
    }

    private void addConfigFileFromDir(List<FileSystemResource> configFiles, File configDir)
    {
        if ((configDir.exists()) && (configDir.isDirectory()))
        {
            Collection<File> propertiesFiles = FileUtils.listFiles(configDir, new String[] { "properties", "yml" }, false);
            for (File file : propertiesFiles) {
                configFiles.add(new FileSystemResource(file));
            }
        }
    }
}
