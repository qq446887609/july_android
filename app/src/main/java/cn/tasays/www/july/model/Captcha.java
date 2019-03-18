package cn.tasays.www.july.model;

public class Captcha {

    private  String captcha_key;//图片验证码key 用以获得服务器缓存保存的手机号 和图片code
    private  String captcha_image_content; //图片验证码base64位
    private  String expired_at;//过期时间

    public String getCaptcha_key() {
        return captcha_key;
    }

    public void setCaptcha_key(String captcha_key) {
        this.captcha_key = captcha_key;
    }

    public String getCaptcha_image_content() {
        return captcha_image_content;
    }

    public void setCaptcha_image_content(String captcha_image_content) {
        this.captcha_image_content = captcha_image_content;
    }

    public String getExpired_at() {
        return expired_at;
    }

    public void setExpired_at(String expired_at) {
        this.expired_at = expired_at;
    }
}
