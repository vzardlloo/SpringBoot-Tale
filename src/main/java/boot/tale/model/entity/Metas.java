package boot.tale.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "t_metas")
public class Metas {

    // 项目主键
    @Id
    private Integer mid;
    // 名称
    private String  name;
    // 项目缩略名
    private String  slug;
    // 项目类型
    private String  type;
    // 选项描述
    private String  description;
    // 项目排序
    private Integer sort;
    // 父级
    private Integer parent;
    // 文章数
    private Integer count;

}
