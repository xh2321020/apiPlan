package com.cnnp.social.onDuty.repository.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import com.cnnp.social.onDuty.repository.entity.TDutyImport;

@Transactional
public interface OnDutyImportDao extends CrudRepository<TDutyImport, Long> ,JpaSpecificationExecutor<TDutyImport>{
	
	@Query("select dutyImport from TDutyImport dutyImport where  dutyImport.userid = ?1  and (dutyImport.startdate BETWEEN ?2 AND ?3 OR dutyImport.enddate BETWEEN ?2 AND ?3 OR ?2 Between dutyImport.startdate AND dutyImport.enddate OR ?3 BETWEEN dutyImport.startdate AND dutyImport.enddate) ")
	public List<TDutyImport> finduser(long userid,String startdate,String enddate);
	
	@Query("delete from TDutyImport dutyImport where  dutyImport.userid = ?1  and to_char(dutyImport.startdate) = ?2 AND to_char(dutyImport.enddate)= ?3 ")
	public void deleteItem(long userid,String startdate,String enddate);


}
