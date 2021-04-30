import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;


public class Main extends Application{
	
	Stick stick;
    ArrayList<Circle>billes;
    ArrayList<String> keyPressed;
    boolean coup;
    GraphicsContext context;
    BorderPane plateau;
    Canvas canvas;
    Plateau pl;
    Scene scene;
    AnimationTimer gameloop;
	
	public static void main(String[] args) {
		Greeter greeter = new Greeter();
		System.out.println(greeter.sayHello());
    	launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Stage window = primaryStage;
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
        pl = new Plateau();
        
        //On crée la racine de la fenètre et on fixe une taille de préfèrence
        Pane root=new Pane();
        root.setPrefSize(pl.width,pl.height);
        
        
        plateau =new BorderPane();
        canvas = new Canvas(pl.width,pl.height);
        context = canvas.getGraphicsContext2D();
        scene = new  Scene(plateau);
        
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
        gameloop = new AnimationTimer() {
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
        gameloop.start();
        pl.render(context);
        startGame(context);
        //on met en place la scene
        window.setScene(scene);
        //on montre la scene
        window.show();
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