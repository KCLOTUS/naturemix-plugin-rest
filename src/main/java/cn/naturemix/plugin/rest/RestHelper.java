package cn.naturemix.plugin.rest;

import cn.naturemix.framework.helper.BeanHelper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.apache.cxf.jaxrs.provider.jsonp.JsonpInInterceptor;
import org.apache.cxf.jaxrs.provider.jsonp.JsonpPostStreamInterceptor;
import org.apache.cxf.jaxrs.provider.jsonp.JsonpPreStreamInterceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.rs.security.cors.CrossOriginResourceSharingFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * Rest助手类
 * @author flytoyou
 * @version 1.0.0
 */
public class RestHelper {

    private static final List<Object> providerList = new ArrayList<Object>();
    private static final List<Interceptor<? extends Message>> inInterceptorList = new ArrayList<Interceptor<? extends Message>>();
    private static final List<Interceptor<? extends Message>> outInterceptorList = new ArrayList<Interceptor<? extends Message>>();

    static {
        //添加JSON Provider
        JacksonJsonProvider jsonProvider = new JacksonJsonProvider();
        providerList.add(jsonProvider);
        //添加Logging Interceptor
        if (RestConfig.isLog()){
            LoggingInInterceptor loggingInInterceptor = new LoggingInInterceptor();
            inInterceptorList.add(loggingInInterceptor);
            LoggingOutInterceptor loggingOutInterceptor = new LoggingOutInterceptor();
            outInterceptorList.add(loggingOutInterceptor);
        }
        //添加JSON Interceptor
        if (RestConfig.isJsonp()){
            JsonpInInterceptor jsonpInInterceptor = new JsonpInInterceptor();
            jsonpInInterceptor.setCallbackParam(RestConfig.getJsonpFuction());
            inInterceptorList.add(jsonpInInterceptor);
            JsonpPreStreamInterceptor jsonpPreStreamInterceptor = new JsonpPreStreamInterceptor();
            outInterceptorList.add(jsonpPreStreamInterceptor);
            JsonpPostStreamInterceptor jsonpPostStreamInterceptor = new JsonpPostStreamInterceptor();
            outInterceptorList.add(jsonpPostStreamInterceptor);
        }
        //添加CORE Provider
        if (RestConfig.isCors()){
            CrossOriginResourceSharingFilter crosProvider = new CrossOriginResourceSharingFilter();
            crosProvider.setAllowOrigins(RestConfig.getCrosOriginList());
            providerList.add(crosProvider);
        }
    }

    //发布Rest服务
    public static void publishService(String wsdl,Class<?> resourceClass){
        JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
        factory.setAddress(wsdl);
        factory.setResourceClasses(resourceClass);
        factory.setResourceProvider(resourceClass,new SingletonResourceProvider(BeanHelper.getBean(resourceClass)));
        factory.setProviders(providerList);
        factory.setInInterceptors(inInterceptorList);
        factory.setOutInterceptors(outInterceptorList);
        factory.create();
    }

    //创建Rest客户端
    public static <T> T createClient(String wsdl,Class<? extends T> resourceClass){
        return JAXRSClientFactory.create(wsdl,resourceClass,providerList);
    }

}
