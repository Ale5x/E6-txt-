import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LogginIn {

    private BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
    private Verification verification = new Verification();
    private UserAction userAction = new UserAction();

    private int choiceNumber;
    private boolean isNext = true;
    private String password;
    private String email;
    private boolean access;
    private String login;

    // Выбираем необходимые действия: вход либо регистрация... После регистрации необходимо войти в систему...
    public void login() {
        verification.readDateUser();
        System.out.println();
        System.out.println("Выберите действие: \n1 - Войти в систему; \n2 - Зарегистрироваться; \n0 - Выход.");

        while(isNext) {
            try {
                System.out.println();
                System.out.print("Действие: ");
                String stringLine = read.readLine();
                if(isInteger(stringLine)) {
                    choiceNumber = Integer.parseInt(stringLine.trim());
                } else {
                    System.out.println("\nВведено не число... Повторите ввод... Введите число...");
                    System.out.print("\nНажмите \"Enter\", чтобы продолжить ");
                    String enter = read.readLine();
                }
                switch (choiceNumber) {
                    case 0:
                        System.out.println("The end program");
                        isNext = false;
                        break;
                    case 1:
                        enterData(choiceNumber);
                        isNext = false;
                        break;
                    case 2:
                        System.out.println("==========Регистрация нового пользователя==========");
                        enterData(choiceNumber);
                        isNext = false;
                        break;
                    default:
                        System.out.println("Выберите существующий пункт, чтобы продолжить...");
                }
            } catch (IOException e) {
                System.out.println("Error " + e);
            }
        }
    }

    /*
        Метод получает значение из метода login() и обрабатывает в соответствии с выбранным действием
     проходит аутентификация пользователя, как только проходит сверка всех данных и verificationUser()
     возвращает true, переходим к дальнейшей работе с библиотекой иначе false и требование зарегистрироваться,
     чтобы продожить.
     */

    public void enterData(int choiceNumber) {
        try {
            System.out.print("Введите E-mail: ");
            email = read.readLine();

            System.out.print("Введите пароль: ");
            password = read.readLine();
            if (choiceNumber == 1) {
                access = verification.verificationUser(email, password);
                if (access) {
                    System.out.println("\n==============Добро пожаловать!==============\n");
                    userAction.work(email);
                } else {
                    System.err.println("В доступе отказано! Неверный пароль либо email");
                    System.err.println("Зарегистрируйтесь, чтобы продолжить! Или проверьте введеные данные...");
                }
            }
            if (choiceNumber == 2) {
                System.out.print("Введите логин: ");
                login = read.readLine();
                access = verification.userAdd(login, email, password);
                if (access) {
                    login();
                } else {
                    System.err.println("Логин занят");
                }
            }
        }catch (NumberFormatException e) {

            System.err.println("Введено не число. Введите число!");

        }catch (IOException e) {
            System.out.println("Error " + e.getStackTrace());
        }
    }

    /*
        Метод проверят тип данных. Необходим для проверки, когда пользователь выбирает действие.
     Если пользователь вводит текст, то метод вернет false
     */
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        return true;
    }
}