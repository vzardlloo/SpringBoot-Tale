package boot.tale.model.dto;


import java.io.Serializable;

public class ThemeDto implements Serializable{
    /**
     * 主题名称
     */
    private String name;
    /**
     * 是否有配置项
     */
    private boolean hasSetting;

    public ThemeDto(String name){
        this.name = name;
    }

}
