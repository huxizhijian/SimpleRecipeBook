package org.huxizhijian.simplerecipebook.bean.search;

/**
 * @author huxizhijian 2017/5/1
 */
public class SearchBean {

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
        return "SearchBean{" +
                "msg='" + msg + '\'' +
                ", result=" + result +
                ", retCode='" + retCode + '\'' +
                '}';
    }
}
