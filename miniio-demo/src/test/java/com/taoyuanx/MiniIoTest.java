package com.taoyuanx;


import com.taoyuanx.demo.DemoBootApplication;
import com.taoyuanx.demo.config.MinioProperties;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoBootApplication.class)
@Slf4j
public class MiniIoTest {
    @Autowired
    MinioClient minioClient;
    @Autowired
    MinioProperties minioProperties;

    /**
     *
     */
    @Test
    public void testMiniIo() throws Exception {

        /**
         * 创建bucket
         */
        boolean isExist =
                minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucketName()).build());
        if (isExist) {
            System.out.println("Bucket already exists.");
        } else {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucketName()).build());
        }
        /**
         * 上传
         */
        UploadObjectArgs.Builder builder =
                UploadObjectArgs.builder().bucket(minioProperties.getBucketName()).object("fq.exe").filename("g://fq.exe");
        minioClient.uploadObject(builder.build());
        /**
         * 下载
         */
        GetObjectArgs getObjectArgs = GetObjectArgs.builder().bucket(minioProperties.getBucketName()).object("fq.exe").build();
        InputStream object = minioClient.getObject(getObjectArgs);
        IOUtils.copy(object, new FileOutputStream("g://download/fq.exe"));
        /**
         * 删除
         */
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(minioProperties.getBucketName()).object("fq.exe").build());

    }

    @Test
    public void encryptServerSideTest1() throws Exception {
        /**
         * 加密上传
         * 服务端加密(自定义密钥)
         *
         */
        KeyGenerator symKeyGenerator = KeyGenerator.getInstance("AES");
        symKeyGenerator.init(256);
        SecretKey aes256Key = symKeyGenerator.generateKey();
        UploadObjectArgs uploadEncrypt = UploadObjectArgs.builder().bucket(minioProperties.getBucketName()).object("fq_encrypt.exe")
                .sse(ServerSideEncryption.withCustomerKey(aes256Key)).filename("g://fq.exe").build();
        minioClient.uploadObject(uploadEncrypt);
        ServerSideEncryptionCustomerKey serverSideEncryptionCustomerKey = ServerSideEncryptionCustomerKey.withCustomerKey(aes256Key);
        GetObjectArgs encryptDownloadObjectArgs = GetObjectArgs.builder().bucket(minioProperties.getBucketName())
                .ssec(serverSideEncryptionCustomerKey).object("fq_encrypt.exe")
                .build();
        /**
         * 自动下载解密
         */
        InputStream encryptDownloadObject = minioClient.getObject(encryptDownloadObjectArgs);
        IOUtils.copy(encryptDownloadObject, new FileOutputStream("g://download/fq_e.exe"));
    }

    @Test
    public void encryptServerSideTest2() throws Exception {
        /**
         * 加密上传
         * 服务端密钥管理
         *
         */
        String serverKeyId = "default-key";
        UploadObjectArgs uploadEncrypt = UploadObjectArgs.builder().bucket(minioProperties.getBucketName()).object("fq_encrypt_2.exe")
                .sse(ServerSideEncryption.withManagedKeys(serverKeyId, null)).filename("g://fq.exe").build();
        minioClient.uploadObject(uploadEncrypt);
        ServerSideEncryption serverSideEncryption = ServerSideEncryptionCustomerKey.withManagedKeys(serverKeyId, null);
        GetObjectArgs encryptDownloadObjectArgs = GetObjectArgs.builder().bucket(minioProperties.getBucketName())
               .object("fq_encrypt.exe")
                .build();
        /**
         * 自动下载解密
         */
        InputStream encryptDownloadObject = minioClient.getObject(encryptDownloadObjectArgs);
        IOUtils.copy(encryptDownloadObject, new FileOutputStream("g://download/fq_e2.exe"));
    }

    @Test
    public void visitTest() throws Exception {
        /**
         * 访问
         */
        UploadObjectArgs.Builder builder =
                UploadObjectArgs.builder().bucket(minioProperties.getBucketName()).object("img/demo.png").filename("g://demo.png");
        minioClient.uploadObject(builder.build());
        /**
         * 获取对象url 通过界面设置 prefix read/write
         */
        String objectUrl = minioClient.getObjectUrl(minioProperties.getBucketName(), "img/demo.png");
        System.out.println(objectUrl);
        /**
         * 获取临时临时链接
         */
        String presignedObjectUrl = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().object("img/demo.png").expiry(15, TimeUnit.MINUTES).method(Method.GET).bucket(minioProperties.getBucketName()).build());
        System.out.println(presignedObjectUrl);
    }
}
