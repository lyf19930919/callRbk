import control.seerControl.BatchSetDo;
import entity.protocolReq.seerReq.seerReq.BatchSetDoReq;

/**
 * @ProjectName: callRbk$
 * @Package: PACKAGE_NAME$
 * @ClassName: MainTest$
 * @Description:
 * @Author: lyf
 * @CreateDate: 2019/11/25$ 13:56$
 * @UpdateUser: lyf
 * @UpdateDate: 2019/11/25$ 13:56$
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class MainTest {
    public static void main(String[] args) {
        BatchSetDoReq setDoEntity2 = new BatchSetDoReq(2, false);
        BatchSetDoReq setDoEntity3 = new BatchSetDoReq(3, false);
        BatchSetDoReq[] batchSetDos = new BatchSetDoReq[]{setDoEntity2, setDoEntity3};
        BatchSetDo.batchSetDoControl("", null);//设置do状态给UR手臂动作
    }

}
