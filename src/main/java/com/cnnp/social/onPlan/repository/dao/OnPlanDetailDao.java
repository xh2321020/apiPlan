package com.cnnp.social.onPlan.repository.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.cnnp.social.onPlan.repository.entity.PlanDetail;


@Transactional
public interface OnPlanDetailDao extends CrudRepository<PlanDetail, String> ,JpaSpecificationExecutor<PlanDetail>{

	
	@Query("select count(*) from PlanDetail ")
	public long countPlanDetal();
	
	@Query("select count(*) from PlanDetail plandetail where plandetail.id = ?1")
	public long countPlanDetal(String id);
	
	@Query("select pland from PlanDetail pland where pland.planid = ?1 ")
	public List<PlanDetail> findforme(String id);
}
