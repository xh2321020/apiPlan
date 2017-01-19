package com.cnnp.social.onPlan.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.cnnp.social.base.NoContentException;
import com.cnnp.social.base.SocialSystemException;
import com.cnnp.social.onPlan.manager.dto.PlanDetailDto;
import com.cnnp.social.onPlan.manager.dto.PlanDetailSubDto;
import com.cnnp.social.onPlan.manager.dto.PlanDto;
import com.cnnp.social.onPlan.manager.dto.PlanModifyDto;
import com.cnnp.social.onPlan.repository.dao.OnPlanModifyInfoDao;
import com.cnnp.social.onPlan.repository.dao.OnPlanDao;
import com.cnnp.social.onPlan.repository.dao.OnPlanDetailDao;
import com.cnnp.social.onPlan.repository.dao.OnPlanDetailSubDao;
import com.cnnp.social.onPlan.repository.entity.PlanDetail;
import com.cnnp.social.onPlan.repository.entity.PlanDetailSub;
import com.cnnp.social.onPlan.repository.entity.PlanInfo;
import com.cnnp.social.onPlan.repository.entity.PlanModifyInfo;

@EnableTransactionManagement
@Component

public class OnPlanManager {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private OnPlanDao onPlanDao;
	@Autowired
	private OnPlanDetailDao onPlanDetailDao;
	@Autowired
	private OnPlanDetailSubDao onPlanDetailSubDao;
	@Autowired
	private OnPlanModifyInfoDao onModifyInfoDao;

	private DozerBeanMapper mapper = new DozerBeanMapper();

	private Workbook wb;
	private Sheet sheet;
	private Row row;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public PlanDto findplan(String id) {
		if (StringUtils.isEmpty(id)) {
			throw new IllegalArgumentException("bad request parameter");
		}
		PlanInfo plan = onPlanDao.findOne(id);
		if (plan == null) {
			throw new NoContentException(310); // No Content Exception
		}
		PlanDto planDto = new PlanDto();
		mapper.map(plan, planDto);
		return planDto;
	}

	public List<PlanDto> findplanlist(String id) {   //查询相关计划    +计划项+ 计划子项+ 前后置计划
		List<PlanInfo> articlecatEntries = onPlanDao.findforme(id);
		if (articlecatEntries == null) {
			return new ArrayList<PlanDto>();
		}
		List<PlanDto> palnDtos = new ArrayList<PlanDto>();

		for (PlanInfo column : articlecatEntries) {
			PlanDto dto = new PlanDto();
			mapper.map(column, dto);
			palnDtos.add(dto);
		}
		return palnDtos;
	}
	

	public PlanDto add(PlanDto plan) {  //新增计划
		if (plan == null) { 
			throw new NoContentException(310); // No Content Exception
		}
		PlanInfo entity = new PlanInfo();
		mapper.map(plan, entity);
		entity.setLastupdatetime(new Date());// 更新时间
		entity = onPlanDao.save(entity);
		if (entity == null) {
			throw new SocialSystemException(311);// Save Exception
		}
		PlanDto newPlan = new PlanDto();
		mapper.map(entity, newPlan);
		modifylog("","add",newPlan.getId(),""); //用户，描述 怎么写？
		return newPlan;
	}

	public PlanDto edit(PlanDto plan) {  //编辑计划
		PlanDto plant = findplan(plan.getId());
		if (plant.getId() == null) {
			throw new NoContentException(310); // No Content Exception
		}
		PlanInfo entity = new PlanInfo();
		mapper.map(plan, entity);
		entity.setLastupdatetime(new Date());// 更新时间
		entity = onPlanDao.save(entity);
		if (entity == null) {
			throw new SocialSystemException(311);// Save Exception
		}		
		PlanDto newPlan = new PlanDto();
		mapper.map(entity, newPlan);
		modifylog("","edit",newPlan.getId(),""); //用户，描述 怎么写？
		return newPlan;
	}
	
	public PlanDto cancel(PlanDto plan) {  //撤销计划
		PlanInfo plant = onPlanDao.findOne(plan.getId());
		if (plant.getId() == null) {
			throw new NoContentException(310); // No Content Exception
		}	
		PlanInfo entity = new PlanInfo();
		entity.setId(plant.getId());
		entity.setStatus("4");//状态1==新增  2==开始计划  3==冻结计划  4==撤销计划  5==办结计划
		entity.setLastupdatetime(new Date());// 更新时间
		entity = onPlanDao.save(entity);
		if (entity == null) {
			throw new SocialSystemException(311);// Save Exception
		}
		PlanDto newPlan = new PlanDto();
		mapper.map(entity, newPlan);
		modifylog("","cancel",newPlan.getId(),""); //用户，描述 怎么写？
		return newPlan;
	}
	
