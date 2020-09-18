package com.hns.adf.crm.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.cmm.util.FileUtils;
import com.hns.adf.crm.service.CarInfoService;
import com.hns.common.util.EgovExcelUtils;
import com.hns.sym.common.service.HnsConst;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.let.utl.fcc.service.EgovStringUtil;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.fdl.cmmn.exception.EgovBizException;
import egovframework.rte.fdl.security.userdetails.util.EgovUserDetailsHelper;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Service("carInfoService")
public class CarInfoServiceImpl extends EgovAbstractServiceImpl implements CarInfoService {

	@Resource(name="carInfoDAO")
	public CarInfoDAO carInfoDAO;
	private Log logger = LogFactory.getLog(this.getClass());
    public static final int BUFF_SIZE = 2048;


	/**
	 * 운행일지관리 상세 조회한다
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getCarDrivingInfoMntDetail(Map<String, Object> map) throws Exception{



		Map<String, Object> fileData = carInfoDAO.getCarDrivingInfoMntDetail(map);
		String uploadPath = EgovProperties.getProperty("adf.upload_path.dashboard");
		String dsbrdImgNm = (String) fileData.get("DSBRD_IMG_NM");
		String dsbrdImgLc = (String) fileData.get("DSBRD_IMG_LOC");

		String imgUrl = uploadPath + "/" + dsbrdImgLc + "/" + dsbrdImgNm;

		if (MapUtils.isNotEmpty(fileData)) {
			if (dsbrdImgNm == null || "".equals(dsbrdImgNm)) {
				fileData.put("src", "");
				fileData.put("src_EXT", "");
			} else {
				File f = new File(imgUrl);
				FileUtils fu = new FileUtils();
				byte[] blob = fu.imageToByteArray(f.getPath());

				fileData.put("src", blob);
				fileData.put("src_EXT", imgUrl.substring(imgUrl.lastIndexOf(".") + 1));

			}
		}


		return fileData;


	}

	/**
	 * 운행일지관리 마감 조회한다
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getCarDrivingInfoMntDedlne(Map<String, Object> map) throws Exception{
		return carInfoDAO.getCarDrivingInfoMntDedlne(map);
	}

	/**
	 * 운행일지관리 차량중복조회
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getCarDrivingInfo(Map<String, Object> map) throws Exception{
		return carInfoDAO.getCarDrivingInfo(map);
	}

	/**
	 * 운행일지관리 주유량, 주행거리 조회한다
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getCarDrivingPreSum(Map<String, Object> map) throws Exception{
		return carInfoDAO.getCarDrivingPreSum(map);
	}

	public Map<String, Object> getCarDrivingMonSum(Map<String, Object> map) throws Exception{
		return carInfoDAO.getCarDrivingMonSum(map);
	}

	public Map<String, Object> getCarDrivingInfoMntSum(Map<String, Object> map) throws Exception{
		return carInfoDAO.getCarDrivingInfoMntSum(map);
	}







	/**
	 * 운행일지관리 목록조회 2
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> getCarDrivingInfoMntList2(Map<String, Object> map) throws Exception{
		return carInfoDAO.getCarDrivingInfoMntList2(map);
	}




	/**
	 * 운행일지를 수정한다.
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void updateCarDrivingInfoMnt(HttpServletRequest request, Map<String, Object> map) throws Exception{
		LoginVO loginVO = EgovUserDetailsHelper.isAuthenticated()
		        ? (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser() : null;

		final MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
		try {


			map.put("LOGIN_ID", loginVO.getId());

			SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");
			SimpleDateFormat fileSaveDir = new SimpleDateFormat("yyyyMM");
			Calendar c1 = Calendar.getInstance();
			String originalFileName = null;
			String uploadFileName = null;
			String originalFileExtention = null;

			//List<MultipartFile> mf = multipartHttpServletRequest.getFiles("CAR_LOG_FILE_LOC1");
			String fileName = EgovStringUtil.isNullToString(request.getAttribute("FILE_TEMP_" + "CAR_LOG_FILE_LOC1"));
			if (fileName != null && !fileName.equals("")) {

				String basePath = EgovProperties.getProperty("adf.upload_path.log");
				logger.debug("basePath : "+basePath);

				originalFileName = fileName;
			  	uploadFileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
				originalFileExtention = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
				uploadFileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
				uploadFileName = 	uploadFileName +sdf.format(c1.getTime())+ "." + originalFileExtention;


				String offsetPath = fileSaveDir.format(c1.getTime());
				String uploadPath = basePath + "/" + offsetPath;

				map.put("CAR_LOG_FILE_LOC", offsetPath); // 기안문서 첨부 문서 상대 경로
				map.put("CAR_LOG_FILE_NM", uploadFileName); // 기안문서 첨부 시스템에 올릴 파일명
				map.put("CAR_LOG_FILE_ORGN_NM", originalFileName); // 기안문서 원본 첨부 파일 명

				//writeUploadedFile(mFile, uploadFileName, uploadPath);
				new com.cmm.util.FileUtils().moveUploadFile(request, uploadPath, uploadFileName,"CAR_LOG_FILE_LOC1");

			}

			//mf = multipartHttpServletRequest.getFiles("DSBRD_IMG_LOC1");
			fileName = EgovStringUtil.isNullToString(request.getAttribute("FILE_TEMP_" + "DSBRD_IMG_LOC1"));
			if (fileName != null && !fileName.equals("")) {

				String basePath = EgovProperties.getProperty("adf.upload_path.dashboard");

				originalFileName = fileName;
			  	uploadFileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
				originalFileExtention = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
				uploadFileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
				uploadFileName = 	uploadFileName +sdf.format(c1.getTime())+ "." + originalFileExtention;

				String offsetPath = fileSaveDir.format(c1.getTime());
				String uploadPath = basePath + "/" + offsetPath;

				map.put("DSBRD_IMG_LOC", offsetPath); // 이미지 첨부 문서 상대 경로
				map.put("DSBRD_IMG_NM", uploadFileName); // 이미지 첨부 시스템에 올릴 파일명
				map.put("DSBRD_IMG_ORGN_NM", originalFileName); // 이미지 원본 첨부 파일 명
				//writeUploadedFile(mFile, uploadFileName, uploadPath);
				new com.cmm.util.FileUtils().moveUploadFile(request, uploadPath, uploadFileName,"DSBRD_IMG_LOC1");

			}


			fileName = EgovStringUtil.isNullToString(request.getAttribute("FILE_TEMP_" + "REFUEL_RECEIPT_FILE_LOC1"));
			if (fileName != null && !fileName.equals("")) {

				String basePath = EgovProperties.getProperty("adf.upload_path.refuel");
				logger.debug("basePath : "+basePath);

				originalFileName = fileName;
			  	uploadFileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
				originalFileExtention = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
				uploadFileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
				uploadFileName = 	uploadFileName +sdf.format(c1.getTime())+ "." + originalFileExtention;


				String offsetPath = fileSaveDir.format(c1.getTime());
				String uploadPath = basePath + "/" + offsetPath;

				map.put("REFUEL_RECEIPT_FILE_LOC", offsetPath); // 기안문서 첨부 문서 상대 경로
				map.put("REFUEL_RECEIPT_FILE_NM", uploadFileName); // 기안문서 첨부 시스템에 올릴 파일명
				map.put("REFUEL_RECEIPT_FILE_ORGN_NM", originalFileName); // 기안문서 원본 첨부 파일 명

				//writeUploadedFile(mFile, uploadFileName, uploadPath);
				new com.cmm.util.FileUtils().moveUploadFile(request, uploadPath, uploadFileName,"REFUEL_RECEIPT_FILE_LOC1");

			}


			carInfoDAO.updateCarDrivingInfoMnt(map);


		} catch (EgovBizException be) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw be;
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e;
		}

	}


	/**
	 * 운행일지를 마감처리한다.
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void insertEndCarDrivingInfoMnt(Map<String, Object> map) throws Exception{
		carInfoDAO.insertEndCarDrivingInfoMnt(map);
	}


	/**
	 * 운행일지를 등록한다.
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void insertCarDrivingInfoMnt(HttpServletRequest request, Map<String, Object> map) throws Exception{


		LoginVO loginVO = EgovUserDetailsHelper.isAuthenticated()
		        ? (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser() : null;

		final MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
		Iterator<String> iterator = multipartHttpServletRequest.getFileNames();
		try {

			map.put("LOGIN_ID", loginVO.getId());
			map.put("CAR_LOG_DATE", map.get("CAR_LOG_DATE")+"-01");


			SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");
			SimpleDateFormat fileSaveDir = new SimpleDateFormat("yyyyMM");
			Calendar c1 = Calendar.getInstance();
			String originalFileName = null;
			String uploadFileName = null;
			String originalFileExtention = null;

			//List<MultipartFile> mf = multipartHttpServletRequest.getFiles("CAR_LOG_FILE_LOC");
			String fileName = EgovStringUtil.isNullToString(request.getAttribute("FILE_TEMP_" + "CAR_LOG_FILE_LOC"));
			if (fileName != null && !"".equals(fileName)) {

				String basePath = EgovProperties.getProperty("adf.upload_path.log");
				logger.debug("basePath : "+basePath);

				originalFileName = fileName;
			  	uploadFileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
				originalFileExtention = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
				uploadFileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
				uploadFileName = 	uploadFileName +sdf.format(c1.getTime())+ "." + originalFileExtention;


				String offsetPath = fileSaveDir.format(c1.getTime());
				String uploadPath = basePath + "/" + offsetPath;

				map.put("CAR_LOG_FILE_LOC", offsetPath); // 기안문서 첨부 문서 상대 경로
				map.put("CAR_LOG_FILE_NM", uploadFileName); // 기안문서 첨부 시스템에 올릴 파일명
				map.put("CAR_LOG_FILE_ORGN_NM", originalFileName); // 기안문서 원본 첨부 파일 명

				//writeUploadedFile(mFile, uploadFileName, uploadPath);
				new com.cmm.util.FileUtils().moveUploadFile(request, uploadPath, uploadFileName,"CAR_LOG_FILE_LOC");
			}

			//mf = multipartHttpServletRequest.getFiles("DSBRD_IMG_LOC");
			fileName = EgovStringUtil.isNullToString(request.getAttribute("FILE_TEMP_" + "DSBRD_IMG_LOC"));
			if (fileName != null || !"".equals(fileName)) {

				String basePath = EgovProperties.getProperty("adf.upload_path.dashboard");

				originalFileName = fileName;
			  	uploadFileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
				originalFileExtention = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
				uploadFileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
				uploadFileName = 	uploadFileName +sdf.format(c1.getTime())+ "." + originalFileExtention;

				String offsetPath = fileSaveDir.format(c1.getTime());
				String uploadPath = basePath + "/" + offsetPath;

				map.put("DSBRD_IMG_LOC", offsetPath); // 이미지 첨부 문서 상대 경로
				map.put("DSBRD_IMG_NM", uploadFileName); // 이미지 첨부 시스템에 올릴 파일명
				map.put("DSBRD_IMG_ORGN_NM", originalFileName); // 이미지 원본 첨부 파일 명
				//writeUploadedFile(mFile, uploadFileName, uploadPath);
				new com.cmm.util.FileUtils().moveUploadFile(request, uploadPath, uploadFileName,"DSBRD_IMG_LOC");

			}

			fileName = EgovStringUtil.isNullToString(request.getAttribute("FILE_TEMP_" + "REFUEL_RECEIPT_FILE_LOC"));
			if (fileName != null && !"".equals(fileName)) {

				String basePath = EgovProperties.getProperty("adf.upload_path.refuel");
				logger.debug("basePath : "+basePath);

				originalFileName = fileName;
			  	uploadFileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
				originalFileExtention = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
				uploadFileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
				uploadFileName = 	uploadFileName +sdf.format(c1.getTime())+ "." + originalFileExtention;


				String offsetPath = fileSaveDir.format(c1.getTime());
				String uploadPath = basePath + "/" + offsetPath;

				map.put("REFUEL_RECEIPT_FILE_LOC", offsetPath); // 기안문서 첨부 문서 상대 경로
				map.put("REFUEL_RECEIPT_FILE_NM", uploadFileName); // 기안문서 첨부 시스템에 올릴 파일명
				map.put("REFUEL_RECEIPT_FILE_ORGN_NM", originalFileName); // 기안문서 원본 첨부 파일 명

				//writeUploadedFile(mFile, uploadFileName, uploadPath);
				new com.cmm.util.FileUtils().moveUploadFile(request, uploadPath, uploadFileName,"REFUEL_RECEIPT_FILE_LOC");
			}
			carInfoDAO.insertCarDrivingInfoMnt(map);

		} catch (EgovBizException be) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw be;
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e;
		}

	}


	/**
	 * 운행일지관리을 삭제한다.
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void deleteCarDrivingInfoMnt(Map<String, Object> map) throws Exception{
		List<Map<String, Object>> mapList = (List<Map<String, Object>>) map.get("List");
		if(mapList != null){
			for (Map<String, Object> mMap : mapList) {
				carInfoDAO.deleteCarDrivingInfoMnt(mMap);
			}
		}
	}



	@Override
	public List<Map<String, Object>> getCarDrivingInfoMntList(Map<String, Object> map) throws Exception {
		return carInfoDAO.getCarDrivingInfoMntList(map);
	}

	@Override
	public List<Map<String, Object>> getCarUsrInfo(Map<String, Object> map) throws Exception {
		return carInfoDAO.selectCarUsrInfo(map);
	}

	@Override
	public List<Map<String, Object>> getCarDrivingInfoMntListMonthly(Map<String, Object> map) throws Exception {
		return carInfoDAO.getCarDrivingInfoMntListMonthly(map);
	}



	@Override
	public List<EgovMap> getCarDrivingExcelList(HashMap map) throws Exception {
		return carInfoDAO.getCarDrivingExcelList(map);
	}

	@Override
	public List<EgovMap> getCarDrivingExcelListMonthly(HashMap map) throws Exception {
		return carInfoDAO.getCarDrivingExcelListMonthly(map);
	}


    /*----------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------------------*/
    /*------------------------------------- 차량 범칙금 관리----------------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------------------*/



