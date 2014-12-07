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
package tools4j.validator.utils.features;

//import static org.junit.Assert.*;

import java.awt.event.KeyEvent;
import java.util.Locale;

import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InputVerifierFeature {
	
	protected FrameFixture testbed;

	@Before
	public void before() {
		Locale.setDefault(Locale.ENGLISH);
		
		testbed = new FrameFixture(new Testbed());
	}
	
	@After
	public void tearDown() {
		testbed.cleanUp();
	}
	
	@Test
	public void inputVerifier_valid() {
		testbed.textBox("tfInputVerifier").enterText("ABC");
		testbed.textBox("tfDocumentFilter").focus();
		testbed.textBox("tfDocumentFilter").requireFocused();
	}
	
	@Test
	public void inputVerifier_invalid() {
		testbed.textBox("tfInputVerifier").enterText("ABC4");
		testbed.button("btnInputVerifier").click();
		testbed.optionPane().pressKey(KeyEvent.VK_ENTER);
		testbed.textBox("tfInputVerifier").requireFocused(); // doesn't lose focus
	}
}
