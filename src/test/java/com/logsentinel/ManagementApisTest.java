package com.logsentinel;

import com.logsentinel.client.ManageApplicationControllerApi;
import com.logsentinel.client.OrganizationUsersControllerApi;
import com.logsentinel.client.model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ManagementApisTest {

    //TODO extract in properties
    private static String managemenOrganizationId = "bb58d2d0-583d-11e8-a378-dbcaa44c8e82";
    private static String managementSecret = "34051092b07d037d01620bc9aab560c315ff39d2ee1d5dcd811431474bc572b6";
    private static String basePath = "http://localhost:8080";

    private static final String EMAIL = "aaaa@bbb.ccc";
    private String applicationId;

    OrganizationUsersControllerApi userActions;
    ManageApplicationControllerApi applicationActions;

    @Before
    public void init() {
        LogSentinelClientBuilder builder = LogSentinelClientBuilder
                .create(applicationId, managemenOrganizationId, managementSecret);
        builder.setBasePath(basePath);
        LogSentinelClient client = builder.build();
        applicationActions = client.getApplicationActions();
        if (applicationId != null) {
            userActions = client.getUserActions();
        }
    }

    //assumes there is working application on basePath. Commented not to break Travis
    //@Test
    public void testCalls() {
        try {
            //create application
            Application app = applicationActions.createApplicationUsingPUT(buildApplicationRequest(null, "app1"));
            assertEquals("app1", app.getName());

            //update application
            app = applicationActions.updateApplicationUsingPOST(buildApplicationRequest(app.getId(), "app2"));
            assertEquals("app2", app.getName());


            // init again with applicationId set, so Application-Id header is present for users api
            applicationId = app.getId();
            init();


            UserRegistrationRequest registrationRequest = buildCreateRequest();
            UserDetails userDetails = userActions.createUsingPUT(registrationRequest);
            assertEquals(EMAIL, userDetails.getEmail());

            // get user by email
            userDetails = userActions.getUserDetailsByEmailUsingGET(EMAIL);
            assertEquals(EMAIL, userDetails.getEmail());

            //get user by id
            userDetails = userActions.getUserDetailsByIdUsingGET(userDetails.getId());
            assertEquals(EMAIL, userDetails.getEmail());

            //update user
            userDetails = userActions.updateUsingPOST(buildUpdateRequest(userDetails));
            assertEquals("new" + EMAIL, userDetails.getEmail());

            //forget user
            userDetails = userActions.forgetUserUsingPOST(userDetails.getId());
            assertEquals("anonymized@logsentinel.com", userDetails.getEmail());

        } catch (ApiException e) {
            e.printStackTrace();
            fail();
        }
    }

    private UpdateApplicationRequest buildApplicationRequest(String id, String name) {

        UpdateApplicationRequest request = new UpdateApplicationRequest();
        request.setId(id);
        request.setName(name);

        return request;
    }

    private UserRegistrationRequest buildCreateRequest() {
        UserRegistrationRequest registrationRequest = new UserRegistrationRequest();

        registrationRequest.setEmail(EMAIL);
        registrationRequest.setPassword("password");
        registrationRequest.setNames("Name name");
        registrationRequest.setOrganizationName("organization");
        registrationRequest.setTimezone("GMT");
        registrationRequest.setSubscriptionPlanCode("FREE");
        registrationRequest.setPosition("boss");
        registrationRequest.setApplicationId(applicationId);
        registrationRequest.setRole(UserRegistrationRequest.RoleEnum.ADMIN);

        return registrationRequest;
    }

    private UpdateUserRequest buildUpdateRequest(UserDetails userDetails) {
        UpdateUserRequest request = new UpdateUserRequest();

        request.setUserId(userDetails.getId());
        request.setEmail("new" + userDetails.getEmail());
        request.setNames(userDetails.getNames());
        request.setProfilePicturePath(null);
        request.setPosition(userDetails.getPosition());
        request.setLanguage(UpdateUserRequest.LanguageEnum.EN);

        return request;
    }
}
