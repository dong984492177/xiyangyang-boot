package com.ywt.console.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.util.Base64Util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public class ImageUtils {

    public static String getBase64ByImgUrl(String url) {
        String suffix = url.substring(url.lastIndexOf(".") + 1);
        try {
            URL urls = new URL(url);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Image image = Toolkit.getDefaultToolkit().getImage(urls);
            BufferedImage biOut = toBufferedImage(image);
            ImageIO.write(biOut, suffix, baos);
            String base64Str = Base64Util.encode(new String(baos.toByteArray(), "utf-8"));
            return base64Str;
        } catch (Exception e) {
            return "";
        }
    }

    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        try {
            int transparency = Transparency.OPAQUE;
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(image.getWidth(null),
                    image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }
        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            bimage = new BufferedImage(image.getWidth(null),
                    image.getHeight(null), type);
        }
        // Copy image to buffered image
        Graphics g = bimage.createGraphics();
        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bimage;
    }

    /**
     * 通过图片的url获取图片的base64字符串
     *
     * @param imgUrl 图片url
     * @return 返回图片base64的字符串
     */
    public static String image2Base64(String imgUrl) {
        URL url = null;
        InputStream is = null;
        ByteArrayOutputStream outStream = null;
        HttpURLConnection httpUrl = null;
        try {
            url = new URL(imgUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            httpUrl.getInputStream();
            is = httpUrl.getInputStream();
            outStream = new ByteArrayOutputStream();
            //创建一个Buffer字符串
            byte[] buffer = new byte[1024];
            //每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len = 0;
            //使用一个输入流从buffer里把数据读取出来
            while ((len = is.read(buffer)) != -1) {
                //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                outStream.write(buffer, 0, len);
            }
            // 对字节数组Base64编码
            return Base64Util.encode(new String(outStream.toByteArray(), "utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpUrl != null) {
                httpUrl.disconnect();
            }
        }
        return imgUrl;
    }

    /**
     * 将网络图片编码为base64
     *
     * @param imageUrl
     * @return
     * @throws
     */
    public static String encodeImageToBase64(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            //将图片文件转化为字节数组字符串，并对其进行Base64编码处理
            System.out.println("图片的路径为:" + url.toString());
            //打开链接
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
                //设置请求方式为"GET"
                conn.setRequestMethod("GET");
                //超时响应时间为5秒
                conn.setConnectTimeout(5 * 1000);
                //通过输入流获取图片数据
                InputStream inStream = conn.getInputStream();
                //得到图片的二进制数据，以二进制封装得到数据，具有通用性
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                //创建一个Buffer字符串
                byte[] buffer = new byte[1024];
                //每次读取的字符串长度，如果为-1，代表全部读取完毕
                int len = 0;
                //使用一个输入流从buffer里把数据读取出来
                while ((len = inStream.read(buffer)) != -1) {
                    //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                    outStream.write(buffer, 0, len);
                }
                //关闭输入流
                inStream.close();
                byte[] data = outStream.toByteArray();
                //对字节数组Base64编码
                BASE64Encoder encoder = new BASE64Encoder();
                String base64 = encoder.encode(data);
                base64 = Base64.encodeBase64String(base64.getBytes());
                System.out.println("网络文件[{}]编码成base64字符串:[{}]" + url.toString() + base64);
                return base64;//返回Base64编码过的字节数组字符串
            } catch (IOException e) {
                e.printStackTrace();
                throw new Exception("图片上传失败,请联系客服!");
            }
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 路径生成base64（有效）
     * @param path
     * @return
     */
   public static String getBase64(String path){

       String encode =null;
       try {
           HttpClient client = new DefaultHttpClient();
           HttpGet get = new HttpGet(path);
           HttpResponse response = client.execute(get);
           HttpEntity entity = response.getEntity();
           byte[] data = EntityUtils.toByteArray(entity);
           BASE64Encoder encoder = new BASE64Encoder();
           encode = encoder.encode(data);
           encode = encode.replaceAll("\n", "").replaceAll("\r", "");
       }catch(Exception e){
           e.printStackTrace();
       }
       return encode;
   }

   public static BufferedImage getBufferedImage(String base64){
       BASE64Decoder decoder = new sun.misc.BASE64Decoder();
       try {
           byte[] bytes = decoder.decodeBuffer(base64);
           ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
           return ImageIO.read(bais);
       } catch (IOException e) {
           e.printStackTrace();
       }
       return null;
   }

    public static String getBase64(BufferedImage bufferedImage){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bytes = baos.toByteArray();

        BASE64Encoder encoder = new sun.misc.BASE64Encoder();
        String png_base64 = encoder.encodeBuffer(bytes).trim();
        png_base64 = png_base64.replaceAll("\n", "").replaceAll("\r", "");

        return png_base64;
    }

    public static void main(String[] args){
        String url ="http://newplatform-dev.oss-cn-shanghai.aliyuncs.com/20201014/8807a335c4c5455da623c829451b15a9.jpg";
        //System.out.println(resizeImageTo40K(getBase64(url)));

        //changeOpacity(getBase64(url),1f);
        //System.out.println(convert(url,50));
        System.out.println(convert(url,10));
    }

  /*  public static String resizeImageTo40K(String base64Img) {
        try {
            BufferedImage src = getBufferedImage(base64Img);
            BufferedImage output = Thumbnails.of(src).size(src.getWidth() / 3, src.getHeight() / 3).asBufferedImage();
            String base64 = getBase64(output);
            if (base64.length() - base64.length() / 8 * 2 > 40000) {
                output = Thumbnails.of(output).scale(1 / (base64.length() / 40000)).asBufferedImage();
                base64 = getBase64(output);
            }
            return base64;
        } catch (Exception e) {
            return base64Img;
        }
    }*/

    /**
     * 生成透明度 高斯模糊
     * @param base64Img
     * @return
     */
    public static String changeOpacity(String base64Img,float opacity){

        BufferedImage srcImg = getBufferedImage(base64Img);
        BufferedImage dstImg = new BufferedImage(srcImg.getWidth(), srcImg.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) dstImg.getGraphics();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, opacity));
        g2.drawImage(srcImg, 0, 0, srcImg.getWidth(), srcImg.getHeight(), null);
        g2.dispose();

        int height = dstImg.getHeight();
        int width = dstImg.getWidth();
        int[][] martrix = new int[3][3];
        int[] values = new int[9];
        for(int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                readPixel(dstImg, i, j, values);
                fillMatrix(martrix, values);
                dstImg.setRGB(i, j, avgMatrix(martrix));
            }
        }
        return getBase64(dstImg);
    }

    private static void readPixel(BufferedImage img, int x, int y, int[] pixels) {
        int xStart = x - 1;
        int yStart = y - 1;
        int current = 0;
        for(int i = xStart; i < 3 + xStart; i++) {
            for (int j = yStart; j < 3 + yStart; j++) {
                int tx = i;
                if (tx < 0) {
                    tx = -tx;
                } else if (tx >= img.getWidth()) {
                    tx = x;
                }
                int ty = j;
                if (ty < 0) {
                    ty = -ty;
                } else if (ty >= img.getHeight()) {
                    ty = y;
                }
                pixels[current++] = img.getRGB(tx, ty);
            }
        }
    }

    private static void fillMatrix(int[][] matrix, int[] values) {
        int filled = 0;
        for (int i = 0; i < matrix.length; i++) {
            int[] x = matrix[i];
            for (int j = 0; j < x.length; j++) {
                x[j] = values[filled++];
            }
        }
    }

    private static int avgMatrix(int[][] matrix) {
        int r = 0;
        int g = 0;
        int b = 0;
        for (int i = 0; i < matrix.length; i++) {
            int[] x = matrix[i];
            for (int j = 0; j < x.length; j++) {
                if (j == 1) {
                    continue;
                }
                Color c = new Color(x[j]);
                r += c.getRed();
                g += c.getGreen();
                b += c.getBlue();
            }
        }
        return new Color(r / 8, g / 8, b / 8).getRGB();
    }

    public static String convert(String path,int alpha){
        //将背景色变透明

         BufferedImage image = getBufferedImage(getBase64((path)));

            int imgHeight = image.getHeight();//取得图片的长和宽
            int imgWidth = image.getWidth();
            int c = image.getRGB(3, 3);
            //防止越位
            if (alpha < 0) {
                alpha = 0;
            } else if (alpha > 10) {
                alpha = 10;
            }
            BufferedImage bi = new BufferedImage(imgWidth, imgHeight,
                    BufferedImage.TYPE_4BYTE_ABGR);//新建一个类型支持透明的BufferedImage
            for(int i = 0; i < imgWidth; ++i)//把原图片的内容复制到新的图片，同时把背景设为透明
            {
                for(int j = 0; j < imgHeight; ++j)
                {
                    //把背景设为透明
                    if(image.getRGB(i, j) == c){
                        bi.setRGB(i, j, c & 0x00ffffff);
                    }
                    //设置透明度
                    else{
                        int rgb = bi.getRGB(i, j);
                        rgb = ((alpha * 255 / 10) << 24) | (rgb & 0x00ffffff);
                        bi.setRGB(i, j, rgb);
                    }
                }
            }
            return getBase64(bi);
    }

    public static String convert2(String path){

        BufferedImage image = getBufferedImage(getBase64((path)));
        int height = image.getHeight();
        int width = image.getWidth();

        // 生产背景透明和内容透明的图片
        ImageIcon imageIcon = new ImageIcon(image);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        //Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics(); // 获取画笔
        //g2D.drawImage(imageIcon.getImage(), 0, 0, null); // 绘制Image的图片

        Graphics2D resizedG = bufferedImage.createGraphics();
        bufferedImage = resizedG.getDeviceConfiguration().createCompatibleImage(width,height,Transparency.TRANSLUCENT);
        resizedG.dispose();
        resizedG = bufferedImage.createGraphics();
        Image from = image.getScaledInstance(width, height,Image.SCALE_AREA_AVERAGING);
        resizedG.drawImage(from, 0, 0, null);
        resizedG.dispose();

       /* int alpha = 0; // 图片透明度
        // 外层遍历是Y轴的像素
        for (int y = bufferedImage.getMinY(); y < bufferedImage.getHeight(); y++) {
            // 内层遍历是X轴的像素
            for (int x = bufferedImage.getMinX(); x < bufferedImage.getWidth(); x++) {
                int rgb = bufferedImage.getRGB(x, y);
                // 对当前颜色判断是否在指定区间内
                if (colorInRange(rgb)) {
                    alpha = 0;
                } else {
                    // 设置为不透明
                    alpha = 255;
                }
                // #AARRGGBB 最前两位为透明度
                //rgb = (alpha << 24) | (rgb & 0x00ffffff);
                rgb = ((alpha * 255 / 10) << 24) | (rgb & 0x00ffffff);
                bufferedImage.setRGB(x, y, rgb);
            }
        }
        // 绘制设置了RGB的新图片
        resizedG.drawImage(bufferedImage, 0, 0, null);*/

        return getBase64(bufferedImage);
    }

    // 判断是背景还是内容
    public static boolean colorInRange(int color) {
        int red = (color & 0xff0000) >> 16;// 获取color(RGB)中R位
        int green = (color & 0x00ff00) >> 8;// 获取color(RGB)中G位
        int blue = (color & 0x0000ff);// 获取color(RGB)中B位
        // 通过RGB三分量来判断当前颜色是否在指定的颜色区间内
        if (red >= color_range && green >= color_range && blue >= color_range) {
            return true;
        }
        return false;
    }

    // 色差范围0~255
    public static int color_range = 210;


    public  static byte[] getBytesByImage(String path) throws Exception{
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(2 * 1000);
        InputStream inStream = conn.getInputStream();
        byte[] btImg = readInputStream(inStream);
        return btImg;
    }

    /**
     * 从输入流中获取字节流数据
     *
     * @param inStream 输入流
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[10240];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }
}
