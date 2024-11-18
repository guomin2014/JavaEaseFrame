package com.gm.javaeaseframe.transaction.spring.boot.autoconfigure.properties;

import java.util.Set;

import org.springframework.aop.config.AopConfigUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

public class CustomAdvisorRegistrar implements ImportBeanDefinitionRegistrar {

	@Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		
		boolean candidateFound = false;
		Set<String> annoTypes = importingClassMetadata.getAnnotationTypes();
		for (String annoType : annoTypes) {
			AnnotationAttributes candidate = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(annoType));//AnnotationConfigUtils.attributesFor(importingClassMetadata, annoType);
			if (candidate == null) {
				continue;
			}
			Object mode = candidate.get("mode");
			Object proxyTargetClass = candidate.get("proxyTargetClass");
			if (mode != null && proxyTargetClass != null && AdviceMode.class == mode.getClass() &&
					Boolean.class == proxyTargetClass.getClass()) {
				candidateFound = true;
				if (mode == AdviceMode.PROXY) {
					AopConfigUtils.registerAutoProxyCreatorIfNecessary(registry);
					if ((Boolean) proxyTargetClass) {
						AopConfigUtils.forceAutoProxyCreatorToUseClassProxying(registry);
						return;
					}
				}
			}
		}
		
//		/* 只读事物、不做更新删除等 */
//		/* 事务管理规则 */
//		RuleBasedTransactionAttribute readOnlyRule = new RuleBasedTransactionAttribute();
//		/* 设置当前事务是否为只读事务，true为只读 */
//		readOnlyRule.setReadOnly(true);
//		/*
//		 * transactiondefinition 定义事务的隔离级别； 如果当前存在事务，则加入该事务；如果当前没有事务，则以非事务的方式继续运行。
//		 */
//		readOnlyRule.setPropagationBehavior(TransactionDefinition.PROPAGATION_SUPPORTS);
//		/* 增删改事务规则 */
//		RuleBasedTransactionAttribute requireRule = new RuleBasedTransactionAttribute();
//		/* 抛出异常后执行切点回滚 建议自定义异常 */
//		requireRule.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
//		/* PROPAGATION_REQUIRED:事务隔离性为1，若当前存在事务，则加入该事务；如果当前没有事务，则创建一个新的事务。这是默认值。 */
//		requireRule.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//		/* 设置事务失效时间，超过10秒 */
//		requireRule.setTimeout(10);
//		Map<String, TransactionAttribute> nameMap = new HashMap<>();
//		nameMap.put("add*", requireRule);
//		nameMap.put("save*", requireRule);
//		nameMap.put("insert*", requireRule);
//		nameMap.put("update*", requireRule);
//		nameMap.put("delete*", requireRule);
//		nameMap.put("remove*", requireRule);
//		NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
//		source.setNameMap(nameMap);
//
//		BeanDefinition adviceBean = BeanDefinitionBuilder.genericBeanDefinition(TransactionInterceptor.class)
//				// 定义输出路径,一般是日志的输出类,方便排查问题
//				// .addPropertyValue("location", "$$transactionInterceptor##")
//				// 定义AspectJ切点表达式
//				.addPropertyValue("transactionAttributeSource", source)
//				// 定义织入的增强对象,就是上面的自定义的around类型的advice的实现
//				.addPropertyReference("transactionManager", "transactionManager").getBeanDefinition();
//		registry.registerBeanDefinition("txAdvice", adviceBean);
//
//		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
//		pointcut.setExpression("execution(* com.easydcloud..*Service.*(..)) || execution(* com.gm.normal..*Service.*(..))");
//		BeanDefinition aspectJBean = BeanDefinitionBuilder.genericBeanDefinition(DefaultPointcutAdvisor.class)
//				// 定义输出路径,一般是日志的输出类,方便排查问题
//				// .addPropertyValue("location", "$$aspectJAdvisor##")
//				// 定义AspectJ切点表达式
//				.addPropertyValue("pointcut", pointcut)
//				// 定义织入的增强对象,就是上面的自定义的around类型的advice的实现
//				.addPropertyReference("advice", "txAdvice").getBeanDefinition();
//		registry.registerBeanDefinition("txAdviceAdvisor", aspectJBean);
	}

}
