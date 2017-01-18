package com.cnnp.social.onPlan.repository.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.cnnp.social.onPlan.repository.entity.PlanInfo;


@Transactional
public interface OnPlanDao extends CrudRepository<PlanInfo, String> ,JpaSpecificationExecutor<PlanInfo>{

	
	@Query("select count(*) from PlanInfo ")
	public long countPlan();
	
	@Query("select count(*) from PlanInfo plan where plan.id = ?1")
	public long countPlan(Long id);
	
	@Query("select plan from PlanInfo plan where plan.createuserid = ?1 or leaderid = ?1 ")
	public List<PlanInfo> findforme(String id);
}
