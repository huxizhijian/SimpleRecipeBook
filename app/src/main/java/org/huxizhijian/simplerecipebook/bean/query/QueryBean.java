package org.huxizhijian.simplerecipebook.bean.query;

/**
 * @author huxizhijian 2017/5/1
 */
public class QueryBean {

    private String msg;

    private Result result;

    private String retCode;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Result getResult() {
        return this.result;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetCode() {
        return this.retCode;
    }

    @Override
    public String toString() {
        return "QueryBean{" +
                "msg='" + msg + '\'' +
                ", result=" + result +
                ", retCode='" + retCode + '\'' +
                '}';
    }
}
