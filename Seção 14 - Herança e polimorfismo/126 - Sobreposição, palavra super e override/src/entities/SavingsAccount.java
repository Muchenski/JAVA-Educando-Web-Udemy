package entities;

public class SavingsAccount extends Account {

	private Double interestRate;

	public SavingsAccount() {
	}

	public SavingsAccount(Integer number, String holder, Double balance, Double interestRate) {
		super(number, holder, balance);
		this.interestRate = interestRate;
	}

	@Override
	public void withDraw(Double amount) {
		balance -= amount;
	}

	public void updateBalance() {
		balance *= interestRate + balance;
	}

	public Double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(Double interestRate) {
		this.interestRate = interestRate;
	}

}
