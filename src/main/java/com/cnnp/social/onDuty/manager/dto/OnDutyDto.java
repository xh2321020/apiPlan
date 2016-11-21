package com.cnnp.social.onDuty.manager.dto;

import java.util.ArrayList;
import java.util.List;

public class OnDutyDto {
	
	private Long id; // 值班ID 
	private long createuserid; // 用户ID
	private String createusername; // 用户姓名
	private String responsibledepartment;// 部门
	private String companyid;// 公司	
	private String updatetime;//  创建or更新时间		
	private String description;// 备注
	
	private List<DutyUserDto> user = new ArrayList<DutyUserDto>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getCreateuserid() {
		return createuserid;
	}

	public void setCreateuserid(long createuserid) {
		this.createuserid = createuserid;
	}

	public String getCreateusername() {
		return createusername;
	}

	public void setCreateusername(String createusername) {
		this.createusername = createusername;
	}

	public String getResponsibledepartment() {
		return responsibledepartment;
	}

	public void setResponsibledepartment(String responsibledepartment) {
		this.responsibledepartment = responsibledepartment;
	}

	public String getCompanyid() {
		return companyid;
	}

	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<DutyUserDto> getUser() {
		return user;
	}

	public void setUser(List<DutyUserDto> user) {
		this.user = user;
	}
	
	
	
	
	
}
