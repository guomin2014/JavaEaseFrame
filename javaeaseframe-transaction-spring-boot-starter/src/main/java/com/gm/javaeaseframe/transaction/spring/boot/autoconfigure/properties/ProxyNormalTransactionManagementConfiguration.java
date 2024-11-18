package com.gm.javaeaseframe.transaction.spring.boot.autoconfigure.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.NoRollbackRuleAttribute;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Configuration(proxyBeanMethods = false)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class ProxyNormalTransactionManagementConfiguration extends AbstractNormalTransactionManagementConfiguration {
	
	@Autowired
    private TransactionProperties transactionProperties;

	@Bean()
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	public AbstractPointcutAdvisor normalTransactionAdvisor(TransactionAttributeSource normalTransactionAttributeSource, TransactionInterceptor normalTransactionInterceptor) {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(transactionProperties.getPoitcutExpression());
        return new DefaultPointcutAdvisor(pointcut, normalTransactionInterceptor);
	}

	@Bean
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	public TransactionAttributeSource normalTransactionAttributeSource() {
//		return new AnnotationTransactionAttributeSource();
		Map<String, TransactionAttribute> nameMap = new HashMap<>();
		List<TransactionMethod> methods = transactionProperties.getMethods();
        if (methods != null) {
        	for (TransactionMethod method : methods) {
        		if (StringUtils.isBlank(method.getName())) {
        			throw new IllegalArgumentException("normal.transaction.methods.name cannot be empty");
        		}
        		if (StringUtils.isBlank(method.getPropagation())) {
        			throw new IllegalArgumentException("normal.transaction.methods.propagation cannot be empty");
        		}
        		String propagation = method.getPropagation();
        		String isolation = method.getIsolation();
        		RuleBasedTransactionAttribute rule = new RuleBasedTransactionAttribute();
        		rule.setReadOnly(method.isReadOnly());
        		rule.setPropagationBehaviorName(DefaultTransactionDefinition.PREFIX_PROPAGATION + propagation.toUpperCase());
        		rule.setTimeout(method.getTimeout());
        		if (StringUtils.isNotBlank(isolation)) {
        			rule.setIsolationLevelName(DefaultTransactionDefinition.PREFIX_ISOLATION + isolation.toUpperCase());
        		}
        		String rollbackRules = method.getRollbackFor();
        		String noRollbackRules = method.getNoRollbackFor();
        		List<RollbackRuleAttribute> ruleList = new ArrayList<>();
        		if (StringUtils.isNotBlank(rollbackRules)) {
        			String[] rrules = rollbackRules.split(",");
        			for (String r : rrules) {
        				if (StringUtils.isNotBlank(r)) {
        					ruleList.add(new RollbackRuleAttribute(r.trim()));
        				}
        			}
        		}
        		if (StringUtils.isNotBlank(noRollbackRules)) {
        			String[] rrules = noRollbackRules.split(",");
        			for (String r : rrules) {
        				if (StringUtils.isNotBlank(r)) {
        					ruleList.add(new NoRollbackRuleAttribute(r.trim()));
        				}
        			}
        		}
        		if (!ruleList.isEmpty()) {
    				rule.setRollbackRules(ruleList);
    			}
        		nameMap.put(method.getName(), rule);
        	}
        }
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        source.setNameMap(nameMap);
        return source;
	}

	@Bean
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	public TransactionInterceptor normalTransactionInterceptor(TransactionAttributeSource normalTransactionAttributeSource) {
		TransactionInterceptor interceptor = new TransactionInterceptor();
		interceptor.setTransactionAttributeSource(normalTransactionAttributeSource);
		if (this.txManager != null) {
			interceptor.setTransactionManager(this.txManager);
		}
		return interceptor;
	}
}
