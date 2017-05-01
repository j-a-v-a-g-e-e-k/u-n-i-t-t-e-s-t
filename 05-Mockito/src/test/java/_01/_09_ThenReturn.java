/*
The thenReturn() methods lets you define the return value when a particular method of the mocked object is been called. 
 */
package _01;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class _09_ThenReturn {
	
	@Test
	public void test1(){
		Iterator i = Mockito.mock(Iterator.class);
		Mockito.when(i.next()).thenReturn("bimal")
								.thenReturn("jain");
		String result = i.next() + " " + i.next();
		Assert.assertEquals(result, "bimal jain");
	}
	
	@Test
	public void test2(){
		Comparable c = Mockito.mock(Comparable.class);
		Mockito.when(c.compareTo("java")).thenReturn(100);
		Mockito.when(c.compareTo("python")).thenReturn(200);
		Assert.assertEquals(c.compareTo("java"), 100);
		
		Mockito.when(c.compareTo(Mockito.anyInt())).thenReturn(0);
		Assert.assertEquals(c.compareTo(9), 0);
	}

}
