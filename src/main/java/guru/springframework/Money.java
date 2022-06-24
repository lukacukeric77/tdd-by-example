package guru.springframework;

import java.util.Objects;

public class Money implements Expression{

    protected final int amount;
    protected final String currency;

    public Money(int amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    protected String currency() {
        return currency;
    }

    public static Money dollar(int amount) {
        return new Money(amount, CurrencyUtil.USD);
    }

    public static Money franc(int amount) {
        return new Money(amount, CurrencyUtil.CHF);
    }

    public boolean equals(Object object) {
        Money money = (Money) object;

        if (money != null) {
            return amount == money.amount && Objects.equals(this.currency, money.currency);
        } else {
            return false;
        }

    }

    @Override
    public String toString() {
        return "Money{" +
            "amount=" + amount +
            ", currency='" + currency + '\'' +
            '}';
    }

    @Override
    public Money reduce(Bank bank, String to) {
        return new Money(amount / bank.rate(this.currency, to), to);
    }

    @Override
    public Expression times(int multiplier) {
        return new Money(amount * multiplier, this.currency);
    }

    @Override
    public Expression plus(Expression added) {
        return new Sum(this, added);
    }

}
