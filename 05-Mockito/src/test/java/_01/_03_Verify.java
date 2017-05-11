/*
==============
Mockito Verify
==============
 To test the state, we use assert, likewise, to verify the test interactions, we use Mockito.verify.
 using assert, we check the state of classUnderTest or SystemUnderTest
 using verify, we check the behavior/interaction of the stub/collaborator

 */

package _01;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import _03_Verify.Account;
import _03_Verify.AccountManager;
import _03_Verify.Customer;
import _03_Verify.NotEnoughFundsException;

public class _03_Verify {
	private Customer classUnderTest;
	private AccountManager mockAccountManager;
	private Account mockAccount;

	@Before
	public void setup(){
		classUnderTest = new Customer();
		mockAccountManager = Mockito.mock(AccountManager.class);
		classUnderTest.setAccountManager(mockAccountManager);
		mockAccount = Mockito.mock(Account.class);
		Mockito.when(mockAccountManager.findAccount(classUnderTest)).thenReturn(mockAccount);
	}

	
	@Test
	public void withdraw_whenInsufficientFund_ThrowNotEnoughFundsException(){
		Mockito.when(mockAccountManager.getBalance(mockAccount)).thenReturn(200L);
		try{
			classUnderTest.withdraw(300L);
			Assert.fail("I should not be reached");
		}catch(Exception ex){
			Assert.assertEquals(ex.getClass(), NotEnoughFundsException.class);
		}
		//verify behavior by interaction
		Mockito.verify(mockAccountManager).findAccount(classUnderTest); //Verifies certain behavior happened once.
		Mockito.verify(mockAccountManager).getBalance(mockAccount); //Verifies certain behavior happened once.
		Mockito.verify(mockAccountManager, Mockito.times(0)).withdraw(mockAccount, 300L);
	}	
	
	@Test
	public void withdraw_whenFundIsSufficient_ReturnAccountBalance() throws NotEnoughFundsException{
		Mockito.when(mockAccountManager.getBalance(mockAccount)).thenReturn(400L).thenReturn(100L);
		long result = classUnderTest.withdraw(300L);
		
		Assert.assertEquals(result, 100L);
		//verify by count
		Mockito.verify(mockAccountManager, Mockito.atLeastOnce()).findAccount(classUnderTest);
		Mockito.verify(mockAccountManager, Mockito.times(2)).getBalance(mockAccount);
		Mockito.verify(mockAccountManager, Mockito.times(1)).withdraw(mockAccount, 300L);
		
		//verify by order
		//we verify the order in which methods were called using inOrder(). To enforce the order verification, we need to call our verify() methods on 
		//the InOrder object.
		InOrder order = Mockito.inOrder(mockAccountManager);
		order.verify(mockAccountManager).findAccount(classUnderTest);
		order.verify(mockAccountManager).getBalance(mockAccount);
		order.verify(mockAccountManager).withdraw(mockAccount, 300L);
		order.verify(mockAccountManager).getBalance(mockAccount);
		
		//unverified interaction
		//We will call verifyNoMoreInteractions(accountManager) in the end after verifying all the methods to make sure that nothing else was invoked on 
		//your mocks.
		// 2 ways to do it:
		order.verifyNoMoreInteractions();
		Mockito.verifyNoMoreInteractions(mockAccountManager);		
	}
}
