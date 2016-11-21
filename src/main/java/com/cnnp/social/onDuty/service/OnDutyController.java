package com.cnnp.social.onDuty.service;

import com.cnnp.social.onDuty.manager.OnDutyManager;
import com.cnnp.social.onDuty.manager.dto.DutyDto;
import com.cnnp.social.onDuty.manager.dto.DutyQueryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0")
public class OnDutyController {
	@Autowired
	private OnDutyManager onDutyManger;

	@RequestMapping(value = "/duties", method = RequestMethod.POST)
	public List<DutyDto> findByDuty(@RequestBody DutyQueryDto duty) {
		return onDutyManger.findAvailableDuty(duty);
	}

//	@RequestMapping(value = "/onduty/add", method = RequestMethod.POST)
//	public DutyStatus save(@RequestBody DutyDto onDutyDto) throws ParseException {
//		DutyStatus status = onDutyManger.addDuty(onDutyDto);
//		return status;
//	}
//
//	@RequestMapping(value = "/onduty/delete", method = RequestMethod.POST)
//	public Boolean del(@RequestParam Long dutyid) {
//		return onDutyManger.delDuty(dutyid);
//	}
//
//	@RequestMapping(value = "/onduty/findByUser", method = RequestMethod.POST)
//	public List<OnDutyDto> findByUser(@RequestBody UserDto user) {
//		return onDutyManger.findByUser(user);
//	}
//
//	@RequestMapping(value = "/onduty/findbyDuty", method = RequestMethod.POST)
//	public List<OnDutyDto> findByDuty(@RequestBody DutyDto duty) {
//		return onDutyManger.findByDuty(duty);
//	}
//
//	@RequestMapping(value = "/onduty/findall", method = RequestMethod.POST)
//	public List<OnDutyDto> listDutyAll() {
//		return onDutyManger.listDutyAll();
//	}
//
//	@RequestMapping(value = "/readfile", method = RequestMethod.POST)
//	public DutyStatus readFile(@RequestParam(value="uploadFile")MultipartFile file) throws Exception{
//		DutyStatus status = onDutyManger.readFile(file);
//		return status;
//	}
//
//	@RequestMapping(value = "/importfile", method = RequestMethod.POST)
//	public DutyStatus importFile(@RequestBody List<DutyImportDto> importList) throws IOException{
//		DutyStatus status = onDutyManger.importFile(importList);
//		return status;
//	}
	      

}