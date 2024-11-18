package com.gm.javaeaseframe.transaction.spring.boot.autoconfigure.annotation;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;
import org.springframework.transaction.config.TransactionManagementConfigUtils;

import com.gm.javaeaseframe.transaction.spring.boot.autoconfigure.properties.CustomAdvisorRegistrar;
import com.gm.javaeaseframe.transaction.spring.boot.autoconfigure.properties.ProxyNormalTransactionManagementConfiguration;

public class NormalTransactionManagementConfigurationSelector extends AdviceModeImportSelector<EnableNormalTransactionManagement> {

	@Override
	protected String[] selectImports(AdviceMode adviceMode) {
		switch (adviceMode) {
		case PROXY:
			return new String[] {CustomAdvisorRegistrar.class.getName(), ProxyNormalTransactionManagementConfiguration.class.getName()};
		case ASPECTJ:
			return new String[] {TransactionManagementConfigUtils.TRANSACTION_ASPECT_CONFIGURATION_CLASS_NAME};
		default:
			return null;
	}
	}

}
