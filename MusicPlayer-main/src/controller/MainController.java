package controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.io.File;
import java.util.Scanner;
import java.util.Optional;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import java.io.File;
import javafx.scene.Node;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Slider;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import model.Song;

/**
 * Classe controladora principal que lida com a lógica e interações da interface do usuário.
 * Além de todos os recursos que são resgatados do arquivo FXML e utilizados ao longo do código.
 * Como as Labels, Panes, Buttons, entre outros.
 */
public class MainController implements Initializable {

    @FXML
    private Label albumLabel;
    @FXML
    private Label artistLabel;
    @FXML
    private ImageView capa;
    @FXML
    private Pane pane;
    @FXML
    private Label songLabel;
    @FXML
    private Button playButton, pauseButton, resetButton, previousButton, nextButton;
    @FXML
    private ComboBox<String> speedBox;
    @FXML
    private Slider volumeSlider;
    @FXML
    private ProgressBar songProgressBar;
    @FXML
    private ListView<String> musicListView;
    @FXML
    private ObservableList<String> musicTitles;
    @FXML
    ListView<String> musicListView1 = new ListView<>();
    @FXML
    private ChoiceBox<String> playlistChoiceBox;
    @FXML
    private TextField barra_pes;

    private Media media;
    private MediaPlayer mediaPlayer;

    private File[] files;
    private File[] files1;
    private File[] files2;
    private File[] files3;
    private File[] files4;
    private File[] files5;
    private File[] files6;
    private File[] files7;
    private File[] todas_musicas;

    private File directory;
    private File directory1;
    private File directory2;
    private File directory3;
    private File directory4;
    private File directory5;
    private File directory6;
    private File directory7;
    private File dic_musicas;

    private ArrayList<File> songs;
    private ArrayList<File> songs1;
    private ArrayList<File> songs2;
    private ArrayList<File> songs3;
    private ArrayList<File> songs4;
    private ArrayList<File> songs5;
    private ArrayList<File> songs6;
    private ArrayList<File> songs7;
    private ArrayList<File> song_todas;

    private int songNumber;
    private int[] speeds = {25, 50, 75, 100, 125, 150, 175, 200};

    private Timer timer;
    private TimerTask task;
    private Boolean running;

    private Song song;
    
    int aux;

    /**
     * Método chamado quando o controlador é inicializado.
     * Inicializando nossos arquivos que serão necessários ao programa.
     * Como os arrays das músicas e diretórios dessas.
     * Além dos tratamentos de erros e checagens, se não são nulos, ou estão em formato indevido.
     * 
     * @param arg0     URL do localizador do documento FXML.
     * @param arg1     Recursos de localização específicos do aplicativo.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        songs = new ArrayList<>();
        songs1 = new ArrayList<>();
        songs2 = new ArrayList<>();
        songs3 = new ArrayList<>();
        songs4 = new ArrayList<>();
        song_todas = new ArrayList<>();

        directory = new File("music/Metal");
        directory1 = new File("music/Gospel");
        directory2 = new File("music/Pop");
        directory3 = new File("music/Samba");
        directory4 = new File("music/Sertanejo");
        dic_musicas = new File("music/Musicas");

        files = directory.listFiles();
        files1 = directory1.listFiles();
        files2 = directory2.listFiles();
        files3 = directory3.listFiles();
        files4 = directory4.listFiles();
        todas_musicas = dic_musicas.listFiles();

        if (files != null) {
            for (File file : files) {
                songs.add(file);
            }
        }
        if (files1 != null) {
            for (File file1 : files1) {
                songs1.add(file1);
            }
        }
        if (files2 != null) {
            for (File file2 : files2) {
                songs2.add(file2);
            }
        }
        if (files3 != null) {
            for (File file3 : files3) {
                songs3.add(file3);
            }
        }
        if (files4 != null) {
            for (File file4 : files4) {
                songs4.add(file4);
            }
        }
        if (todas_musicas != null) {
            for (File todas_musicas : todas_musicas) {
                song_todas.add(todas_musicas);
            }
        }

        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        songLabel.setText(songs.get(songNumber).getName());
        artistLabel.setText(songs.get(songNumber).getName());

        musicTitles = FXCollections.observableArrayList();
        musicListView.setItems(musicTitles);

        playlistChoiceBox.getItems().clear();
        musicListView1.getItems().clear();
        File playlistsDir = new File("PlayLists");
        File[] folders = playlistsDir.listFiles(File::isDirectory);

        if (folders != null) {
            ObservableList<String> playlistOptions = FXCollections.observableArrayList();
            for (File folder : folders) {
                musicListView1.getItems().add(folder.getName());
                playlistOptions.add(folder.getName());
            }
            playlistChoiceBox.setItems(playlistOptions);
        }

        for (int i = 0; i < speeds.length; i++) {
            speedBox.getItems().add(Integer.toString(speeds[i]) + "%");
        }

        speedBox.setOnAction(this::changeSpeed);

        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            }

        });
    }

    /**
     * Reproduz a música selecionada atualmente..
     * Utilizando o metadata para cada música dos arquivos.
     * Além da inicialização do timer dessa.
     * Também apresentando a capa do albúm dessa musica selecionada, caso tenha.
     * Mostrando o título, artista e a qual albúm ela pertence.
     */
    public void playSong() {
        beginTimer();
        changeSpeed(null);
        mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
        mediaPlayer.play();

        mediaPlayer.setOnReady(new Runnable() {

            @Override
            public void run() {

                ObservableMap<String, Object> metadata = media.getMetadata();
                capa.setImage((Image) metadata.get("image"));
                songLabel.setText((String) metadata.get("title"));
                artistLabel.setText((String) metadata.get("artist"));
                albumLabel.setText((String) metadata.get("album"));
                // song.setTitle((String) metadata.get("title"));
                // song.setArtist((String) metadata.get("artist"));
                // song.setAlbum((String) metadata.get("album"));
            }

        });

        mediaPlayer.setOnEndOfMedia(new Runnable() {

            @Override
            public void run() {
                nextSong();
            }

        });
    }