	/**
	 * 범칙금관리 상세 조회한다
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getCarFineInfoMntDetail(Map<String, Object> map) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> detailInfo    = carInfoDAO.getCarFineInfoMntDetail(map);
		List<Map<String,Object>> fileData = carInfoDAO.getMultiFileList(map);
		try {
			String uploadPath = EgovProperties.getProperty("adf.upload_path.fine");
			String fineBillsNm = (String) detailInfo.get("FINE_BILLS_FILE_NM");
			String fineBillsLoc = (String) detailInfo.get("FINE_BILLS_ADD_LOC");

			String imgUrl = uploadPath + "/" + fineBillsLoc + "/" + fineBillsNm;

			if (MapUtils.isNotEmpty(detailInfo)) {
				if (fineBillsNm == null || "".equals(fineBillsNm)) {
					detailInfo.put("src", "");
					detailInfo.put("src_EXT", "");
				} else {
					File f = new File(imgUrl);
					if (f.exists()) {
						FileUtils fu = new FileUtils();
						byte[] blob = fu.imageToByteArray(f.getPath());

						detailInfo.put("src", blob);
						detailInfo.put("src_EXT", imgUrl.substring(imgUrl.lastIndexOf(".") + 1));
					} else {
						detailInfo.put("src", "");
						detailInfo.put("src_EXT", "");
					}


				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}



		resultMap.put("detailInfo", detailInfo);
		resultMap.put("fileData"  , fileData);

		return resultMap;
	}


	/**
	 * 범칙금관리 목록조회 2
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> getCarFineInfoMntList2(Map<String, Object> map) throws Exception{
		return carInfoDAO.getCarFineInfoMntList2(map);
	}




	/**
	 * 범칙금 정보를 수정한다.
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Override
	public void updateCarFineInfoMnt(HttpServletRequest request) throws Exception {
		LoginVO loginVO = EgovUserDetailsHelper.isAuthenticated() ? (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser() : null;
		Map<String, Object> map = new HashMap<String, Object>();

		//범칙금관리 데이터 생성
		map.put("EMP_NM_D" , request.getParameter("EMP_NM_D"));
		map.put("VIOLT_DATE_D" , request.getParameter("VIOLT_DATE_D"));
		map.put("VIOLT_TIME_VAL_D" , request.getParameter("VIOLT_TIME_VAL_D"));
		map.put("CAR_NO_D" , request.getParameter("CAR_NO_D"));
		map.put("CHRG_EMP_NO_D" , request.getParameter("CHRG_EMP_NO_D"));
		map.put("CHRG_TEL_NO_D" , request.getParameter("CHRG_TEL_NO_D"));
		map.put("FINE_SEQL_NO_D" , request.getParameter("FINE_SEQL_NO_D"));
		map.put("FINE_AMT_D" , request.getParameter("FINE_AMT_D"));
		map.put("FINE_DEDLNE_DATE_D" , request.getParameter("FINE_DEDLNE_DATE_D"));
		map.put("EMAIL_SND_DATE_D" , request.getParameter("EMAIL_SND_DATE_D"));
		map.put("FINE_PAY_YN_D" , request.getParameter("FINE_PAY_YN_D"));
		map.put("VIOLT_CTT_GB_CD" , request.getParameter("VIOLT_CTT_GB_CD"));
		map.put("NTE_CTT_D" , request.getParameter("NTE_CTT_D"));
		map.put("pay_UPLOAD_YN" , request.getParameter("pay_UPLOAD_YN"));
		map.put("PRE_PAY_RCT_ADD_LOC_VAL" , request.getParameter("PRE_PAY_RCT_ADD_LOC_VAL"));
		map.put("CAR_FINE_ID" , request.getParameter("CAR_FINE_ID"));

		map.put("LOGIN_ID", loginVO.getId());
		map.put("INPUT_FLAG", request.getParameter("INPUT_FLAG"));

	/*	Map<String,Object> paramMap = new HashMap<String,Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat fileSaveDir = new SimpleDateFormat("yyyyMM");
        Calendar c1 = Calendar.getInstance();
		String fine_pay_doc_nm = retnMap.get("FINE_SEQL_NO_D") + "_" + retnMap.get("EMP_NM_D") + "_" + sdf.format(c1.getTime());
		retnMap.put("LOGIN_ID", loginVO.getId());


		//납부영수증 첨부파일 변경한 경우
		if(retnMap.get("pay_UPLOAD_YN").equals("Y")){
			logger.info("납부영수증 변경 Case");
			// 기존 첨부파일 삭제 후 신규 첨부파일 업로드
			//이전 파일 삭제
			String deleteFile_pay = retnMap.get("PRE_PAY_RCT_ADD_LOC_VAL")+"";
			// 신규 첨부파일 업로드
			String uploadPath_pay = EgovProperties.getProperty( "adf.upload_path.vio") + "/" + fileSaveDir.format(c1.getTime());
			String uploadFile_pay = uploadFile(request, uploadPath_pay, fine_pay_doc_nm, "uploadNamePay_D");
			if(uploadFile_pay != null){
				fine_pay_doc_nm = uploadFile_pay;
				String path_pay = uploadPath_pay+"/"+fine_pay_doc_nm;
				retnMap.put("PAY_RCT_ADD_LOC_VAL_D", path_pay);
			}
		}*/


