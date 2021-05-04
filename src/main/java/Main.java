import javafx.animation.AnimationTimer;
import javafx.application.Application;

import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.awt.*;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main extends Application {

    // queue
    private Stick stick;

    //billes actuellement dans le jeu
    private LinkedList<Circle>billes;

    //touches sur lesquels on appuye
    private ArrayList<String> keyPressed;

    //billes en mouvements
    private ArrayList<Circle> enMouvement;

    //billes en direct
    private ArrayList<Circle> aSupprimer;

    // boolean pour savoir si un coup est en cours et si la barre d'espace à déjà était appuyé ou pas
    private boolean espace;
    private boolean coup;

    //position de la souris a moment du coup
    private double posX,posY;
    //distance pointe de la queue-bille blanche
    private double distance;

    private int billeTotal;
    private int billeRestant;

    private Status status_jeu;



    //pour la partie pause du jeu
    Scene quitScene;
    Scene pauseScene;
    Scene settingsScene;
    Button resume;
    Button quit;
    Button settings;

    @Override
    public void start(Stage primaryStage) throws Exception{

        /*Jeu Information */
        this.status_jeu = Status.ENJEU;
        coup=true;
        espace=false;
        Plateau pl = new Plateau();
        Joueur joueur = new Joueur("Joueur 1");
        pl.setJoueur(joueur);
        /*Jeu Information */

        //pas de changement de taille de la fenêtre
        primaryStage.setResizable(false);
        //on crée la racine du jeu
        BorderPane root =new BorderPane();
        //on met le titre
        primaryStage.setTitle("Billard");
        //on initialise les boooleans
        coup=true;
        espace=false;

        BorderPane root =new BorderPane();
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
            private double timer_game = 0.006;

            @Override
            public void handle(long l) {
                if(status_jeu == Status.VICTOIRE) {
                    System.out.println(status_jeu.getStatut());
                    drawText(status_jeu.getStatut(),600, 150, 30, context);
                    //demandeJouer();
                }else if(status_jeu == Status.DEFAITE){
                    System.out.println(status_jeu.getStatut());
                    drawText(status_jeu.getStatut(),600, 150, 30, context);
                    // demandeJouer();
                }

                pl.render(context);
                miseajour(billes);
                drawText(status_jeu.getStatut(),600, 150, 30, context);
                drawText("Pseudo: " + pl.j.getPseudo(), 1200, 150, 20, context);
                drawText("Points: " + pl.j.getScore(), 1200, 200, 20, context);
                drawText( ("Boules: " + billeRestant+"Boules /"+billeTotal + " Boules"), 1200, 250, 20, context);

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

                    if(keyPressed.contains("ESCAPE")){
                        timer_game = 0.006;
                        System.out.println("j'ai cliké ça va xD ");
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
                                circle.update(timer_game);
                            }
                        }
                        if (enMouvement.isEmpty()) {
                            coup = true;
                            stick.setPos((int) billes.get(0).x, (int) billes.get(0).y);
                        }
                        //supression des billes qui sont tombé
                        for (Circle circle:aSupprimer) {
                            if(circle.id == 0){
                                status_jeu = Status.FINDEPARTIE;
                            }else{
                                ajoutPoint(pl.j, 150);
                            }
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
                        verificationVictoire();

                    }
                    miseajour(billes); //resetSpeed
                    miseajourBillesValeur();
                    resetSpeed();
                    }
            }

            public void resetSpeed(){
                timer_game = 0.006;
            }

            public void miseajour(LinkedList<Circle> c ) {
                for (Circle cercle : c) {
                    cercle.render(context);
                }
            }

            public void verificationVictoire(){
                if(circleBlancPresent()){
                    if(billes.size() == 1){
                        for(Circle c: billes){
                            if(c.id == 0){
                                status_jeu = Status.VICTOIRE;
                            }else{
                                status_jeu = Status.DEFAITE;
                            }
                        }
                    }
                }else{
                    status_jeu = Status.DEFAITE;
                }
            }

            public boolean circleBlancPresent(){
                for(Circle c: billes){
                    if(c.id == 0){
                        return true;
                    }
                }
                return false;
            }

            public void ajoutPoint(Joueur j, int points){
                j.setScore(j.getScore() + points);
            }

            public void demandeJouer(){
                Scanner sc = new Scanner(System.in);
                System.out.println("Voulez vous recommencer ? oui / non");
                String reponse = sc.nextLine();
                if(reponse.equals("oui") || reponse.equals("o")){
                    try {
                        startGame(context);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    System.exit(0);
                }
            }
            public void miseajourBillesValeur(){
                billeRestant = billes.size();
            }
        };
        gameloop.start();
        primaryStage.setScene(plateau);
        primaryStage.show();
    }

    public enum Status{
        MENU(""),
        ENJEU("Bonne chance et bon jeu"),
        VICTOIRE("Victoire, vous avez gagné !"),
        DEFAITE("Defaite, vous avez perdu !"),
        FINDEPARTIE("Fin de partie ressayez !");

        private String s;
        Status(String s){
            this.s = s;
        }
        public String getStatut(){
            return s;
        }

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
        //On retire -1 car on ne compte pas le boule blanchee
        billeTotal = billes.size()-1;
        billeRestant = billeTotal-1;
    }

    public void drawText(String s, int posX, int poxY, double size, GraphicsContext context){
        context.setFont(new Font("Arial", size));
        context.setFill(Color.WHITE);
        context.setLineWidth(3.0);
        context.fillText(s, posX, poxY);
    }
}