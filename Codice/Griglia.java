import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Griglia extends JFrame {

    private static final long serialVersionUID = 1L;
    private final int nToggleButton = 30;
    private final JSplitPane splitContentPane;
    private final JPanel topContentPane,bottomContentPane;
    private final JTextField txtCercaPittogramma;
    private int larg = 100;
    private int lung = 100;
    private static final int fontSize = 15;
    private final JButton buttonDeleteAllWrite,buttonAdd,buttonRemove,buttonCleanAllGrid,buttonSaveGrid,buttonDeleteGrid,buttonSaveModifyGrid,buttonGuide;
    private final JToggleButton buttonModifyGrid;
    private JComboBox comboBoxSavedGrid = new JComboBox<>(new String[0]);
    private static final String folderpathImage = System.getProperty("user.home") + "\\Pictures\\CAAImmagini";
    private static final String folderpathSavedGrid = System.getProperty("user.home") + "\\Documents\\GriglieSalvate";
    private static final String pathAbsImage = folderpathImage + "\\";
    private static final String pathAbsGrid = folderpathSavedGrid + "\\";


    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Griglia frame = new Griglia();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            File folderSavedGrid = new File(folderpathSavedGrid);
            if (!folderSavedGrid.exists() || !folderSavedGrid.isDirectory()) {
                folderSavedGrid.mkdir();
            }
        });
    }

    public Griglia() throws IOException {
        setBackground(new Color(255, 255, 255));
        setMinimumSize(new Dimension(1100, 800));
        setTitle("GridApp");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1400, 800);
        splitContentPane = new JSplitPane();
        splitContentPane.setBackground(new Color(255, 255, 255));
        splitContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        splitContentPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitContentPane.setDividerLocation(150);
        splitContentPane.setEnabled(false);
        getContentPane().setLayout(new GridLayout());
        getContentPane().add(splitContentPane);
        topContentPane = new JPanel();
        bottomContentPane = new JPanel();
        splitContentPane.setTopComponent(topContentPane);
        splitContentPane.setBottomComponent(bottomContentPane);
        topContentPane.setLayout(new BoxLayout(topContentPane, BoxLayout.Y_AXIS));
        bottomContentPane.setLayout(new BoxLayout(bottomContentPane, BoxLayout.Y_AXIS));

        splitContentPane.setVisible(true);
        //crezione dei gruppi di pulsanti uno per la griglia e uno per i simboli che vengono "scritti"
        ButtonGroup buttonGroupImageGrid = new ButtonGroup();
        ButtonGroup buttonGroupImageWrite = new ButtonGroup();

        //creazione parte di "scrittura" dei pittogrammi
        JScrollPane scrollPanelWrite = new JScrollPane();
        topContentPane.add(scrollPanelWrite, BorderLayout.NORTH);
        scrollPanelWrite.setPreferredSize(new Dimension(150,150));
        scrollPanelWrite.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPanelWrite.getVerticalScrollBar().setUnitIncrement(10);

        JPanel panelWrite = new JPanel();
        panelWrite.setBackground(new Color(255, 255, 255));
        panelWrite.setLayout(new WrapLayout(WrapLayout.LEFT));
        panelWrite.setAutoscrolls(true);
        scrollPanelWrite.setViewportView(panelWrite);
        JScrollBar vertical = scrollPanelWrite.getVerticalScrollBar();

        //creazione pulsanti per gestire la parte di "scrittura" dei pittogrammi
        JPanel panelFunctionWrite = new JPanel();
        panelFunctionWrite.setBackground(new Color(255, 255, 255));
        topContentPane.add(panelFunctionWrite, BorderLayout.SOUTH);
        panelFunctionWrite.setLayout(new GridLayout(0, 4, 5, 5));

        //crezione griglia
        JPanel panelGrid = new JPanel();
        panelGrid.setBackground(new Color(255, 255, 255));
        bottomContentPane.add(panelGrid, BorderLayout.CENTER);
        panelGrid.setLayout(new GridLayout(3,15));
        //riempio la griglia con max 30 pulsanti
        for(int i=0;i<nToggleButton;i++){
            ImageButton buttonImage = new ImageButton();
            buttonImage.setPreferredSize(new Dimension((int) Math.round(larg+1.15),(int) Math.round(lung*1.15)));
            buttonImage.setBackground(new Color(255, 255, 255));
            buttonImage.setFont(new Font("Arial", Font.BOLD, fontSize));
            buttonGroupImageGrid.add(buttonImage);
            buttonImage.addMouseListener(new Click(){
                @Override
                public void singoloClick(MouseEvent e) throws IOException {
                    if(!buttonModifyGrid.isSelected()) {
                        Enumeration groupElementsCreateGrid = buttonGroupImageGrid.getElements();
                        Boolean whileExit = false;
                        while (groupElementsCreateGrid.hasMoreElements() && !whileExit) {
                            ImageButton buttonImageSelected = (ImageButton) groupElementsCreateGrid.nextElement();
                            if (buttonImageSelected.isSelected() && !buttonImageSelected.getText().equals("")) {
                                ImageButton buttonImageWrite = new ImageButton();
                                buttonImageWrite.setBackground(new Color(255, 255, 255));
                                buttonImageWrite.setFont(new Font("Arial", Font.BOLD, fontSize));
                                String imagePath = pathAbsImage + buttonImageSelected.getText() + ".png";
                                if (buttonImageSelected.imageExist(imagePath)){
                                    buttonImageWrite.setImage(imagePath, buttonImageSelected.getText(), (int) Math.round(larg - larg * 0.3), (int) Math.round(lung - lung * 0.3));
                                } else if(getClass().getResourceAsStream("/imageLib/" + buttonImageSelected.getText() + ".png") != null) {
                                    buttonImageWrite.setImageJar(ImageIO.read(getClass().getResourceAsStream("/imageLib/" + buttonImageSelected.getText() + ".png")), buttonImageSelected.getText(), (int) Math.round(larg - larg * 0.3), (int) Math.round(lung - lung * 0.3));
                                }
                                buttonImageWrite.addMouseListener(new Click(){
                                    @Override
                                    public void singoloClick(MouseEvent e){
                                        if (e.getButton() == MouseEvent.BUTTON3){
                                            if(!buttonModifyGrid.isSelected()) {
                                                panelWrite.remove(buttonImageWrite);
                                                buttonGroupImageWrite.remove(buttonImageWrite);
                                                panelWrite.revalidate();
                                                panelWrite.repaint();
                                            }
                                        }
                                    }
                                });
                                buttonGroupImageWrite.add(buttonImageWrite);
                                panelWrite.add(buttonImageWrite);
                                whileExit = true;
                            }
                        }
                        buttonGroupImageGrid.clearSelection();
                        vertical.setValue(vertical.getMaximum());
                        panelWrite.revalidate();
                        panelWrite.repaint();
                    }
                }
            });
            panelGrid.add(buttonImage);
        }

        //crezione del riquadro multifunzione
        JPanel panelFunctionGrid = new JPanel();
        panelFunctionGrid.setBackground(new Color(255, 255, 255));
        bottomContentPane.add(panelFunctionGrid, BorderLayout.SOUTH);
        panelFunctionGrid.setLayout(new GridLayout(0, 4, 5, 5));
        panelFunctionGrid.setPreferredSize(new Dimension(5,5));

        //ricerca dei pittogrammi
        txtCercaPittogramma = new JTextField();
        panelFunctionGrid.add(txtCercaPittogramma);
        txtCercaPittogramma.setText("Cerca pittogramma...");
        txtCercaPittogramma.setFont(new Font("Arial", Font.BOLD, fontSize));
        txtCercaPittogramma.setColumns(10);
        txtCercaPittogramma.setEnabled(false);
        txtCercaPittogramma.addMouseListener(new Click() {
            @Override
            public void mouseClicked(MouseEvent e) {
                txtCercaPittogramma.setText("");
            }
        });

        //pulsante per cancellare tutti i pittogrammi scritti nella barra sopra
        buttonDeleteAllWrite = new JButton("Cancella tutto\r\n");
        buttonDeleteAllWrite.setBackground(new Color(255, 255, 255));
        buttonDeleteAllWrite.setFont(new Font("Arial", Font.BOLD, fontSize));
        buttonDeleteAllWrite.setIcon(createIcon(ImageIO.read(getClass().getResourceAsStream("/Icons/delete.png")), "delete"));
        buttonDeleteAllWrite.setToolTipText("Cancella tutto");
        panelFunctionWrite.add(buttonDeleteAllWrite);
        buttonDeleteAllWrite.addActionListener(e -> {
            panelWrite.removeAll();
            panelWrite.revalidate();
            panelWrite.repaint();
        });

        //crezione pulsante per l'aggiunta di un pittogramma all'interno della griglia
        buttonAdd = new JButton("Aggiungi\r\n");
        buttonAdd.setBackground(new Color(255, 255, 255));
        buttonAdd.setFont(new Font("Arial", Font.BOLD, fontSize));
        buttonAdd.setPreferredSize(new Dimension(5,5));
        buttonAdd.setIcon(createIcon(ImageIO.read(getClass().getResourceAsStream("/Icons/add.png")), "add"));
        buttonAdd.setToolTipText("Aggiungi pittogramma alla griglia");
        panelFunctionGrid.add(buttonAdd);
        buttonAdd.setEnabled(false);
        buttonAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Enumeration groupElementsAdd = buttonGroupImageGrid.getElements();
                Boolean whileExit = false;
                while (groupElementsAdd.hasMoreElements() && !whileExit) {
                    ImageButton buttonImageSelected = (ImageButton)groupElementsAdd.nextElement();
                    if (buttonImageSelected.isSelected()) {
                        String nameImage = txtCercaPittogramma.getText().trim();
                        File file = new File(String.valueOf(Griglia.class.getResource("/imageLib/" + nameImage + ".png")));
                        if (!nameImage.isEmpty() && !nameImage.equals("Cerca pittogramma...")) {
                            String imagePath = pathAbsImage + nameImage + ".png";
                            if (buttonImageSelected.imageExist(imagePath)){
                                buttonImageSelected.setImage(imagePath,nameImage,larg,lung);
                            } else if(getClass().getResourceAsStream("/imageLib/" + nameImage + ".png") != null) {
                                try {
                                    buttonImageSelected.setImageJar(ImageIO.read(getClass().getResourceAsStream("/imageLib/" + nameImage + ".png")),nameImage,larg,lung);
                                } catch (IOException ex) {

                                }
                            } else {
                                JOptionPane.showMessageDialog(null,"Pittogramma non trovato");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null,"Inserire il nome del pittograma");
                        }
                        whileExit = true;
                    }
                }
                panelGrid.revalidate();
                panelGrid.repaint();
            }
        });

        //pulsante per rimuovere pittogrammi dalla griglia
        buttonRemove = new JButton("Rimuovi\r\n");
        buttonRemove.setBackground(new Color(255, 255, 255));
        buttonRemove.setFont(new Font("Arial", Font.BOLD, fontSize));
        buttonRemove.setIcon(createIcon(ImageIO.read(getClass().getResourceAsStream("/Icons/remove.png")), "remove"));
        buttonRemove.setPreferredSize(new Dimension(5,5));
        buttonRemove.setToolTipText("Rimuovi pittogramma dalla griglia");
        panelFunctionGrid.add(buttonRemove);
        buttonRemove.setEnabled(false);
        buttonRemove.addActionListener(e -> {
            Enumeration groupElementsRemove = buttonGroupImageGrid.getElements();
            Boolean whileExit = false;
            while (groupElementsRemove.hasMoreElements() && !whileExit) {
                ImageButton buttonImageSelected = (ImageButton) groupElementsRemove.nextElement();
                if (buttonImageSelected.isSelected()) {
                    buttonImageSelected.deleteImage();
                    whileExit = true;
                }
            }
            panelGrid.revalidate();
            panelGrid.repaint();
        });

        //pulsante per cancellare tutti i pittogrammi scritti nella barra griglia
        buttonCleanAllGrid = new JButton("Pulisci griglia\r\n");
        buttonCleanAllGrid.setBackground(new Color(255, 255, 255));
        buttonCleanAllGrid.setFont(new Font("Arial", Font.BOLD, fontSize));
        buttonCleanAllGrid.setIcon(createIcon(ImageIO.read(getClass().getResourceAsStream("/Icons/clean.png")), "clean"));
        buttonCleanAllGrid.setPreferredSize(new Dimension(5,5));
        buttonCleanAllGrid.setToolTipText("Pulisci griglia");
        panelFunctionGrid.add(buttonCleanAllGrid);
        buttonCleanAllGrid.setEnabled(false);
        buttonCleanAllGrid.addActionListener(e -> {
            deleteAllGrid(buttonGroupImageGrid);
            panelWrite.revalidate();
            panelWrite.repaint();
        });

        //combo box per cambiare griglia
        initializeComboBox();
        uploadGrid(buttonGroupImageGrid, String.valueOf(comboBoxSavedGrid.getSelectedItem()) + ".txt");
        panelFunctionGrid.add(comboBoxSavedGrid);
        comboBoxSavedGrid.setFont(new Font("Arial", Font.BOLD, fontSize));
        comboBoxSavedGrid.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {;
                if(comboBoxSavedGrid.getItemCount() > 0) {
                    deleteAllGrid(buttonGroupImageGrid);
                    uploadGrid(buttonGroupImageGrid, String.valueOf(comboBoxSavedGrid.getSelectedItem()) + ".txt");
                    panelGrid.revalidate();
                    panelGrid.repaint();
                }
            }
        });

        //pulsante per salvare le modifiche fatte alla griglia
        buttonSaveModifyGrid = new JButton("Salva modifiche griglia\r\n");
        buttonSaveModifyGrid.setBackground(new Color(255, 255, 255));
        buttonSaveModifyGrid.setFont(new Font("Arial", Font.BOLD, fontSize));
        buttonSaveModifyGrid.setIcon(createIcon(ImageIO.read(getClass().getResourceAsStream("/Icons/confirmModify.png")), "confirmModify"));
        buttonSaveModifyGrid.setToolTipText("Salva modifiche griglia");
        buttonSaveModifyGrid.setEnabled(false);
        panelFunctionGrid.add(buttonSaveModifyGrid);
        buttonSaveModifyGrid.addActionListener(e -> {
            if(comboBoxSavedGrid.getItemCount() > 0) {
                Enumeration groupElementsSave = buttonGroupImageGrid.getElements();
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(pathAbsGrid + String.valueOf(comboBoxSavedGrid.getSelectedItem()) + ".txt"));
                    while (groupElementsSave.hasMoreElements()) {
                        ImageButton buttonImageSelected = (ImageButton) groupElementsSave.nextElement();
                        writer.write(buttonImageSelected.getText());
                        writer.newLine();
                    }
                    writer.close();
                    JOptionPane.showMessageDialog(null, "Modifica avvenuta con successo");
                } catch (Exception wf) {
                    wf.getStackTrace();
                }
            }
        });

        //pulsante per salvare la griglia impostata
        buttonSaveGrid = new JButton("Salva nuova griglia\r\n");
        buttonSaveGrid.setBackground(new Color(255, 255, 255));
        buttonSaveGrid.setFont(new Font("Arial", Font.BOLD, fontSize));
        buttonSaveGrid.setIcon(createIcon(ImageIO.read(getClass().getResourceAsStream("/Icons/save.png")), "save"));
        buttonSaveGrid.setPreferredSize(new Dimension(5,5));
        buttonSaveGrid.setSize(new Dimension(2,5));
        buttonSaveGrid.setToolTipText("Salva nuova griglia");
        buttonSaveGrid.setEnabled(false);
        panelFunctionGrid.add(buttonSaveGrid);
        buttonSaveGrid.addActionListener(e -> {
            Enumeration groupElementsSave = buttonGroupImageGrid.getElements();
            Boolean whileExit = false;
            String newGridName = "";
            while (!whileExit) {
                newGridName = JOptionPane.showInputDialog("Nome della nuova griglia");
                if (newGridName != null) {
                    if (!Files.exists(Paths.get(pathAbsGrid + newGridName + ".txt")) && !newGridName.trim().isEmpty()) {
                        try {
                            BufferedWriter writer = new BufferedWriter(new FileWriter(pathAbsGrid + newGridName.trim() + ".txt"));
                            while (groupElementsSave.hasMoreElements()) {
                                ImageButton buttonImageSelected = (ImageButton) groupElementsSave.nextElement();
                                writer.write(buttonImageSelected.getText());
                                writer.newLine();
                            }
                            writer.close();
                            whileExit = true;
                            comboBoxSavedGrid.addItem(newGridName.trim());
                            comboBoxSavedGrid.setSelectedItem(newGridName.trim());
                            JOptionPane.showMessageDialog(null, "Salvataggio avvenuto con successo");
                        } catch (Exception wf) {
                            wf.getStackTrace();
                        }
                    } else if (!newGridName.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "File gia' esistente, cambiare nome");
                    } else {
                        JOptionPane.showMessageDialog(null, "Inserire un nome");
                    }
                } else {
                    whileExit = true;
                }
            }
            panelGrid.revalidate();
            panelGrid.repaint();
        });

        //pulsante per eliminare una griglia dalla cartella
        buttonDeleteGrid = new JButton("Elimina griglia\r\n");
        buttonDeleteGrid.setBackground(new Color(255, 255, 255));
        buttonDeleteGrid.setFont(new Font("Arial", Font.BOLD, fontSize));
        buttonDeleteGrid.setIcon(createIcon(ImageIO.read(getClass().getResourceAsStream("/Icons/delete.png")), "delete"));
        buttonDeleteGrid.setPreferredSize(new Dimension(5,5));
        buttonDeleteGrid.setToolTipText("Elimina griglia dalla memoria");
        buttonDeleteGrid.setEnabled(false);
        panelFunctionGrid.add(buttonDeleteGrid);
        buttonDeleteGrid.addActionListener(e -> {
            if(comboBoxSavedGrid.getItemCount() > 0) {
                String gridToDelete = String.valueOf(comboBoxSavedGrid.getSelectedItem());
                comboBoxSavedGrid.removeItem(gridToDelete);
                try {
                    Files.delete(Paths.get(pathAbsGrid + gridToDelete + ".txt"));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        //pulsante per modificare la griglia
        buttonModifyGrid = new JToggleButton("Modifica griglia\r\n");
        buttonModifyGrid.setBackground(new Color(255, 255, 255));
        buttonModifyGrid.setFont(new Font("Arial", Font.BOLD, fontSize));
        buttonModifyGrid.setIcon(createIcon(ImageIO.read(getClass().getResourceAsStream("/Icons/modify.png")), "modify"));
        buttonModifyGrid.setToolTipText("Modifica griglia");
        panelFunctionGrid.add(buttonModifyGrid);
        buttonModifyGrid.addActionListener(e -> {
            if(buttonModifyGrid.isSelected()){
                buttonGroupImageGrid.clearSelection();
                comboBoxSavedGrid.setEnabled(false);
                buttonSaveGrid.setEnabled(true);
                buttonDeleteGrid.setEnabled(true);
                buttonSaveModifyGrid.setEnabled(true);
                txtCercaPittogramma.setEnabled(true);
                buttonAdd.setEnabled(true);
                buttonRemove.setEnabled(true);
                buttonCleanAllGrid.setEnabled(true);
            }else{
                buttonGroupImageGrid.clearSelection();
                comboBoxSavedGrid.setEnabled(true);
                buttonSaveGrid.setEnabled(false);
                buttonDeleteGrid.setEnabled(false);
                buttonSaveModifyGrid.setEnabled(false);
                txtCercaPittogramma.setEnabled(false);
                buttonAdd.setEnabled(false);
                buttonRemove.setEnabled(false);
                buttonCleanAllGrid.setEnabled(false);
            }
        });

        //pulsante della guida all'utilizzo
        buttonGuide = new JButton("Guida\r\n");
        buttonGuide.setBackground(new Color(255, 255, 255));
        buttonGuide.setFont(new Font("Arial", Font.BOLD, fontSize));
        buttonGuide.setIcon(createIcon(ImageIO.read(getClass().getResourceAsStream("/Icons/guide.png")), "guide"));
        buttonGuide.setPreferredSize(new Dimension(5,5));
        buttonGuide.setToolTipText("Guida");
        panelFunctionWrite.add(buttonGuide);

        buttonGuide.addActionListener(e -> JOptionPane.showMessageDialog(null,
                "Il pulsante modifica griglia consente di cambiare modalità di utilizzo\n\n"
                        + "Modalità di scrittura dei pittogrammi\n"
                        + " - Per scrivere un pittogramma clicca il pulsante con il tasto sinistro del mouse,\n"
                        + " - Per cancellare un pittogramma cliccalo con il tasto destro del mouse,\n"
                        + " - Cancella tutto, elimina tutti i pittogrammi scritti nella barra di testo,\n\n"
                        + "Modalità di modifica della griglia\n"
                        + " - Aggiungi, rimuovi e pulisci tutto modificano la griglia selezionata,\n"
                        + " - Barra di ricerca \"Cerca pittogramma...\" per cercare i pittogrammi da inserire\n"
                        + " - Modifica griglia, modifica il file relativo alla griglia selezionata con le nouove modifiche.\n"
                        + " - Salva nuova griglia, salva nella cartella \"" +  folderpathSavedGrid + "\" una nuova griglia chiedendo il nome da assegnargli.\n"
                        + " - ELimina griglia, elimina dalla cartella \"" +  folderpathSavedGrid + "\" la griglia seleizonata.\n"
                ));

        pack();
    }

    //funzione per caricare una griglia
    public void uploadGrid(ButtonGroup buttonGroupImageGrid, String gridFileName){
        Enumeration groupElementsUploadGrid = buttonGroupImageGrid.getElements();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathAbsGrid + gridFileName));
            String lineNameImage;
            while ((lineNameImage = reader.readLine()) != null) {
                ImageButton buttonImageSelected = (ImageButton) groupElementsUploadGrid.nextElement();
                String imagePath = pathAbsImage + lineNameImage + ".png";
                if (buttonImageSelected.imageExist(imagePath)){
                    buttonImageSelected.setImage(imagePath,lineNameImage,larg,lung);
                } else if(getClass().getResourceAsStream("/imageLib/" + lineNameImage + ".png") != null) {
                    buttonImageSelected.setImageJar(ImageIO.read(getClass().getResourceAsStream("/imageLib/" + lineNameImage + ".png")),lineNameImage,larg,lung);
                }
            }
            reader.close();
        } catch (Exception wf) {
            wf.getStackTrace();
        }
    }

    public void initializeComboBox(){
        File folderGridSaved = new File(pathAbsGrid);
        String[] stringComboBox;
        if(folderGridSaved.exists() || folderGridSaved.list() != null){
            stringComboBox = folderGridSaved.list();
            for(int i=0;i<stringComboBox.length;i++){
                comboBoxSavedGrid.addItem(stringComboBox[i].replace(".txt",""));
            }
        }
    }

    public boolean deleteAllGrid(ButtonGroup buttonGroupImageGrid){
        Enumeration groupElementsDeleteAllGrid = buttonGroupImageGrid.getElements();
        Boolean whileExit = false;
        while (groupElementsDeleteAllGrid.hasMoreElements() && !whileExit) {
            ImageButton buttonImageSelected = (ImageButton) groupElementsDeleteAllGrid.nextElement();
            buttonImageSelected.deleteImage();
        }
        return true;
    }

    //ho lasciato lo stesso la funzione anche se non ho messo icone
    public ImageIcon createIcon(BufferedImage inputStreamImage, String iconname) {
        ImageIcon icon = new ImageIcon(inputStreamImage);
        Image imageIc = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon iconic = new ImageIcon(imageIc);
        return iconic;
    }

}
