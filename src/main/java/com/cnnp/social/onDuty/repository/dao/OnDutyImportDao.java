/*
 *
 * Copyright 2016 IBM or CNNP.
 * 
 */
package com.cnnp.social.onDuty.repository.dao;

import com.cnnp.social.onDuty.repository.entity.TDutyImport;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * apiDuty
 * Created by Damon on 2016/11/24.
 */
@Transactional
public interface OnDutyImportDao extends CrudRepository<TDutyImport, String>,JpaSpecificationExecutor<TDutyImport> {

}