	public PlanDto freezing(PlanDto plan) { //冻结计划
		PlanInfo plant = onPlanDao.findOne(plan.getId());
		if (plant.getId() == null) {
			throw new NoContentException(310); // No Content Exception
		}	
		PlanInfo entity = new PlanInfo();
		entity.setId(plant.getId());
		entity.setStatus("3");//状态 1==新增  2==开始计划  3==冻结计划  4==撤销计划  5==办结计划
		entity.setLastupdatetime(new Date());// 更新时间
		entity = onPlanDao.save(entity);
		if (entity == null) {
			throw new SocialSystemException(311);// Save Exception
		}
		PlanDto newPlan = new PlanDto();
		mapper.map(entity, newPlan);
		modifylog("","freezing",newPlan.getId(),""); //用户，描述 怎么写？
		return newPlan;
	}
	
	public PlanDto thawing(PlanDto plan) {   //解冻计划
		PlanInfo plant = onPlanDao.findOne(plan.getId());
		if (plant.getId() == null) {
			throw new NoContentException(310); // No Content Exception
		}	
		PlanInfo entity = new PlanInfo();
		entity.setId(plant.getId());
		entity.setStatus("1");//状态 1==新增  2==开始计划  3==冻结计划  4==撤销计划  5==办结计划
		entity.setLastupdatetime(new Date());// 更新时间
		entity = onPlanDao.save(entity);
		if (entity == null) {
			throw new SocialSystemException(311);// Save Exception
		}
		PlanDto newPlan = new PlanDto();
		mapper.map(entity, newPlan);
		modifylog("","thawing",newPlan.getId(),""); //用户，描述 怎么写？
		return newPlan;
	}
	
	public PlanDto start(PlanDto plan) {  //开始计划
		PlanInfo plant = onPlanDao.findOne(plan.getId());
		if (plant.getId() == null) {
			throw new NoContentException(310); // No Content Exception
		}	
		PlanInfo entity = new PlanInfo();
		entity.setId(plant.getId());
		entity.setStatus("2");//状态 1==新增  2==开始计划  3==冻结计划  4==撤销计划  5==办结计划
		entity.setLastupdatetime(new Date());// 更新时间
		entity.setStartdate(plan.getStartdate());//开始时间
		entity = onPlanDao.save(entity);
		if (entity == null) {
			throw new SocialSystemException(311);// Save Exception
		}
		PlanDto newPlan = new PlanDto();
		mapper.map(entity, newPlan);
		modifylog("","start",newPlan.getId(),""); //用户，描述 怎么写？
		return newPlan;
	}
	
	public PlanDto finish(PlanDto plan) {  //结束计划
		PlanInfo plant = onPlanDao.findOne(plan.getId());
		if (plant.getId() == null) {
			throw new NoContentException(310); // No Content Exception
		}	
		PlanInfo entity = new PlanInfo();
		entity.setId(plant.getId());
		entity.setStatus("5");//状态 1==新增  2==开始计划  3==冻结计划  4==撤销计划  5==办结计划
		entity.setLastupdatetime(new Date());// 更新时间
		entity.setEnddate(plan.getEnddate());//计划结束时间
		entity = onPlanDao.save(entity);
		if (entity == null) {
			throw new SocialSystemException(311);// Save Exception
		}
		PlanDto newPlan = new PlanDto();
		mapper.map(entity, newPlan);
		modifylog("","finish",newPlan.getId(),""); //用户，描述 怎么写？
		return newPlan;
	}
	
	//========================计划项===================================
	
	public PlanDetailDto findplandetail(String id) {  //find 计划项
		if (StringUtils.isEmpty(id)) {
			throw new IllegalArgumentException("bad request parameter");
		}
		PlanDetail plandetail = onPlanDetailDao.findOne(id);
		if (plandetail == null) {
			throw new NoContentException(310); // No Content Exception
		}
		PlanDetailDto plandetailDto = new PlanDetailDto();
		mapper.map(plandetail, plandetailDto);
		return plandetailDto;
	}
	
	public List<PlanDetailDto> findplandetaillist(String id) {   //计划项list
		List<PlanDetail> articlecatEntries = onPlanDetailDao.findforme(id);
		if (articlecatEntries == null) {
			return new ArrayList<PlanDetailDto>();
		}
		List<PlanDetailDto> palndetailDtos = new ArrayList<PlanDetailDto>();

		for (PlanDetail column : articlecatEntries) {
			PlanDetailDto dto = new PlanDetailDto();
			mapper.map(column, dto);
			palndetailDtos.add(dto);
		}
		return palndetailDtos;
	}
	
	public PlanDetailDto addplandetail(PlanDetailDto plandetail) {  //新增计划项
		if (plandetail == null) { 
			throw new NoContentException(310); // No Content Exception
		}
		PlanDetail entity = new PlanDetail();
		mapper.map(plandetail, entity);
		// 更新时间
		entity = onPlanDetailDao.save(entity);
		if (entity == null) {
			throw new SocialSystemException(311);// Save Exception
		}
		PlanDetailDto newPlanDetail = new PlanDetailDto();
		mapper.map(entity, newPlanDetail);
		return newPlanDetail;
	}
	
