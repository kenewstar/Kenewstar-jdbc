package com.kenewstar.multiple;

import org.kenewstar.jdbc.annotation.Column;
import org.kenewstar.jdbc.annotation.Id;
import org.kenewstar.jdbc.annotation.Table;

import java.util.UUID;

/**
 * @author xinke.huang@hand-china.com
 * @version 1.0
 * @date 2021/4/1
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

    @Column(columnName = "dept_id")
    private Integer deptId;

    @Column(columnName = "company_id")
    private Integer companyId;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public User(Integer id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
    public User() {}

    @Override
    public String toString() {

        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", deptId=" + deptId +
                ", companyId=" + companyId +
                '}';
    }

}
