package boot.tale.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "t_relationships")

public class Relationships {

    // 内容主键
    @NotNull
    private Integer cid;

    // 项目主键
    @Id
    @NotNull
    private Integer mid;

}
