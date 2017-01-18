package com.cnnp.social.onPlan.manager.dto;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the HP_ADMIN database table.
 * 
 */
@Entity
@Table(name = "PLAN_DETAIL")
public class PlanDetailDto{
	@Id
	@GenericGenerator(name="idGenerator", strategy="uuid")
	@GeneratedValue(generator="idGenerator")
	
	private String id; // 计划项ID
	private String planid; // 计划ID
	private String projectname; // 计划项名称
	private String departmentid;// 部门
	private String companyid;//公司ID
	private Date parentid;//依赖任务项目ID
	private String CONTENT;//描述
	@Temporal(TemporalType.DATE)
	private Date createtime;//创建时间
	@Temporal(TemporalType.DATE)
	private Date startdate;//开始时间
	private String departmentname; // 部门名称
	private String companyname;// 公司名称
	private String leaderid;//责任领导ID
	private Date leadername;//责任领导姓名
	private String picid;//责任人ID
	private Date picname;//责任人姓名
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPlanid() {
		return planid;
	}
	public void setPlanid(String planid) {
		this.planid = planid;
	}
	public String getProjectname() {
		return projectname;
	}
	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}
	public String getDepartmentid() {
		return departmentid;
	}
	public void setDepartmentid(String departmentid) {
		this.departmentid = departmentid;
	}
	public String getCompanyid() {
		return companyid;
	}
	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}
	public Date getParentid() {
		return parentid;
	}
	public void setParentid(Date parentid) {
		this.parentid = parentid;
	}
	public String getCONTENT() {
		return CONTENT;
	}
	public void setCONTENT(String cONTENT) {
		CONTENT = cONTENT;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Date getStartdate() {
		return startdate;
	}
	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}
	public String getDepartmentname() {
		return departmentname;
	}
	public void setDepartmentname(String departmentname) {
		this.departmentname = departmentname;
	}
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public String getLeaderid() {
		return leaderid;
	}
	public void setLeaderid(String leaderid) {
		this.leaderid = leaderid;
	}
	public Date getLeadername() {
		return leadername;
	}
	public void setLeadername(Date leadername) {
		this.leadername = leadername;
	}
	public String getPicid() {
		return picid;
	}
	public void setPicid(String picid) {
		this.picid = picid;
	}
	public Date getPicname() {
		return picname;
	}
	public void setPicname(Date picname) {
		this.picname = picname;
	}
	
	

}
