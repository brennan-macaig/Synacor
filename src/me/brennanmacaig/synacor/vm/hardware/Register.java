package me.brennanmacaig.synacor.vm.hardware;

/**
 * Register.java
 *
 * This is a class to create a generic register object to use for math and such.
 * It has getters and setters. Pretty straight forward.
 */
public class Register {

	private static int value = 0; // All registers start at 0
	
	/**
	 * Get the value in this register.
	 * @return integer value
	 */
	public static int getValue() {
		return value;
	}
	
	/**
	 * Set the value in this register
	 * @param newVal - new value to assign
	 */
	public static void setValue(int newVal) {
		value = newVal;
	}
	
	/**
	 * Get the literal of this register.
	 * <p>Numbers 0 through 32767 mean the literal value.
	 * <p>Numbers 32768 through 32775 actually mean registers 0-7
	 * <p>numbers 32776 through 65535 are invalid altogether. 
	 * @return the literal value of this register.
	 */
	public static int getLit() {
		if(value < 32768) {
			return value;
		} else {
			return (value % 32768);
		}
	}
}
