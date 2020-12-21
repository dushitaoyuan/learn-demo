package com.taoyuanx.demo.thymeleaf;

import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

/**
 * @author dushitaoyuan
 * @date 2020/10/1122:05
 * @desc: thymeleaf 自定义扩展方言
 */
public class CustomExtendDialect extends AbstractDialect implements IExpressionObjectDialect {

    private final IExpressionObjectFactory DICT_EXPRESSION_OBJECTS_FACTORY = new DictExpressionFactory();

    public CustomExtendDialect() {
        super("customExtendDialect");
    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return DICT_EXPRESSION_OBJECTS_FACTORY;
    }
}
