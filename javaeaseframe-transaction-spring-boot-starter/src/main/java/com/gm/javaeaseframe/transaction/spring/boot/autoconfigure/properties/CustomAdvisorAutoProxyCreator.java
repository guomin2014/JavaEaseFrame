package com.gm.javaeaseframe.transaction.spring.boot.autoconfigure.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.autoproxy.AbstractAdvisorAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.NoRollbackRuleAttribute;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class CustomAdvisorAutoProxyCreator extends AbstractAdvisorAutoProxyCreator {

	private static final long serialVersionUID = -2631839902683736912L;
	
	@Autowired
    private TransactionProperties transactionProperties;
	@Autowired
    private TransactionManager transactionManager;

	@Override
	protected void initBeanFactory(ConfigurableListableBeanFactory beanFactory) {
//		System.out.println("****CustomAdvisorAutoProxyCreator****initBeanFactory");
		super.initBeanFactory(beanFactory);
//		System.out.println("****CustomAdvisorAutoProxyCreator****initBeanFactory-->" + beanFactory.getBean(TransactionManager.class));
	}

	@Override
	protected List<Advisor> findCandidateAdvisors() {
		List<Advisor> advisors = super.findCandidateAdvisors();
		System.out.println("****CustomAdvisorAutoProxyCreator****findCandidateAdvisors-->" + advisors);
		if (advisors.size() <= 1) {
			advisors.add(this.txAdviceAdvisor());
		}
		return advisors;
	}
	
//	@Bean
    public TransactionInterceptor txAdvice() {
//        /*只读事物、不做更新删除等*/
//        /*事务管理规则*/
//        RuleBasedTransactionAttribute readOnlyRule = new RuleBasedTransactionAttribute();
//        /*设置当前事务是否为只读事务，true为只读*/
//        readOnlyRule.setReadOnly(true);
//        /* transactiondefinition 定义事务的隔离级别；
//         *如果当前存在事务，则加入该事务；如果当前没有事务，则以非事务的方式继续运行。*/
//        readOnlyRule.setPropagationBehavior(TransactionDefinition.PROPAGATION_SUPPORTS);
//         /*增删改事务规则*/
//        RuleBasedTransactionAttribute requireRule = new RuleBasedTransactionAttribute();
//        /*抛出异常后执行切点回滚 建议自定义异常*/
//        requireRule.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
//        /*PROPAGATION_REQUIRED:事务隔离性为1，若当前存在事务，则加入该事务；如果当前没有事务，则创建一个新的事务。这是默认值。 */
//        requireRule.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//        /*设置事务失效时间，超过10秒*/
//        requireRule.setTimeout(TX_METHOD_TIME_OUT);

        /** 配置事务管理规则
         nameMap声明具备需要管理事务的方法名.
         这里使用addTransactionalMethod  使用setNameMap
         */
        Map<String, TransactionAttribute> nameMap = new HashMap<>();
//        nameMap.put("add*", requireRule);
//        nameMap.put("save*", requireRule);
//        nameMap.put("insert*", requireRule);
//        nameMap.put("update*", requireRule);
//        nameMap.put("delete*", requireRule);
//        nameMap.put("remove*", requireRule);
//        /*进行批量操作时*/
//        nameMap.put("batch*", requireRule);
//        nameMap.put("get*", readOnlyRule);
//        nameMap.put("query*", readOnlyRule);
//        nameMap.put("find*", readOnlyRule);
//        nameMap.put("select*", readOnlyRule);
//        nameMap.put("count*", readOnlyRule);
//        nameMap.put("*", readOnlyRule);
        
        List<TransactionMethod> methods = transactionProperties.getMethods();
        if (methods != null) {
        	for (TransactionMethod method : methods) {
        		if (StringUtils.isBlank(method.getName())) {
        			throw new IllegalArgumentException("gm.normal.transaction.methods.name cannot be empty");
        		}
        		if (StringUtils.isBlank(method.getPropagation())) {
        			throw new IllegalArgumentException("gm.normal.transaction.methods.propagation cannot be empty");
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
//        TransactionInterceptor transactionInterceptor = new TransactionInterceptor(transactionManager, source);
        TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
        transactionInterceptor.setTransactionAttributeSource(source);
        if (this.transactionManager != null) {
        	transactionInterceptor.setTransactionManager(this.transactionManager);
        }
        return transactionInterceptor;
    }
    /**
     * 设置切面=切点pointcut+通知TxAdvice
     * @Role 解决问题：not eligible for auto-proxying
     * @return
     */
//    @Bean
    public Advisor txAdviceAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(transactionProperties.getPoitcutExpression());
        return new DefaultPointcutAdvisor(pointcut, txAdvice());
    }

}
