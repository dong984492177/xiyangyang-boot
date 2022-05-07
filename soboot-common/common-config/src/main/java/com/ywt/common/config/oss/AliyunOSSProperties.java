package com.ywt.common.config.oss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: huangchaoyang
 * @Description: 阿里云oss配置
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Data
@ConfigurationProperties(prefix = "oss.aliyun")
public class AliyunOSSProperties {
    // 阿里云OSS的外网域名
    public String endPoint = "oss-cn-hangzhou-internal.aliyuncs.com";
    // 阿里云OSS的密钥Access Key ID
    public String accessKeyId = "LTAI4FsutHkG3dnySbEYwphK";
    // 阿里云OSS的密钥Access Key Secret
    public String accessKeySecret = "MUoC2dwaf0VTAqB66YdjRyRB6CiTaH";
    // 阿里云OSS的bucket名称
    public String bucketName = "newplatform-prod";

    public String accessDomain = "newplatform-prod.oss-cn-hangzhou.aliyuncs.com";
}
