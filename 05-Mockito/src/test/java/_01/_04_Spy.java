/*
There are times when we would like to use most of the original object�s behavior but mock only a portion of it. This is called spying objects, also 
called as partial mocking.

Using Mockito�s spy feature, we can mock only those methods of a real object that we want to, thus retaining the rest of the original behavior.

We create the spy object using org.mockito.Mockito.spy(real object). In our example, we do it in the @BeforeMethod, buildSpy(). We create the Employee 
bean and then the spy object using spy(emp).
Since we have two Employee beans, one the original and the other spy, one question that naturally arises is, whether the spy object refers to the 
original object internally. Answer is No. Mockito creates a copy of the original object, so when methods are exercised on the spy object, the state of 
the original object remain unaffected. Likewise, if you interact with the real object, the spy object won�t be aware of those interactions.

doReturn():
Use doReturn() in those rare occasions when you cannot use Mockito.when(Object). 

Beware that Mockito.when(Object) is always recommended for stubbing because it is argument type-safe and more readable (especially when stubbing 
consecutive calls). 

Here are those rare occasions when doReturn() comes handy: 

1.When spying real objects and calling real methods on a spy brings side effects 
   List list = new LinkedList();
   List spy = spy(list);

   //Impossible: real method is called so spy.get(0) throws IndexOutOfBoundsException (the list is yet empty)
   when(spy.get(0)).thenReturn("foo");

   //You have to use doReturn() for stubbing:
   doReturn("foo").when(spy).get(0);

2.Overriding a previous exception-stubbing: 
   when(mock.foo()).thenThrow(new RuntimeException());

   //Impossible: the exception-stubbed foo() method is called so RuntimeException is thrown.
   when(mock.foo()).thenReturn("bar");

   //You have to use doReturn() for stubbing:
   doReturn("bar").when(mock).foo();
 

Above scenarios shows a tradeoff of Mockito's elegant syntax. Note that the scenarios are very rare, though. Spying should be sporadic and overriding 
exception-stubbing is very rare. Not to mention that in general overridding stubbing is a potential code smell that points out too much stubbing. 
 */

package _01;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import _04_Spy.Dev;
import _04_Spy.Employee;
import _04_Spy.EngineerAware;


public class _04_Spy {
	private Employee emp;
	private Employee spyEmp;

	@Before
	public void setup(){
		emp = new Employee("bimal","jain",36);
		spyEmp = Mockito.spy(emp);
	}
	
	@Test
	public void verifySpyEffectOnRealInstance(){
		spyEmp.setAge(35);
		Assert.assertFalse(emp.getAge()==spyEmp.getAge());
	}
	
	@Test
	public void verifyEmployeeDetail(){
		Assert.assertEquals(spyEmp.getFirstName(), "bimal");
		Assert.assertEquals(spyEmp.getLastName(), "jain");
		Assert.assertEquals(spyEmp.getFullName(), "bimal jain");
		Assert.assertEquals(spyEmp.getAge(), 36);
		
		Mockito.verify(spyEmp, Mockito.times(2)).getFirstName();
		Mockito.verify(spyEmp, Mockito.times(2)).getLastName();
		Mockito.verify(spyEmp, Mockito.times(1)).getFullName();
		Mockito.verify(spyEmp, Mockito.times(1)).getAge();
	}

	// Below we do the partial mocking. We train the spied Employee bean to return "bakliwal" when getLastName() is called.
	@Test
	public void spyEmployeeName(){
		Mockito.when(spyEmp.getLastName()).thenReturn("bakliwal");
		Assert.assertEquals(spyEmp.getFullName(), "bimal bakliwal");
	}

	// Spying an interface
	@Test
	public void spyOnInterface(){
		EngineerAware engineerAwareSpy = Mockito.spy(new Dev());
		Assert.assertEquals(EngineerAware.Engineer.DEV, engineerAwareSpy.getDesignation());
		
		Mockito.when(engineerAwareSpy.getDesignation()).thenReturn(EngineerAware.Engineer.QA);
		Assert.assertEquals(EngineerAware.Engineer.QA, engineerAwareSpy.getDesignation());
	}
	
	// Stubbing a final method in a Spy Object
	// we can�t train a final method. Method moveTo(), updates employee�s designation. We also have another method, finalMoveTo() which does the same 
	// as moveTo () but is a final method.
	@Test(expected=RuntimeException.class)
	public void stubNonFinalMethod(){
		EngineerAware dev = new Dev();
		Mockito.doThrow(new RuntimeException("cannot move")).when(spyEmp).moveTo(dev);
		spyEmp.moveTo(dev);
	}
	
	// Since finalMoveTo() is final, Mockito fails to train it and instead simply invokes the real object�s method, which is why it fails to throw 
	// RuntimeException.
	@Test(expected=RuntimeException.class)
	public void stubFinalMethod(){
		EngineerAware dev = new Dev();
		Mockito.doThrow(new RuntimeException("cannot move")).when(spyEmp).finalMoveTo(dev);
		spyEmp.moveTo(dev);
	}
	
	// Suppose we want to train our spy object Employee bean to return us �SPY� when getSkill(0) is called, using when() API like below, will throw 
	// NullPointerException. Note that spyEmp.getSkill(0) calls on the original method and since the List object is not yet initialized, it throws 
	// NullPointerException.	
	@Test
	public void spySkillUsingWhenThenReturn(){
		Mockito.when(spyEmp.getSkill(0)).thenReturn("java");
		Assert.assertEquals(spyEmp.getSkill(0), "java");
	}
	
	// This can be done differently using doReturn() for stubbing. For example, in the below style, we get around the NullPointerException.
	@Test
	public void spySkillUsingDoReturnWhen(){
		Mockito.doReturn("java").when(spyEmp).getSkill(0);
		Assert.assertEquals(spyEmp.getSkill(0), "java");
	}

}
