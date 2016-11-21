package com.cnnp.social.onDuty.repository.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.cnnp.social.onDuty.repository.entity.TDutyUser;

@Transactional
public interface OnDutyUserDao extends CrudRepository<TDutyUser, Long> ,JpaSpecificationExecutor<TDutyUser>{
	
	@Query("select max(cast(id as float)) from TDutyUser ")
	public long findmaxid();
	@Query("select count(*) from TDutyUser dutyUser where dutyUser.dutyid = ?1")
	public long userCount(Long id);
	@Query("select dutyUser from TDutyUser dutyUser where dutyUser.userid = ?1")
	public List<TDutyUser> findByUserId(Long id);
	
	@Query("select dutyUser from TDutyUser dutyUser where dutyUser.dutyid = ?1")
	public List<TDutyUser> findByDutyId(Long id);
	
	@Query("select dutyUser from TDutyUser dutyUser where  dutyUser.userid = ?1  and (dutyUser.startdate BETWEEN to_date(?2, 'YYYY-MM-DD HH24:MI:SS') AND to_date(?3, 'YYYY-MM-DD HH24:MI:SS') OR dutyUser.enddate BETWEEN to_date(?2, 'YYYY-MM-DD HH24:MI:SS') AND to_date(?3, 'YYYY-MM-DD HH24:MI:SS') OR to_date(?2, 'YYYY-MM-DD HH24:MI:SS') Between dutyUser.startdate AND dutyUser.enddate OR to_date(?3, 'YYYY-MM-DD HH24:MI:SS') BETWEEN dutyUser.startdate AND dutyUser.enddate) ")
	public List<TDutyUser> finduser(long userid,String startdate,String enddate);
}
