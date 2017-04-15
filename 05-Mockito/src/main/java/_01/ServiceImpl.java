package _01;

public class ServiceImpl {
	IDao foo;
	public ServiceImpl(IDao dao){
		this.foo = dao;
	}
	
	public String question(String question) throws InvalidQuestionException, DAONotAvailableException {
		verifyFooConnection(foo);
		String answer= foo.question(question);		
		switch (answer) {
		case IDao.NO_NEW_TOPIC:
			foo.bye();
			break;
		case IDao.YES_NEW_TOPICS_AVAILABLE:
			answer = foo.question(IDao.WHAT_IS_TODAYS_TOPIC);
			int price = foo.getPrice(answer);
			foo.bye();
			answer = "Topic is " + answer + ", price is " + price;
			break;
		default:
			System.out.println("Answer is " + answer);
			break;
		}
		return answer;
	}
	
	public void verifyFooConnection(IDao foo) throws DAONotAvailableException {
		System.out.println("Is Foo available?");
		String response = foo.greet();
		if (!IDao.HELLO_WORLD.equals(response)) {
			System.out.println("No");
			throw new DAONotAvailableException();
		}
		System.out.println("Yes");
	}	
}
