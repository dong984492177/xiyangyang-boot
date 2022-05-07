package com.ywt.console.models.reqmodel;

import java.util.Date;

import com.ywt.console.models.QueryModel;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class QueryLoginLogReqModel extends QueryModel {

   @DateTimeFormat(pattern = "yyyy-MM-dd")
   private Date startTime;

   @DateTimeFormat(pattern = "yyyy-MM-dd")
   private Date endTime;
}
