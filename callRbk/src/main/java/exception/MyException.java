package exception;

import lombok.Data;

/**
 * @ProjectName: callRbk$
 * @Package: exception$
 * @ClassName: ModbusException$
 * @Description:
 * @Author: lyf
 * @CreateDate: 2020/3/22$ 14:13$
 * @UpdateUser: lyf
 * @UpdateDate: 2020/3/22$ 14:13$
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
@Data
public class MyException extends RuntimeException {
    private String fromSite; //异常类来源
    private String message;  //异常信息

    public MyException(String fromSite, String message) {
        super(message);
        this.message = message;
        this.fromSite = fromSite;
    }
}
