/* 
 * Projeto: Plataforma de Aulas - Estudo em Java com Swing 
 * Autor: Rodrigo 
 * Descrição: Este projeto tem como objetivo criar uma aplicação desktop em Java 
 *            que permita ao usuário assistir vídeos de aulas ou ler PDFs armazenados localmente. 
 *            O foco é educacional, com estrutura clara e código comentado para facilitar o aprendizado. 
 */ 
 
import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*; 
import java.io.File; 
import java.io.IOException; 
import java.util.ArrayList; 
import java.util.List; 
import javax.swing.filechooser.FileSystemView; 
import java.awt.image.BufferedImage; 
import javax.imageio.ImageIO; 
import java.nio.file.Files; 
import java.nio.file.Paths; 
import javax.swing.event.ListSelectionEvent; 
import javax.swing.event.ListSelectionListener; 
import javafx.embed.swing.JFXPanel; 
import javafx.scene.Scene; 
import javafx.scene.layout.StackPane; 
import javafx.scene.media.Media; 
import javafx.scene.media.MediaPlayer; 
import javafx.scene.media.MediaView; 
import javafx.application.Platform; 
import java.nio.file.StandardCopyOption; 
 
public class PlataformaAulas extends JFrame { 
 
    private List<File> listaArquivos = new ArrayList<>(); 
    private JList<File> listaItens; 
    private JButton btnOpen; 
    private JLabel lblTitulo; 
    private JList<String> listaCategorias; 
private String conteudoDir = "videos"; 
private String thumbnailsDir = "thumbnails"; 
private JMenuBar menuBar; 
 
    public PlataformaAulas() { 
        super("Plataforma de Aulas - Projeto de Estudo"); 
 
        // Configurações básicas da janela 
setSize(800, 600); 
setDefaultCloseOperation(EXIT_ON_CLOSE); 
setLocationRelativeTo(null); 
setLayout(new BorderLayout()); 

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
menuGerenciar.add(addCategoria); 
menuGerenciar.add(removeCategoria); 
menuGerenciar.add(addItem); 
menuGerenciar.add(removeItem); 
menuBar.add(menuGerenciar); 
setJMenuBar(menuBar); 

// Título da aplicação 
lblTitulo = new JLabel("Selecione uma categoria e item para abrir", JLabel.CENTER); 
lblTitulo.setFont(new Font("Arial", Font.BOLD, 18)); 
add(lblTitulo, BorderLayout.NORTH); 
 
        // Lista de categorias 
        listaCategorias = new JList<>(); 
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
 
        // Lista de itens 
        listaItens = new JList<>(); 
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
 
        // Criar pasta de thumbnails se não existir 
        new File(thumbnailsDir).mkdirs(); 
    } 
 
    // Carregar categorias (subpastas em videos) 
    private void carregarCategorias() { 
        File pastaConteudo = new File(conteudoDir); 
        if (pastaConteudo.exists() && pastaConteudo.isDirectory()) { 
            File[] subpastas = pastaConteudo.listFiles(File::isDirectory); 
            if (subpastas != null) { 
                String[] nomes = new String[subpastas.length]; 
                for (int i = 0; i < subpastas.length; i++) { 
                    nomes[i] = subpastas[i].getName(); 
                } 
                listaCategorias.setListData(nomes); 
            } 
        } 
    } 
 
    // Carregar itens da categoria selecionada 
    private void carregarItensDaCategoria(String categoria) { 
        listaArquivos.clear(); 
        File pastaCategoria = new File(conteudoDir + "/" + categoria); 
        if (pastaCategoria.exists() && pastaCategoria.isDirectory()) { 
            String[] extensions; 
            if (categoria.equals("conteudo")) { 
                extensions = new String[]{"pdf"}; 
            } else { 
                extensions = new String[]{"mp4", "avi"}; 
            } 
            File[] arquivos =pastaCategoria.listFiles((dir, name) -> { 
                for (String ext : extensions) { 
                    if (name.endsWith("." + ext)) return true; 
                } 
                return false; 
            }); 
            if (arquivos != null) { 
                for (File f : arquivos) { 
                    listaArquivos.add(f); 
                    if (!categoria.equals("conteudo")) { 
                        gerarThumbnail(f); 
                    } 
                } 
            } 
        } 
        listaItens.setListData(listaArquivos.toArray(new File[0])); 
    } 
 
