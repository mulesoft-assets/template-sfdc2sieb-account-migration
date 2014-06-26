/**
 * Mule Anypoint Template
 * Copyright (c) MuleSoft, Inc.
 * All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates.integration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.MessageExchangePattern;
import org.mule.api.MuleException;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.processor.chain.InterceptingChainLifecycleWrapper;
import org.mule.processor.chain.SubflowInterceptingChainLifecycleWrapper;
import org.mule.templates.AbstractTemplatesTestCase;

import com.mulesoft.module.batch.BatchTestHelper;
import com.sforce.soap.partner.SaveResult;

/**
 * The objective of this class is to validate the correct behavior of the Mule Template that make calls to external systems.
 * 
 * The test will invoke the batch process and afterwards check that the accounts had been correctly created and that the ones that should be filtered are not in
 * the destination sand box.
 * 
 * The test validates that no account will get sync as result of the integration.
 * 
 */
@SuppressWarnings("unchecked")
public class BusinessLogicIT extends AbstractTemplatesTestCase {
	protected static final int TIMEOUT_SEC = 120;
	protected static final String ANYPOINT_TEMPLATE_NAME = "account-migration";
	private BatchTestHelper helper;

	private SubflowInterceptingChainLifecycleWrapper createAccountInSalesforceFlow;
	private InterceptingChainLifecycleWrapper queryAccountFromSiebelFlow;
	private static List<String> accountsCreatedInSalesforce = new ArrayList<String>();
	private static List<String> accountsCreatedInSiebel = new ArrayList<String>();
	private static List<Map<String, Object>> createdAccounts = new ArrayList<Map<String,Object>>();
	
	private static SubflowInterceptingChainLifecycleWrapper deleteAccountFromSalesforceFlow;
	private static SubflowInterceptingChainLifecycleWrapper deleteAccountFromSiebelFlow;
	
	@Before
	public void setUp() throws Exception {
		helper = new BatchTestHelper(muleContext);
		getAndInitializeFlows();
		createTestDataInSandBox();
	}

	
	private void getAndInitializeFlows() throws InitialisationException {
		// Flow for creating accounts in Salesforce
		createAccountInSalesforceFlow = getSubFlow("createAccountsInSalesforceFlow");
		createAccountInSalesforceFlow.initialise();

		// Flow for deleting accounts in Salesforce
		deleteAccountFromSalesforceFlow = getSubFlow("deleteAccountsFromSalesforceFlow");
		deleteAccountFromSalesforceFlow.initialise();

		// Flow for deleting accounts in Siebel
		deleteAccountFromSiebelFlow = getSubFlow("deleteAccountsFromSiebelFlow");
		deleteAccountFromSiebelFlow.initialise();

		// Flow for querying the account in Siebel
		queryAccountFromSiebelFlow = getSubFlow("queryAccountFromSiebelFlow");
		queryAccountFromSiebelFlow.initialise();
	}
	
	
	@After
	public void tearDown() throws Exception {
		cleanUpSandboxesByRemovingTestAccounts();
	}

	@Test
	public void testMainFlow() throws Exception {
		runFlow("mainFlow");
		
		// Wait for the batch job executed by the poll flow to finish
		helper.awaitJobTermination(TIMEOUT_SEC * 1000, 500);
		helper.assertJobWasSuccessful();
		
		List<Map<String, Object>> response = (List<Map<String, Object>>) queryAccountFromSiebelFlow.process(getTestEvent(createdAccounts.get(0), MessageExchangePattern.REQUEST_RESPONSE)).getMessage().getPayload();
		Assert.assertEquals("There should be only one account with this name", 1, response.size());
		Assert.assertEquals("Description should match", "Old description", response.get(0).get("Description"));
	}


	private void createTestDataInSandBox() throws MuleException, Exception {
		HashMap<String, Object> account = new HashMap<String, Object>();
		account.put("Name", ANYPOINT_TEMPLATE_NAME + "-" + System.currentTimeMillis() + "Account");
		account.put("Phone", "123456789");
		account.put("NumberOfEmployees", 6000);
		account.put("Industry", "Education");

		Map<String, Object> justCreatedAccount = (Map<String, Object>) account.clone();
		justCreatedAccount.put("Description", "Old description");
		
		Map<String, Object> updatedAccount = (Map<String, Object>) account.clone();
		updatedAccount.put("Description", "Some nice description");
		
		createdAccounts.add(justCreatedAccount);
		createdAccounts.add(updatedAccount);

		// Create accounts in sand-boxes and keep track of them for posterior
		// cleaning up
		accountsCreatedInSalesforce.add(createTestAccountsInSfdcSandbox(justCreatedAccount, createAccountInSalesforceFlow));			
	}
	
	private String createTestAccountsInSfdcSandbox(Map<String, Object> account, InterceptingChainLifecycleWrapper createAccountFlow) throws Exception {
		List<Map<String, Object>> salesforceAccounts = new ArrayList<Map<String, Object>>();
		salesforceAccounts.add(account);
		final List<SaveResult> payloadAfterExecution = (List<SaveResult>) createAccountFlow.process(getTestEvent(salesforceAccounts, MessageExchangePattern.REQUEST_RESPONSE)).getMessage().getPayload();
		return payloadAfterExecution.get(0).getId();
	}
	
	private static void cleanUpSandboxesByRemovingTestAccounts() throws MuleException, Exception {
		final List<String> idList = new ArrayList<String>();
		
		for (String account : accountsCreatedInSalesforce) {
			idList.add(account);
		}
		deleteAccountFromSalesforceFlow.process(getTestEvent(idList, MessageExchangePattern.REQUEST_RESPONSE));
		idList.clear();
		
		for (String account : accountsCreatedInSiebel) {
			idList.add(account);
		}
		deleteAccountFromSiebelFlow.process(getTestEvent(idList, MessageExchangePattern.REQUEST_RESPONSE));
	}


}
