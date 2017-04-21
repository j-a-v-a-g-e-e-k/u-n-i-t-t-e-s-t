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
	//For this test ServiceImpl is also called System Under Test(SUT)
	private ServiceImpl classUnderTest;
	//For this test IDao is also called collaborator
	private IDao DAOMock;

	// gets called right before the invocation of each test method
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
		//ACT
		classUnderTest.question("any question"); 		
		//ASSERT is done above. Also with this approach, we cannot check if certain methods were called or not after an exception is thrown.
	}
	
	// if you catch the exception, then you can further verify other things.
	@Test
	public void question_WhenDAOIsNotAvailable_ThrowDAONotAvailableException2() throws InvalidQuestionException, DAONotAvailableException{
		// ARRANGE: SETUP MOCK EXPECTATION
		when(DAOMock.greet()).thenReturn(null); 
		//ACT
		try{
			classUnderTest.question("any question");
			Assert.fail("I should not be reached");
		} catch(Exception ex){
			Assert.assertEquals(ex.getClass(), DAONotAvailableException.class);
		}
		//ASSERT
		verify(DAOMock, never()).question(IDao.ANY_NEW_TOPICS);
		verify(DAOMock, never()).question(IDao.WHAT_IS_TODAYS_TOPIC);
		verify(DAOMock, never()).getPrice(IDao.TOPIC_MOCKITO);
		verify(DAOMock, never()).bye();
	}

	@Test(expected=InvalidQuestionException.class)
	public void question_WhenInvalidQuestionIsAsked_ThrowInvalidQuestionException() throws InvalidQuestionException, DAONotAvailableException{		
		// ARRANGE: SETUP MOCK EXPECTATION
		when(DAOMock.greet()).thenReturn(IDao.HELLO_WORLD); 
		// can be done in either way shown below
//		when(DAOMock.question(Mockito.anyString())).thenThrow(new InvalidQuestionException());
//		when(DAOMock.question("INVALID_QUESTION")).thenThrow(new InvalidQuestionException());
		// argThat: Allows creating custom argument matchers
		// The API follows builder pattern where each method returns us an Object of type OngoingStubbing so that we can stub further on the returned 
		// object thus allowing us to build the expectations fluently.
		// instead of testing the code against one invalid value, it now tests on a sub-set of values using the ArgumentMatcher. We pass in a 
		// org.mockito.ArgumentMatcher object to argThat(), so that the argument passed in to DAOMock.question() can be tested against the matcher 
		// to know whether it is one of the arguments expected by the mock object. If yes, then the next stub action will follow, in our case, the 
		// method will throw an InvalidQuestionException, if the argument value is not a valid question.
		when(DAOMock.question((String) argThat(new ArgumentMatcher<Object>() {
			@Override
			public boolean matches(Object argument) {
				return !IDao.ANY_NEW_TOPICS.equals(argument) || !IDao.WHAT_IS_TODAYS_TOPIC.equals(argument);
			}			
		}))).thenThrow(new InvalidQuestionException());		
		//ACT
		classUnderTest.question("INVALID_QUESTION");
	}

	@Test
	public void question_WhenNoNewTopicIsAvailable() throws InvalidQuestionException, DAONotAvailableException{		
		// ARRANGE: SETUP MOCK EXPECTATION
		when(DAOMock.greet()).thenReturn(IDao.HELLO_WORLD); 
		when(DAOMock.question(IDao.ANY_NEW_TOPICS)).thenReturn(IDao.NO_NEW_TOPIC);		
		//ACT
		String result = classUnderTest.question(IDao.ANY_NEW_TOPICS);				
		//ASSERT
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
		//ACT
		String result = classUnderTest.question(IDao.ANY_NEW_TOPICS);		
		//ASSERT
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
		//when it needs to return different values for different arguments, Mockito�s argument matcher is handy 
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
