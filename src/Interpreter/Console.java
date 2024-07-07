package Interpreter;

import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Consumer;

import Context.Environnement;
import Exception.SyntaxError;
import Exception.VariableNotExistError;
import Expression.ArithmeticExpression;
import Expression.ArithmeticExpressionFactory;
import Expression.MinimalExpressionFactory;

public class Console {

    // Boolean to check if the program is running or not
    private Boolean running = true;

    // Map that contains the special commands
    private HashMap<String, Consumer<Void>> commands = new HashMap<String, Consumer<Void>>() {{
        put("exit", (Void) -> runExit());
    }};

    // The current environnement of execution
    private Environnement env = new Environnement();

    /**
     * @brief   Run the console
     * @details This method is called when the program start, it will run multiples
     *          commands until the command "exit" is entered.
     *          If the comman is a special command, it will run the associated
     *          method, otherwise it will run the expression parser.
     */
    public void run() {
        System.out.println("Starting console...");
        while (this.running) {
            System.out.println("Enter your command / expr: ");
            String input = new Scanner(System.in).nextLine();
            this.runCommand(input);
        }
    }

    /**
     * @brief       Run a command
     * @param input The command to run
     * @details     Once a command is entered, this method will check if it is a
     *              special command and run it if it is the case, otherwise it will
     *              run the expression parser.
     */
    private void runCommand(String input) {
        for (String command : this.commands.keySet()) {
            if (input.startsWith(command)) {
                this.commands.get(command).accept(null);
                return;
            }
        }
        this.runExpression(input);
    }

    /**
     * @brief       Run the expression parser
     * @param input The expression to parse
     */
    private void runExpression(String input) {
        ArithmeticExpression expr;

        this.env.setExpression(input);
        try {
            expr = new ArithmeticExpressionFactory().parse(input);
            if (expr == null) {
                expr = new MinimalExpressionFactory().parse(input);
            }
            if (expr != null) {
                expr = expr.simplify();
            }
        } catch (SyntaxError err) {
            System.out.println("You have a syntax problem");
            return;
        } catch (VariableNotExistError err) {
            System.out.println("You have a variable problem");
            return;
        }
        if (env == null) {
            System.out.println("Can't parse the expression");
            return;
        }
        System.out.println("expr " + expr.toStringBuilder());
        try {
            System.out.println(expr.evaluate());
        } catch (VariableNotExistError err) {
            System.out.println("You have a variable problem");
        }
    }

    /**
     * @brief   Exit the console
     * @details This method is called when the user enters the command "exit"
     *          in the console. It set the attribute "running" to false, which
     *          will stop the console loop.
     */
    private void runExit() {
        this.running = false;
    }
}
