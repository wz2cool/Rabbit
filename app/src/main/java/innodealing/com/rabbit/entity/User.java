package innodealing.com.rabbit.entity;

import java.io.Serializable;

/**
 * Description innodealing.com.rabbit
 * Created on 2018/3/14.
 */

public class User implements Serializable{
    private String name;
    private long phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }
}
