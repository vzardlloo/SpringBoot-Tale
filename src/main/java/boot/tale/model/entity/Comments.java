package boot.tale.model.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "t_comments")
public class Comments {

    // comment表主键
    @Id
    private Integer coid;
    //内容表主键,关联字段
    private Integer cid;
    //评论生成时的GMT unix时间戳
    private Integer created;

    @NotEmpty(message = "请输入评论作者")
    @Length(max = 30,message = "姓名过长")
    private String author;
    //评论所属用户id
    private Integer author_id;
    //评论所属内容的作者id
    private Integer owner_id;
    //评论者邮件
    @NotEmpty(message = "请输入电子邮箱")
    @Email(message = "请输入正确的邮箱格式")
    private String mail;
    //评论者网址
    @URL
    private String url;
    //评论者ip地址
    private String ip;
    //评论者客户端
    private String agnet;

    @NotEmpty(message = "请输入评论内容")
    @Length(max = 2000,message = "请输入%d字符以内的评论")
    private String content;
    //评论类型
    private String type;
    //评论状态
    private String status;
    //父级评论
    private Integer parent;


}
