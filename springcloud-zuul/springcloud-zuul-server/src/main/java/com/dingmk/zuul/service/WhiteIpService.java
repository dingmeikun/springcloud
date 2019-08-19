package com.dingmk.zuul.service;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WhiteIpService implements BeanFactoryPostProcessor{

	private AtomicReference<HashMap<String, String>> WHITE_IP_REF = new AtomicReference<>(new HashMap<String,String>());

	private static final String whiteIpListStrs = new String( "192.168.101.15;192.168.101.213;192.168.101.214" );
	
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		reload();
	}
	
    @PostConstruct
    public void reload() {
    	String[] whiteIpArray = whiteIpListStrs.split(";");
        if(null == whiteIpArray || whiteIpArray.length == 0) {
            log.error("WHITEIP_ARRAY_IS_EMPTY");
            return;
        }
    	
		HashMap<String, String> whiteIpMap = new HashMap<>();
		for (String ip : whiteIpArray) {
            if (null != ip && (ip = ip.trim()).length() > 0) {
            	log.info("WHITE_IP:{}", ip);
            	whiteIpMap.put(ip, null);
            }
        }
		
		if(whiteIpMap.isEmpty()){
        	log.error("WHITEIP_MAP_IS_EMPTY");
        	return;
        }
		
		log.info("WHITEIP_MAP:{}", whiteIpMap);
        
        WHITE_IP_REF.set(whiteIpMap);
	}
    
    public boolean checkIp(String clientIp) {
        if (null == clientIp) {
            return false;
        }

        String[] array = clientIp.split(",");
        if (null == array || array.length == 0) {
            return false;
        }

        clientIp = array[0];

        if (null == clientIp || (clientIp = clientIp.trim()).isEmpty()) {
            return false;
        }

        return this.WHITE_IP_REF.get().containsKey(clientIp);
    }
    
    /**
 	 *  IP地址校验
     * 
     * @param text ip地址
     * @return 验证信息
     */
    public boolean ipCheck(String ipStr) {
    	if (StringUtils.hasText(ipStr)) {
    		String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
    		
    		if (ipStr.length() < 15) {
    			return false;
    		}
    		
    		Pattern pattern = Pattern.compile(regex);
    	    Matcher matcher = pattern.matcher(ipStr);
    	    return matcher.matches();
    	}
    	return false;
    }
}
