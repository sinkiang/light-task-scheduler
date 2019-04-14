package com.github.ltsopensource.store.jdbc.builder;

/**
 * mysql或者oracle的分隔符
 * Created by zhangjianjun on 2017/5/24.
 */
public enum Delim {
    MYSQL(0, "`"), ORACLE(1, "\"");

    private Integer id;

    private String name;

    private Delim(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public String get(){
        return name;
    }
    
    public Integer getId() {
        return id;
    }

    public String getLeftSpaces(){
        return " "+ name;
    }

    public String getRightSpaces(){
        return name+" ";
    }

}