    /**
     * Seleciona o gênero "Metal" e carrega as músicas desse gênero.
     * Quando clicado no botão desse gênero, pausa a música anterior e mostra uma aba somente para isso.
     */
    public void Metal() {
        mediaPlayer.pause();
        aux = 0;
        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        songLabel.setText(songs.get(songNumber).getName());
        artistLabel.setText(songs.get(songNumber).getName());

        for (int i = 0; i < speeds.length; i++) {
            speedBox.getItems().add(Integer.toString(speeds[i]) + "%");
        }

        speedBox.setOnAction(this::changeSpeed);

        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            }

        });
        musicTitles.clear();

        for (File file : songs) {
            musicTitles.add(file.getName());
        }
    }

    /**
     * Seleciona o gênero "Gospel" e carrega as músicas desse gênero.
     * Quando clicado no botão desse gênero, pausa a música anterior e mostra uma aba somente para isso.
     */
    public void Gospel() {
        mediaPlayer.pause();
        aux = 1;
        media = new Media(songs1.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        songLabel.setText(songs1.get(songNumber).getName());
        artistLabel.setText(songs1.get(songNumber).getName());

        for (int i = 0; i < speeds.length; i++) {
            speedBox.getItems().add(Integer.toString(speeds[i]) + "%");
        }

        speedBox.setOnAction(this::changeSpeed);

        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            }

        });

        musicTitles.clear();

        for (File file : songs1) {
            musicTitles.add(file.getName());
        }
    }

    /**
     * Seleciona o gênero "Pop" e carrega as músicas desse gênero.
     * Quando clicado no botão desse gênero, pausa a música anterior e mostra uma aba somente para isso.
     */
    public void Pop() {
        mediaPlayer.pause();
        aux = 2;
        media = new Media(songs2.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        songLabel.setText(songs2.get(songNumber).getName());
        artistLabel.setText(songs2.get(songNumber).getName());

        for (int i = 0; i < speeds.length; i++) {
            speedBox.getItems().add(Integer.toString(speeds[i]) + "%");
        }

        speedBox.setOnAction(this::changeSpeed);

        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            }

        });

        musicTitles.clear();

        for (File file : songs2) {
            musicTitles.add(file.getName());
        }
    }

    /**
     * Seleciona o gênero "Samba" e carrega as músicas desse gênero.
     * Quando clicado no botão desse gênero, pausa a música anterior e mostra uma aba somente para isso.
     */
    public void Samba() {
        mediaPlayer.pause();
        aux = 3;
        media = new Media(songs3.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        songLabel.setText(songs3.get(songNumber).getName());
        artistLabel.setText(songs3.get(songNumber).getName());

        for (int i = 0; i < speeds.length; i++) {
            speedBox.getItems().add(Integer.toString(speeds[i]) + "%");
        }

        speedBox.setOnAction(this::changeSpeed);

        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            }

        });
        musicTitles.clear();

        for (File file : songs3) {
            musicTitles.add(file.getName());
        }
    }

    /**
     * Seleciona o gênero "Sertanejo" e carrega as músicas desse gênero.
     * Quando clicado no botão desse gênero, pausa a música anterior e mostra uma aba somente para isso.
     */
    public void Sertanejo() {
        mediaPlayer.pause();
        aux = 4;
        media = new Media(songs4.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        songLabel.setText(songs4.get(songNumber).getName());
        artistLabel.setText(songs4.get(songNumber).getName());

        for (int i = 0; i < speeds.length; i++) {
            speedBox.getItems().add(Integer.toString(speeds[i]) + "%");
        }

        speedBox.setOnAction(this::changeSpeed);

        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            }

        });

        musicTitles.clear();

        for (File file : songs4) {
            musicTitles.add(file.getName());
        }
    }

    /**
     * Pausa a reprodução da música atual.
     * Além de parar a barra timer da música.
     */
    public void pauseSong() {
        cancelTimer();
        mediaPlayer.pause();
    }

    /**
     * Reinicia a reprodução da música atual do início.
     * Reniciando tambem a barra de timer.
     */
    public void resetSong() {
        songProgressBar.setProgress(0);
        mediaPlayer.seek(Duration.seconds(0));
    }

    /**
     * Reproduz a música anterior na lista de reprodução do gênero selecionado.
     * Tendo casos auxiliares, para cada gênero, através da variável aux, onde podemos
     * administrar qual o gênero da música anterior.
     * Além de pausar a música atual, para poder retornar.
     */
    public void previousSong() {
        if (aux == 0) {
            // Gênero Metal
            if (songNumber > 0) {
                songNumber--;
                mediaPlayer.stop();

                if (running) {
                    cancelTimer();
                }

                media = new Media(songs.get(songNumber).toURI().toString());
                mediaPlayer = new MediaPlayer(media);

                songLabel.setText(songs.get(songNumber).getName());

                playSong();
            } else {
                songNumber = songs.size() - 1;
                mediaPlayer.stop();

                if (running) {
                    cancelTimer();
                }

                media = new Media(songs.get(songNumber).toURI().toString());
                mediaPlayer = new MediaPlayer(media);

                songLabel.setText(songs.get(songNumber).getName());

                playSong();
            }
        } else if (aux == 1) {
            // Gênero Gospel
            if (songNumber > 0) {
                songNumber--;
                mediaPlayer.stop();

                if (running) {
                    cancelTimer();
                }

                media = new Media(songs1.get(songNumber).toURI().toString());
                mediaPlayer = new MediaPlayer(media);

                songLabel.setText(songs1.get(songNumber).getName());

                playSong();
            } else {
                songNumber = songs1.size() - 1;
                mediaPlayer.stop();

                if (running) {
                    cancelTimer();
                }

                media = new Media(songs1.get(songNumber).toURI().toString());
                mediaPlayer = new MediaPlayer(media);

                songLabel.setText(songs1.get(songNumber).getName());

                playSong();
            }
        } else if (aux == 2) {
            // Gênero Pop
            if (songNumber > 0) {
                songNumber--;
                mediaPlayer.stop();

                if (running) {
                    cancelTimer();
                }

                media = new Media(songs2.get(songNumber).toURI().toString());
                mediaPlayer = new MediaPlayer(media);

                songLabel.setText(songs2.get(songNumber).getName());

                playSong();
            } else {
                songNumber = songs2.size() - 1;
                mediaPlayer.stop();

                if (running) {
                    cancelTimer();
                }

                media = new Media(songs2.get(songNumber).toURI().toString());
                mediaPlayer = new MediaPlayer(media);

                songLabel.setText(songs2.get(songNumber).getName());

                playSong();
            }
        } else if (aux == 3) {
            // Gênero Samba
            if (songNumber > 0) {
                songNumber--;
                mediaPlayer.stop();

                if (running) {
                    cancelTimer();
                }

                media = new Media(songs3.get(songNumber).toURI().toString());
                mediaPlayer = new MediaPlayer(media);

                songLabel.setText(songs3.get(songNumber).getName());

                playSong();
            } else {
                songNumber = songs3.size() - 1;
                mediaPlayer.stop();

                if (running) {
                    cancelTimer();
                }

                media = new Media(songs3.get(songNumber).toURI().toString());
                mediaPlayer = new MediaPlayer(media);

                songLabel.setText(songs3.get(songNumber).getName());

                playSong();
            }
        } else if (aux == 4) {
            // Gênero Sertanejo
            if (songNumber > 0) {
                songNumber--;
                mediaPlayer.stop();

                if (running) {
                    cancelTimer();
                }

                media = new Media(songs4.get(songNumber).toURI().toString());
                mediaPlayer = new MediaPlayer(media);

                songLabel.setText(songs4.get(songNumber).getName());

                playSong();
            } else {
                songNumber = songs4.size() - 1;
                mediaPlayer.stop();

                if (running) {
                    cancelTimer();
                }

                media = new Media(songs4.get(songNumber).toURI().toString());
                mediaPlayer = new MediaPlayer(media);

                songLabel.setText(songs4.get(songNumber).getName());

                playSong();
            }
        } else if (aux == 5) {
            // Outros gêneros (songs5)
            if (songNumber > 0) {
                songNumber--;
                mediaPlayer.stop();

                if (running) {
                    cancelTimer();
                }

                media = new Media(songs5.get(songNumber).toURI().toString());
                mediaPlayer = new MediaPlayer(media);

                songLabel.setText(songs5.get(songNumber).getName());

                playSong();
            } else {
                songNumber = songs5.size() - 1;
                mediaPlayer.stop();

                if (running) {
                    cancelTimer();
                }

                media = new Media(songs5.get(songNumber).toURI().toString());
                mediaPlayer = new MediaPlayer(media);

                songLabel.setText(songs5.get(songNumber).getName());

                playSong();
            }
        } else if (aux == 6) {
            // Todas as músicas (song_todas)
            if (songNumber > 0) {
                songNumber--;
                mediaPlayer.stop();

                if (running) {
                    cancelTimer();
                }

                media = new Media(song_todas.get(songNumber).toURI().toString());
                mediaPlayer = new MediaPlayer(media);

                songLabel.setText(song_todas.get(songNumber).getName());

                playSong();
            } else {
                songNumber = song_todas.size() - 1;
                mediaPlayer.stop();

                if (running) {
                    cancelTimer();
                }

                media = new Media(song_todas.get(songNumber).toURI().toString());
                mediaPlayer = new MediaPlayer(media);

                songLabel.setText(song_todas.get(songNumber).getName());

                playSong();
            }
        }
    }

    /**
     * Reproduz a próxima música na lista de reprodução do gênero selecionado.
     * Tendo casos auxiliares, para cada gênero, através da variável aux, onde podemos
     * administrar o gênero da próxima música.
     */
    public void nextSong() {
        if (aux == 0) {
            // Gênero Metal
            if (songNumber < songs.size() - 1) {
                songNumber++;
                mediaPlayer.stop();

                if (running) {
                    cancelTimer();
                }

                media = new Media(songs.get(songNumber).toURI().toString());
                mediaPlayer = new MediaPlayer(media);

                songLabel.setText(songs.get(songNumber).getName());

                playSong();
            } else {
                songNumber = 0;
                mediaPlayer.stop();

                if (running) {
                    cancelTimer();
                }

                media = new Media(songs.get(songNumber).toURI().toString());
                mediaPlayer = new MediaPlayer(media);

                songLabel.setText(songs.get(songNumber).getName());

                playSong();
            }
        } else if (aux == 1) {
            // Gênero Gospel
            if (songNumber < songs1.size() - 1) {
                songNumber++;
                mediaPlayer.stop();

                if (running) {
                    cancelTimer();
                }

                media = new Media(songs1.get(songNumber).toURI().toString());
                mediaPlayer = new MediaPlayer(media);

                songLabel.setText(songs1.get(songNumber).getName());

                playSong();
            } else {
                songNumber = 0;
                mediaPlayer.stop();

                if (running) {
                    cancelTimer();
                }

                media = new Media(songs1.get(songNumber).toURI().toString());
                mediaPlayer = new MediaPlayer(media);

                songLabel.setText(songs1.get(songNumber).getName());

                playSong();
            }
        } else if (aux == 2) {
            // Gênero Pop
            if (songNumber < songs2.size() - 1) {
                songNumber++;
                mediaPlayer.stop();

                if (running) {
                    cancelTimer();
                }

                media = new Media(songs2.get(songNumber).toURI().toString());
                mediaPlayer = new MediaPlayer(media);

                songLabel.setText(songs2.get(songNumber).getName());

                playSong();
            } else {
                songNumber = 0;
                mediaPlayer.stop();

                if (running) {
                    cancelTimer();
                }

                media = new Media(songs2.get(songNumber).toURI().toString());
                mediaPlayer = new MediaPlayer(media);

                songLabel.setText(songs2.get(songNumber).getName());

                playSong();
            }
        } else if (aux == 3) {
            // Gênero Samba
            if (songNumber < songs3.size() - 1) {
                songNumber++;
                mediaPlayer.stop();

                if (running) {
                    cancelTimer();
                }

                media = new Media(songs3.get(songNumber).toURI().toString());
                mediaPlayer = new MediaPlayer(media);

                songLabel.setText(songs3.get(songNumber).getName());

                playSong();
            } else {
                songNumber = 0;
                mediaPlayer.stop();

                if (running) {
                    cancelTimer();
                }

                media = new Media(songs3.get(songNumber).toURI().toString());
                mediaPlayer = new MediaPlayer(media);

                songLabel.setText(songs3.get(songNumber).getName());

                playSong();
            }
        } else if (aux == 4) {
            // Gênero Sertanejo
            if (songNumber < songs4.size() - 1) {
                songNumber++;
                mediaPlayer.stop();

                if (running) {
                    cancelTimer();
                }

                media = new Media(songs4.get(songNumber).toURI().toString());
                mediaPlayer = new MediaPlayer(media);

                songLabel.setText(songs4.get(songNumber).getName());

                playSong();
            } else {
                songNumber = 0;
                mediaPlayer.stop();

                if (running) {
                    cancelTimer();
                }

                media = new Media(songs4.get(songNumber).toURI().toString());
                mediaPlayer = new MediaPlayer(media);

                songLabel.setText(songs4.get(songNumber).getName());

                playSong();
            }
        } else if (aux == 5) {
            // Outros gêneros (songs5)
            if (songNumber < songs5.size() - 1) {
                songNumber++;
                mediaPlayer.stop();

                if (running) {
                    cancelTimer();
                }

                media = new Media(songs5.get(songNumber).toURI().toString());
                mediaPlayer = new MediaPlayer(media);

                songLabel.setText(songs5.get(songNumber).getName());

                playSong();
            } else {
                songNumber = 0;
                mediaPlayer.stop();

                if (running) {
                    cancelTimer();
                }

                media = new Media(songs5.get(songNumber).toURI().toString());
                mediaPlayer = new MediaPlayer(media);

                songLabel.setText(songs5.get(songNumber).getName());

                playSong();
            }
        } else if (aux == 6) {
            // Todas as músicas (song_todas)
            if (songNumber < song_todas.size() - 1) {
                songNumber++;
                mediaPlayer.stop();

                if (running) {
                    cancelTimer();
                }

                media = new Media(song_todas.get(songNumber).toURI().toString());
                mediaPlayer = new MediaPlayer(media);

                songLabel.setText(song_todas.get(songNumber).getName());

                playSong();
            } else {
                songNumber = 0;
                mediaPlayer.stop();

                if (running) {
                    cancelTimer();
                }

                media = new Media(song_todas.get(songNumber).toURI().toString());
                mediaPlayer = new MediaPlayer(media);

                songLabel.setText(song_todas.get(songNumber).getName());

                playSong();
            }
        }
    }

    /**
     * Altera a velocidade de reprodução da música com base na opção selecionada no speedBox.
     * Se nenhuma opção for selecionada, a velocidade é definida como 1 (normal).
     */
    public void changeSpeed(ActionEvent event) {
        if (speedBox.getValue() == null) {
            mediaPlayer.setRate(1);
        } else {
            mediaPlayer.setRate(Integer.parseInt(speedBox.getValue().substring(0, speedBox.getValue().length() - 1)) * 0.01);
        }
    }

    /**
     * Inicia o timer para atualizar a barra de progresso da música.
     * O timer é configurado para atualizar a cada 1 segundo.
     * Tendo início no começo da música, e fim ao final da música.
     */
    public void beginTimer() {
        timer = new Timer();
        task = new TimerTask() {
            public void run() {
                running = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = mediaPlayer.getTotalDuration().toSeconds();
                songProgressBar.setProgress(current / end);

                if (current / end == 1) {
                    cancelTimer();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    /**
     * Cancela o timer utilizado para atualizar a barra de progresso da música.
     */
    public void cancelTimer() {
        running = false;
        timer.cancel();
    }

    /**
     * Cria uma nova pasta para a playlist especificada pelo usuário.
     * Primeiro, exibe um diálogo de entrada para o usuário digitar o nome da playlist.
     * Em seguida, cria uma nova pasta com o nome fornecido dentro do diretório "PlayLists".
     * Nisso, podemos visualizar as playlists criadas, e os slots disponíveis para isso.
     * Também podemos selecionar qual playlist queremos tratar, podendo adicionar e/ou remover novas músicas
     * por meio de uma choicebox.
     * Tendo as devidas mensagens, para cada caso possível dentro da funcionalidade.
     */
    public void Criarpasta(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Criar Playlist");
        dialog.setHeaderText("Digite o nome da Playlist:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String nomePasta = result.get();
            try {
                File playlistsDir = new File("PlayLists/" + nomePasta);
                File novaPasta = new File(playlistsDir, "abc");
                boolean criada = novaPasta.mkdirs();
                if (criada) {
                    System.out.println("A nova pasta foi criada com sucesso.");
                } else {
                    System.out.println("Falha ao criar a nova pasta.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                File playlistsDir = new File("PlayLists/" + nomePasta);
                File novaPasta = new File(playlistsDir, "abc");

                if (novaPasta.delete()) {
                    System.out.println("A pasta foi deletada com sucesso.");
                } else {
                    System.out.println("Falha ao deletar a pasta.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        musicListView1.getItems().clear();
        playlistChoiceBox.getItems().clear();

        File playlistsDir = new File("PlayLists");
        File[] folders = playlistsDir.listFiles(File::isDirectory);

        if (folders != null) {
            ObservableList<String> playlistOptions = FXCollections.observableArrayList();
            for (File folder : folders) {
                musicListView1.getItems().add(folder.getName());
                playlistOptions.add(folder.getName());
            }
            playlistChoiceBox.setItems(playlistOptions);
        }
    }

    /**
     * Adiciona uma música à playlist selecionada pelo usuário.
     * Abre um diálogo de seleção de arquivo para o usuário escolher a música a ser adicionada.
     * A música selecionada é copiada para o diretório da playlist.
     * Tendo as devidas mensagens, para cada caso possível dentro da funcionalidade.
     */
    public void Adicionar(ActionEvent event) {
        String selectedPlaylist = playlistChoiceBox.getValue();

        if (selectedPlaylist == null) {
            System.out.println("Nenhuma playlist selecionada.");
            return;
        }

        File musicDir = new File("Music/Musicas");

        if (!musicDir.exists()) {
            System.out.println("Diretório de músicas não encontrado.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar Música");
        fileChooser.setInitialDirectory(musicDir);
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Arquivos de Música", "*.mp3", "*.wav"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile == null) {
            System.out.println("Nenhuma música selecionada.");
            return;
        }

        File playlistDir = new File("PlayLists/" + selectedPlaylist);

        if (!playlistDir.exists()) {
            System.out.println("Diretório da playlist não encontrado.");
            return;
        }

        File newFile = new File(playlistDir, selectedFile.getName());

        try {
            Files.copy(selectedFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Música " + selectedFile.getName() + " adicionada à playlist " + selectedPlaylist);
        } catch (IOException e) {
            System.out.println("Falha ao adicionar a música " + selectedFile.getName() + " à playlist " + selectedPlaylist);
            e.printStackTrace();
        }
    }

    /**
     * Remove uma música da playlist selecionada pelo usuário.
     * Abre um diálogo de seleção de arquivo para o usuário escolher a música a ser removida.
     * A música selecionada é excluída do diretório da playlist.
     * Tendo as devidas mensagens, para cada caso possível dentro da funcionalidade.
     */
    public void Remover(ActionEvent event) {
        String selectedPlaylist = playlistChoiceBox.getValue();

        if (selectedPlaylist == null) {
            System.out.println("Nenhuma playlist selecionada.");
            return;
        }

        File playlistDir = new File("PlayLists/" + selectedPlaylist);

        if (!playlistDir.exists()) {
            System.out.println("Diretório da playlist não encontrado.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Remover Música");
        fileChooser.setInitialDirectory(playlistDir);
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Arquivos de Música", "*.mp3", "*.wav"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile == null) {
            System.out.println("Nenhuma música selecionada para remover.");
            return;
        }

        try {
            Files.delete(selectedFile.toPath());
            System.out.println("Música " + selectedFile.getName() + " removida da playlist " + selectedPlaylist);
        } catch (IOException e) {
            System.out.println("Falha ao remover a música " + selectedFile.getName() + " da playlist " + selectedPlaylist);
            e.printStackTrace();
        }
    }

    /**
     * Toca a playlist selecionada pelo usuário.
     * Configura o gênero como "Outros gêneros" (aux = 5).
     * Carrega as músicas da playlist selecionada e configura a primeira música adicionada para reprodução.
     * Antes de toda nova ação, a música sendo tocada anteriormente, é pausada.
     */
    public void TocarPlayList(ActionEvent event) {
        aux = 5;
        String selectedPlaylist = playlistChoiceBox.getValue();

        if (selectedPlaylist == null) {
            System.out.println("Nenhuma playlist selecionada.");
            return;
        }

        File playlistDir = new File("PlayLists/" + selectedPlaylist);

        if (!playlistDir.exists()) {
            System.out.println("Diretório da playlist não encontrado.");
            return;
        }

        File[] musicFiles = playlistDir.listFiles();

        if (musicFiles == null || musicFiles.length == 0) {
            System.out.println("Nenhum arquivo de música encontrado na playlist.");
            return;
        }
        songs5 = new ArrayList<>();
        for (File musicFile : musicFiles) {
            songs5.add(musicFile);
        }

        mediaPlayer.pause();
        media = new Media(songs5.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        songLabel.setText(songs5.get(songNumber).getName());
        artistLabel.setText(songs5.get(songNumber).getName());

        for (int i = 0; i < speeds.length; i++) {
            speedBox.getItems().add(Integer.toString(speeds[i]) + "%");
        }

        speedBox.setOnAction(this::changeSpeed);

        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            mediaPlayer.setVolume(newValue.doubleValue() * 0.01);
        });

        musicTitles.clear();
        for (File file : songs5) {
            musicTitles.add(file.getName());
        }
    }

    /**
     * Realiza uma pesquisa nas músicas disponíveis e atualiza a lista de músicas com base na pesquisa
     * inserida pelo usuário.
     * Define o gênero como "Todas as músicas" caso (aux = 6).
     * Verifica todos os arquivos de música no diretório "Music/Musicas" e adiciona à lista de músicas (song_todas)
     * apenas os arquivos que correspondem à pesquisa, além de exibir inicialmente todas as músicas
     * da lista de músicas salvas.
     * Tendo checanges e tratamentos de erros, para garantir que o requisito inicial seja atendido.
     */
    public void barra_pesquisa() {
        aux = 6;
        String minha_pesquisa = barra_pes.getText();
        File todas_musicas = new File("music/Musicas");

        if (todas_musicas.exists() && todas_musicas.isDirectory()) {
            File[] arquivos = todas_musicas.listFiles();
            if (running) {
                cancelTimer();
                mediaPlayer.pause();
            }
            File selectedFile = null;
            song_todas.clear();
            for (File arquivo : arquivos) {
                if (arquivo.isFile() && arquivo.getName().toLowerCase().endsWith(".mp3")) {
                    if (arquivo.getName().toLowerCase().contains(minha_pesquisa.toLowerCase())) {
                        selectedFile = arquivo;
                        song_todas.add(arquivo);
                    }
                }
            }

            if (selectedFile != null) {
                media = new Media(selectedFile.toURI().toString());
                mediaPlayer = new MediaPlayer(media);

                songLabel.setText(selectedFile.getName());
                artistLabel.setText(selectedFile.getName());

                for (int i = 0; i < speeds.length; i++) {
                    speedBox.getItems().add(Integer.toString(speeds[i]) + "%");
                }

                speedBox.setOnAction(this::changeSpeed);

                volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                    mediaPlayer.setVolume(newValue.doubleValue() * 0.01);
                });

                musicTitles.clear();
                for (File file : song_todas) {
                    musicTitles.add(file.getName());
                }
            }
        }
    }

}




