package _02;

public class Customer {
	Dish dish;
	
	public Customer(Dish dish){
		this.dish = dish;
	}

	public void eat(String spicy) throws WrongDishException, NotSuchATastyException{
		try {
			System.out.println("Taste the food");
			dish.eat(spicy);
			System.out.println("Ate the food");
		} catch (WrongDishException e) {
			System.out.println("Wrong dish!");
			throw e;
		} catch (NotSuchATastyException e) {
			System.out.println("Not very tasty");
			throw e;
		}		
	}
}
