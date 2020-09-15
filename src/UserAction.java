import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserAction {

    private int numberChoice = 777;
    private boolean isNext = true;
    private String lineEquals = "=================";
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private CatalogBooks catalogBooks = new CatalogBooks();
    private Verification verification = new Verification();

    /*
    В данном методе представлено основное меню библиотеки и варианты работы для пользователей и администраторов.
     */
    public void work(String email) {
        catalogBooks.fileBooks();
        try {
            while (isNext) {
                System.out.println();
                System.out.print(lineEquals);
                System.out.print("Действия пользователя");
                System.out.println(lineEquals);
                System.out.println("1 - Просмотр каталога; 2 - Поиск по ключевому слову; 3 - Предложить книгу; \n4 - Сохранение каталога на жесткий диск; 0 - Выход.");
                System.out.print("\n" + lineEquals);
                System.out.print("Действия администратора");
                System.out.print(lineEquals + "\n");
                System.out.println("7 - Добавление книги в каталог; 8 - Просмотр зарегистрируемых пользователей;\n9 - Добавление пользователя в администраторы");
                System.out.print("\nВыбеите действие - ");

                String stringLine = reader.readLine();
                if(isInteger(stringLine)) {
                    numberChoice = Integer.parseInt(stringLine.trim());
                } else {
                    System.out.println("\nВведено не число... Повторите ввод... Введите число...");
                    System.out.print("\nНажмите \"Enter\", чтобы продолжить ");
                    String enter = reader.readLine();
                }

                switch (numberChoice) {
                    case 0:
                        isNext = false;
                        System.out.println("До свидания...");
                        break;
                    case 1:
                        catalogBooks.watchListPage();
                        System.out.println("Действие завершено...");
                        break;
                    case 2:
                        catalogBooks.searchWord();
                        System.out.println("Действие завершено...");
                        break;
                    case 3:
                        catalogBooks.newBook(email);
                        System.out.println("Действие завершено...");
                        break;
                    case 4:
                        catalogBooks.saveCatalogBooks(email);
                        System.out.println("Действие завершено...");
                        break;
                    case 7:
                        catalogBooks.setBookList(email);
                        System.out.println("Действие завершено...");
                        break;
                    case 8:
                        verification.watchUsers(email);
                        System.out.println("Действие завершено...");
                        break;
                    case 9:
                        verification.addAdmin(email);
                        System.out.println("Действие завершено...");
                        break;
                    default:
                        System.out.println("Выберите действие!");
                        break;
                }
            }
        }catch (IOException e) {
            System.out.println("Error - " + e.getStackTrace());
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