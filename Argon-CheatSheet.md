# ARGON PROGLANG CHEATSHEET

---

## 1. Mutability
- variable mutability
- `inert` variables are constants
- `reactive` variables can be changed

## 2. Stdin
- standard in
- `input(<STRLIT>)`

## 3. Stdout
```ARGON
print(<STRLIT>) 
printf(<STRLIT>) 
println(<STRLIT>) 
printerr(<STRLIT>)
```
- standard out
- printerr for error outputs

## 4. If-then
- `filter` is the if statement
- `funnel` is the else statement
- example:
```ARGON
filter(<condition>) {
	// if condition
} funnel filter(<condition>) {
	// else if condition
	filter(<condition>) {
		// nested if
	}
} funnel {
	// else
}
```

## 5. Switch Case
- `when(variable)` is the keyword switch
-  case example `value 1 -> code`
- default case uses the `funnel` keyword
- case block:
```ARGON
when(var) {
	value1 -> code for case 1;
	value2 -> {
		code for case 2;
	}
	funnel -> {code for default case;}
}
```

## 6. Loop statements
#### While Loop
- While loops uses `ferment` keyword
```ARGON
ferment ( x>1 .and. x<10 ) {
	statement/s;
}
```
#### For Loop ***DEPRECATED***
- for loops uses `titrate` keyword and uses a range
- uses a range with the `from` and `to` keywords
- the variable used will increment/decrement within the range
- rate of change for variable determined by `step`
```ARGON
titrate (x from 1 to 5 step 2) {
	statement/s
}
```

#### Do-While Loop
- do-while loops uses `distill` keyword as `do`
- uses `until` keyword as `while`
```ARGON
distill {statement/s} until(x.is.12)
```

## 7. Catalyze and Decompose
- `compose` is functionally `continue`
	- skip to next iteration
- `decompose` is functionally `break`
	- exits the current scope (if statement, or loop)