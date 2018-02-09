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

    public static String rand(int size) {
        StringBuilder num = new StringBuilder();
        for (int i = 0; i < size; i++) {
            double a = Math.random() * 9.0D;
            a = Math.ceil(a);
            int randomNum = (new Double(a)).intValue();
            num.append(randomNum);
        }
        return num.toString();
    }



}
