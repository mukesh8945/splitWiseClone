package com.mukesh.Splitwise.service;

import com.anupam.Splitwise.common.SplitwiseConstants;
import com.anupam.Splitwise.entity.group.GroupEntity;
import com.anupam.Splitwise.entity.member.UserEntity;
import com.anupam.Splitwise.exception.group.GroupNotFoundException;
import com.anupam.Splitwise.exception.group.InvalidGroupException;
import com.anupam.Splitwise.model.Response;
import com.anupam.Splitwise.model.group.Group;
import com.mukesh.Splitwise.converter.group.GroupConverter;
import com.mukesh.Splitwise.handler.group.GroupHandler;
import com.mukesh.Splitwise.service.audit.AuditService;
import com.mukesh.Splitwise.service.group.GroupService;
import com.mukesh.Splitwise.util.TestUtil;
import com.mukesh.Splitwise.validator.group.GroupValidator;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class GroupServiceTest {

    @InjectMocks
    private GroupService groupService;
    @Mock
    private GroupHandler handler;
    @Mock
    private AuditService auditService;
    @Mock
    private GroupValidator groupValidator;
    @Mock
    private GroupConverter groupConverter;
    private static GroupEntity groupEntity;
    private static Group group;
    private static UserEntity userEntity;

    private static final String GROUP_NAME = "TestGroup";
    private static final String USER_EMAIL = "TestAdminEmail";
    private static final UUID GRP_ID=UUID.randomUUID();

    @Test
    public void getGroupTest_01() throws Exception {
        groupEntity = TestUtil.getGroupEntity(GRP_ID);
        //creating stub response when handler method will be called
        Mockito.when(handler.findGroupByName(GROUP_NAME)).thenReturn(groupEntity);
        //actual method call
        Response response = groupService.getGroup(GROUP_NAME);
        //verifying that method was called exactly once--behaviour testing
        verify(handler, times(1)).findGroupByName(GROUP_NAME);
        //asserting response is not null
        assertNotNull(response);
        //asserting the status value
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        //asserting the actual response body
        assertEquals(groupEntity, response.getBody());
    }

    @Test
    public void getGroupTest_02() throws Exception {
        //Stub response
        Mockito.when(handler.findGroupByName(GROUP_NAME)).thenThrow(new GroupNotFoundException("group not found"));
        Exception exception = assertThrows(GroupNotFoundException.class, () -> {
            groupService.getGroup(GROUP_NAME);
        });
        verify(handler, times(1)).findGroupByName(GROUP_NAME);
        assertEquals("group not found", exception.getMessage());
    }

    @Test
    public void deleteGroupTest_01() throws Exception {
        groupEntity = TestUtil.getGroupEntity(GRP_ID);
        Mockito.when(handler.findGroupByName(GROUP_NAME)).thenReturn(groupEntity);
        Response response = groupService.deleteGroup(GROUP_NAME);
        Mockito.verify(handler, times(1)).findGroupByName(GROUP_NAME);
        Mockito.verify(handler, times(1)).deleteGroup(groupEntity);
        Mockito.verify(auditService, times(1)).
                submitAudit(groupEntity, SplitwiseConstants.AuditAction.DELETE_GROUP, "");
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());

    }

    @Test
    public void deleteGroupTest_02() throws Exception {
        Mockito.when(handler.findGroupByName(GROUP_NAME)).thenThrow(new GroupNotFoundException("group not found"));
        Exception exception = assertThrows(GroupNotFoundException.class, () -> {
            groupService.deleteGroup(GROUP_NAME);
        });
        Mockito.verify(handler, times(1)).findGroupByName(GROUP_NAME);
        assertEquals("group not found", exception.getMessage());
    }

    @Test
    public void createGroupTest_01() throws Exception {
        groupEntity = TestUtil.getGroupEntity(GRP_ID);
        group= TestUtil.getGroup();
        userEntity = TestUtil.getUSerEntity();

        Mockito.when(groupValidator.validateGroupDetails(group)).thenReturn(userEntity);
        Mockito.when(groupConverter.convert(group, userEntity)).thenReturn(groupEntity);
        Mockito.when(handler.createGroup(groupEntity)).thenReturn(groupEntity);
        Response response = groupService.createGroup(group);
        Mockito.verify(groupValidator, times(1)).validateGroupDetails(group);
        Mockito.verify(groupConverter, times(1)).convert(group, userEntity);
        Mockito.verify(auditService, times(1)).
                submitAudit(groupEntity, SplitwiseConstants.AuditAction.ADD_GROUP, "");
        assertEquals(HttpStatus.ACCEPTED.value(), response.getStatus());
        assertEquals(USER_EMAIL, userEntity.getEmail());
        assertEquals(groupEntity, response.getBody());

    }

    @Test
    public void createGroupTest_02() throws InvalidGroupException {
        group= TestUtil.getGroup();
        Mockito.when(groupValidator.validateGroupDetails(group)).
                thenThrow(new InvalidGroupException("Invalid group details"));
        Exception exception = assertThrows(InvalidGroupException.class, () -> {
            groupService.createGroup(group);
        });
        Mockito.verify(groupValidator, times(1)).validateGroupDetails(group);
        assertEquals("Invalid group details", exception.getMessage());
    }
}
