package Utils;


import java.io.InputStream;
import java.util.Properties;

public class ProjectConfig {

    private static final Properties properties = new Properties();

    static {
        try (InputStream in = ProjectConfig.class.getClassLoader().getResourceAsStream("project.properties")) {
            if (in == null)
                throw new RuntimeException("Impossible de trouver project.properties");
            properties.load(in);
        }
        catch (Exception e) {
            throw new RuntimeException("Erreur chargement du fichier de config : " + e.getMessage());
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static int getInt(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

    public static boolean getBool(String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }
}
