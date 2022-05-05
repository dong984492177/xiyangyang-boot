package com.ywt.console.config;


import java.util.List;
import java.util.Map;
import org.activiti.api.model.shared.model.VariableInstance;
import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.model.ProcessDefinitionMeta;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.ProcessInstanceMeta;
import org.activiti.api.process.model.ProcessInstance.ProcessInstanceStatus;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.model.payloads.DeleteProcessPayload;
import org.activiti.api.process.model.payloads.GetProcessDefinitionsPayload;
import org.activiti.api.process.model.payloads.GetProcessInstancesPayload;
import org.activiti.api.process.model.payloads.GetVariablesPayload;
import org.activiti.api.process.model.payloads.RemoveProcessVariablesPayload;
import org.activiti.api.process.model.payloads.ResumeProcessPayload;
import org.activiti.api.process.model.payloads.SetProcessVariablesPayload;
import org.activiti.api.process.model.payloads.SignalPayload;
import org.activiti.api.process.model.payloads.StartProcessPayload;
import org.activiti.api.process.model.payloads.SuspendProcessPayload;
import org.activiti.api.process.model.payloads.UpdateProcessPayload;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.process.runtime.conf.ProcessRuntimeConfiguration;
import org.activiti.api.runtime.model.impl.ProcessDefinitionMetaImpl;
import org.activiti.api.runtime.model.impl.ProcessInstanceImpl;
import org.activiti.api.runtime.model.impl.ProcessInstanceMetaImpl;
import org.activiti.api.runtime.shared.NotFoundException;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.core.common.spring.security.policies.ActivitiForbiddenException;
import org.activiti.core.common.spring.security.policies.ProcessSecurityPoliciesManager;
import org.activiti.core.common.spring.security.policies.SecurityPolicyAccess;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.runtime.api.model.impl.APIProcessDefinitionConverter;
import org.activiti.runtime.api.model.impl.APIProcessInstanceConverter;
import org.activiti.runtime.api.model.impl.APIVariableInstanceConverter;
import org.activiti.runtime.api.query.impl.PageImpl;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Primary
@Component
public class ProcessRuntimeImpl implements ProcessRuntime {
    private final RepositoryService repositoryService;
    private final APIProcessDefinitionConverter processDefinitionConverter;
    private final RuntimeService runtimeService;
    private final APIProcessInstanceConverter processInstanceConverter;
    private final APIVariableInstanceConverter variableInstanceConverter;
    private final ProcessRuntimeConfiguration configuration;
    private final ProcessSecurityPoliciesManager securityPoliciesManager;
    private final ApplicationEventPublisher eventPublisher;

    public ProcessRuntimeImpl(RepositoryService repositoryService, APIProcessDefinitionConverter processDefinitionConverter, RuntimeService runtimeService, ProcessSecurityPoliciesManager securityPoliciesManager, APIProcessInstanceConverter processInstanceConverter, APIVariableInstanceConverter variableInstanceConverter, ProcessRuntimeConfiguration configuration, ApplicationEventPublisher eventPublisher) {
        this.repositoryService = repositoryService;
        this.processDefinitionConverter = processDefinitionConverter;
        this.runtimeService = runtimeService;
        this.securityPoliciesManager = securityPoliciesManager;
        this.processInstanceConverter = processInstanceConverter;
        this.variableInstanceConverter = variableInstanceConverter;
        this.configuration = configuration;
        this.eventPublisher = eventPublisher;
    }

    public ProcessDefinition processDefinition(String processDefinitionId) {
        List<org.activiti.engine.repository.ProcessDefinition> list = ((ProcessDefinitionQuery)this.repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionId).orderByProcessDefinitionVersion().asc()).list();
        org.activiti.engine.repository.ProcessDefinition processDefinition;
        if (!list.isEmpty()) {
            processDefinition = (org.activiti.engine.repository.ProcessDefinition)list.get(0);
        } else {
            processDefinition = this.repositoryService.getProcessDefinition(processDefinitionId);
        }

