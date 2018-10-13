import java.util.*;

public class Input
{
    public static int integer(int lower, int upper)
    {
        Scanner sc = new Scanner(System.in);
        int userInput = 0;
        boolean inputValid = false;

        while (! inputValid)
        {
            try
            {
                userInput = sc.nextInt();
                inputValid = true;
                if (userInput < lower || userInput > upper)
                {
                    System.out.println(
                        err("Input is out of bounds.")
                    );
                    inputValid = false;
                }
            }
            catch (InputMismatchException e)
            {
                String flush = sc.nextLine();
                System.out.println(err("Must be an integer."));
                System.out.println("Try again.");
                inputValid = false;
            }
            catch (Exception ex)
            {
                String flush = sc.nextLine();
                System.out.println(
                    err("Unknown exception caught, " + ex.getMessage())
                );
            }
        }
        return userInput;
    }

    public static String string(String prompt)
    {
        System.out.print(prompt);
        return string();
    }

    public static String string()
    {
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    private static String err(String msg)
    {
        return "\u001b[31mInput Error\u001B[0m: " + msg;
    }
}
