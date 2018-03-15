/* Calculator activity; called when clicking on the calculator button on the startup screen.
 * This is done through a weird and probably badly programmed way: the value of each variable is reset upon every change in number.
 * An integer (varcurrentnumbers) is used to see how many numbers are currently on the calculator.
 * The var being set is replaced with another number that is affected by which button is pressed.
 * BigDecimal is used in order to have an infinite number of numbers on the display, disregarding memory limits.
 *
 * TODO: known bugs:
 * after calling equals, every value is set to zero.
 *
 * TODO: next time: work on
 *
 */

package inei.algebrasolver.calculator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import inei.algebrasolver.R;

public class CalculatorActivity extends AppCompatActivity implements View.OnClickListener {

    //Initializes the text that shows up for the calculator.
    TextView calculatortext;

    /* operator value; determines which operation is performed
     * 0 is undefined
     * 1 is add
     * 2 is subtract
     * 3 is multiply
     * 4 is divide
     */
    Integer operator = 0;


    //result on the calculator
    BigDecimal result = BigDecimal.ZERO;
    //first number on the calculator
    BigDecimal var1 = BigDecimal.ZERO;
    //second number on the calculator
    BigDecimal var2 = BigDecimal.ZERO;

    //memory values
    BigDecimal Memory = BigDecimal.ZERO;
    BigDecimal Memory1 = BigDecimal.ZERO;
    BigDecimal Memory2 = BigDecimal.ZERO;
    BigDecimal Memory3 = BigDecimal.ZERO;
    BigDecimal Memory4 = BigDecimal.ZERO;
    BigDecimal Memory5 = BigDecimal.ZERO;
    BigDecimal Memory6 = BigDecimal.ZERO;
    BigDecimal Memory7 = BigDecimal.ZERO;
    BigDecimal Memory8 = BigDecimal.ZERO;
    BigDecimal Memory9 = BigDecimal.ZERO;

    //Checks how many numbers are currently in var1, so that you can have more than one number in a var.
    Integer var1currentnumbers = 0;
    //Checks how many numbers are currently in var2, so that you can have more than one number in a var.
    Integer var2currentnumbers = 0;
    //Checks to see if var1 is already set, so that numbers will go to var2 instead.
    boolean var1set;
    //Checks to see if var2 is set, so that an equals operation can be performed.
    boolean var2set;
    //Checks to see if the decimal button has been pressed for var1.
    boolean var1decimal;
    //Checks to see if the decimal button has been pressed for var2.
    boolean var2decimal;
    //Checks to see how many numbers are after the decimal point for var1.
    Integer var1decimalnumbers;
    //Checks to see how many numbers are after the decimal point for var2.
    Integer var2decimalnumbers;
    //Checks to see if a calculation is completed
    Boolean CalculationCompleted;
    //
    BigDecimal var1tempmemory = BigDecimal.ZERO;
    //
    BigDecimal var2tempmemory = BigDecimal.ZERO;


    //numbers; initializes the number buttons
    Button b0;
    Button b1;
    Button b2;
    Button b3;
    Button b4;
    Button b5;
    Button b6;
    Button b7;
    Button b8;
    Button b9;

