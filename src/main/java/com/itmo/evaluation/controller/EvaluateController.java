package com.itmo.evaluation.controller;

import com.itmo.evaluation.common.BaseResponse;
import com.itmo.evaluation.common.ErrorCode;
import com.itmo.evaluation.common.ResultUtils;
import com.itmo.evaluation.exception.BusinessException;
import com.itmo.evaluation.model.dto.evaluation.EvaluationPushRequest;
import com.itmo.evaluation.model.dto.evaluation.EvaluationRequest;
import com.itmo.evaluation.model.vo.Evaluate.EvaluateTeacherVo;
import com.itmo.evaluation.service.EvaluateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 评测相关接口
 */
@RestController
@Slf4j
@RequestMapping("/evaluation")
public class EvaluateController {

    @Resource
    private EvaluateService evaluateService;

    /**
     * 展示所有教师信息
     * @param token token
     * @param evaluationRequest 请求体（cid）
     * @return 教师信息
     */
    @PostMapping("/teacher/list")
    public BaseResponse<List<EvaluateTeacherVo>> getAllTeacherInfo(@RequestHeader("token") String token, @RequestBody EvaluationRequest evaluationRequest) {
        if (evaluationRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Integer cid = evaluationRequest.getCid();
        List<EvaluateTeacherVo> evaluateTeacherVoList = evaluateService.listAllTeacher(cid, token);
        if (evaluateTeacherVoList == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "您没有该课程信息");
        }
        return ResultUtils.success(evaluateTeacherVoList);
    }

    @PostMapping("/push")
    public BaseResponse<Boolean> pushEvaluation(@RequestHeader("token") String token, @RequestBody List<EvaluationPushRequest> evaluationPushRequestList) {

        Boolean save = evaluateService.pushEvaluation(evaluationPushRequestList, token);

        return ResultUtils.success(save);
    }

}
