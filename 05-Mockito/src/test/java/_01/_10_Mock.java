package _01;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.testng.Assert;

import _10_Mock.Adder;
import _10_Mock.CalculatorApplication;
import _10_Mock.Divisor;
import _10_Mock.Multiplier;
import _10_Mock.Subtracter;


public class _10_Mock {
	private CalculatorApplication classUnderTest;
	private Adder addMock;
	private Divisor divideMock;
	private Multiplier multipleMock;
	private Subtracter subMock;
	
	@Before
	public void setup(){		
		addMock = Mockito.mock(Adder.class);
		divideMock = Mockito.mock(Divisor.class);
		multipleMock = Mockito.mock(Multiplier.class);
		subMock = Mockito.mock(Subtracter.class);
		classUnderTest = new CalculatorApplication(addMock, subMock, multipleMock, divideMock);
	}
	
	@Test
	public void add(){
		Mockito.when(addMock.add(1, 2)).thenReturn(3);
		Object result = classUnderTest.add(1, 2);
		Assert.assertEquals(result, 3);
	}
	

}