    //operators; initializes the operator buttons
    Button equals;
    Button clear;
    Button add;
    Button sub;
    Button div;
    Button mult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);


        var1decimal = false;
        var2decimal = false;

        //numbers; finds the buttons in the XML file and assigns them to the previously initialized variables
        b0 = (Button) findViewById(R.id.zero);
        b1 = (Button) findViewById(R.id.one);
        b2 = (Button) findViewById(R.id.two);
        b3 = (Button) findViewById(R.id.three);
        b4 = (Button) findViewById(R.id.four);
        b5 = (Button) findViewById(R.id.five);
        b6 = (Button) findViewById(R.id.six);
        b7 = (Button) findViewById(R.id.seven);
        b8 = (Button) findViewById(R.id.eight);
        b9 = (Button) findViewById(R.id.nine);

        //operators; finds the buttons in the XML file and assigns them to the previously initialized variables
        equals = (Button) findViewById(R.id.equals);
        clear = (Button) findViewById(R.id.clear);
        add = (Button) findViewById(R.id.add);
        sub = (Button) findViewById(R.id.sub);
        div = (Button) findViewById(R.id.divide);
        mult = (Button) findViewById(R.id.multiply);

        //finds the calculator TextView in the XML file and assigns them to the previously initialized variables
        calculatortext = (TextView) findViewById(R.id.calculatornumbers);
        //exponenttext = (TextView) findViewById(R.id.calculatorexponent);
    }

    //method used to cut down space in the onClick method
    //the pressedbutton variable is the number pressed on the calculator
    public void calculatorMethods(int pressedbutton) {
        boolean hasnumberbeenpressed = false;

        if (var1decimal && var1set && !var2set) {
            var1decimal = false;
            var2decimal = false;
        }

        if (var1decimal) {
            if (!var1set && var1decimalnumbers > 0 && var1.compareTo(BigDecimal.ZERO) >= 0) {
                System.out.println(var1decimalnumbers);
                //multiply the current number by 10 and add the pressed number for calculator-like input
                if (var1decimalnumbers == 4) {
                    var1 = var1.divide(BigDecimal.valueOf(1000), CalculatorOperations.DIVISION_PRECISION, BigDecimal.ROUND_HALF_EVEN);
                }
                //var1 = var1.multiply(BigDecimal.TEN);
                if (var1decimalnumbers == 4) {
                    var1 = var1.add((BigDecimal.valueOf(pressedbutton))
                            .divide(((BigDecimal.TEN).pow(var1decimalnumbers)), CalculatorOperations.DIVISION_PRECISION, BigDecimal.ROUND_HALF_EVEN));
                } else {
                    var1 = var1.add((BigDecimal.valueOf(pressedbutton))
                            .divide(((BigDecimal.TEN).pow(var1decimalnumbers)), CalculatorOperations.DIVISION_PRECISION, BigDecimal.ROUND_HALF_EVEN)
                            .multiply(BigDecimal.valueOf(1000)));
                }
                if (var1decimalnumbers == 4) {
                    var1 = var1.multiply(BigDecimal.valueOf(1000));
                }
                var1 = var1.stripTrailingZeros();
                calculatortext.setText(String.format(var1.toString(), 0));
                var1decimalnumbers = var1decimalnumbers + 1;
                hasnumberbeenpressed = true;
            }
            if (!var1set && !hasnumberbeenpressed && var1decimalnumbers == 0 && pressedbutton != 0 && var1.compareTo(BigDecimal.ZERO) >= 0) {
                var1 = var1.movePointRight(2);
                var1 = var1.multiply(BigDecimal.TEN);
                var1 = var1.add(BigDecimal.valueOf(pressedbutton)
                        .divide(BigDecimal.TEN, CalculatorOperations.DIVISION_PRECISION, BigDecimal.ROUND_HALF_EVEN));
                var1 = var1.stripTrailingZeros();
                var1decimalnumbers = 1;
                calculatortext.setText(String.format(var1.toString(), 0));
                hasnumberbeenpressed = true;
            }
            if (!var1set && !hasnumberbeenpressed && var1.compareTo(BigDecimal.ZERO) < 0) {
                var1 = var1.multiply(BigDecimal.TEN);
                var1 = var1.subtract(BigDecimal.valueOf(pressedbutton));
                calculatortext.setText(String.format(var1.toString(), 0));
                var1decimalnumbers = var1decimalnumbers + 1;
                hasnumberbeenpressed = true;
            }
        } else if (var2decimal) {
            if (!var2set && var2decimalnumbers > 0 && var2.compareTo(BigDecimal.ZERO) >= 0) {
                System.out.println(var2decimalnumbers);
                //multiply the current number by 10 and add the pressed number for calculator-like input
                if (var2decimalnumbers == 4) {
                    var2 = var2.divide(BigDecimal.valueOf(1000), CalculatorOperations.DIVISION_PRECISION, BigDecimal.ROUND_HALF_EVEN);
                }
                    //var1 = var1.multiply(BigDecimal.TEN);
                    if (var2decimalnumbers == 4) {
                        var2 = var2.add((BigDecimal.valueOf(pressedbutton))
                                .divide(((BigDecimal.TEN).pow(var2decimalnumbers)), CalculatorOperations.DIVISION_PRECISION, BigDecimal.ROUND_HALF_EVEN));
                    } else {
                        var2 = var2.add((BigDecimal.valueOf(pressedbutton))
                                .divide(((BigDecimal.TEN).pow(var2decimalnumbers)), CalculatorOperations.DIVISION_PRECISION, BigDecimal.ROUND_HALF_EVEN)
                                .multiply(BigDecimal.valueOf(1000)));
                    }
                    if (var2decimalnumbers == 4) {
                        var2 = var2.multiply(BigDecimal.valueOf(1000));
                    }
                    var2 = var2.stripTrailingZeros();
                    calculatortext.setText(String.format(var2.toString(), 0));
                    var2decimalnumbers = var2decimalnumbers + 1;
                    hasnumberbeenpressed = true;

                //unnecessary
              /*  if (!var2set && !hasnumberbeenpressed && var2decimalnumbers == 0 && pressedbutton != 0 && var2.compareTo(BigDecimal.ZERO) >= 0) {
                    var2 = var2.movePointRight(2);
                    var2 = var2.multiply(BigDecimal.TEN);
                    var2 = var2.add(BigDecimal.valueOf(pressedbutton)
                            .divide(BigDecimal.TEN, CalculatorOperations.DIVISION_PRECISION, BigDecimal.ROUND_HALF_EVEN));
                    var2 = var2.stripTrailingZeros();
                    var2decimalnumbers = 1;
                    calculatortext.setText(String.format(var2.toString(), 0));
                    hasnumberbeenpressed = true;
                }
                if (!var2set && !hasnumberbeenpressed && var2.compareTo(BigDecimal.ZERO) < 0) {
                    var2 = var2.multiply(BigDecimal.TEN);
                    var2 = var2.subtract(BigDecimal.valueOf(pressedbutton));
                    calculatortext.setText(String.format(var2.toString(), 0));
                    var2decimalnumbers = var2decimalnumbers + 1;
                    hasnumberbeenpressed = true;
                } */
            }
        } else {

            if (!var1set && var1currentnumbers > 0 && var1.compareTo(BigDecimal.ZERO) >= 0) {
                //multiply the current number by 10 and add the pressed number for calculator-like input
                var1 = var1.multiply(BigDecimal.valueOf(10));
                var1 = var1.add(BigDecimal.valueOf(pressedbutton));
                calculatortext.setText(String.format(var1.toString(), 0));
                var1currentnumbers = var1currentnumbers + 1;
                hasnumberbeenpressed = true;
            }
            if (!var1set && !hasnumberbeenpressed && var1currentnumbers == 0 && pressedbutton != 0 && var1.compareTo(BigDecimal.ZERO) >= 0) {
                var1 = var1.add(BigDecimal.valueOf(pressedbutton));
                var1currentnumbers = 1;
                calculatortext.setText(String.format(var1.toString(), 0));
                hasnumberbeenpressed = true;
            }
            if (!var1set && !hasnumberbeenpressed && var1.compareTo(BigDecimal.ZERO) < 0) {
                var1 = var1.multiply(BigDecimal.valueOf(10));
                var1 = var1.subtract(BigDecimal.valueOf(pressedbutton));
                calculatortext.setText(String.format(var1.toString(), 0));
                var1currentnumbers = var1currentnumbers + 1;
                hasnumberbeenpressed = true;
            }
            if (var1set && !var2set && !hasnumberbeenpressed && var2currentnumbers > 0 && var2.compareTo(BigDecimal.ZERO) >= 0) {
                //multiply the current number by 10 and add the pressed number for calculator-like input
                var2 = var2.multiply(BigDecimal.valueOf(10));
                var2 = var2.add(BigDecimal.valueOf(pressedbutton));
                calculatortext.setText(String.format(var2.toString(), 0));
                var2currentnumbers = var2currentnumbers + 1;
                hasnumberbeenpressed = true;
            }
            if (var1set && !var2set && !hasnumberbeenpressed && var2currentnumbers == 0 && pressedbutton != 0 && var2.compareTo(BigDecimal.ZERO) >= 0) {
                var2 = var2.add(BigDecimal.valueOf(pressedbutton));
                calculatortext.setText(String.format(var2.toString(), 0));
                var2currentnumbers = var2currentnumbers + 1;
                hasnumberbeenpressed = true;
            }
            if (var1set && !var2set && !hasnumberbeenpressed && var2.compareTo(BigDecimal.ZERO) < 0) {
                var2 = var2.multiply(BigDecimal.valueOf(10));
                var2 = var2.subtract(BigDecimal.valueOf(pressedbutton));
                calculatortext.setText(String.format(var2.toString(), 0));
                var1currentnumbers = var1currentnumbers + 1;
            }
        }
    }


    /* Called by all buttons on the calculator.
     */
    public void onClick(View view) {

        //Make the text smaller to avoid starting a new row. Will not work on different screen sizes!

        if (var1currentnumbers == 9 || var2currentnumbers == 9) {
            calculatortext.setTextSize(40);
        }
        if (var1currentnumbers == 13 || var2currentnumbers == 13) {
            calculatortext.setTextSize(20);
        }
        if (var1currentnumbers == 29 || var2currentnumbers == 29) {
            calculatortext.setTextSize(10);
        }

        if (result.intValue() > 0) {
            Clear();
        }




            switch (view.getId()) {

                case R.id.zero:
                    if (!var1set) {
                        calculatorMethods(0);
                        break;
                    }
                    //if var1 is set, then change var2
                    if (!var2set) {
                        calculatorMethods(0);
                        break;
                    }
                    break;
                case R.id.one:
                    if (!var1set) {
                        calculatorMethods(1);
                        break;
                    }
                    //if var1 is set, then change var2 - since var1set can only be true or false, no other conditions exist and there is no need to specify
                    if (!var2set) {
                        calculatorMethods(1);
                        break;
                    }
                    break;
                case R.id.two:
                    if (!var1set) {
                        calculatorMethods(2);
                        break;
                    }
                    //if var1 is set, then change var2 - since var1set can only be true or false, no other conditions exist and there is no need to specify
                    if (!var2set) {
                        calculatorMethods(2);
                        break;
                    }

                    break;
                case R.id.three:
                    if (!var1set) {
                        calculatorMethods(3);
                        break;
                    }
                    //if var1 is set, then change var2 - since var1set can only be true or false, no other conditions exist and there is no need to specify
                    if (!var2set) {
                        calculatorMethods(3);
                        break;
                    }
                    break;
                case R.id.four:
                    if (!var1set) {
                        calculatorMethods(4);
                        break;
                    }

                    //if var1 is set, then change var2 - since var1set can only be true or false, no other conditions exist and there is no need to specify
                    if (!var2set) {
                        calculatorMethods(4);
                        break;
                    }
                    break;
                case R.id.five:
                    if (!var1set) {
                        calculatorMethods(5);
                        break;
                    }

                    //if var1 is set, then change var2 - since var1set can only be true or false, no other conditions exist and there is no need to specify
                    if (!var2set) {
                        calculatorMethods(5);
                        break;
                    }
                    break;
                case R.id.six:
                    if (!var1set) {
                        calculatorMethods(6);
                        break;
                    }

                    //if var1 is set, then change var2 - since var1set can only be true or false, no other conditions exist and there is no need to specify
                    if (!var2set) {
                        calculatorMethods(6);
                        break;
                    }
                    break;
                case R.id.seven:
                    if (!var1set) {
                        calculatorMethods(7);
                        break;
                    }
                    //if var1 is set, then change var2 - since var1set can only be true or false, no other conditions exist and there is no need to specify
                    if (!var2set) {
                        calculatorMethods(7);
                        break;
                    }
                    break;
                case R.id.eight:
                    if (!var1set) {
                        calculatorMethods(8);
                        break;
                    }
                    //if var1 is set, then change var2 - since var1set can only be true or false, no other conditions exist and there is no need to specify
                    if (!var2set) {
                        calculatorMethods(8);
                    }
                    break;
                case R.id.nine:
                    if (!var1set) {
                        calculatorMethods(9);
                        break;
                    }

                    //if var1 is set, then change var2 - since var1set can only be true or false, no other conditions exist and there is no need to specify
                    if (!var2set) {
                        calculatorMethods(9);
                        break;
                    }

                    break;
                case R.id.plusminus:
                    Plusminus();
                    break;

                case R.id.equals:

                    CalculationCompleted = true;

                    EqualsWithTwoNumbers();

                    break;

                case R.id.clear:

                    Clear();

                    break;

                case R.id.add:
                    if (!var1set) {
                        operator = 1;
                        var1set = true;
                        calculatortext.setText(R.string.zero);
                        break;
                    }

                    break;
                case R.id.sub:
                    if (!var1set) {
                        operator = 2;
                        var1set = true;
                        calculatortext.setText(R.string.zero);
                        break;
                    }

                    break;
                case R.id.divide:
                    if (!var1set) {
                        operator = 3;
                        var1set = true;
                        calculatortext.setText(R.string.zero);
                        break;
                    }

                    break;
                case R.id.multiply:
                    if (!var1set) {
                        operator = 4;
                        var1set = true;
                        calculatortext.setText(R.string.zero);
                        break;
                    }

                    break;
                case R.id.exponent:
                    if (!var1set) {
                        operator = 5;
                        var1set = true;
                        calculatortext.setText(R.string.zero);
                        break;
                    }
                    break;
                case R.id.sqrt:
                    if (!var1set) {
                        operator = 6;
                        var1set = true;
                        Equals();
                        break;
                    }
                    break;
                case R.id.square:
                    if (!var1set) {
                        operator = 7;
                        var1set = true;
                        Equals();
                    }
                    break;
                case R.id.naturallog:
                    if (!var1set) {
                        operator = 8;
                        var1set = true;
                        Equals();
                    }
                    break;
                case R.id.decimalpoint:
                    if (!var1set) {
                        var1decimal = true;
                        var1decimalnumbers = 4;
                        break;
                    } else if (!var2set) {
                        var2decimal = true;
                        var2decimalnumbers = 4;
                        break;
                    }
                    break;
                case R.id.memoryplus:
                    if (!var1set) {
                        Memory = Memory.add(var1);
                        break;
                    } else if (!var2set) {
                        Memory = Memory.add(var2);
                        break;
                    }
                    break;
                case R.id.memoryminus:
                    if (!var1set) {
                        Memory = Memory.subtract(var1);
                        break;
                    } else if (!var2set) {
                        Memory = Memory.subtract(var2);
                        break;
                    }
                    break;
                case R.id.memoryoverwrite:
                    if (!var1set) {
                        Memory = var1;
                        break;
                    } else if (!var2set) {
                        Memory = var2;
                        break;
                    }
                    break;
                case R.id.memoryclear:
                    Memory = BigDecimal.ZERO;
                    break;

                case R.id.memoryrecall:
                    if (!var1set) {
                        var1 = Memory;
                        calculatortext.setText(String.format(var1.toString(), 0));
                        var1currentnumbers = var1.toString().length();
                    } else if (!var2set) {
                        var2 = Memory;
                        calculatortext.setText(String.format(var2.toString(), 0));
                        var2currentnumbers = var2.toString().length();
                    }
                case R.id.agm:
                    if (!var1set) {
                        operator = 9;
                        var1set = true;
                        calculatortext.setText(R.string.zero);
                    }
            }
    }


        @SuppressLint("SetTextI18n")
    public  void EqualsWithTwoNumbers() {



        String calctextchange = "0";

            if (operator == 0) {
                Toast.makeText(getApplicationContext(), R.string.no_operator, Toast.LENGTH_SHORT).show();
                calculatortext.setText(String.format(result.toString(), 0));
            } else if (operator == 1) {
                if(var1tempmemory.compareTo(BigDecimal.ZERO) != 0 && var2tempmemory.compareTo(BigDecimal.ZERO) != 0){
                    result = CalculatorOperations.add(var1, var2);
                    calculatortext.setText(String.format(result.toString(), 0));
                    var2set = true;
                } else{
                    result = CalculatorOperations.add(var1tempmemory, var2tempmemory);
                    calculatortext.setText(String.format(result.toString(), 0));
                    var2set = true;
                }

            } else if (operator == 2) {
                result = CalculatorOperations.subtract(var1, var2);
                calculatortext.setText(String.format(result.toString(), 0));
                var2set = true;
            } else if (operator == 3) {
                //if var2 is not 0
                if (var2.compareTo(BigDecimal.ZERO) != 0) {
                    result = CalculatorOperations.divide(var1, var2);
                    calculatortext.setText(String.format(result.toString(), 0));
                    var2set = true;
                } else if (var1.compareTo(BigDecimal.ZERO) == 0) {
                    result = BigDecimal.ZERO;
                } else {
                    Toast.makeText(getApplicationContext(), R.string.divide_by_zero, Toast.LENGTH_SHORT).show();
                }
            } else if (operator == 4) {

                result = CalculatorOperations.multiply(var1, var2);

                var2set = true;
                calculatortext.setText(String.format(result.toString(), 0));
            } else if (operator == 5) {
                if (var2.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) == 1) {
                    result = CalculatorOperations.exponent(var1, var2);
                    calculatortext.setText(String.format(result.toString(), 0));
                } else {
                    Toast.makeText(getApplicationContext(), R.string.power_too_high, Toast.LENGTH_SHORT).show();
                }
            }

            if (result.toString().length() >= 9 && result.toString().length() < 13) {
                calculatortext.setTextSize(40);
            }
            if (result.toString().length() >= 13 && result.toString().length() < 29) {
                calculatortext.setTextSize(20);
            }

            if (result.toString().length() >= 29) {
                BigDecimal TextChange = BigDecimal.valueOf(10).pow(29);
                if (result.compareTo(TextChange) == 1) {
                    calculatortext.setTextSize(40);
                    calctextchange = result.multiply(BigDecimal.TEN).toString().substring(0, 10);
                    calculatortext.setText(calctextchange + "E+" + String.valueOf((result.toString().length()) - 1));
                    System.out.println(calctextchange);
                } //else {
                // calculatortext.setTextSize(10);
                // }
            }

    }


    @SuppressLint("SetTextI18n")
    public void Equals(){


        String calctextchange = "0";




            if (var1 != null && var2 != null) {

                if (operator == 6) {



                    if (result.signum() == 1) {
                        result = CalculatorOperations.squareroot(result);
                        result = result.divide(BigDecimal.TEN, CalculatorOperations.DIVISION_PRECISION, BigDecimal.ROUND_HALF_EVEN).multiply(BigDecimal.TEN);
                        calculatortext.setText(String.format(result.toString(), 0));

                    } else if (!var2set && var1set) {
                        if (var1.signum() == 1) {
                            result = CalculatorOperations.squareroot(var1);
                            result = result.divide(BigDecimal.TEN, CalculatorOperations.DIVISION_PRECISION, BigDecimal.ROUND_HALF_EVEN).multiply(BigDecimal.TEN);
                            calculatortext.setText(String.format(result.toString(), 0));
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.negative_sqrt, Toast.LENGTH_LONG).show();

                        }
                    } else if (var2set && var1set) {
                        if (var2.signum() == 1) {
                            result = CalculatorOperations.squareroot(var2);
                            result = result.divide(BigDecimal.TEN, CalculatorOperations.DIVISION_PRECISION, BigDecimal.ROUND_HALF_EVEN).multiply(BigDecimal.TEN);
                            calculatortext.setText(String.format(result.toString(), 0));
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.negative_sqrt, Toast.LENGTH_LONG).show();
                        }
                    }
                } else if (operator == 7) {
                    if (!var2set && var1set) {
                        result = CalculatorOperations.square(var1);
                        calculatortext.setText(String.format(result.toString(), 0));
                    }
                } else if (operator == 8) {
                    if (!var2set && var1set) {
                        result = CalculatorOperations.naturallog(var1);
                        result = result.divide(BigDecimal.TEN, CalculatorOperations.DIVISION_PRECISION, BigDecimal.ROUND_HALF_EVEN).multiply(BigDecimal.TEN);
                        calculatortext.setText(String.format(result.toString(), 0));
                    }
                } else if (operator == 9) {
                    result = CalculatorOperations.ArithmeticGeometricMean(var1, var2);
                    calculatortext.setText(String.format(result.toString(), 0));
                }

                if (result.toString().length() >= 9 && result.toString().length() < 13) {
                    calculatortext.setTextSize(40);
                }
                if (result.toString().length() >= 13 && result.toString().length() < 29) {
                    calculatortext.setTextSize(20);
                }

                if (result.toString().length() >= 29) {
                    BigDecimal TextChange = BigDecimal.valueOf(10).pow(29);
                    if (result.compareTo(TextChange) == 1) {
                        calculatortext.setTextSize(40);
                        calctextchange = result.multiply(BigDecimal.TEN).toString().substring(0, 10);
                        calculatortext.setText(calctextchange + "E+" + String.valueOf((result.toString().length()) - 1));
                        System.out.println(calctextchange);
                    } //else {
                    // calculatortext.setTextSize(10);
                    // }
                }

                //calctextchange = result.toString();


                //exponenttext.setText(String.valueOf((result.toString().length()) - 1));
            } else {
                Toast.makeText(getApplicationContext(), R.string.no_numbers, Toast.LENGTH_SHORT).show();
            }

    }



    //resets all variables on the calculator. must be in this class.
    public void Clear() {
        var1 = BigDecimal.ZERO;
        var2 = BigDecimal.ZERO;
        result = BigDecimal.ZERO;
        var1currentnumbers = 0;
        var2currentnumbers = 0;
        var1set = false;
        var2set = false;
        var1decimalnumbers = 4;
        var2decimalnumbers = 4;
        var1decimal = false;
        var2decimal = false;
        var1tempmemory = BigDecimal.ZERO;
        var2tempmemory = BigDecimal.ZERO;
        calculatortext.setTextSize(60);
        calculatortext.setText(R.string.zero);
        CalculationCompleted = false;
    }

    //switches the sign. must be in this class
    public void Plusminus() {
        if (!var1set) {
            var1 = var1.negate();
            calculatortext.setText(String.format(var1.toString(), 0));
        }
        if (var1set) {
            var2 = var2.negate();
            calculatortext.setText(String.format(var2.toString(), 0));
        }
    }


}
