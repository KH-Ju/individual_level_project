package com.hns.adf.crm.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmm.acl.service.AclMenuCacheService;
import com.hns.adf.crm.service.CarInfoService;
import com.hns.ast.inv.service.AssetInvService;
import com.hns.common.cmm.service.CommonFileService;
import com.hns.common.util.EgovExcelUtils;
import com.hns.common.util.StringUtil;
import com.hns.sym.common.service.HnsConst;
import com.hns.sym.common.service.HnsPagingUtil;
import com.hns.sym.program.service.ProgramService;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.rte.fdl.cmmn.exception.EgovBizException;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.fdl.security.userdetails.util.EgovUserDetailsHelper;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
 * 운행일지 관리 controller 클래스.
 * @author 조재훈
 * @since 2018.12.05
 * @version 1.0
 * @see
 */
@Controller
public class CarInfoController extends EgovFileMngUtil {
	/**
	 * 사용되는 서비스 선언
	 */
	private String downloadPath = EgovProperties.getProperty("FILE.Sample.Path");
	private Log logger = LogFactory.getLog(this.getClass());

    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;

	@Resource(name = "carInfoService")
	private CarInfoService carInfoService;

	@Resource(name = "assetInvService")
	private AssetInvService assetInvService;

	@Resource(name = "programService")
    private ProgramService programService;

	@Resource(name = "propertiesService")
	private EgovPropertyService egovPropertyService;

	@Resource(name = "commonFileService")
	CommonFileService fileService;



	@Resource(name = "propertiesService") protected EgovPropertyService pService;

	/**
	 * 운행일지관리 목록
	 * @param request
	 * @param model
	 * @return
	 */

