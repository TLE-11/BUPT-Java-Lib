package Rational;

import java.math.BigInteger;

public class TestRationalClass {
    /** Main method */
    public static void main(String[] args) {
        // Create and initialize two rational numbers r1 and r2 using BigInteger
        Rational r1 = new Rational(BigInteger.valueOf(14), BigInteger.valueOf(37));
        Rational r2 = new Rational(BigInteger.valueOf(23), BigInteger.valueOf(67));

        // Display results
        System.out.println(r1 + " + " + r2 + " = " + r1.add(r2));
        System.out.println(r1 + " - " + r2 + " = " + r1.subtract(r2));
        System.out.println(r1 + " * " + r2 + " = " + r1.multiply(r2));
        System.out.println(r1 + " / " + r2 + " = " + r1.divide(r2));
        System.out.println(r2 + " is " + r2.doubleValue());

        Rational rational = new Rational(BigInteger.valueOf(0), BigInteger.valueOf(1));
        for (int i = 2; i <= 100; i++) {
            // 计算1/2 + 2/3 + 3/4 + ... + 99/100的和
            rational = rational.add(new Rational(BigInteger.valueOf(i - 1), BigInteger.valueOf(i)));
        }
        System.out.println("Final result: " + rational);
        System.out.println("Double value: " + rational.doubleValue());

        BigInteger bi1 = new BigInteger("232947298742947294274927897");
        BigInteger bi2 = new BigInteger("232947298742947294274927897");

        BigInteger bi3 = bi1.multiply(bi2);
        System.out.println(bi3);
        /*
           14/37 + 23/67 = 1789/2479
           14/37 - 23/67 = 87/2479
           14/37 * 23/67 = 322/2479
           14/37 / 23/67 = 938/851
           23/67 is 0.34328358208955223

           Final result: 264414864639329557497913717698145082779489/
                         2788815009188499086581352357412492142272
           Double value: 94.81262248236038
           */
    }
}