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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import javafx.scene.shape.Rectangle;
import javafx.scene.media.MediaPlayer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.animation.AnimationTimer;
import javafx.application.Application;


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


    private Status status_jeu;



// pour le menu d'acceuil du jeu
    Scene settingsScene;
    Scene menu;
    StyledButton play;
    StyledButton settingsOptions;
    StyledButton back;
    StyledButton leave;


    MediaPlayer backgroundMusic;
    MediaPlayer clickSound;

    private double volume;
    private boolean b;//monter ou baisser le son

    //pour la partie pause du jeu
    Scene pauseScene;
    Scene settingsScenePause;
    StyledButton resume;
    StyledButton music;
    StyledButton quit;
    StyledButton settings;
    StyledButton backPause;


   
    @Override
    public void start(Stage primaryStage) throws Exception{

        /*Jeu Information */
        this.status_jeu = Status.ENJEU;
        coup=true;
        espace=false;
        // creation de 2 joueurs

        Joueur joueur1 = new Joueur("Joueur 1");
        Joueur joueur2 = new Joueur("Joueur 2");

        //initialisation du Plateau
        Plateau pl = new Plateau(joueur1,joueur2);

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



        //la partie pause du jeu
        MenuAccueil menuImagePause = new MenuAccueil();
        Image imgPause = menuImagePause.chargerImage();
        ImageView imgVPause = new ImageView(imgPause);
        Group paneScenePause = new Group();
        paneScenePause.getChildren().add(imgVPause);
        resume = new StyledButton("Reprendre la partie",500,250);
        resume.getText().setFont(resume.getText().getFont().font(25));
        resume.setOnMouseClicked(e -> primaryStage.setScene(plateau));

        //option avec jeu en pause

        MenuAccueil menuImageOptionPause = new MenuAccueil();
        Image imgOptionPause = menuImageOptionPause.chargerImage();
        ImageView imgVOptionPause = new ImageView(imgOptionPause);
        Group paneOptionScenePause = new Group();
        paneOptionScenePause.getChildren().add(imgVOptionPause);
        settingsScenePause = new Scene (paneOptionScenePause, pl.width,pl.height);
        settings = new StyledButton("Réglages",500,325);
        settings.getText().setFont(settings.getText().getFont().font(25));
        settings.setOnMouseClicked(e -> primaryStage.setScene(settingsScenePause));
        music = new StyledButton("Musique",500,250);
        backPause = new StyledButton("Retour",500,325);
        backPause.setOnMouseClicked(e -> primaryStage.setScene(plateau));
        paneOptionScenePause.getChildren().addAll(backPause,music);
        quit = new StyledButton("Quitter la partie",500,400);
        quit.getText().setFont(quit.getText().getFont().font(25));
        quit.setOnMouseClicked(e -> primaryStage.close());
        paneScenePause.getChildren().addAll(resume,settings,quit);
        pauseScene = new Scene (paneScenePause, pl.width,pl.height);


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
                //Aficher le text (Victoire , Defaite , Fin Partie )
                if(status_jeu == Status.VICTOIRE) {
                    drawText(status_jeu.getStatut(),600, 150, 30, context);
                }else if(status_jeu == Status.DEFAITE){
                    drawText(status_jeu.getStatut(),600, 150, 30, context);
                }

                pl.render(context);
                miseajour(billes);
                misajourAffichage();

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
                                circle.update(timer_game);
                            }
                        }

                        if (enMouvement.isEmpty()) {
                            coup = true;
                            stick.setPos((int) billes.get(0).x, (int) billes.get(0).y);
                            pl.changementJoueur();
                        }
                        System.out.println(enMouvement.size() + "test");

                        //supression des billes qui sont tombé
                        for (Circle circle:aSupprimer) {
                            if(circle.id != 0){
                                //Joueur 1 < - Bleu
                                //Joueur 2 < - Rouge
                                if(pl.courant == pl.joueur1 ) {
                                    if (circle.id % 2 == 0 && circle.id != 0) {
                                        retirerPoint(pl.courant, 200);
                                    } else {
                                        ajoutPoint(pl.courant, 150);
                                    }
                                }else{
                                    if (circle.id % 2 != 0 && circle.id != 15) {
                                        retirerPoint(pl.courant, 200);
                                    } else {
                                        ajoutPoint(pl.courant, 150);
                                    }
                                }
                            }
                            billes.remove(circle);
                        }
                        // context <- Graphic 2D Canvas
                        //si la bille blanche est tombé on la remet en jeu
                        if(billes.getFirst().id!=0){
                            try {
                                enMouvement.clear();
                                billes.addFirst(new Circle(300 ,413,20,0));
                                stick.setPos((int) billes.get(0).x, (int) billes.get(0).y);
                                stick.render(context);
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        //on reset la list
                        aSupprimer=new ArrayList<>();
                        verificationVictoire();

                    }
                    miseajour(billes); //resetSpeed
                    resetSpeed();
                    }
            }

            public void resetSpeed(){
                timer_game = 0.006;
            }

            /**
             *Cette fonction mets à jour les billes
             *
             * */
            public void miseajour(LinkedList<Circle> c ) {
                for (Circle cercle : c) {
                    cercle.render(context);
                }
            }

            /**
             *Cette fonction  change de statut du Jeu en fonction du nombre de billes restantes
             *et compare le score de deux joueurs pour attribue un gagnant
             * */
            public void verificationVictoire(){
                if(circleBlancPresent()){
                    if(billes.size() == 1){
                        for(Circle c: billes){
                            if(c.id == 0) {
                                status_jeu = Status.VICTOIRE;
                                if (pl.joueur1.getScore() > pl.joueur2.getScore()) {
                                    drawText("Joueur " + pl.joueur1.getPseudo() + " avec " + pl.joueur1.getScore(), 600, 300, 18, context);
                                } else {
                                    drawText("Joueur " + pl.joueur2.getPseudo() + " avec " + pl.joueur2.getScore(), 600, 300, 18, context);
                                }
                            }else if (c.id==15){
                                status_jeu=Status.DEFAITE;
                            }else{
                                status_jeu = Status.DEFAITE;
                            }
                        }
                    }
                }else{
                    status_jeu = Status.DEFAITE;
                }
            }

            /**
            *Cette fonction renvoie la présence de la bille blanche sur le Plateau
            * @return boolean
            * */
            public boolean circleBlancPresent(){
                for(Circle c: billes){
                    if(c.id == 0){
                        return true;
                    }
                }
                return false;
            }
            /**
             *Cette fonction ajoute de points au score du Joueur
             * @param Joueur j <- c'est un objet Joueur
             * @param int points
             * */
            public void ajoutPoint(Joueur j, int points){
                j.setScore(j.getScore() + points);
            }

            /**
             *Cette fonction retire de points au score du Joueur
             * @param Joueur j <- c'est un objet Joueur
             * @param int points
             * */
            public void retirerPoint(Joueur j, int points){
                if (j.getScore()>=points){
                    j.setScore(j.getScore() - points);
                }else{
                    j.setScore(0);
                }

            }

            /**
             *Cette fonction renvoie le nombre de boules rouges au Plateau
             * on cherche le nombre de boules avec leur id et la c'est des boules rouge
             * donc avec leur id paire et on doit exclure l'id = 0 car
             * c'est l'id de la boule blanche
             * @return int  c'est le nombre de boules rouges au plateau
             * */
            public int getBouleRougeRestant(){
                int boule = 0;
                for (Circle c :billes){
                    if ( c.id %2 == 0 && c.id !=0 ){
                        boule++;
                    }
                }
                return boule;
            }

            /**
             *Cette fonction renvoie le nombre de boules bleus au Plateau
             * on cherche le nombre de boules avec leur id et la c'est des boules
             * impaire et on doit exclure l'id = 15 car c'est l'id de la boule
             * noir
             * @return int  c'est le nombre de boules bleu au plateau
             * */
            public int getBouleBleuRestant(){
                int boule = 0;
                for (Circle c :billes){
                    if ( c.id %2 != 0 && c.id != 15 ){
                        boule++;
                    }
                }
                return boule;
            }

            /**
             *Cette fonction mets à jour l'affichage du texte
             * */
            public void misajourAffichage(){
                drawText(status_jeu.getStatut(),600, 150, 30, context);
                drawRectangle(context,230 , 30,630,160);
                drawRectangle(context,160 , 100,145,150);
                drawRectangle(context,165 , 100,1195,150);
                drawText("Pseudo: " + pl.joueur1.getPseudo(), 150, 180, 20, context);
                drawText("Points: " + pl.joueur1.getScore(), 150, 200, 20, context);
                drawText( ( getBouleBleuRestant()+" Boules Bleus "), 150, 220, 20, context);
                drawText("Pseudo: " + pl.joueur2.getPseudo(), 1200, 180, 20, context);
                drawText("Points: " + pl.joueur2.getScore(), 1200, 200, 20, context);
                drawText( (getBouleRougeRestant() + " Boules rouges"), 1200, 220, 20, context);
                drawText( "Au tour de : " + pl.courant.getPseudo(), 650, 180, 20, context);
            }

        };
        //pour la partie accueil
        Group paneMenuScene = new Group();
        MenuAccueil menuImage = new MenuAccueil();
        Image imgAccueil = menuImage.chargerImage();
        ImageView imgVAcceuil = new ImageView(imgAccueil);
        paneMenuScene.getChildren().add(imgVAcceuil);
        play = new StyledButton("Jouer",500,250);
        play.setOnMouseClicked(e->
                {
                    primaryStage.setScene(plateau);
                    gameloop.start();
                }
                );
        // pour la partie option dans le menu d'acceuil
        settingsOptions = new StyledButton("Options", 500,325);
        MenuAccueil menuImageOption = new MenuAccueil();
        Image imgOption = menuImageOption.chargerImage();
        ImageView imgVOption = new ImageView(imgOption);
        Group paneOptionScene = new Group();
        paneOptionScene.getChildren().add(imgVOption);
        settingsScene = new Scene (paneOptionScene, pl.width,pl.height);
        settingsOptions.setOnMouseClicked(e -> primaryStage.setScene(settingsScene));


        menu= new Scene (paneMenuScene, pl.width,pl.height);
        menu.setOnKeyPressed(
                (KeyEvent event)->
                {
                    if (event.getCode() == KeyCode.ESCAPE) {
                        primaryStage.close();
                    }
                }
        );



        back = new StyledButton("Retour",500,250);
        back.setPadding(new Insets(10,10,10,10));
        back.prefHeight(30);
        back.setPrefWidth(250);
        back.setLayoutX(500);
        back.setLayoutY(500);
        back.setOnMouseClicked(e -> primaryStage.setScene(menu));
        paneOptionScene.getChildren().add(back);
        //fin partie option

        leave = new StyledButton("Quitter",500, 400);
        leave.setOnMouseClicked(e -> primaryStage.close());
        paneMenuScene.getChildren().addAll(play,settingsOptions,leave);

        //fin partie acceuil

        primaryStage.setScene(menu);
        primaryStage.show();

    }

    /**
     *Cette sous-classe enumeration donne un statut de jeu du Plateau
     *en fonction de l'enumeration le statut de la partie change
     *
     * Par exemple : Si le jeu est en Victoire alors on verifie le statut et
     * on annonce un gagnant
     * Si c'est Menu c'est a dire que l'utilisateur est sur le Menu
     * etc
     * */
    public enum Status{
        MENU(""),
        ENJEU("Bonne chance et bon jeu"),
        VICTOIRE("Victoir, vous avez gagné !"),
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
        billes.add(new Circle(1090,452,20,7));
        billes.add(new Circle(1126,354,20,9));
        billes.add(new Circle(1126,393,20,6));
        billes.add(new Circle(1126,433,20,11));
        billes.add(new Circle(1126,472,20,8));
        billes.add(new Circle(1162,335,20,10));
        billes.add(new Circle(1162,374,20,13));
        billes.add(new Circle(1162,413,20,12));
        billes.add(new Circle(1162,452,20,14));
        billes.add(new Circle(1162,491,20,15));

        for(Circle x:billes)x.render(context);
        stick.render(context);
    }
    /**
     *Cette fonction affiche du texte
     * @param String message a afficher
     * @param int posX position x  sur le plateau
     *  @param int posY position y  sur le plateau
     * @param double size la taille d'ecriture
     * @param context c'est le graphique de la canvas
     * */
    public void drawText(String s, int posX, int poxY, double size, GraphicsContext context){
        context.setFont(new Font("Arial", size));
        context.setFill(Color.WHITE);
        context.setLineWidth(3.0);
        context.fillText(s, posX, poxY);
    }

    /**
     *Cette fonction affiche du texte
     * @param int posX position x  sur le plateau
     *  @param int posY position y  sur le plateau
     * @param int largeur taille  en largeur
     * @param int hauteur taille  en hauteur
     * @param context c'est le graphique de la canvas
     * */
    private void drawRectangle(GraphicsContext context,int largeur , int hauteur , int posX , int posY ){
        context.setFill(new Color(0.1, 0.1, 0.1, 0.4));
        context.fillRect(posX,posY, largeur , hauteur);
        context.setStroke(Color.BLACK);

    }

}