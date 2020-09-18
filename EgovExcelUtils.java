package com.hns.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.hns.common.cmm.service.EgovWebUtil;
import com.hns.sym.common.service.HnsConst;

import SCSL.SLBsUtil;
import SCSL.SLDsFile;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.let.utl.fcc.service.EgovStringUtil;
import egovframework.rte.fdl.cmmn.exception.EgovBizException;
import egovframework.rte.fdl.security.userdetails.util.EgovUserDetailsHelper;
import egovframework.rte.psl.dataaccess.util.EgovMap;

public class EgovExcelUtils{

    public static boolean makeExcelFile(String saveFilePath, String title, List<String> headerData, List<EgovMap> listData, String checkdir) throws EgovBizException{

    	System.out.println("saveFilePath "+saveFilePath);
    	System.out.println("title "+saveFilePath);
    	System.out.println("saveFilePath "+saveFilePath);
    	System.out.println("saveFilePath "+saveFilePath);
    	System.out.println("saveFilePath "+saveFilePath);
    	System.out.println("saveFilePath "+saveFilePath);
    	System.out.println("saveFilePath "+saveFilePath);
    	System.out.println("saveFilePath "+saveFilePath);
    	System.out.println("saveFilePath "+saveFilePath);

        boolean resultFlag = true;
        saveFilePath = saveFilePath+".xls";

        //보안취약점점검(20190320)
        saveFilePath = StringUtil.filePathReplace(saveFilePath);

        System.out.println("saveFilePath "+saveFilePath);
        try{
            File cFile = new File(EgovWebUtil.filePathBlackList(checkdir));

            if (!cFile.isDirectory())
            cFile.mkdir();

            HSSFWorkbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet("sheet1");
            EgovMap dataMap = null;
            String header [] = new String[headerData.size()];
            int size [] = new int[headerData.size()];
            String align [] = new String[headerData.size()];
            String key [] = new String[headerData.size()];
            String temp [] = null;
            int rowIndex = 0;

            Row row = null;
            Cell cell = null;

            // Column Width
            for(int i=0;i<headerData.size();i++){
                temp = headerData.get(i).split("@");
                header[i] = temp[0];
                align[i] = temp[1];
                size[i] = Integer.parseInt(temp[2]);
                key[i] = temp[3];
                sheet.setColumnWidth(i, size[i]);
            }

            // Row
            for(int i=0;i<=listData.size()+1;i++){
                row = sheet.createRow(i);
                // Cell
                for(int j=0;j<headerData.size();j++){
                    cell = row.createCell(j);
                }
            }

//            // 타이틀
//            row = sheet.getRow(rowIndex++);
//            cell = row.getCell(0);
//            row.setHeight((short)700);
//            cell.setCellStyle(getStyle(workbook, "Title", "C"));
//            cell.setCellValue(new HSSFRichTextString(title));
//            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headerData.size() - 1));

            // 헤더
            row = sheet.getRow(rowIndex++);
            row.setHeight((short)300);
            for(int i=0;i<header.length;i++){
                cell = row.getCell(i);
                if(align[i].equals("RED")){
                    cell.setCellStyle(getStyle(workbook, "Header2", "C"));
                }else if(align[i].equals("YELLOW")){
                    cell.setCellStyle(getStyle(workbook, "Header3", "C"));
                }else{
                    cell.setCellStyle(getStyle(workbook, "Header", "C"));
                }
                cell.setCellValue(new HSSFRichTextString(header[i]));
            }

            // 데이터
            for(int i=0;i<listData.size();i++){
                dataMap = listData.get(i);

                row = sheet.getRow(rowIndex++);
                row.setHeight((short)250);
                for(int j=0;j<key.length;j++){
                	  cell = row.getCell(j);
                	  Object obj = MapUtils.getObject( dataMap, key[j]);
                	  if( obj instanceof java.lang.Long) {
                		  cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                		  cell.setCellValue(((Long)obj).longValue());
                		  continue;
                	  }

                	  if( obj instanceof java.lang.Double) {
                		  cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                		  cell.setCellValue(((Double)obj).doubleValue());
                		  continue;
                	  }

                      String data = (String)dataMap.get(key[j]);
                      if(StringUtils.isNumeric(data) && data.length() > 0 ){
//                      	double d = Double.parseDouble(data);
//                      	cell.setCellType(Cell.CELL_TYPE_NUMERIC);
//                        cell.setCellValue(d);

                    	  cell.setCellValue(new HSSFRichTextString(data));
                      }else{
                      	cell.setCellValue(new HSSFRichTextString(data));
                      }
                }
            }

            FileOutputStream fos = new FileOutputStream(saveFilePath);
            workbook.write(fos);
            fos.close();

        }catch(Exception e){
        	e.printStackTrace();
            throw new EgovBizException("makeExcelFile중 에러가 발생하였습니다.");
        }

        return resultFlag;

    }

