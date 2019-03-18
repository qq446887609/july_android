package cn.tasays.www.july.api;

public class UserApi extends BaseAPi {

    public void login(String address,String mobile,String password)
    {
        base_url = base_url + address;
    }

}
