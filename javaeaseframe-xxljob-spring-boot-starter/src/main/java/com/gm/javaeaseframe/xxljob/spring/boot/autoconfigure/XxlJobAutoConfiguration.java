package com.gm.javaeaseframe.xxljob.spring.boot.autoconfigure;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gm.javaeaseframe.common.util.NetUtil;
import com.gm.javaeaseframe.xxljob.spring.boot.autoconfigure.properties.XxlJobProperties;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({XxlJobProperties.class})
public class XxlJobAutoConfiguration {

	@Autowired
	private XxlJobProperties xxlJobProperties;
	
	@Bean
	@ConditionalOnMissingBean
    public XxlJobSpringExecutor xxlJobExecutor() {
		String localIp = xxlJobProperties.getIp();
		if (StringUtils.isBlank(localIp)) {
			localIp = NetUtil.getLocalHostIP();
		}
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(xxlJobProperties.getAdminAddresses());
        xxlJobSpringExecutor.setAppname(xxlJobProperties.getAppname());
        xxlJobSpringExecutor.setAccessToken(xxlJobProperties.getAccessToken());
        xxlJobSpringExecutor.setIp(localIp);
        xxlJobSpringExecutor.setPort(xxlJobProperties.getPort());
        xxlJobSpringExecutor.setAddress(xxlJobProperties.getAddress());
        xxlJobSpringExecutor.setLogPath(xxlJobProperties.getLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(xxlJobProperties.getLogRetentionDays());
        return xxlJobSpringExecutor;
    }
}
