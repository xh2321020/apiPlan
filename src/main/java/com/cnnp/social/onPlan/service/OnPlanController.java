package com.cnnp.social.onPlan.service;

import com.cnnp.social.base.SocialSystemException;
import com.cnnp.social.onPlan.manager.OnPlanManager;
import com.cnnp.social.onPlan.manager.dto.PlanDetailDto;
import com.cnnp.social.onPlan.manager.dto.PlanDetailSubDto;
import com.cnnp.social.onPlan.manager.dto.PlanDto;
import com.cnnp.social.onPlan.manager.dto.PlanModifyDto;
import com.cnnp.social.onPlan.manager.dto.PlanQueryDto;
import com.cnnp.social.onPlan.repository.entity.PlanInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0")
public class OnPlanController {
	@Autowired
	private OnPlanManager onPlanManger;
	
//	@RequestMapping(value = "/Planlist", method = RequestMethod.POST)
//	public @ResponseBody List<PlanDto> Planlist(@RequestBody PlanQueryDto plan) {
//		return onPlanManger.findAvailablePlan(plan);
//	}
	//============================= 计划 =========================================
	
	@RequestMapping(value = "/Planlist/{id}", method = RequestMethod.GET)   //计划列表
	public @ResponseBody List<PlanDto> findplanlist(@PathVariable String id) {
		return onPlanManger.findplanlist(id);
	}
	@RequestMapping(value = "/Queryplan/{id}", method = RequestMethod.GET) //查询计划
	public @ResponseBody PlanDto findplan(@PathVariable String id) {
		return onPlanManger.findplan(id);
	}
	@RequestMapping(value = "/Addplan", method = RequestMethod.POST)  //新增计划
	public @ResponseBody PlanDto Addplan(@RequestBody PlanDto plan) {
		return onPlanManger.add(plan);
	}
	@RequestMapping(value = "/Editplan", method = RequestMethod.POST)  //修改计划
	public @ResponseBody PlanDto Editplan(@RequestBody PlanDto plan) {
		return onPlanManger.edit(plan);
	}
	@RequestMapping(value = "/Cancelplan", method = RequestMethod.POST)  //撤销计划
	public @ResponseBody PlanDto Cancelplan(@RequestBody PlanDto plan) {
		return onPlanManger.cancel(plan);
	}
	@RequestMapping(value = "/Freezingplan", method = RequestMethod.POST)  //冻结计划
	public @ResponseBody PlanDto Freezingplan(@RequestBody PlanDto plan) {
		return onPlanManger.freezing(plan);
	}
	@RequestMapping(value = "/Thawingplan", method = RequestMethod.POST)  //解冻计划
	public @ResponseBody PlanDto Thawingplan(@RequestBody PlanDto plan) {
		return onPlanManger.thawing(plan);
	}
	@RequestMapping(value = "/Startplan", method = RequestMethod.POST)  //开始计划
	public @ResponseBody PlanDto Startplan(@RequestBody PlanDto plan) {
		return onPlanManger.start(plan);
	}
	@RequestMapping(value = "/Finishlan", method = RequestMethod.POST)  //完成计划
	public @ResponseBody PlanDto Finishlan(@RequestBody PlanDto plan) {
		return onPlanManger.finish(plan);
	}
	//==========================计划任务项=========================================
	
	@RequestMapping(value = "/PlanDetaillist/{id}", method = RequestMethod.GET)   //计划项列表
	public @ResponseBody List<PlanDetailDto> findplandetaillist(@PathVariable String id) {
		return onPlanManger.findplandetaillist(id);
	}
	@RequestMapping(value = "/Queryplandetail/{id}", method = RequestMethod.GET) //查询计划项
	public @ResponseBody PlanDetailDto findplanDetal(@PathVariable String id) {
		return onPlanManger.findplandetail(id);
	}
	@RequestMapping(value = "/Addplandetail", method = RequestMethod.POST)  //添加计划任务项
	public @ResponseBody PlanDetailDto Addplandetail(@RequestBody PlanDetailDto plandetail) {
		return onPlanManger.addplandetail(plandetail);
	}
	@RequestMapping(value = "/Editplandetail", method = RequestMethod.POST)  //编辑计划任务项
	public @ResponseBody PlanDetailDto Editplandetail(@RequestBody PlanDetailDto plandetail) {
		return onPlanManger.editplandetail(plandetail);
	}
	@RequestMapping(value = "/Deleteplandetail/{id}", method = RequestMethod.GET)  //删除计划任务项------------------
	public String Deleteplandetail(@RequestParam String id) {
		return onPlanManger.delplandetail(id);
	}
    //========================计划子任务项========================================
	@RequestMapping(value = "/PlanDetaiSublist/{id}", method = RequestMethod.GET)   //子任务项列表
	public @ResponseBody List<PlanDetailSubDto> findplandetailsublist(@PathVariable String id) {
		return onPlanManger.findplandetailsublist(id);
	}
	@RequestMapping(value = "/Queryplandetailsub/{id}", method = RequestMethod.GET) //查询子任务项
	public @ResponseBody PlanDetailSubDto findplanDetalsub(@PathVariable String id) {
		return onPlanManger.findplandetailsub(id);
	}
	@RequestMapping(value = "/Addplandetailsub", method = RequestMethod.POST)  //添加计划任务项
	public @ResponseBody PlanDetailSubDto Addplandetailsub(@RequestBody PlanDetailSubDto plandetailsub) {
		return onPlanManger.addplandetailsub(plandetailsub);
	}
	@RequestMapping(value = "/Editplandetailsub", method = RequestMethod.POST)  //编辑计划任务项
	public @ResponseBody PlanDetailSubDto Editplandetailsub(@RequestBody PlanDetailSubDto plandetailsub) {
		return onPlanManger.editplandetailsub(plandetailsub);
	}
	@RequestMapping(value = "/Deleteplandetailsub", method = RequestMethod.GET)  //删除计划任务项
	public String Deleteplandetailsub(@RequestParam String id) {
		return onPlanManger.delplandetailsub(id);
	}
	//==================================修改日志============================================
	@RequestMapping(value = "/ModifyLoglist/{id}", method = RequestMethod.GET)   //日志列表
	public @ResponseBody List<PlanModifyDto> Modifyloglist(@PathVariable String id) {
		return onPlanManger.modifyloglist(id);
	}
	
	//==================================导出================================================Explorerplan
	@RequestMapping(value = "/Explorerplan/{id}", method = RequestMethod.GET)   //日志列表
	public String Explorerplan(@PathVariable String id) {
		return onPlanManger.explorerplan(id);
	}
}