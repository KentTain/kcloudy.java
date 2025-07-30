package kc.service.webapiservice.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import kc.dto.account.OrganizationSimpleDTO;
import kc.dto.config.ConfigEntityDTO;
import kc.dto.search.RootOrgWithUsersSearchDTO;
import kc.dto.search.WorkflowTaskSearchDTO;
import kc.dto.workflow.*;
import kc.framework.base.SeedEntity;
import kc.framework.enums.ConfigType;
import kc.framework.tenant.ApplicationConstant;
import kc.framework.util.SerializeHelper;
import kc.service.base.ServiceResult;
import kc.service.webapiservice.IWorkflowApiService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@lombok.extern.slf4j.Slf4j
public class WorkflowApiService extends IdSrvWebApiServiceBase implements IWorkflowApiService {
    private final static String ServiceName = "kc.service.webapiservice.impl.WorkflowApiService";

    /**
     * 启动流程前，根据流程定义编码及提交的表单数据，获取流程启动执行数据 </br>
     *      包括：启动后的下一审批环节的任务数据、执行人数据、预定义的回调函数
     *
     * @param userId 启动人用户Id
     * @param userDisplayName 启动人姓名
     * @param wfDefCode 启动的流程定义编码
     * @param formData 流程定义中的表单数据
     * @return 流程启动执行数据（下一审批环节的任务数据、执行人数据、预定义的回调函数）
     */
    @Override
    public List<WorkflowStartExecuteData> GetWorkflowStartExecuteData(String userId, String userDisplayName, String wfDefCode, List<WorkflowProFieldDTO> formData){
        String jsonData = SerializeHelper.ToJson(formData);
        ServiceResult<List<WorkflowStartExecuteData>> result = null;
        result = WebSendPost(
                new TypeReference<ServiceResult<List<WorkflowStartExecuteData>>>() {},
                ServiceName + ".GetWorkflowStartExecuteData",
                WorkflowApiServerUrl() + "WorkflowApi/GetWorkflowStartExecuteData?userId=" + userId + "&userDisplayName=" + userDisplayName + "&wfDefCode=" + wfDefCode,
                ApplicationConstant.WorkflowScope,
                jsonData,
                callback ->
                {
                    return callback;
                },
                failCallback ->
                {
                    log.error(ServiceName + " throw error: " + failCallback.toString());
                },
                true);

        if (result != null && result.isSuccess() && result.getResult() != null)
        {
            return result.getResult();
        }

        return null;
    }

    /**
     * 启动一个流程实例
     *
     * @param userId 启动人用户Id
     * @param userDisplayName 启动人姓名
     * @param formData 流程定义中的表单数据
     * @return 流程实例的编码（可以根据此编码进行后续的查询及操作）
     */
    @Override
    public String StartWorkflow(String userId, String userDisplayName, WorkflowExecuteData formData){
        String jsonData = SerializeHelper.ToJson(formData);
        ServiceResult<String> result = null;
        result = WebSendPost(
                new TypeReference<ServiceResult<String>>() {},
                ServiceName + ".StartWorkflow",
                WorkflowApiServerUrl() + "WorkflowApi/StartWorkflow?userId=" + userId + "&userDisplayName=" + userDisplayName,
                ApplicationConstant.WorkflowScope,
                jsonData,
                callback ->
                {
                    return callback;
                },
                failCallback ->
                {
                    log.error(ServiceName + " throw error: " + failCallback.toString());
                },
                true);

        if (result != null && result.isSuccess() && result.getResult() != null)
        {
            return result.getResult();
        }

        return null;
    }

    /**
     * 审核流程前，根据流程实例编码，获取流程实例的可执行数据 </br>
     *      包括：提交的表单数据（获取后可使用接口：SubmitWorkflow，进行表单数据更新操作）
     *          启动后的下一审批环节的任务数据、执行人数据、预定义的回调函数
     *
     * @param userId 审核人用户Id
     * @param userDisplayName 审核人姓名
     * @param processCode 流程实例编码
     * @return 流程审核执行数据（下一审批环节的任务数据、执行人数据、预定义的回调函数）
     */
    @Override
    public WorkflowExecuteData GetWorkflowSubmitExecuteData(String userId, String userDisplayName, String processCode) {
        ServiceResult<WorkflowExecuteData> result = null;
        result = WebSendGet(
                new TypeReference<ServiceResult<WorkflowExecuteData>>() {
                },
                ServiceName + ".GetWorkflowSubmitExecuteData",
                WorkflowApiServerUrl() + "WorkflowApi/GetWorkflowSubmitExecuteData?userId=" + userId + "&userDisplayName=" + userDisplayName + "&processCode=" + processCode,
                ApplicationConstant.WorkflowScope,
                callback ->
                {
                    return callback;
                },
                failCallback ->
                {
                    log.error(ServiceName + " throw error: " + failCallback.toString());
                },
                true);

        if (result != null && result.isSuccess() && result.getResult() != null) {
            return result.getResult();
        }

        return null;
    }

