package _01;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import _07_PowerMock_Private.PrivateMethodExample;


@RunWith(PowerMockRunner.class)
public class _07_PowerMock_Private {
	// should be PowerMockito.spy() and NOT Mockito.spy()
	private PrivateMethodExample classUnderTest = PowerMockito.spy(new PrivateMethodExample());
	
	@PrepareForTest(PrivateMethodExample.class)
	@Test	
	public void testGetDetails() throws Exception{
		PowerMockito.when(classUnderTest, "iAmPrivate").thenReturn("bimal");
		String result = classUnderTest.getDetails();
		Assert.assertEquals(result, "Mock private method example: bimal");
	}
}
