package com.gm.javaeaseframe.transaction.spring.boot.autoconfigure.properties;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.gm.javaeaseframe.common.code.PlatformConstants;

@ConfigurationProperties(PlatformConstants.PLATFORM_CONFIG_PREFIX + ".transaction")
public class TransactionProperties {

	/**
     * 切入点（可以有多个）
     * 格式必须符合要求
     */
    private Set<String> pointcut = new HashSet<>();
//	/** 全局事务位置配置,配置切入点表达式 */
//	private String poitcutExpression = "execution(* com.gmframework..*Service.*(..))";
	/** 事务方法 */
	private List<TransactionMethod> methods;
	
	public void setPointcut(Set<String> pointcut) {
        this.pointcut = pointcut;
    }
	/**
     * 获取切入点表达式（可以有多个组合）
     * -----------------------------------
     * execution(...) || execution(...)
     * -----------------------------------
     * @return
     */
	public String getPoitcutExpression() {

        StringBuilder sb = new StringBuilder();
        //在Spring项目开发过程中，经常会遇到各种基类（super class），比如各种GenericDao ,BaseDao，模板模式类中定义的方法，这些基类有若干个子类实现，
        //若想通过AOP方式关注某个具体子类执行某个基类方法的执行，则需要采取正确的姿势来定义pointcut，否则会无法正确拦截，因为Spring AOP 默认忽略继承自父类的方法
        /** spring aop只能代理目标类存在的方法，不能代理目标类父类的方法，从而导致有多级service实现类时，会出现事物失效。解决办法如下：
         * 1、不使用多级service实现类，只保留一级。
         * 2、把需要使用事务的父类方法，在子类再次声明，子类的实现使用,super.父类方法
         * 3、在需要使用事务的父类方法上添加@Transactional或将父类方法加入事务拦截规则（本工具采用该方案）
        */
        // 默认切入点：【com.gm】包及子包下，所有以【Impl】结尾的类的所有方法 "execution(* com.gm.service..*Impl.*(..))";
        sb.append("execution(* com.gm..*Service.*(..))");
        // 组合多个切入点
        for (String p : this.pointcut) {
        	if (StringUtils.isBlank(p)) {
        		continue;
        	}
        	if (sb.length() > 0) {
        		sb.append(" || ");
        	}
        	sb.append("execution(").append(p).append(")");
        }
        return sb.toString();
	}
	public List<TransactionMethod> getMethods() {
		return methods;
	}
	public void setMethods(List<TransactionMethod> methods) {
		this.methods = methods;
	}
}
