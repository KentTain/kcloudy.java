package kc.service.webapiservice;

import kc.dto.config.ConfigEntityDTO;
import kc.dto.search.WorkflowTaskSearchDTO;
import kc.dto.workflow.*;
import kc.framework.base.SeedEntity;
import kc.framework.enums.ConfigType;

import java.util.List;

public interface IWorkflowApiService {
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
    List<WorkflowStartExecuteData> GetWorkflowStartExecuteData(String userId, String userDisplayName, String wfDefCode, List<WorkflowProFieldDTO> formData);

    /**
     * 启动一个流程实例
     *
     * @param userId 启动人用户Id
     * @param userDisplayName 启动人姓名
     * @param formData 流程定义中的表单数据
     * @return 流程实例的编码（可以根据此编码进行后续的查询及操作）
     */
    String StartWorkflow(String userId, String userDisplayName, WorkflowExecuteData formData);

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
    WorkflowExecuteData GetWorkflowSubmitExecuteData(String userId, String userDisplayName, String processCode);

    /**
     * 处理流程实例（流程过程）：Submit流程，进入下一个流程处理节点
     *
     * @param userId 启动人用户Id
     * @param userDisplayName 启动人姓名
     * @param processCode 流程实例编码（启动流程所返回的流程实例编码）
     * @param formData 审核流程的数据对象
     * @return 流程启动执行数据（下一审批环节的任务数据、执行人数据、预定义的回调函数）
     */
    Boolean SubmitWorkflow(String userId, String userDisplayName, String processCode, WorkflowExecuteData formData);

    /**
     * 获取用户的可执行任务列表
     *
     * @param model 查询条件，包括：用户Id、用户所属角色Id列表、当前用户所属部门Id列表
     * @return
     */
    List<WorkflowProTaskDTO> LoadUserWorkflowTasks(WorkflowTaskSearchDTO model);

    /**
     * 根据流程实例编码，获取流程实例
     *
     * @param code 流程实例编码
     * @return 流程实例
     */
    WorkflowProcessDTO GetWorkflowProcessByCode(String code);

    /**
     * 根据流程实例编码，获取流程实例的开始任务
     *
     * @param code 流程实例编码
     * @return 流程实例
     */
    WorkflowProcessDTO GetStartTaskByProcessCode(String code);

    /**
     * 根据流程实例编码，获取流程实例的当前任务
     *
     * @param code 流程实例编码
     * @return 流程实例
     */
    WorkflowProcessDTO GetCurrentTaskByProcessCode(String code);

    /**
     * 根据流程实例编码，获取流程实例的下一任务
     *
     * @param code 流程实例编码
     * @return 流程实例
     */
    WorkflowProcessDTO GetNextTaskByProcessCode(String code);
}
