package com.cnnp.social.onDuty.manager.dto;

import java.util.List;

public class DutyStatus {
	

	private String Stauts;
	
	private List<DutyImportDto> unImportList;
	
	private String log;
	
	private List<DutyImportDto> importList;
	
	
	public List<DutyImportDto> getImportList() {
		return importList;
	}

	public void setImportList(List<DutyImportDto> importList) {
		this.importList = importList;
	}

	public String getStauts() {
		return Stauts;
	}

	public void setStauts(String stauts) {
		Stauts = stauts;
	}

	public List<DutyImportDto> getUnImportList() {
		return unImportList;
	}

	public void setUnImportList(List<DutyImportDto> unImportList) {
		this.unImportList = unImportList;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}
	
	

}
