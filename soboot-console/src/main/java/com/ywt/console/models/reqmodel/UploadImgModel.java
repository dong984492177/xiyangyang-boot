package com.ywt.console.models.reqmodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadImgModel {

	private String key;
	private String base64Img;
	private String url;
}
