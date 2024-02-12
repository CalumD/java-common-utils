# java-common-utils

[![Build](https://github.com/CalumD/java-common-utils/actions/workflows/simple-tests.yml/badge.svg)](https://github.com/CalumD/java-common-utils/actions/workflows/simple-tests.yml)
[![JDK used](https://img.shields.io/badge/JDK--Used-21.28+85-green)](https://www.azul.com/downloads/?version=java-21-sts&package=jdk)
[![Junit](https://img.shields.io/badge/JUnit-v5-green)](https://junit.org/junit5/docs/current/user-guide/)
[![GitHub release](https://img.shields.io/github/v/release/CalumD/java-common-utils)](https://github.com/CalumD/java-common-utils/releases)
[![Maven release](https://maven-badges.herokuapp.com/maven-central/com.clumd.projects/java-common-utils/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.clumd.projects/java-common-utils)
[![CodeQL](https://github.com/CalumD/java-common-utils/actions/workflows/CodeQL.yml/badge.svg)](https://github.com/CalumD/java-common-utils/actions/workflows/CodeQL.yml)

A collection of common utilities I find myself writing for most Java projects

Best used with a simple Maven import

```xml
<dependency>
    <groupId>com.clumd.projects</groupId>
    <artifactId>java-common-utils</artifactId>
    <version>3.0.0</version>
</dependency>
```

### Arg Parser

This is a simple yet powerful Java CLI arg parser. It takes a collection of all possible arguments, and using the args
provided in the CLI, will do some intelligent parsing and validation, then return you the subset of your Arguments which
was input to your program along with any associated values.

It also has the functionality to auto-generate the standard CLI boilerplate help text (check out the test
arg_parser/JavaArgParserTest.java#test_getting_boiler_plate_after_set_equals for an example of this), using typical
MAN-page formatting.

##### Usage

The easiest way to create an Argument is with the Lombok Builder, something along the lines of the following (note that
this is a builder, you dont need to use every option every time if the default suffices).

```java
Argument<Integer> anIntegerArgument = Argument.<Integer>builder()
        .uniqueId("my number arg")
        .description("A number between 10 and 30")
        .shortOptions(Set.of('n'))
        .longOptions(Set.of("number", "input"))
        .mustBeUsedWith(Set.of("some other arg"))
        .mustNotBeUsedWith(Set.of("some mutually exclusive arg"))
        .isMandatory(true)
        .hasValue(true)
        .valueIsOptional(false)
        .shouldShortCircuit(false)
        .conversionFunction(Integer::parseInt)
        .validationFunction(i -> i > 10 && i < 30)
        .defaultValue(18)
        .build();
```

This will give you an argument which can be referenced on the CLI with any of:
`-n 11`, `-n=16`, `-n="28"`, `--number 11`, `--number=16`, `--input="28"`.

The conversion function is required as all arguments passed into the main method of your Java program will be a String,
so you need a suitable conversion from String into the type you'd like.

If you used `-n 4` on the CLI, you would get an exception
like `java.text.ParseException: Argument value failed validation. Check argument {my number arg} documentation: {A number between 10 and 30}`,
so a well written description can be very helpful.

You can collect the values of your returned Argument after parsing with `anIntegerArgument.getArgumentResult();` which
will return you the type-safe parsed version of your result. In this case it is an `Integer`.

The ideal pattern for implementation is to define a single ```static final List<Argument<?>> arguments;``` followed by a
bunch of ```static final Argument<[type]> [variable name];```s at the top of your class. Then have a ```static {}```
block where you initialise each of your ```[variable name]```s, and the list with a ```List.of([variable names])```.
Then have a method which calls the ```parseFromCLI``` followed by a switch based n the argumentID of the variables
remaining.
In each of the cases in this switch, you can then reference the variable name of the known Argument, to give you the
type-safety guarantees and avoid unnecessary casting from Objects. Obviously you can skip this complexity if all your
Arguments are of the same type, then you can just make the list of arguments typed correctly instead of a ```?```.

### Base Enhancements

This is for all instances of base java, where I see opportunity for enhanced functionality or use.

An example of this is the FunctionPotentialException class, which allows users to implement lambdas which may throw
checked exceptions, so long as it is wrapped by a method calling down to the "apply" method.

Or an extension of the regular Exception and RuntimeException to allow for a cleaner way to print a stacktrace (
basically avoiding the lines of code, and only keeping the exception names and their messages.)

### Files

Who hasn't written their own comfort lib for parsing files yet?

This has helper methods to get the contents of filesystem files and application resource files in various String
representations, along with ways of creating and deleting files and folders.

### Models

Why import the whole of Apache Commons, when I need the basic functionality from usually around 1% of it?
