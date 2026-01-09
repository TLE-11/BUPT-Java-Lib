package Rational;

import java.math.BigInteger;

public class Rational extends Number implements Comparable<Rational> {
  // Data fields for numerator and denominator
  private BigInteger numerator = BigInteger.valueOf(0);
  private BigInteger denominator = BigInteger.valueOf(1);

  /** Construct a rational with default properties */
  public Rational() {
    this(BigInteger.valueOf(0), BigInteger.valueOf(1));
  }

  /** Construct a rational with specified numerator and denominator */
  public Rational(BigInteger numerator, BigInteger denominator) {
    if (denominator.equals(BigInteger.ZERO)) {
      throw new IllegalArgumentException("Denominator cannot be zero");
    }

    BigInteger gcd = gcd(numerator.abs(), denominator.abs());
    int sign = denominator.compareTo(BigInteger.ZERO) > 0 ? 1 : -1;

    this.numerator = numerator.divide(gcd).multiply(BigInteger.valueOf(sign));
    this.denominator = denominator.abs().divide(gcd);
  }

  /** Find GCD of two numbers using Euclidean algorithm */
  private static BigInteger gcd(BigInteger a, BigInteger b) {
    while (!b.equals(BigInteger.ZERO)) {
      BigInteger temp = b;
      b = a.mod(b);
      a = temp;
    }
    return a;
  }

  // 其余方法保持不变...
  /** Return numerator */
  public BigInteger getNumerator() {
    return numerator;
  }

  /** Return denominator */
  public BigInteger getDenominator() {
    return denominator;
  }

  /** Add a rational number to this rational */
  public Rational add(Rational secondRational) {
    BigInteger n = numerator.multiply(secondRational.getDenominator())
            .add(denominator.multiply(secondRational.getNumerator()));
    BigInteger d = denominator.multiply(secondRational.getDenominator());
    return new Rational(n, d);
  }

  /** Subtract a rational number from this rational */
  public Rational subtract(Rational secondRational) {
    BigInteger n = numerator.multiply(secondRational.getDenominator())
            .subtract(denominator.multiply(secondRational.getNumerator()));
    BigInteger d = denominator.multiply(secondRational.getDenominator());
    return new Rational(n, d);
  }

  /** Multiply a rational number to this rational */
  public Rational multiply(Rational secondRational) {
    BigInteger n = numerator.multiply(secondRational.getNumerator());
    BigInteger d = denominator.multiply(secondRational.getDenominator());
    return new Rational(n, d);
  }

  /** Divide a rational number from this rational */
  public Rational divide(Rational secondRational) {
    BigInteger n = numerator.multiply(secondRational.getDenominator());
    BigInteger d = denominator.multiply(secondRational.getNumerator());
    return new Rational(n, d);
  }

  @Override
  public String toString() {
    if (denominator.equals(BigInteger.ONE)) {
      return numerator.toString();
    } else {
      return numerator + "/" + denominator;
    }
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
        return true;
    }
    if (!(other instanceof Rational)) {
        return false;
    }
    Rational otherRational = (Rational) other;
    return numerator.multiply(otherRational.denominator)
            .equals(denominator.multiply(otherRational.numerator));
  }

  @Override
  public int intValue() {
    return numerator.divide(denominator).intValue();
  }

  @Override
  public float floatValue() {
    return numerator.floatValue() / denominator.floatValue();
  }

  @Override
  public double doubleValue() {
    return numerator.doubleValue() / denominator.doubleValue();
  }

  @Override
  public long longValue() {
    return numerator.divide(denominator).longValue();
  }

  @Override
  public int compareTo(Rational o) {
    BigInteger left = numerator.multiply(o.denominator);
    BigInteger right = denominator.multiply(o.numerator);
    return left.compareTo(right);
  }
}