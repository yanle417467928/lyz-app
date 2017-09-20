package cn.com.leyizhuang.app.foundation.pojo;

import java.io.Serializable;

/**
 * 工序包列表
 * Created by caiyu on 2017/9/18.
 */

public class Process implements Serializable {

    private static final long serialVersionUID = 1713125788030092982L;
    //工序ID
    private long id;
    //工序名称
    private String ProcessName;

    public Process() {
        super();
    }

    public Process(Long id, String processName) {
        this.id = id;
        this.ProcessName = processName;
    }

    @Override
    public String toString() {
        return "Process{" +
                "id=" + id +
                ", ProcessName='" + ProcessName + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProcessName() {
        return ProcessName;
    }

    public void setProcessName(String processName) {
        this.ProcessName = processName;
    }
}
