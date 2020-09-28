package org.kenewstar.jdbc.core;

/**
 * 排序参数类
 * @author kenewstar
 * @date 2020-08-08
 * @version 0.1
 */
public class Sort {

    /**
     * 排序的属性名
     */
    private String fieldName;
    /**
     * 排序方式
     */
    private KenewstarOrder order;
    /**
     * 降序
     */
    public static final KenewstarOrder DESC = KenewstarOrder.DESC;
    /**
     * 升序
     */
    public static final KenewstarOrder ASC = KenewstarOrder.ASC;

    /**
     * 无参构造
     */
    public Sort() {
    }

    /**
     * 全参构造
     * @param fieldName 属性名称
     * @param order 排序方式(降序|升序)
     */
    public Sort(String fieldName, KenewstarOrder order) {
        this.fieldName = fieldName;
        this.order = order;
    }

    /**
     * 排序方式枚举类
     * @author kenewstar
     */
    private enum KenewstarOrder{
        //降序,升序
        DESC, ASC;

    }

    //=====getter&&setter============//

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public KenewstarOrder getOrder() {
        return order;
    }

    public void setOrder(KenewstarOrder order) {
        this.order = order;
    }
}
