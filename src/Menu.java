import java.util.*;

public class Menu
{
    private String[] options;
    private int numOptions;
    public static final int DEFAULT_SIZE = 10;

    public Menu()
    {
        options = new String[DEFAULT_SIZE];
        numOptions = 0;
    }

    public Menu(int size)
    {
        if (size < 1)
        {
            throw new IllegalArgumentException(
                err("Size is too small")
            );
        }
        else
        {
            options = new String[size];
            numOptions = 0;
        }
    }

    public void addOption(String inOption)
    {
        if (inOption == null || inOption.equals(""))
        {
            throw new IllegalArgumentException(
                err("Option is invalid")
            );
        }
        else if (numOptions >= options.length)
        {
            throw new IllegalArgumentException(
                err("Too many options")
            );
        }
        else
        {
            options[numOptions++] = inOption;
        }
    }

    public int getUserInput()
    {
        printMenu();
        return Input.integer(1, options.length - 1);
    }

    public void printMenu()
    {
        for (int i = 0; i < options.length; i++)
        {
            System.out.println(options[i]);
        }
    }

    private String err(String msg)
    {
        return "\u001b[31mMenu\u001B[0m: " + msg;
    }
}
