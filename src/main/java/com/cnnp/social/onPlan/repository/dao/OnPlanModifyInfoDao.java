package com.cnnp.social.onPlan.repository.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import com.cnnp.social.onPlan.repository.entity.PlanModifyInfo;


@Transactional
public interface OnPlanModifyInfoDao extends CrudRepository<PlanModifyInfo, String> ,JpaSpecificationExecutor<PlanModifyInfo>{

	
	@Query("select count(*) from PlanModifyInfo ")
	public long countPlan();
	
	@Query("select count(*) from PlanModifyInfo planmi where planmi.id = ?1")
	public long countPlan(Long id);
	
	@Query("select planmi from PlanModifyInfo planmi where planmi.planid = ?1 ")
	public List<PlanModifyInfo> findforme(String id);
}
