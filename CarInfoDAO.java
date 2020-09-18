package com.hns.adf.crm.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Repository("carInfoDAO")
public class CarInfoDAO extends EgovAbstractDAO {




	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCarDrivingInfoMntList2(Map<String, Object> map) {
		return (List<Map<String, Object>>) list("carInfoDAO.getCarDrivingInfoMntList2", map);
	}




/*운행일지관리 일별 목록*/
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCarDrivingInfoMntList(Map<String, Object> map) {
		return (List<Map<String, Object>>) list("carInfoDAO.getCarDrivingInfoMntList", map);
	}
/*운행일지관리 월별 목록*/
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCarDrivingInfoMntListMonthly(Map<String, Object> map) {
		return (List<Map<String, Object>>) list("carInfoDAO.getCarDrivingInfoMntListMonthly", map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectCarUsrInfo(Map<String, Object> map) throws Exception {
        return (List<Map<String, Object>>) list("carInfoDAO.selectCarUsrInfo", map);
    }

	@SuppressWarnings("unchecked")
	public List<EgovMap> getCarDrivingExcelList(Map<String, Object> map) throws Exception {
		return (List<EgovMap>) list("carInfoDAO.getCarDrivingExcelList", map);
	}
	@SuppressWarnings("unchecked")
	public List<EgovMap> getCarDrivingExcelListMonthly(Map<String, Object> map) throws Exception {
		return (List<EgovMap>) list("carInfoDAO.getCarDrivingExcelListMonthly", map);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getCarDrivingInfoMntDetail(Map<String, Object> map) {
		return (Map<String, Object>)select("carInfoDAO.getCarDrivingInfoMntDetail", map);
	}

	/*운행일지관리 마감여부 조회*/
	@SuppressWarnings("unchecked")
	public Map<String, Object> getCarDrivingInfoMntDedlne(Map<String, Object> map) {
		return (Map<String, Object>)select("carInfoDAO.getCarDrivingInfoMntDedlne", map);
	}

	/*운행일지관리 차량중복조회*/
	@SuppressWarnings("unchecked")
	public Map<String, Object> getCarDrivingInfo(Map<String, Object> map) {
		return (Map<String, Object>)select("carInfoDAO.getCarDrivingInfo", map);
	}

	/*운행일지관리 주유량, 주행거리 조회*/

	@SuppressWarnings("unchecked")
	public Map<String, Object> getCarDrivingPreSum(Map<String, Object> map) {
		return (Map<String, Object>)select("carInfoDAO.getCarDrivingPreSum", map);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getCarDrivingMonSum(Map<String, Object> map) {
		return (Map<String, Object>)select("carInfoDAO.getCarDrivingMonSum", map);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getCarDrivingInfoMntSum(Map<String, Object> map) {
		return (Map<String, Object>)select("carInfoDAO.getCarDrivingInfoMntSum", map);
	}


    /**
	 * 운행일지관리를 수정한다.
	 * @param map - 수정할 정보가 담긴 map
	 * @return int
	 * @exception Exception
	 */
    public void updateCarDrivingInfoMnt(Map<String, Object> map) throws Exception {
    	update("carInfoDAO.updateCarDrivingInfoMnt", map);
    }

    /**
	 * 운행일지 마감처리한다.
	 * @param map - 마감처리할 정보가 담긴 map
	 * @return int
	 * @exception Exception
	 */
    public void insertEndCarDrivingInfoMnt(Map<String, Object> map) throws Exception {
    	insert("carInfoDAO.insertEndCarDrivingInfoMnt", map);
    }

	/**
	 * 운행일지관리를 등록한다.
	 * @param map - 등록할 정보가 담긴 map
	 * @return int
	 * @exception Exception
	 */
    public void insertCarDrivingInfoMnt(Map<String, Object> map) throws Exception {
        insert("carInfoDAO.insertCarDrivingInfoMnt", map);
    }




    /**
	 * 운행일지관리를 삭제한다.
	 * @param map - 삭제할 정보가 담긴 map
	 * @return int
	 * @exception Exception
	 */
    public void deleteCarDrivingInfoMnt(Map<String, Object> map) throws Exception {
    	delete("carInfoDAO.deleteCarDrivingInfoMnt", map);
    }




    /*----------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------------------*/
    /*------------------------------------- 차량 범칙금 관리-----------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------------------*/




	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCarFineInfoMntList2(Map<String, Object> map) {
		return (List<Map<String, Object>>) list("carInfoDAO.getCarFineInfoMntList2", map);
	}




/*범칙금관리 목록*/
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCarFineInfoMntList(Map<String, Object> map) {
		return (List<Map<String, Object>>) list("carInfoDAO.getCarFineInfoMntList", map);
	}

	@SuppressWarnings("unchecked")
	public List<EgovMap> getCarFineExcelList(Map<String, Object> map) throws Exception {
		return (List<EgovMap>) list("carInfoDAO.getCarFineExcelList", map);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getCarFineInfoMntDetail(Map<String, Object> map) {
		return (Map<String, Object>)select("carInfoDAO.getCarFineInfoMntDetail", map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getMultiFileList(Map<String, Object> map) {
		return (List<Map<String, Object>>) list("carInfoDAO.getMultiFileList", map);
	}


    /**
	 * 범칙금관리를 수정한다.
	 * @param map - 수정할 정보가 담긴 map
	 * @return int
	 * @exception Exception
	 */

    public void updateCarFineInfoMnt(Map<String, Object> map) throws Exception {
        update("carInfoDAO.updateCarFineInfoMnt", map);
    }

    public void updateCarInfoMnt(Map<String, Object> map) throws Exception {
        update("carInfoDAO.updateCarInfoMnt", map);
    }



	/**
	 * 범칙금관리를 등록한다.
	 * @param map - 등록할 정보가 담긴 map
	 * @return int
	 * @exception Exception
	 */

    public String insertCarFineInfoMnt(Map<String, Object> map) throws Exception {
        return (String)insert("carInfoDAO.insertCarFineInfoMnt", map);
    }



    /**
	 * 범칙금관리를 삭제한다.
	 * @param map - 삭제할 정보가 담긴 map
	 * @return int
	 * @exception Exception
	 */
    public void deleteCarFineInfoMnt(Map<String, Object> map) throws Exception {
    	delete("carInfoDAO.deleteCarFineInfoMnt", map);
    }




	public Map<String, Object> getFineBillsFile(Map<String, Object> map) throws Exception {
		return (Map<String, Object>)select("carInfoDAO.getFineBillsFile", map);
	}

	public Map<String, Object> getMultiBillsFile(Map<String, Object> map) throws Exception {
		return (Map<String, Object>)select("carInfoDAO.getMultiBillsFile", map);
	}

	public Map<String, Object> getResvCarFile(Map<String, Object> map) throws Exception {
		return (Map<String, Object>)select("carInfoDAO.getResvCarFile", map);
	}


	public Map<String, Object> getCarLogFile(Map<String, Object> map) throws Exception {
		return (Map<String, Object>)select("carInfoDAO.getCarLogFile", map);
	}

	public Map<String, Object> getCarRefuelFile(Map<String, Object> map) throws Exception {
		return (Map<String, Object>)select("carInfoDAO.getCarRefuelFile", map);
	}

	public Map<String, Object> getCarDashboardImg(Map<String, Object> map) throws Exception {
		return (Map<String, Object>)select("carInfoDAO.getCarDashboardImg", map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCarDeptStatList(Map<String, Object> map) {
		return (List<Map<String, Object>>) list("carInfoDAO.getCarDeptStatList", map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCarUserStatList(Map<String, Object> map) {
		return (List<Map<String, Object>>) list("carInfoDAO.getCarUserStatList", map);
	}





}
