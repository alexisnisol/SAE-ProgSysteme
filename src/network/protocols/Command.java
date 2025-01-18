package network.protocols;

/**
 * Classe représentant une commande réseau composée d'un nom de commande et d'arguments.
 * Cette classe permet d'analyser un message réseau et de le convertir en une structure manipulable.
 */
public class Command {

    /** Nom de la commande. */
    private String name;

    /** Arguments associés à la commande. */
    private String[] args;

    /**
     * Constructeur de la classe Command.
     *
     * @param command le nom de la commande.
     * @param args les arguments de la commande.
     */
    public Command(String command, String[] args) {
        this.name = command;
        this.args = args;
    }

    /**
     * Analyse un message réseau et le convertit en une instance de Command.
     *
     * @param message le message réseau à analyser.
     * @return une instance de Command correspondant au message analysé.
     */
    public static Command parse(String message) {
        String[] params = message.split(" ");
        String command = params[0].toUpperCase();
        params = java.util.Arrays.copyOfRange(params, 1, params.length);
        return new Command(command, params);
    }

    /**
     * Récupère le nom de la commande.
     *
     * @return le nom de la commande.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Récupère les arguments de la commande.
     *
     * @return un tableau contenant les arguments de la commande.
     */
    public String[] getArgs() {
        return this.args;
    }
}
