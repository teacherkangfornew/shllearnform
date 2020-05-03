package com.shl.itextdemo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Html2PdfDemo {
    private static final String path = "D:\\wkhtmltopdf\\wkhtmltopdf64\\bin\\wkhtmltopdf.exe ";
    private static String tokenParam = "access_token=eyJhbGciOiJIUzUxMiIsInppcCI6IkRFRiJ9.eNqUUs1u1DAQfpecncrZJJufG-oJqXDiBRx7kvXWiVd2siSLkOCCOPRAhTggLvQBuPRUKqG-zC7Qt2Cc3Sw98RMpsb-Zbybz873wlq30co-JLMsiHvtpLIQfiZD5RRyCH0VpFqVhUMQJ9YhnuwLJM55RVhTzOc_EnIcxDdwtFQkrgdLQEaW1SKwHXZaSw0qxFvxWn0PjWzBrMI7CWi8P4mQe0yihKfGgXx0MUZI5gzbVmW6qp6wGTLb7enf__vX25tX25svJj4_ftreX3z9f7S5uT_C4_3DnjQEH8tHEtVk9FmhxVa3A1KdagKtNNqXOayabFt-c0oDYwbZQP2ENq8CQRpuaqQm1BlmyqfK1hOcj-2hxoNaGqd--PRSsZfkROTsTwoC1Z9K2-z_yhdbqlCloBDP_k-kv9ayh6QC7G4vfu2UNz1ihYERYhe4M34OF7uwUON0fph2mIVimmBkepnVrrKtxvhGdoVSKci6SgJUJoChEMqMpoLAoLeh-PW76SO43CDuUwiPOddegEDy7gGbZO41B7zSGN6MVTOvq-0rh_GrZkKVd9UQMiNtekV4yrSThYBf4xYdwK0rCq0oN5BwKdPcY1BebgZFqM5Sk1EqgEA5NVRuOqYdDPZN8rt_u3nz6eXWxu3x38IxN_oPysck_UJOQZUFJ3ehe_gIAAP__.bila5XwCOO9e_9nHunD-QogKmVhmCLlIZH9a7TpgxZN9_WIBnyjcTarRAjj-e2_3dpqdOAUNxUjUWgpXr0UeZQ";

    public static void main(String[] args) throws FileNotFoundException {
        String srcPath = "http://192.168.20.65:9898/xcoffice/xcurl/printform?" + tokenParam;
        String formKey = "402894bf6f02f654016f03310f0c015b";
        String bpmBusinessKey = "244c08e96d7a406695646a73777114b9";
        srcPath += "&formKey=" + formKey + "&bpmBusinessKey=" + bpmBusinessKey;
        convertHtmlToPDF(srcPath, "D:\\html2pdfdir\\aac.pdf");

    }

    static boolean convertHtmlToPDF(String srcPath, String destPath) {
        File file = new File(destPath);
        File parent = file.getParentFile();
        //如果pdf保存路径不存在，则创建路径
        if (!parent.exists()) {
            parent.mkdirs();
        }

        StringBuilder cmd = new StringBuilder();
        cmd.append(path);
        // cmd.append(" --javascript-delay 3000 ");
        cmd.append("  --page-size A4 --no-outline  --no-stop-slow-scripts --enable-forms ");
        // 页眉下面的线
        // cmd.append("  --header-line");
        //页眉中间内容
        // cmd.append("  --header-center 这里是页眉这里是页眉这里是页眉这里是页眉 ");
        // 设置页面上边距 (default 10mm)
        // cmd.append("  --margin-top 30mm ");
        // cmd.append(" --header-spacing 10 ");//    (设置页眉和内容的距离,默认0)
        cmd.append(srcPath);
        cmd.append(" ");
        cmd.append(destPath);

        boolean result = true;
        try {
            Process proc = Runtime.getRuntime().exec(cmd.toString());

            proc.waitFor();
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }

        return result;
    }
}
