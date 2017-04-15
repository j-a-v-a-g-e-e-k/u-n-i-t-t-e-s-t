/*
For void methods, we cannot use when().thenReturn() or when().thenThrow().
We can stub a void method to throw an exception using doThrow(). Other than that we can also make use of doNothing(), doAnswer() or doReturn() APIs.

Stub void method Using doAnswer
Suppose we want to custom behavior a method’s behavior based on the arguments passed then we can use doAnswer() API.
Answer interface specifies an action that is executed when you interact with the mock’s method. We can customize the behavior based on the mock’s method 
name or the method arguments which is passed to it. In case of non-void methods, you can even make the answer to customize the method’s return value.

 */

package _01;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import _02.Customer;
import _02.Dish;
import _02.NotSuchATastyException;
import _02.WrongDishException;
import junit.framework.Assert;

public class _02_Test {
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

	private class SpiceAnswer implements Answer {
		@Override
		public String answer(InvocationOnMock invocation) throws Throwable {
			String arg = (String) invocation.getArguments()[0];
			if ("too spicy".equals(arg)) {
				throw new RuntimeException("Spicy dish!");
			}
			return arg;
		}		
	}

	// Stub void method with consecutive calls
	@Test
	public void eatMultipleDishes() throws WrongDishException, NotSuchATastyException {
		System.out.println("Train dish to not throw NotSoTastyException when called first time and return in subsequent calls");
		Mockito.doThrow(NotSuchATastyException.class)
			.doNothing()
			.when(dishMock).eat("medium");
		try {
			classUnderTest.eat("medium");
			System.out.println("I should not be printed");
			Assert.fail("allows eating, should have failed with NotSoTastyException");
		} catch(NotSuchATastyException e) {
			System.out.println("Coudln't eat the dish, not very tasty");
		}
		classUnderTest.eat("medium");
		System.out.println("Finished the dish, no exception thrown");
		classUnderTest.eat("medium");
		System.out.println("Finished the dish, no exception thrown");		
	}

}
