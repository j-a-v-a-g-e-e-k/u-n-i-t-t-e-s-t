/*
 * 
 * 1. What is mocking?
Mocking is a testing technique where real components are replaced with objects that have a predefined behavior (mock objects) only for the test/tests that have been created for. In other words, a mock object is an object that is configured to return a specific output for a specific input, without performing any real action.
1.1. Why should we mock?
If we start mocking wildly, without understanding why mocking is important and how can it help us, we will probably put on doubt the usefulness of mocking.
There are several scenarios where we should use mocks:
When we want to test a component that depends on other component, but which is not yet developed. This happens often when working in team, and component development is divided between several developers. If mocking wouldn’t exist, we would have to wait until the other developer/developers end the required component/component for testing ours.
When the real component performs slow operations, usual with dealing with database connections or other intense disk read/write operations. It is not weird to face database queries that can take 10, 20 or more seconds in production environments. Forcing our tests to wait that time would be a considerable waste of useful time that can be spent in other important parts of the development.
When there are infrastructure concerns that would make impossible the testing. This is similar in same way to the first scenario described when, for example, our development connects to a database, but the server where is hosted is not configured or accessible for some reason.

argThat: Allows creating custom argument matchers		

Instead of testing the code against one invalid value, it now tests on a sub-set of values using the ArgumentMatcher. We pass in a 
org.mockito.ArgumentMatcher object to argThat(), so that the argument passed in to DAOMock.question() can be tested against the matcher to know whether 
it is one of the arguments expected by the mock object. If yes, then the next stub action will follow, in our case, the method will throw an 
InvalidQuestionException, if the argument value is not a valid question.
When it needs to return different values for different arguments, Mockito's argument matcher is handy 

The API follows builder pattern where each method returns us an Object of type OngoingStubbing so that we can stub further on the returned object thus 
allowing us to build the expectations fluently.
 */

package _01;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import _01_HelloWorld.DAONotAvailableException;
import _01_HelloWorld.IDao;
import _01_HelloWorld.InvalidQuestionException;
import _01_HelloWorld.ServiceImpl;

@SuppressWarnings({ "unchecked", "deprecation" })
public class _01_HelloWorld {
	// For this test ServiceImpl is also called System Under Test(SUT)
	private ServiceImpl classUnderTest;
	// For this test IDao is also called collaborator
	private IDao DAOMock;

	// called right before the invocation of each test method
	@Before 
	public void setup(){		
		//ARRANGE
		DAOMock = mock(IDao.class); 
		classUnderTest = new ServiceImpl(DAOMock);
	}

	@Test(expected=DAONotAvailableException.class)
	public void question_WhenDAOIsNotAvailable_ThrowDAONotAvailableException() throws InvalidQuestionException, DAONotAvailableException{		
		// ARRANGE: SETUP MOCK EXPECTATION
		when(DAOMock.greet()).thenReturn(null); 		
		// ACT
		classUnderTest.question("any question"); 		
		// ASSERT is done above. Also with this approach, we cannot check if certain methods were called or not after an exception is thrown.
		// below should not be reached
		Assert.fail();
	}
	
	// if you catch the exception, then you can further verify other things.
	@Test
	public void question_WhenDAOIsNotAvailable_ThrowDAONotAvailableException2() throws InvalidQuestionException, DAONotAvailableException{
		// ARRANGE: SETUP MOCK EXPECTATION
		when(DAOMock.greet()).thenReturn(null); 		
		try{
			// ACT
			classUnderTest.question("any question");
			// ASSERT
			Assert.fail("I should not be reached");
		} catch(Exception ex){
			Assert.assertEquals(ex.getClass(), DAONotAvailableException.class);
		}
		Mockito.verify(DAOMock, Mockito.times(1)).greet();
		Mockito.verify(DAOMock, Mockito.never()).question(IDao.ANY_NEW_TOPICS);
		Mockito.verify(DAOMock, Mockito.never()).question(IDao.WHAT_IS_TODAYS_TOPIC);
		Mockito.verify(DAOMock, Mockito.never()).getPrice(IDao.TOPIC_MOCKITO);
		Mockito.verify(DAOMock, Mockito.never()).bye();
	}

