package com.cnnp.social.onDuty.repository.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "T_DUTY_IMP")
public class TDutyImport {
	@Id
	@GenericGenerator(name="idGenerator", strategy="uuid")
	@GeneratedValue(generator="idGenerator")
	private String id;
	private int seqno;

	private String batchno;
	private String companycode;
	private String companyName;
	private String log;
	private Date importtime;

	@OneToOne(cascade ={CascadeType.ALL})
	@JoinColumn(name="duty_id")
	private TDuty duty;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getSeqno() {
		return seqno;
	}

	public void setSeqno(int seqno) {
		this.seqno = seqno;
	}

	public String getBatchno() {
		return batchno;
	}

	public void setBatchno(String batchno) {
		this.batchno = batchno;
	}

	public String getCompanycode() {
		return companycode;
	}

	public void setCompanycode(String companycode) {
		this.companycode = companycode;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public Date getImporttime() {
		return importtime;
	}

	public void setImporttime(Date importtime) {
		this.importtime = importtime;
	}

	public TDuty getDuty() {
		return duty;
	}

	public void setDuty(TDuty duty) {
		this.duty = duty;
	}
}
