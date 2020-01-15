package com.project.gelingeducation.controller;

import com.project.gelingeducation.domain.JsonData;
import com.project.gelingeducation.domain.Subject;
import com.project.gelingeducation.service.SubjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/subject")
@RestController
@Slf4j
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/findall")
    public Object findAll() throws Exception {
        return subjectService.findAll();
    }

    @RequestMapping(value = "/findbyid", method = RequestMethod.POST)
    public Object findById(long id) throws Exception {
        return subjectService.findById(id);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object add(Subject subject) {
        subjectService.insert(subject);
        return JsonData.buildSuccess();
    }

    @RequestMapping(value = "/delect", method = RequestMethod.POST)
    public Object delect(long id) {
        subjectService.delectd(id);
        return JsonData.buildSuccess();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Object update(Subject subject) {
        subjectService.updated(subject);
        return JsonData.buildSuccess();
    }

}
