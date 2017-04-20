/*
Most of the mocking frameworks in Java, including Mockito, cannot mock static methods or final classes. If we come across a situation where we need to test these components, we won’t be able to unless we re-factor the code and make them testable. For example:
Making private methods packaged or protected
Avoiding static methods
But re-factoring at the cost of good design may not always be the right solution.
In such scenarios, it makes sense to use a testing framework like Powermock which allows us to mock even the static, final and private methods.
Good thing about Powermock is that it doesn’t re-invent the testing framework and in fact enhances the testing frameworks like Easymock and Mockito.

In the beginning of test class you will notice @RunWith annotation that contains PowerMockRunner.class as value. This statement tells JUnit to execute the test using PowerMockRunner.
You may also see annotation @PrepareForTest which takes the class to be mocked. This is required when we want to mock final classes or methods which either final, private, static or native.
We will use PowerMockito.mockStatic statement which takes in the class to be mocked. It tells PowerMockito to mock all the static methods. We then stub the static method’s behavior.

This class should be annotated with @RunWith(PowerMockRunner.class) annotation. When a class is annotated with @RunWith or extends a class annotated with @RunWith, JUnit will invoke the class it references to run the tests in that class instead of the runner built into JUnit.

We need another class level annotation for this example: @PrepareForTest. This annotation tells PowerMock to prepare certain classes for testing. Classes needed to be defined using this annotation are typically those that needs to be byte-code manipulated. This includes final classes, classes with final, private, static or native methods that should be mocked and also classes that should be return a mock object upon instantiation.
This annotation can be placed at both test classes and individual test methods. If placed on a class all test methods in this test class will be handled by PowerMock (to allow for testability). To override this behavior for a single method just place a @PrepareForTest annotation on the specific test method. This is useful in situations where for example you’d like to modify class X in test method A but in test method B you want X to be left intact. In situations like this you place a @PrepareForTest on method B and exclude class X from the list.
Sometimes you need to prepare inner classes for testing, this can be done by suppling the fully-qualified name of the inner-class that should be mocked to the list. You can also prepare whole packages for test by using wildcards. The annotation should always be combined with the @RunWith(PowerMockRunner.class) if using junit 4.x. The difference between this annotation and the @PrepareOnlyThisForTest annotation is that this annotation modifies the specified classes and all its super classes whereas the @PrepareOnlyThisForTest annotation manipulates only the specified classes.


 */


package _01;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import _05_PowerMock.Service;
import _05_PowerMock.ServiceListener;
import _05_PowerMock.SomeSystem;

@RunWith(PowerMockRunner.class)
public class _05_PowerMock {
	private SomeSystem classUnderTest;
	private Service service;
	private ServiceListener listener;
	
	@Before
	public void setup(){
		service = Mockito.mock(Service.class);
		listener = Mockito.mock(ServiceListener.class);
		// spy is only needed for tests where you want to stub some behaviour of classUnderTest, and not needed for all the tests.
	}
	
	@PrepareForTest({ SomeSystem.class })
	@Test
	public void start(){
		classUnderTest = new SomeSystem();
		classUnderTest.add(service);
		classUnderTest.setServiceListener(listener);
		
		// here we are not mocking the static method-startServiceStaticWay but mocking something which will be returned by the static method
		Mockito.when(service.start()).thenReturn(1);
		classUnderTest.start();
		Mockito.verify(listener).onSuccess(service);
		Mockito.when(service.start()).thenReturn(0);
		classUnderTest.start();
		Mockito.verify(listener).onFailure(service);
	}
	
	@PrepareForTest({ SomeSystem.class })
	@Test
	public void staticNonVoidMethod(){
		classUnderTest = new SomeSystem();
		classUnderTest.add(service);
		classUnderTest.setServiceListener(listener);
		
		PowerMockito.mockStatic(SomeSystem.class);
		
		PowerMockito.when(SomeSystem.startServiceStaticWay(service)).thenReturn(1);
		classUnderTest.start();
		Mockito.verify(listener).onSuccess(service);
		
		PowerMockito.when(SomeSystem.startServiceStaticWay(service)).thenReturn(0);
		classUnderTest.start();
		Mockito.verify(listener).onFailure(service);
		
		//We first let PowerMock know that we will are going to verify static method by calling PowerMockito.verifyStatic(). Then we actually have to invoke the static method. This is not considered as an actual method invocation but as a static method verification.
		PowerMockito.verifyStatic();
		SomeSystem.startServiceStaticWay(service);
	}
	
	@PrepareForTest({ SomeSystem.class })
	@Test
	public void staticVoidMethod() throws Exception{
		classUnderTest = new SomeSystem();
		classUnderTest.add(service);
		classUnderTest.setServiceListener(listener);
		
		PowerMockito.mockStatic(SomeSystem.class);
		// Since a void method doesn’t return anything, the earlier way of mocking static methods won’t work here.
		PowerMockito.doNothing().when(SomeSystem.class, "notifyServiceListener", listener, service, true);
		//or
		PowerMockito.doNothing().when(SomeSystem.class, "notifyServiceListener", new Object[] { listener, service, true});
		PowerMockito.when(service.start()).thenReturn(1);
		classUnderTest.start();
		
		Mockito.verify(listener, Mockito.never()).onSuccess(service);
		PowerMockito.verifyStatic();
		SomeSystem.startServiceStaticWay(service);
		
	}
	
	@PrepareForTest({ SomeSystem.class })
	@Test
	public void privateMethodAddEvent() throws Exception{
		classUnderTest = PowerMockito.spy(new SomeSystem());
		classUnderTest.add(service);
		classUnderTest.setServiceListener(listener);
		
		PowerMockito.doNothing().when(classUnderTest, "addEvent", service, true);
		Mockito.when(service.start()).thenReturn(1);
		Mockito.when(service.getName()).thenReturn("serviceA");		
		classUnderTest.start();
		Assert.assertTrue(classUnderTest.getEvents().isEmpty());
		PowerMockito.verifyPrivate(classUnderTest).invoke("addEvent", new Object[] { service, true });
	}
	
	@PrepareForTest({ SomeSystem.class })
	@Test
	public void privateMethodGetEvent() throws Exception{
		classUnderTest = PowerMockito.spy(new SomeSystem());
		classUnderTest.add(service);
		classUnderTest.setServiceListener(listener);
		
		final String serviceA = "serviceA";
		final String serviceA_is_successful = serviceA + " is successful";
		
		PowerMockito.when(classUnderTest, "getEvent", serviceA, true).thenReturn(serviceA_is_successful);
		Mockito.when(service.start()).thenReturn(1);
		Mockito.when(service.getName()).thenReturn(serviceA);
		
		classUnderTest.start();
		Assert.assertTrue(!classUnderTest.getEvents().isEmpty());
		Assert.assertEquals(serviceA_is_successful, classUnderTest.getEvents().get(0));
		System.out.println(classUnderTest.getEvents());
		PowerMockito.verifyPrivate(classUnderTest).invoke("getEvent", new Object[] { serviceA, true });
	}
	
}
