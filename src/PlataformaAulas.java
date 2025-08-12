/* 
 * Projeto: Plataforma de Aulas - Estudo em Java com Swing 
 * Autor: Rodrigo 
 * Descrição: Este projeto tem como objetivo criar uma aplicação desktop em Java 
 *            que permita ao usuário assistir vídeos de aulas ou ler PDFs armazenados localmente. 
 *            O foco é educacional, com estrutura clara e código comentado para facilitar o aprendizado. 
 */ 
 
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
// Add JavaFX SDK to project classpath and module path
// Make sure to use the correct JavaFX version matching your Java version
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
 
public class PlataformaAulas extends JFrame { 
 
    private List<File> listaArquivos = new ArrayList<>(); 
    private JList<File> listaItens; 
    private JButton btnOpen; 
    private JLabel lblTitulo; 
    private JList<String> listaCategorias; 
    private ConteudoManager manager = new ConteudoManager(); 
    private JMenuBar menuBar; 
 
    public PlataformaAulas() { 
        super("Plataforma de Aulas - Projeto de Estudo"); 
 
        // Configurações básicas da janela 
        setSize(800, 600); 
        setDefaultCloseOperation(EXIT_ON_CLOSE); 
        setLocationRelativeTo(null); 
        setLayout(new BorderLayout()); 
        Platform.setImplicitExit(false);
        FlatLightLaf.setup();

        // Menu para gerenciar categorias e itens 
        menuBar = new JMenuBar(); 
        JMenu menuGerenciar = new JMenu("Gerenciar"); 
        JMenuItem addCategoria = new JMenuItem("Adicionar Categoria"); 
        addCategoria.addActionListener(e -> adicionarCategoria()); 
        JMenuItem removeCategoria = new JMenuItem("Remover Categoria"); 
        removeCategoria.addActionListener(e -> removerCategoria()); 
        JMenuItem addItem = new JMenuItem("Adicionar Item"); 
        addItem.addActionListener(e -> adicionarItem()); 
        JMenuItem removeItem = new JMenuItem("Remover Item"); 
        removeItem.addActionListener(e -> removerItem()); 
        JMenuItem editItem = new JMenuItem("Editar Item");
        editItem.addActionListener(e -> editarItem());
        JMenuItem editCategoria = new JMenuItem("Editar Categoria");
        editCategoria.addActionListener(e -> editarCategoria());
        menuGerenciar.add(addCategoria); 
        menuGerenciar.add(removeCategoria); 
        menuGerenciar.add(editCategoria);
        menuGerenciar.add(addItem); 
        menuGerenciar.add(removeItem); 
        menuGerenciar.add(editItem); 
        menuBar.add(menuGerenciar); 
        setJMenuBar(menuBar); 

        // Adicionar ícones aos itens do menu
        addCategoria.setIcon(new FlatSVGIcon("resources/add_icon.svg"));
        removeCategoria.setIcon(new FlatSVGIcon("resources/remove_icon.svg"));
        addItem.setIcon(new FlatSVGIcon("resources/add_icon.svg"));
        removeItem.setIcon(new FlatSVGIcon("resources/remove_icon.svg"));
        editItem.setIcon(new FlatSVGIcon("resources/edit_icon.svg"));
        editCategoria.setIcon(new FlatSVGIcon("resources/edit_icon.svg"));

        // Cabeçalho com logo
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel logoLabel = new JLabel(new FlatSVGIcon("resources/logo.svg").setColorFilter(new FlatSVGIcon.ColorFilter(color -> Color.BLUE)));
        headerPanel.add(logoLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Lista de categorias com tooltip
        listaCategorias = new JList<>();
        listaCategorias.setToolTipText("Categorias disponíveis");
        carregarCategorias(); 
        listaCategorias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
        listaCategorias.addListSelectionListener(new ListSelectionListener() { 
            @Override 
            public void valueChanged(ListSelectionEvent e) { 
                if (!e.getValueIsAdjusting()) { 
                    String categoria = listaCategorias.getSelectedValue(); 
                    if (categoria != null) { 
                        carregarItensDaCategoria(categoria); 
                    } 
                } 
            } 
        }); 
        add(new JScrollPane(listaCategorias), BorderLayout.WEST); 
 
        // Lista de itens com tooltip
        listaItens = new JList<>();
        listaItens.setToolTipText("Itens da categoria selecionada");
        listaItens.setCellRenderer(new ItemListRenderer()); 
        listaItens.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
        add(new JScrollPane(listaItens), BorderLayout.CENTER); 
 
        // Carregar itens iniciais 
        if (listaCategorias.getModel().getSize() > 0) { 
            listaCategorias.setSelectedIndex(0); 
            carregarItensDaCategoria(listaCategorias.getSelectedValue()); 
        } 
 
        // Botão de abertura 
        btnOpen = new JButton("Abrir Item"); 
        btnOpen.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                File itemSelecionado = listaItens.getSelectedValue(); 
                if (itemSelecionado != null) { 
                    if (itemSelecionado.getName().endsWith(".pdf")) { 
                        abrirItem(itemSelecionado); 
                    } else { 
                        abrirVideoIntegrado(itemSelecionado); 
                    } 
                } else { 
                    JOptionPane.showMessageDialog(null, "Selecione um item da lista."); 
                } 
            } 
        }); 
        add(btnOpen, BorderLayout.SOUTH); 
        // Layout organizado com JSplitPane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(listaCategorias), new JScrollPane(listaItens));
        splitPane.setDividerLocation(200);
        add(splitPane, BorderLayout.CENTER);
    } 
 
    // Carregar categorias 
    private void carregarCategorias() { 
        String[] categorias = manager.carregarCategorias(); 
        listaCategorias.setListData(categorias); 
        // Seleciona a primeira categoria se houver alguma, para carregar seus itens
        if (listaCategorias.getModel().getSize() > 0) {
            listaCategorias.setSelectedIndex(0);
        }
    } 
 
    // Carregar itens da categoria selecionada 
    private void carregarItensDaCategoria(String categoria) { 
        listaArquivos = manager.carregarItensDaCategoria(categoria); 
        listaItens.setListData(listaArquivos.toArray(new File[0])); 
    } 
 
    // Renderizador personalizado para JList com thumbnail ou ícone do sistema 
    private class ItemListRenderer extends DefaultListCellRenderer { 
        private FileSystemView fileSystemView = FileSystemView.getFileSystemView(); 
 
        @Override 
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) { 
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus); 
            File item = (File) value; 
            String thumbnailPath = manager.getThumbnailsDir() + "/" + item.getName() + ".png"; 
            if (new File(thumbnailPath).exists()) { 
                try { 
                    BufferedImage img = ImageIO.read(new File(thumbnailPath));
                    Image scaled = img.getScaledInstance(150, 112, Image.SCALE_SMOOTH); // Tamanho maior
                    label.setIcon(new ImageIcon(scaled));
                    label.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // Borda
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                label.setIcon(fileSystemView.getSystemIcon(item));
            }
            label.setText(item.getName());
            label.setHorizontalTextPosition(SwingConstants.RIGHT);
            label.setFont(new Font("SansSerif", Font.PLAIN, 14)); // Fonte personalizada
            return label;
        }
    } 
 
    // Método para abrir item externo (para PDFs) 
    private void abrirItem(File item) { 
        JProgressBar progress = new JProgressBar();
        progress.setIndeterminate(true);
        JOptionPane.showMessageDialog(this, progress, "Carregando...", JOptionPane.PLAIN_MESSAGE);
        try { 
            Runtime.getRuntime().exec(new String[] {"cmd", "/c", "start", "", item.getAbsolutePath()}); 
        } catch (IOException ex) { 
            JOptionPane.showMessageDialog(null, "Erro ao abrir item: " + ex.getMessage()); 
        } 
    } 

    // Método para abrir vídeo integrado com JavaFX 
    private void abrirVideoIntegrado(File video) { 
        JDialog dialog = new JDialog(this, "Player de Vídeo", true); 
        dialog.setSize(800, 600); 
        dialog.setLocationRelativeTo(this); 
        JFXPanel fxPanel = new JFXPanel(); 
        dialog.add(fxPanel); 
        Platform.runLater(() -> { 
            try { 
                Media media = new Media(video.toURI().toString()); 
                MediaPlayer mediaPlayer = new MediaPlayer(media); 
                mediaPlayer.setOnError(() -> System.err.println("Erro no MediaPlayer: " + mediaPlayer.getError().getMessage())); 
                MediaView mediaView = new MediaView(mediaPlayer); 
                StackPane root = new StackPane(); 
                root.getChildren().add(mediaView); 
                Scene scene = new Scene(root, 800, 600); 
                fxPanel.setScene(scene); 
                mediaPlayer.play(); 
            } catch (Exception ex) { 
                JOptionPane.showMessageDialog(null, "Exceção ao abrir vídeo: " + ex.getMessage()); 
            } 
        }); 
        dialog.setVisible(true); 
    } 

    // Métodos para gerenciar categorias e itens 
    private void adicionarCategoria() { 
        String nome = JOptionPane.showInputDialog(this, "Nome da nova categoria:"); 
        if (nome != null && !nome.trim().isEmpty()) { 
            manager.adicionarCategoria(nome); 
            carregarCategorias(); 
            JOptionPane.showMessageDialog(this, "Categoria adicionada."); 
        } 
    } 

    private void removerCategoria() { 
        String categoria = listaCategorias.getSelectedValue(); 
        if (categoria != null) { 
            manager.removerCategoria(categoria); 
            carregarCategorias(); 
            JOptionPane.showMessageDialog(this, "Categoria removida."); 
        } else { 
            JOptionPane.showMessageDialog(this, "Selecione uma categoria."); 
        } 
    } 

    private void adicionarItem() { 
        String categoria = listaCategorias.getSelectedValue(); 
        if (categoria != null) { 
            JFileChooser chooser = new JFileChooser(); 
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
                File selectedFile = chooser.getSelectedFile(); 
                manager.adicionarItem(categoria, selectedFile); 
                carregarItensDaCategoria(categoria); 
                JOptionPane.showMessageDialog(this, "Item adicionado."); 
            } 
        } else { 
            JOptionPane.showMessageDialog(this, "Selecione uma categoria."); 
        } 
    } 

    private void removerItem() { 
        File item = listaItens.getSelectedValue(); 
        if (item != null) { 
            manager.removerItem(item); 
            String categoria = listaCategorias.getSelectedValue(); 
            carregarItensDaCategoria(categoria); 
            JOptionPane.showMessageDialog(this, "Item removido."); 
        } else { 
            JOptionPane.showMessageDialog(this, "Selecione um item."); 
        } 
    }
    
    private void editarItem() {
        File itemSelecionado = listaItens.getSelectedValue();
        if (itemSelecionado != null) {
            String novoNome = JOptionPane.showInputDialog(this, "Novo nome para o item:", itemSelecionado.getName());
            if (novoNome != null && !novoNome.isEmpty()) {
                File novoArquivo = new File(itemSelecionado.getParent(), novoNome);
                if (itemSelecionado.renameTo(novoArquivo)) {
                    carregarItensDaCategoria(listaCategorias.getSelectedValue());
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao renomear item.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um item para editar.");
        }
    }
    
    private void editarCategoria() {
        String categoriaSelecionada = listaCategorias.getSelectedValue();
        if (categoriaSelecionada != null) {
            String novoNome = JOptionPane.showInputDialog(this, "Novo nome para a categoria:", categoriaSelecionada);
            if (novoNome != null && !novoNome.isEmpty()) {
                File pastaAtual = new File(manager.getVideosDir(), categoriaSelecionada);
                File novaPasta = new File(manager.getVideosDir(), novoNome);
                if (pastaAtual.renameTo(novaPasta)) {
                    carregarCategorias();
                    // Seleciona a categoria recém-renomeada para recarregar seus itens
                    int newIndex = -1;
                    for (int i = 0; i < listaCategorias.getModel().getSize(); i++) {
                        if (listaCategorias.getModel().getElementAt(i).equals(novoNome)) {
                            newIndex = i;
                            break;
                        }
                    }
                    if (newIndex != -1) {
                        listaCategorias.setSelectedIndex(newIndex);
                    } else {
                        // Se a categoria renomeada não for encontrada (improvável), selecione a primeira
                        if (listaCategorias.getModel().getSize() > 0) {
                            listaCategorias.setSelectedIndex(0);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao renomear categoria.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma categoria para editar.");
        }
    } 
 
    public static void main(String[] args) { 
        SwingUtilities.invokeLater(() -> { 
            new PlataformaAulas().setVisible(true); 
        }); 
    }
}