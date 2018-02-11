package boot.tale.extension;


import boot.tale.service.SiteService;
import ch.qos.logback.core.joran.spi.InterpretationContext;
import jetbrick.template.runtime.InterpretContext;

import java.util.ArrayList;
import java.util.List;

public class Theme {

    private static SiteService siteService;

    public static final List Empty = new ArrayList(0);

    public static void setSiteService(SiteService ss) {
        siteService = ss;
    }

    /**
     * 获取header keywords
     *
     * @return
     */
    public static String meta_keywords() {
        InterpretContext ctx = InterpretContext.current();
        Object value = ctx.getValueStack().getValue("keywords");
        if (null != value) {
            return value.toString();
        }

        return null;
    }

}
