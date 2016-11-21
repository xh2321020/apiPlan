package com.cnnp.social.cache.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cnnp.social.cache.repository.dao.DBCacheDataDao;
import com.cnnp.social.cache.repository.entity.TDicData;

@RestController
@RequestMapping("/api/v1.0/cache")
public class CacheController {
	@Autowired
	private DBCacheDataDao dbCacheDataDao;
	
	@RequestMapping(value = "/find/type/{dictype}", method = RequestMethod.GET)
	public @ResponseBody List<TDicData> findByType(@PathVariable("dictype") String dictype) {
		return dbCacheDataDao.findByType(dictype.toUpperCase());
	}
	
	@RequestMapping(value = "/find/PID/{pid}", method = RequestMethod.GET)
	public @ResponseBody List<TDicData> findByPid(@PathVariable("pid") long pid) {
		return dbCacheDataDao.findByPID(pid);
	}
}
