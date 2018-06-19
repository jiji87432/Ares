/*
 * 文 件 名:  ExcelFileUtils.java
 * 版    权:  支付有限公司. Copyright 2011-2014,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  tao.zhang
 * 修改时间:  2014-10-29
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.pay.risk.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.pay.common.util.StringUtil;
import com.pay.risk.Constant;

/**
 * Excel文件解析服务工具类
 * <p>
 * @author tao.zhang
 * @version [V1.0, 2014-10-29]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ExcelFileUtils {
    /***/
    protected static final Logger logger = LogManager.getLogger(ExcelFileUtils.class);

    /**
     * 根据输入Excel文件解析出Excel文件中第一个sheet页。
     * @param ExcelFile
     * @return Sheet
     */
    public static Sheet loadExcelSheet(final MultipartFile excelFile) {
        if (excelFile != null && !StringUtil.isNull(excelFile.getName())) {
            String fileName = excelFile.getOriginalFilename();
            if (fileName.lastIndexOf(".") != 0 && fileName.lastIndexOf(".") != -1) {
                String suffix = fileName.substring(fileName.lastIndexOf("."));
                if (Constant.EXCEL_SUFFIX_XLSX.equalsIgnoreCase(suffix) || Constant.EXCEL_SUFFIX_XLS.equalsIgnoreCase(suffix)) {
                    FileInputStream fileInputStream = null;
                    try {
                        fileInputStream = (FileInputStream) excelFile.getInputStream();
                        Workbook workBook = null;
                        if (Constant.EXCEL_SUFFIX_XLSX.equalsIgnoreCase(suffix)) {
                            workBook = new XSSFWorkbook(fileInputStream);
                        } else {
                            workBook = new HSSFWorkbook(fileInputStream);
                        }
                        Sheet sheet = workBook.getSheetAt(0);
                        return sheet;
                    } catch (Exception e) {
                        logger.error("Excel文件解析异常！");
                        e.printStackTrace();
                    } finally {
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                logger.error("Excel文件读取数据流关闭出错！");
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取对应Excel的Sheet页中所有记录Row中每个Cell的值。
     * <p>
     * @param Sheet
     * @return List - 记录Row数据集 , ArrayList - 记录Row中Cell数据集
     */
    public static List<ArrayList<String>> loadExcelRowsValues(final Sheet sheet) {
        /*
         * Sheet 页中存在非标题数据记录Row
         */
        if (sheet != null && sheet.getLastRowNum() >= 1) {
            logger.info("sheet last row num = [" + sheet.getLastRowNum() + "]");
            /*
             * 从非标题行开始解析每一记录Row中数据。
             */
            /* excel sheet 页中 行值 */
            List<ArrayList<String>> rowsValues = new LinkedList<ArrayList<String>>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                /*
                 * 记录Row中存在数据Cell
                 */
                if (row != null && row.getLastCellNum() > 0) {
                    logger.info("row last cell num = [" + row.getLastCellNum() + "]");
                    /* 单元格值集 */
                    ArrayList<String> cellsValues = new ArrayList<String>();
                    /*
                     * 逐个字段域解析记录Row中每个字段域中值
                     */
                    for (int j = 0; j < row.getLastCellNum(); j++) {
                        if (row.getCell(j) != null) {
                            row.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
                            String cellValue = row.getCell(j).getStringCellValue().trim();
                            cellsValues.add(StringUtil.isNull(cellValue) ? Constant.EXCEL_CELL_VALUE_NULL : cellValue);
                        } else {
                            cellsValues.add(Constant.EXCEL_CELL_VALUE_NULL);
                        }
                    }
                    rowsValues.add(cellsValues);
                }
            }
            return rowsValues;
        }
        return null;
    }

    /**
     * 解析Excel文件获取解析结果。
     * <p>
     * 每个Record的记录值集。
     */
    public static List<ArrayList<String>> getExcelCellsValue(final MultipartFile excelFile) {
        if (excelFile != null) {
            Sheet sheet = loadExcelSheet(excelFile);
            if (sheet != null && sheet.getLastRowNum() >= 1) {
                List<ArrayList<String>> rowsValues = loadExcelRowsValues(sheet);
                if (rowsValues != null && !rowsValues.isEmpty()) {
                    return rowsValues;
                }
            }
        }
        return null;
    }
}
