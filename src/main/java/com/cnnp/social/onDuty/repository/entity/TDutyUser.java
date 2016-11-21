package com.cnnp.social.onDuty.repository.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.dozer.Mapping;

/**
 * The persistent class for the HP_ADMIN database table.
 * 
 */
@Entity
@Table(name = "DUTYPEOPLE")
public class TDutyUser{
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id; // ID 
	@Mapping(value="dutyid")
	private long dutyid; // 值班ID
	private long userid; // 值班者ID
	private String username; // 值班者姓名
	private String description;// 备注
	@Temporal(TemporalType.DATE)
	private Date startdate;// 开始时间	
	@Temporal(TemporalType.DATE)
	private Date enddate;// 结束时间
	
	
	public Date getStartdate() {
		return startdate;
	}
	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}
	public Date getEnddate() {
		return enddate;
	}
	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getDutyid() {
		return dutyid;
	}
	public void setDutyid(long dutyid) {
		this.dutyid = dutyid;
	}
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
