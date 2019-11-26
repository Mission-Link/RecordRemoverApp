import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

import java.io.File;
import java.util.ArrayList;

public class RecordRemover {
    private String pathToTargetDir;
    private ArrayList<File> fileArrayList;
    private ArrayList<File> fileArrayListToRemove;
    private int durationToRemove;
    private int removeCounter;
    private String logic; //not implemented yet

    //constructors

    public RecordRemover(String pathToTargetDir, int durationToRemove) {
        this.pathToTargetDir = pathToTargetDir;
        this.durationToRemove = durationToRemove;
        fileArrayList = (new DirectoryParser(pathToTargetDir).parseAudioFileDir());
        fileArrayListToRemove = null;
        removeCounter = 0;
    }

    public RecordRemover(String pathToTargetDir, String logic, int durationToRemove) {
        this.pathToTargetDir = pathToTargetDir;
        this.logic = logic;
        this.durationToRemove = durationToRemove;

        fileArrayList = (new DirectoryParser(pathToTargetDir).parseAudioFileDir());
        fileArrayListToRemove = null;
        removeCounter = 0;
    }

    //methods

    public void removeByDefault() {
        fileArrayListToRemove = new ArrayList<>();
        if(fileArrayList.size() == 0){
            System.out.println("There are no audio files in directory \""+pathToTargetDir+"\"");
            System.out.println("Exit...");
            return;
        }
        System.out.println("Collection with audio files has size: "
                + fileArrayList.size());
        System.out.println("-----------------------------");
        System.out.println("Files in directory \"" + pathToTargetDir + "\" are:");
        ArrayList<String[]> stringArrayList = new ArrayList<>();

        for (File tmp : fileArrayList) {
            long size = Library.calculateFileSize(tmp);
            int duration = getTrackDuration(tmp);
            System.out.println("File: \"" + tmp.getName() + "\" has size: " +
                    Library.parseCalculatedSize(size) + " and durationToRemove: " + duration);
            //checks whether the file less than desired duration
            if (duration < durationToRemove) {
                stringArrayList.add(new String[]{tmp.getName(), String.valueOf(duration)});
                fileArrayListToRemove.add(tmp);
            }
        }//enf of for each loop

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Audio files with desired duration less than \'"
                + 40 + "\' s are: ");
        for (String[] tmp : stringArrayList) {
            System.out.println(tmp[0] + " - " + tmp[1]);
        }
        //deleting files
        System.out.println("-----------------------------");
        System.out.println("Start deleting files: ");
        removeFiles(fileArrayListToRemove, removeCounter);
        System.out.println("Deleted " + removeCounter + " files");

    }//enf of removeByDefault() method

    private static void removeFiles(ArrayList<File> fileArrayListToRemove, int removedCounter) {
        assert fileArrayListToRemove.size() > 0;
        for (File tmp : fileArrayListToRemove) {
            if (tmp.exists()) {
                tmp.delete();
                if (tmp.exists()) {
                    System.out.println("impossible to delete \"" + tmp.getName() + "\"");
                } else {
                    System.out.println("Deleted successfully: \"" + tmp.getName() + "\"");
                    removedCounter += 1;
                }
            }
        }//enf of for each loop
    }//end of helper method removeFiles()

    //helper method that gets duration
    private static int getTrackDuration(File file) {
        int duration = 0;
        AudioFile audioFile = null;
        try {
            audioFile = AudioFileIO.read(file);
            duration = audioFile.getAudioHeader().getTrackLength();
        } catch (Exception e) {
            System.out.println("Impossible to get durationToRemove of a file: \"" +
                    file.getName() + "\"");
            System.out.println(e.getMessage());
        }
        return duration;
    }//end of method getDuration()

}//end of class
