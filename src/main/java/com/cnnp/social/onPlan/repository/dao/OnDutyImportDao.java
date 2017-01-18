/*
 *
 * Copyright 2016 IBM or CNNP.
 * 
 */
package com.cnnp.social.onPlan.repository.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.cnnp.social.onPlan.repository.entity.PlanModifyInfo;

/**
 * apiDuty
 * Created by Damon on 2016/11/24.
 */
@Transactional
public interface OnDutyImportDao extends CrudRepository<PlanModifyInfo, String>,JpaSpecificationExecutor<PlanModifyInfo> {

}
