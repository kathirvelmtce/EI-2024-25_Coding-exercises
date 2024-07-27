package com.ex1;


// Strategy interface
interface PaymentStrategy {
    void pay(int amount);
}

// Concrete Strategies
class CreditCardPayment implements PaymentStrategy {
    private String name;
    private String cardNumber;

    public CreditCardPayment(String name, String cardNumber) {
        this.name = name;
        this.cardNumber = cardNumber;
    }

    @Override
    public void pay(int amount) {
        System.out.println(amount + " paid with credit card (name: " + name + ", card number: " + cardNumber + ")");
    }
}

class PayPalPayment implements PaymentStrategy {
    private String email;

    public PayPalPayment(String email) {
        this.email = email;
    }

    @Override
    public void pay(int amount) {
        System.out.println(amount + " paid using PayPal (email: " + email + ")");
    }
}

// Context
class ShoppingCart {
    private PaymentStrategy paymentStrategy;

    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;
    }

    public void checkout(int amount) {
        paymentStrategy.pay(amount);
    }
}

// Usage example
public class PaymentStrategyDemo {
    public static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart();

        // Pay with Credit Card
        cart.setPaymentStrategy(new CreditCardPayment("John Doe", "1234567890123456"));
        cart.checkout(100);

        // Pay with PayPal
        cart.setPaymentStrategy(new PayPalPayment("john.doe@example.com"));
        cart.checkout(200);
    }
}