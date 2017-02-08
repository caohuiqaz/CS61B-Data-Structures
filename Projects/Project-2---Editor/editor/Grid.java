package editor;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.util.HashMap;

public class Grid 
{
    Pointer pointer;
    Line[] lines;
    String fontName;
    int fontSize;
    int lineCount;
    int topMargin;
    int bottomMargin;
    int leftMargin;
    int rightMargin;
    Group root;
    int characterHeight;
    int screenBound;
    int windowHeight;
	public class Pointer
	{
		private int xCoord;
		private int xPosition;
		private int yPosition;
		public Grid parent;
		
		public Pointer(int x, int y, Grid p)
		{
			xPosition = x;
            yPosition = y;
            parent = p;
            xCoord = getXCoord(x);
		}
		
		public int xCoord()
		{
			return xCoord;
		}
		 public int xPosition() 
		{
            return xPosition;
        }
        public int yPosition()
		{
            return yPosition;
        }
		 public void setxPosition(int x)
		{
            xPosition = x;
            xCoord = getXCoord(x);
        }
		public void setxNotCoord(int x)
		{
            xPosition = x;
        }
		public void incxPosition(int newX)
		{
            xPosition = xPosition + 1;
            xCoord = xCoord + newX;
        }
		public void setxCoord(int x)
		{
			xCoord = x;
		}
		public void setyPosition(int y)
		{
			yPosition = y;
		}
		public int getXCoord(int i)
		{
			int returnX = parent.leftMargin();
			for(int j=0; j<i; j++)
			{
				if(parent.lines()[yPosition()].content.containsKey(j))
				{
					returnX = returnX + parent.lines[yPosition].getPixelsAtPositon(j);
				}
			}
			return returnX;
		}
		
		public int getXPosition(int i)
		{
			int closesti = parent.leftMargin();
			int closestX = Math.abs(i - getXCoord(0));
			for(int j=i; j<=parent.lines[yPosition].contents.size();j++)
			{
				if(closedX > Math.abs(i - getXCoord(j)))
				{
					closedX = Math.abs(i - getXCoord(j));
					closesti = j;
				}
			}
			return closesti;
		}
		
	}
	public class Line
	{
		private int yPosition;
        private int yCoordinate;
        private HashMap<Integer, Text> contents;
        public Grid parent;
        private Text last;
		
		public Line(int y, Grid p)
		{
			parent = p;
			yPosition = y;
			Text dCharacterHeight = new Text(10,10,"a");
			dCharacterHeight.setFont(Font.font(parent.fontName(), parent.fontSize()));
			double dubCharacterHeight = dCharacterHeight.getLayoutBounds().getHeight();
			int characterHeight = (int) Math.round(dubCharacterHeight);
			yCoordinate = yPosition * characterHeight + parent.topMargin();
			contents = new Hashmap<>();
			last = null;
		}
		public int getyCoordinate() {
            return yCoordinate;
        }

        public int yPosition() {
            return yPosition;
        }
		public int getPixelsAtPositon(int i)
		{
			Text textobj = getTextAtPosition(i);
			if(textobj == null)
			{
				return 0;
			}
			else
			{
				textobj.setFont(Font.font(parent.fontName(), parent.fontSize()));
				double dubCharacterHeight = textobj.getLayoutBounds().getWidth();
				return (int) Math.round(dubCharacterWidth);
			}
		}
		public String getCharAtposition(int i)
		{
			Text textobj = getTextAtPosition(i);
			if(textobj == null)
			{
				return 0;
			}
			else
			{
				return textobj.getText();
			}		
		}
		public Text getTextAtPosition(int i)
		{
			if(contents.containsKey(i))
			{
				return contents.get(i);
			}
			else
			{
				return null;
			}
		}
		public void lineWrap(Line y)
		{
			int lastspace = 0;
			for(Integer key : y.contents.keySet())
			{
				if(y.contents.containsKey(key))
				{
					if(y.contents.get(key).getText().equals(" "))
					{
						lastspace = key;
					}
				}
			}
			if(lastspace == 0)
			{
				return;
			}
			else
			{
				for(int i = lastspace +1; i<=y.contents.size() -1; i++)
				{
					Text toMove = lines()[y.yPosition()].contents.remove(i);
					if(lines()[y.yPosition() + 1].contents.containsKey(0))
					{
						 lines()[y.yPosition() + 1].keyShift();
					}
					lines().[y.yPosition()+1].contents.put(0,toMove);
				}
			}
		}
		public void keyShift() 
		{
            for (int i = contents.size() - 1; i >= 0; i--) 
			{
                contents.put(i + 1, contents.get(i));
            }
        }
		public void Type(String characterTyped)
		{
			Text dCharacterWidth = new Text(10,10,characterTyped);
			dCharacterWidth.setFont(Font.font(parent.fontName(),parent.fontSize());
			double dubCharacterWidth = dCharacterWidth.getLayoutBounds().getWidth();
			int characterWidth = (int) Math.round(dubCharacterWidth);
			Text displayText = new Text(parent.pointer.xCoord(), yCoordinate,characterTyped);
			if(pointer.xCoord() + characterWidth > rightMargin())
			{
				Text newLineChar = new Text("\r\n");
				lines()[pointer.yPosition()].contents.put(pointer.xPosition(),newLineChar);
				pointer.setyPosition(yPosition() + 1);
                if (pointer.yPosition() == parent.lines().length - 1) 
				{
                    parent.resizeLines();
                }
                pointer.setxPosition(0);
                Type(characterTyped);
                return;
			}
			boolean willInstert = false;
			if(parent.lines()[pointer.yPosition()].contents.containsKey(pointer.xPosition()))
			{
				willInstert = true;
			}
			else
			{
				last = displayText;
			}
			Text oldText = parent.lines()[pointer.yPosition()].contents.put(pointer.xPosition(),displayText);
			pointer.incxPosition(characterWidth);
			if(willInstert)
			{
				insert(yPosition(), oldText, pointer.xPosition(), characterWidth, pointer.xCoord());
			}
			displayText.setTextOrigin(yPos.TOP);
			displayText.setFont(Font.font(fontName(), fontSize()));
			displayText.toFront();
			root.getChildren().add(displayText);
			renderAll();
		}
		
		public void TypeButDontRender(String characterTyped)
		{
			Text dCharacterWidth = new Text(10, 10, characterTyped);
            dCharacterWidth.setFont(Font.font(parent.fontName(), parent.fontSize()));
            double dubCharacterWidth = dCharacterWidth.getLayoutBounds().getWidth();
            int characterWidth = (int) Math.round(dubCharacterWidth);
			Text displayText = new Text(parent.pointer.xCoord(), yCoordinate, characterTyped);
            if (! parent.lines()[pointer.yPosition()].contents.containsKey(pointer.xPosition())) 
			{
                last = displayText;
            }
			if(pointer.xCoord() + characterWidth > rightMargin())
			{
				Text newLineChar = new Text("\r\n");
                lines()[pointer.yPosition()].contents.put(pointer.xPosition(), newLineChar);
                pointer.setyPosition(yPosition() + 1);
                if (pointer.yPosition() == parent.lines().length - 1) 
				{
                    parent.resizeLines();
                }
                pointer.setxPosition(0);
                TypeButDontRender(characterTyped);
                return;
			}
			parent.lines()[pointer.yPosition()].contents.put(pointer,xPosition(),displayText);
			ointer.incxPosition(characterWidth);
            displayText.setTextOrigin(VPos.TOP);
            displayText.setFont(Font.font(fontName(), fontSize()));
            displayText.toFront();
            root.getChildren().add(displayText);
		}
		
	}

}