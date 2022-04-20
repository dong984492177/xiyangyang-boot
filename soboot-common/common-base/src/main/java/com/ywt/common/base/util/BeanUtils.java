package com.ywt.common.base.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: huangchaoyang
 * @Description: Bean 工具类
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public class BeanUtils
{
    /** Bean方法名中属性名开始的下标 */
    private static final int BEAN_METHOD_PROP_INDEX = 3;

    /** * 匹配getter方法的正则表达式 */
    private static final Pattern GET_PATTERN = Pattern.compile("get(\\p{javaUpperCase}\\w*)");

    /** * 匹配setter方法的正则表达式 */
    private static final Pattern SET_PATTERN = Pattern.compile("set(\\p{javaUpperCase}\\w*)");

    /**
     * Bean属性复制工具方法。
     *
     * @param dest 目标对象
     * @param src 源对象
     */
    public static void copyBeanProp(Object dest, Object src)
    {
        List<Method> destSetters = getSetterMethods(dest);
        List<Method> srcGetters = getGetterMethods(src);
        try
        {
            for (Method setter : destSetters)
            {
                for (Method getter : srcGetters)
                {
                    if (isMethodPropEquals(setter.getName(), getter.getName())
                            && setter.getParameterTypes()[0].equals(getter.getReturnType()))
                    {
                        setter.invoke(dest, getter.invoke(src));
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 获取对象的setter方法。
     *
     * @param obj 对象
     * @return 对象的setter方法列表
     */
    public static List<Method> getSetterMethods(Object obj)
    {
        // setter方法列表
        List<Method> setterMethods = new ArrayList<Method>();

        // 获取所有方法
        Method[] methods = obj.getClass().getMethods();

        // 查找setter方法

        for (Method method : methods)
        {
            Matcher m = SET_PATTERN.matcher(method.getName());
            if (m.matches() && (method.getParameterTypes().length == 1))
            {
                setterMethods.add(method);
            }
        }
        // 返回setter方法列表
        return setterMethods;
    }

    /**
     * 获取对象的getter方法。
     *
     * @param obj 对象
     * @return 对象的getter方法列表
     */

    public static List<Method> getGetterMethods(Object obj)
    {
        // getter方法列表
        List<Method> getterMethods = new ArrayList<Method>();
        // 获取所有方法
        Method[] methods = obj.getClass().getMethods();
        // 查找getter方法
        for (Method method : methods)
        {
            Matcher m = GET_PATTERN.matcher(method.getName());
            if (m.matches() && (method.getParameterTypes().length == 0))
            {
                getterMethods.add(method);
            }
        }
        // 返回getter方法列表
        return getterMethods;
    }

    /**
     * 检查Bean方法名中的属性名是否相等。<br>
     * 如getName()和setName()属性名一样，getName()和setAge()属性名不一样。
     *
     * @param m1 方法名1
     * @param m2 方法名2
     * @return 属性名一样返回true，否则返回false
     */

    public static boolean isMethodPropEquals(String m1, String m2)
    {
        return m1.substring(BEAN_METHOD_PROP_INDEX).equals(m2.substring(BEAN_METHOD_PROP_INDEX));
    }


    /**
     * @param orin  源对象
     * @param target  目标对象
     */
    public static void copyProperties(final Object orin,final Object target){
        try{
            org.apache.commons.beanutils.BeanUtils.copyProperties(target, orin);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * @Description：拷贝list元素
     * @param orin 源list对象
     * @param target 目标list对象
     * @param orinClass 源list元素
     * @param targetClass 目标list元素
     * @param <T1> 源list元素类型
     * @param <T2> 目标list元素类型
     */
    public static <T1,T2> void copyProperties(final List<T1> orin, final List<T2> target, Class<T1> orinClass, Class<T2> targetClass){
        if(orin==null||target==null){
            return ;
        }
        if(target.size()!=0){
            //防止目标对象被覆盖，要求必须长度为零
            throw new RuntimeException("目标对象存在值");
        }
        try{
            for (T1 orig: orin) {
                T2 t = targetClass.newInstance();
                target.add(t);
                copyProperties(orig,t);
            }

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

}
