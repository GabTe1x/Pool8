import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.TranslateTransition;

public class Main extends Application{
	
	private Menu menu;

	public static void main(String[] args) {
		Greeter greeter = new Greeter();
		System.out.println(greeter.sayHello());
    	launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("PoolGame");

        InputStream s = Files.newInputStream(Paths.get("src/main/ressources/icone.png"));
        Image icon=new Image(s);
        s.close();
        primaryStage.getIcons().add(icon);

        Pane root=new Pane();
        root.setPrefSize(1080,624);

        InputStream is = Files.newInputStream(Paths.get("src/main/ressources/background.jpg"));
        Image bg=new Image(is);
        is.close();
        ImageView bgView=new ImageView(bg);
        bgView.setFitWidth(1080);
        bgView.setFitHeight(624);

        menu = new Menu();

        root.getChildren().addAll(bgView,menu);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
		
	}
	private class MenuButton extends StackPane{
        private Text text;

        public MenuButton(String name){
            text=new Text(name);
            text.setFont(text.getFont().font(25));
            text.setFill(Color.BLACK);

            Rectangle bg= new Rectangle(250,30);
            bg.setFill(Color.SKYBLUE);
            bg.setOpacity(0.7);
            bg.setEffect(new GaussianBlur(3.5));

            setAlignment(Pos.CENTER_LEFT);
            getChildren().addAll(bg,text);

            setOnMouseEntered(event ->{
                text.setFill(Color.BLUE);
                bg.setFill(Color.YELLOW);
            });

            setOnMouseExited(event ->{
                bg.setFill(Color.SKYBLUE);
                text.setFill(Color.BLACK);
            });
        }
    }
	private class Menu extends Parent{
		
		public Menu(){
            VBox menu = new VBox(10);
            VBox menu_options=new VBox(10);

            menu.setTranslateX(400);
            menu.setTranslateY(200);

            menu_options.setTranslateX(400);
            menu_options.setTranslateY(200);

            int offset = 800;
            menu_options.setTranslateX(offset);

            MenuButton play = new MenuButton("Play");
            MenuButton options = new MenuButton("Options");
            options.setOnMouseClicked(e->{
            	getChildren().add(menu_options);
            	
            	TranslateTransition f=new TranslateTransition(Duration.seconds(0.25),menu);
            	TranslateTransition ff=new TranslateTransition(Duration.seconds(0.5),menu_options);
            	
            	f.setToX(menu.getTranslateX()-offset);
            	ff.setToX(menu.getTranslateX());
            	f.play();
            	ff.play();
            	f.setOnFinished(evt->{
            		getChildren().remove(menu);
            	});
            });
            
            MenuButton exit = new MenuButton("Exit");
            exit.setOnMouseClicked(e->{
            	System.exit(0);
            });
            MenuButton volume = new MenuButton("Volume");
            MenuButton musique = new MenuButton("Musique");
            MenuButton back = new MenuButton("Retour");
            back.setOnMouseClicked(e->{
            	getChildren().add(menu);
            	
            	TranslateTransition f=new TranslateTransition(Duration.seconds(0.25),menu_options);
            	TranslateTransition ff=new TranslateTransition(Duration.seconds(0.5),menu);
            	
            	f.setToX(menu_options.getTranslateX()+offset);
            	ff.setToX(menu_options.getTranslateX());
            	f.play();
            	ff.play();
            	f.setOnFinished(evt->{
            		getChildren().remove(menu_options);
            	});
            });
            
            menu_options.getChildren().addAll(volume,musique,back);
            menu.getChildren().addAll(play,options,exit);
            getChildren().addAll(menu);
		}
	}
}