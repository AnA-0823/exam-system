package priv.ana.core.web.domain.entities;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import priv.ana.core.enums.UserRoleEnum;
import priv.ana.core.enums.UserStatusEnum;

@Data
@Accessors(chain = true)
@TableName("t_user")
public class User {
    @TableId
    private Integer id;
    private String username;
    private String password;
    private UserRoleEnum role;
    private UserStatusEnum status;
}
