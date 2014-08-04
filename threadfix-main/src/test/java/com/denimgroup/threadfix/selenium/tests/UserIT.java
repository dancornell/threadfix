////////////////////////////////////////////////////////////////////////
//
//     Copyright (c) 2009-2014 Denim Group, Ltd.
//
//     The contents of this file are subject to the Mozilla Public License
//     Version 2.0 (the "License"); you may not use this file except in
//     compliance with the License. You may obtain a copy of the License at
//     http://www.mozilla.org/MPL/
//
//     Software distributed under the License is distributed on an "AS IS"
//     basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
//     License for the specific language governing rights and limitations
//     under the License.
//
//     The Original Code is ThreadFix.
//
//     The Initial Developer of the Original Code is Denim Group, Ltd.
//     Portions created by Denim Group, Ltd. are Copyright (C)
//     Denim Group, Ltd. All Rights Reserved.
//
//     Contributor(s): Denim Group, Ltd.
//
////////////////////////////////////////////////////////////////////////
package com.denimgroup.threadfix.selenium.tests;

import com.denimgroup.threadfix.CommunityTests;
import com.denimgroup.threadfix.selenium.pages.DashboardPage;
import com.denimgroup.threadfix.selenium.pages.UserChangePasswordPage;
import com.denimgroup.threadfix.selenium.pages.UserIndexPage;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Category(CommunityTests.class)
public class UserIT extends BaseIT {

	@Test
	public void testCreateUser() {
		String userName = "testCreateUser" + getRandomString(3);
        String password = "testCreateUser";
		UserIndexPage userIndexPage = loginPage.login("user", "password")
                .clickManageUsersLink();

		userIndexPage.clickAddUserLink()
                    .enterName(userName)
                    .enterPassword(password)
                    .enterConfirmPassword(password)
                    .clickAddNewUserBtn();
        assertTrue("User name was not present in the table.", userIndexPage.isUserNamePresent(userName));
		assertTrue("Success message was not displayed.", userIndexPage.isSuccessDisplayed(userName));
	}

    @Test
    public void testUserFieldValidation() {
        UserIndexPage userIndexPage = loginPage.login("user", "password")
                .clickManageUsersLink()
                .clickAddUserLink();

        userIndexPage.enterName("        ");
        userIndexPage.enterPassword("  ");
        userIndexPage.enterConfirmPassword("  ");

        userIndexPage = userIndexPage.clickAddNewUserBtnInvalid();
        sleep(5000);

        assertTrue("Name is required error was not present.",
                userIndexPage.getRequiredNameError().equals("Name is required."));
        assertTrue("Password is required error was not present.",
                userIndexPage.getPasswordRequiredError().equals("Password is required."));
        assertTrue("Confirm Password is required error was not present.",
                userIndexPage.getConfirmPasswordRequiredError().equals("Confirm Password is required."));

        // Test length
        userIndexPage.enterName("Test User");
        userIndexPage.enterPassword("test");
        userIndexPage.enterConfirmPassword("test");

        userIndexPage = userIndexPage.clickAddNewUserBtnInvalid();

        assertTrue("Password length error not present", userIndexPage.getPasswordLengthError().equals("8 characters needed"));

        // Test non-matching passwords
        userIndexPage.enterName("new name");
        userIndexPage.enterPassword("lengthy password 1");
        userIndexPage.enterConfirmPassword("lengthy password 2");
        userIndexPage = userIndexPage.clickAddNewUserBtnInvalid();
        assertTrue("Password matching error is not correct.", userIndexPage.getPasswordMatchError().equals("Passwords do not match."));
    }

    @Test
    public void testCreateDuplicateUser(){
        String userName = "testDuplicateUser" + getRandomString(3);
        String password = getRandomString(15);
        // Create a user
        UserIndexPage userIndexPage = loginPage.login("user", "password")
                .clickManageUsersLink()
                .clickAddUserLink();
        userIndexPage.enterName(userName);
        userIndexPage.enterPassword(password);
        userIndexPage.enterConfirmPassword(password);

        userIndexPage = userIndexPage.clickAddNewUserBtn();

        assertTrue("User name was not present in the table.", userIndexPage.isUserNamePresent(userName));
        assertTrue("Success message was not displayed.", userIndexPage.isSuccessDisplayed(userName));

        DashboardPage dashboardPage = userIndexPage.logout()
                .login(userName, password);

        assertTrue("user: "+userName+" was not logged in.",dashboardPage.isLoggedInUser(userName));

        userIndexPage = dashboardPage.logout()
                .login("user", "password")
                .clickManageUsersLink()
                .clickAddUserLink();
        // Test name uniqueness check

        userIndexPage.enterName(userName);
        userIndexPage.enterPassword("dummy password");
        userIndexPage.enterConfirmPassword("dummy password");

        userIndexPage = userIndexPage.clickAddNewUserBtnInvalid();
        sleep(5000);
        assertTrue("Name uniqueness error is not correct.", userIndexPage.getNameError().equals("That name is already taken."));
    }

