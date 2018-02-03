package boot.tale.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_options",uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
public class Options {

    // 配置名称
    @Id
    private String name;

    // 配置值
    private String value;

    // 配置描述
    private String description;
}
