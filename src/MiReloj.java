import com.formdev.flatlaf.FlatDarculaLaf;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MiReloj extends JFrame{
    private JPanel pnlGUI;
    private JLabel lblTiempo;
    private JLabel lblDia;
    private JLabel lblFecha;
    private JSlider sldrDescanso;
    private JLabel lblDescanso;

    SimpleDateFormat tiempoFormat, diaFormat, fechaFormat;
    String tiempo, dia, fecha;
    int descansoStart = 50, descansoEnd = 0;

    public MiReloj() throws InterruptedException, UnsupportedAudioFileException, LineUnavailableException, IOException {
        setTitle("ApzReloj");
        setSize(300,100);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        setContentPane(pnlGUI);
        sldrDescanso.addChangeListener(e-> {
            lblDescanso.setText(String.valueOf(sldrDescanso.getValue()));
            descansoStart = sldrDescanso.getValue();
        });
        tiempoFormat = new SimpleDateFormat("hh:mm:ss a");
        diaFormat = new SimpleDateFormat("E");
        fechaFormat = new SimpleDateFormat("dd/MM/yyyy");

        actualizarTiempo();
    }

    private void darSonido(int eleccion) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File file1 = new File(lCadenas[0]);
        File file2 = new File(lCadenas[1]);
        File file3 = new File(lCadenas[2]);

        AudioInputStream audinstream1 = AudioSystem.getAudioInputStream(file1);
        AudioInputStream audinstream2 = AudioSystem.getAudioInputStream(file2);
        AudioInputStream audinstream3 = AudioSystem.getAudioInputStream(file3);

        Clip clip1 = AudioSystem.getClip();
        Clip clip2 = AudioSystem.getClip();
        Clip clip3 = AudioSystem.getClip();

        clip1.open(audinstream1);
        clip2.open(audinstream2);
        clip3.open(audinstream3);

        switch (eleccion){
            case 1: clip2.setMicrosecondPosition(0); clip2.start(); break;
            case 2: clip3.setMicrosecondPosition(0); clip3.start(); break;
            case 3: clip1.setMicrosecondPosition(0); clip1.start(); break;
        }
    }

    private void actualizarTiempo() throws InterruptedException, UnsupportedAudioFileException, LineUnavailableException, IOException {
        int hora=0, minuto=0, segundo=0;

        while (true){
            tiempo  = tiempoFormat.format(Calendar.getInstance().getTime());
            dia     = diaFormat.format(Calendar.getInstance().getTime());
            fecha   = fechaFormat.format(Calendar.getInstance().getTime());
            lblTiempo.setText(tiempo);
            lblDia.setText(dia);
            lblFecha.setText(fecha);

            hora = Calendar.getInstance().get(Calendar.HOUR);
            minuto = Calendar.getInstance().get(Calendar.MINUTE);
            segundo = Calendar.getInstance().get(Calendar.SECOND);

            if(segundo == 0 && minuto < descansoStart && minuto > descansoEnd)
                darSonido(3);
            else if (segundo == 0 && minuto == descansoStart)
                darSonido(1);
            else if (segundo == 0 && minuto == descansoEnd) {
                darSonido(2);
            }

            Thread.sleep(1000);
        }
    }

    static String[] lCadenas;
    public static void main(String[] args) throws InterruptedException, UnsupportedAudioFileException, LineUnavailableException, IOException, UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        UIManager.setLookAndFeel(new FlatDarculaLaf());

        List<String> lAudiosPaths = new ArrayList<String>();
        lAudiosPaths = Files.readAllLines(Paths.get("src/lista.txt"));
        lCadenas = lAudiosPaths.toArray(new String[0]);
        for(String cadena : lCadenas){
            System.out.println(cadena);
        }

        MiReloj mireloj = new MiReloj();
    }

}