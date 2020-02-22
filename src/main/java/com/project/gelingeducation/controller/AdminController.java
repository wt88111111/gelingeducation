package com.project.gelingeducation.controller;

import com.project.gelingeducation.domain.AdminInfo;
import com.project.gelingeducation.domain.JsonData;
import com.project.gelingeducation.service.AdminInfoService;
import com.project.gelingeducation.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * 管理员controller
 */
//跨域
//@CrossOrigin(origins = {"/"}, maxAge = 72000L)
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminInfoService adminInfoService;

    /**
     * 注册接口
     *
     * @param adminInfo
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Object register(@RequestBody AdminInfo adminInfo) {
        return JsonData.buildSuccess(adminInfoService.register(adminInfo));
    }

    /**
     * 登录接口
     *
     * @param adminInfo 用户账号密码
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object login(@RequestBody AdminInfo adminInfo) {
        return JsonData.buildSuccess(adminInfoService.login(adminInfo));
    }

    /**
     * 获取用户信息接口
     *
     * @param id 用户id
     * @return
     */
    @RequestMapping(value = "/getInfo", method = RequestMethod.GET)
    public Object getInfo(int id) {
        return JsonData.buildSuccess(adminInfoService.findById(id));
    }

    /**
     * 用户头像上传
     */
    @RequestMapping("/uploadIcon")
    public Object springUpload(HttpServletRequest request) throws IllegalStateException, IOException {
        String path = null;
        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        //检查form中是否有enctype="multipart/form-data"
        if (multipartResolver.isMultipart(request)) {
            //将request变成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            //获取multiRequest 中所有的文件名
            Iterator iter = multiRequest.getFileNames();
            String userId = multiRequest.getParameter("userId");
            String time = String.valueOf(System.currentTimeMillis());
            while (iter.hasNext()) {
                //一次遍历所有文件
                MultipartFile file = multiRequest.getFile(iter.next().toString());
                if (file != null) {
                    path = "D:/gelingeducation/admin/icon/" + time + userId + FileUtils.getSuffixName(file.getOriginalFilename());
                    //上传
                    file.transferTo(new File(path));
                }
            }

            adminInfoService.updateCoverImg(Long.valueOf(userId), time);
        } else {
            return JsonData.buildError("图片上传失败");
        }
        return JsonData.buildSuccess(path);
    }
}