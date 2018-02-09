package boot.tale.service;

import boot.tale.exception.TaleException;
import boot.tale.kit.ConstKit;
import boot.tale.kit.DateKit;
import boot.tale.kit.StringKit;
import boot.tale.kit.TaleKits;
import boot.tale.model.dto.Types;
import boot.tale.model.entity.Contents;
import boot.tale.model.repository.CommentsRepository;
import boot.tale.model.repository.ContentsRepository;
import boot.tale.model.repository.RelationshipsRepository;
import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContentsService {
    @Autowired
    private MetasService metasService;
    @Autowired
    private ContentsRepository contentsRepository;
    @Autowired
    private RelationshipsRepository relationshipsRepository;
    @Autowired
    private CommentsRepository commentsRepository;

    /**
     * 根据id获取文章
     *
     * @param id
     * @return
     */
    public Optional<Contents> getContents(String id) {
        if (StringKit.isNotBlank(id)) {
            return Optional.ofNullable(contentsRepository.findByCid(Integer.parseInt(id)));
        }
        return Optional.empty();
    }

    public Integer publish(Contents contents) {
        if (null == contents) {
            throw new TaleException("文章对象为空");
        }
        if (StringKit.isNotBlank(contents.getTitle())) {
            throw new TaleException("文章标题不能为空");
        }

        if (contents.getTitle().length() > ConstKit.MAX_TITLE_COUNT) {
            throw new TaleException("文章标题最多可以输入" + ConstKit.MAX_TITLE_COUNT);
        }
        if (StringKit.isNotBlank(contents.getContent())) {
            throw new TaleException("文章内容不能为空");
        }

        //最多可以输入5w字
        int len = contents.getContent().length();
        if (len > ConstKit.MAX_TITLE_COUNT) {
            throw new TaleException("文章内容最多可以输入" + ConstKit.MAX_TITLE_COUNT + "个字符");
        }

        if (null == contents.getAuthorId()) {
            throw new TaleException("请登录后发布文章");
        }

        if (StringKit.isNotBlank(contents.getSlug())) {
            if (contents.getSlug().length() < 5) {
                throw new TaleException("路径太短了");
            }
            if (!TaleKits.isPath(contents.getSlug())) {
                throw new TaleException("你输入的路径不合法");
            }
            int count = contentsRepository.countBySlugAndType(contents.getSlug(), contents.getType());
            if (count > 0) {
                throw new TaleException("该路径已经存在");
            }
        }

        contents.setContent(EmojiParser.parseToAliases(contents.getContent()));

        int time = DateKit.nowUnix();
        contents.setCreated(time);
        contents.setModified(time);

        String tags = contents.getTags();
        String categories = contents.getCategories();

        Integer cid = contentsRepository.saveAndFlush(contents).getCid();

        metasService.saveMetas(cid, tags, Types.TAG);
        metasService.saveMetas(cid, categories, Types.CATEGORY);

        return cid;
    }

    public void updateArticle(Contents contents) {
        contents.setModified(DateKit.nowUnix());
        contents.setContent(EmojiParser.parseToAliases(contents.getContent()));
        contents.setTags(contents.getTags() != null ? contents.getTags() : "");
        contents.setCategories(contents.getCategories() != null ? contents.getCategories() : "");

        String tags = contents.getTags();
        String categories = contents.getCategories();
        Integer cid = contents.getCid();

        contentsRepository.saveAndFlush(contents);

        if (null != contents.getType() && !contents.getType().equals(Types.PAGE)) {
            relationshipsRepository.delete(cid);
        }

        metasService.saveMetas(cid, tags, Types.TAG);
        metasService.saveMetas(cid, categories, Types.CATEGORY);
    }

    /**
     * 根据文章id删除
     *
     * @param cid
     */
    public void delete(int cid) {
        Optional<Contents> contents = this.getContents(cid + "");
        contents.ifPresent(contents1 -> {
            contentsRepository.delete(cid);
            relationshipsRepository.delete(cid);
            commentsRepository.deleteByCid(cid);
        });
    }

    /**
     * 查询分类/标签下的文章归档
     *
     * @param mid
     * @param page
     * @param offset
     * @return
     */
    public Page<Contents> getArticles(Integer mid, int page, int offset) {
        Pageable pageable = new PageRequest(page, offset, Sort.Direction.DESC, String.valueOf(mid));
        return contentsRepository.findAll(pageable);
    }


}
