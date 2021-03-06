/*
============
VOID METHODS
============
For void methods, we cannot use when().thenReturn() or when().thenThrow().
We can stub a void method to throw an exception using doThrow(). Other than that we can also make use of doNothing() or doAnswer() APIs.

Stub void method Using doAnswer
Suppose we want to custom behavior a method's behavior based on the arguments passed then we can use doAnswer() API.
Answer interface specifies an action that is executed when you interact with the mock�s method. We can customize the behavior based on the mockito's 
method name or the method arguments which is passed to it. In case of non-void methods, you can even make the answer to customize the method's return 
value.
 */

package _01;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import _02_VoidMethod.Customer;
import _02_VoidMethod.Dish;
import _02_VoidMethod.NotSuchATastyException;
import _02_VoidMethod.WrongDishException;


public class _02_VoidMethod {
	private Customer classUnderTest;
	private Dish dishMock;

	@Before
	public void setup(){
		dishMock = Mockito.mock(Dish.class);
		classUnderTest = new Customer(dishMock);		
	}

	@Test
	public void eat_doNothing() throws WrongDishException, NotSuchATastyException{
		Mockito.doNothing().when(dishMock).eat("any spice level");
		classUnderTest.eat("any spice level");
		System.out.println("Finished the dish, no exception thrown");
	}

	@Test(expected=WrongDishException.class)
	public void eat_throwWrongDishException() throws WrongDishException, NotSuchATastyException{
		Mockito.doThrow(new WrongDishException()).when(dishMock).eat("any spice level");
		classUnderTest.eat("any spice level");
	}

	@Test(expected=NotSuchATastyException.class)
	public void eat_throwNotSuchATastyException() throws WrongDishException, NotSuchATastyException{
		Mockito.doThrow(new NotSuchATastyException()).when(dishMock).eat("any spice level");
		classUnderTest.eat("any spice level");
	}

	@Test(expected=RuntimeException.class)
	public void eat_ifVerySpicy_ThrowException() throws WrongDishException, NotSuchATastyException{
		String spicy = "spicy";
		Mockito.doAnswer(new SpiceAnswer()).when(dishMock).eat(spicy);
		classUnderTest.eat(spicy);

		spicy = "too spicy";
		Mockito.doAnswer(new SpiceAnswer()).when(dishMock).eat(spicy);
		classUnderTest.eat(spicy);
	}

	private class SpiceAnswer implements Answer<Void> {
		@Override
		public Void answer(InvocationOnMock invocation) throws Throwable {
			String arg = (String) invocation.getArguments()[0];
			if ("too spicy".equals(arg) || "extra spicy".equals(arg)) {
				throw new RuntimeException("Spicy dish!");
			}
			return null;
		}		
	}

	// Stub void method with consecutive calls
	@Test
	public void eatMultipleDishes() throws WrongDishException, NotSuchATastyException {
		Mockito.doThrow(NotSuchATastyException.class)
		.doNothing()
		.doAnswer(new SpiceAnswer())
		.when(dishMock).eat("extra spicy");
		try {
			classUnderTest.eat("extra spicy");
			Assert.fail("I should not be reached");
		} catch(NotSuchATastyException ex) {
			Assert.assertEquals(ex.getClass(), NotSuchATastyException.class);;
		}
		classUnderTest.eat("extra spicy");
		System.out.println("Finished the dish, no exception thrown");
		try{
			classUnderTest.eat("extra spicy");	
		}catch(RuntimeException ex){
			Assert.assertEquals(ex.getClass(), RuntimeException.class);
		}
	}

}
