package boot.tale.kit;

import java.io.File;
import java.util.HashMap;

/**
 * tale 常量
 */
public class ConstKit {

    public static final String USER_IN_COOKIE = "S_L_ID";
    public static String ASE_SALT = "0123456789abcdef";
    public static String LOGIN_SESSION_KEY = "login_user";
    public static Boolean INSTALLED = false;
    public static Boolean ENABLED_CDN = true;
    public static String CLASSPATH = (new File(ConstKit.class.getResource("/").getPath())).getPath();


    public static final int MAX_TITLE_COUNT = 200;

}
