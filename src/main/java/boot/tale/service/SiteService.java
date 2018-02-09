package boot.tale.service;

import boot.tale.configuration.DataSourseConfiguration;
import boot.tale.controller.admin.AttachController;
import boot.tale.exception.TaleException;
import boot.tale.kit.*;
import boot.tale.model.dto.*;
import boot.tale.model.entity.*;
import boot.tale.model.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SiteService {

    @Autowired
    private CommentsService commentsService;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private LogsRepository logsRepository;
    @Autowired
    private CommentsRepository commentsRepository;
    @Autowired
    private ContentsRepository contentsRepository;
    @Autowired
    private AttachRepository attachRepository;
    @Autowired
    private MetasRepository metasRepository;
    //缓存
    public TaleCache taleCache = new TaleCache();

    public void initSite(Users users) {
        String pwd = DigestUtils.md5Digest((users.getUsername() + users.getPassword()).getBytes()).toString();
        users.setPassword(pwd);
        users.setScreen_name(users.getUsername());
        users.setCreated(DateKit.nowUnix());

        Integer uid = usersRepository.save(users).getUid();

        try {
            String cp = SiteService.class.getClassLoader().getResource("").getPath();
            File lock = new File(cp + "install.lock");
            lock.createNewFile();
            ConstKit.INSTALLED = Boolean.TRUE;
            Logs logs = new Logs(LogActions.INIT_SITE, null, "", uid.intValue());
            logsRepository.save(logs);
        } catch (Exception e) {
            throw new TaleException("初始化站点失败");
        }
    }

    /**
     * 最新收到的评论
     *
     * @param limit 评论数
     * @return
     */
    public List<Comments> recentComments(int limit) {
        if (limit < 0 || limit > 10) {
            limit = 10;
        }
        Pageable pageable = new PageRequest(1, limit, Sort.Direction.DESC);
        Page<Comments> commentsPage = commentsRepository.findAll(pageable);
        return this.castPageToList(commentsPage);
    }

    public List<Contents> getContents(String type, int limit) {
        if (limit < 0 || limit > 20) {
            limit = 10;
        }
        //最新文章
        if (Types.RECENT_ARTICLE.equals(type)) {
            Pageable pageable = new PageRequest(1, limit, Sort.Direction.DESC);
            Page<Contents> contentsPage = contentsRepository.findAllByStatusAndType(Types.PUBLISH, Types.ARTICLE, pageable);
            return this.castPageToList(contentsPage);
        }
        //随机文章
        if (Types.RANDOM_ARTICLE.equals(type)) {
            List<Contents> contentsList = contentsRepository.findAllByTypeAndStatus(Types.ARTICLE, Types.PUBLISH)
                    .stream().limit(10)
                    .collect(Collectors.toList());
            return contentsList;
        }
        return new ArrayList<>();
    }

    /**
     * 获取后台统计数据
     *
     * @return
     */
    public Statistics getStatistics() {
        Statistics statistics = taleCache.get(Types.C_STATISTICS);
        if (null != statistics) {
            return statistics;
        }
        statistics = new Statistics();

        long articles = contentsRepository.countByType(Types.ARTICLE);
        long pages = contentsRepository.countByType(Types.PAGE);
        long comments = commentsRepository.findAll().size();
        long attachs = attachRepository.findAll().size();
        long tags = metasRepository.countByType(Types.TAG);
        long categories = metasRepository.countByType(Types.CATEGORY);

        statistics.setArticles(articles);
        statistics.setPages(pages);
        statistics.setComments(comments);
        statistics.setAttachs(attachs);
        statistics.setAttachs(tags);
        statistics.setCategories(categories);

        taleCache.set(Types.C_STATISTICS, statistics);
        return statistics;
    }

    public List<Archive> getArchives() {
        List<Archive> archives = attachRepository.getAttaches();
        if (null != archives) {
            return archives.stream()
                    .map(this::parseArchive)
                    .collect(Collectors.toList());
        }
        return Collections.EMPTY_LIST;
    }

    private Archive parseArchive(Archive archive) {
        String date_str = archive.getData_str();
        Date sd = DateKit.toDate(date_str + "01", "yyyy年MM月dd");
        archive.setDate(sd);
        int start = DateKit.toUnix(sd);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sd);
        calendar.add(Calendar.MONTH, 1);
        Date endSd = calendar.getTime();
        int end = DateKit.toUnix(endSd) - 1;

        List<Contents> contents = contentsRepository.findByTypeAndStatusAndCreatedIsBetweenOrderByCreatedDesc(Types.ARTICLE, Types.PUBLISH, start, end);

        archive.setArticles(contents);

        return archive;
    }

    /**
     * 查询一条评论
     *
     * @param coid
     * @return
     */
    public Comments getComment(Integer coid) {
        if (null != coid) {
            return commentsRepository.findByCoid(coid);
        }
        return null;
    }

    /**
     * 系统备份
     *
     * @param bkType
     * @param bkPath
     * @param fmt
     * @return
     * @throws Exception
     */
    public BackResponse backup(String bkType, String bkPath, String fmt) throws Exception {
        BackResponse backResponse = new BackResponse();
        //备份附件
        if ("attach".equals(bkType)) {
            if (!StringKit.isNotBlank(bkPath)) {
                throw new TaleException("请输入备份文件存储路径");
            }
            if (!Files.isDirectory(Paths.get(bkPath))) {
                throw new TaleException("请输入一个存在的目录");
            }
            String bkAttachDir = AttachController.CLASSPATH + "upload";
            String bkThemesDir = AttachController.CLASSPATH + "templates/theme";

            String fname = DateKit.toString(new Date(), fmt) + "_" + StringKit.rand(5) + ".zip";

            String attachPath = bkPath + "/" + "attachs_" + fname;
            String themePath = bkPath + "/" + "themes_" + fname;

            ZipUtils.zipFolder(bkAttachDir, attachPath);
            ZipUtils.zipFolder(bkThemesDir, themePath);

            backResponse.setAttach_path(attachPath);
            backResponse.setTheme_path(themePath);
        }
        //备份数据库
        if ("db".equals(bkType)) {
            String filePath = "upload/" + DateKit.toString(new Date(), "yyyyMMddHHmmss") + "_" + StringKit.rand(8) + ".db";
            String cp = AttachController.CLASSPATH + filePath;
            Files.createDirectories(Paths.get(cp));
            Files.copy(Paths.get(DataSourseConfiguration.DB_PATH), Paths.get(cp));
            backResponse.setSql_path("/" + filePath);
            //
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    new File(cp).delete();
                }
            }, 10 * 1000);
        }

        return backResponse;
    }

    public List<Metas> getMetas(String searchType, String type, int limit) {


        return null;
    }


    //========util=====
    private <T> List<T> castPageToList(Page<T> page) {
        List list = new ArrayList();
        for (T p : page) {
            list.add(p);
        }
        return list;
    }


}
