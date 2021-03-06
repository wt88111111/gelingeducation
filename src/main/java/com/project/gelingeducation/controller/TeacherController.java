package com.project.gelingeducation.controller;

import com.project.gelingeducation.common.annotation.Log;
import com.project.gelingeducation.common.dto.JsonResult;
import com.project.gelingeducation.entity.Teacher;
import com.project.gelingeducation.service.ITeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

/**
 * @Author: LL
 * @Description: 教师实体类的Controller
 */
@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    /**
     * 教师实体类的service
     */
    @Autowired
    private ITeacherService teacherService;

    /**
     * 获取教师列表
     *
     * @param currentPage 页码
     * @param pageSize    页数
     * @return 页码为空返回全都list，不为空返回分页实体类
     */
    @Log("获取老师")
    @RequestMapping("/lists")
    public Object queryAll(@RequestParam(required = false) Integer currentPage,
                           @RequestParam(required = false) Integer pageSize) {
        return JsonResult.buildSuccess(teacherService.queryAll(currentPage, pageSize));
    }

    /**
     * 删除教师
     *
     * @param id 教师id
     * @return /
     */
    @Log("删除老师")
    @RequestMapping(value = "/delete")
    public Object delete(Long id) {
        teacherService.delTeacher(id);
        return JsonResult.buildSuccess();
    }

    /**
     * 添加教师
     *
     * @param teacher 教师实体类
     * @return /
     */
    @Log("添加老师")
    @PostMapping("/add")
    public Object addTeacher(@RequestBody Teacher teacher) {
        teacherService.save(teacher);
        return JsonResult.buildSuccess();
    }

    /**
     * 更新教师
     *
     * @param teacher 教师实体类
     * @return /
     */
    @Log("更新教师")
    @PostMapping("/update")
    public Object updateTeacher(@RequestBody Teacher teacher) {
        teacherService.update(teacher);
        return JsonResult.buildSuccess();
    }

    /**
     * 批量删除教师
     *
     * @param ids 1,2,3 格式的专题id
     * @return
     */
    @Log("批量删除教师")
    @RequestMapping(value = "/batches_delete", method = RequestMethod.GET)
    public Object batchesDeletes(String ids) {
        teacherService.delMore(ids);
        return JsonResult.buildSuccess();
    }

    /**
     * 搜索教师列表
     *
     * @param name 教师名
     * @return
     */
    @Log("搜索教师列表")
    @RequestMapping("/search")
    public Object searchCriteria(@RequestParam String name,
                                 @RequestParam Integer currentPage,
                                 @RequestParam Integer pageSize) throws UnsupportedEncodingException {
        return JsonResult.buildSuccess(teacherService.searchCriteria(name, currentPage, pageSize));
    }
}
