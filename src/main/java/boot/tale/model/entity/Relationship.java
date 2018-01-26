package boot.tale.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "t_relationships")
public class Relationship {

    // 内容主键
    private Integer cid;

    // 项目主键
    @Id
    private Integer mid;

}
