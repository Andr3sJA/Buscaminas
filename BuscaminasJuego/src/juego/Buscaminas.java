package juego;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Representa la ventana principal para el juego.
 * 
 * @author Andrés.
 */
public class Buscaminas extends Application {

    /**
     * Calcula el tamaño de la baldosa según el tamaño de la pantalla.
     */
    private final int TAMAGNIO_BALDOSA = (int) Screen.getPrimary().getVisualBounds().getHeight() / 30;
    
    /**
     * Ancho de la pantalla.
     */
    private final int ANCHO = (int) (Screen.getPrimary().getVisualBounds().getWidth() * 0.50);
    
    /**
     * Alto de la pantalla.
     */
    private final int ALTO = (int) (Screen.getPrimary().getVisualBounds().getHeight() * 0.60);

    /**
     * Representa el tamaño en X para la boldosa.
     */
    private int TAMAGNIO_X = 15;
    
    /**
     * Representa el tamaño en Y para la boldosa.
     */
    private int TAMAGNIO_Y = 15;

    /**
     * Dificultad del juego que indica la cantidad de minas que debe aparecer en el tablero de juego.
     */
    private double dificultad = 0.2;

    /**
     * Representa el tablero del juego.
     */
    private Baldosa[][] tablero;
    
    /**
     * Ventana o contenedor donde se encuentra el tablero.
     */
    private BorderPane ventana;
    
    /**
     * Muestra texto emergente en el juego.
     */
    private Text txtContenido;
    
    /**
     * Hace referencia a la imagen que representa una mina.
     */
    private final Image imgMina = new Image("juego/mina.png");
    
    /**
     * Hace referencia a la imagen que representa una bandera.
     */
    private final Image imgBandera = new Image("juego/bandera.png");
    
    /**
     * Hace referencia a la imagen que representa cuando el jugador ha ganado.
     */
    private final Image imgGanando = new Image("juego/ganar.png");
    
    /**
     * Hace referencia a la imagen que representa cuando el jugador ha perdido.
     */
    private final Image juegoPerdido = new Image("juego/perder.png");
    
    /**
     * Muestra imágenes emergentes en el juego.
     */
    private ImageView imgPrincipal;

