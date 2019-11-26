import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class MyFile {
    private static final String SEP = File.separator;

    private int ID;
    private File file;
    private String fileExtension;

    //constructors

    MyFile() {
    }

    MyFile(String path, String name, String extension) throws IOException {
        file = new File(path, name + "." + extension);
        file.createNewFile();
        fileExtension = extension;
        ID = -1;
    }

    public MyFile(File file) throws IOException {
        this.file = file;
        fileExtension = defineFileExtension();
        ID = -1;
    }

    public MyFile(File file, boolean continueIDCount) throws IOException {
        this.file = file;
        fileExtension = defineFileExtension();
    }

//methods

    static void copyMyFileManyTimes(int n, MyFile mf) throws IOException {
        while (n > 0) {
            mf.createCopyWithContaining();
            n--;
        }
    }

    /**
     * Method createCopyWithContaining creates a copy of MyFile object.
     * It's enhanced and modified version (ver 2) of this method. It's features are
     * concise and simplicity.
     * @return MyFile object myCopy
     * @throws IOException
     */
    MyFile createCopyWithContaining() throws IOException {

        Path sourceFilePath = this.file.toPath();

        Path newFilePath = Paths.get(sourceFilePath.getParent().toString() + SEP + this.defineFileNameWithoutExt()
                + ++ID + "." + this.getFileExtension());

        Path fileCopieded = Files.copy(sourceFilePath, newFilePath); //tmp == newFilePath
        if (fileCopieded != null) {
            System.out.println("File \"" + fileCopieded.toString() + "\" copied successfully");
        } else System.out.println("File \"" + fileCopieded.toString() + "\" cannot be renamed by coping");

        File dr = new File(fileCopieded.toString());

        MyFile myCopy = new MyFile(dr, true);

        myCopy.ID = this.ID + 1;
        return myCopy;
    }

    public File getFile() {
        return file;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    /**
     * Method defineFileExtension() used for
     * retrieving extension of a file
     * @return String file extension
     */
    String defineFileExtension() {
        String fileName = this.file.getName();
        int indexOfPoint = fileName.indexOf('.');
        String ext = "";
        ext = fileName.substring(indexOfPoint + 1);
        return ext;
    }

    /**
     * Method defineFileNameWithoutExt() used for retrieving file name
     * without extension and path
     * @return String file name
     */
    String defineFileNameWithoutExt() {
        String fileName = this.file.getName();
        int indexOfPoint = fileName.indexOf('.');
        String name = "";
        name = fileName.substring(0, indexOfPoint);
        return name;
    }

    public void setFile(File file) {
        this.file = file;
    }

}//end of MyFile class