    @Test
    public void testEditUserName() {
        String userName = getRandomString(8);
        String editedUserName = getRandomString(8);
        String password = getRandomString(15);
        String editedPassword = getRandomString(15);

        UserIndexPage userIndexPage = loginPage.login("user", "password")
                .clickManageUsersLink()
                .clickAddUserLink()
                .enterName(userName)
                .enterPassword(password)
                .enterConfirmPassword(password)
                .clickAddNewUserBtn();

        DashboardPage dashboardPage= userIndexPage.logout()
                .login(userName, password);

        assertTrue("New user was not able to login.", dashboardPage.isLoggedin());

        userIndexPage = dashboardPage.logout()
                .login("user", "password")
                .clickManageUsersLink()
                .clickEditLink(userName)
                .enterName(editedUserName)
                .enterPassword(editedPassword)
                .enterConfirmPassword(editedPassword)
                .clickModalSubmit();

        dashboardPage = userIndexPage.logout()
                .login(editedUserName, editedPassword);

        assertTrue("Edited user was not able to login.", dashboardPage.isLoggedin());
    }

	@Test
	public void testEditPassword() {
		String userName = getRandomString(8);
        String password = getRandomString(15);
        String editedPassword = getRandomString(15);

		UserIndexPage userIndexPage = loginPage.login("user", "password")
                .clickManageUsersLink();

		assertFalse("User was already in the table.", userIndexPage.isUserNamePresent(userName));

        UserChangePasswordPage userChangePasswordPage = userIndexPage.clickAddUserLink()
				.enterName(userName)
				.enterPassword(password)
				.enterConfirmPassword(password)
				.clickAddNewUserBtn()
				.logout()
				.login(userName, password)
                .clickChangePasswordLink()
                .setCurrentPassword(password)
                .setNewPassword(editedPassword)
                .setConfirmPassword(editedPassword)
                .clickUpdate();


		DashboardPage dashboardPage = userChangePasswordPage.logout()
                .login(userName, editedPassword);

        assertTrue("Edited user could not login.", dashboardPage.isLoggedin());
	}

    @Test
    public void testEditPasswordValidation() {
        UserChangePasswordPage changePasswordPage = loginPage.login("user", "password")
                .clickChangePasswordLink()
                .setCurrentPassword(" ")
                .setNewPassword("password1234")
                .setConfirmPassword("password1234")
                .clickUpdateInvalid();

        assertTrue("Password is required error was not present.",
                changePasswordPage.getPasswordRequiredError().equals("Password is required."));

        changePasswordPage = changePasswordPage.setCurrentPassword("password")
                .setNewPassword("                     ")
                .setConfirmPassword("password1234")
                .clickUpdateInvalid();

        assertTrue("Password match error not present",
                changePasswordPage.getErrorText("passwordMatchError").contains("Passwords do not match."));

        changePasswordPage = changePasswordPage.setCurrentPassword("password")
                .setConfirmPassword("                  ")
                .setNewPassword("password1234")
                .clickUpdateInvalid();

        assertTrue("Password match error not present",
                changePasswordPage.getErrorText("passwordMatchError").contains("Passwords do not match."));

        changePasswordPage = changePasswordPage.setCurrentPassword("password")
                .setConfirmPassword("      ")
                .setNewPassword("password124")
                .clickUpdateInvalid();

        assertTrue("Field required error missing",
                changePasswordPage.getErrorText("confirmRequiredError").contains("This field is required."));

        changePasswordPage.logout();
    }

