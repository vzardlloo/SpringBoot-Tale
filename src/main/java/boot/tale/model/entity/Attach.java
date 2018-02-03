package boot.tale.model.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 附件
 */
@Data
@Entity
@Table(name = "t_attach")
public class Attach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //附件名称
    private String fname;
    //附件类型
    @NotNull
    private String ftype;
    //附件key
    @NotNull
    private String fkey;
    //附件作者id
    @NotNull
    private Integer author_id;
    @NotNull
    private Integer created;
}
