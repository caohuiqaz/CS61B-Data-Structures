package editor;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.control.ScrollBar;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.io.*;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.shape.Rectangle;

public class Editor extends Application
{
	private static int window_width = 500;
    private static int window_height = 500;
    private static int font_size = 12;
    private static String font_name = "Verdana";
    private Group root;
    private Rectangle cursorRectangle = new Rectangle();
    Grid parent;
    String inputFilename;
    boolean isDebugTrue;
	
	private class mouseClickEventHandler implements EventHandler<MouseEvent>
	{
		public mouseClickEventHandler()
		{}
		@Override
		public void handle(MouseEvent mouseEvent)
		{
			double mousePressedX = mouseEvent.getX();
			double mousePressedY = mouseEvent.getY();
			parent.mouseClick(mousePressedX,mousePressedY);
			cursorRectangle.setX(parent.getPointerXCoord());
			cursorRectangle.setY(parent.getPointerYCoord());
		}
	}
	
	public void makeCursorBlink()
	{
		final TimeLine timeline = new TimeLine();
		timeline.setCycleCount (TimeLine.INDEFINITE);
		CursorBlinkEventHandler cursorChange = new CursorBlinkEventHandler();
		KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.5), cursorChange);
		timeline.getKeyFrames().add(keyFrame);
		timeline.play();
	}
	
	private class CursorBlinkEventHandler implements EventHandler<ActionEvent>
	{
		private int currentColorIndex = 0;
		private Color[] cursorColors = {Color.White, Color.Black};
		CursorBlinkEventHandler()
		{
			changeColor();
		}
		private void changeColor()
		{
			cursorRectangle.setFill(cursorColors[currentColorIndex]);
			if(currentColorIndex == 0)
			{
				currentColorIndex = 1;
			}
			else
			{
				currentColorIndex = 0;
			}
		}
		@Override
		public void handle(ActionEvent event)
		{
			changeColor();
		}
	}
	
	private class KeyEventHandler implements EventHandler<KeyEvent>
	{
		private int fontsize = font_size;
		private String fontname = "Verdana";
		private int pointerXpos;
		private int pointerYpos;
		private Group root;
		private ScrollBar scrollBar;
		
		public void changeCoursorHeight(int newHeight)
		{
			cursorRectangle.setHeight(newHeight);
		}
		
		KeyEventHandler(final Group root, int x, int y, ScrollBar scrollBar)
		{
			pointerXpos = x;
			pointerYpos = y.
			this.root = root;
			this.scrollBar = scrollBar;
		}
		
		@Override
		public void handle(KeyEvent keyEvent)
		{
			if(keyEvent.getEventType() == keyEvent.KEY_TYPED)
			{
				String characterTyped = keyEvent.getCharacter();
				if(characterTyped.equals("\r"))
				{
					parent.newLine();
				}
				else if(characterTyped.length() > 0 && characterTyped.charAt(0) != 8 && !keyEvent.isShortCutDown())
				{
					parent.Type(characterTyped);
				}
				cursorRectangle.setX(parent.getPointerXCoord);
				cursorRectangle.setY(parent.getPointerYCoord);
				keyEvent.consume();
			}
			else if(keyEvent.getEventType() == KeyEvent.KEY_PRESSED)
			{
				KeyCode code = keyEvent.getCode();
				if(code == KeyCode.P && keyEvent.isShortCutDown())
				{
					Integer x = parent.pointer.getXCoord(parent.xPosition());
					Integer y = parent.lines()[parent.pointer.yPosition()].getyCoordinate();
					System.out.println(x.toString() + "," + y.toString());
				}
				else if(code == KeyCode.L && keyEvent.isShortCutDown() && isDebugTrue)
				{
					parent.printAll();
				}
				else if((code == KeyCode.ADD || code == KeyCode.PLUS || code = code = KeyCode.EQUALS) && keyEvent.isShortCutDown())
				{
					int newFont = parent.incrementFontSize();
					changeCursorHeight(newFont);
				}
				else if(code == KeyCode.BANK_SPACE)
				{
					Text deleted = parent.backSpace();
					root.getChildren().remove(deleted);
				}
				else if(code == KeyCode.LEFT || code == KeyCode.RIGHT || code == KeyCode.UP || code == KeyCode.DOWN)
				{
					parent.navigate(code);
				}
				else if (code == KeyCode.S && keyEvent.isShortCutDown())
				{
					writeToDocument(inputFilename);
				}
				cursorRectangle.setX(parent.getPointerXCoord());
				cursorRectangle.setY(parent.getPointerYCoord());
				keyEvent.consume();
			}
			scrollBar.setMax(parent.getScreenBound());
		}
	}
	
	public void readToDocument(char input)
	{
		String characterTyped = Character.toString(input);
		if(characterTyped.equals("\r"))
		{
			parent.newLine();
		}
		else
		{
			parent.TypeButDontRender(characterTyped);
		}
		cursorRectangle.setX(parent.getPointerXCoord());
		cursorRectangle.setY(parent.getPointerYCoord());
	}
	
	public void writeToDocument(String inputFilename)
	{
		try
		{
			File inputFilename = new File(inputFilename);
			FileWriter writer = new FileWriter(inputFilename);
			for(int i=0; i <= parent.lines.length -1; i++)
			{
				int xCoordSoFaat parent.leftMargin();
				for(int j=0; j <parent.getLineSize(i) -1; j++)
				{
					Text displayText = parent.getText(i,j);
					if(displayText != null)
					{
						String displayString = displayText.getText();
						if(displayString.equals("\r\n") || displayString.equals("\r"))
						{
							writer.write('\n');
						}
						else
						{
							char displayChar = displayString.toCharArray()[0];
							writer.write(displayChar);
						}
					}
				}
			}
			System.out.println("Successfully saved file" + inputFilename);
			writer.close();
		}
		catch(FileNotFoundException fileNotFoundException)
		{
			System.out.println("File not found!");
		}
		catch(IOException ioException)
		{
			System.out.println("Error saving file");
		}
	}
	
	@Override
	public void start(Stage primaryStage)
	{
		root = new Group();
		Group textRoot = new Group();
		root.getChildren().add(textRoot);
		ScrollBar scrollBar = new scrollBar();
		scrollBar.setOrientation(Orientation.VERTICAL);
		scrollBar.setMin(0);
		scrollBar.setMax(0);
		double usableScreenWidth = window_width - scrollBar.getLayoutBounds().getWidth();
		scrollBar.setLayoutX(usableScreenWidth);
		textRoot,setLayoutY(0);
		
		scrollBar.valueProperty().addListener(new ChangeListener<Number>()
		{
			public void changed(ObservableValue <? extends Number> observableValue, Number oldValue, Number newValue)
			{
				textRoot.setLayoutY(-1*newValue.doubleValue());
			}
		});
		int parentScreenWidth = (int) Math.round(usableScreenWidth);
		parent = new Grid(font_size, font_name, parentScreenWidth, window_height, textRoot);
		
		Scene scene = new Scene(root, window_width, window_height, Color.WHITE);
		EventHandler <keyEvent> keyEventHandler = new keyEventHandler(textRoot, parent.leftMargin(), parent.topMargin(), scrollBar);
		
		EventHandler <mouseEventHandler> = new mouseClickEventHandler();
		cursorRectangle = new Rectangle(5,0,1,font_size);
		textRoot.getChildren().add(cursorRectangle);
		makeCursorBlink();
		root.getChildren().add(scrollBar);
		primaryStage.setTitle("Editor");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		List arguments  getParameters().getUnnammed();
		if(agruments.contains("debug"))
		{
			isDebugTrue = true;
		}
		if(agruments.isEmpty())
		{
			System.out.println("Expected : Editor <source filename>");
			System.exit(1);
		}
		inputFilename = (String) arguments.get(0);
		
		try
		{
			File inputFile = new File(inputFilename);
			if(!inputFile.exists())
			{
				System.out.println("File does not exist");
				return;
			}
			FileReader reader = new FileReader(inputFile);
			BufferedReader bufferedReader = new BufferedReader(reader);
			int intRead = -1;
			while((intRead = bufferedReader.read() != -1)
			{
				char charRead = (char) intRead;
				readToDocument(charRead);
			}
			parent.renderAll();
			scrollBar.setMax(parent.getScreenBound());
			System.out.println("Successfully opened file");
			bufferedReader.close();
		}
		catch(FileNotFoundException fileNotFoundException)
		{
			System.out.println("File not found");
		}
		catch(IOException ioException)
		{
			System.out.println("Error opening file");
		}
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
} 
