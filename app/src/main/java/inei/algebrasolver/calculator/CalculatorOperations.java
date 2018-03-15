/* contains the methods used by the calculator activity to do the math, operations etc.
 *
 */
package inei.algebrasolver.calculator;

import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculatorOperations {

    //the number of decimal places of which to round for division. Higner values will take longer and use more memory.
    static final int DIVISION_PRECISION = 50;
    //the number of times the formula for evaluating a square root is repeated. Higher values will take longer and use more memory.
    static final int SQUARE_ROOT_PRECISION = 100;
    //the tolerance for lack of precision, within a certain amount of decimal places. 28 right now.
    static final BigDecimal SQUARE_ROOT_PRECISION_TOLERANCE = (BigDecimal.valueOf(10).pow(28));
    //
    static final BigDecimal AGM_TORELANCE = (BigDecimal.valueOf(10)).pow(15);

    //adds var1 and var2.
    public static BigDecimal add(BigDecimal var1, BigDecimal var2) {
        return var1.add(var2);
    }

    //subtracts var2 from var1.
    public static BigDecimal subtract(BigDecimal var1, BigDecimal var2) {
        return var1.subtract(var2);
    }

    //multiplies var1 and var2.
    public static BigDecimal multiply(BigDecimal var1, BigDecimal var2) {
        return var1.multiply(var2);
    }

    //divides var1 by var2, with the amount of numbers past the decimal point being equal to DIVISION_PRECISION
    public static BigDecimal divide(BigDecimal var1, BigDecimal var2) {
        return var1.divide(var2, DIVISION_PRECISION, BigDecimal.ROUND_HALF_EVEN);
    }

    /*raises var1 to the power of var2. Because of laziness, only integer values can be used for var2.
      an andriod system doesn't have enough memory to do such a large calculation anyway.*/
    public static BigDecimal exponent(BigDecimal var1, BigDecimal var2) {

        if(var2.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) == 1) {
            return var1.pow(var2.intValue());
        } else {
            return BigDecimal.ZERO;
        }
    }

    //squares var.
    public static BigDecimal square(BigDecimal var){
        return var.pow(2);
    }

    //gets the square root of var, using the babylonian method. See https://en.wikipedia.org/wiki/Methods_of_computing_square_roots#babylonian_method.
    public static BigDecimal squareroot(BigDecimal var) {
        //the variables SQUARE_ROOT_PRECISION, DIVISION_PRECISION, and SQUARE_ROOT_PRECISION_TOLERANCE are constants that are set elsewhere, not included
        //in this part of the code.

        double initialguess = 0;

        BigDecimal result;

        /*checks the length of the number to squareroot. For some reason length 1 does not work unless a special case is used.
         *if even digits then the answer starts with sqrt of 10 and with digits / 2
         *if odd digits then the answer is (digits / 2 + 1)*/
        if(var.toString().length() / 2 == 0 && var.toString().length() != 1){
            //sqrt(10) * (length of string / 2) * 10
            initialguess = 3.1622776601683793319988935444327 * (((var.toString().length()) / 2) * 10 );
        } else if (var.toString().length() / 2 != 0){
            //((length of string / 2) + 1) * 10
            initialguess = ((var.toString().length() / 2) + 1) * 10;
        } else if (var.toString().length() == 1){
            //a simple arbitrary guess for a number less than 10.
            initialguess = 3;
        }

        //means x sub n plus 1.
        BigDecimal xsnp1 = BigDecimal.ZERO;

        //repeats the calculations SQUARE_ROOT_PRECISION times.
        for(int i = 1; i < SQUARE_ROOT_PRECISION; i++){
            //special case: the first "calculation" is simply the initial guess, will always be reached only once.
            if(xsnp1.equals(BigDecimal.ZERO)){
                xsnp1 = BigDecimal.valueOf(initialguess);
            //in any other case, do the calculations. See https://en.wikipedia.org/wiki/Methods_of_computing_square_roots#babylonian_method.
            }else{
                xsnp1 = (BigDecimal.valueOf(1).divide(BigDecimal.valueOf(2), DIVISION_PRECISION, BigDecimal.ROUND_HALF_UP))
                        .multiply(xsnp1.add((var.divide(xsnp1, DIVISION_PRECISION, BigDecimal.ROUND_HALF_UP))));
            }
        }

        result = xsnp1;

        //if the result and the real square root are too different, then rerun the calculations to improve accuracy.
        /*if the square of the result has a greater difference than 1 / SQUARE_ROOT_PRECISION_TOLERANCE,
          then rerun calculations to get a better result that is more accurate.*/
        if(var.subtract(result.pow(2)).abs()
                .compareTo((BigDecimal.ONE.divide(SQUARE_ROOT_PRECISION_TOLERANCE, DIVISION_PRECISION, BigDecimal.ROUND_HALF_UP))) == 1){
            //sets a while loop that is identical to the if statement.
            while (var.subtract(result.pow(2)).abs()
                    .compareTo((BigDecimal.ONE.divide(SQUARE_ROOT_PRECISION_TOLERANCE, DIVISION_PRECISION, BigDecimal.ROUND_HALF_UP))) == 1) {
                //emergency break for preventing an endless loop.
                int EmergencyBreak = 0;
                //tells the imprecision in the console.
                System.out.println("maximum precision tolerance exceeded, rerunning calculations. the current impresicion is"
                        + var.subtract(result.pow(2)).abs().subtract(SQUARE_ROOT_PRECISION_TOLERANCE) + ".");
                //reruns the calculations like above.
                for (int i = 1; i < SQUARE_ROOT_PRECISION; i++) {
                    result = (BigDecimal.valueOf(1).divide(BigDecimal.valueOf(2), DIVISION_PRECISION, BigDecimal.ROUND_HALF_UP))
                            .multiply(result.add((var.divide(result, DIVISION_PRECISION, BigDecimal.ROUND_HALF_UP))));
                }
                //adds 1 to the emergency break for every completed loop.
                EmergencyBreak = EmergencyBreak + 1;
                //breaks out of the while loop if it repeats 10 times.
                if(EmergencyBreak == 10){
                    System.out.println("maximum recalculations reached, aborting. This should be fixed.");
                    break;
                }
            }
        }

        return result;
    }


    //see https://en.wikipedia.org/wiki/Natural_logarithm#high_precision
    public static BigDecimal naturallog(BigDecimal var) {


    BigDecimal result = BigDecimal.ZERO;
        //ln must be greater than 0(not inclusive), else it is undefined.
        if (var.signum() > 0) {



        } else {
            //TODO
        }
        return result;
    }

    //see https://en.wikipedia.org/wiki/Arithmetic%E2%80%93geometric_mean
    public static BigDecimal ArithmeticGeometricMean(BigDecimal var1, BigDecimal var2){

        BigDecimal an = BigDecimal.ZERO;
        BigDecimal gn = BigDecimal.ZERO;
        BigDecimal result = BigDecimal.ZERO;
        int EmergencyBreak = 0;

        while(EmergencyBreak < 10) {

            //going to use sqrt precision, it works fine anyway.
            for (int i = 0; i < SQUARE_ROOT_PRECISION; i++) {
                if (an.compareTo(BigDecimal.ZERO) == 0 && gn.compareTo(BigDecimal.ZERO) == 0) {
                    an = var1.add(var2);
                    an = an.divide(BigDecimal.valueOf(2), DIVISION_PRECISION, BigDecimal.ROUND_HALF_EVEN);
                    gn = var1.multiply(var2);
                    gn = CalculatorOperations.squareroot(gn);
                } else {
                    an = an.add(gn);
                    an = an.divide(BigDecimal.valueOf(2), DIVISION_PRECISION, BigDecimal.ROUND_HALF_EVEN);
                    gn = an.multiply(gn);
                    gn = CalculatorOperations.squareroot(gn);
                }
            }

            if(an.subtract(gn).compareTo((BigDecimal.ONE.divide(SQUARE_ROOT_PRECISION_TOLERANCE, DIVISION_PRECISION, BigDecimal.ROUND_HALF_UP))) == -1){
                result = an;
                break;
            } else {
                EmergencyBreak = EmergencyBreak + 1;
            }
        }
        return result;
    }



    //this method for calculating a square root WOULD work, but an android system does not have enough memory!

    //uses newton's method of finding a square root. see https://en.wikipedia.org/wiki/Newton%27s_method#Examples for details
    // private BigDecimal sqrtNewtonsMethod(BigDecimal numbertosqrt, BigDecimal xValue, BigDecimal precision){
        /*for this explanation, 100 will be the number to sqrt.
        a sqrt can be found by solving x^2 = 100.
        the x value is unknown, and will have to be found by guessing and trial and error.
        this is the function to use: f(x) = x^2 - 100. the next line does this. */

        /*
        BigDecimal functionNumerator = xValue.pow(2).add(numbertosqrt.negate());
        //the derivative is equal to 2x. the next line does this.
        BigDecimal functionDenominator = xValue.multiply(BigDecimal.valueOf(2));
        //the first guess is found by subtracting x^2 - 100/ 2x from the initial guess. The next line does the division.
        BigDecimal NumeratorOverDenominator = functionNumerator.divide(functionDenominator, 2 * sqrtDigits.intValue(), RoundingMode.HALF_DOWN);
        //this line does the subtraction part.
        NumeratorOverDenominator = xValue.add(NumeratorOverDenominator.negate());

        //squares NumeratorOverDenominator, used in determining the (likely horrible!) precision error
        BigDecimal NumeratorOverDenominatorSquared = NumeratorOverDenominator.pow(2);
        //determines the precision error, this works because the square of a square root is always the original number.
        BigDecimal PrecisionError = NumeratorOverDenominatorSquared.subtract(numbertosqrt);
        //PrecisionError needs to be positive for the calculations to work.
        PrecisionError = PrecisionError.abs();
        if (PrecisionError.compareTo(precision) <= -1){
            return  NumeratorOverDenominator;
        }
        return sqrtNewtonsMethod(numbertosqrt, NumeratorOverDenominator, precision);
    }
*/

}
