# RecordRemoverApp
A java console utility that parses a given directory (by its path) and removes all audio files with duration less than specified. 
User provides two parameters:
-p stands for Path to target directory
-d stands for Duration (in seconds) of an audio file
Usage example: java -jar [ProgramName].jar -p "D:\folder1\folderWithAudio" -d [X]
  Path example: 
  If path to folder with audio files have a space somewhere then encapsulate it with quote characters(" "), like this:
  D:\folder1\folderWithAudio - this path can be passed even without quote characters
  "D:\folder1\folder unknown\folderWithAudio" - has a space between, must be passed with quotes
  Duration example:
  where X is a number with 1 (minimal) - 5 (maximum) character length, must not start by a zero
  001 - bad
  1 - good
  100500000 - the length is too big
  99999 - still legit



