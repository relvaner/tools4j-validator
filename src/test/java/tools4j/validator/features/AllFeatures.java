package tools4j.validator.features;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	BasicFeature.class, 
	StringFeature.class,
	EmailFeature.class,
	PickListFeature.class,
	DateFeature.class,
	NumberFeature.class,
	LongFeature.class,
	IntegerFeature.class,
	ShortFeature.class,
	ByteFeature.class,
	FloatFeature.class,
	DoubleFeature.class,
	BooleanFeature.class,
	BigIntegerFeature.class,
	BigDecimalFeature.class,
	BusinessRulesFeature.class,
	DynamicValidationFeature.class
})
public class AllFeatures {

}
