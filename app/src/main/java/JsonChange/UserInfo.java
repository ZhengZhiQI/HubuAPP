package JsonChange;

/**
 * Created by Alan on 2017/1/21.
 */
//json转换成java实例方法
public class UserInfo {
    /**
     * id : 0
     * name : Alan
     * password : 123
     */

    private String id;
    private String name;
    private String password;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