        if (!this.securityPoliciesManager.canRead(processDefinition.getKey())) {
            throw new ActivitiObjectNotFoundException("Unable to find process definition for the given id:'" + processDefinitionId + "'");
        } else {
            return this.processDefinitionConverter.from(processDefinition);
        }
    }

    public Page<ProcessDefinition> processDefinitions(Pageable pageable) {
        return this.processDefinitions(pageable, ProcessPayloadBuilder.processDefinitions().build());
    }

    public Page<ProcessDefinition> processDefinitions(Pageable pageable, GetProcessDefinitionsPayload getProcessDefinitionsPayload) {
        if (getProcessDefinitionsPayload == null) {
            throw new IllegalStateException("payload cannot be null");
        } else {
            GetProcessDefinitionsPayload securityKeysInPayload = this.securityPoliciesManager.restrictProcessDefQuery(SecurityPolicyAccess.READ);
            if (!securityKeysInPayload.getProcessDefinitionKeys().isEmpty()) {
                getProcessDefinitionsPayload.setProcessDefinitionKeys(securityKeysInPayload.getProcessDefinitionKeys());
            }

            ProcessDefinitionQuery processDefinitionQuery = this.repositoryService.createProcessDefinitionQuery();
            if (getProcessDefinitionsPayload.hasDefinitionKeys()) {
                processDefinitionQuery.processDefinitionKeys(getProcessDefinitionsPayload.getProcessDefinitionKeys());
            }

            return new PageImpl(this.processDefinitionConverter.from(processDefinitionQuery.list()), Math.toIntExact(processDefinitionQuery.count()));
        }
    }

    public ProcessInstance processInstance(String processInstanceId) {
        org.activiti.engine.runtime.ProcessInstance internalProcessInstance = (org.activiti.engine.runtime.ProcessInstance)this.runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (internalProcessInstance == null) {
            throw new NotFoundException("Unable to find process instance for the given id:'" + processInstanceId + "'");
        } else if (!this.securityPoliciesManager.canRead(internalProcessInstance.getProcessDefinitionKey())) {
            throw new ActivitiObjectNotFoundException("You cannot read the process instance with Id:'" + processInstanceId + "' due to security policies violation");
        } else {
            return this.processInstanceConverter.from(internalProcessInstance);
        }
    }

    public Page<ProcessInstance> processInstances(Pageable pageable) {
        return this.processInstances(pageable, ProcessPayloadBuilder.processInstances().build());
    }

    public Page<ProcessInstance> processInstances(Pageable pageable, GetProcessInstancesPayload getProcessInstancesPayload) {
        if (getProcessInstancesPayload == null) {
            throw new IllegalStateException("payload cannot be null");
        } else {
            GetProcessInstancesPayload securityKeysInPayload = this.securityPoliciesManager.restrictProcessInstQuery(SecurityPolicyAccess.READ);
            ProcessInstanceQuery internalQuery = this.runtimeService.createProcessInstanceQuery();
            if (!securityKeysInPayload.getProcessDefinitionKeys().isEmpty()) {
                getProcessInstancesPayload.setProcessDefinitionKeys(securityKeysInPayload.getProcessDefinitionKeys());
            }

            if (getProcessInstancesPayload.getProcessDefinitionKeys() != null && !getProcessInstancesPayload.getProcessDefinitionKeys().isEmpty()) {
                internalQuery.processDefinitionKeys(getProcessInstancesPayload.getProcessDefinitionKeys());
            }

            if (getProcessInstancesPayload.getBusinessKey() != null && !getProcessInstancesPayload.getBusinessKey().isEmpty()) {
                internalQuery.processInstanceBusinessKey(getProcessInstancesPayload.getBusinessKey());
            }

            if (getProcessInstancesPayload.isSuspendedOnly()) {
                internalQuery.suspended();
            }

            if (getProcessInstancesPayload.isActiveOnly()) {
                internalQuery.active();
            }

            if (getProcessInstancesPayload.getParentProcessInstanceId() != null) {
                internalQuery.superProcessInstanceId(getProcessInstancesPayload.getParentProcessInstanceId());
            }

            return new PageImpl(this.processInstanceConverter.from(internalQuery.listPage(pageable.getStartIndex(), pageable.getMaxItems())), Math.toIntExact(internalQuery.count()));
        }
    }

    public ProcessRuntimeConfiguration configuration() {
        return this.configuration;
    }

    public ProcessInstance start(StartProcessPayload startProcessPayload) {
        ProcessDefinition processDefinition = null;
        if (startProcessPayload.getProcessDefinitionId() != null) {
            processDefinition = this.processDefinition(startProcessPayload.getProcessDefinitionId());
        }

        if (processDefinition == null && startProcessPayload.getProcessDefinitionKey() != null) {
            processDefinition = this.processDefinition(startProcessPayload.getProcessDefinitionKey());
        }

        if (processDefinition == null) {
            throw new IllegalStateException("At least Process Definition Id or Key needs to be provided to start a process");
        } else if (!this.securityPoliciesManager.canWrite(processDefinition.getKey())) {
            throw new ActivitiForbiddenException("Operation not permitted for " + processDefinition.getKey() + " due security policy violation");
        } else {
            return this.processInstanceConverter.from(this.runtimeService.createProcessInstanceBuilder().processDefinitionId(startProcessPayload.getProcessDefinitionId()).processDefinitionKey(startProcessPayload.getProcessDefinitionKey()).businessKey(startProcessPayload.getBusinessKey()).variables(startProcessPayload.getVariables()).name(startProcessPayload.getName()).start());
        }
    }

    public ProcessInstance suspend(SuspendProcessPayload suspendProcessPayload) {
        ProcessInstance processInstance = this.processInstance(suspendProcessPayload.getProcessInstanceId());
        if (!this.securityPoliciesManager.canWrite(processInstance.getProcessDefinitionKey())) {
            throw new ActivitiForbiddenException("Operation not permitted for " + processInstance.getProcessDefinitionKey() + " due security policy violation");
        } else {
            this.runtimeService.suspendProcessInstanceById(suspendProcessPayload.getProcessInstanceId());
            return this.processInstanceConverter.from((org.activiti.engine.runtime.ProcessInstance)this.runtimeService.createProcessInstanceQuery().processInstanceId(suspendProcessPayload.getProcessInstanceId()).singleResult());
        }
    }

    public ProcessInstance resume(ResumeProcessPayload resumeProcessPayload) {
        ProcessInstance processInstance = this.processInstance(resumeProcessPayload.getProcessInstanceId());
        if (!this.securityPoliciesManager.canWrite(processInstance.getProcessDefinitionKey())) {
            throw new ActivitiForbiddenException("Operation not permitted for " + processInstance.getProcessDefinitionKey() + " due security policy violation");
        } else {
            this.runtimeService.activateProcessInstanceById(resumeProcessPayload.getProcessInstanceId());
            return this.processInstanceConverter.from((org.activiti.engine.runtime.ProcessInstance)this.runtimeService.createProcessInstanceQuery().processInstanceId(resumeProcessPayload.getProcessInstanceId()).singleResult());
        }
    }

    public ProcessInstance delete(DeleteProcessPayload deleteProcessPayload) {
        ProcessInstanceImpl processInstance = (ProcessInstanceImpl)this.processInstance(deleteProcessPayload.getProcessInstanceId());
        if (!this.securityPoliciesManager.canWrite(processInstance.getProcessDefinitionKey())) {
            throw new ActivitiForbiddenException("Operation not permitted for " + processInstance.getProcessDefinitionKey() + " due security policy violation");
        } else {
            this.runtimeService.deleteProcessInstance(deleteProcessPayload.getProcessInstanceId(), deleteProcessPayload.getReason());
            processInstance.setStatus(ProcessInstanceStatus.DELETED);
            return processInstance;
        }
    }

    public List<VariableInstance> variables(GetVariablesPayload getVariablesPayload) {
        this.processInstance(getVariablesPayload.getProcessInstanceId());
        Map<String, org.activiti.engine.impl.persistence.entity.VariableInstance> variables = this.runtimeService.getVariableInstances(getVariablesPayload.getProcessInstanceId());
        return this.variableInstanceConverter.from(variables.values());
    }

    public void removeVariables(RemoveProcessVariablesPayload removeProcessVariablesPayload) {
        ProcessInstanceImpl processInstance = (ProcessInstanceImpl)this.processInstance(removeProcessVariablesPayload.getProcessInstanceId());
        if (!this.securityPoliciesManager.canWrite(processInstance.getProcessDefinitionKey())) {
            throw new ActivitiForbiddenException("Operation not permitted for " + processInstance.getProcessDefinitionKey() + " due security policy violation");
        } else {
            this.runtimeService.removeVariables(removeProcessVariablesPayload.getProcessInstanceId(), removeProcessVariablesPayload.getVariableNames());
        }
    }

    public void setVariables(SetProcessVariablesPayload setProcessVariablesPayload) {
        ProcessInstanceImpl processInstance = (ProcessInstanceImpl)this.processInstance(setProcessVariablesPayload.getProcessInstanceId());
        if (!this.securityPoliciesManager.canWrite(processInstance.getProcessDefinitionKey())) {
            throw new ActivitiForbiddenException("Operation not permitted for " + processInstance.getProcessDefinitionKey() + " due security policy violation");
        } else {
            this.runtimeService.setVariables(setProcessVariablesPayload.getProcessInstanceId(), setProcessVariablesPayload.getVariables());
        }
    }

    @Transactional
    public void signal(SignalPayload signalPayload) {
        this.eventPublisher.publishEvent(signalPayload);
    }

    public ProcessDefinitionMeta processDefinitionMeta(String processDefinitionKey) {
        this.processDefinition(processDefinitionKey);
        return new ProcessDefinitionMetaImpl(processDefinitionKey);
    }

    public ProcessInstanceMeta processInstanceMeta(String processInstanceId) {
        this.processInstance(processInstanceId);
        ProcessInstanceMetaImpl processInstanceMeta = new ProcessInstanceMetaImpl(processInstanceId);
        processInstanceMeta.setActiveActivitiesIds(this.runtimeService.getActiveActivityIds(processInstanceId));
        return processInstanceMeta;
    }

    public ProcessInstance update(UpdateProcessPayload updateProcessPayload) {
        ProcessInstance processInstance = this.processInstance(updateProcessPayload.getProcessInstanceId());
        if (!this.securityPoliciesManager.canWrite(processInstance.getProcessDefinitionKey())) {
            throw new ActivitiForbiddenException("Operation not permitted for " + processInstance.getProcessDefinitionKey() + " due security policy violation");
        } else {
            if (updateProcessPayload.getBusinessKey() != null) {
                this.runtimeService.updateBusinessKey(updateProcessPayload.getProcessInstanceId(), updateProcessPayload.getBusinessKey());
            }

            if (updateProcessPayload.getName() != null) {
                this.runtimeService.setProcessInstanceName(updateProcessPayload.getProcessInstanceId(), updateProcessPayload.getName());
            }

            ProcessInstance updatedProcessInstance = this.processInstanceConverter.from((org.activiti.engine.runtime.ProcessInstance)this.runtimeService.createProcessInstanceQuery().processInstanceId(updateProcessPayload.getProcessInstanceId()).singleResult());
            return updatedProcessInstance;
        }
    }
}
