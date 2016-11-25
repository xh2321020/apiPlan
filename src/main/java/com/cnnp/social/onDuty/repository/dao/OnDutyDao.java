package com.cnnp.social.onDuty.repository.dao;

import com.cnnp.social.onDuty.repository.entity.TDuty;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Transactional
public interface OnDutyDao extends CrudRepository<TDuty, String> ,JpaSpecificationExecutor<TDuty>{
	
	@Query("select max(cast(id as float)) from TDuty ")
	public long findmaxid();
	
	@Query("select count(*) from TDuty ")
	public long countDuty();
	
	@Query("select count(*) from TDuty duty where duty.id = ?1")
	public long countDuty(Long id);
	
	@Query("select duty from TDuty duty where duty.id = ?1")
	public TDuty findByDutyId(Long id);



}
