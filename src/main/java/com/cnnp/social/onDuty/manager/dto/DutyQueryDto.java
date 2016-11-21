/*
 *
 * Copyright 2016 IBM or CNNP.
 * 
 */
package com.cnnp.social.onDuty.manager.dto;

import java.util.Date;

/**
 * apiDuty
 * Created by Damon on 2016/11/17.
 */
public class DutyQueryDto {
    private Date startDate; //值班开始日期
    private Date endDate;//值班结束日期
    private String companyCode; //公司编码
    private String departmentCode;//部门编码

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }
}
