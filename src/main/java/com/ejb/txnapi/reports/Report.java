package com.ejb.txnapi.reports;

import net.sf.jasperreports.engine.JasperPrint;

import java.io.*;

public class Report implements java.io.Serializable {

    public Report() { }

    private byte[] bytes;
    private String viewType;
    private JasperPrint jasperPrint;

    public byte[] getBytes() {
        return (bytes);
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getViewType() {
        return (viewType);
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public JasperPrint getJasperPrint() {
        return (jasperPrint);
    }

    public void setJasperPrint(JasperPrint jasperPrint) {
        this.jasperPrint = jasperPrint;
    }

    public void setFileToByte(File file) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1; ) {
                bos.write(buf, 0, readNum); //no doubt here is 0
            }
        } catch (IOException ex) {
        }
        byte[] bytes = bos.toByteArray();
        setBytes(bytes);
    }
}