package com.cnnp.social.onPlan.repository.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the HP_ADMIN database table.
 * 
 */
@Entity
@Table(name = "PLAN_DETAIL_SUB")
public class PlanDetailSub{
	@Id
	@GenericGenerator(name="idGenerator", strategy="uuid")
	@GeneratedValue(generator="idGenerator")
	
	private String id; // 计划项子项ID
	private String subprojectname; // 计划子项名称
	private String projectid; // 计划项ID
	private String departmentid;// 部门
	private String companyid;//公司ID
	private Date parentid;//依赖任务项目ID
	private String content;//描述
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
	public String getSubprojectname() {
		return subprojectname;
	}
	public void setSubprojectname(String subprojectname) {
		this.subprojectname = subprojectname;
	}
	public String getProjectid() {
		return projectid;
	}
	public void setProjectid(String projectid) {
		this.projectid = projectid;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
