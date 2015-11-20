
package com.hellboy.core;

import com.hellboy.entity.Money;
import com.hellboy.entity.MoneyFlow;
import com.hellboy.entity.Stock;
import org.apache.poi.hssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by hellboy on 2015/10/5.
 */
public class PoiExcel {

    public static void exportExcel(List<Stock> data ,String name,String path,String  time) {
        //第一步创建workbook
        HSSFWorkbook wb = new HSSFWorkbook();
        //第二步创建sheet
        HSSFSheet sheet = wb.createSheet(name);
        sheet.createFreezePane( 0, 1);
        sheet.setDefaultColumnWidth(12);
        //第三步创建行row:添加表头0行
        HSSFRow row = sheet.createRow(0);
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  //居中

        //第四步创建单元格
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("时间");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("名称");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("代码");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("价格");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("涨跌幅%");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("净流入率%");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("净流入/万");
        cell.setCellStyle(style);

        cell = row.createCell(7);
        cell.setCellValue("主力流入率%");
        cell.setCellStyle(style);

        cell = row.createCell(8);
        cell.setCellValue("散户流入率%");
        cell.setCellStyle(style);

        cell = row.createCell(9);
        cell.setCellValue("流入资金/万");
        cell.setCellStyle(style);

        cell = row.createCell(10);
        cell.setCellValue("流出资金/万");
        cell.setCellStyle(style);


        cell = row.createCell(11);
        cell.setCellValue("行业");
        cell.setCellStyle(style);

        cell = row.createCell(12);
        cell.setCellValue("地区");
        cell.setCellStyle(style);

        //第五步插入数据
        for (int i = 0; i < data.size(); i++) {
            Stock stock = data.get(i);
            Money money = stock.getMoneylist().stream().filter(x->x.getTicktime().equals(time)).findFirst().get();
            //创建行
            row = sheet.createRow(i + 1);
            //创建单元格并且添加数据
            row.createCell(0).setCellValue(money.getTicktime());
            row.createCell(1).setCellValue(stock.getName());
            row.createCell(2).setCellValue(stock.getNum());
            row.createCell(3).setCellValue(money.getTrade());
            row.createCell(4).setCellValue(money.getChangeratio());
            row.createCell(5).setCellValue(money.getRatioamount());
            row.createCell(6).setCellValue(money.getNetamount());
            row.createCell(7).setCellValue(money.getR0_ratio());
            row.createCell(8).setCellValue(money.getR3_ratio());
            row.createCell(9).setCellValue(money.getInamount());
            row.createCell(10).setCellValue(money.getOutamount());
            row.createCell(11).setCellValue(stock.getIndustry());
            row.createCell(12).setCellValue(stock.getArea());
        }

        //第六步将生成excel文件保存到指定路径下
        try {
            File file = new File(path);
            if(file.exists()){
                file.delete();
            }
            FileOutputStream fout = new FileOutputStream(path);
            wb.write(fout);
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Excel文件生成成功...");
    }


    public static void exportExcel(List<Stock> data ,String name,String path,int index) {
        //第一步创建workbook
        HSSFWorkbook wb = new HSSFWorkbook();
        //第二步创建sheet
        HSSFSheet sheet = wb.createSheet(name);
        sheet.createFreezePane( 0, 1);
        sheet.setDefaultColumnWidth(12);
        //第三步创建行row:添加表头0行
        HSSFRow row = sheet.createRow(0);
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  //居中

        //第四步创建单元格
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("时间");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("名称");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("代码");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("价格");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("涨跌幅%");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("净流入率%");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("净流入/万");
        cell.setCellStyle(style);

        cell = row.createCell(7);
        cell.setCellValue("主力流入率%");
        cell.setCellStyle(style);

        cell = row.createCell(8);
        cell.setCellValue("散户流入率%");
        cell.setCellStyle(style);

        cell = row.createCell(9);
        cell.setCellValue("流入资金/万");
        cell.setCellStyle(style);

        cell = row.createCell(10);
        cell.setCellValue("流出资金/万");
        cell.setCellStyle(style);


        cell = row.createCell(11);
        cell.setCellValue("行业");
        cell.setCellStyle(style);

        cell = row.createCell(12);
        cell.setCellValue("地区");
        cell.setCellStyle(style);

        //第五步插入数据
        for (int i = 0; i < data.size(); i++) {
            Stock stock = data.get(i);
            Money money = stock.getMoneylist().get(index);
            //创建行
            row = sheet.createRow(i + 1);
            //创建单元格并且添加数据
            row.createCell(0).setCellValue(money.getTicktime());
            row.createCell(1).setCellValue(stock.getName());
            row.createCell(2).setCellValue(stock.getNum());
            row.createCell(3).setCellValue(money.getTrade());
            row.createCell(4).setCellValue(money.getChangeratio());
            row.createCell(5).setCellValue(money.getRatioamount());
            row.createCell(6).setCellValue(money.getNetamount());
            row.createCell(7).setCellValue(money.getR0_ratio());
            row.createCell(8).setCellValue(money.getR3_ratio());
            row.createCell(9).setCellValue(money.getInamount());
            row.createCell(10).setCellValue(money.getOutamount());
            row.createCell(11).setCellValue(stock.getIndustry());
            row.createCell(12).setCellValue(stock.getArea());
        }

        //第六步将生成excel文件保存到指定路径下
        try {
            File file = new File(path);
            if(file.exists()){
                file.delete();
            }
            FileOutputStream fout = new FileOutputStream(path);
            wb.write(fout);
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Excel文件生成成功...");
    }



    public static void exportMoneyFlowExcel(List<MoneyFlow> data ,String name,String path,String date) {
        //第一步创建workbook
        HSSFWorkbook wb = new HSSFWorkbook();
        //第二步创建sheet
        HSSFSheet sheet = wb.createSheet(name);
        sheet.createFreezePane( 0, 1);
        sheet.setDefaultColumnWidth(12);
        //第三步创建行row:添加表头0行
        HSSFRow row = sheet.createRow(0);
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  //居中

        //第四步创建单元格
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("时间");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("代码");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("名称");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("价格");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("涨幅%");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("净流入/万");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("主力净流入/万");
        cell.setCellStyle(style);

        cell = row.createCell(7);
        cell.setCellValue("主力流入/万");
        cell.setCellStyle(style);

        cell = row.createCell(8);
        cell.setCellValue("主力流出/万");
        cell.setCellStyle(style);

        cell = row.createCell(9);
        cell.setCellValue("换手率");
        cell.setCellStyle(style);

        cell = row.createCell(10);
        cell.setCellValue("成交量/手");
        cell.setCellStyle(style);

        cell = row.createCell(11);
        cell.setCellValue("主力罗盘");
        cell.setCellStyle(style);

        cell = row.createCell(12);
        cell.setCellValue("行业");
        cell.setCellStyle(style);

        cell = row.createCell(13);
        cell.setCellValue("地区");
        cell.setCellStyle(style);

        //第五步插入数据
        for (int i = 0; i < data.size(); i++) {
            MoneyFlow moneyFlow = data.get(i);
            //创建行
            row = sheet.createRow(i + 1);
            //创建单元格并且添加数据
            row.createCell(0).setCellValue(date);
            row.createCell(1).setCellValue(moneyFlow.getStock().getNum());
            row.createCell(2).setCellValue(moneyFlow.getStock().getName());
            row.createCell(3).setCellValue(moneyFlow.getTrade());
            row.createCell(4).setCellValue(moneyFlow.getChangeratio());
            row.createCell(5).setCellValue(moneyFlow.getNetamount());
            row.createCell(6).setCellValue(moneyFlow.getMainnetmount());
            row.createCell(7).setCellValue(moneyFlow.getMainin());
            row.createCell(8).setCellValue(moneyFlow.getMainout());
            row.createCell(9).setCellValue(moneyFlow.getTurnover());
            row.createCell(10).setCellValue(moneyFlow.getVolume());
            row.createCell(11).setCellValue(moneyFlow.getZllp());
            row.createCell(12).setCellValue(moneyFlow.getStock().getIndustry());
            row.createCell(13).setCellValue(moneyFlow.getStock().getArea());
        }

        //第六步将生成excel文件保存到指定路径下
        try {
            File file = new File(path);
            if(file.exists()){
                file.delete();
            }
            FileOutputStream fout = new FileOutputStream(path);
            wb.write(fout);
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Excel文件生成成功...");
    }
}