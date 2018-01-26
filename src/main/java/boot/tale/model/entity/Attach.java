package boot.tale.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 附件
 */
@Data
@Entity
@Table(name = "t_attach")
public class Attach {
    @Id
    private Integer id;
    //附件名称
    private String fname;
    //附件类型
    private String ftype;
    //附件key
    private String fkey;
    //附件作者id
    private String author_id;
    private String create;
}
