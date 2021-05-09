import javafx.geometry.Pos;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


public class StyledButton extends StackPane {
    private Text text;
    private int posX, posY;
    private Rectangle bg = new Rectangle(300, 55);

    public StyledButton(String name, int posX, int posY) {
        text = new Text(name);
        text.setFont(text.getFont().font(50));
        text.setFill(Color.BLACK);
        text.setTextAlignment(TextAlignment.CENTER);

        //background du bouton
        bg.setFill(Color.SKYBLUE);
        bg.setOpacity(0.8);
        bg.setEffect(new GaussianBlur(3.5));
        this.setLayoutX(posX);
        this.setLayoutY(posY);

        getChildren().addAll(bg, text);

        //event lorsque la souris passe au-dessus
        setOnMouseEntered(event -> {
            text.setFill(Color.BLUE);
            bg.setFill(Color.YELLOW);
        });

        //retour à la normal quand elle sort
        setOnMouseExited(event -> {
            bg.setFill(Color.SKYBLUE);
            text.setFill(Color.BLACK);
        });

    }
    public Text getText() {
        return text;
    }
    public void setText(Text text) {
        this.text = text;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void setBg(Rectangle bg) {
        this.bg = bg;
    }

    public Rectangle getBg() {
        return bg;
    }
    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }
}