		final MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
		String originalFileName = null;

		SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");
		SimpleDateFormat fileSaveDir = new SimpleDateFormat("yyyyMM");
		Calendar c1 = Calendar.getInstance();
		String uploadFileName = null;
		String originalFileExtention = null;

		// 기안문 첨부 문서
		//List<MultipartFile> mf = multipartHttpServletRequest.getFiles("FINE_BILLS_ADD_LOC1");
		String fileName = EgovStringUtil.isNullToString(request.getAttribute("FILE_TEMP_" + "FINE_BILLS_ADD_LOC1"));
		if (fileName != null && !"".equals(fileName)) {

			String basePath = EgovProperties.getProperty("adf.upload_path.fine");
			logger.debug("basePath : "+basePath);

			originalFileName = fileName;
		  	uploadFileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
			originalFileExtention = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
			uploadFileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
			uploadFileName = 	uploadFileName +sdf.format(c1.getTime())+ "." + originalFileExtention;


			String offsetPath = fileSaveDir.format(c1.getTime());
			String uploadPath = basePath + "/" + offsetPath;

			map.put("FINE_BILLS_ADD_LOC", offsetPath); // 기안문서 첨부 문서 상대 경로
			map.put("FINE_BILLS_FILE_NM", uploadFileName); // 기안문서 첨부 시스템에 올릴 파일명
			map.put("FINE_BILLS_ORGN_FILE_NM", originalFileName); // 기안문서 원본 첨부 파일 명

			//writeUploadedFile(mFile, uploadFileName, uploadPath);
			new com.cmm.util.FileUtils().moveUploadFile(request, uploadPath, uploadFileName,"FINE_BILLS_ADD_LOC1");

		}

