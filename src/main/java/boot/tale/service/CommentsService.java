package boot.tale.service;

import boot.tale.exception.TaleException;
import boot.tale.kit.DateKit;
import boot.tale.kit.StringKit;
import boot.tale.model.dto.Comment;
import boot.tale.model.entity.Comments;
import boot.tale.model.entity.Contents;
import boot.tale.model.repository.CommentsRepository;
import boot.tale.model.repository.ContentsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentsService {

    private ContentsRepository contentsRepository;
    private CommentsRepository commentsRepository;

    public void saveComment(Comments comment) {
        if (comment.getContent().length() < 5 || comment.getContent().length() > 2000) {
            throw new TaleException("评论字数在5-2000个字符");
        }
        if (null == comment.getCid()) {
            throw new TaleException("文章评论不能为空");
        }
        Contents contents = contentsRepository.findByCid(comment.getCid());
        if (null != contents) {
            throw new TaleException("改文章不存在");
        }
        try {
            comment.setOwner_id(contents.getAuthorId());
            comment.setCreated(DateKit.nowUnix());
            //自增主键
            comment.setCoid(null);
            comment.setParent(comment.getCoid());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 删除评论
     *
     * @param coid
     * @param cid
     */
    public void delete(Integer coid, Integer cid) {
        commentsRepository.delete(coid);
        Contents contents = contentsRepository.findByCid(cid);
        if (null != contents && contents.getCommentsNum() > 0) {
            contents.setCommentsNum(contents.getCommentsNum() - 1);
            contentsRepository.saveAndFlush(contents);
        }
    }

    /**
     * 获取文章下的评论
     *
     * @param cid
     * @param page
     * @param limit
     * @return
     */
    public Page<Comment> getComments(Integer cid, int page, int limit) {
        if (null != cid) {
            Pageable pageable = new PageRequest(page, limit, Sort.Direction.DESC, String.valueOf(cid));
            Page<Comments> cp = commentsRepository.findAll(pageable);
            return cp.map(parent -> {
                Comment comment = new Comment(parent);
                List<Comments> children = new ArrayList<>();
                getChildren(children, comment.getCoid());
                comment.setChildren(children);
                if (!children.isEmpty()) {
                    comment.setLevels(1);
                }
                return comment;
            });
        }
        return null;
    }

    /**
     * 获取评论下的追加评论
     *
     * @param list
     * @param coid
     */
    private void getChildren(List<Comments> list, Integer coid) {
        List<Comments> cms = commentsRepository.findByParentOrderByCoidDesc(coid);
        if (null != cms) {
            list.addAll(cms);
            cms.forEach(c -> getChildren(list, c.getCoid()));
        }

    }

    /**
     * 根据主键查询评论
     *
     * @param coid
     * @return
     */
    public Comments findCommentById(Integer coid) {
        if (null != coid) {
            return commentsRepository.findByCoid(coid);
        }
        return null;
    }


}
