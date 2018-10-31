package com.mmuu.travel.client.bean;

import java.util.List;

/**
 * 描述
 * JiaGuangWei on 2017/1/25 17:32
 */
public class FeelBackBean {
    /**
     * checkName : 无法解锁
     * id : 11
     * parentId : 8
     */

    private String checkName;
    private int id;
    private int parentId;

    private String relation;
    private String relationName;

    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }


    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }
}
