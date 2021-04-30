import javafx.animation.AnimationTimer;
import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.LinkedList;

public class Main extends Application {

    // queue
    Stick stick;

    //billes actuellement dans le jeu
    LinkedList<Circle>billes;

    //touches sur lesquels on appuye
    ArrayList<String> keyPressed;

    //billes en mouvements
    ArrayList<Circle> enMouvement;

    // boolean pour savoir si un coup est en cours et si la barre d'espace à déjà était appuyé ou pas
    boolean espace;
    boolean coup;

    //position de la souris a moment du coup
    double posX,posY;
    //distance pointe de la queue-bille blanche
    double distance;

    @Override
    public void start(Stage primaryStage) throws Exception{

        //pas de changement de taille de la fenêtre
        primaryStage.setResizable(false);
        //on crée la racine du jeu
        BorderPane root =new BorderPane();
        //on met le titre
        primaryStage.setTitle("Billard");
        //on initialise les boooleans
        coup=true;
        espace=false;

        //création du plateau
        Plateau pl = new Plateau();
        //création de la convas avec les dimensions de l'image du plateau
        Canvas bg = new Canvas(pl.width,pl.height);
        //on récupère le context de la canvas
        GraphicsContext context = bg.getGraphicsContext2D();
        //on la met au centre de la racine
        root.setCenter(bg);

        //on crée la scene avec la racine
        Scene plateau = new Scene(root);
        //on dessine le plateau
        pl.render(context);
        //on dessine les billes sur le plateau
        startGame(context);

        //iniatialisation des deux array
        keyPressed = new ArrayList<>();
        enMouvement = new ArrayList<Circle>();

        // on récupère les touches utilisées par le joueur
        plateau.setOnKeyPressed(
                (KeyEvent event)->
                {
                    String keyname = event.getCode().toString();
                    if(!keyPressed.contains(keyname))keyPressed.add(keyname);
                }
        );
        // on enlève les touches qui ne sont plus utilisées par le joueur
        plateau.setOnKeyReleased(
                (KeyEvent event)->
                {
                    String keyname = event.getCode().toString();
                    if(keyPressed.contains(keyname))keyPressed.remove(keyname);
                }
        );



        //traitement des mouvements de la souris pour les coups
        plateau.setOnMouseMoved(
                mouseEvent -> {
                    //on sauvegarde la position de la souris
                    posX= mouseEvent.getX();
                    posY= mouseEvent.getY();
                    //positions de la bille blanche
                    double x2=billes.get(0).x;
                    double y2=billes.get(0).y;
                    //traitement du dessins de la queue de billard
                    if(posX<x2 && posY<y2 ) {
                        TriangleRectangle t = new TriangleRectangle(posX, posY, x2, y2, posX, y2);
                        stick.rotation = (int) t.getAngle();
                    }
                    else if(posX>x2 && posY<y2 ) {
                        TriangleRectangle t = new TriangleRectangle(posX, posY, x2, y2, posX, y2);
                        stick.rotation = 180 -(int) t.getAngle();
                    }
                    else if(posX<x2 && posY>y2 ) {
                        TriangleRectangle t = new TriangleRectangle(posX, posY, x2, y2, posX, y2);
                        stick.rotation =360 - (int) t.getAngle();
                    }
                    else if(posX>x2 && posY>y2 ) {
                        TriangleRectangle t = new TriangleRectangle(posX, posY, x2, y2, posX, y2);
                        stick.rotation = 180 + (int) t.getAngle();
                    }
                }
        );

        AnimationTimer gameloop = new AnimationTimer() {
            @Override
            public void handle(long l) {
                pl.render(context);
                //process user input
                if(keyPressed.contains("SPACE")){
                    if(coup && Math.abs(stick.r-20)<200){
                        stick.r+=2;
                        distance=stick.r;
                    }
                    espace = true;
                }
                if(!keyPressed.contains("SPACE")){
                    if(coup && stick.r-20>0)stick.r-=55;
                    if(coup && stick.r-20<=0){
                        stick.r=20;
                        if(espace) {
                            billes.get(0).setDir((distance/5)*(billes.getFirst().x-posX),(distance/5)*(billes.getFirst().y-posY));
                            espace = false;
                            coup=false;
                        }
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
                    if(circle.enMouvement())enMouvement.add(circle);
                    else enMouvement.remove(circle);
                    if (circle.x-20 < 56) circle.x=1441-20;
                    if (circle.y+20 >= 768)circle.y=56+20;
                    if (circle.y-20 < 56)circle.y=768-20;
                    if (circle.x+20 >= 1441)circle.x=56+20;

                    circle.update(0.016);
                    circle.render(context);
                }
                if(enMouvement.isEmpty()){
                    coup=true;
                    stick.setPos((int)billes.get(0).x,(int)billes.get(0).y);
                }
                if(coup)stick.render(context);
            }
        };
        gameloop.start();
        primaryStage.setScene(plateau);
        primaryStage.show();
    }


    public static void main(String[] args) { launch(args);}

    public void startGame(GraphicsContext context)throws Exception{
        this.billes = new LinkedList<>();
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