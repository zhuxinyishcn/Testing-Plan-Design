# CSCE -361 -  Blackbox Testing - Individual
Name as it appears on Canvas: __Xinyi Zhu__  
GitLab User ID: __1584__  
### `Calculator.calculate()` to compute answer
1. Start up
	a. The function takes two arguments, an integer, `type` (which may be interpreted as a character) and a character array, `token`, which must have a `\0` character after the last printable character.
	b. The function returns a string; however, it also has side-effects. It will update the stack backing the postfix calculator, and it may output to `stdout`.
2. Action on input
a. The expected input to the calculator is a valid (well-formed) postfix expression containing integers, real numbers, and the mathematical operators `+`, `-`, `*`, `/`, `=`, separated by whitespace.
b. Integers and real numbers are limited to 20 characters; entry of numbers exceeding this size results in an error message.
c. Entry of any character other than `1`, `2`, `3`, `4`, `5`, `6`, `7`, `8`, `9`, `0`, `.`, `+`, `-`, `*`, `/`, `=`, `c`, `q` results in an error message.
d. The size of the stack is limited to 100 elements. Attempts to exceed the maximum stack size results in the display of an error message.

###  `Calculator.calculate()` Test Requirement

1.  If  `type`  is the declared constant  `NUMBER`  then  `token`  will be treated as the textual representation of an integer or fixed-point real number.  `token`  will be parsed to determine the number’s value, and this number will be pushed onto the stack. The function will return  `null`.

2.  If  `type`  is the declared constant  `OPERAND_TOO_LONG`  then the function will return the first 20 characters of  `token`followed by “… is too long”.
    
3.  If  `type`  is the character  `=`  then the function will return a tab character followed by a textual representation of the value at the top of the stack; the stack will remain unchanged.
    
4.  If  `type`  is the character  `+`,  `-`,  `*`, or  `/`  then the function will pop a value off the stack
    1.  If the value is  `0.0`  and  `type`  is  `/`  then the function will return “zero divisor popped”.
    2.  Otherwise, the function will pop a second value off the stack and apply the appropriate arithmetic operation. The second value popped off the stack will be treated as the first operand, and the first value popped off the stack will be treated as the second operand. The result of the operation will pushed onto the stack. The function will return  `null`.
5.  If  `type`  is the character  `c`  then the function will pop the top value off the stack and return  `null`.
    
6.  If  `type`  is any other value then the function will return “unknown command” followed by  `type`  (interpreted as a character).
    
7.  If the function attempts to pop a value off the stack when there are no values on the stack, the function will output “error: stack empty”, place the value 0 on the stack, and return the tab character followed by  `0.0`.
    
8.  The stack is limited to 100 values. If the function attempts to push a value onto a full stack then the function will output “error: stack full” and return  `null`.
### Test 1
### Equivalence partitions: Integer, Double and Char (not acceptable)
### PURPOSE

Test invocation of `Calculator.calculate()`, If `type` is the declared constant `NUMBER` then `token` will be treated as the textual representation of an integer or fixed-point real number. `token` will be parsed to determine the number’s value, and this number will be pushed onto the stack
### SETUP

None needed

### INPUTS and EXPECTED OUTPUTS
| input |expected output |
| :-- |:---------------:| 
| `Calculator.NUMBER`, `4` |`null`    |  
| `Calculator.NUMBER`, `12.23` |`null`    |  
| `Calculator.NUMBER`, `not a number` |`NumberFormatException`| 
### TESTING REQUIREMENTS COVERED
1
### Test 2
### Equivalence partitions: None
### PURPOSE

Test invocation of `Calculator.calculate()`, If `type` is the declared constant `OPERAND_TOO_LONG` then the function will return the first 20 characters of `token` followed by “… is too long”.
### SETUP

None needed

### INPUTS and EXPECTED OUTPUTS
| input |expected output |
| :-- |:---------------:| 
| `Calculator.OPERAND_TOO_LONG`, `a random 20+ long number` |`the random 20+ long number... is too long`|  

### TESTING REQUIREMENTS COVERED
1,2
### Test 3
### Equivalence partitions: Integer, Double, Char (not acceptable)
### PURPOSE

Test invocation of `Calculator.calculate()`,if `type` is the character `=` then the function will return a tab character followed by a textual representation of the value at the top of the stack; the stack will remain unchanged.
### SETUP

None needed

### INPUTS and EXPECTED OUTPUTS
| input |expected output |
| :-- |:---------------:| 
| `2` ,`=` |`2`|  
| `21.35` ,`=` |`21.35`|  
| `not a number` ,`=` |`NumberFormatException`|  

### TESTING REQUIREMENTS COVERED
1,3
### Test 4
### Equivalence partitions: Integer, Double and Char(not acceptable), 
### Equivalence partitions: Divide non-zero number, Divide by Zero
### PURPOSE

Test invocation of `Calculator.calculate()`, test If `type` is the character `+`, `-`, `*`, or `/` then the function will pop a value off the stack
### SETUP

None needed

