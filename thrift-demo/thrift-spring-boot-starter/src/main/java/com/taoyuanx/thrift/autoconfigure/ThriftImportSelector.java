package com.taoyuanx.thrift.autoconfigure;

import com.taoyuanx.thrift.core.server.ThriftServerConfigHandler;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import com.taoyuanx.thrift.core.client.ThriftClientHandler;

import java.util.ArrayList;
import java.util.List;

public class ThriftImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        boolean enableThriftClient = importingClassMetadata.hasAnnotation(EnableThriftClient.class.getName());
        boolean enableThriftServer = importingClassMetadata.hasAnnotation(EnableThriftServer.class.getName());
        List<String> selectImports = new ArrayList<>();
        if (enableThriftClient) {
            selectImports.add(ThriftClientHandler.class.getName());
        }
        if (enableThriftServer) {
            selectImports.add(ThriftServerConfigHandler.class.getName());
        }
        return selectImports.toArray(new String[selectImports.size()]);

    }

}