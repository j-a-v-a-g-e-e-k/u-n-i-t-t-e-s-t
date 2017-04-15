package _02_VoidMethod;

public interface Dish {
	void eat(String spicy) throws WrongDishException, NotSuchATastyException;
	String getSpice();
}