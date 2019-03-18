package cn.tasays.www.july.model;

/**
 * Created by 适可而止 on 2019/3/19.
 * Desc:
 */

public class VerificationCodes {
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String key;//发送图片验证码后返回的key
}
