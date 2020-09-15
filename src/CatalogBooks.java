import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;

public class CatalogBooks {

    private int numberChoice;
    private String typeBook;
    private String word;
    private String author;
    private String nameBook;
    private String type;
    private boolean isNext = true;
    private String lineEquals = "=====================";

    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private Calendar date = new GregorianCalendar();
    private Verification verification = new Verification();

    private Path fileCatalogBooks = null;
    private Path pathFileCatalogBooks = Paths.get("Library/Data library/Catalog books.txt");

    private List<String> catalogBooksFile;
    private List<String> catalogBooksList = new ArrayList<>();

    // Считывание каталога из текстового файла
    public void fileBooks() {
        catalogBooksList.clear();
        try {
            if (Files.exists(pathFileCatalogBooks)) {
            } else {
                System.out.println("Файл с каталогами не найден! Возможно он был перемещён или удален.");
            }
            catalogBooksFile = Files.readAllLines(pathFileCatalogBooks);
            for(String book : catalogBooksFile) {
                catalogBooksList.add(book);
            }
        } catch (IOException e) {
            System.out.println("Error - " + e);
        }
    }

    // Постраничный просмотр каталога книг
    public void watchListPage() {
        System.out.println("\nПросмотр каталога\n");
        fileBooks();
        int page = 0;
        int step = 20;
        int numberBook = 0;
        int numberBookMax;

        numberBookMax = numberBook + step;
        for (int i = 0; i  < numberBookMax; i++) {
            if (catalogBooksList.get(numberBook) != null) {
                System.out.println(catalogBooksList.get(i));
            }
        }
        page++;

            System.out.print("\n" + lineEquals);
            System.out.print(page);
            System.out.println(lineEquals + "\n");

        numberBook += step;
        boolean isNextWatchCatalog = true;
        while(isNextWatchCatalog) {
            try {
                System.out.println("1 - Предыдущая страница; 2 - Переход к заданной страние; 3 - Следущая страница; 0 - Выход ");
                System.out.print("\nДействие - ");
                String stringLine = reader.readLine();
                if(isInteger(stringLine)) {                                 // проверка на введение числа, а не текста
                    numberChoice = Integer.parseInt(stringLine.trim());
                } else {
                    System.out.println("\nВведено не число... Повторите ввод... Введите число...");
                    System.out.print("\nНажмите \"Enter\", чтобы продолжить ");
                    String enter = reader.readLine();
                }
                if (numberChoice == 0) {
                    System.out.println("Конец просмотра");
                    isNextWatchCatalog = false;
                } else if (numberChoice == 1) {
                    page--;
                    if(page <= 0) {
                        page = 1;
                    }
                } else if (numberChoice == 2) {
                    System.out.print("Введите номер страницы: ");
                    page = Integer.parseInt(reader.readLine().trim());
                } else if (numberChoice == 3) {
                    page++;
                    if(page > catalogBooksList.size() / step) {
                        page = catalogBooksList.size() / step;

                        if((page * step) < catalogBooksList.size()) {

                            page = catalogBooksList.size() / step + 1;
                        }
                    }
                } else {
                    System.out.println("Сделайте правильный выбор либо нажмите '0'");
                }


                numberBook = page * step - step;
                numberBookMax = numberBook + step;
                for (int i = 0; (i + numberBook) < numberBookMax; numberBook++) {
                    if (numberBook < catalogBooksList.size()) {
                            System.out.println(catalogBooksList.get(numberBook));
                    }
                }
                if (!(numberChoice == 0)) {
                    if (page > 0) {
                        System.out.print(lineEquals);
                        System.out.print(page);
                        System.out.println(lineEquals);
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {

            } catch (IOException e) {

            }
        }
    }

    /*
        Поиск по клучевому слову в каталоге книг, в результате будет совершён вывод тех результатов,
     которые соответствуют критерию поиска

     */
    public void searchWord() {
        try {
            int add = 0;
            System.out.print("\nПоиск по ключевому слову.\nВведите ключевое слово - ");
            word = reader.readLine().trim();
            System.out.println();
            for(String line : catalogBooksList) {
                if(!((line.toLowerCase().indexOf(word.trim())) == -1) || !(line.indexOf(word.trim()) ==  -1)) {
                    System.out.println(line);
                    add++;
                }
            }
            System.out.println("\nКолличество найденых результатов - " + add);

            System.out.println("Нажмите \"Enter\" чтобы продолжить...");
            String enter = reader.readLine();
        }catch (IOException e) {
            System.out.println("Error - " + e);
        }
    }

    /*
    Регистрация книги в книжном фонде библиотеки.
     */
    public void setBookList(String email) {
        try {
        if(verification.accessAdmin(email)) {
            System.out.println("\nРегистрации книги в книжном фонде библиотеке");
            int numberBookCatalog = catalogBooksList.size() + 1;
            while (isNext) {

                    System.out.print("\nВведите имя автора: ");
                    author = reader.readLine();

                    System.out.print("Введите название книги: ");
                    nameBook = reader.readLine();

                    System.out.print("Введите тип книги (бумажный либо электронный): ");
                    typeBook = reader.readLine();
                    if ("Электронный".toLowerCase().startsWith(typeBook.trim()) || "электронный".toLowerCase().endsWith(typeBook.trim())) {
                        type = "Электронный";
                    } else if ("Бумажный".toLowerCase().startsWith(typeBook.trim()) || "бумажный".toLowerCase().endsWith(typeBook.trim())) {
                        type = "Бумажный";
                    } else {
                        System.out.println("Ошибка! Некоректно ведённые данные. Выберите тип книги из допустимых вариантов...");
                    }

                    String newBook = numberBookCatalog + "«" + nameBook + "» - " + author;

                    if (Files.exists(pathFileCatalogBooks)) {
                        Files.write(pathFileCatalogBooks, "\n".getBytes(), StandardOpenOption.APPEND);
                        Files.write(pathFileCatalogBooks, newBook.getBytes(), StandardOpenOption.APPEND);
                        System.out.println("Книга сохранена в каталог...");
                        numberBookCatalog++;
                    }

                    System.out.println("Продолжить заполнение? 1 - да; 0 - нет");
                    System.out.print("Действие - ");

                    String stringLine = reader.readLine();

                    // проверка на введение числа, а не текста

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
                            break;
                        case 1:
                            break;
                        default:
                            System.out.println("Для продолжнения заполнения нажмите - 1 или 0 - для выхода");
                    }
            }
        }
            System.out.println("Нажмите \"Enter\" чтобы продолжить...");
            String enter = reader.readLine();
    } catch (NumberFormatException e) {
        System.err.println("Введите корректно данные: введите число!");
    } catch (IOException e) {
        System.err.println("Error " + e);
    }
    }

    /*
    Сохранение каталога книг на диск D либо по пути, который укажет пользователь.
     */
    public void saveCatalogBooks(String email) {

        int number = 77;
        try {
            System.out.println("\n1 - Сохранение на диск D; 2 - Указать путь пользователя");
            System.out.print("Выбеите действие - ");
            String stringLine = reader.readLine();

            // Проверка на введение числа, а не текста

            if(isInteger(stringLine)) {
                number = Integer.parseInt(stringLine.trim());
            } else {
                System.out.println("\nВведено не число... Повторите ввод... Введите число...");
                System.out.print("\nНажмите \"Enter\", чтобы продолжить ");
                String enter = reader.readLine();
            }
            switch (number) {
                case 1:
                    fileCatalogBooks = Paths.get("D:/Catalog books.txt");
                    break;
                case 2:
                    System.out.print("Укажите путь - ");
                    String path = reader.readLine();
                    System.out.print("Укажите имя файла - ");
                    String nameFile = reader.readLine();
                    // замена "\" на "/"
                    fileCatalogBooks = Paths.get(path.replaceAll("/", "\\") +  "/" + nameFile + ".txt");
                    break;
                default:
                    fileCatalogBooks = Paths.get("D:/Catalog books.txt");
                    System.out.println("Файл будет сохранен по умолчанию... " + fileCatalogBooks.getParent());
                    break;
            }

            // Проверка на существование файла txt, если его нет, автоматически создание
            if (!Files.exists(fileCatalogBooks)) {
                Files.createFile(fileCatalogBooks);

                for(String book : catalogBooksList) {
                    Files.write(fileCatalogBooks, book.getBytes(), StandardOpenOption.APPEND);
                    Files.write(fileCatalogBooks,"\r\n".getBytes(), StandardOpenOption.APPEND);
                }

                Files.write(fileCatalogBooks, "\r\n".getBytes(), StandardOpenOption.APPEND);
                Files.write(fileCatalogBooks, "User - ".getBytes(), StandardOpenOption.APPEND);
                Files.write(fileCatalogBooks, email.getBytes(), StandardOpenOption.APPEND);
                Files.write(fileCatalogBooks, "\r\n".getBytes(), StandardOpenOption.APPEND);
                Files.write(fileCatalogBooks, date.getTime().toString().getBytes(), StandardOpenOption.APPEND);

                System.out.println("Файл сохранён на диск...");
            }

            System.out.println("Нажмите \"Enter\" чтобы продолжить...");
            String enter = reader.readLine();
        }catch (IOException e) {
            System.out.println("Ошибка в сохранении каталога на диск");
            e.printStackTrace();
        }
    }

    /*
        Сообщение пользователя для администратора насчёт добавления книги в книжный фонд.
    В сообщение будет приведено сообщение пользователя, название книги, кто отправил
    сообщение и дата отправки сообщения. Сообщение сохраняется по адресу D:/Library/Message for Admin
    Если директория не создана, то создается директория "Message for Admin".
     */
    public void newBook(String user) {
        try {
            System.out.println("\nВедите ваше сообщение: ");
            String message = reader.readLine();
            System.out.print("Введите название книги - ");
            String book = reader.readLine();

            SimpleDateFormat dateFormat = new SimpleDateFormat("y.MM.d HH.mm.ss");
            String newDate = dateFormat.format(new Date());

            if(!Files.exists(Paths.get("Library/Message for Admin"))) {
                Files.createDirectory(Paths.get("Library/Message for Admin"));
            }

            Path newBookInTheCatalog = Paths.get("Library/Message for Admin/! Message user for Admin from "
                    + user + " - " + newDate +".txt");
            if(!Files.exists(newBookInTheCatalog)) {
                Files.createFile(newBookInTheCatalog);
                Files.write(newBookInTheCatalog, "Сообщение пользователя: ".getBytes(), StandardOpenOption.APPEND);
                Files.write(newBookInTheCatalog, message.getBytes(), StandardOpenOption.APPEND);
                Files.write(newBookInTheCatalog, "\r\n".getBytes(), StandardOpenOption.APPEND);
                Files.write(newBookInTheCatalog, "Предложенная книга - ".getBytes(), StandardOpenOption.APPEND);
                Files.write(newBookInTheCatalog, book.getBytes(), StandardOpenOption.APPEND);
                Files.write(newBookInTheCatalog, "\r\n".getBytes(), StandardOpenOption.APPEND);
                Files.write(newBookInTheCatalog, "********************\r\n".getBytes(), StandardOpenOption.APPEND);
                Files.write(newBookInTheCatalog, "User - ".getBytes(), StandardOpenOption.APPEND);
                Files.write(newBookInTheCatalog, user.getBytes(), StandardOpenOption.APPEND);
                Files.write(newBookInTheCatalog, "\r\n".getBytes(), StandardOpenOption.APPEND);
                Files.write(newBookInTheCatalog, date.getTime().toString().getBytes(), StandardOpenOption.APPEND);
            }

            System.out.println("Сообщение отправлено...");
            System.out.println("Нажмите \"Enter\" чтобы продолжить...");
            String enter = reader.readLine();
        }catch (IOException e) {

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
