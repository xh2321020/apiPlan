package com.cnnp.social.onPlan.repository.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.cnnp.social.onPlan.repository.entity.PlanDetailSub;


@Transactional
public interface OnPlanDetailSubDao extends CrudRepository<PlanDetailSub, String> ,JpaSpecificationExecutor<PlanDetailSub>{

	
	@Query("select count(*) from PlanDetailSub ")
	public long countPlanDetalSub();
	
	@Query("select count(*) from PlanDetailSub plandetail where plandetail.id = ?1")
	public long countPlanSub(Long id);
	
	@Query("select plandsub from PlanDetailSub plandsub where plandsub.projectid = ?1 ")
	public List<PlanDetailSub> findforme(String id);
}
