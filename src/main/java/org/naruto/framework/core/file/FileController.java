package org.naruto.framework.core.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.naruto.framework.core.web.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class FileController {

    @Autowired
    FileService fileService;

    @ResponseBody
    @RequestMapping(value = "/v1/file/upload", method = RequestMethod.POST)
    public ResponseEntity<ResultEntity> uploadFile(@RequestParam("file") MultipartFile file) throws Exception{

        return ResponseEntity.ok(ResultEntity.ok(fileService.uploadFile(file)));
    }
}
