package com.kenewstar.test;

import org.kenewstar.jdbc.annotation.Column;
import org.kenewstar.jdbc.annotation.Id;
import org.kenewstar.jdbc.annotation.Table;
import org.kenewstar.jdbc.core.base.BaseEntity;

/**
 * @author xinke.huang@hand-china.com
 * @version 1.0
 * @date 2021/4/6
 */
@Table
public class Dept extends BaseEntity {
    @Id
    @Column
    private Integer id;
    @Column
    private String name;

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
}
