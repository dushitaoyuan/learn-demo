package com.taoyuanx.demo.thymeleaf;

import com.taoyuanx.demo.thymeleaf.func.DictHelper;
import com.taoyuanx.demo.utils.SpringContextUtil;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.expression.IExpressionObjectFactory;

import java.util.*;

/**
 * 字典表达式解析
 */
public class DictExpressionFactory implements IExpressionObjectFactory {

    public static final String DICT_LIST_EXPRESSION_NAME = "dict";

    private static final Set<String> ALL_EXPRESSION_OBJECT_NAMES = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(DICT_LIST_EXPRESSION_NAME)));

    @Override
    public Set<String> getAllExpressionObjectNames() {
        return ALL_EXPRESSION_OBJECT_NAMES;
    }

    @Override
    public Object buildObject(IExpressionContext context, String expressionObjectName) {
        if (null == expressionObjectName) {
            return null;
        }
        switch (expressionObjectName) {
            case DICT_LIST_EXPRESSION_NAME:
                return getDitHelper();
        }
        return null;
    }

    @Override
    public boolean isCacheable(String expressionObjectName) {
        return DICT_LIST_EXPRESSION_NAME != null && DICT_LIST_EXPRESSION_NAME.equals(expressionObjectName);
    }

    private DictHelper dictHelper = null;

    private Object getDitHelper() {
        if (Objects.isNull(dictHelper)) {
            dictHelper = SpringContextUtil.getBean(DictHelper.class);
        }
        return dictHelper;
    }
}