    /**
     * Crea el panel principal para el juego. Todas las baldosas se incluyen.
     *
     * @return Contenedor del panel del juego.
     */
    private Parent crearJuego() {
        // Representa el panel para el juego (filas y columnas):
        GridPane componenteRaiz = new GridPane();
        
        // Tablero con un conjunto x e y de filas y columnas:
        tablero = new Baldosa[TAMAGNIO_X][TAMAGNIO_Y];

        // Se llena de baldosas el tablero:
        for (int y = 0; y < TAMAGNIO_Y; y++) {
            for (int x = 0; x < TAMAGNIO_X; x++) {
                Baldosa baldosa = new Baldosa(x, y, Math.random() < dificultad);

                tablero[x][y] = baldosa;
                componenteRaiz.add(baldosa, x, y);
            }
        }

        for (int y = 0; y < TAMAGNIO_Y; y++) {
            for (int x = 0; x < TAMAGNIO_X; x++) {
                Baldosa baldosa = tablero[x][y];

                if (baldosa.tieneMina) {
                    continue;
                }

                long bombas = getBaldosasContiguas(baldosa).stream().filter(t -> t.tieneMina).count();

                if (bombas > 0) {
                    baldosa.txtContenido.setText(String.valueOf(bombas));

                    switch ((int) bombas) {
                        case 1:
                            baldosa.txtContenido.setFill(Color.BLUE);
                            break;
                        case 2:
                            baldosa.txtContenido.setFill(Color.GREEN);
                            break;
                        case 3:
                            baldosa.txtContenido.setFill(Color.RED);
                            break;
                        case 4:
                            baldosa.txtContenido.setFill(Color.PURPLE);
                            break;
                        case 5:
                            baldosa.txtContenido.setFill(Color.MAROON);
                            baldosa.txtContenido.setId("baldosasVecinas");
                            break;
                        case 6:
                            baldosa.txtContenido.setFill(Color.DARKTURQUOISE);
                            baldosa.txtContenido.setId("baldosasVecinas");
                            break;
                        case 7:
                            baldosa.txtContenido.setFill(Color.GRAY);
                            baldosa.txtContenido.setId("baldosasVecinas");
                            break;
                        case 8:
                            baldosa.txtContenido.setFill(Color.BLACK);
                            baldosa.txtContenido.setId("baldosasVecinas");
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        componenteRaiz.setHgap(1);
        componenteRaiz.setVgap(1);
        componenteRaiz.setAlignment(Pos.CENTER);
        componenteRaiz.setPadding(new Insets(20, 20, 20, 20));
        componenteRaiz.setGridLinesVisible(false);

        return componenteRaiz;
    }

    /**
     * Obtiene el conjunto de baldosas contiguas.
     *
     * @param baldosa Baldosa sobre la que se encontrará las baldosas contiguas.
     * @return Lista de las baldosas contiguas.
     */
    private List<Baldosa> getBaldosasContiguas(Baldosa baldosa) {
        List<Baldosa> baldosasContiguas = new ArrayList<>();

        int[] puntos = new int[]{
            -1, -1,
            -1, 0,
            -1, 1,
            0, -1,
            0, 1,
            1, -1,
            1, 0,
            1, 1
        };

        for (int i = 0; i < puntos.length; i++) {
            int dx = puntos[i];
            int dy = puntos[++i];

            int nuevoX = baldosa.x + dx;
            int nuevoY = baldosa.y + dy;

            if (nuevoX >= 0 && nuevoX < TAMAGNIO_X
                    && nuevoY >= 0 && nuevoY < TAMAGNIO_Y) {
                baldosasContiguas.add(tablero[nuevoX][nuevoY]);
            }
        }

        return baldosasContiguas;
    }

    /**
     * Representa una baldosa en el tablero.
     */
    private class Baldosa extends StackPane {

        /**
         * Representa la posición en X para la boldosa.
         */
        private final int x;
        
        /**
         * Representa la posición en Y para la bolda;
         */
        private final int y;
        
        /**
         * Valida si la boldosa tiene bomba.
         */
        private final boolean tieneMina;
        
        /**
         * Determina si la baldosa ha sido marcada con bandera.
         */
        private boolean marcada = false;
        
        /**
         * Determina si la baldosa ha sido destapada.
         */
        private boolean estaAbierta = false;
        
        /**
         * Representa la imagen que va dentro de la baldosa.
         */
        private final ImageView imagenBaldosa = new ImageView();

        /**
         * La boldosa está representada por un rectángulo.
         */
        private final Rectangle rectangulo = new Rectangle(TAMAGNIO_BALDOSA - 2, TAMAGNIO_BALDOSA - 2);
        
        /**
         * Contenido textual de la baldosa (1, 2, 3,...)
         */
        private final Text txtContenido = new Text();

        /**
         * Crea una nueva baldosa con una ubicación especifica y un estado.
         *
         * @param x Posición en x.
         * @param y Posición en y.
         * @param tieneBomba Tiene o no bomba.
         */
        public Baldosa(int x, int y, boolean tieneBomba) {
            this.x = x;
            this.y = y;
            this.tieneMina = tieneBomba;

            imagenBaldosa.setFitHeight(TAMAGNIO_BALDOSA);
            imagenBaldosa.setFitWidth(TAMAGNIO_BALDOSA);

            if (tieneBomba) {
                imagenBaldosa.setImage(imgMina);
            }

            rectangulo.setFill(Color.GRAY);
            rectangulo.setStroke(Color.BLACK);

            txtContenido.setFont(Font.font(18));
            txtContenido.setText(tieneBomba ? "X" : "");
            txtContenido.setVisible(false);

            imagenBaldosa.setVisible(false);

            getChildren().addAll(rectangulo, txtContenido, imagenBaldosa);

            // Asigna el evento (el suceso) que debe ocurrir cuando se 
            // presiona el botón izquierdo (principal) o derecho (secundario) 
            // sobre cada una de las boldsas:
            setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    if (!marcada) {
                        abrir();
                    }
                    haGanado();
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    if (!marcada) {
                        if (!estaAbierta) {
                            marcada = true;
                            rectangulo.setFill(Color.AQUAMARINE);
                            imagenBaldosa.setImage(imgBandera);
                            imagenBaldosa.setVisible(true);
                            haGanado();
                        }
                    } else {
                        if (!estaAbierta) {
                            marcada = false;
                            rectangulo.setFill(Color.GRAY);
                            imagenBaldosa.setVisible(false);

                            if (tieneBomba) {
                                imagenBaldosa.setImage(imgMina);
                            }
                        }
                    }
                }
            });
        }

        /**
         * Abre una baldosa y descubre su contenido.
         */
        public void abrir() {
            if (estaAbierta) {
                return;
            }

            if (tieneMina) {
                imagenBaldosa.setVisible(true);
                rectangulo.setFill(Color.LIGHTSALMON);
                mostrarTodasBombas();
                txtContenido.setText("¡Perdiste!");
                txtContenido.setVisible(true);
                return;
            }

            estaAbierta = true;
            txtContenido.setVisible(true);
            rectangulo.setFill(Color.LIGHTGRAY);

            if (txtContenido.getText().isEmpty()) {
                getBaldosasContiguas(this).forEach(Baldosa::abrir);
            }
        }
    }

    /**
     * Revela la posición donde hay una bomba.
     */
    private void mostrarTodasBombas() {
        for (int y = 0; y < TAMAGNIO_Y; y++) {
            for (int x = 0; x < TAMAGNIO_X; x++) {
                if (tablero[x][y].tieneMina) {
                    tablero[x][y].imagenBaldosa.setImage(imgMina);
                    tablero[x][y].imagenBaldosa.setVisible(true);
                }
            }
        }

        imgPrincipal.setImage(juegoPerdido);
    }

    /**
     * Valida si el juego ya se ha ganado.
     */
    private void haGanado() {
        for (int y = 0; y < TAMAGNIO_Y; y++) {
            for (int x = 0; x < TAMAGNIO_X; x++) {
                if (tablero[x][y].tieneMina && tablero[x][y].marcada) {
                    continue;
                }
                if (tablero[x][y].estaAbierta) {
                    continue;
                } else {
                    return;
                }
            }
        }

        txtContenido.setText("¡Ganaste :D!");
        txtContenido.setVisible(true);
    }

    /**
     * Establece el número de baldosas que tiene el tablero.
     *
     * @param cantidadBaldosas Número de baldosas del juego.
     */
    private void setCantidadBaldosas(int cantidadBaldosas) {
        TAMAGNIO_X = cantidadBaldosas;
        TAMAGNIO_Y = cantidadBaldosas;
        reiniciar();
    }

    /**
     * Establece el nivel de dificultad del juego.
     *
     * @param dificultad Nivel de dificultad.
     */
    private void setDificultad(double dificultad) {
        this.dificultad = dificultad;
        reiniciar();
    }

    /**
     * Reinicia el juego.
     */
    private void reiniciar() {
        ventana.setCenter(crearJuego());
        imgPrincipal.setImage(imgGanando);
        txtContenido.setVisible(false);
    }

    /**
     * Configuar el inicio del juego.
     *
     * @param stage Escena principal del juego.
     * @throws Exception Exceptions should not be thrown.
     */
    @Override
    public void start(Stage stage) throws Exception {
        stage.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth() * 0.50);
        stage.setMinHeight(Screen.getPrimary().getVisualBounds().getHeight() * 0.65);

        ventana = new BorderPane();
        ventana.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, null, null)));

        MenuBar mbrPrincipal = new MenuBar();
        Menu mnuOpciones = new Menu("Opciones");
        MenuItem mniReiniciar = new MenuItem("Reiniciar");
        mniReiniciar.setOnAction(e -> reiniciar());
        MenuItem mniNivelFacil = new MenuItem("Nivel Fácil");
        mniNivelFacil.setOnAction(e -> setCantidadBaldosas(7));
        MenuItem mniNivelIntermedio = new MenuItem("Nivel Intermedio");
        mniNivelIntermedio.setOnAction(e -> setCantidadBaldosas(11));
        MenuItem mniNivelAvanzando = new MenuItem("Nivel Experto");
        mniNivelAvanzando.setOnAction(e -> setCantidadBaldosas(15));
        MenuItem mniDificultadFacil = new MenuItem("Dificultad Baja");
        mniDificultadFacil.setOnAction(e -> setDificultad(0.08));
        MenuItem mniDificultadMedia = new MenuItem("Dificultad Media");
        mniDificultadMedia.setOnAction(e -> setDificultad(0.20));
        MenuItem mniDificultadAlta = new MenuItem("Dificultad Alta");
        mniDificultadAlta.setOnAction(e -> setDificultad(0.35));
        mnuOpciones.getItems().addAll(mniReiniciar, mniNivelFacil, mniNivelIntermedio, mniNivelAvanzando, mniDificultadFacil, mniDificultadMedia, mniDificultadAlta);

        mbrPrincipal.getMenus().addAll(mnuOpciones);

        VBox contenidoVertical = new VBox(10);
        contenidoVertical.setAlignment(Pos.CENTER);
        contenidoVertical.setPadding(new Insets(0, 50, 0, 0));

        Text txtLogo = new Text("Buscaminas");
        txtLogo.setFont(Font.font("Courier New", FontWeight.EXTRA_BOLD, 30));
        txtLogo.setFill(Color.BLACK);
        txtLogo.setVisible(true);

        Text txtVersion = new Text("v. 1.0.0");
        txtVersion.setFont(Font.font("Courier New", FontPosture.ITALIC, 12));
        txtVersion.setFill(Color.BLACK);
        txtVersion.setVisible(true);

        Button btnReiniciar = new Button("Reiniciar");
        btnReiniciar.setOnMouseClicked(e -> reiniciar());
        imgPrincipal = new ImageView(imgGanando);
        imgPrincipal.setFitWidth(200);
        imgPrincipal.setFitHeight(200);
        imgPrincipal.setVisible(true);
        contenidoVertical.getChildren().addAll(txtLogo, txtVersion, btnReiniciar, imgPrincipal);

        HBox contenidoHorizontal = new HBox();
        txtContenido = new Text();
        txtContenido.setVisible(false);
        contenidoHorizontal.getChildren().add(txtContenido);

        ventana.setTop(mbrPrincipal);
        ventana.setRight(contenidoVertical);
        ventana.setCenter(crearJuego());
        ventana.setBottom(contenidoHorizontal);

        Scene scene = new Scene(ventana, ANCHO, ALTO);

        scene.getStylesheets().add(Buscaminas.class.getResource("Buscaminas.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Buscaminas");
        stage.show();
    }

    /**
     * Punto de entrada a la aplicación
     *
     * @param args Parámetros de la línea de comandos.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