### INPUTS and EXPECTED OUTPUTS
| input |expected output |
| :-- |:---------------:| 
| `+`, `8 2` |`10`|  
| `-`, `8 2` |`6`|  
| `+`, `8 2` |`10`|  
| `*`, `8 2` |`16`|  
| `/`, `8 2` |`4`|  
| `/`, `2 0` |`zero divisor popped`|  
| `+`, `82.45  22.03` |`104.48`|  
| `-`, `82.45  22.03` |`60.42`| 
| `*`, `82.45  22.03` |`1816.3735`| 
| `/`, `82.45  22.03` |`3.742623694961416`| 
| `/`, `923.76 0` |`zero divisor popped`|  


### TESTING REQUIREMENTS COVERED
1, 4

### Test 5
### Equivalence partitions: None
### PURPOSE

Test invocation of `Calculator.calculate()`, test If type is the character `c` then the function will pop the top value off the stack and return null.
### SETUP

None needed

### INPUTS and EXPECTED OUTPUTS
| input |expected output |
| :-- |:---------------:| 
| `c`, `random number array` |`null`    |  

### TESTING REQUIREMENTS COVERED
5
### Test 6
### Equivalence partitions: None
### PURPOSE

Test invocation of `Calculator.calculate()`, test If type is any other value then the function will return “unknown command” followed by type
### SETUP

None needed

### INPUTS and EXPECTED OUTPUTS
| input |expected output |
| :-- |:---------------:| 
| `x`, `empty char array` |`unknown command x`     |  

### TESTING REQUIREMENTS COVERED
6
### Test 7
### Equivalence partitions: None
### PURPOSE

Test invocation of `Calculator.calculate()`, test stack is empty then the program prints an error message.
### SETUP

None needed

### INPUTS and EXPECTED OUTPUTS
| input |expected output |
| :-- |:---------------:| 
| `=`, `empty char array` | `error: stack empty`     |  

### TESTING REQUIREMENTS COVERED

7



### Test 8
### Equivalence partitions: Integer,Double, Char(not acceptable)
### PURPOSE

Test invocation of `Calculator.calculate()`,and  operation of `Calculator.calculate()`on an integer or double in the unexpected range.

### SETUP

None needed

### INPUTS and EXPECTED OUTPUTS
| input |expected output |
| :-- |:---------------:| 
| 100+  integer  | `error: stack full`     |  
| 100+  double| `error: stack full`     |  
| 100+  char| `NumberFormatException`    | 
### TESTING REQUIREMENTS COVERED
1, 8

# CSCE -361 -  Whitebox Testing - Pair
Name as it appears on Canvas: __Xinyi Zhu,__ __Cody Berglund__  
GitLab User ID: __1584, 1603__ 
## `WhiteBox`  Test Requirement
`100%` statement coverage on this calculator class
### Test 9
### PURPOSE

Test invocation of the `calculate` class,  test whether the loop terminates when the user types "q"
### SETUP

None needed

### INPUTS and EXPECTED OUTPUTS
| input |expected output |
| :-- |:---------------:| 
| `q` | The program terminates   |  
### Test 10
### PURPOSE

Test invocation of the `calculate` class,  test getToken function can correctly notice the whitespace, tab, and line change
### SETUP

None needed

### INPUTS and EXPECTED OUTPUTS
| input |expected output |
| :-- |:---------------:| 
| `23\t89\t+\t=\tq` | The program correctly accept the inputs| 
| `23\n89\n+\n=\nq23\n89\n+\n=\nq` | The program correctly accept the inputs   | 
| `23 89 + = q` | The program correctly accept the inputs   | 
### Test 11
### PURPOSE

Test invocation of the `calculate` class, getToken function can correctly notice the token is too long for integer and double number
### SETUP

None needed

### INPUTS and EXPECTED OUTPUTS
| input |expected output |
| :-- |:---------------:| 
| `8753984759834743789724 2 * = \n q` | 8753984759834743789... is too long | 
| `1.12875398475983474324 2 * = \n q` | 1.12875398475983474... is too long   
### Test 12
### PURPOSE

Test invocation of the `calculate` class, test getToken function can correctly parsing a 20+ long number which end with `.`,  and test getToken function can correctly parsing a incorrect that a double number has two `.`
### SETUP

None needed

### INPUTS and EXPECTED OUTPUTS
| input |expected output |
| :-- |:---------------:| 
| `1128753984759847430322. 2 * = q` | `None` | 
| `1.243.3 2 + = \n q` |    `None`
### Test 13
### PURPOSE

Test invocation of the `main` class, test if the main function correctly executes when called 
### SETUP

None needed

### INPUTS and EXPECTED OUTPUTS
| input |expected output |
| :-- |:---------------:| 
| `None` | `None` | 

### Test 14
### PURPOSE

Test invocation of the `calculate` class,  test if getToken function can correctly parse an incorrect string that contains integers or double and characters 
### SETUP

None needed

### INPUTS and EXPECTED OUTPUTS
| input |expected output |
| :-- |:---------------:| 
| `123c4567890 2 + = q` | `None` | 
| `12.3c4567890 2 + = q` | `None` | 
