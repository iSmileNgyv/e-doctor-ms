package com.example.auth.service;

import java.util.List;

public interface RoleAccessService {
    boolean hasAccess(String operationCode, List<String> roleCodes);
    void deleteRoleAccess(String roleCode, String operationCode);
    void addRoleAccess(String roleCode, String operationCode);
    List<String> getOperationCodesByRoleCode(String roleCode);
}
