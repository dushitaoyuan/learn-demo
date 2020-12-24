package com.taoyuanx.ognl;

import ognl.MemberAccess;
import ognl.Ognl;
import ognl.OgnlException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dushitaoyuan
 * @date 2020/12/24
 */
public class OgnlUtil {
    private static final DefaultMemberAccess MEMBER_ACCESS = new DefaultMemberAccess(true);
    private static final OgnlClassResolver CLASS_RESOLVER = new OgnlClassResolver();
    private static final Map<String, Object> expressionCache = new ConcurrentHashMap<>();

    private static final Integer EXPRESSION_CACHE_MAX_SIZE = 50000;


    public static Object getValue(String expression, Object root) {
        try {
            Map context = Ognl.createDefaultContext(root, MEMBER_ACCESS, CLASS_RESOLVER, null);
            return Ognl.getValue(parseExpression(expression), context, root);
        } catch (OgnlException e) {
            throw new RuntimeException("Error evaluating expression '" + expression + "'. Cause: " + e, e);
        }
    }


    private static Object parseExpression(String expression) throws OgnlException {
        Object node = expressionCache.get(expression);
        if (node == null) {
            node = Ognl.parseExpression(expression);
            if (expressionCache.size() > EXPRESSION_CACHE_MAX_SIZE) {
                expressionCache.clear();
            }
            expressionCache.put(expression, node);
        }
        return node;
    }
}