	@RequestMapping("/hns/adf/crm/getCarDrivingInfoMntListInit.do")
	public String getCarDrivingInfoMntMng(HttpServletRequest request, Model model) throws Exception {
		System.out.println("getCarDrivingInfoMntListInit");
		LoginVO loginVO =  (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		model.addAttribute("LoginVO", loginVO);
		return "/adf/crm/CarDrivingInfoMntList";
	}


	/**
	 * 운행일지관리 등록 화면이동
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/hns/adf/crm/getCarDrivingInfoMntViewPop.do")
	public String getCarDrivingInfoViewPop(HttpServletRequest request, Model model) throws Exception {
		System.out.println("pop1");
		return "/adf/crm/CarDrivingInfoMntViewPop";
	}

	/**
	 * 본부 지점별 차량 현황 화면이동
	 * @param request
	 * @param model
	 * @return
	 */

	@RequestMapping("/hns/adf/crm/getCarDeptStatListInit.do")
	public String getCarDeptStatListInit(HttpServletRequest request, Model model) throws Exception {
		System.out.println("getCarDeptStatListInit");
		LoginVO loginVO =  (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> mngDeptList = null;
		List<Map<String,Object>> hqList = null;
		List<Map<String,Object>> deptList = null;
		List<Map<String,Object>> prodGroupList = null;

		mngDeptList = assetInvService.mngDeptList();
		hqList = assetInvService.hqList();
		deptList = assetInvService.deptList(null);
		prodGroupList = assetInvService.prodGroupList();

		map.put("CD_IDX", "CAR_GRP_CD");
		List<Map<String,Object>> resultList = programService.getCommonCodeIdxDtlList(map);

		model.addAttribute( "mngDeptList", mngDeptList);
		model.addAttribute( "hqList", hqList);
		model.addAttribute( "deptList", deptList);
		model.addAttribute( "prodGroupList", prodGroupList);
		model.addAttribute("LoginVO", loginVO);
		model.addAttribute("result" , resultList);
		return "/adf/crm/CarDeptStatList";
	}

	/**
	 * 개인별 차량 현황 화면이동
	 * @param request
	 * @param model
	 * @return
	 */

	@RequestMapping("/hns/adf/crm/getCarUserStatListInit.do")
	public String getCarUserStatListInit(HttpServletRequest request, Model model) throws Exception {
		System.out.println("getCarUserStatListInit");
		LoginVO loginVO =  (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> mngDeptList = null;
		List<Map<String,Object>> hqList = null;
		List<Map<String,Object>> deptList = null;
		List<Map<String,Object>> prodGroupList = null;

		mngDeptList = assetInvService.mngDeptList();
		hqList = assetInvService.hqList();
		deptList = assetInvService.deptList(null);
		prodGroupList = assetInvService.prodGroupList();

		model.addAttribute( "mngDeptList", mngDeptList);
		model.addAttribute( "hqList", hqList);
		model.addAttribute( "deptList", deptList);
		model.addAttribute( "prodGroupList", prodGroupList);
		model.addAttribute("LoginVO", loginVO);

		return "/adf/crm/CarUserStatList";
	}

	/**
	 * 운전자 차량번호 조회
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/hns/adf/crm/getCarUsrInfo.json", method=RequestMethod.POST)
	@ResponseBody
	public Model getCarUsrInfo(@RequestBody Map<String, Object> map, Model model) {
		try {
			List<Map<String,Object>> result = carInfoService.getCarUsrInfo(map);

			model.addAttribute(HnsConst.DATA, result);

		} catch(Exception e) {
			model.addAttribute("err_code","F");
		}

		return model;
	}


	/**
	 * 운행일지리스트 일별 데이터 조회
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/hns/adf/crm/getCarDrivingInfoMntList.json", method=RequestMethod.POST)
	@ResponseBody
	public Model getCarDrivingInfoMntList(@RequestBody Map<String, Object> map, Model model) {
		try {
			PaginationInfo paginationInfo = new PaginationInfo();
			int totalCnt = 0;
			HnsPagingUtil.setPagingInfo( map , egovPropertyService, paginationInfo);
			LoginVO loginVO =  (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
			map.put( HnsConst.LOGIN_ID, loginVO.getId());
			map.put( HnsConst.AUTHOR_CODE, loginVO.getAuthor());
			map.put( HnsConst.LOGIN_ORG, loginVO.getDeptcode());

			List<Map<String,Object>> resultList = carInfoService.getCarDrivingInfoMntList(map);
			System.out.println("운행일지 리스트==============" + resultList);

			if (resultList != null && resultList.size() > 0) {
				totalCnt = MapUtils.getInteger( ((Map<String, Object>) resultList.get(0)) , HnsConst.TOTAL_CNT);
			}
			paginationInfo.setTotalRecordCount(totalCnt);
			model.addAttribute( HnsConst.DATA, resultList);
			model.addAttribute( HnsConst.PAGINATION_INFO, paginationInfo);
			model.addAttribute( HnsConst.STATUS, HnsConst.OK);
		} catch(Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("err_code","F");
		}
		return model;
	}

	/**
	 * 운행일지리스트 월별 데이터 조회
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/hns/adf/crm/getCarDrivingInfoMntListMonthly.json", method=RequestMethod.POST)
	@ResponseBody
	public Model getCarDrivingInfoMntListMonthly(@RequestBody Map<String, Object> map, Model model) {
		try {
			PaginationInfo paginationInfo = new PaginationInfo();
			int totalCnt = 0;
			HnsPagingUtil.setPagingInfo( map , egovPropertyService, paginationInfo);
			LoginVO loginVO =  (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
			map.put( HnsConst.LOGIN_ID, loginVO.getId());
			map.put( HnsConst.AUTHOR_CODE, loginVO.getAuthor());
			List<Map<String,Object>> resultList = carInfoService.getCarDrivingInfoMntListMonthly(map);
			System.out.println("운행일지 월별리스트==============" + resultList);
			if (resultList != null && resultList.size() > 0) {
				totalCnt = MapUtils.getInteger( ((Map<String, Object>) resultList.get(0)) , HnsConst.TOTAL_CNT);
			}
			paginationInfo.setTotalRecordCount(totalCnt);
			model.addAttribute( HnsConst.DATA, resultList);
			model.addAttribute( HnsConst.PAGINATION_INFO, paginationInfo);
			model.addAttribute( HnsConst.STATUS, HnsConst.OK);
		} catch(Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("err_code","F");
		}
		return model;
	}


    /**
     * 운행일지관리 상세 정보를 조회한다.
     * @param map
     * @param model
     * @return
     * @throws Exception
     */
	@RequestMapping(value="/hns/adf/crm/getCarDrivingInfoMntDetail.json", method=RequestMethod.POST)
    @ResponseBody
    public Model getCarDrivingInfoMntDetail(@RequestBody Map<String,Object> map, Model model) throws Exception{
		Map<String,Object> result = carInfoService.getCarDrivingInfoMntDetail(map);
	   	System.out.println("상세정보==== " + result);
	   	model.addAttribute("result", result);
       	return model;
    }


    /**
     * 운행일지관리 마감 여부 조회한다.
     * @param map
     * @param model
     * @return
     * @throws Exception
     */
	@RequestMapping(value="/hns/adf/crm/getCarDrivingInfoMntDedlne.json", method=RequestMethod.POST)
    @ResponseBody
    public Model getCarDrivingInfoMntDedlne(@RequestBody Map<String,Object> map, Model model) throws Exception{
		Map<String,Object> result = carInfoService.getCarDrivingInfoMntDedlne(map);
	   	System.out.println("마감여부==== " + result);
	   	model.addAttribute("result", result);
       	return model;
    }

	/**
     * 차량 중복 조회
     * @param map
     * @param model
     * @return
     * @throws Exception
     */
	@RequestMapping(value="/hns/adf/crm/getCarDrivingInfo.json", method=RequestMethod.POST)
    @ResponseBody
    public Model getCarDrivingInfo(@RequestBody Map<String,Object> map, Model model) throws Exception{
		Map<String,Object> result = carInfoService.getCarDrivingInfo(map);
	   	model.addAttribute("result", result);
       	return model;
    }


	/**
     * 운행일지관리 주유량, 주행거리 조회한다.
     * @param map
     * @param model
     * @return
     * @throws Exception
     */
	@RequestMapping(value="/hns/adf/crm/getCarDrivingSum.json", method=RequestMethod.POST)
    @ResponseBody
    public Model getCarDrivingSum(@RequestBody Map<String,Object> map, Model model) throws Exception{

		if (map.get("CAR_LOG_DATE") == null || map.get("CAR_LOG_DATE").equals("")) {
			map.put("CAR_LOG_DATE", map.get("CAR_LOG_DATE_D")+"-01");
			map.put("CAR_NO", map.get("CAR_NO_D"));
		} else {
			map.put("CAR_LOG_DATE", map.get("CAR_LOG_DATE")+"-01");
		}

		Map<String,Object> preData = carInfoService.getCarDrivingPreSum(map);
		Map<String,Object> monData = carInfoService.getCarDrivingMonSum(map);
	   	System.out.println("전월최종주행거리==== " + preData);
	   	System.out.println("당월최종주행거리==== " + monData);
	   	model.addAttribute("preData", preData);
	   	model.addAttribute("monData", monData);
       	return model;
    }


    /**
     * 운행일지관리 주유량, 주행거리 조회한다.
     * @param map
     * @param model
     * @return
     * @throws Exception
     */
	@RequestMapping(value="/hns/adf/crm/getCarDrivingInfoMntSum.json", method=RequestMethod.POST)
    @ResponseBody
    public Model getCarDrivingInfoMntSum(@RequestBody Map<String,Object> map, Model model) throws Exception{
		map.put("CAR_LOG_DATE", map.get("CAR_LOG_DATE")+"-01");
		Map<String,Object> result = carInfoService.getCarDrivingInfoMntSum(map);
	   	System.out.println("주유량 및 주행거리==== " + result);
	   	model.addAttribute("result", result);
       	return model;
    }

	@RequestMapping(value="/hns/adf/crm/getCarDrivingInfoMntSumD.json", method=RequestMethod.POST)
    @ResponseBody
    public Model getCarDrivingInfoMntSumD(@RequestBody Map<String,Object> map, Model model) throws Exception{
		map.put("CAR_NO", map.get("CAR_NO_D"));
		map.put("CAR_LOG_DATE", map.get("CAR_LOG_DATE_D"));
		Map<String,Object> result = carInfoService.getCarDrivingInfoMntSum(map);
	   	System.out.println("주유량 및 주행거리==== " + result);
	   	model.addAttribute("result", result);
       	return model;
    }

    /**
     * 운행일지관리 정보를 수정한다.
     * @param map
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/hns/adf/crm/updateCarDrivingInfoMnt.json", method=RequestMethod.POST)
    @ResponseBody
    public Model updateCarDrivingInfoMnt(@RequestParam HashMap<String,Object> map, HttpServletRequest request, Model model) throws Exception{
    	LoginVO loginVO = EgovUserDetailsHelper.isAuthenticated()? (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser():null;
    	map.put("LOGIN_ID", loginVO.getId());
    	map.put("MOD_NM", loginVO.getName());
    	System.out.println("ID=== " + loginVO.getId());
		System.out.println("NM=== " + loginVO.getName());
		System.out.println("정보수정==============");
    	String result = null;
        try {
        	carInfoService.updateCarDrivingInfoMnt(request, map);
	    	result = egovMessageSource.getMessage("success.common.update");
	    } catch(Exception e) {
	    	result = egovMessageSource.getMessage("fail.common.update");
	    }
	   	model.addAttribute("result", result);
       	return model;
    }

    /**
     * 운행일지 마감처리한다.
     * @param map
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/hns/adf/crm/insertEndCarDrivingInfoMnt.json", method=RequestMethod.POST)
    @ResponseBody
    public Model insertEndCarDrivingInfoMnt(@RequestBody Map<String,Object> map, Model model) throws Exception{
    	LoginVO loginVO = EgovUserDetailsHelper.isAuthenticated()? (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser():null;
    	map.put("LOGIN_ID", loginVO.getId());
    	String result = null;
        try {
        	System.out.println("마감============== " + map);
        	carInfoService.insertEndCarDrivingInfoMnt(map);
        	result = egovMessageSource.getMessage("success.common.insert");
        } catch(Exception e) {
	    	result = egovMessageSource.getMessage("fail.common.insert");
        }
	   	model.addAttribute("result", result);
       	return model;
    }



    /**
     * 운행일지관리 정보를 등록한다.
     * @param map
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/hns/adf/crm/insertCarDrivingInfoMnt.json", method=RequestMethod.POST)
  	@ResponseBody
  	public Model insertCarDrivingInfoMnt(@RequestParam HashMap<String,Object> map, HttpServletRequest request, Model model) throws Exception {

    	System.out.println("HashMap<String,Object> map " +map);
    	System.out.println("HashMap<String,Object> map " +map);

  		String result = null;
  		try {
        	carInfoService.insertCarDrivingInfoMnt(request, map);
        	result = egovMessageSource.getMessage("success.common.insert");
  			model.addAttribute("status", HnsConst.OK);
  		} catch (EgovBizException e) {
  			model.addAttribute("status", HnsConst.FAIL);
  			result = egovMessageSource.getMessage("fail.common.insert");
  			e.printStackTrace();
  		} catch (Exception e) {
  			result = egovMessageSource.getMessage("fail.common.insert");
  			model.addAttribute("status", HnsConst.FAIL);
  			e.printStackTrace();
  		}
  		model.addAttribute("result", result);
  		return model;
  	}

    /*@RequestMapping(value="/hns/adf/crm/insertCarDrivingInfoMnt.json", method=RequestMethod.POST)
    @ResponseBody
    public Model insertCarDrivingInfoMnt(@RequestBody Map<String,Object> map, Model model) throws Exception{
    	LoginVO loginVO = EgovUserDetailsHelper.isAuthenticated()? (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser():null;
    	map.put("LOGIN_ID", loginVO.getId());
    	String result = null;
        try {
        	System.out.println("등록============== " + map);
        	carInfoService.insertCarDrivingInfoMnt(map);
        	result = egovMessageSource.getMessage("success.common.insert");
        } catch(Exception e) {
	    	result = egovMessageSource.getMessage("fail.common.insert");
        }
	   	model.addAttribute("result", result);
       	return model;
    }*/


    /**
     * 운행일지 정보를 삭제한다.
     * @param map
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/hns/adf/crm/deleteCarDrivingInfoMnt.json", method=RequestMethod.POST)
    @ResponseBody
    public Model deleteCarDrivingInfoMnt(@RequestBody Map<String,Object> map, Model model) throws Exception{
    	String result = null;
    	System.out.println("삭제============== " + map);
        try {
        	carInfoService.deleteCarDrivingInfoMnt(map);
	    	result = egovMessageSource.getMessage("success.common.delete");
        } catch(Exception e) {
	    	result = egovMessageSource.getMessage("fail.common.delete");
        }
	   	model.addAttribute("result", result);
       	return model;
    }

    /**
     * 운행일지조회 엑셀리스트 조회
     * @param request
     * @param response
     * @param req
     * @param session
     * @throws Exception
     */
    @RequestMapping(value = "/hns/adf/crm/getCarDrivingExcelList.json")
    public void getAppntExcelList(HttpServletRequest request, HttpServletResponse response , HttpServletRequest req, HttpSession session) throws Exception {

    	try {
			String checkdir = "";
			String saveFilePath = "";
			String path = request.getSession().getServletContext().getRealPath("/");

			checkdir = downloadPath;
			saveFilePath = checkdir+"/"+StringUtil.getTimeStamp()+StringUtil.getRandomNo(1);

			String title = "운행일지리스트";

			List<String> headerData = new ArrayList<String>();
			List<EgovMap> listData = new ArrayList<EgovMap>();
			HashMap map = new HashMap();
			EgovMap data = null;

			int tmpcnt = 0;
			headerData.add("NO@L@2000@data" 		+ tmpcnt++);
			headerData.add("운행일지ID@L@5000@data" 	+ tmpcnt++);
			headerData.add("운전자사번@L@5000@data" 	+ tmpcnt++);
			headerData.add("운전자이름@L@5000@data" 	+ tmpcnt++);
			headerData.add("부서명@L@5000@data" 	+ tmpcnt++);
			headerData.add("운행일자@L@5000@data" 	+ tmpcnt++);
			headerData.add("차량번호@L@5000@data" 	+ tmpcnt++);
			headerData.add("당월연비@L@5000@data" 	+ tmpcnt++);
			headerData.add("주행거리@L@5000@data" 	+ tmpcnt++);
			headerData.add("주유량@L@5000@data" 	+ tmpcnt++);
			headerData.add("주유카드번호@L@5000@data" 	+ tmpcnt++);
			headerData.add("하이패스카드번호@L@5000@data" 	+ tmpcnt++);
			headerData.add("유종@L@5000@data" 	+ tmpcnt++);
			headerData.add("비고@L@5000@data" 	+ tmpcnt++);
			headerData.add("최초등록일자@L@5000@data" 	+ tmpcnt++);
			headerData.add("최초등록사용자ID@L@5000@data" 	+ tmpcnt++);
			headerData.add("최종등록일자@L@5000@data" 	+ tmpcnt++);
			headerData.add("최종등록사용자ID@L@5000@data" 	+ tmpcnt++);

			LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
			map.put(HnsConst.LOGIN_ID			, loginVO.getId()); //LOGIN_ID
			map.put("CAR_LOG_ID"			, request.getParameter("CAR_LOG_ID"));
			map.put("DRV_EMP_NM"			, request.getParameter("DRV_EMP_NM"));
			map.put("ORG_NM"			, request.getParameter("ORG_NM"));
			map.put("CAR_LOG_DATE"			, request.getParameter("CAR_LOG_DATE"));
			map.put("CAR_NO"			, request.getParameter("CAR_NO"));
			map.put("MON_GAS_CST"			, request.getParameter("MON_GAS_CST"));
			map.put("CAR_RUN_DST"			, request.getParameter("CAR_RUN_DST"));
			map.put("GAS_VOL"			, request.getParameter("GAS_VOL"));
			map.put("GAS_CARD_NO"			, request.getParameter("GAS_CARD_NO"));
			map.put("HIPASS_CARD_NO"			, request.getParameter("HIPASS_CARD_NO"));
			map.put("CAR_GAS_CD_NM"			, request.getParameter("CAR_GAS_CD_NM"));
			map.put("NTE_CTT"			, request.getParameter("NTE_CTT"));
			map.put("FRST_REG_DATE"			, request.getParameter("FRST_REG_DATE"));
			map.put("FRST_REG_USER_ID"			, request.getParameter("FRST_REG_USER_ID"));
			map.put("LAST_REG_DATE"			, request.getParameter("LAST_REG_DATE"));
			map.put("LAST_REG_USER_ID"			, request.getParameter("LAST_REG_USER_ID"));
			map.put("CAR_LOG_STA_YMD"		, request.getParameter("CAR_LOG_STA_YMD"));
			map.put("CAR_LOG_END_YMD"		, request.getParameter("CAR_LOG_END_YMD"));
			map.put("SEARCH_KEY"		, request.getParameter("SEARCH_KEY"));
			map.put("SEARCH_VAL"			, request.getParameter("SEARCH_VAL"));

			List<EgovMap> resultList =  carInfoService.getCarDrivingExcelList(map);


			EgovMap tempData = null;
			for (int k = 0; k < resultList.size(); k++) {
				tempData = (EgovMap) resultList.get(k);
				data = new EgovMap();
				for (int m = 0; m < tempData.size(); m++) {
					String val = Objects.toString(tempData.getValue(m), Strings.EMPTY);
					data.put("data" + m, val);
				}
				listData.add(data);
			}

			String excelTitle = "운행일지리스트";

			EgovExcelUtils.makeExcelFile(saveFilePath, title, headerData, listData, checkdir);
			EgovExcelUtils.downloadFile(response, saveFilePath, excelTitle + ".xls");

		} catch (Exception e) {
			logger.error("e.getMessage() :" + e.getMessage());
		}
    }

    /**
     * 월별 운행일지 엑셀리스트 조회
     * @param request
     * @param response
     * @param req
     * @param session
     * @throws Exception
     */
    @RequestMapping(value = "/hns/adf/crm/getCarDrivingExcelListMonthly.json")
    public void getAppntExcelListMonthly(HttpServletRequest request, HttpServletResponse response , HttpServletRequest req, HttpSession session) throws Exception {

    	try {
			String checkdir = "";
			String saveFilePath = "";
			String path = request.getSession().getServletContext().getRealPath("/");
			checkdir = downloadPath;
			saveFilePath = checkdir+"/"+StringUtil.getTimeStamp()+StringUtil.getRandomNo(1);
			String title = "월별 운행일지";
			List<String> headerData = new ArrayList<String>();
			List<EgovMap> listData = new ArrayList<EgovMap>();
			HashMap map = new HashMap();
			EgovMap data = null;

			int tmpcnt = 0;
			headerData.add("NO@L@2000@data" + tmpcnt++);
			headerData.add("운행월@L@5000@data" 	+ tmpcnt++);
			headerData.add("차량번호@L@5000@data" 	+ tmpcnt++);
			headerData.add("운전자사번@L@5000@data" 	+ tmpcnt++);
			headerData.add("운전자이름@L@5000@data" 	+ tmpcnt++);
			headerData.add("부서명@L@5000@data" 	+ tmpcnt++);
			headerData.add("주행거리@L@5000@data" 	+ tmpcnt++);
			headerData.add("주유량@L@5000@data" 	+ tmpcnt++);
			headerData.add("주유카드번호@L@5000@data" 	+ tmpcnt++);
			headerData.add("하이패스카드번호@L@5000@data" 	+ tmpcnt++);
			headerData.add("유종@L@5000@data" 	+ tmpcnt++);
			headerData.add("차량상태@L@5000@data" 	+ tmpcnt++);

			LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
			map.put(HnsConst.LOGIN_ID			, loginVO.getId()); //LOGIN_ID
			map.put("CAR_LOG_DATE"			, request.getParameter("CAR_LOG_DATE"));
			map.put("CAR_NO"			, request.getParameter("CAR_NO"));
			map.put("DRV_EMP_NM"			, request.getParameter("DRV_EMP_NM"));
			map.put("ORG_NM"			, request.getParameter("ORG_NM"));
			map.put("CAR_RUN_DST"			, request.getParameter("CAR_RUN_DST"));
			map.put("GAS_VOL"			, request.getParameter("GAS_VOL"));
			map.put("GAS_CARD_NO"			, request.getParameter("GAS_CARD_NO"));
			map.put("HIPASS_CARD_NO"			, request.getParameter("HIPASS_CARD_NO"));
			map.put("CAR_GAS_CD_NM"			, request.getParameter("CAR_GAS_CD_NM"));
			map.put("CAR_STAT_CD_NM"			, request.getParameter("CAR_STAT_CD_NM"));
			map.put("CAR_LOG_MON_STA_YMD"		, request.getParameter("CAR_LOG_MON_STA_YMD"));
			map.put("CAR_LOG_MON_END_YMD"		, request.getParameter("CAR_LOG_MON_END_YMD"));
			map.put("SEARCH_KEY"		, request.getParameter("SEARCH_KEY"));
			map.put("SEARCH_VAL"			, request.getParameter("SEARCH_VAL"));
			List<EgovMap> resultList =  carInfoService.getCarDrivingExcelListMonthly(map);
			EgovMap tempData = null;
			for (int k = 0; k < resultList.size(); k++) {
				tempData = (EgovMap) resultList.get(k);
				data = new EgovMap();
				for (int m = 0; m < tempData.size(); m++) {
					String val = Objects.toString(tempData.getValue(m), Strings.EMPTY);
					data.put("data" + m, val);
				}
				listData.add(data);
			}
			String excelTitle = "월별 운행일지리스트";
			EgovExcelUtils.makeExcelFile(saveFilePath, title, headerData, listData, checkdir);
			EgovExcelUtils.downloadFile(response, saveFilePath, excelTitle + ".xls");

		} catch (Exception e) {
			logger.error("e.getMessage() :" + e.getMessage());
		}
    }
/*----------------------------------------------------------------------------------------------------------*/
/*----------------------------------------------------------------------------------------------------------*/
/*------------------------------------- 차량 범칙금 관리-----------------------------------------------------*/
/*----------------------------------------------------------------------------------------------------------*/
/*----------------------------------------------------------------------------------------------------------*/
    /**
	 * 과태료 고지서 등록관리 화면이동
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/hns/adf/crm/getCarFineInfoMntListInit.do")
	public String getCarFineInfoMntMng(HttpServletRequest request, Model model) throws Exception {
		System.out.println("getCarFineInfoMntListInit");
		return "/adf/crm/CarFineInfoMntList";
	}

	/**
	 * 과태료 납부확인 화면 이동
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/hns/adf/crm/getCarFineInfoListInit.do")
	public String getCarFineInfoMntList(HttpServletRequest request, Model model) throws Exception {
		System.out.println("CarFineInfoList");
		LoginVO loginVO =  (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		model.addAttribute("LoginVO", loginVO);
		return "/adf/crm/CarFineInfoList";
	}


	/**
	 * 범칙금관리 등록 화면이동
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/hns/adf/crm/getCarFineInfoMntViewPop.do")
	public String getCarFineInfoViewPop(HttpServletRequest request, Model model) throws Exception {
		System.out.println("pop1");
		return "/adf/crm/CarFineInfoMntViewPop";
	}




	/**
	 * 범칙금리스트 데이터 조회
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/hns/adf/crm/getCarFineInfoMntList.json", method=RequestMethod.POST)
	@ResponseBody
	public Model getCarFineInfoMntList(@RequestBody Map<String, Object> map, Model model) {
		try {
			PaginationInfo paginationInfo = new PaginationInfo();
			int totalCnt = 0;
			HnsPagingUtil.setPagingInfo( map , egovPropertyService, paginationInfo);
			LoginVO loginVO =  (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
			map.put( HnsConst.LOGIN_ID, loginVO.getId());
			map.put( HnsConst.AUTHOR_CODE, loginVO.getAuthor());
			map.put( HnsConst.LOGIN_ORG, loginVO.getDeptcode());
			List<Map<String,Object>> resultList = carInfoService.getCarFineInfoMntList(map);
			System.out.println("범칙금 리스트==============" + resultList);
			if (resultList != null && resultList.size() > 0) {
				totalCnt = MapUtils.getInteger( ((Map<String, Object>) resultList.get(0)) , HnsConst.TOTAL_CNT);
			}
			paginationInfo.setTotalRecordCount(totalCnt);
			model.addAttribute( HnsConst.DATA, resultList);
			model.addAttribute( HnsConst.PAGINATION_INFO, paginationInfo);
			model.addAttribute( HnsConst.STATUS, HnsConst.OK);

		} catch(Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("err_code","F");
		}
		return model;
	}


    /**
     * 범칙금관리 상세 정보를 조회한다.
     * @param map
     * @param model
     * @return
     * @throws Exception
     */
	@RequestMapping(value="/hns/adf/crm/getCarFineInfoMntDetail.json", method=RequestMethod.POST)
    @ResponseBody
    public Model getCarFineInfoMntDetail(@RequestBody Map<String,Object> map, Model model) throws Exception{
		Map<String,Object> result = carInfoService.getCarFineInfoMntDetail(map);
	   	System.out.println("상세정보==== " + result);
	   	model.addAttribute("result", result);
       	return model;
    }


    /**
     * 범칙금관리 정보를 수정한다.
     * @param map
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/hns/adf/crm/updateCarFineInfoMnt.json", method=RequestMethod.POST)
	@ResponseBody
	public Model updateCarFineInfoMnt(HttpServletRequest request, Model model) throws Exception {
		String result = null;
		try {
			carInfoService.updateCarFineInfoMnt(request);
			result = egovMessageSource.getMessage("success.common.update");
			model.addAttribute("status", HnsConst.OK);
		} catch (EgovBizException e) {
			model.addAttribute("status", HnsConst.FAIL);
			result = egovMessageSource.getMessage("fail.common.update");
			e.printStackTrace();
		} catch (Exception e) {
			result = egovMessageSource.getMessage("fail.common.update");
			model.addAttribute("status", HnsConst.FAIL);
			e.printStackTrace();
		}
		model.addAttribute("result", result);
		return model;
	}




    /**
     * 범칙금관리 정보를 등록한다.
     * @param map
     * @param model
     * @return
     * @throws Exception
     */

    @RequestMapping(value="/hns/adf/crm/insertCarFineInfoMnt.json", method=RequestMethod.POST)
	@ResponseBody
	public Model insertCarFineInfoMnt(HttpServletRequest request, Model model) throws Exception {

		String result = null;
		try {
			carInfoService.insertCarFineInfoMnt(request);
			result = egovMessageSource.getMessage("success.common.insert");
			model.addAttribute("status", HnsConst.OK);
		} catch (EgovBizException e) {
			model.addAttribute("status", HnsConst.FAIL);
			result = egovMessageSource.getMessage("fail.common.insert");
			e.printStackTrace();
		} catch (Exception e) {
			result = egovMessageSource.getMessage("fail.common.insert");
			model.addAttribute("status", HnsConst.FAIL);
			e.printStackTrace();
		}
		model.addAttribute("result", result);
		return model;
	}

    @RequestMapping(value="/hns/adf/crm/insertMultiFiles.json", method=RequestMethod.POST)
   	@ResponseBody
   	public Model insertMultiFiles(@RequestBody Map<String, Object> map, Model model) throws Exception {

   		String result = null;
   		try {
   			carInfoService.insertMultiFiles(map);
   			result = egovMessageSource.getMessage("success.common.insert");
   			model.addAttribute("status", HnsConst.OK);
   		} catch (EgovBizException e) {
   			model.addAttribute("status", HnsConst.FAIL);
   			result = egovMessageSource.getMessage("fail.common.insert");
   			e.printStackTrace();
   		} catch (Exception e) {
   			result = egovMessageSource.getMessage("fail.common.insert");
   			model.addAttribute("status", HnsConst.FAIL);
   			e.printStackTrace();
   		}
   		model.addAttribute("result", result);
   		return model;
   	}



    /**
     * 범칙금 정보를 삭제한다.
     * @param map
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/hns/adf/crm/deleteCarFineInfoMnt.json", method=RequestMethod.POST)
    @ResponseBody
    public Model deleteCarFineInfoMnt(@RequestBody Map<String,Object> map, Model model) throws Exception{
    	String result = null;
    	System.out.println("삭제============== " + map);
        try {
        	carInfoService.deleteCarFineInfoMnt(map);
	    	result = egovMessageSource.getMessage("success.common.delete");

        } catch(Exception e) {
	    	result = egovMessageSource.getMessage("fail.common.delete");
        }
	   	model.addAttribute("result", result);
       	return model;
    }



    /**
     * 범칙금조회 엑셀리스트 조회
     * @param request
     * @param response
     * @param req
     * @param session
     * @throws Exception
     */
    @RequestMapping(value = "/hns/adf/crm/getCarFineExcelList.json")
    public void getFineExcelList(HttpServletRequest request, HttpServletResponse response , HttpServletRequest req, HttpSession session) throws Exception {

    	try {
			String checkdir = "";
			String saveFilePath = "";
			String path = request.getSession().getServletContext().getRealPath("/");

			checkdir = downloadPath;
			saveFilePath = checkdir+"/"+StringUtil.getTimeStamp()+StringUtil.getRandomNo(1);

			String title = "범칙금리스트";

			List<String> headerData = new ArrayList<String>();
			List<EgovMap> listData = new ArrayList<EgovMap>();
			HashMap map = new HashMap();
			EgovMap data = null;

			int tmpcnt = 0;
			headerData.add("NO@L@2000@data" 		+ tmpcnt++);
			headerData.add("차량범칙금ID@L@5000@data" 	+ tmpcnt++);
			headerData.add("운전자사번@L@5000@data" 	+ tmpcnt++);
			headerData.add("운전자이름@L@5000@data" 	+ tmpcnt++);
			headerData.add("부서명@L@5000@data" 	+ tmpcnt++);
			headerData.add("위반일자@L@5000@data" 	+ tmpcnt++);
			headerData.add("위반시간@L@5000@data" 	+ tmpcnt++);
			headerData.add("차량번호@L@5000@data" 	+ tmpcnt++);
			headerData.add("담당자사번@L@5000@data" 	+ tmpcnt++);
			headerData.add("담당자이름@L@5000@data" 	+ tmpcnt++);
			headerData.add("담당자 연락처@L@5000@data" 	+ tmpcnt++);
			headerData.add("과태료 일련번호@L@5000@data" 	+ tmpcnt++);
			headerData.add("과태료 금액@L@5000@data" 	+ tmpcnt++);
			headerData.add("위반내용@L@5000@data" 	+ tmpcnt++);
			headerData.add("과태료납기기한@L@5000@data" 	+ tmpcnt++);
			headerData.add("메일발송일자@L@5000@data" 	+ tmpcnt++);
			headerData.add("과태료납부여부@L@5000@data" 	+ tmpcnt++);
			headerData.add("비고@L@5000@data" 	+ tmpcnt++);
			headerData.add("최초등록일자@L@5000@data" 	+ tmpcnt++);
			headerData.add("최초등록사용자ID@L@5000@data" 	+ tmpcnt++);
			headerData.add("최종등록일자@L@5000@data" 	+ tmpcnt++);
			headerData.add("최종등록사용자ID@L@5000@data" 	+ tmpcnt++);


			LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
			map.put( HnsConst.LOGIN_ID, loginVO.getId());
			map.put( HnsConst.AUTHOR_CODE, loginVO.getAuthor());
			map.put( HnsConst.LOGIN_ORG, loginVO.getDeptcode());
			map.put("CAR_FINE_ID"			, request.getParameter("CAR_FINE_ID"));
			map.put("DRV_EMP_NM"			, request.getParameter("DRV_EMP_NM"));
			map.put("ORG_NM"			, request.getParameter("ORG_NM"));
			map.put("VIOLT_DATE"			, request.getParameter("VIOLT_DATE"));
			map.put("VIOLT_TIME_VAL"			, request.getParameter("VIOLT_TIME_VAL"));
			map.put("CAR_NO"			, request.getParameter("CAR_NO"));
			map.put("CHRG_EMP_NM"			, request.getParameter("CHRG_EMP_NM"));
			map.put("CHRG_TEL_NO"			, request.getParameter("CHRG_TEL_NO"));
			map.put("FINE_SEQL_NO"			, request.getParameter("FINE_SEQL_NO"));
			map.put("FINE_AMT"			, request.getParameter("FINE_AMT"));
			map.put("VIOLT_CTT_GB_CD_NM"			, request.getParameter("VIOLT_CTT_GB_CD_NM"));
			map.put("FINE_DEDLNE_DATE"			, request.getParameter("FINE_DEDLNE_DATE"));
			map.put("EMAIL_SND_DATE"			, request.getParameter("EMAIL_SND_DATE"));
			map.put("FINE_PAY_YN"			, request.getParameter("FINE_PAY_YN"));
			map.put("NTE_CTT"			, request.getParameter("NTE_CTT"));
			map.put("FRST_REG_DATE"			, request.getParameter("FRST_REG_DATE"));
			map.put("FRST_REG_USER_ID"			, request.getParameter("FRST_REG_USER_ID"));
			map.put("LAST_REG_DATE"			, request.getParameter("LAST_REG_DATE"));
			map.put("LAST_REG_USER_ID"			, request.getParameter("LAST_REG_USER_ID"));
			map.put("VIOLT_STA_YMD"		, request.getParameter("VIOLT_STA_YMD"));
			map.put("VIOLT_END_YMD"		, request.getParameter("VIOLT_END_YMD"));
			map.put("SEARCH_KEY"		, request.getParameter("SEARCH_KEY"));
			map.put("SEARCH_VAL"			, request.getParameter("SEARCH_VAL"));

			List<EgovMap> resultList =  carInfoService.getCarFineExcelList(map);
			EgovMap tempData = null;
			for (int k = 0; k < resultList.size(); k++) {
				tempData = (EgovMap) resultList.get(k);
				data = new EgovMap();
				for (int m = 0; m < tempData.size(); m++) {
					String val = Objects.toString(tempData.getValue(m), Strings.EMPTY);
					data.put("data" + m, val);
				}
				listData.add(data);
			}

			String excelTitle = "범칙금리스트";
			EgovExcelUtils.makeExcelFile(saveFilePath, title, headerData, listData, checkdir);
			EgovExcelUtils.downloadFile(response, saveFilePath, excelTitle + ".xls");
		} catch (Exception e) {
			logger.error("e.getMessage() :" + e.getMessage());
		}
    }

    /**
     * 파일 다운로드
     */

    @RequestMapping("/hns/adf/crm/downloadFiles.json")
	public void downloadFile(@RequestParam Map<String,Object> map, HttpServletRequest request, HttpServletResponse response) throws Exception {

    	String strPath = StringUtil.fileDownloadPathReplace(String.valueOf(map.get("path")));

		File file = new File(strPath);
        response.setContentType("application/download; charset=utf-8");
        response.setContentLength((int) file.length());
        String header = request.getHeader("User-Agent");
        boolean b = header.indexOf("MSIE") > -1;
        String fileName = null;

        if (b) {
            fileName = URLEncoder.encode(file.getName(), "UTF-8");
        } else {
            fileName = new String(file.getName().getBytes("UTF-8"), "ISO-8859-1");
        }
    	response.setHeader("Pragma", "no-cache");
		response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ";");
        response.setHeader("Content-Transfer-Encoding", "binary;");
        response.setCharacterEncoding("UTF-8");

        OutputStream out = response.getOutputStream();
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(file);

            FileCopyUtils.copy(fis, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }

            out.flush();
        }

	}



    /**
	 * 본부 지점별 차량 현황 조회
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/hns/adf/crm/getCarDeptStatList.json", method=RequestMethod.POST)
	@ResponseBody
	public Model getCarDeptStatList(@RequestBody Map<String, Object> map, Model model) {
		try {
			PaginationInfo paginationInfo = new PaginationInfo();
			int totalCnt = 0;
			HnsPagingUtil.setPagingInfo( map , egovPropertyService, paginationInfo);
			LoginVO loginVO =  (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
			map.put( HnsConst.LOGIN_ID, loginVO.getId());
			map.put( HnsConst.AUTHOR_CODE, loginVO.getAuthor());
			map.put( HnsConst.LOGIN_ORG, loginVO.getDeptcode());

			List<Map<String,Object>> resultList = carInfoService.getCarDeptStatList(map);

			if (resultList != null && resultList.size() > 0) {
				totalCnt = MapUtils.getInteger( ((Map<String, Object>) resultList.get(0)) , HnsConst.TOTAL_CNT);
			}

			paginationInfo.setTotalRecordCount(totalCnt);
			model.addAttribute( HnsConst.DATA, resultList);
			model.addAttribute( HnsConst.PAGINATION_INFO, paginationInfo);
			model.addAttribute( HnsConst.STATUS, HnsConst.OK);

		} catch(Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("err_code","F");
		}
		return model;
	}

	/**
	 * 개인별별 차량 현황 조회
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/hns/adf/crm/getCarUserStatList.json", method=RequestMethod.POST)
	@ResponseBody
	public Model getCarUserStatList(@RequestBody Map<String, Object> map, Model model) {
		try {
			PaginationInfo paginationInfo = new PaginationInfo();
			int totalCnt = 0;
			HnsPagingUtil.setPagingInfo( map , egovPropertyService, paginationInfo);
			LoginVO loginVO =  (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
			map.put( HnsConst.LOGIN_ID, loginVO.getId());
			map.put( HnsConst.AUTHOR_CODE, loginVO.getAuthor());
			map.put( HnsConst.LOGIN_ORG, loginVO.getDeptcode());

			List<Map<String,Object>> resultList = carInfoService.getCarUserStatList(map);

			if (resultList != null && resultList.size() > 0) {
				totalCnt = MapUtils.getInteger( ((Map<String, Object>) resultList.get(0)) , HnsConst.TOTAL_CNT);
			}

			paginationInfo.setTotalRecordCount(totalCnt);
			model.addAttribute( HnsConst.DATA, resultList);
			model.addAttribute( HnsConst.PAGINATION_INFO, paginationInfo);
			model.addAttribute( HnsConst.STATUS, HnsConst.OK);

		} catch(Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("err_code","F");
		}
		return model;
	}

}
