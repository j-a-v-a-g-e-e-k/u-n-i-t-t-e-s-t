package _01;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import _06_PowerMock_Static.AccountHolder;
import _06_PowerMock_Static.AccountManager;
import _06_PowerMock_Static.AccountSummary;
import _06_PowerMock_Static.Statement;
import junit.framework.Assert;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ AccountManager.class })
public class _06_PowerMock_Static {
	private AccountHolder classUnderTest = new AccountHolder("bimal", "XYZ123");
	
	
	@Test
	public void testGetMiniStatement(){
		PowerMockito.mockStatic(AccountManager.class);
		PowerMockito.when(AccountManager.getSummary(classUnderTest)).thenReturn(new AccountSummary(classUnderTest, 100L));
		
		Statement result = classUnderTest.getMiniStatement();
		Assert.assertEquals(result.getAccountSummary().getAccountHolder().getName(), "bimal");
		Assert.assertEquals(result.getAccountSummary().getCurrentBalance(), 100L);
		
		
		PowerMockito.verifyStatic();
		AccountManager.getSummary(classUnderTest);
		AccountManager.getTransactions(classUnderTest);		
	}
	
}
