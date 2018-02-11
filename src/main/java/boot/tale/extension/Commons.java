package boot.tale.extension;


import boot.tale.kit.CollectionKit;
import boot.tale.kit.StringKit;
import boot.tale.service.SiteService;
import org.springframework.data.domain.Page;

public class Commons {

    private static SiteService siteService;

    private static final String TEMPLATES = "/templates/";

    public static void setSiteService(SiteService ss) {
        Theme.setSiteService(ss);
    }

    /**
     * 判断分页中是否有数据
     *
     * @param paginator
     * @return
     */
    public static boolean is_empty(Page paginator) {
        return null == paginator || paginator.getTotalPages() == 0;
    }

    /**
     * 判断字符串不为空
     *
     * @param string
     * @return
     */
    public static boolean not_empty(String string) {
        return StringKit.isNotBlank(string);
    }


}
