package com.ywt.console.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.ywt.console.models.ScheduleCornModel;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: huangchaoyang
 * @Description: corn表达式工具
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Slf4j
public class CronUtils {

    /**
     * 构建Cron表达式
     * @param taskScheduleModel
     * @return String
     */
    public static String createCronExpression(ScheduleCornModel taskScheduleModel) {
        StringBuffer cronExp = new StringBuffer("");

        if (null == taskScheduleModel) {
            log.info("执行周期未配置");
        }

        if (null != taskScheduleModel.getSecond()
                && null != taskScheduleModel.getMinute()
                && null != taskScheduleModel.getHour()) {
            //秒
            cronExp.append(taskScheduleModel.getSecond()).append(" ");
            //分
            cronExp.append(taskScheduleModel.getMinute()).append(" ");
            //小时
            cronExp.append(taskScheduleModel.getHour()).append(" ");

            //每天
            if (taskScheduleModel.getJobType().intValue() == 1) {
                cronExp.append("* ");//日
                cronExp.append("* ");//月
                cronExp.append("?");//周
            }

            //按每周
            else if (taskScheduleModel.getJobType().intValue() == 2) {

                //从某日开始
                cronExp.append(taskScheduleModel.getDay()).append("/1 ");
                //从某月开始
                cronExp.append(taskScheduleModel.getMonth()).append("/1 ");
                //周
                Integer[] weeks = taskScheduleModel.getDayOfWeeks();
                for (int i = 0; i < weeks.length; i++) {
                    if (i == 0) {
                        cronExp.append(weeks[i]);
                    } else {
                        cronExp.append(",").append(weeks[i]);
                    }
                }
                cronExp.append(" ");
                //从某年开始
                cronExp.append(taskScheduleModel.getYear()).append("- ");
            }
            //按每月
            else if (taskScheduleModel.getJobType().intValue() == 3) {
                //一个月中的哪几天
                Integer[] days = taskScheduleModel.getDayOfMonths();
                for (int i = 0; i < days.length; i++) {
                    if (i == 0) {
                        cronExp.append(days[i]);
                    } else {
                        cronExp.append(",").append(days[i]);
                    }
                }
                //月份
                cronExp.append(" * ");
                //周
                cronExp.append("?");
            }
        } else {
            System.out.println("时或分参数未配置");
        }
        return cronExp.toString();
    }

    /***
     * 把时间转换为cron表达式
     * @param date
     * @param dateFormat : e.g:yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String formatDateByPattern(Date date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String formatTimeStr = null;
        if (date != null) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }

    /***
     * convert Date to cron ,eg.  "0 06 10 15 1 ? 2014"
     * @param date  : 时间点
     * @return
     */
    public static String getCron(Date date) {
        String dateFormat = "ss mm HH dd MM ? yyyy";
        return formatDateByPattern(date, dateFormat);
    }

    public static String[] parseTime(String time){
      return   time.split(":");
    }

    public static void main(String[] args) {

        ScheduleCornModel taskScheduleModel = new ScheduleCornModel();
        taskScheduleModel.setJobType(1);//按每天
        Integer hour = 14; //时
        Integer minute = 20; //分
        Integer second = 00; //秒
        taskScheduleModel.setHour(hour);
        taskScheduleModel.setMinute(minute);
        taskScheduleModel.setSecond(second);
        taskScheduleModel.setDay(1);
        taskScheduleModel.setMonth(10);
        taskScheduleModel.setYear(2020);
        String cropExp = createCronExpression(taskScheduleModel);
        System.out.println(cropExp);
        //每周的哪几天执行
        taskScheduleModel.setJobType(2);//按每周
        Integer[] dayOfWeeks = new Integer[3];
        dayOfWeeks[0] = 1;
        dayOfWeeks[1] = 2;
        dayOfWeeks[2] = 3;
        taskScheduleModel.setDayOfWeeks(dayOfWeeks);
        cropExp = createCronExpression(taskScheduleModel);
        System.out.println(cropExp);
    }

}
