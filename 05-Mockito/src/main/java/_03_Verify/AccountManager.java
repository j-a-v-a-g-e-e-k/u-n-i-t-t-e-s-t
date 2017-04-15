package _03_Verify;

public interface AccountManager {

	long getBalance(Account account);

	long withdraw(Account account, long amount);

	Account findAccount(Customer customer);

}
