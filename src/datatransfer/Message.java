package datatransfer;

import java.io.Serializable;

/**
 * @author Dean Comeau
 */
public class Message implements Serializable{

	private static final long serialVersionUID = -3123211031738365740L;
	private String command;
	private Tuna tuna;

	/**
	 * @param command command to be set to class variable
	 */
	public Message(String command) {
		this.command = command;
	}

	/**
	 * @param command command to be set to class variable
	 * @param tuna tuna object to be set to class variable
	 */
	public Message (String command, Tuna tuna) {
		this.command = command;
		this.tuna = tuna;
	}

	/**
	 * @return command
	 * command getter
	 */
	public String getCommand() {
		return this.command;
	}

	/**
	 * @param command command setter
	 */
	public void setCommand(String command) {
		this.command = command;
	}

	/**
	 * @return Tuna object, tuna getter
	 */
	public Tuna getTuna() {
		return this.tuna;
	}
	/**
	 * @param tuna tuna to be set as class variable
	 * tuna setter
	 */
	public void setTuna(Tuna tuna) {
		this.tuna = tuna;
	}





}
