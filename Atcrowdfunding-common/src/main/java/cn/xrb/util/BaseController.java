package cn.xrb.util;

import java.util.HashMap;
import java.util.Map;

/**所有controller的抽取
 * @author xieren8iao
 * @create 2019/10/17 - 13:25
 */
public class BaseController {
    private ThreadLocal<Map<String,Object>> datas=new ThreadLocal<Map<String, Object>>();
    protected void start(){
        Map<String,Object> resultMap=new HashMap<String, Object>();
        datas.set(resultMap);
    }
    protected Object end(){
        Map<String,Object> resultMap=datas.get();
        datas.remove();
        return resultMap;
    }
    protected void success(boolean flag){
        Map<String,Object> resultMap=datas.get();
        resultMap.put("success",flag);
    }
    public void param(String key,Object value){
        Map<String,Object>  resultMap=datas.get();
        resultMap.put(key, value);
    }
    public void error(String msg){
        Map<String,Object> resultMap=datas.get();
        resultMap.put("messeage", msg);

    }
}
