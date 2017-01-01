package me.brennanmacaig.synacor.vm;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import me.brennanmacaig.synacor.vm.hardware.Register;
import me.brennanmacaig.synacor.vm.logging.Log;
import me.brennanmacaig.synacor.vm.logging.LogTypes;

@SuppressWarnings("static-access")
public class Commands {
	
	public static Log l;
	public static LogTypes lt;
	public static VM vm;
	private static int bignum = 32768;
	/**
	 * Initialize commands for use.
	 */
	public static void initCMD() {
		l = new Log();
		vm = new VM();
		l.log("Commands Initialized Successfully. Ready for use.", "OS-CMD", lt.GOOD);
	}
	
	/**
	 * System halt - kill VM
	 */
	public static void halt() {
		l.osOut("Halt command issued.");
		l.log("System halt received! Quitting VM...", "VM-HOST", lt.PANIC);
	}
	
	/**
	 * Set the value of register A to the value of register B
	 */
	public static void set(Register a, Register b) {
		a.setValue(b.getValue());
	}
	
	/**
	 * Push value a into the stack
	 */
	public static void push(int a) {
		vm.stack.push(a);
	}
	
	/**
	 * Remove the top element from the stack and push it to a
	 */
	public static void pop(Register a) {
		if(vm.stack.empty()) {
			l.log("No elements to pop from stack!", "CMD_POP", lt.ERROR);
			l.log("Forcing system halt -- panic", "CMD_POP", lt.ERROR);
			vm.exit(true);
		} else {
			a.setValue(vm.stack.pop());			
		}
	}
	
	/**
	 * If B and C are equal set register A to be 1, otherwise set A to be 0
	 * @param a - register to output result to
	 * @param b - test register one
	 * @param c - test register two
	 */
	public static void eq(Register a, Register b, Register c) {
		if(b.getLit() == c.getLit()) {
			a.setValue(1);
		} else {
			a.setValue(0);
		}
	}
	
	/**
	 * If B is greater than C then set A to be 1, otherwise set A to be 0
	 * @param a - register to output result to
	 * @param b - test register one
	 * @param c - test register two
	 */
	public static void gt(Register a, Register b, Register c) {
		if(b.getLit() > c.getLit()) {
			a.setValue(1);
		} else {
			a.setValue(0);
		}
	}
	
	/**
	 * Jump to block A of memory
	 */
	public static void jmp(int a) {
		if(a < vm.memory.size()) {
			vm.offset = a;
		} else {
			l.log("Instruction is outside of range!", "JMP-CMD", lt.ERROR);
			l.log("Forcing a system halt -- panic", "JMP-CMD", lt.ERROR);
			vm.exit(true);
		}
	}
	
	/**
	 * If register A is nonzero jump to instruction B
	 */
	public static void jt(Register a, int b) {
		if(a.getLit() != 0) {
			if(b < vm.memory.size()) {
				vm.offset = b;
			} else {
				l.log("Instruction is outside of range!", "JT-CMD", lt.ERROR);
				l.log("Forcing a system halt -- panic", "JT-CMD", lt.ERROR);
				vm.exit(true);
			}
		}
	}
	
	/**
	 * If register A is zero jump to instruction B
	 */
	public static void jf(Register a, int b) {
		if(a.getLit() == 0) {
			if(b < vm.memory.size()) {
				vm.offset = b;
			} else {
				l.log("Instruction is outside of range!", "JF-CMD", lt.ERROR);
				l.log("Forcing a system halt -- panic", "JF-CMD", lt.ERROR);
				vm.exit(true);
			}
		}
	}
	
	/**
	 * Store into A the value of B + C (% 32768)
	 */
	public static void add(Register a, Register b, Register c) {
		a.setValue((b.getLit() + c.getLit()) % bignum); 
	}
	
	/**
	 * Store into A the value of B*C (% 32768)
	 */
	public static void mult(Register a, Register b, Register c) {
		a.setValue((b.getLit() + c.getLit()) % bignum);
	}
	
	/**
	 * Store into A the remainder of B%C
	 */
	public static void mod(Register a, Register b, Register c) {
		a.setValue(b.getLit()%c.getLit());
	}
	
	/**
	 * Stores into A the bitwise AND of B and C
	 */
	public static void and(Register a, Register b, Register c) {
		a.setValue(b.getLit() & c.getLit());
	}
	
	/**
	 * Stores into A the bitwise OR of B and C
	 */
	public static void or(Register a, Register b, Register c) {
		a.setValue(b.getLit() | c.getLit());
	}
	
	/**
	 * Stores into 15-bit inverse of B into A
	 */
	public static void not(Register a, Register b) {
		a.setValue(~b.getLit() & ((1 << 15) - 1));
	}
	
	/**
	 * Read memory at address B and store it to A
	 */
	public static void rmem(Register a, Register b) {
		Map<Integer, Byte> map = vm.memory.get(b.getLit());
		int val = ((map.get(0) & 0xff << 8) | (map.get(1) & 0xff));
		a.setValue(val);
	}
	
	/**
	 * Write B to memory at address A
	 */
	public static void wmem(Register a, Register b) {
		byte x = (byte) (b.getLit() & 0xFF);
		byte y = (byte) ((b.getLit() >> 8) & 0xFF);
		Map<Integer, Byte> map = new HashMap<Integer, Byte>();
		map.put(0, x);
		map.put(1, y);
		vm.memory.set(a.getLit(), map);
	}
	
	/**
	 * Push the address of the next instruction to the stack, and then jump to A
	 */
	public static void call(Register a) {
		push(vm.offset);
		jmp(a.getLit());
	}
	
	/**
	 * Remove the top element from the stack and jump to it. Empty stack == halt
	 */
	public static void ret() {
		int a = 0;
		try {
			a = vm.stack.pop();
		} catch (Exception e) {
			halt();
		}
		jmp(a);
	}
	
	/**
	 * Write the character represented by the ASCII code from register A to the terminal.
	 */
	public static void out(Register a) {
		char c = (char) a.getLit();
		String st = Character.toString(c);
		l.osOut(st);
	}
	
	/**
	 * Read in from keyboard until a new line character is found, and set the char value to a
	 */
	public static void in(Register a) {
		Scanner sc = new Scanner(System.in);
		String s = sc.nextLine();
		char c = 0;
		for(int i = 0; i < s.length(); i++) {
			c = s.charAt(i);
		}
		a.setValue((int) c);
	}
	
	/**
	 * Looks exactly like what it is. No operation here.
	 */
	public static void noop() {}
	
}
