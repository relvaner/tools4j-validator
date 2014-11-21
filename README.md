Introduction
============

The framework is used for checking of data for correctness. Specifically data can be checked for correctness by means of regular expression and the value range or field length for strings. Additionally own validators can be written that can then define additional verifiable criteria (Derived classes of type Validator <T>). Validation is a maximum of three steps (Transformation → Validation → BusinessRule). When passing a string, the string is first transformed to the desired data type. During the transformation process decimal delimeters will transferred to a point notation (comma → point). Then, the actual validation (pattern, value range, length range) is performed. At the conclusion rules can be checked for compliance. In Table 1 is a list of the various validators and its constraints to see.

Overview
========

| Validator | Constraints |
| --------- | ----------- |
| StringValidator     | NotNull, Null, NotEmpty, Empty, Pattern, Min, Max, Size |
| EmailValidator      | NotNull, Null, NotEmpty, Empty, Pattern, Min, Max, Size |
| PickListValidator   | NotNull, Null, NotEmpty, Empty, Pattern, Min, Max, Size, List |
| DateValidator       | NotNull, Null, Pattern, Valid, Past, Future |
| BigDecimalValidator | NotNull, Null, Min, Max, Range |
| BigIntegerValidator | NotNull, Null, Min, Max, Range |
| DoubleValidator     | NotNull, Null, Min, Max, Range |
| FloatValidator      | NotNull, Null, Min, Max, Range |
| LongValidator       | NotNull, Null, Min, Max, Range |
| IntegerValidator    | NotNull, Null, Min, Max, Range |
| ShortValidator      | NotNull, Null, Min, Max, Range |
| ByteValidator       | NotNull, Null, Min, Max, Range |
| BooleanValidator    | NotNull, Null, AssertFalse, AssertTrue |

Table 1: Overview of validators and possible constraints.

Examples
========
<b>Example of a StringValidator:</b>

Validation with a pattern, followed by validation and output an error message and the type of error message.
<pre><code>StringValidator v = new StringValidator();
v.setPattern("[A-Za-z]+");

v.validate("122");

System.out.println(v.getViolationMessage());

The input value "122" is not allowed ("[A-Za-z]+" expected)!

System.out.println(v.getViolationConstraint());

Pattern</code></pre>
<b>Example of a IntegerValidator:</b>

Validation with specifying a minimum, followed by validation and output an error message.
<pre><code>IntegerValidator v = new IntegerValidator();
v.setMin(25);

v.validate(23);

System.out.println(v.getViolationMessage());

The input value "23" is out of range (>=25)!</code></pre>

Language Support for the output of error messages (currently German and English):

<pre><code>Validator.setViolationMessageLanguage("de");</code></pre>

Dynamic Validation
==================
A dynamic validation is made possible by the coding of validation properties in JSON format. Definition of validation properties to an integer field in JSON:

<pre><code>{
    "type": "Integer",
    "constraints": [
        {"constraint": "NotNull"},
        {"constraint": "Range", "min": 2, "max": 10}
                   ]
}</code></pre>

<b>Example:</b>

Given are the constraints and the value which has to be checked. Using the ValidatorFactory the type of validator is determined. Then the corresponding validator is instantiated and then the validation is performed.

<pre><code>String constraints = "{ … }";
String type = ValidatorFactory.parseType(constraints);

Validator<?> v = ValidatorFactory.createValidator(type);
v.setConstraints(constraints);

v.validateString(value);</code></pre>

Validation properties in JSON syntax obtained by:

<pre><code>v.getConstraintsAsJsonObject().toString();</code></pre>