	public PlanDetailDto editplandetail(PlanDetailDto plandetail) {  //编辑计划
		PlanDetailDto plantdetail = findplandetail(plandetail.getId());
		if (plantdetail.getId() == null) {
			throw new NoContentException(310); // No Content Exception
		}
		PlanDetail entity = new PlanDetail();
		mapper.map(plandetail, entity);
		//entity.setLastupdatetime(new Date());// 更新时间
		entity = onPlanDetailDao.save(entity);
		if (entity == null) {
			throw new SocialSystemException(311);// Save Exception
		}
		PlanDetailDto newPlanDetail = new PlanDetailDto();
		mapper.map(entity, newPlanDetail);
		return newPlanDetail;
	}
	
	public String delplandetail(String id) {  //删除计划项
		if (StringUtils.isEmpty(id)) {
			throw new IllegalArgumentException("bad request parameter");
		}
		PlanDetailDto plantetail = findplandetail(id);
		if (plantetail == null) {
			throw new NoContentException(310); // No Content Exception
		}
		onPlanDetailDao.delete(id);
		return "true";
	}
	
	//==============================子计划项====================================
	
	public PlanDetailSubDto findplandetailsub(String id) {  //find 子计划
		if (StringUtils.isEmpty(id)) {
			throw new IllegalArgumentException("bad request parameter");
		}
		PlanDetailSub plandetailsub = onPlanDetailSubDao.findOne(id);
		if (plandetailsub == null) {
			throw new NoContentException(310); // No Content Exception
		}
		PlanDetailSubDto plandetailsubDto = new PlanDetailSubDto();
		mapper.map(plandetailsubDto, plandetailsubDto);
		return plandetailsubDto;
	}
	
	public List<PlanDetailSubDto> findplandetailsublist(String id) {   //子计划list
		List<PlanDetailSub> articlecatEntries = onPlanDetailSubDao.findforme(id);
		if (articlecatEntries == null) {
			return new ArrayList<PlanDetailSubDto>();
		}
		List<PlanDetailSubDto> palndetailsubDtos = new ArrayList<PlanDetailSubDto>();

		for (PlanDetailSub column : articlecatEntries) {
			PlanDetailSubDto dto = new PlanDetailSubDto();
			mapper.map(column, dto);
			palndetailsubDtos.add(dto);
		}
		return palndetailsubDtos;
	}
	
	public PlanDetailSubDto addplandetailsub(PlanDetailSubDto plandetailsub) {  //新增子计划
		if (plandetailsub == null) { 
			throw new NoContentException(310); // No Content Exception
		}
		PlanDetailSub entity = new PlanDetailSub();
		mapper.map(plandetailsub, entity);
		// 更新时间
		entity = onPlanDetailSubDao.save(entity);
		if (entity == null) {
			throw new SocialSystemException(311);// Save Exception
		}
		PlanDetailSubDto newPlanDetailSub = new PlanDetailSubDto();
		mapper.map(entity, newPlanDetailSub);
		return newPlanDetailSub;
	}
	
	public PlanDetailSubDto editplandetailsub(PlanDetailSubDto plandetailsub) {  //编辑子计划
		PlanDetailSubDto plantdetailsub = findplandetailsub(plandetailsub.getId());
		if (plantdetailsub.getId() == null) {
			throw new NoContentException(310); // No Content Exception
		}
		PlanDetailSub entity = new PlanDetailSub();
		mapper.map(plantdetailsub, entity);
		//entity.setLastupdatetime(new Date());// 更新时间
		entity = onPlanDetailSubDao.save(entity);
		if (entity == null) {
			throw new SocialSystemException(311);// Save Exception
		}
		PlanDetailSubDto newPlanDetailsub = new PlanDetailSubDto();
		mapper.map(entity, newPlanDetailsub);
		return newPlanDetailsub;
	}
	
	public String delplandetailsub(String id) {  //删除子计划
		if (StringUtils.isEmpty(id)) {
			throw new IllegalArgumentException("bad request parameter");
		}
		PlanDetailSubDto plantetail = findplandetailsub(id);
		if (plantetail == null) {
			throw new NoContentException(310); // No Content Exception
		}
		onPlanDetailSubDao.delete(id);
		return "true";
	}
	//===========================日志=======================================
	
	public List<PlanModifyDto> modifyloglist(String id) {   //子计划list
		List<PlanModifyInfo> articlecatEntries = onModifyInfoDao.findforme(id);
		if (articlecatEntries == null) {
			return new ArrayList<PlanModifyDto>();
		}
		List<PlanModifyDto> modifylist = new ArrayList<PlanModifyDto>();

		for (PlanModifyInfo column : articlecatEntries) {
			PlanModifyDto dto = new PlanModifyDto();
			mapper.map(column, dto);
			modifylist.add(dto);
		}
		return modifylist;
	}
	public void modifylog(String username,String modifytype,String planid,String descript){
		PlanModifyInfo entity = new PlanModifyInfo();
		entity.setOperatordate(new Date());
		entity.setOperatorid(username);//用户明？？？
		entity.setOperatortype(modifytype);
		entity.setPlanid(planid);
		entity.setDescription(descript);//描述
		entity = onModifyInfoDao.save(entity);
		if (entity == null) {
			throw new SocialSystemException(311);// Save Exception
		}
	}
	//==========================导出========================================
	public String explorerplan(String id){
		
		return "excelname";
		
	}
}