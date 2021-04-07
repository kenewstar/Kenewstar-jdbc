package com.kenewstar;

import com.kenewstar.multiple.Company;
import com.kenewstar.multiple.Dept;
import com.kenewstar.multiple.User;
import org.kenewstar.jdbc.annotation.OfTable;

/**
 * @author xinke.huang@hand-china.com
 * @version 1.0
 * @date 2021/4/1
 */
public class UserAndDeptDTO {

    @OfTable(entityClass = User.class)
    private Integer id;

    @OfTable(entityClass = User.class)
    private String name;

    @OfTable(entityClass = User.class)
    private Integer age;

    @OfTable(entityClass = Dept.class, fieldName = "name")
    private String deptName;

    @OfTable(entityClass = Company.class, fieldName = "name")
    private String companyName;

    @Override
    public String toString() {
        return "UserAndDeptDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", deptName='" + deptName + '\'' +
                ", companyName='" + companyName + '\'' +
                '}';
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }


}
