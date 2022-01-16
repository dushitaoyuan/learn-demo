package com.ncs.single.mvc.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.ncs.commons.api.MapResultBuilder;
import com.ncs.commons.utils.PropertiesUtil;
import com.ncs.single.mvc.commons.ConfigConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author dushitaoyuan
 * @desc 例子
 * @date 2019/12/17
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    @GetMapping
    public MapResultBuilder demo(){
        return MapResultBuilder.newBuilder(1).put("name",PropertiesUtil.getSystemProperty(ConfigConstants.APPLICATION_NAME));
    }


    @Autowired
    private Producer captchaProducer;
    @GetMapping("verification")
    public void verification(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        String capText = captchaProducer.createText();
        request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
    }

}
