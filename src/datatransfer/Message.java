package datatransfer;

import java.io.Serializable;

/**
 * Need programming comments with correct author name throughout this class
 * @author xyz abc
 */
public class Message implements Serializable{

	private static final long serialVersionUID = -3123211031738365740L;
	private String command;
	private Tuna tuna;

	public Message(String command) {
		this.command = command;
	}

	public Message (String command, Tuna tuna) {
		this.command = command;
		this.tuna = tuna;
	}

	public String getCommand() {
		return this.command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public Tuna getTuna() {
		return this.tuna;
	}
	public void setTuna(Tuna tuna) {
		this.tuna = tuna;
	}





}
