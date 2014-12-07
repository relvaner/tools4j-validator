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

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

import tools4j.validator.DoubleValidator;
import tools4j.validator.StringValidator;
import tools4j.validator.utils.ValidationDocumentFilter;
import tools4j.validator.utils.ValidationInputVerifier;
import tools4j.validator.utils.ValidationLabel;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.EtchedBorder;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class Testbed extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextField tfDocumentFilter;
	private JTextField tfInputVerifier;
	private JLabel lblViolationMessage;
	private JTextField tfDocumentFilter_Output;
	private JTextField tfInputVerifier_Output;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Testbed();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Testbed() {
		setBounds(100, 100, 479, 184);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		getContentPane().setLayout(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		tfDocumentFilter = new JTextField();
		tfDocumentFilter.setName("tfDocumentFilter");
		tfDocumentFilter.setBounds(10, 21, 86, 20);
		contentPane.add(tfDocumentFilter);
		tfDocumentFilter.setColumns(10);
		AbstractDocument doc = (AbstractDocument)tfDocumentFilter.getDocument();
		DoubleValidator documentFilterValidator = new DoubleValidator();
		doc.setDocumentFilter(new ValidationDocumentFilter(this, documentFilterValidator));
		
		tfInputVerifier = new JTextField();
		tfInputVerifier.setName("tfInputVerifier");
		tfInputVerifier.setBounds(10, 64, 86, 20);
		contentPane.add(tfInputVerifier);
		tfInputVerifier.setColumns(10);
		StringValidator inputVerifierValidator = new StringValidator();
		inputVerifierValidator.setPattern("[\\p{Alpha}]+");
		tfInputVerifier.setInputVerifier(new ValidationInputVerifier(this, inputVerifierValidator));
		
		JButton btnInputVerifier = new JButton("Okay");
		btnInputVerifier.setName("btnInputVerifier");
		btnInputVerifier.setBounds(117, 63, 89, 23);
		contentPane.add(btnInputVerifier);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(10, 104, 431, 30);
		contentPane.add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		lblViolationMessage = new ValidationLabel();
		lblViolationMessage.setName("lblViolationMessage");
		lblViolationMessage.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(lblViolationMessage);
		
		tfDocumentFilter_Output = new JTextField();
		tfDocumentFilter_Output.setName("tfDocumentFilter_Output");
		tfDocumentFilter_Output.setBounds(256, 21, 86, 20);
		contentPane.add(tfDocumentFilter_Output);
		tfDocumentFilter_Output.setColumns(10);
		doc = (AbstractDocument)tfDocumentFilter_Output.getDocument();
		doc.setDocumentFilter(new ValidationDocumentFilter(documentFilterValidator, tfDocumentFilter_Output, lblViolationMessage));
		tfDocumentFilter_Output.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
			}

			@Override
			public void focusLost(FocusEvent e) {
				lblViolationMessage.setText("");
			}
		});
		
		tfInputVerifier_Output = new JTextField();
		tfInputVerifier_Output.setName("tfInputVerifier_Output");
		tfInputVerifier_Output.setBounds(256, 64, 86, 20);
		contentPane.add(tfInputVerifier_Output);
		tfInputVerifier_Output.setColumns(10);
		tfInputVerifier_Output.setInputVerifier(new ValidationInputVerifier(inputVerifierValidator, lblViolationMessage));
		
		JButton btnInputVerifier_Output = new JButton("Okay");
		btnInputVerifier_Output.setName("btnInputVerifier_Output");
		btnInputVerifier_Output.setBounds(352, 63, 89, 23);
		contentPane.add(btnInputVerifier_Output);
		
		setVisible(true);
	}
}
