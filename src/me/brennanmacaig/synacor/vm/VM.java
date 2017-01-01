package me.brennanmacaig.synacor.vm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

import me.brennanmacaig.synacor.vm.hardware.Register;
import me.brennanmacaig.synacor.vm.logging.Log;
import me.brennanmacaig.synacor.vm.logging.LogTypes;

/**
 * VM.java
 * Copyright (C) 2016 Brennan Macaig
 * Protected under the MIT License
 * 
 * Written as part of the Synacor OSCON challenge
 *
 * This is the main file to get run by the VM
 */

@SuppressWarnings("static-access")
public class VM {
	public static Log l;
	public static LogTypes lt;
	public static int offset = 0;
	public static ArrayList<Map<Integer, Byte>> memory;
	public static Map<String, String> map;
	public static Stack<Integer> stack;
	public static Map<Integer, Register> registers;
	public static boolean debug = true;
	
	public static void runVM() {
		int startoffset = offset;
	}
	public static void main(String[] args) {
		
		memory = new ArrayList<Map<Integer, Byte>>();
		map = new HashMap<String, String>();
		stack = new Stack<Integer>();
		registers = new HashMap<Integer, Register>();
		l = new Log();
		Log.init();
		l.log("Synacor VM Challenge | v1.0", "VM_HOST", lt.INFO);
		l.log("Created in 2016 by Brennan Macaig.", "VM_HOST", lt.INFO);
		populateMap();
		initRegisters();
		Commands.initCMD();
		String filepath = "challenge.bin";
		
		if(debug) {
			testMode();
		}
	}
	
	/**
	 * From a hardcoded map populates it with numbers and their corresponding functions.
	 */
	private static void populateMap() {
		//      KEY | VALUE
		l.log("Initializing Command Map...", "VM_HOST", LogTypes.INFO);
		map.put("0", "halt");
		map.put("1", "set");
		map.put("2", "push");
		map.put("3", "pop");
		map.put("4", "eq");
		map.put("5", "gt");
		map.put("6", "jmp");
		map.put("7", "jt");
		map.put("8", "jf");
		map.put("9", "add");
		map.put("10", "mult");
		map.put("11", "mod");
		map.put("12", "and");
		map.put("13", "or");
		map.put("14", "not");
		map.put("15", "rmem");
		map.put("16", "wmem");
		map.put("17", "call");
		map.put("18", "ret");
		map.put("19", "out");
		map.put("20", "in");
		map.put("21", "noop");
		l.log("Command Map Completed with 0 errors", "VM_HOST", LogTypes.GOOD);
	}
	
	private static void binRead(String filepath) throws FileNotFoundException {
		InputStream in = new FileInputStream(filepath);
		Map<Integer, Byte> map = new HashMap<Integer, Byte>();
		
		
	}
	
	private static void initRegisters() {
		l.log("Initializing Register Map...", "VM_HOST", LogTypes.INFO);
		Register zero = new Register(), 
				 one = new Register(), 
				 two = new Register(), 
				 three = new Register(), 
				 four = new Register(), 
				 five = new Register(), 
				 six = new Register(), 
				 seven = new Register();
		
		registers.put(0, zero);
		registers.put(1, one);
		registers.put(2, two);
		registers.put(3, three);
		registers.put(4, four);
		registers.put(5, five);
		registers.put(6, six);
		registers.put(7, seven);
		l.log("Register Map Completed with 0 errors", "VM_HOST", LogTypes.GOOD);
	}
	
	private static void testMode() {
		l.log("Debug ON. Running test mode.", "VM_HOST", LogTypes.INFO);
		registers.get(0).setValue(1);
		Commands.set(registers.get(0), registers.get(1));
		Commands.push(1);
		Commands.pop(registers.get(2));
		// print some stuff just to see what happens
		l.osOut("Register 0: " + registers.get(0).getLit() + "\n");
		
		exit(false);
	}
	
	/**
	 * Exits the VM. For use in a panic or normal OS exit.
	 */
	public static void exit(boolean panic) {
		
		if(panic == true) {
			l.log("VM recieved a panic exit command.", "VM_HOST", LogTypes.PANIC);
			l.log("Goodbye!", "VM_HOST", LogTypes.PANIC);
			Runtime.getRuntime().exit(1);
		} else {
			l.log("VM recieved exit command.", "VM_HOST", LogTypes.INFO);
			l.log("Goodbye!", "VM_HOST", LogTypes.INFO);
			Runtime.getRuntime().exit(0);
		}
	}
}
