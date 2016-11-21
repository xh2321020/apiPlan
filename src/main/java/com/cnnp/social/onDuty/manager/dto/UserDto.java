package com.cnnp.social.onDuty.manager.dto;

public class UserDto {
	
	private Long userid;
	
	private Long dutyid;
	
	private String username;
	
	private String startdate;
	
	private String enddate;
	
	private String userdescription;

	
	
	public Long getDutyid() {
		return dutyid;
	}

	public void setDutyid(Long dutyid) {
		this.dutyid = dutyid;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getUserdescription() {
		return userdescription;
	}

	public void setUserdescription(String userdescription) {
		this.userdescription = userdescription;
	}
	
	
	

}
