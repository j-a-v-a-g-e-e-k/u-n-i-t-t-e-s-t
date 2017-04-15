package _02;

public interface Dish {
	void eat(String spicy) throws WrongDishException, NotSuchATastyException;
	String getSpice();
}