    /**
     * 处理流程实例（流程过程）：Submit流程，进入下一个流程处理节点
     *
     * @param userId 启动人用户Id
     * @param userDisplayName 启动人姓名
     * @param processCode 流程实例编码（启动流程所返回的流程实例编码）
     * @param formData 审核流程的数据对象
     * @return 流程启动执行数据（下一审批环节的任务数据、执行人数据、预定义的回调函数）
     */
    @Override
    public Boolean SubmitWorkflow(String userId, String userDisplayName, String processCode, WorkflowExecuteData formData){
        String jsonData = SerializeHelper.ToJson(formData);
        ServiceResult<Boolean> result = null;
        result = WebSendPost(
                new TypeReference<ServiceResult<Boolean>>() {},
                ServiceName + ".SubmitWorkflow",
                WorkflowApiServerUrl() + "WorkflowApi/SubmitWorkflow?userId=" + userId + "&userDisplayName=" + userDisplayName + "&processCode=" + processCode,
                ApplicationConstant.WorkflowScope,
                jsonData,
                callback ->
                {
                    return callback;
                },
                failCallback ->
                {
                    log.error(ServiceName + " throw error: " + failCallback.toString());
                },
                true);

        if (result != null && result.isSuccess() && result.getResult() != null)
        {
            return result.getResult();
        }

        return null;
    }


    /**
     * 获取用户的可执行任务列表
     *
     * @param model 查询条件，包括：用户Id、用户所属角色Id列表、当前用户所属部门Id列表
     * @return 可执行任务列表
     */
    @Override
    public List<WorkflowProTaskDTO> LoadUserWorkflowTasks(WorkflowTaskSearchDTO model) {
        String jsonData = SerializeHelper.ToJson(model);
        ServiceResult<List<WorkflowProTaskDTO>> result = null;
        result = WebSendPost(
                new TypeReference<ServiceResult<List<WorkflowProTaskDTO>>>() {},
                ServiceName + ".GetWorkflowStartExecuteData",
                WorkflowApiServerUrl() + "WorkflowApi/LoadUserWorkflowTasks",
                ApplicationConstant.WorkflowScope,
                jsonData,
                callback ->
                {
                    return callback;
                },
                failCallback ->
                {
                    log.error(ServiceName + " throw error: " + failCallback.toString());
                },
                true);

        if (result != null && result.isSuccess() && result.getResult() != null) {
            return result.getResult();
        }

        return null;
    }

    /**
     * 根据流程实例编码，获取流程实例
     *
     * @param code 流程实例编码
     * @return 流程实例
     */
    @Override
    public WorkflowProcessDTO GetWorkflowProcessByCode(String code) {
        ServiceResult<WorkflowProcessDTO> result = null;
        result = WebSendGet(
                new TypeReference<ServiceResult<WorkflowProcessDTO>>() {
                },
                ServiceName + ".GetWorkflowProcessByCode",
                WorkflowApiServerUrl() + "WorkflowApi/GetWorkflowProcessByCode?code=" + code,
                ApplicationConstant.WorkflowScope,
                callback ->
                {
                    return callback;
                },
                failCallback ->
                {
                    log.error(ServiceName + " throw error: " + failCallback.toString());
                },
                true);

        if (result != null && result.isSuccess() && result.getResult() != null) {
            return result.getResult();
        }

        return null;
    }

    /**
     * 根据流程实例编码，获取流程实例的开始任务
     *
     * @param code 流程实例编码
     * @return 流程实例
     */
    @Override
    public WorkflowProcessDTO GetStartTaskByProcessCode(String code) {
        ServiceResult<WorkflowProcessDTO> result = null;
        result = WebSendGet(
                new TypeReference<ServiceResult<WorkflowProcessDTO>>() {
                },
                ServiceName + ".GetStartTaskByProcessCode",
                WorkflowApiServerUrl() + "WorkflowApi/GetStartTaskByProcessCode?code=" + code,
                ApplicationConstant.WorkflowScope,
                callback ->
                {
                    return callback;
                },
                failCallback ->
                {
                    log.error(ServiceName + " throw error: " + failCallback.toString());
                },
                true);

        if (result != null && result.isSuccess() && result.getResult() != null) {
            return result.getResult();
        }

        return null;
    }

    /**
     * 根据流程实例编码，获取流程实例的当前任务
     *
     * @param code 流程实例编码
     * @return 流程实例
     */
    @Override
    public WorkflowProcessDTO GetCurrentTaskByProcessCode(String code) {
        ServiceResult<WorkflowProcessDTO> result = null;
        result = WebSendGet(
                new TypeReference<ServiceResult<WorkflowProcessDTO>>() {
                },
                ServiceName + ".GetCurrentTaskByProcessCode",
                WorkflowApiServerUrl() + "WorkflowApi/GetCurrentTaskByProcessCode?code=" + code,
                ApplicationConstant.WorkflowScope,
                callback ->
                {
                    return callback;
                },
                failCallback ->
                {
                    log.error(ServiceName + " throw error: " + failCallback.toString());
                },
                true);

        if (result != null && result.isSuccess() && result.getResult() != null) {
            return result.getResult();
        }

        return null;
    }

    /**
     * 根据流程实例编码，获取流程实例的下一任务
     *
     * @param code 流程实例编码
     * @return 流程实例
     */
    @Override
    public WorkflowProcessDTO GetNextTaskByProcessCode(String code) {
        ServiceResult<WorkflowProcessDTO> result = null;
        result = WebSendGet(
                new TypeReference<ServiceResult<WorkflowProcessDTO>>() {
                },
                ServiceName + ".GetNextTaskByProcessCode",
                WorkflowApiServerUrl() + "WorkflowApi/GetNextTaskByProcessCode?code=" + code,
                ApplicationConstant.WorkflowScope,
                callback ->
                {
                    return callback;
                },
                failCallback ->
                {
                    log.error(ServiceName + " throw error: " + failCallback.toString());
                },
                true);

        if (result != null && result.isSuccess() && result.getResult() != null) {
            return result.getResult();
        }

        return null;
    }
}