	@Test
	public void testEditUserFieldValidation() {
		String baseUserName = "testEditUser" + getRandomString(3);
		String userNameDuplicateTest = "duplicate-user" + getRandomString(3);

		// Set up the two User objects for the test

		UserIndexPage userIndexPage = loginPage.login("user", "password")
                .clickManageUsersLink()
                .clickAddUserLink()
                .enterName(baseUserName)
                .enterPassword("lengthy password 2")
                .enterConfirmPassword("lengthy password 2")
                .clickAddNewUserBtn();

        userIndexPage = userIndexPage.clickOrganizationHeaderLink()
                .clickManageUsersLink()
                .clickAddUserLink()
                .enterName(userNameDuplicateTest)
                .enterPassword("lengthy password 2")
                .enterConfirmPassword("lengthy password 2")
                .clickAddNewUserBtn();

		// Test submission with no changes
		userIndexPage = userIndexPage.clickManageUsersLink()
                .clickEditLink(baseUserName)
                .clickUpdateUserBtn(baseUserName);
		assertTrue("User name was not present in the table.",userIndexPage.isUserNamePresent(baseUserName));

        userIndexPage = userIndexPage.clickManageUsersLink();

		// Test Empty
		userIndexPage = userIndexPage.clickEditLink(baseUserName)
                .enterName("")
                .enterPassword("")
                .enterConfirmPassword("")
                .clickUpdateUserBtnInvalid(baseUserName);

		assertTrue("Name error not present", userIndexPage.isSaveChangesButtonClickable(baseUserName));

        userIndexPage.clickManageUsersLink();
    }

    @Test
    public void testEditUserValidationWhiteSpace (){
        String userName = "userName"+ getRandomString(5);
        String passWord = "passWord"+ getRandomString(5);

        UserIndexPage userIndexPage = loginPage.login("user", "password")
                .clickManageUsersLink()
                .clickAddUserLink()
                .enterName(userName)
                .enterPassword(passWord)
                .enterConfirmPassword(passWord)
                .clickAddNewUserBtn();

		// Test White Space
		userIndexPage = userIndexPage.clickManageUsersLink()
                .clickAddUserLink()
                .enterName("        ")
                .enterPassword("             ")
                .enterConfirmPassword("             ")
                .clickAddNewUserBtn();

        sleep(5000);
		assertTrue("Name error not present", userIndexPage.getRequiredNameError().equals("Name is required."));
    }

    @Test
    public void testEditUserValidationPasswordMatching(){
        String userName = "userName"+ getRandomString(5);

        UserIndexPage userIndexPage = loginPage.login("user", "password").clickManageUsersLink();
		// Test non-matching passwords
		userIndexPage = userIndexPage.clickAddUserLink()
                .enterName(userName)
                .enterPassword("lengthy password 1")
                .enterConfirmPassword("lengthy password 2")
                .clickAddNewUserBtn();

        sleep(5000);
		assertTrue("Password matching error is not correct.", userIndexPage.getPasswordMatchError().equals("Passwords do not match."));

    }

    @Test
    public void testEditUserValidationLength(){
        UserIndexPage userIndexPage = loginPage.login("user", "password")
                .clickManageUsersLink();

		// Test length
		userIndexPage = userIndexPage.clickAddUserLink()
                .enterName("Test User")
                .enterPassword("test")
                .enterConfirmPassword("test")
                .clickAddNewUserBtn();

        sleep(5000);
		assertTrue("Password length error not present", userIndexPage.getPasswordLengthError().equals("8 characters needed"));

    }

    @Test
    public void testEditUserValidationUnique(){
        String userName = "userName"+ getRandomString(5);
        String passWord = "passWord"+ getRandomString(5);

        UserIndexPage userIndexPage = loginPage.login("user", "password")
                .clickManageUsersLink()
                .clickAddUserLink()
                .enterName(userName)
                .enterPassword(passWord)
                .enterConfirmPassword(passWord)
                .clickAddNewUserBtn();
	
		// Test name uniqueness check
		userIndexPage = userIndexPage
				.clickManageUsersLink()
				.clickAddUserLink()
				.enterName(userName)
				.enterPassword("lengthy password 2")
				.enterConfirmPassword("lengthy password 2")
				.clickAddNewUserBtn();

        sleep(5000);
		assertTrue("Name uniqueness error is not correct.", userIndexPage.getNameError().equals("That name is already taken."));
		
	}

	@Test
	public void testNavigation() {
		loginPage.login("user", "password")
                 .clickManageUsersLink();
        assertTrue("Could not navigate to User Index Page.",driver.findElements(By.id("newUserModalLink")).size() != 0);
		}

    @Test
    public void testDeleteUser(){
        String userName = "testDeleteUser" + getRandomString(3);
        String password = "testDeleteUser";
        UserIndexPage userIndexPage = loginPage.login("user", "password")
                .clickManageUsersLink();

        userIndexPage.clickAddUserLink()
                .enterName(userName)
                .enterPassword(password)
                .enterConfirmPassword(password)
                .clickAddNewUserBtn()
                .clickEditLink(userName)
                .clickDelete(userName);
        assertTrue("Deletion Message not displayed.", userIndexPage.isSuccessDisplayed(userName));
        assertFalse("User still present in user table.", userIndexPage.isUserNamePresent(userName));
    }
}
