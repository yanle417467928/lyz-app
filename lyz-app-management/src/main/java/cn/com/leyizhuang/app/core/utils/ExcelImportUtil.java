package cn.com.leyizhuang.app.core.utils;

import cn.com.leyizhuang.app.foundation.pojo.FitOrderExcel;
import cn.com.leyizhuang.app.foundation.pojo.FitOrderExcelModel;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: ${todo}
 * @Author Richard
 * @Date 2018/4/1610:53
 */
public class ExcelImportUtil {

    /**
     * 读取excel中的数据
     *
     * @param file 文件
     * @return List<StudentBean>
     * @author zhang 2015-08-18 00:08
     */
    public static FitOrderExcel readFitOrderExcel(MultipartFile file) {
        String path = file.getOriginalFilename();
        if (path != null && !path.equals("")) {
            String ext = getExt(path);
            if (ext != null && !ext.equals("")) {
                if (ext.equals("xls")) {
                    return readXls(file);
                } else if (ext.equals("xlsx")) {
                    return readXlsx(file);
                }
            }
        }
        return new FitOrderExcel();
    }

    /**
     * 读取后缀为xls的excel文件的数据
     *
     * @param file 文件
     * @return List<StudentBean>
     * @author zhang 2015-08-18 00:10
     */
    private static FitOrderExcel readXls(MultipartFile file) {
        FitOrderExcel fitOrderExcel = new FitOrderExcel();
        HSSFWorkbook hssfWorkbook = null;
        try {
            InputStream is = file.getInputStream();
            hssfWorkbook = new HSSFWorkbook(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FitOrderExcelModel excelModel = null;
        List<FitOrderExcelModel> list = new ArrayList<>();
        if (hssfWorkbook != null) {
            // Read the Sheet
            for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
                HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
                if (hssfSheet == null) {
                    continue;
                }
                //read first row
                HSSFCell firstRowCell1 = hssfSheet.getRow(0).getCell(0);
                String remark = getValue(firstRowCell1);
                fitOrderExcel.setRemark(remark);
                // Read the Row
                for (int rowNum = 2; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                    HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                    if (hssfRow != null) {
                        excelModel = new FitOrderExcelModel();
                        HSSFCell externalCode = hssfRow.getCell(0);
                        HSSFCell qty = hssfRow.getCell(1);
                        HSSFCell internalCode = hssfRow.getCell(2);
                        HSSFCell internalName = hssfRow.getCell(3);
                        if (StringUtils.isNotBlank(getValue(externalCode))) {
                            excelModel.setExternalCode(getValue(externalCode));
                        }
                        if (StringUtils.isNotBlank(getValue(qty))) {
                            excelModel.setQty(Double.valueOf(getValue(qty)).intValue());
                        }
                        if (StringUtils.isNotBlank(getValue(internalCode))) {
                            excelModel.setInternalCode(getValue(internalCode));
                        }
                        if (StringUtils.isNotBlank(getValue(internalName))) {
                            excelModel.setInternalName(getValue(internalName));
                        }
                        if (null != excelModel.getExternalCode() && null != excelModel.getQty()) {
                            list.add(excelModel);
                        }
                    }
                }
            }
        }
        fitOrderExcel.setExcelModelList(list);
        return fitOrderExcel;
    }

    /**
     * 读取后缀为xlsx的excel文件的数据
     *
     * @param file 文件
     * @return List<StudentBean>
     * @author zhang 2015-08-18 00:08
     */
    private static FitOrderExcel readXlsx(MultipartFile file) {

        FitOrderExcel fitOrderExcel = new FitOrderExcel();
        XSSFWorkbook xssfWorkbook = null;
        try {
            InputStream is = file.getInputStream();
            xssfWorkbook = new XSSFWorkbook(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FitOrderExcelModel excelModel = null;
        List<FitOrderExcelModel> list = new ArrayList<FitOrderExcelModel>();
        if (xssfWorkbook != null) {
            // Read the Sheet
            for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
                XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
                if (xssfSheet == null) {
                    continue;
                }
                //read first row
                XSSFCell firstRowCell1 = xssfSheet.getRow(0).getCell(0);
                String remark = getValue(firstRowCell1);
                fitOrderExcel.setRemark(remark);
                // Read the Row
                for (int rowNum = 2; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                    XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                    if (xssfRow != null) {
                        excelModel = new FitOrderExcelModel();
                        XSSFCell externalCode = xssfRow.getCell(0);
                        XSSFCell qty = xssfRow.getCell(1);
                        XSSFCell internalCode = xssfRow.getCell(2);
                        XSSFCell internalName = xssfRow.getCell(3);
                        if (StringUtils.isNotBlank(getValue(externalCode))) {
                            excelModel.setExternalCode(getValue(externalCode));
                        }
                        if (StringUtils.isNotBlank(getValue(qty))) {
                            excelModel.setQty(Double.valueOf(getValue(qty)).intValue());
                        }
                        if (StringUtils.isNotBlank(getValue(internalCode))) {
                            excelModel.setInternalCode(getValue(internalCode));
                        }
                        if (StringUtils.isNotBlank(getValue(internalName))) {
                            excelModel.setInternalName(getValue(internalName));
                        }
                        if (null != excelModel.getExternalCode() && null != excelModel.getQty()) {
                            list.add(excelModel);
                        }
                    }
                }
            }
        }
        fitOrderExcel.setExcelModelList(list);
        return fitOrderExcel;
    }

    /**
     * 获取文件扩展名
     *
     * @param path 文件路径
     * @return String
     * @author zhang 2015-08-17 23:26
     */
    private static String getExt(String path) {
        if (path == null || path.equals("") || !path.contains(".")) {
            return null;
        } else {
            return path.substring(path.lastIndexOf(".") + 1, path.length());
        }
    }


    /**
     * 判断后缀为xlsx的excel文件的数据类型
     *
     * @param xssfRow
     * @return String
     * @author zhang 2015-08-18 00:12
     */
    @SuppressWarnings("static-access")
    private static String getValue(XSSFCell xssfRow) {
        if (xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN) {
            return String.valueOf(xssfRow.getBooleanCellValue());
        } else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
            return String.valueOf(xssfRow.getNumericCellValue());
        } else {
            return String.valueOf(xssfRow.getStringCellValue());
        }
    }

    /**
     * 判断后缀为xls的excel文件的数据类型
     *
     * @param hssfCell
     * @return String
     * @author zhang 2015-08-18 00:12
     */
    @SuppressWarnings("static-access")
    private static String getValue(HSSFCell hssfCell) {
        if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
            return String.valueOf(hssfCell.getNumericCellValue());
        } else {
            return String.valueOf(hssfCell.getStringCellValue());
        }
    }

}
