package org.kenewstar.jdbc.demo;

import org.kenewstar.jdbc.annotation.Column;
import org.kenewstar.jdbc.annotation.Id;
import org.kenewstar.jdbc.annotation.Param;
import org.kenewstar.jdbc.annotation.Table;

/**
 * 用户实体类
 * @author kenewstar
 */
@Table
public class User {

    @Id
    @Column
    private Integer id;
    @Column
    private String name;
    @Column
    private Integer age;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName( String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public User(){

    }
    public User(Integer id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
