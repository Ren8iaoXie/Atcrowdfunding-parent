package cn.xrb.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

/**
 * @author xieren8iao
 * @create 2019/11/6 - 10:18
 */
public class NoListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        System.out.println("审批不通过");
    }
}
