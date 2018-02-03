package boot.tale.model.dto;


import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class LoginParam {
    @NotEmpty(message = "用户名不可为空")
    private String username;
    @NotEmpty(message = "密码不能为空")
    private String password;

    private String remeberMe;

}
