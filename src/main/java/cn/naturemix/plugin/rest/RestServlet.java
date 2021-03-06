package cn.naturemix.plugin.rest;

import cn.naturemix.framework.helper.ClassHelper;
import cn.naturemix.framework.util.CollectionUtil;
import cn.naturemix.framework.util.StringUtil;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;

import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import java.util.Set;

/**
 * Rest Servlet
 * @author flytoyou
 * @version 1.0.0
 */
@WebServlet(urlPatterns = RestConstant.SERVLET_URL,loadOnStartup = 0)
public class RestServlet extends CXFNonSpringServlet{

    @Override
    protected void loadBus(ServletConfig sc){
        //初始化CXF总线
        super.loadBus(sc);
        Bus bus = getBus();
        BusFactory.setDefaultBus(bus);
        //发布Rest服务
        publishRestService();
    }

    private void publishRestService() {
        //遍历所有标注了Rest注解的类
        Set<Class<?>> restClassSet = ClassHelper.getClassSetByAnnotation(Rest.class);
        if (CollectionUtil.isNotEmpty(restClassSet)){
            for (Class<?> restClass : restClassSet){
                //获取Rest地址
                String address = getAddress(restClass);
                //发布Rest服务
                RestHelper.publishService(address,restClass);
            }
        }
    }

    private String getAddress(Class<?> restClass) {
        String address;
        //若Rest注解类的value属性不为空，则获取当前值，否则获取类名
        String value = restClass.getAnnotation(Rest.class).value();
        if (StringUtil.isNotEmpty(value)){
            address = value;
        }else {
            address = restClass.getSimpleName();
        }
        //保证最前面只有一个/
        if (!address.startsWith("/")){
            address = "/" +address;
        }
        address = address.replaceAll("\\/+","/");
        return address;
    }

}
