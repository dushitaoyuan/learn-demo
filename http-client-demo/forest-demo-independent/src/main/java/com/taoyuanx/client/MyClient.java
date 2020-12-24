package com.taoyuanx.client;

import com.dtflys.forest.annotation.DataObject;
import com.dtflys.forest.annotation.DataParam;
import com.dtflys.forest.annotation.Post;

import java.util.Map;

public interface MyClient {

    @Post(url = "http://localhost:8080/hello")
    String hello(@DataParam("hello")String hello);
    @Post(url = "http://localhost:8080/json", contentType = "application/json",dataType = "json")
    Map json(@DataObject Map map);

}