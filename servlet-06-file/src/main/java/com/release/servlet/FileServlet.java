package com.release.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

/**
 * @author yancheng
 * @since 2022/7/6
 */
public class FileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        //判断上传的文件是普通表单还是文件表单
        if (!ServletFileUpload.isMultipartContent(req)) {
            return;//如果是普通表单，则终止
        }

        String uploadPath = this.getServletContext().getRealPath("/WEB-INF/upload");
        File uploadFile = new File(uploadPath);
        if (!uploadFile.exists()) {
            uploadFile.mkdir();
        }

        //缓存临时文件
        String tempPath = this.getServletContext().getRealPath("/WEB-INF/temp");
        File tempFile = new File(tempPath);
        if (!tempFile.exists()) {
            tempFile.mkdir();
        }

        //处理上传的文件，一般都需要通过流来获取，我们可以使用request.getInputStream(),原生态的文件上传流获取，十分麻烦
        //但是我们都建议使用Apache的文件上传组件来实现，common-fileupload,它需要依赖于common-io组件；

        try {
            //1.创建DiskFileItemFactory对象，处理文件上传路径或者大小限制的
            DiskFileItemFactory factory = getDiskFileItemFactory(tempFile);
            //2.获取ServletFileUpload
            ServletFileUpload upload = getServletFileUpload(factory);
            //3.处理上传的文件
            String msg = uploadParseRequest(upload, req, uploadPath);

            //servlet请求转发消息
            req.setAttribute("msg", msg);
            req.getRequestDispatcher("info.jsp").forward(req, resp);
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        doGet(req, resp);
    }


    /**
     * 创建DiskFileItemFactory对象，处理文件上传路径或者大小限制的
     *
     * @param tempFile
     * @return
     */
    private DiskFileItemFactory getDiskFileItemFactory(File tempFile) {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(1024 * 1024);//缓存区大小为1M
        factory.setRepository(tempFile);//临时目录的保存目录
        return factory;
    }

    /**
     * 获取ServletFileUpload
     *
     * @param factory
     * @return
     */
    private ServletFileUpload getServletFileUpload(DiskFileItemFactory factory) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);
        ServletFileUpload upload = new ServletFileUpload(factory);
        //监听文件上传进度
        upload.setProgressListener((pByteRead, pContentLength, pItems) ->
                System.out.println("总大小：" + pContentLength + " 已上传：" + pByteRead + " 上传百分比：" + df.format((float) pByteRead / pContentLength * 100) + "%")
        );
        //处理乱码
        upload.setHeaderEncoding("utf-8");
        //设置单个文件的最大值
        upload.setFileSizeMax(1024 * 1024 * 10);
        //设置总共能够上传文件的大小
        upload.setSizeMax(1024 * 1024 * 10);
        return upload;
    }


    /**
     * 处理上传的文件
     *
     * @param upload
     * @param req
     * @param uploadPath
     * @return
     */
    private String uploadParseRequest(ServletFileUpload upload, HttpServletRequest req, String uploadPath) throws FileUploadException, IOException {
        String msg = "";
        //解析前端的请求，封装成一个FileItem对象
        List<FileItem> fileItems = upload.parseRequest(req);
        for (FileItem fileItem : fileItems) {
            //判断上传的文件是普通表单还是带文件的表单
            if (fileItem.isFormField()) {
                String name = fileItem.getFieldName();
                String value = fileItem.getString("UTF-8");
                System.out.println("name:" + name + " value:" + value);
            } else {
                //判断它是上传的文件
                //==========================处理文件============================
                //拿到文件名字
                String uploadFileName = fileItem.getName();
                System.out.println("上传的文件名：" + uploadFileName);
                if (uploadFileName.trim().equals("") || uploadFileName == null) {
                    continue;
                }
                //获得上传的文件名
                String fileName = uploadFileName.substring(uploadFileName.lastIndexOf("/") + 1);
                //获得文件的后缀名
                String fileExtName = uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1);
                System.out.println("文件信息【文件名：" + fileName + "  文件类型：" + fileExtName + "】");
                //可以使用UUID，保证文件名唯一
                //UUID.randomUUID(),随机生一个唯一识别的通用码
                String uuidPath = UUID.randomUUID().toString();

                //==========================存放地址============================
                //文件真实存在的路径realPath
                String realPath = uploadPath + "/" + uuidPath;
                //给每个文件创建一个对应的文件夹
                File realPathFile = new File(realPath);
                if (!realPathFile.exists()) {
                    realPathFile.mkdir();
                }
                //==========================文件传输============================
                //获得文件上传的流
                InputStream inputStream = fileItem.getInputStream();
                //创建一个文件输出流
                FileOutputStream fos = new FileOutputStream(realPath + "/" + fileName);
                //创建一个缓冲区
                byte[] buffer = new byte[1024 * 1024];
                //判断是否读取完毕
                int len = 0;
                //如果大于0说明还存在数据
                while ((len = inputStream.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                //关闭流
                fos.close();
                inputStream.close();
                msg = "文件上传成功！";
                fileItem.delete();//上传成功，清除临时文件
            }
        }
        return msg;
    }
}