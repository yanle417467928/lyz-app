package cn.com.leyizhuang.app.foundation.pojo;


/**
 * @author CrazyApeDX
 *         Created on 2017/3/24.
 */
public class Manager {

    private static final long serialVersionUID = -2016854268132811202L;

    public Manager() {
        super();
    }

    public Manager(Long id, String name, String mobile) {
        super();
        this.id = id;
        this.name = name;
        this.mobile = mobile;
    }

    private Long id;
    private String name;
    private String mobile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