	public void question_WhenInvalidQuestionIsAsked_ThrowInvalidQuestionException() throws InvalidQuestionException, DAONotAvailableException{		
		// ARRANGE: SETUP MOCK EXPECTATION
		when(DAOMock.greet()).thenReturn(IDao.HELLO_WORLD); 
		// can be done in different ways as shown below
		// when(DAOMock.question(Mockito.anyString())).thenThrow(new InvalidQuestionException());
		// when(DAOMock.question("INVALID_QUESTION")).thenThrow(new InvalidQuestionException());
		when(DAOMock.question((String) argThat(new ArgumentMatcher<Object>() {
			@Override
			public boolean matches(Object argument) {
				return !IDao.ANY_NEW_TOPICS.equals(argument) || !IDao.WHAT_IS_TODAYS_TOPIC.equals(argument);
			}			
		}))).thenThrow(new InvalidQuestionException());		
		try{
			// ACT
			classUnderTest.question("INVALID_QUESTION");
			// ASSERT
			Assert.fail("I should not be reached");
		} catch(Exception ex){
			Assert.assertEquals(ex.getClass(), InvalidQuestionException.class);
		}
		Mockito.verify(DAOMock, Mockito.times(1)).greet();
		Mockito.verify(DAOMock, Mockito.times(1)).question("INVALID_QUESTION");
		Mockito.verify(DAOMock, Mockito.never()).question(IDao.ANY_NEW_TOPICS);
		Mockito.verify(DAOMock, Mockito.never()).question(IDao.WHAT_IS_TODAYS_TOPIC);
		Mockito.verify(DAOMock, Mockito.never()).getPrice(IDao.TOPIC_MOCKITO);
		Mockito.verify(DAOMock, Mockito.never()).bye();
	}

	@Test
	public void question_WhenNoNewTopicIsAvailable() throws InvalidQuestionException, DAONotAvailableException{		
		// ARRANGE: SETUP MOCK EXPECTATION
		when(DAOMock.greet()).thenReturn(IDao.HELLO_WORLD); 
		when(DAOMock.question(IDao.ANY_NEW_TOPICS)).thenReturn(IDao.NO_NEW_TOPIC);		
		// ACT
		String result = classUnderTest.question(IDao.ANY_NEW_TOPICS);				
		// ASSERT
		// state verification
		Assert.assertEquals(result, IDao.NO_NEW_TOPIC); 
		// behavior verification
		verify(DAOMock, times(1)).question(IDao.ANY_NEW_TOPICS);
		verify(DAOMock, never()).question(IDao.WHAT_IS_TODAYS_TOPIC);
		verify(DAOMock, never()).getPrice(IDao.TOPIC_MOCKITO);
		verify(DAOMock, times(1)).bye();
	}

	@Test
	public void question_WhenNewTopicIsAvailable() throws InvalidQuestionException, DAONotAvailableException{
		// ARRANGE: SETUP MOCK EXPECTATION
		when(DAOMock.greet()).thenReturn(IDao.HELLO_WORLD); 
		when(DAOMock.question(IDao.ANY_NEW_TOPICS)).thenReturn(IDao.YES_NEW_TOPICS_AVAILABLE);
		when(DAOMock.question(IDao.WHAT_IS_TODAYS_TOPIC)).thenReturn(IDao.TOPIC_MOCKITO);
		when(DAOMock.getPrice(IDao.TOPIC_MOCKITO)).thenReturn(99);		
		// ACT
		String result = classUnderTest.question(IDao.ANY_NEW_TOPICS);		
		// ASSERT
		Assert.assertEquals(result, "Topic is Mockito, price is 99");
		verify(DAOMock, times(1)).question(IDao.ANY_NEW_TOPICS);
		verify(DAOMock, times(1)).question(IDao.WHAT_IS_TODAYS_TOPIC);
		verify(DAOMock, times(1)).getPrice(IDao.TOPIC_MOCKITO);
		verify(DAOMock, times(1)).bye();
	}

	// Behavior Verification using ArgumentMatcher and Answer Callback. Same test as above.
	@Test
	public void question_WhenNewTopicIsAvailable2() throws InvalidQuestionException, DAONotAvailableException{
		// ARRANGE: SETUP MOCK EXPECTATION
		when(DAOMock.greet()).thenReturn(IDao.HELLO_WORLD); 		
		when(DAOMock.question((String) argThat(new ArgumentMatcher<Object>(){
			@Override
			public boolean matches(Object argument) {
				return IDao.ANY_NEW_TOPICS.equals(argument) || IDao.WHAT_IS_TODAYS_TOPIC.equals(argument);
			}
		})))
		.thenAnswer(new Answer<String>(){
			@Override
			public String answer(InvocationOnMock invocation) throws Throwable {
				String arg = (String) invocation.getArguments()[0];
				if (IDao.ANY_NEW_TOPICS.equals(arg)) {
					return IDao.YES_NEW_TOPICS_AVAILABLE;
				} else if (IDao.WHAT_IS_TODAYS_TOPIC.equals(arg)) {
					return IDao.TOPIC_MOCKITO;
				} else {
					throw new InvalidQuestionException();
				}
			}
		});
		when(DAOMock.getPrice(IDao.TOPIC_MOCKITO)).thenReturn(99);
		// ACT
		String result = classUnderTest.question(IDao.ANY_NEW_TOPICS);
		// ASSERT
		Assert.assertEquals(result, "Topic is Mockito, price is 99");
		verify(DAOMock, times(1)).question(IDao.ANY_NEW_TOPICS);
		verify(DAOMock, times(1)).question(IDao.WHAT_IS_TODAYS_TOPIC);
		verify(DAOMock, times(1)).getPrice(IDao.TOPIC_MOCKITO);
		verify(DAOMock, times(1)).bye();
	}

}
