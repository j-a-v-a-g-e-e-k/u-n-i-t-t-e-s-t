package _06_PowerMock_Static;

public class AccountSummary {
	private AccountHolder accountHolder;
	private long currentBalance;

	public AccountSummary(AccountHolder accountHolder, long currentBalance) {
		this.accountHolder = accountHolder;
		this.currentBalance = currentBalance;
	}

	public AccountHolder getAccountHolder() {
		return accountHolder;
	}

	public long getCurrentBalance() {
		return currentBalance;
	}
}