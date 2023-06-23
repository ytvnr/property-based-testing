---
# try also 'default' to start simple
theme: seriph
colorSchema: dark
# apply any windi css classes to the current slide
class: 'text-center'
# https://sli.dev/custom/highlighters.html
highlighter: shiki
# show line numbers in code blocks
lineNumbers: false
fonts:
  sans: 'Robot'
  serif: 'Robot Slab'
# some information about the slides, markdown enabled
info: |
  ## Slidev Starter Template
  Presentation slides about Mutation Testing with PiTest

  Learn more at [Sli.dev](https://sli.dev)
# persist drawings in exports and build
drawings:
  persist: false
# page transition
transition: slide-left
# use UnoCSS
css: unocss
#background: ./assets/xmen.jpeg
---

# Introduction to Property-Based Testing

With JQwik

<div class="pt-12">
  <span @click="$slidev.nav.next" class="px-2 py-1 rounded cursor-pointer" hover="bg-white bg-opacity-10">
    Let's go <carbon:arrow-right class="inline"/>
  </span>
</div>

<div class="abs-br m-6 flex gap-2">
  <a href="https://github.com/ytvnr/property-based-testing" target="_blank" alt="GitHub"
    class="text-xl slidev-icon-btn opacity-50 !border-none !hover:text-white">
    <carbon-logo-github />
  </a>
</div>

<!--
The last comment block of each slide will be treated as slide notes. It will be visible and editable in Presenter Mode along with the slide. [Read more in the docs](https://sli.dev/guide/syntax.html#notes)
-->

---
transition: fade-out
---

# What is Property-Based Testing

PBT tries to combine the intuitiveness of Microtests with the effectiveness of randomized, generated test data.

Microtest is a small test that test small code.

Historically, PBT is "The thing that Quickcheck does". Quickcheck is a combinator library originally written in Haskell designed to generate test cases for tests suites. It has been adapted to other languages later.

In Quickcheck, we write assertions logical **properties** that function should fulfill. Then, Quickcheck tries to generate a test case that falsifies such assertions, one a case is found, quickcheck reduces to the minimal failing subset, enabling the developer to fix its function. 

<!--
A unit test tests a code unit (method, class, or small set of classes).
Micro-tests can be understood the same way by testing a very small portion of code.
A combinator library implements "combinators" for a functional language. It offers functions (a combinator) that combine functions together to make bigger functions.
-->

---
transition: fade-out
---

# Which library will we use ?

For this talk, we will use [Jqwik](https://jqwik.net/) which is an alternative test engine for the JUnit 5 platform that focuses on PBT.

A lot of libraries can be found for PBT.
For Java, you can use the following:
- JUnit-Quickcheck (integrates with JUnit 4)
- QuickTheories
- Vavr (a functional library that comes with a PBT module)
- jetCheck
- ScalaCheck
- KotlinTest
- And a lot of others

---
transition: fade-out
---

# Testing properties

_A property is supposed to describe a generic invariant or post condition of your code, given some precondition_

Let's play with the [Fizz Buzz Kata](http://codingdojo.org/kata/FizzBuzz/).

Iterating over numbers from 1 to 100,
- If the number is divisible by 3, then return "Fizz"
- If the number is divisible by 5, then return "Buzz"
- If the number is divisible by both 3 and 5, then return "FizzBuzz"
- Else, return the number

---
transition: fade-out
---

# Simple implementation of Fizz Buzz Kata

```java {all|6-9|all}
List<String> fizzBuzz() {
    return IntStream.range(1, 101).mapToObj((int i) -> {
      boolean divBy3 = i % 3 == 0;
      boolean divBy5 = i % 5 == 0;

        return divBy3 && divBy5 ? "FizzBuzz"
             : divBy3 ? "Fizz"
             : divBy5 ? "Buzz"
             : String.valueOf(i);
    }).collect(Collectors.toList());
}
```
<br>
<br>
<br>
<br>
> This function generates a list of a 100 elements matching the requirements of the Fizz Buzz Kata.

---
transition: fade-out
---

# A property from Fizz Buzz Kata

A property that can be extracted and tested from this function is: "Every multiple of 3 element starts with 'Fizz'".

First, we will create a _Precondition_ through `@Provide` of Jqwik library: "Consider number between 1 and 100 that are divisble by 3"

```java {all}
@Provide
Arbitrary<Integer> divisibleBy3() {
    return Arbitraries.integers().between(1, 100).filter(i -> i % 3 == 0);
}
```

Then, use it in your `@Property` and define the _PostCondition_: "The string returned by `fizzBuzz()` starts with `Fizz`" 

```java {all}
@Property
boolean every_third_element_starts_with_Fizz(@ForAll("divisibleBy3") int i) {
    return fizzBuzz().get(i - 1).startsWith("Fizz");
}
```

---
transition: slide-up
---

# Other properties to test

Now, we also have to test a number divisible by 5 will end with "Buzz" and a number divisible by both 3 and 5 will be equal to "FizzBuzz"

```java {all}
@Property
    boolean every_fifth_element_starts_with_Buzz(@ForAll("divisibleBy5") int i) {
        return fizzBuzz().get(i - 1).endsWith("Buzz");
    }

    @Property
    boolean every_third_and_fifth_element_starts_with_Buzz(@ForAll("divisibleBy3And5") int i) {
        return fizzBuzz().get(i - 1).equals("FizzBuzz");
    }

@Provide
    Arbitrary<Integer> divisibleBy5() {
       return Arbitraries.integers().between(1, 100).filter(i -> i % 5 == 0);
       }

@Provide
    Arbitrary<Integer> divisibleBy3And5() {
       return Arbitraries.integers().between(1, 100).filter(i -> i % 5 == 0 && i % 3 == 0);
       }
```
---
transition: slide-up
---

# What is Mutation Testing?

```java
int index = 0;
while(...) {
    ...;
    index++;
    if (index == 10) break;
}
```

becomes:

```java
int index = 0;
while(...) {
    ...;
    index++;
    if (index <= 10) break;
}
```

---
transition: slide-up
layout: image-right
image: ./assets/kill-em.jpeg
---

# Dead mutant are good mutants

Our goal is to kill all the mutants.

If a Unit Test fails, it means the code modification is detected: **the mutant is killed**.

Else, the mutant survived, meaning it would not be detected if the case occurs in the real world.

<br>

**Mutation killing report provides you the Test Strength** (killed mutants / all mutants for which there was test coverage)


---
transition: fade
layout: fact
---

# PiTest

A state-of-the-art mutation testing system for Java and the JVM

<div class="abs-br m-6 flex gap-2">
  <a href="http://pitest.org/" target="_blank" alt="PiTest.org"
    class="text-xl slidev-icon-btn opacity-50 !border-none !hover:text-white">
    <span> http://pitest.org/ </span>
    <carbon-book />
  </a>
  <a href="https://github.com/hcoles/pitest" target="_blank" alt="PiTest"
    class="text-xl slidev-icon-btn opacity-50 !border-none !hover:text-white">
    <span> https://github.com/hcoles/pitest </span>
    <carbon-logo-github />
  </a>
</div>

<!--
It's a mutation testing system that helps us to automate the mutation testing and reporting
-->

---
transition: slide-up
---

# Mutators

- ❓ **CONDITIONAL_BOUNDARIES** - replace relational operator `<, <=, >, >=`
- 📭 **EMPTY_RETURNS** - replaces return values with an ‘empty’ value for that type (e.g. empty strings, empty Optionals, zero for integers)
- ❌ **FALSE_RETURNS** - replaces primitive and boxed boolean return values with false
- ✅ **TRUE_RETURNS** - replaces primitive and boxed boolean return values with true
- 🕳️ **NULL_RETURNS** - replaces return values with null (unless annotated with NotNull or mutable by EMPTY_RETURNS)
- ➕ **INCREMENTS** - replaces increments (++) with decrements (--) and vice versa
- ➖ **INVERT_NEGS** - inverts negation of integer and floating point numbers (e.g. -1 to 1)
- 🧮 **MATH** - replaces binary arithmetic operations with another operation (e.g. + to -)
- ❗️ **NEGATIVE_CONDITIONALS** - negates conditionals (e.g. == to !=)
- 0️⃣ **PRIMITIVE_RETURNS** - replaces primitive return values with 0 (unless they already return zero)
- 🗑️ **VOID_METHOD_CALLS** - removes method calls to void methods

For the full list: https://pitest.org/quickstart/mutators/

<!--
Math: arithmetic operators, bitwise operators, shift operators 
-->

---
transition: slide-up
---

# Configuration

```xml
 <plugin>
  <groupId>org.pitest</groupId>
  <artifactId>pitest-maven</artifactId>
  <version>${pitest-parent.version}</version>
  <configuration>
    <targetClasses>
      <param>io.ytvnr.*</param>
    </targetClasses>
    <targetTests>
      <param>io.ytvnr.*</param>
    </targetTests>
  </configuration>
  <dependencies>
    <dependency>
      <groupId>org.pitest</groupId>
      <artifactId>pitest-junit5-plugin</artifactId>
      <version>${pitest-junit5-plugin.version}</version>
    </dependency>
  </dependencies>
</plugin>
```

For more details: https://pitest.org/quickstart/maven/

---
transition: slide-up
layout: image
image: ./assets/kill-the-mutants.jpeg
---



<!--
let's try to seek and destroy the mutants

Show the classes we have, and the tests.
Generate coverage with jacoco
Generate coverage with PIT
-->

---
transition: fade-out
---

# Interpreting the results

<img border="rounded" src="assets/pit-report.png">

---
transition: fade-out
---

# Interpreting the results

<img border="rounded" src="assets/range-validator-report.png">

---
transition: fade-out
---

# Mutant states

- 💗 **Survived** - The mutant survived the mutator. Test is not written well enough
- 💀 **Killed** - Congratulations! 🎉
- 👀 **No coverage** - Same as Survived, but there was no test exercising the mutated line of code
- 💥 **Non viable** - Mutation that could not be loaded by the JVM as the bytecode is invalid. (PIT tries to minimize it)
- ⌛️ **Timed out** - May happen if mutator changes the exit condition of loop, making it infinite
- 🧠 **Memory error** - Might occur if a mutation increases the amount of memory used by the system.
- 🏃 **Run error** - something went wrong when trying to test the mutation

<!--
For RangeValidatorTest

first, just add assertions and run `task mutate`
then, add case for 0 (false) and 100 (true)

For Palindrome

just add a case when it's not a palindrome
-->

---
class: px-20
---

# Pros and cons of Mutation Testing

| **Pros**                                                      | **Cons**                                                                                       |
|---------------------------------------------------------------|------------------------------------------------------------------------------------------------|
| Cover entire source code and detects not well-tested parts    | Expensive and time-consuming process because mutant programs should be generated independently |
| Detects high quality bugs, hard to find with usual testing    | Being complicated & long to perform, should be automated (thanks PiTest!)                      |
| Reveals hidden faults in code such as source code ambiguities | Can't be used for black-box testing (needs to change source code)                              |
| Customers get the most stable and reliable system             | Testers needs programming knowledge                                                            |
| Loopholes in test can be identified                           | Needs some tuning: mutate high value code (not pojos)                                          |


---
transition: fade
---

# Summary

- Mutation testing is time-consuming, it requires automation
- Mutation testing is the most comprehensive technique to test any program
- Mutation testing is a unit testing method
- It uses fault injection to generate mutants
- Other systems exists: µJava, Jester, Jumble, etc.

## To go further

- [Don't let your code dry](http://blog.pitest.org/dont-let-your-code-dry/)
- [PiTest PR setup](https://blog.pitest.org/pitest-pr-setup/)

---
layout: image
class: text-center align-middle
image: ./assets/victory.jpeg
---

[//]: # (I can do CSS, but it's so simple with <br> 😅)
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>

[https://github.com/ytvnr/mutation-testing](https://github.com/ytvnr/mutation-testing)
