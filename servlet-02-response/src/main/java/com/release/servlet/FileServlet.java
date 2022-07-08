package com.release.servlet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * 文件下载
 * @author yancheng
 * @since 2022/7/6
 */
public class FileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = this.getServletContext().getRealPath("/upload");
        File realPath = new File(path);
        if (!realPath.exists()) {
            realPath.mkdir();
        }

//    1.要获取下载文件路径
        String downPath = this.getServletContext().getRealPath("/upload");
        System.out.println("下载文件路径："+downPath);
//    2.下载文件名是什么
        String fileName = "a.png";
//    3.设置浏览器能够支持下载我们需要的东西
        resp.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(fileName,"UTF-8"));
//    4.获取下载文件的输入流
        File file = new File(downPath, fileName);
        FileInputStream inputStream = new FileInputStream(file);
//    5.创建缓冲区
        int len = 0;
        byte[] buffer = new byte[1024];
//    6.获取OutputStream对象
        ServletOutputStream outputStream = resp.getOutputStream();
//    7.将FileOutputStream流写入到buffer缓冲区,使用OutputStream将缓冲区中的数据输出到客户端
        while ((len = inputStream.read(buffer))>0){
            outputStream.write(buffer,0,len);
        }
        inputStream.close();
        outputStream.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }
}
