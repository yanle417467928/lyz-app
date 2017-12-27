package cn.com.leyizhuang.app.foundation.pojo.queue;

import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author 翟永超
 * @create 2016/11/13.
 * @blog http://blog.didispace.com
 */
@Component
public class QueueTestUser implements Serializable {

    private String name;
    private Integer age;

    public QueueTestUser() {

    }

    public QueueTestUser(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "name=" + name + ", age=" + age;
    }
}
