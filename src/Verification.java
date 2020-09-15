import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Verification {

    private  String passwordCipherResult;
    private int add;
    private String loginUser;
    private String lineStar = "*************************";
    private int choiceNumber;
    private boolean isNext = true;
    private String emailUser;
    private boolean accessUser;

    private Path fileUsersEmail = Paths.get("Library/Data library/E-Mail users.txt");
    private Path fileUsersPassword = Paths.get("Library/Data library/Password users.txt");
    private Path fileUsersLogin = Paths.get("Library/Data library/Login users.txt");
    private Path fileUsersAdmin = Paths.get("Library/Data library/Admin users.txt");
    private Path fileDataUsers = Paths.get("Library/Save files/Data users.txt");

    private List<String> userLoginList = new ArrayList<>();
    private List<String> userAdminList = new ArrayList<>();
    private List<String> userEmailList = new ArrayList<>();
    private List<String> userPasswordList = new ArrayList<>();

    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private Calendar date = new GregorianCalendar();
    private StringBuilder passwordCipher;

    /*
        Считывание данных пользователей из txt, если данные не считаются, будет сообщение,
    какой файл не удалось прочитать.
     */
    public void readDateUser() {
        try {
            userEmailList.clear();
            userLoginList.clear();
            userPasswordList.clear();
            if(Files.exists(fileUsersLogin)) {
                List<String> lineLoginUser = Files.readAllLines(fileUsersLogin);
                for (String login : lineLoginUser) {
                    userLoginList.add(login);
                }
            }else System.out.println("File с \"login\" не найден...");

            if(Files.exists(fileUsersEmail)) {
                List<String> lineEmailUser = Files.readAllLines(fileUsersEmail);
                for (String email : lineEmailUser) {
                    userEmailList.add(email);
                }
            }else System.out.println("File с \"email\" не найден...");

            if(Files.exists(fileUsersPassword)) {
                List<String> linePasswordUser = Files.readAllLines(fileUsersPassword);
                for (String password : linePasswordUser) {
                    userPasswordList.add(password);
                }
            }else System.out.println("File с \"password\" не найден...");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Ошибка в чтении из файла");
        }
    }

    /*
        Верификация пользователя. Если пользователь зарегистрирован, то для подтверждения необходимо
    ввести email и password. Если пользователь имеется в списках, то метод вернёт true. Проверяются данные,
    чтобы они не были null. Также проходит проверку email, если не будет символа `@`, то будет сообщение о
    проверки введенного email.
     */
    public boolean verificationUser(String email, String password) {
        if (!(email == null) && !(password == null)) {
            if (email.matches("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b")) {
                for (int i = 0; i < userEmailList.size(); i++) {
                    if (email.trim().toLowerCase().equals(userEmailList.get(i).trim().toLowerCase())) {
                        if (cipherPassword(password).equals(userPasswordList.get(i).trim())) {
                            emailUser = userEmailList.get(i);
                            loginUser = userLoginList.get(i);
                            return true;
                        }
                    }
                }
            } else {
                System.err.println("Корректно заполните поля");
            }
        } else {
            System.out.println("Введите корректно E-mail");
        }
        return false;
    }

    /*
    Алгоритм шифрования
     */
    public String cipherPassword(String passwordUser) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(passwordUser.getBytes());
            passwordCipher = new StringBuilder();
            for (byte b : bytes) {
                passwordCipher.append(String.format("%02X", b));
            }
            passwordCipherResult =  passwordCipher.toString();
        }catch (NoSuchAlgorithmException e) {
            System.err.println("Ошибка в алгоритме шифрования");
        }
        return  passwordCipherResult;
    }

    /*
        Регистрация нового пользователя. Система пробегается по списку пользователей, если соответствия
    с введенным данным нет, то счётчик +1, если пользователь есть в списке, то по итогу счётчик будет меньше
    длинны списка пользователей. Проходят проверку все приходящие данные, чтобы они не были null. Если
    пользователя нет в списке, то система сохраняте данные в файл.
     */
    public boolean userAdd(String login, String email, String password) {

        if(!(login == null) || !(email == null) || !(password == null)) {
            for (String mail : userEmailList) {
                if (!(mail.toLowerCase().equals(email.toLowerCase()))) {
                    add++;
                }
            }
            if (email.matches("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b")) {
                if ((add == (userEmailList.size()))) {
                    try {
                        if (Files.exists(fileUsersEmail)) {
                            if (Files.exists(fileUsersPassword)) {
                                if (Files.exists(fileUsersLogin)) {
                                    Files.write(fileUsersPassword, "\n".getBytes(), StandardOpenOption.APPEND);
                                    Files.write(fileUsersPassword, cipherPassword(password).getBytes(), StandardOpenOption.APPEND);
                                    Files.write(fileUsersEmail, "\n".getBytes(), StandardOpenOption.APPEND);
                                    Files.write(fileUsersEmail, email.getBytes(), StandardOpenOption.APPEND);
                                    Files.write(fileUsersLogin, "\n".getBytes(), StandardOpenOption.APPEND);
                                    Files.write(fileUsersLogin, login.getBytes(), StandardOpenOption.APPEND);
                                    return true;
                                }
                            }
                        }
                    } catch (IOException e) {
                        System.err.println("Ошибка в добавлении нового пользователя");
                    }
                } else if (add < userEmailList.size()) {
                    System.out.println("Email занят, возможно вы зарегистрированы");
                }
            } else {
                System.out.println("Введите корректно данные...");
            }
        }
        return false;
    }

    /*
        Добавление пользователя в администраторы. Администратор вводит email. Происходит проверка наличие символа `@`
    и только тогда происходит сохранение в файле "Admin"
     */
    public boolean accessAdmin(String email){
        if (!(email == null)) {
            if (email.matches("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b")) {
                try {
                    userAdminList.clear();
                    if (Files.exists(fileUsersAdmin)) {
                        List<String> lineAdminUser = Files.readAllLines(fileUsersAdmin);
                        for (String admin : lineAdminUser) {
                            userAdminList.add(admin);
                        }
                    }
                    for (int j = 0; j < userAdminList.size(); j++) {
                        if (userAdminList.get(j).toLowerCase().trim().equals(email.toLowerCase().trim())) {
                            return true;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Ошибка в чтении из файла");
                }
            }
        }
        System.out.println("\n" + email + " - у вас нет доступа к данному разделу...");
        System.out.println("\n***В доступе отказано. Чтобы продолжить, нужны права администратора*** \n");
        return false;
    }

    /*
        Просмотр списка пользователей зарегистрируемых в библеотеке: Email и login
    Также доступен выбор для просмотра полных данных пользователей Email, login и Password
     */
    public void watchUsers(String email) {
        System.out.println(lineStar + "\n");
        try {
            if(accessAdmin(email)) {
                readDateUser();
                System.out.println("Просмотр зарегистрированных users в библиотеке...");
                System.out.println("Email | Login");
                for(int i = 0; i < userEmailList.size(); i++) {
                    System.out.print(userEmailList.get(i) + " | ");
                    System.out.print(userLoginList.get(i) + " | ");
                    System.out.println();
                }
                isNext = true;
                while (isNext) {
                    System.out.println("\n1 - Вывести полную информацию об пользователях; 2 - Сохранение информации "
                            + "об пользователях в файл txt; 0 - Закончить просмотр");
                    System.out.print("\nВыберите действие: - ");
                    String stringLine = reader.readLine();
                    if(isInteger(stringLine)) {
                        choiceNumber = Integer.parseInt(stringLine.trim());
                    } else {
                        System.out.println("\nВведено не число... Повторите ввод... Введите число...");
                        System.out.print("\nНажмите \"Enter\", чтобы продолжить ");
                        String enter = reader.readLine();
                    }
                    switch (choiceNumber) {
                        case 0:
                            isNext = false;
                            break;
                        case 1:
                            allDataUsers(email);
                            break;
                        case 2:
                            saveDataUsersToFile(email);
                            isNext = false;
                            break;
                    }
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    Просмотр полных данных пользователей Email, login и Password
     */
    public void allDataUsers(String email) {
        try {
            if(accessAdmin(email)) {
                readDateUser();
                System.out.println("\nВсе данные users: ");
                System.out.println("Email | Login | Password");
                for(int i = 0; i < userEmailList.size(); i++) {
                    System.out.print(userEmailList.get(i) + " | ");
                    System.out.print(userLoginList.get(i) + " | ");
                    System.out.print(userPasswordList.get(i));
                    System.out.println();
                }
                System.out.println("\nАдминистраторы: ");

                for(String admin : userAdminList) {
                    System.out.println(admin);
                }
            }
        }catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    /*
       Сохранение всех данных пользователя в файл формата txt. Доступно сохранение по умолчанию
   и на указанный путь. Происходит сохранение всех пользователей, а также пользователей, которые
   имеются в списке администраторов, имя пользователя/администратора, который сохраняет данные и дата.
    */
    public void saveDataUsersToFile(String email) {
        readDateUser();
        try {
            System.out.println("\n1 - Сохранение в корень проекта; 2 - Указать путь пользователя");
            System.out.print("Выбеите действие - ");
            int number = 77;
            String stringLine = reader.readLine();
            if(isInteger(stringLine)) {
                number = Integer.parseInt(stringLine.trim());
            } else {
                System.out.println("\nВведено не число... Повторите ввод... Введите число...");
                System.out.print("\nНажмите \"Enter\", чтобы продолжить ");
                String enter = reader.readLine();
            }
            Path fileDataUsers;
            switch (number) {
                case 1:
                    fileDataUsers = Paths.get("Library/Library users data.txt");
                    break;
                case 2:
                    System.out.print("Укажите путь - ");
                    String path = reader.readLine();
                    System.out.print("Укажите имя файла - ");
                    String nameFile = reader.readLine();

                    // замена "\" на "/"
                    fileDataUsers = Paths.get(path.replaceAll("/", "\\") + "/" + nameFile + ".txt");
                    break;
                default:
                    fileDataUsers = Paths.get("Library/Library users data.txt");
                    System.out.println("Файл будет сохранен по умолчанию.... " + fileDataUsers.getParent());
                    break;
            }
            if(!Files.exists(Paths.get("Library/Save files"))) {
                Files.createDirectory(Paths.get("Library/Save files"));
            }

            if(Files.exists(fileDataUsers)) {
                Files.delete(fileDataUsers);
            }

            if (!Files.exists(fileDataUsers)) {
                Files.createFile(fileDataUsers);
                Files.write(fileDataUsers, "Email | Login | Password".getBytes(), StandardOpenOption.APPEND);
                Files.write(fileDataUsers, "\r\n".getBytes(), StandardOpenOption.APPEND);
                for (int i = 0; i < userEmailList.size(); i++) {

                    Files.write(fileDataUsers, userEmailList.get(i).getBytes(), StandardOpenOption.APPEND);
                    Files.write(fileDataUsers, " | ".getBytes(), StandardOpenOption.APPEND);
                    Files.write(fileDataUsers, userLoginList.get(i).getBytes(), StandardOpenOption.APPEND);
                    Files.write(fileDataUsers, " | ".getBytes(), StandardOpenOption.APPEND);
                    Files.write(fileDataUsers, userPasswordList.get(i).getBytes(), StandardOpenOption.APPEND);
                    Files.write(fileDataUsers, "\r\n".getBytes(), StandardOpenOption.APPEND);

                }
                Files.write(fileDataUsers, "\r\n".getBytes(), StandardOpenOption.APPEND);
                Files.write(fileDataUsers, lineStar.getBytes(), StandardOpenOption.APPEND);
                Files.write(fileDataUsers, "\r\n".getBytes(), StandardOpenOption.APPEND);
                Files.write(fileDataUsers, "Администраторы".getBytes(), StandardOpenOption.APPEND);
                Files.write(fileDataUsers, "\r\n".getBytes(), StandardOpenOption.APPEND);

                for (String admin : userAdminList) {
                    Files.write(fileDataUsers, admin.getBytes(), StandardOpenOption.APPEND);
                    Files.write(fileDataUsers, "\r\n".getBytes(), StandardOpenOption.APPEND);
                }
                Files.write(fileDataUsers, "\r\n".getBytes(), StandardOpenOption.APPEND);
                Files.write(fileDataUsers, lineStar.getBytes(), StandardOpenOption.APPEND);
                Files.write(fileDataUsers, "\r\n".getBytes(), StandardOpenOption.APPEND);
                Files.write(fileDataUsers, "User/Admin - ".getBytes(), StandardOpenOption.APPEND);
                Files.write(fileDataUsers, email.getBytes(), StandardOpenOption.APPEND);
                Files.write(fileDataUsers, "\r\n".getBytes(), StandardOpenOption.APPEND);
                Files.write(fileDataUsers, date.getTime().toString().getBytes(), StandardOpenOption.APPEND);
                System.out.println("\nФайл сохранен");
            }

            System.out.println("Нажмите \"Enter\" чтобы продолжить...");
            String enter = reader.readLine();
        }catch (ArrayIndexOutOfBoundsException e) {
            //  e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
        Добавление пользователя в администраторы. Администратор вводит email пользователя,
    которого необходимо добавить в список администраторов. Проверка наличие введенного email на
    наличие такого email в списке пользователей. Если пользователь есть, то происходит сохранение
    в файл Admin...
     */
    public void addAdmin(String email) {
        try {
        System.out.println("\nДобавление пользователя в администраторы...");
        if(accessAdmin(email)) {
                System.out.print("Введите email нужного пользователя - ");
                String addEmailAdmin = reader.readLine();
                readDateUser();
                for (int i = 0; i < userEmailList.size(); i++) {
                    if (addEmailAdmin.toLowerCase().trim().equals(userEmailList.get(i).toLowerCase())) {
                        if (Files.exists(fileUsersAdmin)) {
                            Files.write(fileUsersAdmin, "\r\n".getBytes(), StandardOpenOption.APPEND);
                            Files.write(fileUsersAdmin, addEmailAdmin.getBytes(), StandardOpenOption.APPEND);
                            System.out.println("\nПользователь добавлен в администраторы...");
                        }
                    }
                }
                int beforeAdmin = userAdminList.size();
                accessAdmin(email);
                if(!(beforeAdmin < userAdminList.size())) {
                    System.out.println("\nПользователь не добавлен в администраторы...");
                }
                System.out.println("Нажмите \"Enter\" чтобы продолжить...");
                String enter = reader.readLine();
        }
    } catch (IOException e) {

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