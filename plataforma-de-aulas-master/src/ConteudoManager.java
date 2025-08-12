import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.Arrays;

public class ConteudoManager {
    private String conteudoDir = "plataforma-de-aulas-master" + File.separator + "videos";
    private String thumbnailsDir = "plataforma-de-aulas-master" + File.separator + "thumbnails";

    public ConteudoManager() {
        new File(thumbnailsDir).mkdirs();
    }

    public String[] carregarCategorias() {
        File pastaConteudo = new File(conteudoDir);
        System.out.println("conteudoDir absolute: " + pastaConteudo.getAbsolutePath());
        System.out.println("Exists: " + pastaConteudo.exists());
        System.out.println("Is directory: " + pastaConteudo.isDirectory());
        if (pastaConteudo.exists() && pastaConteudo.isDirectory()) {
            File[] subpastas = pastaConteudo.listFiles(File::isDirectory);
            if (subpastas != null) {
                System.out.println("Found " + subpastas.length + " subfolders");
                String[] nomes = new String[subpastas.length];
                for (int i = 0; i < subpastas.length; i++) {
                    nomes[i] = subpastas[i].getName();
                    System.out.println("Category: " + nomes[i]);
                }
                return nomes;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Diretório de conteúdo não encontrado ou não é um diretório: " + pastaConteudo.getAbsolutePath());
        }
        return new String[0];
    }

    public List<File> carregarItensDaCategoria(String categoria) {
        List<File> listaArquivos = new ArrayList<>();
        File pastaCategoria = new File(conteudoDir + "/" + categoria);
        if (pastaCategoria.exists() && pastaCategoria.isDirectory()) {
            String[] extensions;
            if (categoria.equals("conteudo")) {
                extensions = new String[]{"pdf"};
            } else {
                extensions = new String[]{"mp4", "avi"};
            }
            File[] arquivos = pastaCategoria.listFiles((dir, name) -> Arrays.stream(extensions).anyMatch(ext -> name.endsWith("." + ext)));
            if (arquivos != null) {
                for (File f : arquivos) {
                    listaArquivos.add(f);
                    if (!categoria.equals("conteudo")) {
                        gerarThumbnail(f);
                    }
                }
            }
        }
        return listaArquivos;
    }

    public void gerarThumbnail(File video) {
        String thumbnailPath = thumbnailsDir + "/" + video.getName() + ".png";
        if (!new File(thumbnailPath).exists()) {
            File ffmpeg = new File("tools/ffmpeg.exe");
            if (ffmpeg.exists()) {
                try {
                    ProcessBuilder pb = new ProcessBuilder(ffmpeg.getAbsolutePath(), "-i", video.getAbsolutePath(), "-ss", "00:00:01.000", "-vframes", "1", thumbnailPath);
                    pb.redirectErrorStream(true);
                    Process process = pb.start();
                    process.waitFor();
                } catch (Exception ex) {
                    System.err.println("Erro ao gerar thumbnail: " + ex.getMessage());
                }
            } else {
                System.err.println("ffmpeg não encontrado em tools/ffmpeg.exe. Thumbnail não gerado.");
            }
        }
    }

    public void adicionarCategoria(String nome) {
        File novaPasta = new File(conteudoDir + "/" + nome);
        if (novaPasta.mkdir()) {
            // Carregar categorias seria chamado na UI
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao adicionar categoria.");
        }
    }

    public void removerCategoria(String categoria) {
        File pasta = new File(conteudoDir + "/" + categoria);
        if (pasta.delete()) {
            // Carregar categorias seria chamado na UI
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao remover categoria (verifique se está vazia)."); 
        }
    }

    public void adicionarItem(String categoria, File selectedFile) {
        File destino = new File(conteudoDir + "/" + categoria + "/" + selectedFile.getName());
        try {
            Files.copy(selectedFile.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            // Carregar itens seria chamado na UI
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao adicionar item: " + ex.getMessage());
        }
    }

    public void removerItem(File item) {
        if (item.delete()) {
            // Carregar itens seria chamado na UI
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao remover item.");
        }
    }

    public String getThumbnailsDir() {
        return thumbnailsDir;
    }

    public String getConteudoDir() {
        return conteudoDir;
    }

    public String getVideosDir() {
        return conteudoDir;
    }
}