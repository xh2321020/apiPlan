package com.cnnp.social.onDuty.service;

import com.cnnp.social.base.SocialSystemException;
import com.cnnp.social.onDuty.manager.OnDutyManager;
import com.cnnp.social.onDuty.manager.dto.DutyDto;
import com.cnnp.social.onDuty.manager.dto.DutyQueryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0")
public class OnDutyController {
	@Autowired
	private OnDutyManager onDutyManger;

	@RequestMapping(value = "/duties", method = RequestMethod.POST)
	public @ResponseBody List<DutyDto> findByDuty(@RequestBody DutyQueryDto duty) {
		return onDutyManger.findAvailableDuty(duty);
	}

	@RequestMapping(value = "/duty", method = RequestMethod.POST)
	public @ResponseBody DutyDto saveOrUpdate(@RequestBody DutyDto duty) {
		return onDutyManger.saveOrUpdate(duty);
	}

	@RequestMapping(value = "/duty/{id}", method = RequestMethod.POST)
	public void delete(@PathVariable String id) {
		onDutyManger.delete(id);
	}
	@RequestMapping(value = "/duty/{id}", method = RequestMethod.GET)
	public @ResponseBody DutyDto find(@PathVariable String id) {
		return onDutyManger.find(id);
	}

	@RequestMapping(value = "/import", method = RequestMethod.POST)
	public void importDate(MultipartFile file){
		InputStream ins=null;
		try {
			ins=file.getInputStream();
			onDutyManger.importData(ins,file.getOriginalFilename());
		} catch (IOException e) {
			throw new SocialSystemException(312,file.getOriginalFilename());
		}finally {
			if(ins!=null){
				try {
					ins.close();
				} catch (IOException e) {
					throw new SocialSystemException(312,file.getOriginalFilename());
				}
			}
		}

	}

}