    public static boolean makeStaExcelFile(String saveFilePath, String title, List<String> headerData, List<EgovMap> listData, String checkdir, int stringNum) throws EgovBizException{

        boolean resultFlag = true;

        try{
            File cFile = new File(EgovWebUtil.filePathBlackList(checkdir));

            if (!cFile.isDirectory())
            cFile.mkdir();

            HSSFWorkbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet("sheet1");
            EgovMap dataMap = null;
            String header [] = new String[headerData.size()];
            int size [] = new int[headerData.size()];
            String align [] = new String[headerData.size()];
            String key [] = new String[headerData.size()];
            String temp [] = null;
            int rowIndex = 0;

            Row row = null;
            Cell cell = null;

            // Column Width
            for(int i=0;i<headerData.size();i++){
                temp = headerData.get(i).split("@");
                header[i] = temp[0];
                align[i] = temp[1];
                size[i] = Integer.parseInt(temp[2]);
                key[i] = temp[3];
                sheet.setColumnWidth(i, size[i]);
            }

            // Row
            for(int i=0;i<=listData.size()+1;i++){
                row = sheet.createRow(i);
                // Cell
                for(int j=0;j<headerData.size();j++){
                    cell = row.createCell(j);
                }
            }

            // 타이틀
//            row = sheet.getRow(rowIndex++);
//            cell = row.getCell(0);
//            row.setHeight((short)700);
//            cell.setCellStyle(getStyle(workbook, "Title", "C"));
//            cell.setCellValue(new HSSFRichTextString(title));
//            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headerData.size() - 1));

            // 헤더
            row = sheet.getRow(rowIndex++);
            row.setHeight((short)300);
            for(int i=0;i<header.length;i++){
                cell = row.getCell(i);
                if(align[i].equals("RED")){
                    cell.setCellStyle(getStyle(workbook, "Header2", "C"));
                }else if(align[i].equals("YELLOW")){
                    cell.setCellStyle(getStyle(workbook, "Header3", "C"));
                }else{
                    cell.setCellStyle(getStyle(workbook, "Header", "C"));
                }
                cell.setCellValue(new HSSFRichTextString(header[i]));
            }

            // 데이터
            for(int i=0;i<listData.size();i++){
                dataMap = listData.get(i);

                row = sheet.getRow(rowIndex++);
                row.setHeight((short)250);
                for(int j=0;j<key.length;j++){
                    cell = row.getCell(j);
//                    cell.setCellStyle(getStyle(workbook, "Normal", align[j]));
                    if(j < stringNum){
                        cell.setCellValue((String)dataMap.get(key[j]));
                    }else{
//                        cell.setCellValue(Integer.parseInt((String)dataMap.get(key[j]))    );
                        cell.setCellValue(Double.parseDouble((String)dataMap.get(key[j]))    );
                    }
                }
            }

            FileOutputStream fos = new FileOutputStream(saveFilePath);
            workbook.write(fos);
            fos.close();

        }catch(Exception e){
            throw new EgovBizException("makeExcelFile중 에러가 발생하였습니다.");
        }

        return resultFlag;

    }

    public static boolean makeGridExcelFile(String saveFilePath, String title, String headerData, String sizeData, String alignData, String listData){

        boolean resultFlag = true;

        try{

            HSSFWorkbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet(title);
            String header [] = headerData.split("");
            String list [] = listData.split("");
            String size [] = sizeData.split("");
            String align [] = alignData.split("");
            String data [] = null;
            int rowIndex = 0;
            int rowCount = 0;

            Row row = null;
            Cell cell = null;

            // Column Width
            for(int i=0;i<size.length;i++){
                sheet.setColumnWidth(i, Integer.parseInt(size[i]) * 50);
            }

            // Row
            rowCount = list.length + 2;
            for(int i=0;i<rowCount;i++){
                row = sheet.createRow(i);
                // Cell
                for(int j=0;j<header.length;j++){
                    cell = row.createCell(j);
                }
            }

            // ŸƲ
            row = sheet.getRow(rowIndex++);
            cell = row.getCell(0);
            row.setHeight((short)700);
            cell.setCellStyle(getStyle(workbook, "Title", "C"));
            cell.setCellValue(new HSSFRichTextString(title));
            sheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 0, header.length - 1));

            // Ʈ
            row = sheet.getRow(rowIndex++);
            row.setHeight((short)300);
            for(int i=0;i<header.length;i++){
                cell = row.getCell(i);
                cell.setCellStyle(getStyle(workbook, "Header", "C"));
                cell.setCellValue(new HSSFRichTextString(header[i]));
            }

            // Ʈ
            for(int i=0;i<list.length;i++){
                data = list[i].split("");
                row = sheet.getRow(rowIndex++);
                row.setHeight((short)250);
                for(int j=0;j<data.length;j++){
                    cell = row.getCell(j);
                    cell.setCellStyle(getStyle(workbook, "Normal", align[j]));
                    cell.setCellValue(new HSSFRichTextString(data[j]));
                }
            }

