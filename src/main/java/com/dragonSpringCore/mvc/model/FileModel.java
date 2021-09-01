package com.dragonSpringCore.mvc.model;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

/*
* 文件Model，方便用户进行文件的各种处理
* */
public class FileModel {
    private  byte[] fileData;
    private String fileName;
    private int flag;

    public FileModel(HttpServletRequest request) throws UnsupportedEncodingException {
        initFile(request);
    }

    public void initFile(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8"); //设置编码
        flag = 0;
        try {
            //创建磁盘文件项工厂
            DiskFileItemFactory factory=new DiskFileItemFactory();
            //创建文件上传的核心对象
            ServletFileUpload upload=new ServletFileUpload(factory);
            //解析request获得文件项对象集合
            List<FileItem> parseRequest = upload.parseRequest(request);
            for (FileItem item : parseRequest) {
                //判断是否是普通表单项
                boolean formField = item.isFormField();
                if(!formField){
                    //文件上传项获得文件的名称，获得文件的内容
                    fileName = item.getName();
                    InputStream in=item.getInputStream();
                    //1.分配一块内存空间，存放我文件的数据
                    fileData = new byte[in.available()];
                    //2.将数据读入到内存空间
                    in.read(fileData);
                    in.close();
                    item.delete();
                    flag = 1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] getFileData(){
        return fileData;
    }

    public String getFileName(){
        return fileName;
    }

    public Boolean getFileStatus(){
        return flag == 1;
    }
}
