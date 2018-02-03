package boot.tale.kit;


public class StringKit {

    public static boolean isNotBlank(String... str){

        for (String s : str){
            if (null == str || "".equals(s.trim())){
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(String str) {
        return null != str && !"".equals(str.trim());
    }





}
