package boot.tale.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "t_options")
public class OPtions {

    // 配置名称
    @Id
    private String name;

    // 配置值
    private String value;

    // 配置描述
    private String description;
}
