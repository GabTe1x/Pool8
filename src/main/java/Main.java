import javafx.animation.AnimationTimer;
import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.awt.*;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
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

    //billes en direct
    ArrayList<Circle> aSupprimer;

    // boolean pour savoir si un coup est en cours et si la barre d'espace à déjà était appuyé ou pas
    boolean espace;
    boolean coup;

    //position de la souris a moment du coup
    double posX,posY;
    //distance pointe de la queue-bille blanche
    double distance;

    //pour la partie pause du jeu
    Scene quitScene;
    Scene pauseScene;
    Scene settingsScene;
    Button resume;
    Button quit;
    Button settings;

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
        aSupprimer = new ArrayList<>();



        //les scenes pour la partie pause
        Group panePauseScene = new Group();
        resume = new Button("Reprendre la partie");
        resume.setPadding(new Insets(10,10,10,10));
        resume.prefHeight(30);
        resume.setPrefWidth(250);
        resume.setLayoutX(500);
        resume.setLayoutY(200);
        resume.setOnAction(e -> primaryStage.setScene(plateau));

        quit = new Button("Quitter la partie");
        quit.setPadding(new Insets(10,10,10,10));
        quit.prefHeight(30);
        quit.setPrefWidth(250);
        quit.setLayoutX(500);
        quit.setLayoutY(250);
        quit.setOnAction(e -> primaryStage.close());

        settings = new Button("Réglages");
        settings.setPadding(new Insets(10,10,10,10));
        settings.prefHeight(30);
        settings.setPrefWidth(250);
        settings.setLayoutX(500);
        settings.setLayoutY(300);
        settings.setOnAction(e -> primaryStage.setScene(settingsScene));

        panePauseScene.getChildren().addAll(resume,settings,quit);
        pauseScene = new Scene (panePauseScene, pl.width,pl.height);

        // on récupère les touches utilisées par le joueur
        // le bouton 'esc' permet de mettre le jeu en pause
        plateau.setOnKeyPressed(
                (KeyEvent event)->
                {
                    if (event.getCode()== KeyCode.ESCAPE){
                        primaryStage.setScene(pauseScene);
                    }
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

        plateau.setOnMouseClicked(
                (MouseEvent event)->
                {
                    System.out.println(event.getX()+""+event.getY());
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
                        stick.rotation =360- (int) t.getAngle();
                    }
                    else if(posX>x2 && posY>y2 ) {
                        TriangleRectangle t = new TriangleRectangle(posX, posY, x2, y2, posX, y2);
                        stick.rotation = 180+(int) t.getAngle();
                    }
                }
        );

        AnimationTimer gameloop = new AnimationTimer() {
            @Override
            public void handle(long l) {
                pl.render(context);
                if(!billes.isEmpty()) {
                    //process user input
                    if (keyPressed.contains("SPACE")) {
                        if (coup && Math.abs(stick.r - 20) < 200) {
                            stick.r += 2;
                            distance = stick.r;
                        }
                        espace = true;
                    }
                    if (!keyPressed.contains("SPACE")) {
                        if (coup && stick.r - 20 > 0) stick.r -= 55;
                        if (coup && stick.r - 20 <= 0) {
                            stick.r = 20;
                            if (espace) {
                                billes.get(0).setDir((distance / 5) * (billes.getFirst().x - posX), (distance / 5) * (billes.getFirst().y - posY));
                                espace = false;
                                coup = false;
                            }
                        }
                    }

                    //process game objects
                    if (coup) stick.render(context);
                    for (int i = 0; i < 4; i++) {
                        for (Circle circle : billes) {
                            for (Circle c : billes) {
                                if (circle.id != c.id) {
                                    if (circle.overlap(c)) {
                                        circle.collision(c);
                                        circle.repulsion(c);
                                    }
                                }
                            }
                            if (circle.enMouvement()) enMouvement.add(circle);
                            else enMouvement.remove(circle);
                            if (circle.testCollision()) aSupprimer.add(circle);
                            if(!aSupprimer.contains(circle)) {
                                circle.update(0.004);
                                circle.render(context);
                            }
                        }
                        if (enMouvement.isEmpty()) {
                            coup = true;
                            stick.setPos((int) billes.get(0).x, (int) billes.get(0).y);
                        }
                        //supression des billes qui sont tombé
                        for (Circle circle:aSupprimer) {
                            billes.remove(circle);
                        }
                        //si la bille blanche est tombé on la remet en jeu
                        if(billes.getFirst().id!=0){
                            try {
                                billes.addFirst(new Circle(300 ,413,20,0));
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        //on reset la list
                        aSupprimer=new ArrayList<>();
                    }
                }
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