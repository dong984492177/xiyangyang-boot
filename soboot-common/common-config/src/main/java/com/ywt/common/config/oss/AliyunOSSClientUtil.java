package com.ywt.common.config.oss;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;

/**
 * @Author: huangchaoyang
 * @Description: 阿里云oss
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public class AliyunOSSClientUtil {
    private final Logger logger = LoggerFactory.getLogger(AliyunOSSClientUtil.class);

    private AliyunOSSProperties properties;

    public AliyunOSSClientUtil(AliyunOSSProperties properties) {
        this.properties = properties;
    }

    public static String FORMAT = new SimpleDateFormat("yyyyMMdd").format(new Date());

    private OSSClient getOSSClient() {
        return  (OSSClient) new OSSClientBuilder().build(properties.getEndPoint(), properties.getAccessKeyId(), properties.getAccessKeySecret());
    }

    /**
     * 创建存储空间
     * @param bucketName 存储空间
     * @return
     */
    public String createBucketName(String bucketName){
        //存储空间
        if(!getOSSClient().doesBucketExist(bucketName)){
            //创建存储空间
            Bucket bucket=getOSSClient().createBucket(bucketName);
            logger.info("创建存储空间成功");
            return bucket.getName();
        }
        return bucketName;
    }

    /**
     * 删除存储空间buckName
     * @param bucketName  存储空间
     * @return 是否成功
     */
    public boolean deleteBucket(String bucketName){
        try {
            getOSSClient().deleteBucket(bucketName);
            logger.info("删除" + bucketName + "Bucket成功");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String createFolder(String folder){
        return this.createFolder(properties.getBucketName(), folder);
    }

    /**
     * 创建模拟文件夹
     * @param bucketName 存储空间
     * @param folder   模拟文件夹名如"qj_nanjing/"
     * @return  文件夹名
     */
    public String createFolder(String bucketName,String folder){
        //文件夹名
        final String keySuffixWithSlash =folder;
        //判断文件夹是否存在，不存在则创建
        if(!getOSSClient().doesObjectExist(bucketName, keySuffixWithSlash)){
            //创建文件夹
            getOSSClient().putObject(bucketName, keySuffixWithSlash, new ByteArrayInputStream(new byte[0]));
            logger.info("创建文件夹成功");
            //得到文件夹名
            OSSObject object = getOSSClient().getObject(bucketName, keySuffixWithSlash);
            String fileDir = object.getKey();
            return fileDir;
        }
        return keySuffixWithSlash;
    }

    public boolean deleteFile(String key){
        return this.deleteFile(properties.getBucketName(), key);
    }

    /**
     * 根据key删除OSS服务器上的文件
     * @param bucketName  存储空间
     * @param key Bucket下的文件的路径名+文件名 如："upload/cake.jpg"
     * @return 是否成功
     */
    public boolean deleteFile(String bucketName, String key){
        try {
            getOSSClient().deleteObject(bucketName,  key);
            logger.info("删除" + bucketName + "下的文件" +  key + "成功");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 通过文件名判断并获取OSS服务文件上传时文件的contentType
     * @param fileName 文件名
     * @return 文件的contentType
     */
    public String getContentType(String fileName){
        //文件的后缀名
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        if(".bmp".equalsIgnoreCase(fileExtension)) {
            return "image/bmp";
        }
        if(".gif".equalsIgnoreCase(fileExtension)) {
            return "image/gif";
        }
        if(".jpeg".equalsIgnoreCase(fileExtension) || ".jpg".equalsIgnoreCase(fileExtension)  || ".png".equalsIgnoreCase(fileExtension) ) {
            return "image/jpeg";
        }
        if(".html".equalsIgnoreCase(fileExtension)) {
            return "text/html";
        }
        if(".txt".equalsIgnoreCase(fileExtension)) {
            return "text/plain";
        }
        if(".vsd".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.visio";
        }
        if(".ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.ms-powerpoint";
        }
        if(".doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension)) {
            return "application/msword";
        }
        if(".xml".equalsIgnoreCase(fileExtension)) {
            return "text/xml";
        }
        //默认返回类型
        return "image/jpeg";
    }

    /**
     * 获得url链接
     * @param key 上传图片的路径+名称（如：test-kaka/headImage/1546404670068899.jpg）
     * @return 图片链接：http://xxxxx.oss-cn-beijing.aliyuncs.com/test/headImage/1546404670068899.jpg?Expires=1861774699&OSSAccessKeyId=****=p%2BuzEEp%2F3JzcHzm%2FtAYA9U5JM4I%3D
     * （Expires=1861774699&OSSAccessKeyId=LTAISWCu15mkrjRw&Signature=p%2BuzEEp%2F3JzcHzm%2FtAYA9U5JM4I%3D 分别为：有前期、keyID、签名）
     */
    public String getUrl(String key) {
        // 设置URL过期时间为10年  3600l* 1000*24*365*10
        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
        // 生成URL
        URL url = getOSSClient().generatePresignedUrl(properties.getBucketName(), key, expiration);

        return url.toString().substring(0, url.toString().lastIndexOf("?"));
    }

    /**
     * 上传图片(字节数组) 返回图片的路径
     * @param b 数组
     * @param folder 文件夹
     * @param fileName 文件名称
     * @return url
     */
    public String uploadByteOSS(byte[] b, String folder, String fileName) {
        return this.uploadByteOSS(b, folder, fileName, properties.getBucketName());
    }

    /**
     * 上传图片(字节数组) 返回图片的路径
     * @param b 数组
     * @param folder 文件夹
     * @param fileName 文件名称
     * @param bucketName bucket名称
     * @return url
     */
    public String uploadByteOSS(byte[] b, String folder, String fileName, String bucketName) {

        if(StringUtils.isEmpty(folder)){
            folder = FORMAT + "/";
        }else{
            folder = folder + "/" + FORMAT + "/";
        }
        // 文件名
        String timefile = UUID.randomUUID().toString().replaceAll("-","");// 文件名
        fileName = timefile + fileName;
        logger.info("上传到路径" + folder + fileName);

        Long fileSize = (long) b.length;
        // 创建上传Object的Metadata
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(fileSize);
        // 指定该Object被下载时的网页的缓存行为
        metadata.setCacheControl("no-cache");
        // 指定该Object下设置Header
        metadata.setHeader("Pragma", "no-cache");
        // 指定该Object被下载时的内容编码格式
        metadata.setContentEncoding("utf-8");
        // 文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
        // 如果没有扩展名则填默认值application/octet-stream
        metadata.setContentType(getContentType(fileName));
        // 指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
        metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");
        String filepath = folder + fileName;
        PutObjectResult putResult = getOSSClient().putObject(bucketName, filepath, new ByteArrayInputStream(b));
        getOSSClient().shutdown();
        // 生成URL
        // 设置URL过期时间为10年  3600l* 1000*24*365*10
//        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
//        URL url = getOSSClient().generatePresignedUrl(properties.getBucketName(), filepath, expiration);
//        return url.toString().substring(0, url.toString().lastIndexOf("?"));
        return "http://"+properties.getAccessDomain() + "/" + filepath;
    }

    /**
     * 上传图片(base64码) 返回图片的路径
     * @param base64Str base64码字符串
     * @param folder 文件夹
     * @param fileName 文件名
     * @return
     */
    public String uploadBase64(String base64Str, String folder, String fileName) {
        return this.uploadBase64(base64Str, folder, fileName, properties.getBucketName());
    }

    /**
     * 上传图片(base64码) 返回图片的路径
     * @param base64Str base64码字符串
     * @param folder 文件夹
     * @param fileName 文件名
     * @param bucketName OSS的bucket名称
     * @return
     */
    public String uploadBase64(String base64Str, String folder, String fileName, String bucketName) {
        byte[] bytes = base64ToByte(base64Str);
        String filepath = uploadByteOSS(bytes, folder, fileName, bucketName);
        return filepath;

    }

    /**
     * 上传file文件
     * @param file 文件
     * @param folder 文件夹
     * @return String 路径
     * @throws IOException
     */
    public String uploadFile(File file, String folder) throws IOException {
        return this.uploadFile(file, folder, properties.getBucketName());
    }

    /**
     * 上传file文件
     * @param file 文件
     * @param folder 文件夹
     * @param bucketName OSS的bucket名称
     * @return String 路径
     * @throws IOException
     */
    public String uploadFile(File file, String folder, String bucketName) throws IOException {
        String fileName = file.getName();
        byte[] bytes = FileUtils.readFileToByteArray(file);
        return uploadByteOSS(bytes, folder, fileName, bucketName);
    }

    /**
     * base64码转为byte[]
     * @param base64Str base64码
     * @return byte[]
     */
    public byte[] base64ToByte(String base64Str){
        byte[] bytes = Base64.getDecoder().decode(base64Str);
        return bytes;
    }

    /**
     * byte[]转为base64
     * @param bytes 字节数组
     * @return base64Str
     */
    public String byteToBase64(byte[] bytes){
        String base64Str = Base64.getEncoder().encodeToString(bytes);
        return base64Str;
    }

    /**
     * 文件下载
     * @param key 文件key
     * @param filename 文件名
     * @throws OSSException
     * @throws ClientException
     */
    public void downloadFile(String key, String filename){
        this.downloadFile(properties.getBucketName(), key, filename);
    }

    /**
     * 文件下载
     * @param bucketName OSS的bucket名称
     * @param key 文件key
     * @param filename 文件名
     * @throws OSSException
     * @throws ClientException
     */
    public void downloadFile(String bucketName, String key, String filename)
            throws OSSException, ClientException {
        getOSSClient().getObject(new GetObjectRequest(bucketName, key), new File(filename));
    }

    /**
     * 文件下载转为字节数组
     * @param key 文件key
     * @return
     * @throws Exception
     */
    public byte[] downloadFile2Byte(String key) throws Exception {
        return this.downloadFile2Byte(properties.getBucketName(), key);
    }

    /**
     * 文件下载转为字节数组
     * @param bucketName OSS的bucket名称
     * @param key 文件key
     * @return
     * @throws Exception
     */
    public byte[] downloadFile2Byte(String bucketName, String key)
            throws Exception {
        OSSObject ossObject = getOSSClient().getObject(bucketName, key);
        InputStream in = ossObject.getObjectContent();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    /**
     * 图片下载转为base64
     * @param key 文件key
     * @return
     * @throws Exception
     */
    public String downloadImage2Base64(String key) throws Exception {
        return this.downloadImage2Base64(properties.getBucketName(), key);
    }

    public String downloadImage2Base64ByPath(String path) throws Exception {
        String key = splitDomain(path);
        return this.downloadImage2Base64(properties.getBucketName(), key);
    }

    /**
     * 图片下载转为base64
     * @param bucketName OSS的bucket名称
     * @param key 文件key
     * @return
     * @throws Exception
     */
    public String downloadImage2Base64(String bucketName, String key)
            throws Exception {
        OSSObject ossObject = getOSSClient().getObject(bucketName, key);
        InputStream in = ossObject.getObjectContent();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        String base64Str = byteToBase64(out.toByteArray());
        return base64Str;
    }

    public boolean deleteFileByPah(String path){
        String key = splitDomain(path);
        return this.deleteFile(properties.getBucketName(), key);
    }

    private String splitDomain(String path) {
        String[] arrUrl = path.split("//");
        int start = arrUrl[1].indexOf("/");
        return arrUrl[1].substring(start + 1);
    }

    public static void main(String[] args) {
		File f = new File("C:\\Users\\Administrator\\Documents\\ucam3.0\\通用公区.png");
		AliyunOSSClientUtil o = new AliyunOSSClientUtil(new AliyunOSSProperties());
		try {
			long t1 = System.currentTimeMillis();
			String s = o.uploadFile(f, "ych");
			System.out.println("===" + s);
			System.out.println((System.currentTimeMillis()-t1)/1000 + "s");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
