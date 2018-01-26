package boot.tale.model.entity;

import boot.tale.kit.ConstKit;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "t_contents")
public class Contents {
    // 主键
    @Id
    private Integer cid;
    //内容标题
    @NotEmpty(message = "标题不可以为空")
    @Length(max = ConstKit.MAX_TITLE_COUNT,message = "文章标题最多可以输入%d个字符")
    private String title;
    //内容缩略名
    private String slug;
    //内容生成的GMT unix时间戳
    private Integer created;
    //内容更改的GMT unix时间戳
    private Integer modified;
    //内容文字
    @NotEmpty(message = "内容不可以为空")
    private String content;
    //内容所属用户id
    private Integer authorId;
    //点击次数
    private Integer hits;
    //内容类别
    private String type;
    //内容类型，Markdown or html
    private String fmtType;
    //文章缩略图
    private String thumbImg;
    //标签列表
    private String tags;
    //分类列表
    private String categories;
    //内容的评论数量
    private Integer commentsNum;
    //是否允许评论
    private Boolean allowComment;
    //是否允许出现在聚合中
    private Boolean allowFeed;
}