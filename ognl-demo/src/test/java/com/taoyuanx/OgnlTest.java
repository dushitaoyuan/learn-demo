package com.taoyuanx;


import com.taoyuanx.ognl.DefaultMemberAccess;
import com.taoyuanx.ognl.OgnlClassResolver;
import com.taoyuanx.ognl.OgnlUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class OgnlTest {


    @Test
    public void testOgnl() throws Exception {
        Map map = new HashMap();
        map.put("name", "1211");
        map.put("obj", map);
        System.out.println(OgnlUtil.getValue("name", map));
        System.out.println(OgnlUtil.getValue("obj.name", map));
    }

    @Test
    public void testOgnlObjectPrivate() throws Exception {
        Map map = new HashMap();
        map.put("name", "1211");
        map.put("obj", new DataModel("name111", "value111"));
        map.put("obj3", map);
        System.out.println(OgnlUtil.getValue("name", map));
        System.out.println(OgnlUtil.getValue("obj.name", map));
        System.out.println(OgnlUtil.getValue("obj.value", map));

        System.out.println(OgnlUtil.getValue("obj3.obj.name", map));

        System.out.println(OgnlUtil.getValue("obj3.obj.value", map));
        System.out.println(OgnlUtil.getValue("obj3.name", map));
    }


}
