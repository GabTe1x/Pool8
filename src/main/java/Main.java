import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;

public class Main extends Application{

	Stage window;
	//menu principale
	Scene scene,scene2;
	private Menu menu;
	//la musique de fond
	private MediaPlayer mediaPlayer;
	//son du jeu
	private double volume;
	private boolean b;//monter ou baisser le son
	
	
	Stick stick;
    ArrayList<Circle>billes;
    ArrayList<String> keyPressed;
    boolean coup;
	
	public static void main(String[] args) {
		Greeter greeter = new Greeter();
		System.out.println(greeter.sayHello());
    	launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		window = primaryStage;
		//on initialise le son
		volume=1.0;
		coup=true;
		
		//on récupére la musique de fond et la joue en boucle jusqu'à la fin du programme
		Media m =new Media(new File("src/ressources/msc.wav").toURI().toString());
		mediaPlayer=new MediaPlayer(m);
		mediaPlayer.setOnEndOfMedia(new Runnable() {
			public void run() {
				mediaPlayer.seek(Duration.ZERO);
			}
		});
		
		
		//désactivation du changement de taille de la fenêtre
		window.setResizable(false);
		
		//initalisationn du titre de la fenêtre
		window.setTitle("PoolGame");
		
		//On récupère l'icone de la fenêtre puis on l'ajoute 
        InputStream s = Files.newInputStream(Paths.get("src/ressources/icone.png"));
        Image icon=new Image(s);
        s.close();
        window.getIcons().add(icon);
        
        //On crée le plateau
        Plateau pl = new Plateau();
        
        //On crée la racine de la fenètre et on fixe une taille de préfèrence
        Pane root=new Pane();
        root.setPrefSize(pl.width,pl.height);

        //on récupère l'image de background et on ajuste la taille
        InputStream is = Files.newInputStream(Paths.get("src/ressources/background.jpg"));
        Image bg=new Image(is);
        is.close();
        ImageView bgView=new ImageView(bg);
        bgView.setFitWidth(pl.width);
        bgView.setFitHeight(pl.height);

        //on initialise le menu
        menu = new Menu();

        //on ajoute le tout à la fenètre
        root.getChildren().addAll(bgView,menu);
        scene = new Scene(root);
        
        BorderPane plateau =new BorderPane();
        plateau.setBackground(new Background(new BackgroundFill(Color.DARKGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        Canvas canvas = new Canvas(pl.width,pl.height);
        GraphicsContext context = canvas.getGraphicsContext2D();
        plateau.setCenter(canvas);
        startGame(context);
        pl.render(context);
        scene2 = new  Scene(plateau);
        
        keyPressed = new ArrayList<>();

        plateau.setOnKeyPressed(
                (KeyEvent event)->
                {
                    String keyname = event.getCode().toString();
                    if(!keyPressed.contains(keyname))keyPressed.add(keyname);
                }
        );

        plateau.setOnKeyReleased(
                (KeyEvent event)->
                {
                    String keyname = event.getCode().toString();
                    if(keyPressed.contains(keyname))keyPressed.remove(keyname);
                }
        );
        AnimationTimer gameloop = new AnimationTimer() {
            @Override
            public void handle(long l) {
                pl.render(context);
                //process user input
                if(keyPressed.contains("LEFT"))stick.rotation-=1;
                if(keyPressed.contains("RIGHT"))stick.rotation+=1;
                if(keyPressed.contains("SPACE")){
                    if(coup && Math.abs(stick.r-20)<200)stick.r+=2;
                }
                if(!keyPressed.contains("SPACE")){
                    if(coup && stick.r-20>0)stick.r-=55;
                    if(coup && stick.r-20<=0){
                        stick.r=20;
                    }
                }



                //process game objects
                for (Circle circle:billes) {
                    for (Circle c:billes) {
                        if(circle.id!=c.id){
                            if(circle.overlap(c)) {
                                circle.collision(c);
                                circle.repulsion(c);
                            }
                        }
                    }
                    circle.update(1/60.0);
                    circle.render(context);
                    stick.render(context);
                }
            }
        };
        
        //on met en place la scene
        window.setScene(scene);
        //on montre la scene
        window.show();
        //on joue la musique
        mediaPlayer.play();
	}
	
	
	// Code pour les bouttons
	private class MenuButton extends StackPane{
        private Text text;

        public MenuButton(String name){
            text=new Text(name);
            text.setFont(text.getFont().font(25));
            text.setFill(Color.BLACK);

            //background du bouton
            Rectangle bg= new Rectangle(250,30);
            bg.setFill(Color.SKYBLUE);
            bg.setOpacity(0.7);
            bg.setEffect(new GaussianBlur(3.5));

            setAlignment(Pos.CENTER_LEFT);
            getChildren().addAll(bg,text);

            //event lorsque la souris passe au-dessus
            setOnMouseEntered(event ->{
                text.setFill(Color.BLUE);
                bg.setFill(Color.YELLOW);
            });
            
            //retour à la normal quand elle sort
            setOnMouseExited(event ->{
                bg.setFill(Color.SKYBLUE);
                text.setFill(Color.BLACK);
            });
        }
    }	
	
	// création du menu et initialisation des sous menu
	private class Menu extends Parent{
		
		public Menu(){
			// on crée les 3 menu disponible
            VBox menu = new VBox(10);
            VBox menu_options=new VBox(10);

            //on met leur position
            menu.setTranslateX(400);
            menu.setTranslateY(200);
            
            menu_options.setTranslateY(200);
            // pour qu'il n'apparaisse pas dans l'écran on l'affiche hors de celui-ci
            int offset = 800;
            menu_options.setTranslateX(offset);
            
            //mettra en place la scene pour la partie
            MenuButton play = new MenuButton("Play");
            play.setOnMouseClicked(e->{
            	buttonSound();
            	window.setScene(scene2);
            });
            MenuButton options = new MenuButton("Options");
            options.setOnMouseClicked(e->{
            	getChildren().add(menu_options);
            	buttonSound();
            	//animation transition menu basique et menu options
            	TranslateTransition f=new TranslateTransition(Duration.seconds(0.25),menu);
            	TranslateTransition ff=new TranslateTransition(Duration.seconds(0.5),menu_options);
            	
            	// création de l'animation
            	f.setToX(menu.getTranslateX()-offset);
            	ff.setToX(menu.getTranslateX());
            	//on joue l'animation
            	f.play();
            	ff.play();
            	f.setOnFinished(evt->{
            		getChildren().remove(menu);
            	});
            });
            
            MenuButton exit = new MenuButton("Exit");
            //Quitte le jeu
            exit.setOnMouseClicked(e->{
            	buttonSound();
            	System.exit(0);
            });
            MenuButton volumebtn = new MenuButton("Volume");
            volumebtn.setOnMouseClicked(e->{
            	buttonSound();
            	if(volume>=1) {
            		b=true;
            		volume-=0.2;
            	}else {
            		if(volume>0 && b)volume-=0.2;
            		else {
            			if(volume<0.1) {
            				volume+=0.2;
            				b=false;
            			}else volume+=0.2;
            	}}
            	System.out.println(volume);
            });
            
            // bouton musique
            MenuButton musique = new MenuButton("Musique");
            musique.setOnMouseClicked(e->{
            	buttonSound();
            	//désactive ou active le son de la musique selon la valeur du volume
            	if(mediaPlayer.getVolume()!=0)
            		mediaPlayer.setVolume(0);
            	else mediaPlayer.setVolume(volume);
            		
            });
            
            //bouton retour
            MenuButton back = new MenuButton("Retour");
            back.setOnMouseClicked(e->{
            	getChildren().add(menu);
            	buttonSound();
            	//animation de transition entre le menu basique et le menu options
            	TranslateTransition f=new TranslateTransition(Duration.seconds(0.25),menu_options);
            	TranslateTransition ff=new TranslateTransition(Duration.seconds(0.5),menu);
            	
            	// on crée l'animation
            	f.setToX(menu_options.getTranslateX()+offset);
            	ff.setToX(menu_options.getTranslateX());
            	
            	//on joue l'animation
            	f.play();
            	ff.play();
            	f.setOnFinished(evt->{
            		getChildren().remove(menu_options);
            	});            	
            });
            
            // on ajoute les boutons aux menus correspondant
            menu_options.getChildren().addAll(volumebtn,musique,back);
            menu.getChildren().addAll(play,options,exit);
            //puis on ajoute le menu de base au menu principale du jeu
            getChildren().addAll(menu);
		}
	}
	
	//fonction pour jouer un son à chauqe clic sur un boutton
	void buttonSound() {
		Media m =new Media(new File("src/ressources/btn.wav").toURI().toString());
		MediaPlayer z=new MediaPlayer(m);
		z.setVolume(volume);
		z.play();
	}
	public void startGame(GraphicsContext context)throws Exception{
        this.billes = new ArrayList<>();
        this.stick = new Stick(300,413);
        billes.add(new Circle(300 ,413,20,0));
        billes.add(new Circle(1022,413,20,1));
        billes.add(new Circle(1056,393,20,3));
        billes.add(new Circle(1056,433,20,2));
        billes.add(new Circle(1090,374,20,4));
        billes.add(new Circle(1090,413,20,5));
        billes.add(new Circle(1090,452,20,6));
        billes.add(new Circle(1126,354,20,7));
        billes.add(new Circle(1126,393,20,8));
        billes.add(new Circle(1126,433,20,9));
        billes.add(new Circle(1126,472,20,10));
        billes.add(new Circle(1162,335,20,11));
        billes.add(new Circle(1162,374,20,12));
        billes.add(new Circle(1162,413,20,13));
        billes.add(new Circle(1162,452,20,14));
        billes.add(new Circle(1162,491,20,15));
        for(Circle x:billes)x.render(context);
        stick.render(context);
    }
}