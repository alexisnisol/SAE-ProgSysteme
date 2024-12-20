package network.protocols;

public class Command {

    private String name;
    private String[] args;

    public Command(String command, String[] args) {
        this.name = command;
        this.args = args;
    }

    public static Command parse(String message) {
        String[] params = message.split(" ");
        String command = params[0].toUpperCase();
        params = java.util.Arrays.copyOfRange(params, 1, params.length);
        return new Command(command, params);
    }

    public String getName() {
        return this.name;
    }

    public String[] getArgs() {
        return this.args;
    }

}
