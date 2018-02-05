/*
 * tools4j-validator - Framework for Validation
 * Copyright (c) 2014, David A. Bauer
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package tools4j.validator;

import java.io.StringReader;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

public class ValidatorFactory {
	public static Validator<?> createValidator(String name) {
		Validator<?> result = null;
		
		if (name!=null)
			switch (name.toUpperCase().replace("VALIDATOR", "")) {
				case "STRING":  	result = new StringValidator(); break;
				case "BOOLEAN": 	result = new BooleanValidator(); break;
				case "BYTE": 		result = new ByteValidator(); break;
				case "SHORT": 		result = new ShortValidator(); break;
				case "INTEGER": 	result = new IntegerValidator(); break;
				case "LONG": 		result = new LongValidator(); break;
				case "FLOAT":		result = new FloatValidator(); break;
				case "DOUBLE": 		result = new DoubleValidator(); break;
				case "BIGINTEGER": 	result = new BigIntegerValidator(); break;
				case "BIGDECIMAL": 	result = new BigDecimalValidator(); break;
				case "DATE": 		result = new DateValidator(); break;
				case "EMAIL": 		result = new EmailValidator(); break;	
				case "PICKLIST": 	result = new PickListValidator(); break;
			}
		
		return result;
	}
	
	public static String parseType(String json) {
		JsonParser parser = Json.createParser(new StringReader(json));
		while (parser.hasNext()) {
			 Event e = parser.next();
			 if (e == Event.KEY_NAME)
				 if (parser.getString().equalsIgnoreCase("type")) {
					 parser.next();
					 return parser.getString();
				 }
		}
		parser.close();
		
		return null;
	}
}
