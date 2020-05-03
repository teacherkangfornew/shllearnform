package com.shl.test;

import com.shl.util.CommonUtil;
import com.shl.util.DbBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class HousingCycleData extends DbBase {


    public static void main(String[] args) throws IOException {
        String filePreffixe = writeSomeFile();
        File file = new File("D:/html2pdfdir/bb/");
        if (file.exists()) {
            // File[] aimList = file.listFiles(f -> f.getName().startsWith(filePreffixe));
            File[] aimList = file.listFiles();
            if (aimList != null) {
                ZipOutputStream zip = new ZipOutputStream(new FileOutputStream("D:/html2pdfdir/" + "aaa.zip"), StandardCharsets.UTF_8);
                byte[] buffer = new byte[4096];
                int len;
                FileInputStream in = null;
                for (File f : aimList) {
                    in = new FileInputStream(f);
                    ZipEntry zipentry = new ZipEntry(f.getName());
                    zip.putNextEntry(zipentry);
                    while ((len = in.read(buffer)) !=-1) {
                        zip.write(buffer, 0, len);
                    }
                    in.close();
                }
                zip.finish();
                zip.close();
                for (File f : aimList) {
                    //noinspection ResultOfMethodCallIgnored
                    f.delete();
                }
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }
        }
    }

    private static String writeSomeFile() throws IOException {
        String filePreffix = CommonUtil.UUID();
        String directory = "D:/html2pdfdir/bb/";
        File f = new File(directory);
        if (!f.exists()) {
            f.mkdir();
        }
        FileOutputStream out = null;
        for (int i = 0; i < 5; i++) {
            f = new File(directory + filePreffix + "_" + CommonUtil.UUID() + ".pdf");
            boolean b = true;
            if (!f.exists()) {
                b = f.createNewFile();
            }
            if (b) {
                out = new FileOutputStream(f);
                out.flush();
                out.close();
            }
        }
        return filePreffix;
    }


    private void cycleTable() {
        String sql = "INSERT INTO housing_cycle (id, isdelete,  optlock, domain_id, cycle_data_total, cycle_end_time, " +
                "cycle_free_amount, cycle_meter_read, cycle_out_amount, cycle_out_money, cycle_start_time, cycle_type, " +
                "cycle_used_amount, house_id, pre_cycle_meter_read, housing_cycle_id) " +
                "VALUES (:id, 0, 0, 0, :dateTotal, :cycleEnd, :freeAmount, :read, :out, " +
                ":outmoney, :cycleEnd, :cycleType, :usedAmount, :houseId, 100, NULL)";
        Calendar startC = Calendar.getInstance();
        Calendar endC = Calendar.getInstance();
        endC.add(Calendar.YEAR, 1);
        int q = endC.get(Calendar.YEAR) + endC.get(Calendar.MONTH);
        while (q != (startC.get(Calendar.YEAR) + startC.get(Calendar.MONTH))) {

            startC.add(Calendar.MONTH, 2);
        }
    }

    private void payCycleTable() {
        String sql = "INSERT INTO housing_pay_cycle (id,  isdelete,  optlock, domain_id," +
                " house_id, room_id, userid, free_amount, parentid, out_amount, out_money, electric_money, gas_money, total_money, water_money)" +
                " VALUES (:id, 0, 0, 0, :houseId, :roomId, :userId, :freeAmount, :parentId, :outAmount, :eleMoney, " +
                ":gasMoney, :totalMoney, :waterMoney)";


    }
}
