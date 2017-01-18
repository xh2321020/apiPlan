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
import com.cnnp.social.onPlan.repository.dao.OnModifyInfoDao;
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
	private OnPlanDetailDao onPlanDetailDao;
	private OnPlanDetailSubDao onPlanDetailSubDao;
	private OnModifyInfoDao onModifyInfoDao;
	// @Autowired
	// private OnDutyImportDao onDutyImportDao;

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
		modifylog("","add",entity.getParentid(),""); //用户，描述 怎么写？
		
		PlanDto newPlan = new PlanDto();
		mapper.map(entity, newPlan);
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
		modifylog("","edit",entity.getParentid(),""); //用户，描述 怎么写？
		
		PlanDto newPlan = new PlanDto();
		mapper.map(entity, newPlan);
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
		modifylog("","cancel",entity.getParentid(),""); //用户，描述 怎么写？
		PlanDto newPlan = new PlanDto();
		mapper.map(entity, newPlan);
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
		modifylog("","freezing",entity.getParentid(),""); //用户，描述 怎么写？
		PlanDto newPlan = new PlanDto();
		mapper.map(entity, newPlan);
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
		modifylog("","thawing",entity.getParentid(),""); //用户，描述 怎么写？
		PlanDto newPlan = new PlanDto();
		mapper.map(entity, newPlan);
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
		modifylog("","start",entity.getParentid(),""); //用户，描述 怎么写？
		PlanDto newPlan = new PlanDto();
		mapper.map(entity, newPlan);
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
		modifylog("","finish",entity.getParentid(),""); //用户，描述 怎么写？
		PlanDto newPlan = new PlanDto();
		mapper.map(entity, newPlan);
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
	
	public void delplandetail(String id) {  //删除计划项
		if (StringUtils.isEmpty(id)) {
			throw new IllegalArgumentException("bad request parameter");
		}
		PlanDetailDto plantetail = findplandetail(id);
		if (plantetail == null) {
			throw new NoContentException(310); // No Content Exception
		}
		onPlanDetailDao.delete(id);
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
	
	public void delplandetailsub(String id) {  //删除子计划
		if (StringUtils.isEmpty(id)) {
			throw new IllegalArgumentException("bad request parameter");
		}
		PlanDetailSubDto plantetail = findplandetailsub(id);
		if (plantetail == null) {
			throw new NoContentException(310); // No Content Exception
		}
		onPlanDetailSubDao.delete(id);
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
		PlanModifyInfo pmi = new PlanModifyInfo();
		pmi.setOperatordate(new Date());
		pmi.setOperatorid(username);//用户明？？？
		pmi.setOperatortype(modifytype);
		pmi.setPlanid(planid);
		pmi.setDescription(descript);//描述
		onModifyInfoDao.save(pmi);
	}
	
	
	// public void delete(String id){
		// if(StringUtils.isEmpty(id)){
		// throw new IllegalArgumentException("bad request parameter");
		// }
		// PlanInfo duty=onDutyDao.findOne(id);
		// if(duty==null){
		// throw new NoContentException(310); //No Content Exception
		// }
		// onDutyDao.delete(duty);
		// }
	
	// public List<PlanDto> findAvailablePlan(final PlanQueryDto queryDto){
	// List<PlanInfo> availableDutyEntries=onPlanDao.findAll(new
	// Specification<PlanInfo>(){
	// @Override
	// public Predicate toPredicate(Root<PlanInfo> root, CriteriaQuery<?> query,
	// CriteriaBuilder cb) {
	// Path<String> createuserid = root.get("createuserid");
	// Path<String> leaderid = root.get("leaderid");
	//
	// List<Predicate> predicateList = new ArrayList<Predicate>();
	// if(StringUtils.isNotEmpty(queryDto.getCreateuserid())){
	// predicateList.add(cb.equal(createuserid,queryDto.getCreateuserid()));
	// }
	// if (StringUtils.isNotEmpty(queryDto.getLeaderid())){
	// predicateList.add(cb.equal(leaderid,queryDto.getLeaderid()));
	// }
	// Predicate[] predicates = new Predicate[predicateList.size()];
	// predicateList.toArray(predicates);
	// query.where(predicates);
	// return null;
	// }
	// });
	//
	// List<PlanDto> availableDuties = new ArrayList<PlanDto>();
	// for(PlanInfo entry : availableDutyEntries){
	// PlanDto duty = new PlanDto();
	// mapper.map(entry,duty);
	// availableDuties.add(duty);
	// }
	// return availableDuties;
	// }

	// public PlanDto saveOrUpdate(PlanDto duty){
	// if(duty==null){
	// throw new NoContentException(310); //No Content Exception
	// }
	// PlanInfo entity=new PlanInfo();
	// mapper.map(duty,entity);
	// entity.setUpdateTime(new Date());//更新时间
	// entity=onDutyDao.save(entity);
	// if(entity==null){
	// throw new SocialSystemException(311);//Save Exception
	// }
	// PlanDto newDuty=new PlanDto();
	// mapper.map(entity,newDuty);
	// return newDuty;
	// }
	//
	// /**
	// * 删除值班信息
	// * @param id 值班ID
	// */
	// public void delete(String id){
	// if(StringUtils.isEmpty(id)){
	// throw new IllegalArgumentException("bad request parameter");
	// }
	// PlanInfo duty=onDutyDao.findOne(id);
	// if(duty==null){
	// throw new NoContentException(310); //No Content Exception
	// }
	// onDutyDao.delete(duty);
	// }
	//
	// /**
	// * 查看值班详情
	// * @param id 值班信息ID
	// * @return DutyDto
	// */
	// public PlanDto find(String id){
	// if(StringUtils.isEmpty(id)){
	// throw new IllegalArgumentException("bad request parameter");
	// }
	// PlanInfo duty=onDutyDao.findOne(id);
	// if(duty==null){
	// throw new NoContentException(310); //No Content Exception
	// }
	// PlanDto dutyDto=new PlanDto();
	// mapper.map(duty,dutyDto);
	// return dutyDto;
	// }
	//
	// @Transactional
	// public DutyStatus addDuty(DutyDto dutyDto) throws ParseException{
	// DutyStatus status = new DutyStatus();
	// if(dutyDto==null){
	// status.setStauts("False");
	// status.setLog("the object is null");
	// return status;
	// }
	//
	// TDuty onDuty = new TDuty();
	// long dutyId = 0;
	// //if the first add duty, will set a new id. else use the duty form's id.
	// if(dutyDto.getDutyid() == null ||dutyDto.getDutyid()==0){
	// if(onDutyDao.countDuty()==0){
	// dutyId = 1;
	// }else{
	// dutyId = onDutyDao.findmaxid() +1;
	// }
	// dutyDto.setDutyid(dutyId);
	// }
	// // has records on the dutyuser
	// //TODO 值班信息必须要有
	// if(dutyDto.getUser().size()==0){
	// status.setStauts("False");
	// status.setLog("the user list is null");
	// return status;
	// }
	//
	// List<UserDto> userlist = new ArrayList<UserDto>();
	// for(UserDto tempUser : dutyDto.getUser()){
	//// String startDate = sdf.format( tempUser.getStartdate());
	//// String endDate = sdf.format( tempUser.getEnddate());
	// TDutyUser user = new TDutyUser();
	// List<TDutyUser> tempList = onDutyUserDao.finduser(user.getUserid(),
	// tempUser.getStartdate(), tempUser.getEnddate());
	// if(tempList.size()==0){
	// userlist.add(tempUser);
	// }
	// }
	//
	//
	// if(userlist.size()==0){
	// status.setStauts("False");
	// status.setLog("user had added before");
	// return status;
	// }
	// dutyDto.setUser(userlist);
	// for(int i=0;i<dutyDto.getUser().size();i++){
	// dutyDto.getUser().get(i).setDutyid(dutyDto.getDutyid());
	// }
	//
	// //check if there are old duty or not, if has will delete the record first
	// and add it later.
	// if(delDuty(dutyDto.getDutyid())){
	// System.out.println("the conference records which id is " +
	// dutyDto.getDutyid() + " are deleted");
	// };
	//
	// onDuty.setId(dutyDto.getDutyid());
	// onDuty.setCompanyid(dutyDto.getCompany());
	// onDuty.setCreateuserid(dutyDto.getCreateuserid());
	// onDuty.setCreateusername(dutyDto.getCreateusername());
	// onDuty.setResponsibledepartment(dutyDto.getDepartment());
	// onDuty.setDescription(onDuty.getDescription());
	// Date now = new Date();
	// onDuty.setUpdatetime(now);
	// List<TDutyUser> userList = new ArrayList<TDutyUser>();
	// for(UserDto tempUser : dutyDto.getUser()){
	// TDutyUser user = new TDutyUser();
	//
	// user.setUserid(tempUser.getUserid());
	// user.setDutyid(tempUser.getDutyid());
	// user.setUsername(tempUser.getUsername());
	// user.setStartdate(sdf.parse(tempUser.getStartdate()));
	// user.setEnddate(sdf.parse(tempUser.getEnddate()));
	// user.setDescription(tempUser.getUserdescription());
	//
	// userList.add(user);
	// }
	// onDuty.setUser(userList);
	// onDutyDao.save(onDuty);
	// status.setStauts("True");
	// return status;
	// }
	//
	//
	// public Boolean delDuty(Long dutyid){
	// TDuty dutyItem = onDutyDao.findOne(dutyid);
	// if(dutyItem==null){
	// return false;
	// }
	// onDutyDao.delete(dutyid);
	// return true;
	// }
	//
	// //list all duty
	// public List<OnDutyDto> listDutyAll(){
	// List<OnDutyDto> dutyDtoList = new ArrayList<OnDutyDto>();
	//
	// List<TDuty> dutyList = new ArrayList<TDuty>();
	// dutyList = (List<TDuty>) onDutyDao.findAll();
	// for(TDuty tempDuty : dutyList){
	// OnDutyDto onDutyDto = new OnDutyDto();
	// mapper.map(tempDuty,onDutyDto);
	// dutyDtoList.add(onDutyDto);
	// }
	// return dutyDtoList;
	// }
	//
	//
	// public DutyStatus importDutyInfoToDB(){
	// DutyStatus status = new DutyStatus();
	//
	// List<TDutyImport> tempDutyList = new ArrayList<TDutyImport>();
	// // read temp table
	// tempDutyList = (List<TDutyImport>) onDutyImportDao.findAll();
	//
	// List<DutyImportDto> userlist = new ArrayList<DutyImportDto>();
	//
	// List<DutyImportDto> unImportList = new ArrayList<DutyImportDto>();
	//
	// //check if there are reduplicate items on DB
	// for(TDutyImport tempDuty : tempDutyList){
	// DutyImportDto duty = new DutyImportDto();
	// mapper.map(tempDuty, duty);
	// List<TDutyUser> dutyUserList = new ArrayList<TDutyUser>();
	// dutyUserList = onDutyUserDao.finduser(tempDuty.getUserid(),
	// tempDuty.getStartdate(), tempDuty.getEnddate());
	// if(dutyUserList.size()==0){
	// userlist.add(duty);
	// }else{
	// unImportList.add(duty);
	// }
	// }
	// if(userlist.size()==0){
	// status.setStauts("False");
	// status.setLog("import items were all imported to DB!");
	// if(unImportList.size()>0){
	// status.setUnImportList(unImportList);
	// }
	// onDutyImportDao.deleteAll();
	// return status;
	// }
	//
	// // add items to DB
	// long dutyId = 0;
	// for(DutyImportDto dutyItem : userlist){
	// try {
	// if(onDutyDao.countDuty()==0){
	// dutyId = 1;
	// }else{
	// dutyId = onDutyDao.findmaxid() +1;
	// }
	// Date startDate = sdf.parse(dutyItem.getStartdate());
	// Date endDate = sdf.parse(dutyItem.getEnddate());
	// TDutyUser tUser = new TDutyUser();
	// TDuty tDuty = new TDuty();
	// List<TDutyUser> userList = new ArrayList<TDutyUser>();
	// String companyId =
	// (dutyItem.getCompanyid()==null||"".equals(dutyItem.getCompanyid().trim()))?"":dutyItem.getCompanyid().trim();
	// String description =
	// (dutyItem.getDescription()==null||"".equals(dutyItem.getDescription().trim()))?"":dutyItem.getDescription().trim();
	// String responsibleDepartment =
	// (dutyItem.getResponsibledepartment()==null||"".equals(dutyItem.getResponsibledepartment().trim()))?"":dutyItem.getResponsibledepartment().trim();
	// String userName =
	// (dutyItem.getUsername()==null||"".equals(dutyItem.getUsername().trim()))?"":dutyItem.getUsername().trim();
	// String createusername =
	// (dutyItem.getCreateusername()==null||"".equals(dutyItem.getCreateusername().trim()))?"":dutyItem.getCreateusername().trim();
	// Date now = new Date();
	// tDuty.setCompanyid(companyId);
	// tDuty.setDescription(description);
	// tDuty.setResponsibledepartment(responsibleDepartment);
	// tDuty.setCreateusername(createusername);
	// tDuty.setCreateuserid(dutyItem.getCreateuserid());
	// tDuty.setId(dutyId);
	// tDuty.setUpdatetime(now);
	// tUser.setDutyid(dutyId);
	// tUser.setUserid(dutyItem.getUserid());
	// tUser.setUsername(userName);
	// tUser.setDescription(description);
	// tUser.setStartdate(startDate);
	// tUser.setEnddate(endDate);
	// userList.add(tUser);
	// tDuty.setUser( userList);
	// // add record to duty table
	// onDutyDao.save(tDuty);
	// // remove the item of temp table
	//// onDutyImportDao.deleteItem(dutyItem.getUserid(),dutyItem.getStartdate(),dutyItem.getEnddate());
	//
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	// }
	// status.setStauts("Ture");
	// status.setUnImportList(unImportList);
	// onDutyImportDao.deleteAll();
	// return status;
	// }
	//
	// public DutyStatus readFile(MultipartFile file) throws Exception {
	//
	// DutyStatus status = new DutyStatus();
	//
	// List<DutyImportDto> dutyList = new ArrayList<DutyImportDto>();
	//
	// //check if the file is exist
	// if(file.isEmpty()||file ==null){
	// status.setStauts("False");
	// status.setLog("the file is null");
	// return status;
	// }
	//
	// //check if the file is xls or xlsx
	// String name = file.getOriginalFilename();
	// int beginIndex = name.indexOf(".");
	// int endIndex = name.length();
	// String suffix = name.substring(beginIndex,endIndex);
	// if(!".xls".equals(suffix)&&!".xlsx".equals(suffix)){
	// status.setStauts("False");
	// status.setLog("the file is not excel");
	// return status;
	// }
	// //read xls or xlsx
	// dutyList = readExcel(file,suffix);
	//
	// // update the status
	// status.setStauts("True");
	// status.setImportList(dutyList);
	//
	// return status;
	// }
	//
	// public DutyStatus importFile(List<DutyImportDto> importList){
	// //save the return list to temp table
	// DutyStatus status = new DutyStatus();
	// try{
	// for (DutyImportDto dutyItem : importList){
	// TDutyImport item = new TDutyImport();
	// mapper.map(dutyItem, item);
	// onDutyImportDao.save(item);
	// }
	// // check the item if can save to DB, if save it or return the mismatch
	// list
	// status = importDutyInfoToDB();
	// return status;
	// }catch (Exception e){
	// status.setStauts("False");
	// onDutyImportDao.deleteAll();
	// }
	// return status;
	// }
	//
	// public void importData(InputStream is,String filename){
	// try {
	// String suffix=StringUtils.substringAfterLast(filename,".");
	// if(StringUtils.isNoneBlank(filename) ||
	// filename.equalsIgnoreCase("xlsx")){
	// wb = new XSSFWorkbook(is);
	// }else{
	// wb = new HSSFWorkbook(is);
	// }
	// List<PlanModifyInfo> duties = readExcelContent(wb);
	// if(duties == null || CollectionUtils.isEmpty(duties)){
	// throw new NoContentException(310);
	// }
	// for(PlanModifyInfo duty : duties){
	// onDutyImportDao.save(duty);
	// }
	//
	// } catch (IOException e) {
	// throw new SocialSystemException(312,filename);
	// } catch (RuntimeException e){
	// throw new SocialSystemException(311);
	// }
	// }
	////
	// private boolean validateSheet(Workbook wb){
	// if(wb==null){
	// logger.error("xls or xlsx has no sheet");
	// return false;
	// }
	// Sheet sheet=wb.getSheetAt(0);
	// if(sheet == null){
	// logger.error("xls or xlsx has no sheet");
	// return false;
	// }
	//
	// int rowNum=sheet.getLastRowNum();
	// if(rowNum < 4){
	// logger.error("content sheet is empty");
	// return false;
	// }
	//
	// Cell companyField =sheet.getRow(1).getCell(7);
	// if(companyField == null){
	// logger.error("所属公司为必填字段，请选择所属公司后重新导入");
	// return false;
	// }
	//
	//
	// Cell batchnoField=sheet.getRow(1).getCell(5);
	// if(batchnoField == null ||
	// !batchnoField.getStringCellValue().contains("B-") ||
	// batchnoField.getStringCellValue().length()!=8 ){
	// logger.error("导入批次号为必填字段，并且格式为<B- + 6位数字>");
	// return false;
	// }
	// return true;
	// }
	//
	// private boolean validateRow(Row row){
	// if(StringUtils.isBlank(row.getCell(0).getStringCellValue()) ||
	// !StringUtils.isNumeric(row.getCell(0).getStringCellValue())){
	// logger.error("序号不能为空，且必须为数字");
	// return false;
	// }
	// if(StringUtils.isBlank(row.getCell(1).getStringCellValue())){
	// logger.error("员工号不能为空");
	// return false;
	// }
	// if(StringUtils.isBlank(row.getCell(3).getStringCellValue()) ||
	// !StringUtils.isNumeric(row.getCell(3).getStringCellValue())){
	// logger.error("部门编号不能为空，且必须为数字");
	// return false;
	// }
	// if(StringUtils.isBlank(row.getCell(5).getStringCellValue()) ||
	// !isDate(row.getCell(5).getStringCellValue())){
	// logger.error("开始日期不能为空,且格式为YYYY-MM-DD");
	// return false;
	// }
	// if(StringUtils.isBlank(row.getCell(6).getStringCellValue()) ||
	// !isDate(row.getCell(6).getStringCellValue())){
	// logger.error("结束日期不能为空,且格式为YYYY-MM-DD");
	// return false;
	// }
	//
	// return true;
	// }
	// private boolean isDate(String date)
	// {
	// /**
	// * 判断日期格式和范围
	// */
	// String rexp =
	// "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))"
	// +
	// "[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?("
	// +
	// "(0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}("+
	// "([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))"
	// +
	// "[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?("
	// +
	// "(0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
	//
	// Pattern pat = Pattern.compile(rexp);
	//
	// Matcher mat = pat.matcher(date);
	//
	// boolean dateType = mat.matches();
	//
	// return dateType;
	// }
	// public List<PlanModifyInfo> readExcelContent(Workbook wb){
	// if(!validateSheet(wb)){
	// return null;
	// }
	// Sheet sheet=wb.getSheetAt(0);
	// int rowNum=sheet.getLastRowNum();
	// Cell companyField =sheet.getRow(1).getCell(7);
	// Cell batchnoField=sheet.getRow(1).getCell(5);
	//
	// String companyFieldValue=companyField.getStringCellValue();
	// String companyCode=StringUtils.substringBefore(companyFieldValue,"-");
	// //公司编号
	// String companyName=StringUtils.substringAfter(companyFieldValue,"-");
	// //公司名称
	// String batchno=batchnoField.getStringCellValue(); //批次号
	//
	// List<PlanModifyInfo> list=new ArrayList<PlanModifyInfo>();
	// for (int i = 4; i <= rowNum; i++) {
	// Row row=sheet.getRow(i);
	// if(!validateRow(row)){ //校验失败
	// continue;
	// }
	// PlanModifyInfo dutyImport=new PlanModifyInfo();
	// dutyImport.setBatchno(batchno);
	// dutyImport.setCompanycode(companyCode);
	// dutyImport.setCompanyName(companyName);
	// dutyImport.setSeqno(Integer.valueOf(row.getCell(0).getStringCellValue()));//序号
	// dutyImport.setImporttime(new Date());
	//
	// dutyImport.setDuty(new PlanInfo());
	// dutyImport.getDuty().setCompanyCode(companyCode);
	// dutyImport.getDuty().setCompanyName(companyName);
	// dutyImport.getDuty().setCaretaker(row.getCell(1).getStringCellValue());
	// dutyImport.getDuty().setCaretakerName(row.getCell(2).getStringCellValue());
	// dutyImport.getDuty().setDepartmentCode(row.getCell(3).getStringCellValue());
	// dutyImport.getDuty().setDepartmentName(row.getCell(4).getStringCellValue());
	// dutyImport.getDuty().setUpdateTime(new Date());
	// try{
	// dutyImport.getDuty().setStartDate(DateUtils.parseDate(
	// row.getCell(5).getStringCellValue(),"yyyy-MM-dd"));
	// dutyImport.getDuty().setEndDate(DateUtils.parseDate(
	// row.getCell(6).getStringCellValue(),"yyyy-MM-dd"));
	// }
	// catch (ParseException exception){
	// logger.error(exception.getMessage());
	// }
	// dutyImport.getDuty().setStatus(2); //草稿
	// list.add(dutyImport);
	// }
	// return list;
	// }
	//
	// public List<OnDutyDto> findByDuty(DutyDto duty ){
	// StringBuffer sql = new StringBuffer("select
	// id,createuserid,createusername,updatetime,responsibledepartment,companyid,description
	// from DUTY where 1=1 ");
	// StringBuffer query = new StringBuffer();
	// if(duty.getDutyid()!=null && (!"".equals(duty.getDutyid()))){
	// query.append(" and id=" + duty.getDutyid());
	// }
	// if(duty.getCreateuserid()!=null && (!"".equals(duty.getCreateuserid()))){
	// query.append(" and createuserid=" + duty.getCreateuserid());
	// }
	// if(duty.getCreateusername()!=null &&
	// (!"".equals(duty.getCreateusername().trim())) ){
	// query.append(" and createusername='" + duty.getCreateusername()+"'");
	// }
	// if(duty.getUpdatetime()!=null && (!"".equals(duty.getUpdatetime()))){
	// query.append(" and updatetime='" + duty.getUpdatetime()+"'");
	// }
	// if(duty.getDepartment()!=null &&
	// (!"".equals(duty.getDepartment().trim()))){
	// query.append(" and responsibledepartment='" + duty.getDepartment()+"'");
	// }
	// if(duty.getCompany()!=null && (!"".equals(duty.getCompany().trim()))){
	// query.append(" and companyid='" + duty.getCompany()+"'");
	// }
	// if(duty.getDutydescription()!=null &&
	// (!"".equals(duty.getDutydescription().trim()))){
	// query.append(" and description='" + duty.getDutydescription()+"'");
	// }
	// sql= sql.append(query);
	//
	//
	// List<Map<String,Object>> querylist ;
	//
	// querylist =jdbcTemplate.queryForList(sql.toString());
	// if(querylist==null || querylist.size()==0){
	// return null;
	// }
	//
	//
	// List<OnDutyDto> dutylist = new ArrayList<OnDutyDto>();
	// for(int i=0;i<querylist.size();i++){
	// OnDutyDto tempDuty = new OnDutyDto();
	// tempDuty.setId(Long.parseLong((String) querylist.get(i).get("id")));
	// tempDuty.setCreateuserid(Long.parseLong((String)
	// querylist.get(i).get("createuserid")));
	// tempDuty.setCreateusername((String)
	// querylist.get(i).get("createusername"));
	// tempDuty.setCompanyid((String) querylist.get(i).get("companyid"));
	// tempDuty.setResponsibledepartment((String)
	// querylist.get(i).get("responsibledepartment"));
	// tempDuty.setDescription((String) querylist.get(i).get("description"));
	// tempDuty.setUpdatetime(querylist.get(i).get("updatetime").toString());
	//
	// List<DutyUserDto> userList = new ArrayList<DutyUserDto>();
	//
	// List<TDutyUser> tempUserlist = new ArrayList<TDutyUser>();
	//
	// DutyUserDto user = new DutyUserDto();
	//
	// tempUserlist = onDutyUserDao.findByDutyId(tempDuty.getId());
	//
	// for(TDutyUser tempUser : tempUserlist){
	// mapper.map(tempUser, user);
	// userList.add(user);
	// }
	// tempDuty.setUser(userList);
	// dutylist.add(tempDuty);
	// }
	//
	// return dutylist;
	//
	// }
	//
	// public List<OnDutyDto> findByUser(UserDto user ){
	//
	// StringBuffer sql = new StringBuffer("select
	// userid,username,dutyid,id,description,startdate,enddate from DUTYPEOPLE
	// where 1=1 ");
	//
	// StringBuffer query = new StringBuffer();
	// if(user.getUserid()!=null && (!"".equals(user.getUserid()))){
	// query.append(" and userid=" + user.getUserid());
	// }
	// if(user.getUsername()!=null && (!"".equals(user.getUsername().trim()))){
	// query.append(" and username='" +user.getUsername() +"'");
	// }
	// if(user.getDutyid()!=null && (!"".equals(user.getDutyid())) ){
	// query.append(" and dutyid=" + user.getDutyid());
	// }
	// if(user.getUserdescription()!=null &&
	// (!"".equals(user.getUserdescription()))){
	// query.append(" and description='" + user.getUserdescription()+"'");
	// }
	// if(user.getStartdate()!=null &&
	// (!"".equals(user.getStartdate().trim()))){
	// query.append(" and startdate='" + user.getStartdate()+"'");
	// }
	// if(user.getEnddate()!=null && (!"".equals(user.getEnddate().trim()))){
	// query.append(" and enddate='" + user.getEnddate()+"'");
	// }
	// sql= sql.append(query);
	//
	// List<Map<String,Object>> querylist ;
	//
	// querylist =jdbcTemplate.queryForList(sql.toString());
	// if(querylist==null || querylist.size()==0){
	// return null;
	// }
	//
	// List<OnDutyDto> dutylist = new ArrayList<OnDutyDto>();
	// List<DutyUserDto> userlist = new ArrayList<DutyUserDto>();
	//
	// for(int i=0;i<querylist.size();i++){
	// DutyUserDto tempUser = new DutyUserDto();
	// tempUser.setUserid(Long.parseLong((String)querylist.get(i).get("userid")));
	// tempUser.setDutyid(Long.parseLong((String)querylist.get(i).get("dutyid")));
	// tempUser.setDescription((String)querylist.get(i).get("description"));
	// tempUser.setUsername((String)querylist.get(i).get("username"));
	// tempUser.setStartdate((Date) querylist.get(i).get("startdate"));
	// tempUser.setEnddate((Date) querylist.get(i).get("enddate"));
	// userlist.add(tempUser);
	// Long tempid = Long.parseLong((String)querylist.get(0).get("dutyid"));
	// TDuty tempDuty = new TDuty();
	// OnDutyDto dutyDto = new OnDutyDto();
	// if(tempid!=tempUser.getDutyid()){
	// tempDuty = onDutyDao.findByDutyId(tempid);
	// mapper.map(tempDuty, dutyDto);
	// dutyDto.setUser(userlist);
	// dutylist.add(dutyDto);
	// }else{
	// tempid =
	// Long.parseLong((String)querylist.get(querylist.size()-1).get("dutyid"));
	// tempDuty = onDutyDao.findByDutyId(tempid);
	// mapper.map(tempDuty, dutyDto);
	// dutyDto.setUser(userlist);
	// dutylist.add(dutyDto);
	// }
	// }
	// return dutylist;
	//
	// }
	// public List<PlanDto> findAvailableDuty(final DutyQueryDto queryDto){
	// List<PlanInfo> availableDutyEntries=onDutyDao.findAll(new
	// Specification<PlanInfo>(){
	// @Override
	// public Predicate toPredicate(Root<PlanInfo> root, CriteriaQuery<?> query,
	// CriteriaBuilder cb) {
	// Path<String> companyCodePath = root.get("companyCode");
	// Path<String> departmentCodePath = root.get("departmentCode");
	// Path<Date> ondutyStartDate = root.get("startDate");
	// Path<Date> ondutyEndDate = root.get("endDate");
	// Path<Integer> statusPath = root.get("status");
	//
	// List<Predicate> predicateList = new ArrayList<Predicate>();
	// if(StringUtils.isNotEmpty(queryDto.getCompanyCode())){
	// predicateList.add(cb.equal(companyCodePath,queryDto.getCompanyCode()));
	// }
	// if (StringUtils.isNotEmpty(queryDto.getDepartmentCode())){
	// predicateList.add(cb.equal(departmentCodePath,queryDto.getDepartmentCode()));
	// }
	// predicateList.add(cb.equal(statusPath,1)); //显示生效的值班
	//
	// if(queryDto.getStartDate()!=null){ //值班日期区间查询
	// predicateList.add(cb.greaterThanOrEqualTo(ondutyStartDate,queryDto.getStartDate()));
	// }
	// if(queryDto.getEndDate()!=null){
	// predicateList.add(cb.lessThanOrEqualTo(ondutyStartDate,queryDto.getStartDate()));
	// }
	// Predicate[] predicates = new Predicate[predicateList.size()];
	// predicateList.toArray(predicates);
	// query.where(predicates);
	// return null;
	// }
	// });
	//
	// List<PlanDto> availableDuties = new ArrayList<PlanDto>();
	// for(PlanInfo entry : availableDutyEntries){
	// PlanDto duty = new PlanDto();
	// mapper.map(entry,duty);
	// availableDuties.add(duty);
	// }
	// return availableDuties;
	// }
}