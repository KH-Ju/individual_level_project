package com.hns.adf.crm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import egovframework.rte.psl.dataaccess.util.EgovMap;

public interface CarInfoService {

	/**
	 * 운행일지관리 일별 조회
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getCarDrivingInfoMntList(Map<String, Object> map) throws Exception;

	/**
	 * 운전자 차량번호 조회
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 */

	public List<Map<String, Object>> getCarUsrInfo(Map<String, Object> map) throws Exception;

	/**
	 * 운행일지관리 월별 조회
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getCarDrivingInfoMntListMonthly(Map<String, Object> map) throws Exception;

	/**
	 * 일별 운행일지관리 엑셀 조회
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<EgovMap> getCarDrivingExcelList(HashMap map) throws Exception;

	/**
	 * 월별 운행일지관리 엑셀 조회
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<EgovMap> getCarDrivingExcelListMonthly(HashMap map) throws Exception;

	/**
	 * 운행일지관리 목록조회 2
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getCarDrivingInfoMntList2(Map<String, Object> map) throws Exception;

	/**
	 * 운행일지관리 상세
	 *
	 * @param map
	 * @return
	 */
	public Map<String, Object> getCarDrivingInfoMntDetail(Map<String, Object> map) throws Exception;

	/**
	 * 운행일지관리 마감조회
	 *
	 * @param map
	 * @return
	 */
	public Map<String, Object> getCarDrivingInfoMntDedlne(Map<String, Object> map) throws Exception;

	/**
	 * 운행일지관리 차량중복조회
	 *
	 * @param map
	 * @return
	 */
	public Map<String, Object> getCarDrivingInfo(Map<String, Object> map) throws Exception;

	/**
	 * 운행일지관리 주유량, 주행거리조회
	 *
	 * @param map
	 * @return
	 */
	public Map<String, Object> getCarDrivingPreSum(Map<String, Object> map) throws Exception;
	public Map<String, Object> getCarDrivingMonSum(Map<String, Object> map) throws Exception;
	public Map<String, Object> getCarDrivingInfoMntSum(Map<String, Object> map) throws Exception;

	/**
	 * 운행일지관리를 수정한다.
	 * @param request
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void updateCarDrivingInfoMnt(HttpServletRequest request, Map<String, Object> map) throws Exception;

	/**
	 * 운행일지를 마감처리한다.
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void insertEndCarDrivingInfoMnt(Map<String, Object> map) throws Exception;

	/**
	 * 운행일지관리를 등록한다.
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public void insertCarDrivingInfoMnt(HttpServletRequest request, Map<String, Object> map) throws Exception;

	/**
	 * 운행일지관리를 삭제한다.
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void deleteCarDrivingInfoMnt(Map<String, Object> map) throws Exception;

	/*----------------------------------------------------------------------------------------------------------*/
	/*----------------------------------------------------------------------------------------------------------*/
	/*------------------------------------- 차량 범칙금 관리-----------------------------------------------------*/
	/*----------------------------------------------------------------------------------------------------------*/
	/*----------------------------------------------------------------------------------------------------------*/

	/**
	 * 범칙금관리 조회
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getCarFineInfoMntList(Map<String, Object> map) throws Exception;

	/**
	 * 범칙금관리 엑셀 조회
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<EgovMap> getCarFineExcelList(HashMap map) throws Exception;

	/**
	 * 범칙금관리 목록조회 2
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getCarFineInfoMntList2(Map<String, Object> map) throws Exception;

	/**
	 * 범칙금관리 상세
	 *
	 * @param map
	 * @return
	 */
	public Map<String, Object> getCarFineInfoMntDetail(Map<String, Object> map) throws Exception;

	/**
	 * 범칙금관리를 수정한다.
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void updateCarFineInfoMnt(HttpServletRequest request) throws Exception;

	/**
	 * 범칙금관리를 등록한다.
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void insertCarFineInfoMnt(HttpServletRequest request) throws Exception;

	public void insertMultiFiles(Map<String, Object> map) throws Exception;

	/**
	 * 범칙금관리를 삭제한다.
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void deleteCarFineInfoMnt(Map<String, Object> map) throws Exception;

	/**
	 * 파일 삭제
	 *
	 * @param filePath
	 * @throws Exception
	 */
	public void deleteFile(String filePath) throws Exception;


	/**
	 * 본부 지점별 차량 현황 조회
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getCarDeptStatList(Map<String, Object> map) throws Exception;

	/**
	 * 개인별 차량 현황 조회
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getCarUserStatList(Map<String, Object> map) throws Exception;

}
