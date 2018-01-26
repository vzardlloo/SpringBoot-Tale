package boot.tale.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

/**
 * 日志记录
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_logs")
public class Logs {

    // 项目主键
    @Id
    private Integer id;

    // 产生的动作
    private String action;

    // 产生的数据
    private String data;

    // 发生人id
    private Integer author_id;

    // 日志产生的ip
    private String ip;

    // 日志创建时间
    private Integer created;

    public Logs(String action, String data, String ip, Integer uid) {
        this.action = action;
        this.data = data;
        this.ip = ip;
        this.author_id = uid;
        this.created = (int)Instant.now().getEpochSecond();
    }
}