		//업데이트
		carInfoDAO.updateCarFineInfoMnt(map);
	}


	@Override
	public void insertMultiFiles(Map<String, Object> map) throws Exception {
		try {
			LoginVO loginVO = EgovUserDetailsHelper.isAuthenticated() ? (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser() : null;

			map.put("LOGIN_ID"      , loginVO.getId());

			carInfoDAO.updateCarInfoMnt(map);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void insertCarFineInfoMnt(HttpServletRequest request) throws Exception {
		LoginVO loginVO = EgovUserDetailsHelper.isAuthenticated() ? (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser() : null;
		Map<String, Object> retnMap = new HashMap<String, Object>();
		retnMap = getPayMap(request,"C");
		retnMap.put("LOGIN_ID", loginVO.getId());
		retnMap.put("PAY_RCT_ADD_LOC_VAL", "");


		final MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;

		try {
			String pPersonId = request.getParameter("PERSON_ID");
			String pEmp_no = request.getParameter("MAIN_EMP_NO");

			Map<String, Object> map = new HashMap<String, Object>();

			map = getPayMap(request,"C");
			map.put("LOGIN_ID", loginVO.getId());
			map.put("PAY_RCT_ADD_LOC_VAL", "");
			map.put("INPUT_FLAG", request.getParameter("INPUT_FLAG"));


			SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");
			SimpleDateFormat fileSaveDir = new SimpleDateFormat("yyyyMM");
			Calendar c1 = Calendar.getInstance();
			String originalFileName = null;
			String uploadFileName = null;
			String originalFileExtention = null;

			// 기안문 첨부 문서
			//List<MultipartFile> mf = multipartHttpServletRequest.getFiles("FINE_BILLS_ADD_LOC");
			String fileName = EgovStringUtil.isNullToString(request.getAttribute("FILE_TEMP_" + "FINE_BILLS_ADD_LOC"));
			if (fileName != null && !"".equals(fileName)) {

				String basePath = EgovProperties.getProperty("adf.upload_path.fine");
				logger.debug("basePath : "+basePath);

				originalFileName = fileName;
			  	uploadFileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
				originalFileExtention = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
				uploadFileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
				uploadFileName = 	uploadFileName +sdf.format(c1.getTime())+ "." + originalFileExtention;


				String offsetPath = fileSaveDir.format(c1.getTime());
				String uploadPath = basePath + "/" + offsetPath;

				map.put("FINE_BILLS_ADD_LOC", offsetPath); // 기안문서 첨부 문서 상대 경로
				map.put("FINE_BILLS_FILE_NM", uploadFileName); // 기안문서 첨부 시스템에 올릴 파일명
				map.put("FINE_BILLS_ORGN_FILE_NM", originalFileName); // 기안문서 원본 첨부 파일
				                                                // 명

				//writeUploadedFile(mFile, uploadFileName, uploadPath);
				new com.cmm.util.FileUtils().moveUploadFile(request, uploadPath, uploadFileName,"FINE_BILLS_ADD_LOC");

			}

			String userId = loginVO.getId();


			carInfoDAO.insertCarFineInfoMnt(map);


		} catch (EgovBizException be) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw be;
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e;
		}
	}

	//범칙금관리 신규 데이터 Map 생성
	private Map<String, Object> getPayMap(HttpServletRequest request, String option){
		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("EMP_NO" , request.getParameter("EMP_NO"));
		retMap.put("EMP_NM" , request.getParameter("EMP_NM"));
		retMap.put("ORG_CD" , request.getParameter("ORG_CD"));
		retMap.put("VIOLT_DATE" , request.getParameter("VIOLT_DATE"));
		retMap.put("VIOLT_TIME_VAL" , request.getParameter("VIOLT_TIME_VAL"));
		retMap.put("CAR_NO" , request.getParameter("CAR_NO"));
		retMap.put("CHRG_EMP_NO" , request.getParameter("CHRG_EMP_NO"));
		retMap.put("CHRG_TEL_NO" , request.getParameter("CHRG_TEL_NO"));
		retMap.put("FINE_SEQL_NO" , request.getParameter("FINE_SEQL_NO"));
		retMap.put("FINE_AMT" , request.getParameter("FINE_AMT"));
		retMap.put("FINE_DEDLNE_DATE" , request.getParameter("FINE_DEDLNE_DATE"));
		retMap.put("EMAIL_SND_DATE" , request.getParameter("EMAIL_SND_DATE"));
		retMap.put("FINE_PAY_YN" , request.getParameter("FINE_PAY_YN"));
		retMap.put("VIOLT_CTT_GB_CD_R" , request.getParameter("VIOLT_CTT_GB_CD_R"));
		retMap.put("NTE_CTT" , request.getParameter("NTE_CTT"));
		return retMap;
	}

	/**
	 * 범칙금 정보를 삭제한다.
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void deleteCarFineInfoMnt(Map<String, Object> map) throws Exception{
		List<Map<String, Object>> mapList = (List<Map<String, Object>>) map.get("List");
		if(mapList != null){
			for (Map<String, Object> mMap : mapList) {
				carInfoDAO.deleteCarFineInfoMnt(mMap);
			}
		}
	}






	@Override
	public List<Map<String, Object>> getCarFineInfoMntList(Map<String, Object> map) throws Exception {
		return carInfoDAO.getCarFineInfoMntList(map);
	}

	@Override
	public List<Map<String, Object>> getCarDeptStatList(Map<String, Object> map) throws Exception {
		return carInfoDAO.getCarDeptStatList(map);
	}

	@Override
	public List<Map<String, Object>> getCarUserStatList(Map<String, Object> map) throws Exception {
		return carInfoDAO.getCarUserStatList(map);
	}





	@Override
	public List<EgovMap> getCarFineExcelList(HashMap map) throws Exception {
		return carInfoDAO.getCarFineExcelList(map);
	}






	@Override
	public void deleteFile(String filePath) throws Exception {

		String deleteFile = filePath;

		try{
			File delfile = new File(deleteFile);
			delfile.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