    // Gerar thumbnail usando FFmpeg para vídeos 
    private void gerarThumbnail(File video) { 
        String thumbnailPath = thumbnailsDir + "/" + video.getName() + ".png"; 
        if (!new File(thumbnailPath).exists()) { 
            try { 
                ProcessBuilder pb = new ProcessBuilder("tools/ffmpeg.exe", "-i", video.getAbsolutePath(), "-ss", "00:00:01.000", "-vframes", "1", thumbnailPath); 
                pb.redirectErrorStream(true); 
                Process process = pb.start(); 
                process.waitFor(); 
            } catch (Exception ex) { 
                ex.printStackTrace(); 
            } 
        } 
    } 
 
    // Renderizador personalizado para JList com thumbnail ou ícone do sistema 
    private class ItemListRenderer extends DefaultListCellRenderer { 
        private FileSystemView fileSystemView = FileSystemView.getFileSystemView(); 
 
        @Override 
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) { 
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus); 
            File item = (File) value; 
            String thumbnailPath = thumbnailsDir + "/" + item.getName() + ".png"; 
            if (new File(thumbnailPath).exists()) { 
                try { 
                    BufferedImage img = ImageIO.read(new File(thumbnailPath)); 
                    label.setIcon(new ImageIcon(img.getScaledInstance(100, 75, Image.SCALE_SMOOTH))); 
                } catch (IOException e) { 
                    e.printStackTrace(); 
                } 
            } else { 
                label.setIcon(fileSystemView.getSystemIcon(item)); 
            } 
            label.setText(item.getName()); 
            label.setHorizontalTextPosition(SwingConstants.RIGHT); 
            return label; 
        } 
    } 
 
    // Método para abrir item externo (para PDFs) 
private void abrirItem(File item) { 
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
            MediaView mediaView = new MediaView(mediaPlayer); 
            StackPane root = new StackPane(); 
            root.getChildren().add(mediaView); 
            Scene scene = new Scene(root); 
            fxPanel.setScene(scene); 
            mediaPlayer.play(); 
        } catch (Exception ex) { 
            ex.printStackTrace(); 
        } 
    }); 
    dialog.setVisible(true); 
} 

// Métodos para gerenciar categorias e itens 
private void adicionarCategoria() { 
    String nome = JOptionPane.showInputDialog(this, "Nome da nova categoria:"); 
    if (nome != null && !nome.trim().isEmpty()) { 
        File novaPasta = new File(conteudoDir + "/" + nome); 
        if (novaPasta.mkdir()) { 
            carregarCategorias(); 
            JOptionPane.showMessageDialog(this, "Categoria adicionada."); 
        } else { 
            JOptionPane.showMessageDialog(this, "Erro ao adicionar categoria."); 
        } 
    } 
} 

private void removerCategoria() { 
    String categoria = listaCategorias.getSelectedValue(); 
    if (categoria != null) { 
        File pasta = new File(conteudoDir + "/" + categoria); 
        if (pasta.delete()) { 
            carregarCategorias(); 
            JOptionPane.showMessageDialog(this, "Categoria removida."); 
        } else { 
            JOptionPane.showMessageDialog(this, "Erro ao remover categoria (verifique se está vazia)."); 
        } 
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
            File destino = new File(conteudoDir + "/" + categoria + "/" + selectedFile.getName()); 
            try { 
                Files.copy(selectedFile.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING); 
                carregarItensDaCategoria(categoria); 
                JOptionPane.showMessageDialog(this, "Item adicionado."); 
            } catch (IOException ex) { 
                JOptionPane.showMessageDialog(this, "Erro ao adicionar item: " + ex.getMessage()); 
            } 
        } 
    } else { 
        JOptionPane.showMessageDialog(this, "Selecione uma categoria."); 
    } 
} 

private void removerItem() { 
    File item = listaItens.getSelectedValue(); 
    if (item != null) { 
        if (item.delete()) { 
            String categoria = listaCategorias.getSelectedValue(); 
            carregarItensDaCategoria(categoria); 
            JOptionPane.showMessageDialog(this, "Item removido."); 
        } else { 
            JOptionPane.showMessageDialog(this, "Erro ao remover item."); 
        } 
    } else { 
        JOptionPane.showMessageDialog(this, "Selecione um item."); 
    } 
} 
 
    public static void main(String[] args) { 
        SwingUtilities.invokeLater(() -> { 
            new PlataformaAulas().setVisible(true); 
        }); 
    } 
}