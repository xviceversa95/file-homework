import java.io.*;
import java.time.LocalDateTime;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class Main {
    // установить базовую директорию для установки игры:
    public static final String BASE_DIR = "H://Games/";
    public static StringBuilder log = new StringBuilder();
    // Делаем перевод строки независимым от ОС:
    public static String separator = System.lineSeparator();

    public static void main(String[] args) {

    // Проверям наличие корневой директории:
    // Если корневая директория отсутствует, создадим её:
    // Добавить проверку на чтение и запись

        File root_dir = new File(BASE_DIR);
        if (!root_dir.exists()) {
            root_dir.mkdir();
            log.append(LocalDateTime.now() + " Корневая директория "+ root_dir.getName() + " создана по адресу " + root_dir.getAbsolutePath() + separator);
        } else {
            log.append(LocalDateTime.now() + " Корневая директория " + root_dir.getName() + " существует по адресу " + root_dir.getAbsolutePath() + separator);
        }
        if (root_dir.canRead() && root_dir.canWrite()) {
            log.append(LocalDateTime.now() + " Корневая директория доступна для чтения и записи" + separator);
        } else {
            log.append(LocalDateTime.now() + " Корневая директория недоступна для чтения или записи" + separator);
        }

    // Список базовых директорий:
        String[] baseDirectories = {
                "src",
                "res",
                "savegames",
                "temp"
        };
    // Список поддиректорий res:
        String[] resDirectories = {
                "drawables",
                "vectors",
                "icons"
        };

    // Список поддиректорий src:
        String[] srcDirectories = {
                "main",
                "test"
        };

    // Список файлов директории BASE_DIR/src/main:
        String[] mainFiles = {
                "Main.txt",
                "Utils.java"
        };

    // Список файлов директории BASE_DIR/temp:
       String[] tempFiles = {
               "temp.txt"
       };



    // Создаём базовые директории внутри корневой папки игры:
        for (String directory : baseDirectories) {
            createDirectory(root_dir, directory);
        }

    // Создаём субдиректории в src:
        for (String directory : srcDirectories) {
            File src = new File(BASE_DIR, "src");
            createDirectory(src, directory);
        }

    // Создаём субдиректории в res:
        for (String directory : resDirectories) {
            File res = new File(BASE_DIR, "res");
            createDirectory(res, directory);
        }

    // Создаём файлы в директории BASE_DIR/src/main:
        for (String item : mainFiles) {
            File parentDir = new File(BASE_DIR, "src/main");
            createFile(parentDir, item);
        }

    // Создаём файлы в директории BASE_DIR/src/temp:
        for (String item : tempFiles) {
            File parentDir = new File(BASE_DIR, "temp");
            createFile(parentDir, item);
        }

        //Пишем лог
        String logText = log.toString();
        File logFile = new File(root_dir, "temp/temp.txt");
        try (FileWriter writer = new FileWriter(logFile)) {
            writer.write(logText);
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        //Создаём экземпляры сохранений:
        GameProgress[] gameList = {
                new GameProgress(100, 100, 2, 14.5),
                new GameProgress(50, 30, 2, 29.52),
                new GameProgress(100, 50, 14, 23.54)
        };

        String zipPath = "H://Games/savegames/savepath.zip";

        String[] savepathList = {
                "H://Games/savegames/savegames1.dat",
                "H://Games/savegames/savegames2.dat",
                "H://Games/savegames/savegames3.dat"
        };

        for (String path : savepathList){
            for (GameProgress save : gameList) {
                saveGame(path, save);
            }
        }
        zipFiles(zipPath, savepathList);

    }



    //Метод для создания директорий:
    public static void createDirectory(File parent_dir, String directory) {
        File dir = new File(parent_dir, directory);
        if (!dir.exists()) {
            dir.mkdir();
            log.append(LocalDateTime.now() + " Создана директория " + dir.getName() + " по адресу " + dir.getAbsolutePath() + separator);
        } else {
            log.append(LocalDateTime.now() + " Директория " + dir.getName() + " уже существует" + separator);
        }
    }

    //Метод для создания файлов в директориях:
    public static void createFile (File parent_dir, String fileName) {
        try {
            File file = new File(parent_dir, fileName);
            file.createNewFile();
            log.append(LocalDateTime.now() + " Создан файл " + file.getName() + " по адресу " + file.getAbsolutePath() + separator);
        } catch (IOException exception) {
            log.append(LocalDateTime.now() + " Ошибка создания файла: " + exception.getMessage());
        }
    }

    public static void saveGame (String savepath, GameProgress progress) {
        File file = new File(savepath);
        try (FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(progress);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    public static void zipFiles(String zipPath, String[] fileList) {

             for (String file : fileList) {
                 try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipPath));
                     FileInputStream fis = new FileInputStream(file)) {
                     ZipEntry entry = new ZipEntry(file);
                     zout.putNextEntry(entry);
                     byte[] buffer = new byte[fis.available()];
                     fis.read(buffer);
                     zout.write(buffer);
                     zout.closeEntry();
                     System.out.println("Файл " + file + " закинули в архив");
                 } catch (IOException ex) {
                     System.out.println(ex.getMessage());
                 }



             }
        }
    }


