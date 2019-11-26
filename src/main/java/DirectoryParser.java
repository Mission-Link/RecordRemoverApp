import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class DirectoryParser {
    private String pathToTargetDir;
    private LinkedHashMap<String, String> mapWithAudioFormats;

    //constructors
//    public DirectoryParser() {
//        mapWithAudioFormats = createMapWithAudioFormats(loadFileExtensionsFromFile());
//    }

    public DirectoryParser(String pathToTargetDir) {
        this.pathToTargetDir = pathToTargetDir;
        mapWithAudioFormats = createMapWithAudioFormats(loadFileExtensionsFromFile());
    }

    //methods

    /**
     * Method loadFileExtensionsFromFile() loads list of audio file extension
     * from resource folder. If resource folder is unavailable then method
     * uses a hard-coded list.
     *
     * @return (String) list of file extensions
     */
    private static String loadFileExtensionsFromFile() {
        String containing;
        String FS = File.separator;
        String path = "src" + FS + "main" + FS + "resources" + FS + "AudioExtensions";
        File file = new File(path);
        if (!file.exists()) {//check whether the file with audio formats exists
            System.out.println("File with list of Audio Extensions does not exist");
            System.out.println("Loading reserve list");
            containing =
                    ".3GA - 3GPP Audio FileAverage\n" +
                            ".AAC - Advanced Audio Coding FileVery Common\n" +
                            ".DAC - DAC Sound FileAverage\n" +
                            ".FLAC - Free Lossless Audio Codec FileCommon\n" +
                            ".M4A - MPEG-4 Audio LayerVery Common\n" +
                            ".MP3 - MP3 Audio FileVery Common\n" +
                            ".OGG - Ogg Vorbis Codec Compressed WAV FileCommon\n" +
                            ".WAV - Waveform AudioVery Common\n";
            return containing;
        }

        FileInputStream fis = null;
        byte[] brar = null;
        try {
            fis = new FileInputStream(file);
            brar = new byte[fis.available()];
            fis.read(brar);
        } catch (Exception e) {
            System.out.println("Impossible to read from file \"" + file.getAbsolutePath() + "\"");
            System.out.println(e.getMessage());
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                System.out.println("Impossible to close a file input stream");
                System.out.println(e.getMessage());
            }
        }
        assert brar != null;
        containing = new String(brar);
        return containing;
    }//end of method loadExtensions()

    /**
     * Method parseAudioFileDir() used for retrieving all
     * AUDIO files that stored inside a directory (default method)
     * (by path that defined by filed variable 'pathToTargetDir')
     * <p>
     * this is a modified method from Library class
     *
     * @return ArrayList<File> collection that stores
     * all discovered files
     */
    public ArrayList<File> parseAudioFileDir() {
        ArrayList<File> fileArrayList = new ArrayList<>();
        File file = new File(pathToTargetDir);
        if (!file.exists()) {
            System.out.println("The directory you wanna parse for audio files does not exist");
            return fileArrayList;
        }
        pfRec(fileArrayList, file, mapWithAudioFormats); //recursive worker method invocation
        return fileArrayList;
    }

    /**
     * Method parseAudioFileDirByPath() used for retrieving all
     * AUDIO files that stored inside a directory
     * <p>
     * this is a modified method from Library class
     *
     * @param pathToTargetDir (String) path to directory
     *                        with audio files
     * @return ArrayList<File> collection that stores
     * all discovered files
     */
    public ArrayList<File> parseAudioFileDirByPath(String pathToTargetDir) throws IOException {
        ArrayList<File> fileArrayList = new ArrayList<>();
        File file = new File(pathToTargetDir);
        if (!file.exists()) {
            System.out.println("The directory you wanna parse for audio files does not exist");
            System.out.println("Exit...");
            return fileArrayList;
        }
        pfRec(fileArrayList, file, mapWithAudioFormats); //recursive worker method invocation
        return fileArrayList;
    }

    //helper method for parseAudioFileDir() and parseAudioFileDirByPath()
    private static void pfRec(ArrayList<File> fileArrayList, File file,
                              LinkedHashMap<String, String> mapWithAudioFormats) { //throws IOException

        if (!file.isDirectory()) {//just a normal file (not dir)
            System.out.println("Processing file: \"" + file.getAbsolutePath() + "\"");
            String extension = defineFileExtension(file); //get file extension
            if (mapWithAudioFormats.containsKey(extension.toUpperCase())) {
                System.out.println("Recognized audio format: \'" + extension + "\' ,add file");
                fileArrayList.add(file);
            } else {
                System.out.println("Not an audio format: \'" + extension + "\'");
            }
        } else {//if file is a dir
            File[] fileRar = file.listFiles();
            assert fileRar != null;
            for (File tmp : fileRar) {
                if (!tmp.isDirectory()) {//if an ordinary file
                    System.out.println("Processing file: \"" + tmp.getAbsolutePath() + "\"");
                    String extension = defineFileExtension(tmp);
                    if (mapWithAudioFormats.containsKey(extension.toUpperCase())) {
                        System.out.println("Recognized audio format: \'" + extension + "\' ,add file");
                        fileArrayList.add(tmp);
                    } else {
                        System.out.println("Not an audio format: \'" + extension + "\'");
                    }
                } else {//if a directory then recursive call
                    System.out.println("Processing directory: \"" + tmp.getAbsolutePath() + "\"");
                    pfRec(fileArrayList, tmp, mapWithAudioFormats);
                }
            }
        }
    }//end of helper method

    //helper method to retrieve file extension
    private static String defineFileExtension(File file) {
        String fileName = file.getName();
        int indexOfPoint = fileName.lastIndexOf('.');
        String ext = "";
        ext = fileName.substring(indexOfPoint + 1);
        return ext;
    }

    private static LinkedHashMap<String, String> createMapWithAudioFormats(String listOfAudioFormats) {
        Pattern p2 = Pattern.compile("\\b(\\s+)(-)(\\s+)(\\b)");
        String[] stringRar = listOfAudioFormats.split("\n"); //removing new line esc char
        ArrayList<String[]> geo2 = new ArrayList<>();
        for (int i = 0; i < stringRar.length; i++) {
//            System.out.println(i + ") " + stringRar[i]);
            String[] moveIt = p2.split(stringRar[i]);
            geo2.add(new String[]{moveIt[0], moveIt[1]});
        }

        LinkedHashMap<String, String> hm = new LinkedHashMap<String, String>();
        for (String[] tmp : geo2) {
//            System.out.println(count + ")" + tmp[0] + " " + tmp[1]);
            hm.put(tmp[0].substring(1), tmp[1]); //substring is for removing space char
        }
        return hm;
    }

    public LinkedHashMap<String, String> getMapWithAudioFormats() {
        return mapWithAudioFormats;
    }

    public void printMapWithAudioFormats() {
        System.out.println("printing Map With Audio Formats: ");
        Set<Map.Entry<String, String>> set = mapWithAudioFormats.entrySet();
        for (Map.Entry<String, String> tmp : set) {
            System.out.println("key: " + tmp.getKey() + ", value: " + tmp.getValue());
        }
    }

}//end of class
