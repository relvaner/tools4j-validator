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
