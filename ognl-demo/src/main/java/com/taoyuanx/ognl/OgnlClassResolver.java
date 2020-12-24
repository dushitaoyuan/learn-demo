package com.taoyuanx.ognl;

import ognl.DefaultClassResolver;

public class OgnlClassResolver extends DefaultClassResolver {

    @Override
    protected Class toClassForName(String className) throws ClassNotFoundException {
        return classForName(className);
    }

    public Class<?> classForName(String className) throws ClassNotFoundException {
        ClassLoader classLoader[] = new ClassLoader[]{
                Thread.currentThread().getContextClassLoader(),
                getClass().getClassLoader(),
                ClassLoader.getSystemClassLoader()};
        for (ClassLoader cl : classLoader) {

            if (null != cl) {

                try {

                    return Class.forName(className, true, cl);

                } catch (ClassNotFoundException e) {
                    // we'll ignore this until all classloaders fail to locate the class
                }

            }

        }

        return null;
    }


}