            FileOutputStream fos = new FileOutputStream(saveFilePath);
            workbook.write(fos);
            fos.close();

        }catch(Exception e){
            e.printStackTrace();
            resultFlag = false;
        }

        return resultFlag;

    }

    private static HSSFCellStyle getStyle(HSSFWorkbook workbook, String cellType, String align){

        HSSFCellStyle style = null;
        HSSFFont font = workbook.createFont();

        font.setFontHeight((short)180);
        font.setFontName("돋움");

        style = workbook.createCellStyle();
        style.setFont(font);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setWrapText(true);

        if(align == null){
            align = "C";
        }

        if(align.equalsIgnoreCase("l") || align.equalsIgnoreCase("left")){
            style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        }else if(align.equalsIgnoreCase("r") || align.equalsIgnoreCase("right")){
            style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            style.setDataFormat(workbook.createDataFormat().getFormat("#,###,###,###,###,###,###"));
        }

        if(cellType.equals("Normal")){
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setTopBorderColor(HSSFColor.BLACK.index);
            style.setBottomBorderColor(HSSFColor.BLACK.index);
            style.setLeftBorderColor(HSSFColor.BLACK.index);
            style.setRightBorderColor(HSSFColor.BLACK.index);
        }else if(cellType.equals("Header")){
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setTopBorderColor(HSSFColor.BLACK.index);
            style.setBottomBorderColor(HSSFColor.BLACK.index);
            style.setLeftBorderColor(HSSFColor.BLACK.index);
            style.setRightBorderColor(HSSFColor.BLACK.index);
            style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            style.setFont(font);
        }else if(cellType.equals("Header2")){
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setTopBorderColor(HSSFColor.BLACK.index);
            style.setBottomBorderColor(HSSFColor.BLACK.index);
            style.setLeftBorderColor(HSSFColor.BLACK.index);
            style.setRightBorderColor(HSSFColor.BLACK.index);
            style.setFillForegroundColor(HSSFColor.RED.index);
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            style.setFont(font);
        }else if(cellType.equals("Header3")){
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setTopBorderColor(HSSFColor.BLACK.index);
            style.setBottomBorderColor(HSSFColor.BLACK.index);
            style.setLeftBorderColor(HSSFColor.BLACK.index);
            style.setRightBorderColor(HSSFColor.BLACK.index);
            style.setFillForegroundColor(HSSFColor.YELLOW.index);
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            style.setFont(font);
        }else if(cellType.equals("Title")){
            font.setFontHeight((short)300);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            style.setFont(font);
        }else if(cellType.equals("Chapter")){
            font.setFontHeight((short)250);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            style.setFont(font);
        }else if(cellType.equals("Notice")){
            font.setColor(HSSFColor.RED.index);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            style.setFont(font);
        }else if(cellType.equals("D") || cellType.equals("DD")){
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setTopBorderColor(HSSFColor.BLACK.index);
            style.setBottomBorderColor(HSSFColor.BLACK.index);
            style.setLeftBorderColor(HSSFColor.BLACK.index);
            style.setRightBorderColor(HSSFColor.BLACK.index);
            style.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            style.setFont(font);
        }else if(cellType.equals("I")|| cellType.equals("DI")){
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setTopBorderColor(HSSFColor.BLACK.index);
            style.setBottomBorderColor(HSSFColor.BLACK.index);
            style.setLeftBorderColor(HSSFColor.BLACK.index);
            style.setRightBorderColor(HSSFColor.BLACK.index);
            style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            style.setFont(font);
        }else if(cellType.equals("R") || cellType.equals("DR")){
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setTopBorderColor(HSSFColor.BLACK.index);
            style.setBottomBorderColor(HSSFColor.BLACK.index);
            style.setLeftBorderColor(HSSFColor.BLACK.index);
            style.setRightBorderColor(HSSFColor.BLACK.index);
            style.setFillForegroundColor(HSSFColor.LIME.index);
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            style.setFont(font);
        }

        return style;

    }


    public static void downloadFile(HttpServletResponse response, String serverFilePath, String downloadFileName) throws Exception{

    	// 자산시스템 암호화 모듈 라이센스 문제로 암호화 모듈 주석
        //String retFileName = SecureFileUtil.encFile(serverFilePath);
        //File f = new File( retFileName);

    	//엑셀 암호화
    	SLDsFile sFile = new SLDsFile();

    	sFile.SettingPathForProperty("/home/tomadm/softcamp/02_Module/02_ServiceLinker/softcamp.properties");

		sFile.SLDsInitDAC();
		sFile.SLDsAddUserDAC("SECURITYDOMAIN", "111001100", 0, 0, 0);

		String srcFile = serverFilePath+".xls";       //기존파일
		String dstFile = serverFilePath +"_ENC.xls";  //암호화파일

		int ret;
		ret = sFile.SLDsEncFileDACV2("/home/tomadm/softcamp/04_KeyFile/keyDAC_SVR0.sc", "system", srcFile, dstFile, 1);

		System.out.println("###############################");
		System.out.println(srcFile);
		System.out.println("SLDsEncFileDAC :" + ret);
		// 로컬에서 파일 암호화 실패 시
		if (ret == 3000) {
			dstFile = srcFile;
		}
		System.out.println("###############################");

    	//보안취약점점검(20190320)
    	//serverFilePath = StringUtil.fileDownloadPathReplace(serverFilePath);
		serverFilePath = StringUtil.fileDownloadPathReplace(dstFile);
    	String basePath = EgovProperties.getProperty("File.UploadPath");
    	if (!serverFilePath.startsWith(basePath)) {
			return;
		}

    	System.out.println("===================>"+serverFilePath);

        File f = new File(serverFilePath);
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        byte b[] = new byte[4096];
        int read = 0;

        if(f.isFile()){

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(downloadFileName, "UTF-8") + ";");
            response.setHeader("Content-Transfer-Encoding", "binary;");
            response.setHeader("Pragma", "no-cache;");
            response.setHeader("Expires", "-1;");
            response.setContentLength((int)f.length());//size setting

            try{

                bis = new BufferedInputStream(new FileInputStream(f));
                bos = new BufferedOutputStream(response.getOutputStream());

                while((read = bis.read(b)) != -1){
                    bos.write(b, 0, read);
                }
                bis.close();
                bos.close();

            }catch(Exception e){
                throw new EgovBizException("엑셀데이터 세팅 중 에러가 발생하였습니다.");
            }finally{
                if(bis != null){
                    bis.close();
                }
                f.delete();
            }

        }else{

            throw new EgovBizException("파일이 잘못 되었습니다.."+f.getName());

        }

    }

    public static void downloadTemplateFile(HttpServletResponse response, String fileName) throws Exception{

        String downloadFileName = fileName + HnsConst.DOT_XLSX;

        System.out.println("downloadFileName== " + downloadFileName);
        String fileFullPathNm = EgovProperties.getProperty( HnsConst.FILE_SAMPLE_PATH) + downloadFileName ;
        System.out.println("fileFullPathNm==== " + fileFullPathNm); //C:/eGovFrameHS/workspace/hnsehr/form

    	//보안취약점점검(20190320)
        fileFullPathNm = StringUtil.fileDownloadPathReplace(fileFullPathNm);
        String basePath = EgovProperties.getProperty("File.UploadPath");
        if (!fileFullPathNm.startsWith(basePath)) {
			return;
		}

        File f = new File(fileFullPathNm);
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        byte b[] = new byte[4096];
        int read = 0;

        if(f.isFile()){

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(downloadFileName, "UTF-8") + ";");
            response.setHeader("Content-Transfer-Encoding", "binary;");
            response.setHeader("Pragma", "no-cache;");
            response.setHeader("Expires", "-1;");
            response.setContentLength((int)f.length());//size setting

            try{

                bis = new BufferedInputStream(new FileInputStream(f));
                bos = new BufferedOutputStream(response.getOutputStream());

                while((read = bis.read(b)) != -1){
                    bos.write(b, 0, read);
                }
                bis.close();
                bos.close();

            }catch(Exception e){
                throw new EgovBizException("엑셀데이터 세팅 중 에러가 발생하였습니다.");
            }finally{
                if(bis != null){
                    bis.close();
                }
            }

        }else{

            throw new EgovBizException("파일이 잘못 되었습니다..");

        }

    }

    public static Map<String, Object> readFile(InputStream fileIn, boolean xss, int startReadRow) throws Exception{

        Map<String, Object> map = new HashMap<String, Object>();
        List<EgovMap> list = new ArrayList<EgovMap>();
        EgovMap data = null;
        POIFSFileSystem fs = null;
        XSSFWorkbook xworkbook = null;
        HSSFWorkbook workbook = null;
        Sheet sheet = null;

        if(xss){
            xworkbook = new XSSFWorkbook(fileIn);
            sheet = xworkbook.getSheetAt(0);
        }else{
            fs = new POIFSFileSystem(fileIn);
            workbook = new HSSFWorkbook(fs);
            sheet = workbook.getSheetAt(0);
        }


        Row row = null;
        Cell cell = null;
        int rowCnt = sheet.getPhysicalNumberOfRows();
        int colCnt = 0;

/*        int maxRow = 10000;
        if(rowCnt > maxRow){
        	throw new EgovBizException( "excel 파일은 10,000행 이하로만 업로드가 가능합니다.");
        }*/

        for(int i=startReadRow;i<rowCnt;i++){

            row = sheet.getRow(i);
            colCnt = row.getLastCellNum();
            data = new EgovMap();

            for(int j=0;j<colCnt;j++){

                cell = row.getCell(j);
                if(cell != null){

                    switch(cell.getCellType()){

                        case HSSFCell.CELL_TYPE_FORMULA :
                            data.put("column" + j, new Double(cell.getNumericCellValue()).toString());
                            break;
                        case HSSFCell.CELL_TYPE_NUMERIC :
                             if(HSSFDateUtil.isCellDateFormatted(cell)){
                                 SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                                 data.put("column" + j, sf.format(cell.getDateCellValue()));
                              } else {
                                 data.put("column" + j, (int)cell.getNumericCellValue());
                              }


                            break;
                        case HSSFCell.CELL_TYPE_STRING :
                            data.put("column" + j, cell.getStringCellValue());
                            break;
                        case HSSFCell.CELL_TYPE_BLANK :
                            data.put("column" + j, cell.getStringCellValue());
                            break;
                        case HSSFCell.CELL_TYPE_ERROR :
                            data.put("column" + j, cell.getStringCellValue());
                            break;
                        default :
                            break;

                    }

                }

            }

            list.add(data);

        }

        map.put("uploadData", list);

        return map;

    }




    public static Map<String, Object> readFileAbu(InputStream fileIn, boolean xss, int startReadRow) throws Exception{

        Map<String, Object> map = new HashMap<String, Object>();
        List<EgovMap> list = new ArrayList<EgovMap>();
        EgovMap data = null;
        POIFSFileSystem fs = null;
        XSSFWorkbook xworkbook = null;
        HSSFWorkbook workbook = null;
        Sheet sheet = null;

        if(xss){
            xworkbook = new XSSFWorkbook(fileIn);
            sheet = xworkbook.getSheetAt(0);
        }else{
            fs = new POIFSFileSystem(fileIn);
            workbook = new HSSFWorkbook(fs);
            sheet = workbook.getSheetAt(0);
        }


        Row row = null;
        Cell cell = null;
        int rowCnt = sheet.getPhysicalNumberOfRows();
        int colCnt = 0;

        for(int i=startReadRow;i<rowCnt;i++){

            row = sheet.getRow(i);
            colCnt = row.getLastCellNum();
            data = new EgovMap();

            for(int j=0;j<colCnt;j++){

                cell = row.getCell(j);
                if(cell != null){

                    switch(cell.getCellType()){

                        case HSSFCell.CELL_TYPE_FORMULA :
                            data.put("column" + j, new Double(cell.getNumericCellValue()).toString());
                            break;
                        case HSSFCell.CELL_TYPE_NUMERIC :
                             if(HSSFDateUtil.isCellDateFormatted(cell)){
                                 SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                                 data.put("column" + j, sf.format(cell.getDateCellValue()));
                              } else {
//                                 data.put("column" + j, cell.getStringCellValue());
                                 BigDecimal nCellValue = new BigDecimal(cell.getNumericCellValue());
                                 data.put("column" + j, nCellValue.toString());

                              }


                            break;
                        case HSSFCell.CELL_TYPE_STRING :
                            data.put("column" + j, cell.getStringCellValue());
                            break;
                        case HSSFCell.CELL_TYPE_BLANK :
                            data.put("column" + j, cell.getStringCellValue());
                            break;
                        case HSSFCell.CELL_TYPE_ERROR :
                            data.put("column" + j, cell.getStringCellValue());
                            break;
                        default :
                            break;

                    }

                }

            }

            list.add(data);

        }

        map.put("uploadData", list);

        return map;

    }

    /**
     * @Description : excel
     *
     * @param fileExt
     * @return xls,null :false, xlsx : true
     */
    public static boolean hssXssType(String fileExt) {
        if(StringUtil.isEmpty(fileExt)) return false;
        else {
            if(fileExt.toLowerCase().equals("xlsx")) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * @Description : excel upload after return data map
     *
     * @param HttpServletRequest
     * @return Map<String, Object>
     *
     * @see
     *  Map<String, Object> data = EgovExcelUtils.uploadFileInfo(request);
     */
    public static Map<String, Object> uploadFileInfo(HttpServletRequest request) throws Exception, IOException {

        final MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        final Map<String, MultipartFile> files = multipartHttpServletRequest.getFileMap();
        Iterator<Entry<String, MultipartFile>> itr = files.entrySet().iterator();
        //MultipartFile file = null;
        Entry<String, MultipartFile> entry = null;

        Map<String, Object> data = new HashMap<String, Object>();

        int startReadRow = StringUtils.isEmpty(multipartHttpServletRequest.getParameter("startReadRow")) ?   1 : Integer.parseInt(multipartHttpServletRequest.getParameter("startReadRow")) ;
        LoginVO loginVO = EgovUserDetailsHelper.isAuthenticated()? (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser():null;
        String strDir = EgovProperties.getProperty("File.Temp")+loginVO.getId() + "/";
        String fileName = EgovStringUtil.isNullToString(request.getAttribute("FILE_TEMP_ORG_NAME_ONE"));
        File file = new File(strDir+fileName);
        if (file.exists() == false) {
        	strDir = EgovProperties.getProperty("File.Temp");
        	file = new File(strDir+fileName);
        }

        data = EgovExcelUtils.getDataForEncExcelFile( file, startReadRow);

        file.delete();



        data.put("startReadRow", startReadRow);

        return data;
    }

    /**
     * 암호화된 엑셀파일을 읽어 data를 반환
     * @param file
     * @param startReadRow
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getDataForEncExcelFile( MultipartFile multipartFile, int startReadRow) throws Exception{

        long size = multipartFile.getSize();
        long maxSize = 3145728;
        if(size > maxSize){
        	throw new EgovBizException("업로드 파일 사이즈는 3MB 이하로만 등록이 가능합니다.");
        }

    	// 자산시스템 암호화 모듈 라이센스 문제로 암호화 모듈 주석
        //InputStream inputStream = SecureFileUtil.getDecFile(multipartFile);
        InputStream inputStream = multipartFile.getInputStream();


        return EgovExcelUtils.readFile( inputStream, StringUtil.hssXssType(FilenameUtils.getExtension( multipartFile.getOriginalFilename())), startReadRow);
    }

    /**
     * 암호화된 엑셀파일을 읽어 data를 반환
     * @param file
     * @param startReadRow
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getDataForEncExcelFile( File file, int startReadRow) throws Exception{

        long size = file.length();
        long maxSize = 3145728;
        if(size > maxSize){
        	throw new EgovBizException("업로드 파일 사이즈는 3MB 이하로만 등록이 가능합니다.");
        }

    	// 자산시스템 암호화 모듈 라이센스 문제로 암호화 모듈 주석
        //InputStream inputStream = SecureFileUtil.getDecFile(multipartFile);
        InputStream inputStream = new FileInputStream(file);


        return EgovExcelUtils.readFile( inputStream, StringUtil.hssXssType(FilenameUtils.getExtension( file.getName())), startReadRow);
    }

    /**
     * 여러개 시트 엑셀파일을 생성
     * @param pSheetName 시트이름 리스트  , pHeaderData 데이터 헤드 시트별 리스트,pListData 시트별 데이터 리스트
     * @param
     * @return
     * @throws Exception
     */
    public static boolean makeMuitiSheetExcelFile(String saveFilePath, List<String> pSheetName, List<List<String>> pHeaderData, List<List<EgovMap>> pListData, String checkdir) throws EgovBizException{

        boolean resultFlag = true;

        //보안취약점점검(20190320)
        saveFilePath = StringUtil.filePathReplace(saveFilePath);
        saveFilePath = saveFilePath + ".xls";

        try{
            File cFile = new File(EgovWebUtil.filePathBlackList(checkdir));

            if (!cFile.isDirectory())
            cFile.mkdir();

            HSSFWorkbook workbook = new HSSFWorkbook();

            int iSheetCnt = pSheetName.size();

            for(int iSht=0;iSht<iSheetCnt;iSht++){
            	String sSheetName = pSheetName.get(iSht);
            	List<String> headerData = pHeaderData.get(iSht);
            	List<EgovMap> listData = pListData.get(iSht);

                Sheet sheet = workbook.createSheet(sSheetName);


                EgovMap dataMap = null;
                String header [] = new String[headerData.size()];
                int size [] = new int[headerData.size()];
                String align [] = new String[headerData.size()];
                String key [] = new String[headerData.size()];
                String temp [] = null;
                int rowIndex = 0;

                Row row = null;
                Cell cell = null;

                // Column Width
                for(int i=0;i<headerData.size();i++){
                    temp = headerData.get(i).split("@");
                    header[i] = temp[0];
                    align[i] = temp[1];
                    size[i] = Integer.parseInt(temp[2]);
                    key[i] = temp[3];
                    sheet.setColumnWidth(i, size[i]);
                }

                // Row
                for(int i=0;i<=listData.size()+1;i++){
                    row = sheet.createRow(i);
                    // Cell
                    for(int j=0;j<headerData.size();j++){
                        cell = row.createCell(j);
                    }
                }

//                // 타이틀
//                row = sheet.getRow(rowIndex++);
//                cell = row.getCell(0);
//                row.setHeight((short)700);
//                cell.setCellStyle(getStyle(workbook, "Title", "C"));
//                cell.setCellValue(new HSSFRichTextString(title));
//                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headerData.size() - 1));

                // 헤더
                row = sheet.getRow(rowIndex++);
                row.setHeight((short)350);
                for(int i=0;i<header.length;i++){
                    cell = row.getCell(i);
                    if(align[i].equals("RED")){
                        cell.setCellStyle(getStyle(workbook, "Header2", "C"));
                    }else if(align[i].equals("YELLOW")){
                        cell.setCellStyle(getStyle(workbook, "Header3", "C"));
                    }else{
                        cell.setCellStyle(getStyle(workbook, "Header", "C"));
                    }
                    cell.setCellValue(new HSSFRichTextString(header[i]));
                }

                // 데이터
                for(int i=0;i<listData.size();i++){
                    dataMap = listData.get(i);

                    row = sheet.getRow(rowIndex++);
                    row.setHeight((short)300);
                    for(int j=0;j<key.length;j++){
                    	  cell = row.getCell(j);
                    	  Object obj = MapUtils.getObject( dataMap, key[j]);
                    	  if( obj instanceof java.lang.Long) {
                    		  cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                    		  cell.setCellValue(((Long)obj).longValue());
                    		  continue;
                    	  }

                    	  if( obj instanceof java.lang.Double) {
                    		  cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                    		  cell.setCellValue(((Double)obj).doubleValue());
                    		  continue;
                    	  }

                          String data = (String)dataMap.get(key[j]);
                          if(StringUtils.isNumeric(data) && data.length() > 0 ){
//                          	double d = Double.parseDouble(data);
//                          	cell.setCellType(Cell.CELL_TYPE_NUMERIC);
//                            cell.setCellValue(d);

                        	  cell.setCellValue(new HSSFRichTextString(data));
                          }else{
                          	cell.setCellValue(new HSSFRichTextString(data));
                          }
                    }
                }

            }

            FileOutputStream fos = new FileOutputStream(saveFilePath);
            workbook.write(fos);
            fos.close();

        }catch(Exception e){
        	e.printStackTrace();
            throw new EgovBizException("makeExcelFile중 에러가 발생하였습니다.");
        }

        return resultFlag;

    }
    /**
     * 여러개 시트 엑셀파일을 생성
     * @param pSheetName 시트이름 리스트  , pHeaderData 데이터 헤드 시트별 리스트,pListData 시트별 데이터 리스트
     * @param
     * @return
     * @throws Exception
     */
    public static boolean makeMuitiSheetExcelFileApplyStyle(String excelTitle,String sHeaderType,String saveFilePath, List<String> pSheetName, List<List<String>> pHeaderData, List<List<EgovMap>> pListData, String checkdir) throws EgovBizException{

        boolean resultFlag = true;

        try{
        	saveFilePath = saveFilePath + ".xls";
            File cFile = new File(EgovWebUtil.filePathBlackList(checkdir));

            if (!cFile.isDirectory())
            cFile.mkdir();

            HSSFWorkbook workbook = new HSSFWorkbook();

            int iSheetCnt = pSheetName.size();

            for(int iSht=0;iSht<iSheetCnt;iSht++){
            	String sSheetName = pSheetName.get(iSht);
            	List<String> headerData = pHeaderData.get(iSht);
            	List<EgovMap> listData = pListData.get(iSht);

                Sheet sheet = workbook.createSheet(sSheetName);


                EgovMap dataMap = null;
                String header [] = new String[headerData.size()];
                int size [] = new int[headerData.size()];
                String align [] = new String[headerData.size()];
                String key [] = new String[headerData.size()];
                String style [] = new String[headerData.size()];
                String dataStyle [] = new String[headerData.size()];
                CellStyle dataCellStyleStyle [] = new CellStyle[headerData.size()];
                String temp [] = null;
                int rowIndex = 0;

                Row row = null;
                Cell cell = null;

                // Column Width
                for(int i=0;i<headerData.size();i++){
                    temp = headerData.get(i).split("@");
                    header[i] = temp[0];
                    align[i] = temp[1];
                    size[i] = Integer.parseInt(temp[2]);
                    key[i] = temp[3];
                    style[i] = temp[4];
                    dataStyle[i] = temp[5];
                    sheet.setColumnWidth(i, size[i]);
                }


                int iOff = 0;
                //특수 경우에 헤더라인 위에 구분 표시
                if(!"".equals(sHeaderType)){
                	iOff = 1;
                }

                // Row
                for(int i=0;i<=listData.size()+1+iOff;i++){
                    row = sheet.createRow(i);
                    // Cell
                    for(int j=0;j<headerData.size();j++){
                        cell = row.createCell(j);
                    }
                }

//                // 타이틀
//                row = sheet.getRow(rowIndex++);
//                cell = row.getCell(0);
//                row.setHeight((short)700);
//                cell.setCellStyle(getStyle(workbook, "Title", "C"));
//                cell.setCellValue(new HSSFRichTextString(title));
//                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headerData.size() - 1));
                //특수 경우에 헤더라인 위에 구분 표시
                if(iSht ==0 && "H1".equals(sHeaderType)){
                    row = sheet.getRow(rowIndex++);
                    row.setHeight((short)1800);
                    sheet.addMergedRegion(new CellRangeAddress(0,0,0,4));
                    cell = row.getCell(0);
                    CellStyle cs = workbook.createCellStyle();
                    cs.setWrapText(true);
                    cs.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
                    StringBuffer sb = new StringBuffer();
                    sb.append("참고사항.\r\n");
                    sb.append("1.참고 사항 행을 삭제 하지 마세요.\r\n");
                    sb.append("2.노란색은 마스터 데이터 입니다. 변경 하지 마세요.\r\n");
                    sb.append("3.연두색은 필수 입력대상입니다.\r\n");
                    sb.append("4.회색은 선택 입력대상입니다.\r\n");
                    sb.append("5.날짜는 2019-10-10 또는 20191010 양식으로 입력하세요.\r\n");
                    sb.append("6.데이터 입력 후 테두리 지정 필수입니다. 테두리 없는 경우 오류 발생할 수 있습니다.");
                    cell.setCellValue(new HSSFRichTextString(sb.toString()));
                    cell.setCellStyle(cs);
                }else if("H2".equals(sHeaderType)){
                    row = sheet.getRow(rowIndex++);
                    row.setHeight((short)1300);
                    sheet.addMergedRegion(new CellRangeAddress(0,0,0,4));
                    cell = row.getCell(0);
                    CellStyle cs = workbook.createCellStyle();
                    cs.setWrapText(true);
                    cs.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
                    StringBuffer sb = new StringBuffer();
                    sb.append("참고사항.\r\n");
                    sb.append("1.참고 사항 행을 삭제 하지 마세요.\r\n");
                    sb.append("2.연두색은 필수 입력대상입니다.\r\n");
                    sb.append("3.회색은 선택 입력대상입니다.");
                    cell.setCellValue(new HSSFRichTextString(sb.toString()));
                    cell.setCellStyle(cs);
	            }else if("H3".equals(sHeaderType)){
	            	row = sheet.getRow(rowIndex++);
	            	row.setHeight((short)1600);
	            	sheet.addMergedRegion(new CellRangeAddress(0,0,0,4));
	            	cell = row.getCell(0);
	            	CellStyle cs = workbook.createCellStyle();
	            	cs.setWrapText(true);
	            	cs.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
	            	StringBuffer sb = new StringBuffer();
	            	sb.append("참고사항.\r\n");
	            	sb.append("1.참고 사항 행을 삭제 하지 마세요.\r\n");
	            	sb.append("2.연두색은 필수 입력대상입니다.\r\n");
	            	sb.append("3.회색은 선택 입력대상입니다.\r\n");
	            	sb.append("4-1.발급구분  미배부_지역본부(14) : 이동후 본부는 필수 입력대상입니다.\r\n");
	            	sb.append("4-2.발급구분  미배부_지점/팀(11) : 이동후 본부, 이동후 지점 필수 입력대상입니다.\r\n");
	            	sb.append("4-3.발급구분  배부/정지(12/13) : 이동후 본부, 이동후 지점, 이동후 사용자 필수 입력대상입니다.");

	            	cell.setCellValue(new HSSFRichTextString(sb.toString()));
	            	cell.setCellStyle(cs);
	            }
                // 헤더
                row = sheet.getRow(rowIndex++);
                row.setHeight((short)450);
                for(int i=0;i<header.length;i++){
                    cell = row.getCell(i);
                    if(!" ".equals(style[i])){
                        cell.setCellStyle(getStyle(workbook, style[i], "C"));
                    }else{
                        cell.setCellStyle(getStyle(workbook, "Header", "C"));
                    }
                    cell.setCellValue(new HSSFRichTextString(header[i]));
                }
                //데이터 스타일
                for(int i=iOff+1;i<=listData.size()+1;i++){
                    row = sheet.getRow(i);
                    // Cell
                    for(int j=0;j<headerData.size();j++){
                    	  cell = row.getCell(j);
                        if(!" ".equals(dataStyle[j])){
                        	if(i== iOff+1){
                                dataCellStyleStyle[j] = getStyle(workbook, dataStyle[j], "L");
                        	}
                            cell.setCellStyle(dataCellStyleStyle[j]);
                        }
                    }
                }

                // 데이터
                for(int i=0;i<listData.size();i++){
                    dataMap = listData.get(i);

                    row = sheet.getRow(rowIndex++);
                    row.setHeight((short)300);
                    for(int j=0;j<key.length;j++){
                    	  cell = row.getCell(j);
                    	  Object obj = MapUtils.getObject( dataMap, key[j]);
                    	  if( obj instanceof java.lang.Long) {
                    		  cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                    		  cell.setCellValue(((Long)obj).longValue());
                    		  continue;
                    	  }

                    	  if( obj instanceof java.lang.Double) {
                    		  cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                    		  cell.setCellValue(((Double)obj).doubleValue());
                    		  continue;
                    	  }

                          String data = (String)dataMap.get(key[j]);
                          if(StringUtils.isNumeric(data) && data.length() > 0 ){
//                          	double d = Double.parseDouble(data);
//                          	cell.setCellType(Cell.CELL_TYPE_NUMERIC);
//                            cell.setCellValue(d);

                        	  cell.setCellValue(new HSSFRichTextString(data));
                          }else{
                          	cell.setCellValue(new HSSFRichTextString(data));
                          }
                    }
                }

            }

            FileOutputStream fos = new FileOutputStream(saveFilePath);
            workbook.write(fos);
            fos.close();

        }catch(Exception e){
        	e.printStackTrace();
            throw new EgovBizException("makeExcelFile중 에러가 발생하였습니다.");
        }

        return resultFlag;

    }

    public static boolean makeInspectionExcelFile(String saveFilePath, String title, List<String> headerData, List<EgovMap> listData, String checkdir) throws EgovBizException{

        boolean resultFlag = true;

        //보안취약점점검(20190320)
        saveFilePath = StringUtil.filePathReplace(saveFilePath);
        saveFilePath = saveFilePath + ".xls";
        try{
            File cFile = new File(EgovWebUtil.filePathBlackList(checkdir));

            if (!cFile.isDirectory())
            cFile.mkdir();

            HSSFWorkbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet("sheet1");
            EgovMap dataMap = null;
            String header [] = new String[headerData.size()];
            int size [] = new int[headerData.size()];
            String align [] = new String[headerData.size()];
            String key [] = new String[headerData.size()];
            String temp [] = null;
            int rowIndex = 0;

            Row row = null;
            Cell cell = null;

            // Column Width
            for(int i=0;i<headerData.size();i++){
                temp = headerData.get(i).split("@");
                header[i] = temp[0];
                align[i] = temp[1];
                size[i] = Integer.parseInt(temp[2]);
                key[i] = temp[3];
                sheet.setColumnWidth(i, size[i]);
            }

            // Row
            for(int i=0;i<=listData.size()+1;i++){
                row = sheet.createRow(i);
                // Cell
                for(int j=0;j<headerData.size();j++){
                    cell = row.createCell(j);
                }
            }

            row = sheet.getRow(rowIndex++);
            row.setHeight((short)1300);
            sheet.addMergedRegion(new CellRangeAddress(0,0,0,4));
            cell = row.getCell(0);
            CellStyle cs = workbook.createCellStyle();
            cs.setWrapText(true);
            cs.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
            StringBuffer sb = new StringBuffer();
            sb.append("※ 참고사항 ※\r\n");
            sb.append("1.회색은 수정불가 대상입니다.\r\n");
            sb.append("2.노란색은 필수입력 대상입니다.\r\n");
            cell.setCellValue(new HSSFRichTextString(sb.toString()));
            cell.setCellStyle(cs);

            // 헤더
            row = sheet.getRow(rowIndex++);
            row.setHeight((short)300);
            for(int i=0;i<header.length;i++){
                cell = row.getCell(i);
                if(align[i].equals("RED")){
                    cell.setCellStyle(getStyle(workbook, "Header2", "C"));
                }else if(align[i].equals("YELLOW")){
                    cell.setCellStyle(getStyle(workbook, "Header3", "C"));
                }else{
                    cell.setCellStyle(getStyle(workbook, "Header", "C"));
                }
                cell.setCellValue(new HSSFRichTextString(header[i]));
            }

            // 데이터
            for(int i=0;i<listData.size();i++){
                dataMap = listData.get(i);

                row = sheet.getRow(rowIndex++);
                row.setHeight((short)250);
                for(int j=0;j<key.length;j++){
                	  cell = row.getCell(j);
                	  Object obj = MapUtils.getObject( dataMap, key[j]);
                	  if( obj instanceof java.lang.Long) {
                		  cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                		  cell.setCellValue(((Long)obj).longValue());
                		  continue;
                	  }

                	  if( obj instanceof java.lang.Double) {
                		  cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                		  cell.setCellValue(((Double)obj).doubleValue());
                		  continue;
                	  }

                      String data = (String)dataMap.get(key[j]);
                      if(StringUtils.isNumeric(data) && data.length() > 0 ){
//                      	double d = Double.parseDouble(data);
//                      	cell.setCellType(Cell.CELL_TYPE_NUMERIC);
//                        cell.setCellValue(d);

                    	  cell.setCellValue(new HSSFRichTextString(data));
                      }else{
                      	cell.setCellValue(new HSSFRichTextString(data));
                      }
                }
            }

            FileOutputStream fos = new FileOutputStream(saveFilePath);
            workbook.write(fos);
            fos.close();

        }catch(Exception e){
            throw new EgovBizException("makeExcelFile중 에러가 발생하였습니다.");
        }

        return resultFlag;

    }

}
