package com.cnnp.social.onDuty.manager;

import com.cnnp.social.onDuty.manager.dto.DutyDto;
import com.cnnp.social.onDuty.manager.dto.DutyQueryDto;
import com.cnnp.social.onDuty.repository.dao.OnDutyDao;
import com.cnnp.social.onDuty.repository.dao.OnDutyImportDao;
import com.cnnp.social.onDuty.repository.dao.OnDutyUserDao;
import com.cnnp.social.onDuty.repository.entity.TDuty;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.criteria.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



@EnableTransactionManagement
@Component

public class OnDutyManager {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private OnDutyDao  onDutyDao;
	
	@Autowired
	private OnDutyImportDao  onDutyImportDao;
	
	@Autowired
	private OnDutyUserDao  onDutyUserDao;
	private DozerBeanMapper mapper = new DozerBeanMapper();
	
	private Workbook wb;  
    private Sheet sheet;  
    private Row row;  
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


	/**
	 * 查询有效的值班信息
	 * @param queryDto DutyQueryDto
	 * @return List<DutyDto>
	 */
	public List<DutyDto> findAvailableDuty(final DutyQueryDto queryDto){
		List<TDuty>  availableDutyEntries=onDutyDao.findAll(new Specification<TDuty>(){
			@Override
			public Predicate toPredicate(Root<TDuty> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Path<String> companyCodePath = root.get("companyCode");
				Path<String> departmentCodePath = root.get("departmentCode");
				Path<Date> ondutyStartDate = root.get("startDate");
				Path<Date> ondutyEndDate = root.get("endDate");
				Path<Integer> statusPath = root.get("status");

				List<Predicate> predicateList = new ArrayList<Predicate>();
				if(StringUtils.isNotEmpty(queryDto.getCompanyCode())){
					predicateList.add(cb.equal(companyCodePath,queryDto.getCompanyCode()));
				}
				if (StringUtils.isNotEmpty(queryDto.getDepartmentCode())){
					predicateList.add(cb.equal(departmentCodePath,queryDto.getDepartmentCode()));
				}
				predicateList.add(cb.equal(statusPath,1)); //显示生效的值班

				if(queryDto.getStartDate()!=null){ //值班日期区间查询
					predicateList.add(cb.greaterThanOrEqualTo(ondutyStartDate,queryDto.getStartDate()));
				}
				if(queryDto.getEndDate()!=null){
					predicateList.add(cb.lessThanOrEqualTo(ondutyStartDate,queryDto.getStartDate()));
				}
				Predicate[] predicates = new Predicate[predicateList.size()];
				predicateList.toArray(predicates);
				query.where(predicates);
				return null;
			}
		});

		List<DutyDto> availableDuties = new ArrayList<DutyDto>();
		for(TDuty entry : availableDutyEntries){
			DutyDto duty = new DutyDto();
			mapper.map(entry,duty);
			availableDuties.add(duty);
		}
		return availableDuties;
	}

//
//	@Transactional
//	public DutyStatus addDuty(DutyDto dutyDto) throws ParseException{
//		DutyStatus status = new DutyStatus();
//		if(dutyDto==null){
//			status.setStauts("False");
//			status.setLog("the object is null");
//			return status;
//		}
//
//		TDuty onDuty = new TDuty();
//		long dutyId = 0;
//		//if the first add duty, will set a new id. else use the duty form's id.
//		if(dutyDto.getDutyid() == null ||dutyDto.getDutyid()==0){
//			if(onDutyDao.countDuty()==0){
//				dutyId = 1;
//			}else{
//				dutyId = onDutyDao.findmaxid() +1;
//			}
//			dutyDto.setDutyid(dutyId);
//		}
//		// has records on the dutyuser
//		//TODO 值班信息必须要有
//		if(dutyDto.getUser().size()==0){
//			status.setStauts("False");
//			status.setLog("the user list is null");
//			return status;
//		}
//
//		List<UserDto> userlist = new ArrayList<UserDto>();
//		for(UserDto tempUser : dutyDto.getUser()){
////			String startDate = sdf.format( tempUser.getStartdate());
////			String endDate = sdf.format( tempUser.getEnddate());
//			TDutyUser user = new TDutyUser();
//			List<TDutyUser> tempList = onDutyUserDao.finduser(user.getUserid(), tempUser.getStartdate(), tempUser.getEnddate());
//			if(tempList.size()==0){
//				userlist.add(tempUser);
//			}
//		}
//
//
//		if(userlist.size()==0){
//			status.setStauts("False");
//			status.setLog("user had added before");
//			return status;
//		}
//		dutyDto.setUser(userlist);
//		for(int i=0;i<dutyDto.getUser().size();i++){
//			dutyDto.getUser().get(i).setDutyid(dutyDto.getDutyid());
//		}
//
//		//check if there are old duty or not, if has will delete the record first and add it later.
//		if(delDuty(dutyDto.getDutyid())){
//			System.out.println("the conference records which id is  " + dutyDto.getDutyid() + " are deleted");
//		};
//
//		onDuty.setId(dutyDto.getDutyid());
//		onDuty.setCompanyid(dutyDto.getCompany());
//		onDuty.setCreateuserid(dutyDto.getCreateuserid());
//		onDuty.setCreateusername(dutyDto.getCreateusername());
//		onDuty.setResponsibledepartment(dutyDto.getDepartment());
//		onDuty.setDescription(onDuty.getDescription());
//		Date now = new Date();
//		onDuty.setUpdatetime(now);
//		List<TDutyUser> userList = new ArrayList<TDutyUser>();
//		for(UserDto tempUser : dutyDto.getUser()){
//			TDutyUser user = new TDutyUser();
//
//			user.setUserid(tempUser.getUserid());
//			user.setDutyid(tempUser.getDutyid());
//			user.setUsername(tempUser.getUsername());
//			user.setStartdate(sdf.parse(tempUser.getStartdate()));
//			user.setEnddate(sdf.parse(tempUser.getEnddate()));
//			user.setDescription(tempUser.getUserdescription());
//
//			userList.add(user);
//		}
//		onDuty.setUser(userList);
//		onDutyDao.save(onDuty);
//		status.setStauts("True");
//		return status;
//	}
//
//
//	public Boolean delDuty(Long dutyid){
//		TDuty dutyItem = onDutyDao.findOne(dutyid);
//		if(dutyItem==null){
//			return false;
//		}
//		onDutyDao.delete(dutyid);
//		return true;
//	}
//
//	//list all duty
//	public List<OnDutyDto> listDutyAll(){
//		List<OnDutyDto> dutyDtoList = new ArrayList<OnDutyDto>();
//
//		List<TDuty> dutyList = new ArrayList<TDuty>();
//		dutyList = (List<TDuty>) onDutyDao.findAll();
//		for(TDuty tempDuty : dutyList){
//			OnDutyDto onDutyDto = new OnDutyDto();
//			mapper.map(tempDuty,onDutyDto);
//			dutyDtoList.add(onDutyDto);
//		}
//		return dutyDtoList;
//	}
//
//
//	public DutyStatus importDutyInfoToDB(){
//		DutyStatus status = new DutyStatus();
//
//		List<TDutyImport> tempDutyList = new ArrayList<TDutyImport>();
//		// read temp table
//		tempDutyList = (List<TDutyImport>) onDutyImportDao.findAll();
//
//		List<DutyImportDto> userlist = new ArrayList<DutyImportDto>();
//
//		List<DutyImportDto> unImportList = new ArrayList<DutyImportDto>();
//
//		//check if there are reduplicate items on DB
//		for(TDutyImport tempDuty : tempDutyList){
//			DutyImportDto duty = new DutyImportDto();
//			mapper.map(tempDuty, duty);
//			List<TDutyUser> dutyUserList = new ArrayList<TDutyUser>();
//			dutyUserList = onDutyUserDao.finduser(tempDuty.getUserid(), tempDuty.getStartdate(), tempDuty.getEnddate());
//			if(dutyUserList.size()==0){
//				userlist.add(duty);
//			}else{
//				unImportList.add(duty);
//			}
//		}
//		if(userlist.size()==0){
//			status.setStauts("False");
//			status.setLog("import items were all imported to DB!");
//			if(unImportList.size()>0){
//				status.setUnImportList(unImportList);
//			}
//			onDutyImportDao.deleteAll();
//			return status;
//		}
//
//		// add items to DB
//		long dutyId = 0;
//		for(DutyImportDto dutyItem : userlist){
//			try {
//				if(onDutyDao.countDuty()==0){
//					dutyId = 1;
//				}else{
//					dutyId = onDutyDao.findmaxid() +1;
//				}
//				Date startDate = sdf.parse(dutyItem.getStartdate());
//				Date endDate = sdf.parse(dutyItem.getEnddate());
//				TDutyUser tUser = new TDutyUser();
//				TDuty tDuty = new TDuty();
//				List<TDutyUser> userList = new ArrayList<TDutyUser>();
//				String companyId = (dutyItem.getCompanyid()==null||"".equals(dutyItem.getCompanyid().trim()))?"":dutyItem.getCompanyid().trim();
//				String description = (dutyItem.getDescription()==null||"".equals(dutyItem.getDescription().trim()))?"":dutyItem.getDescription().trim();
//				String responsibleDepartment = (dutyItem.getResponsibledepartment()==null||"".equals(dutyItem.getResponsibledepartment().trim()))?"":dutyItem.getResponsibledepartment().trim();
//				String userName = (dutyItem.getUsername()==null||"".equals(dutyItem.getUsername().trim()))?"":dutyItem.getUsername().trim();
//				String createusername = (dutyItem.getCreateusername()==null||"".equals(dutyItem.getCreateusername().trim()))?"":dutyItem.getCreateusername().trim();
//				Date now = new Date();
//				tDuty.setCompanyid(companyId);
//				tDuty.setDescription(description);
//				tDuty.setResponsibledepartment(responsibleDepartment);
//				tDuty.setCreateusername(createusername);
//				tDuty.setCreateuserid(dutyItem.getCreateuserid());
//				tDuty.setId(dutyId);
//				tDuty.setUpdatetime(now);
//				tUser.setDutyid(dutyId);
//				tUser.setUserid(dutyItem.getUserid());
//				tUser.setUsername(userName);
//				tUser.setDescription(description);
//				tUser.setStartdate(startDate);
//				tUser.setEnddate(endDate);
//				userList.add(tUser);
//				tDuty.setUser( userList);
//				// add record to duty table
//				onDutyDao.save(tDuty);
//				// remove the item of temp table
////				onDutyImportDao.deleteItem(dutyItem.getUserid(),dutyItem.getStartdate(),dutyItem.getEnddate());
//
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//		}
//		status.setStauts("Ture");
//		status.setUnImportList(unImportList);
//		onDutyImportDao.deleteAll();
//		return status;
//	}
//
//	public DutyStatus readFile(MultipartFile file) throws Exception {
//
//		DutyStatus status = new DutyStatus();
//
//		List<DutyImportDto> dutyList = new ArrayList<DutyImportDto>();
//
//		//check if the file is exist
//		if(file.isEmpty()||file ==null){
//			status.setStauts("False");
//			status.setLog("the file is null");
//			return status;
//		}
//
//		//check if the file is xls or xlsx
//		String name  = file.getOriginalFilename();
//		int beginIndex = name.indexOf(".");
//		int endIndex = name.length();
//		String suffix = name.substring(beginIndex,endIndex);
//		if(!".xls".equals(suffix)&&!".xlsx".equals(suffix)){
//			status.setStauts("False");
//			status.setLog("the file is not excel");
//			return status;
//		}
//		//read xls or xlsx
//		dutyList = readExcel(file,suffix);
//
//		// update the status
//		status.setStauts("True");
//		status.setImportList(dutyList);
//
//		return status;
//	}
//
//	public DutyStatus importFile(List<DutyImportDto> importList){
//		//save the return list to temp table
//		DutyStatus status = new DutyStatus();
//		try{
//			for (DutyImportDto dutyItem : importList){
//				TDutyImport item = new TDutyImport();
//				mapper.map(dutyItem, item);
//				onDutyImportDao.save(item);
//			}
//			// check the item if can save to DB, if save it or return the mismatch list
//			status = importDutyInfoToDB();
//			return status;
//		}catch (Exception e){
//			status.setStauts("False");
//			onDutyImportDao.deleteAll();
//		}
//		return status;
//	}
//
//	public List<DutyImportDto> readExcel(MultipartFile file,String suffix) throws Exception{
//		List<DutyImportDto> contentList = new ArrayList<DutyImportDto>();
//		try {
//			InputStream is = file.getInputStream();
//			if(".xls".equals(suffix)){
//                wb = new HSSFWorkbook(is);
//            }else if(".xlsx".equals(suffix)){
//                wb = new XSSFWorkbook(is);
//            }
//			contentList = readExcelContent(wb);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return contentList;
//	}
//
//	public List<DutyImportDto> readExcelContent(Workbook wb) throws Exception{
//		List<DutyImportDto> contentList = new ArrayList<DutyImportDto>();
//		DutyImportDto contentDto = new DutyImportDto();
//        sheet = wb.getSheetAt(0);
//        // get all rows
//        int rowNum = sheet.getLastRowNum();
//        // get the content, title is the first row
//        for (int i = 1; i <= rowNum; i++) {
//            row = sheet.getRow(i);
//            contentDto.setUserid((long) row.getCell(0).getNumericCellValue());
//            contentDto.setUsername(row.getCell(1).getStringCellValue());
//            contentDto.setResponsibledepartment(row.getCell(2).toString());
//            contentDto.setCompanyid(row.getCell(3).toString());
//            contentDto.setStartdate(row.getCell(4).toString());
//            contentDto.setEnddate(row.getCell(5).toString());
//            contentDto.setDescription(row.getCell(6).getStringCellValue());
//            contentDto.setCreateusername(row.getCell(7).getStringCellValue());
//            contentDto.setCreateuserid((long) row.getCell(8).getNumericCellValue());
//            contentList.add(contentDto);
//        }
//        return contentList;
//    }
//
//	public List<OnDutyDto> findByDuty(DutyDto duty ){
//		StringBuffer sql = new StringBuffer("select id,createuserid,createusername,updatetime,responsibledepartment,companyid,description from DUTY where 1=1 ");
//		StringBuffer query = new StringBuffer();
//		if(duty.getDutyid()!=null && (!"".equals(duty.getDutyid()))){
//			query.append(" and id=" + duty.getDutyid());
//		}
//		if(duty.getCreateuserid()!=null && (!"".equals(duty.getCreateuserid()))){
//			query.append(" and createuserid=" + duty.getCreateuserid());
//		}
//		if(duty.getCreateusername()!=null && (!"".equals(duty.getCreateusername().trim())) ){
//			query.append(" and createusername='" + duty.getCreateusername()+"'");
//		}
//		if(duty.getUpdatetime()!=null && (!"".equals(duty.getUpdatetime()))){
//			query.append(" and updatetime='" + duty.getUpdatetime()+"'");
//		}
//		if(duty.getDepartment()!=null && (!"".equals(duty.getDepartment().trim()))){
//			query.append(" and responsibledepartment='" + duty.getDepartment()+"'");
//		}
//		if(duty.getCompany()!=null && (!"".equals(duty.getCompany().trim()))){
//			query.append(" and companyid='" + duty.getCompany()+"'");
//		}
//		if(duty.getDutydescription()!=null && (!"".equals(duty.getDutydescription().trim()))){
//			query.append(" and description='" + duty.getDutydescription()+"'");
//		}
//		sql= sql.append(query);
//
//
//		List<Map<String,Object>> querylist ;
//
//		querylist =jdbcTemplate.queryForList(sql.toString());
//		if(querylist==null || querylist.size()==0){
//			return null;
//		}
//
//
//		List<OnDutyDto> dutylist = new ArrayList<OnDutyDto>();
//		for(int i=0;i<querylist.size();i++){
//			OnDutyDto tempDuty = new OnDutyDto();
//			tempDuty.setId(Long.parseLong((String) querylist.get(i).get("id")));
//			tempDuty.setCreateuserid(Long.parseLong((String) querylist.get(i).get("createuserid")));
//			tempDuty.setCreateusername((String) querylist.get(i).get("createusername"));
//			tempDuty.setCompanyid((String) querylist.get(i).get("companyid"));
//			tempDuty.setResponsibledepartment((String) querylist.get(i).get("responsibledepartment"));
//			tempDuty.setDescription((String) querylist.get(i).get("description"));
//			tempDuty.setUpdatetime(querylist.get(i).get("updatetime").toString());
//
//			List<DutyUserDto> userList = new ArrayList<DutyUserDto>();
//
//			List<TDutyUser> tempUserlist = new ArrayList<TDutyUser>();
//
//			DutyUserDto user = new DutyUserDto();
//
//			tempUserlist = onDutyUserDao.findByDutyId(tempDuty.getId());
//
//			for(TDutyUser tempUser : tempUserlist){
//				mapper.map(tempUser, user);
//				userList.add(user);
//			}
//			tempDuty.setUser(userList);
//			dutylist.add(tempDuty);
//		}
//
//		return dutylist;
//
//	}
//
//	public List<OnDutyDto> findByUser(UserDto user ){
//
//		StringBuffer sql = new StringBuffer("select userid,username,dutyid,id,description,startdate,enddate from DUTYPEOPLE where 1=1 ");
//
//		StringBuffer query = new StringBuffer();
//		if(user.getUserid()!=null && (!"".equals(user.getUserid()))){
//			query.append(" and userid=" + user.getUserid());
//		}
//		if(user.getUsername()!=null && (!"".equals(user.getUsername().trim()))){
//			query.append(" and username='" +user.getUsername() +"'");
//		}
//		if(user.getDutyid()!=null && (!"".equals(user.getDutyid())) ){
//			query.append(" and dutyid=" + user.getDutyid());
//		}
//		if(user.getUserdescription()!=null && (!"".equals(user.getUserdescription()))){
//			query.append(" and description='" + user.getUserdescription()+"'");
//		}
//		if(user.getStartdate()!=null && (!"".equals(user.getStartdate().trim()))){
//			query.append(" and startdate='" + user.getStartdate()+"'");
//		}
//		if(user.getEnddate()!=null && (!"".equals(user.getEnddate().trim()))){
//			query.append(" and enddate='" + user.getEnddate()+"'");
//		}
//		sql= sql.append(query);
//
//		List<Map<String,Object>> querylist ;
//
//		querylist =jdbcTemplate.queryForList(sql.toString());
//		if(querylist==null || querylist.size()==0){
//			return null;
//		}
//
//		List<OnDutyDto> dutylist = new ArrayList<OnDutyDto>();
//		List<DutyUserDto> userlist = new ArrayList<DutyUserDto>();
//
//		for(int i=0;i<querylist.size();i++){
//			DutyUserDto tempUser = new DutyUserDto();
//			tempUser.setUserid(Long.parseLong((String)querylist.get(i).get("userid")));
//			tempUser.setDutyid(Long.parseLong((String)querylist.get(i).get("dutyid")));
//			tempUser.setDescription((String)querylist.get(i).get("description"));
//			tempUser.setUsername((String)querylist.get(i).get("username"));
//			tempUser.setStartdate((Date) querylist.get(i).get("startdate"));
//			tempUser.setEnddate((Date) querylist.get(i).get("enddate"));
//			userlist.add(tempUser);
//			Long tempid = Long.parseLong((String)querylist.get(0).get("dutyid"));
//			TDuty tempDuty = new TDuty();
//			OnDutyDto dutyDto = new OnDutyDto();
//			if(tempid!=tempUser.getDutyid()){
//				tempDuty = onDutyDao.findByDutyId(tempid);
//				mapper.map(tempDuty, dutyDto);
//				dutyDto.setUser(userlist);
//				dutylist.add(dutyDto);
//			}else{
//				tempid = Long.parseLong((String)querylist.get(querylist.size()-1).get("dutyid"));
//				tempDuty = onDutyDao.findByDutyId(tempid);
//				mapper.map(tempDuty, dutyDto);
//				dutyDto.setUser(userlist);
//				dutylist.add(dutyDto);
//			}
//		}
//		return dutylist;
//
//	}
}