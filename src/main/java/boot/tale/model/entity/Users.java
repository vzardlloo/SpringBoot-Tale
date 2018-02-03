package boot.tale.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_users")
public class Users {

    // User 表主键
    @Id
    @Column(unique = true)
    private Integer uid;
    // 用户名
    @NotNull
    @Column(unique = true)
    private String username;
    // 用户密码
    @NotNull
    private String password;
    //用户邮箱
    private String mail;
    //用户主页
    private String home_url;
    //用户显示的时间
    private String screen_name;
    //用户注册时的GMT unix时间戳
    @NotNull
    private Integer created;
    //最后活动时间
    private Integer activated;
    //上次登录最后活跃时间
    private Integer logged;
    //用户组
    private String group_name;
}
