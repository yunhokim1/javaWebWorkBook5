package org.zerock.b01.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.b01.dto.ReplyDTO;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/replies")
public class ReplyController {

    @ApiOperation(value = "Replies POST", notes = "POST 방식으로 댓글 등록")
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> register(@Valid @RequestBody ReplyDTO replyDTO,
                                                      BindingResult bindingResult) throws BindException {
        log.info(String.valueOf(replyDTO));

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        HashMap<String, Long> resultMap = new HashMap<>();
        resultMap.put("rno", 111L);

        return resultMap;
    }
}
