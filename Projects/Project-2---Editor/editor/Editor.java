package editor;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Editor extends Application {
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 500;
	String textToDisplay = "";

    /** An EventHandler to handle keys that get pressed. */
    private class KeyEventHandler implements EventHandler<KeyEvent> {
        int textCenterX;
        int textCenterY;

        private static final int STARTING_FONT_SIZE = 20;
        private static final int STARTING_TEXT_POSITION_X = 250;
        private static final int STARTING_TEXT_POSITION_Y = 250;

        /** The Text to display on the screen. */
        private Text displayText = new Text(STARTING_TEXT_POSITION_X, STARTING_TEXT_POSITION_Y, "");
        private int fontSize = STARTING_FONT_SIZE;

        private String fontName = "Verdana";

        KeyEventHandler(final Group root, int windowWidth, int windowHeight) {
            //textCenterX = windowWidth / 2;
            //textCenterY = windowHeight / 2;
			textCenterX = 0;
			textCenterY = 0;
            // Initialize some empty text and add it to root so that it will be displayed.
            displayText = new Text(textCenterX, textCenterY, "");
            // Always set the text origin to be VPos.TOP! Setting the origin to be VPos.TOP means
            // that when the text is assigned a y-position, that position corresponds to the
            // highest position across all letters (for example, the top of a letter like "I", as
            // opposed to the top of a letter like "e"), which makes calculating positions much
            // simpler!
            displayText.setTextOrigin(VPos.TOP);
            displayText.setFont(Font.font(fontName, fontSize));

            // All new Nodes need to be added to the root in order to be displayed.
            root.getChildren().add(displayText);
        }

        @Override
        public void handle(KeyEvent keyEvent) {
            if (keyEvent.getEventType() == KeyEvent.KEY_TYPED) {
                // Use the KEY_TYPED event rather than KEY_PRESSED for letter keys, because with
                // the KEY_TYPED event, javafx handles the "Shift" key and associated
                // capitalization.
                String characterTyped = keyEvent.getCharacter();
                if (characterTyped.length() > 0 && characterTyped.charAt(0) != 8) {
                    // Ignore control keys, which have non-zero length, as well as the backspace
                    // key, which is represented as a character of value = 8 on Windows.
					textToDisplay = textToDisplay + characterTyped;
                    displayText.setText(textToDisplay);
					keyEvent.consume();
                }
				else if(characterTyped.charAt(0) == 8 && textToDisplay.length() != 0)
				{
					textToDisplay = textToDisplay.substring(0, textToDisplay.length() - 1);
					displayText.setText(textToDisplay);
					keyEvent.consume();
				}

                centerText();
            } else if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {
                // Arrow keys should be processed using the KEY_PRESSED event, because KEY_PRESSED
                // events have a code that we can check (KEY_TYPED events don't have an associated
                // KeyCode).
                KeyCode code = keyEvent.getCode();
                if (code == KeyCode.UP) {
                    fontSize += 5;
                    displayText.setFont(Font.font(fontName, fontSize));
                    centerText();
                } else if (code == KeyCode.DOWN) {
                    fontSize = Math.max(0, fontSize - 5);
                    displayText.setFont(Font.font(fontName, fontSize));
                    centerText();
                }
            }
        }

        private void centerText() {
            // Figure out the size of the current text.
            double textHeight = displayText.getLayoutBounds().getHeight();
            double textWidth = displayText.getLayoutBounds().getWidth();

            // Calculate the position so that the text will be centered on the screen.
            double textTop = textCenterY - textHeight / 2;
            double textLeft = textCenterX - textWidth / 2;

            // Re-position the text.
			// Window width is 500.
            displayText.setX(textCenterX);
            displayText.setY(textCenterY);

            // Make sure the text appears in front of any other objects you might add.
            displayText.toFront();
        }
    }

	
       
    @Override
    public void start(Stage primaryStage) {
        // Create a Node that will be the parent of all things displayed on the screen.
        Group root = new Group();
        // The Scene represents the window: its height and width will be the height and width
        // of the window displayed.
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT, Color.WHITE);

        // To get information about what keys the user is pressing, create an EventHandler.
        // EventHandler subclasses must override the "handle" function, which will be called
        // by javafx.
        EventHandler<KeyEvent> keyEventHandler =
                new KeyEventHandler(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        // Register the event handler to be called for all KEY_PRESSED and KEY_TYPED events.
        scene.setOnKeyTyped(keyEventHandler);
        scene.setOnKeyPressed(keyEventHandler);

        primaryStage.setTitle("Single Letter Display Simple");

        // This is boilerplate, necessary to setup the window where things are displayed.
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        if(args.length<1)
		{
			System.out.println("Expected usage: CopyFile <source filename> <destination filename>");
            System.exit(1);
		}
		String inputFilename = args[0];
		try {
            File inputFile = new File(inputFilename);
            // Check to make sure that the input file exists!
            if (!inputFile.exists()) {
                System.out.println(inputFilename
                    + " does not exist");
                return;
            }
			FileReader reader = new FileReader(inputFile);
            // It's good practice to read files using a buffered reader.  A buffered reader reads
            // big chunks of the file from the disk, and then buffers them in memory.  Otherwise,
            // if you read one character at a time from the file using FileReader, each character
            // read causes a separate read from disk.  You'll learn more about this if you take more
            // CS classes, but for now, take our word for it!
            BufferedReader bufferedReader = new BufferedReader(reader);

            int intRead = -1;
            // Keep reading from the file input read() returns -1, which means the end of the file
            // was reached.
            while ((intRead = bufferedReader.read()) != -1) {
                // The integer read can be cast to a char, because we're assuming ASCII.
                char charRead = (char) intRead;
               // readToDocument(charRead);
            }
//			parent.renderAll();

            // Close the reader and writer.
            bufferedReader.close();
		}
		catch (FileNotFoundException fileNotFoundException) {
            System.out.println("File not found! Exception was: " + fileNotFoundException);
        } catch (IOException ioException) {
            System.out.println("Error when copying; exception was: " + ioException);
        }
		launch(args);
    }
}