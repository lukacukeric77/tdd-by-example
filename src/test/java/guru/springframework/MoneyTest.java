package guru.springframework;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class MoneyTest {

    @Test
    void testMultiplication() {
        Expression five = Money.dollar(5);
        assertEquals(Money.dollar(10), five.times(2));
        assertEquals(Money.dollar(15), five.times(3));

        Expression five1 = Money.franc(5);
        assertEquals(Money.franc(10), five1.times(2));
    }

    @Test
    void testEquality() {
        assertEquals(Money.dollar(5), Money.dollar(5));
        assertNotEquals(Money.dollar(5), Money.dollar(8));
        assertNotEquals(Money.dollar(5), Money.franc(5));
        assertNotEquals(Money.franc(5), Money.franc(8));
        assertEquals(Money.franc(5), Money.franc(5));
    }

    @Test
    void testCurrency() {
        assertEquals(CurrencyUtil.USD, Money.dollar(1).currency());
        assertEquals(CurrencyUtil.CHF, Money.franc(1).currency());
    }

    @Test
    void testSimpleAddition() {
        Expression five = Money.dollar(5);
        Expression sum = five.plus(five);
        Bank bank = new Bank();
        Expression reduced = bank.reduce(sum, CurrencyUtil.USD);
        assertEquals(Money.dollar(10), reduced);
    }

    @Test
    void testPlusReturnsSum() {
        Expression five = Money.dollar(5);
        Expression result = five.plus(five);
        Sum sum = (Sum) result;
        assertEquals(five, sum.augmend);
        assertEquals(five, sum.addmend);
    }

    @Test
    void testReduceSum() {
        Expression sum = new Sum(Money.dollar(3), Money.dollar(4));
        Bank bank = new Bank();
        Expression result = bank.reduce(sum, CurrencyUtil.USD);
        assertEquals(Money.dollar(7), result);
    }

    @Test
    void testReduceMoney() {
        Bank bank = new Bank();
        Expression result = bank.reduce(Money.dollar(1), CurrencyUtil.USD);
        assertEquals(Money.dollar(1), result);
    }

    @Test
    void testReduceMoneyDifferentCurrency() {
        Bank bank = new Bank();
        bank.addRate(CurrencyUtil.CHF, CurrencyUtil.USD, 2);
        Expression result = bank.reduce(Money.franc(2), CurrencyUtil.USD);
        assertEquals(Money.dollar(1), result);
    }

    @Test
    void testIdentityRate() {
        assertEquals(1, new Bank().rate(CurrencyUtil.USD, CurrencyUtil.USD));
        assertEquals(1, new Bank().rate(CurrencyUtil.CHF, CurrencyUtil.CHF));
    }

    @Test
    void testMixedAddition() {
        Expression fiveBucks = Money.dollar(5);
        Expression tenFrancs = Money.franc(10);
        Bank bank = new Bank();
        bank.addRate(CurrencyUtil.CHF, CurrencyUtil.USD, 2);
        Expression result = bank.reduce(fiveBucks.plus(tenFrancs), CurrencyUtil.USD);
        assertEquals(Money.dollar(10), result);
    }

    @Test
    void testSumPlusMoney() {
        Expression fiveBucks = Money.dollar(5);
        Expression tenFrancs = Money.franc(10);
        Bank bank = new Bank();
        bank.addRate(CurrencyUtil.CHF, CurrencyUtil.USD, 2);
        Expression sum = new Sum(fiveBucks, tenFrancs).plus(fiveBucks);
        Expression result = bank.reduce(sum, CurrencyUtil.USD);
        assertEquals(Money.dollar(15), result);
    }

    @Test
    void testSumTimes() {
        Expression fiveBucks = Money.dollar(5);
        Expression tenFrancs = Money.franc(10);
        Bank bank = new Bank();
        bank.addRate(CurrencyUtil.CHF, CurrencyUtil.USD, 2);
        Expression sum = new Sum(fiveBucks, tenFrancs).times(2);
        Expression result = bank.reduce(sum, CurrencyUtil.USD);
        assertEquals(Money.dollar(20), result);
    }

    @Test
    void testToString() {
        String expected = "Money{amount=3, currency='USD'}";
        Money test = Money.dollar(3);
        assertEquals(test.toString(), expected);
    }

    @Test
    void testPairMoneyEquals() {
        Pair pair = new Pair(CurrencyUtil.USD, CurrencyUtil.CHF);
        assertEquals(pair, pair);
        Money money = new Money(3, CurrencyUtil.USD);
        assertNotEquals(pair, money);
        assertNotEquals(money, null);
    }
}
