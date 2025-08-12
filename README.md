# Plataforma de Aulas

## Descrição
Esta é uma aplicação desktop em Java usando Swing integrado com JavaFX para gerenciar categorias, itens, reproduzir vídeos internamente e abrir PDFs. Vídeos têm thumbnails gerados com FFmpeg, e PDFs usam ícones do sistema. Agora com tema FlatLaf para uma UI moderna, ícones SVG personalizados, layout reorganizado com JSplitPane e toolbar, thumbnails maiores, cores e fontes customizadas, feedback de loading e tooltips.

## Requisitos
- Java JDK 11 ou superior instalado (para suporte a módulos JavaFX).
- FFmpeg (não incluído; baixe e coloque em `tools/`).
- JavaFX SDK (baixe de https://gluonhq.com/products/javafx/ e extraia para `javafx-sdk/`).
- FlatLaf, FlatLaf Extras e JSVG (já incluídos em `lib/`).

## Instalação de Dependências
1. **Instale Java JDK**: Baixe e instale do site oficial da Oracle ou use OpenJDK. Adicione ao PATH se necessário.
2. **FFmpeg**: Baixe o executável do site oficial do FFmpeg (versão estática para Windows), extraia `ffmpeg.exe` e coloque em `tools/`.

## Como Rodar a Aplicação
1. Navegue até o diretório do projeto: `cd c:\Users\Rodrigo Bergenthal\Desktop\java\plataforma-de-aulas-master`.
2. Compile o código: `javac --module-path "javafx-sdk\javafx-sdk-17.0.12\lib" --add-modules javafx.controls,javafx.fxml,javafx.media,javafx.swing -cp ".;lib\flatlaf-3.6.1.jar;lib\flatlaf-extras-3.6.1.jar;lib\jsvg-2.0.0.jar;src" -d out src\PlataformaAulas.java`.
3. Execute: `java --module-path "javafx-sdk\javafx-sdk-17.0.12\lib" --add-modules javafx.controls,javafx.fxml,javafx.media,javafx.swing -cp ".;lib\flatlaf-3.6.1.jar;lib\flatlaf-extras-3.6.1.jar;lib\jsvg-2.0.0.jar;out" PlataformaAulas`.

Ou em um comando só (PowerShell): `javac --module-path "javafx-sdk\javafx-sdk-17.0.12\lib" --add-modules javafx.controls,javafx.fxml,javafx.media,javafx.swing -cp ".;lib\flatlaf-3.6.1.jar;lib\flatlaf-extras-3.6.1.jar;lib\jsvg-2.0.0.jar;src" -d out src\PlataformaAulas.java; if ($LASTEXITCODE -eq 0) { java --module-path "javafx-sdk\javafx-sdk-17.0.12\lib" --add-modules javafx.controls,javafx.fxml,javafx.media,javafx.swing -cp ".;lib\flatlaf-3.6.1.jar;lib\flatlaf-extras-3.6.1.jar;lib\jsvg-2.0.0.jar;out" PlataformaAulas }`.

## Estrutura de Pastas
- `videos/`: Contém subpastas para categorias (ex: `VideoAula/` para vídeos, `conteudo/` para PDFs).
- `thumbnails/`: Armazena thumbnails gerados para vídeos.
- `tools/`: Onde colocar `ffmpeg.exe` (não incluído no repositório).
- `lib/`: Bibliotecas como FlatLaf, FlatLaf Extras e JSVG.
- `src/resources/`: Ícones SVG e logo.

## Uso
- Use o menu "Gerenciar" para adicionar/remover categorias e itens.
- Selecione uma categoria na lista à esquerda.
- Selecione um item na lista central.
- Clique em "Abrir Item" para reproduzir vídeo internamente ou abrir PDF.

## Melhorias Recentes
- Tema FlatLaf para UI moderna.
- Ícones SVG personalizados em botões e menus.
- Layout reorganizado com JSplitPane e toolbar.
- Thumbnails maiores com bordas.
- Cores e fontes customizadas.
- Feedback visual de loading com barra de progresso.
- Tooltips e header com logo.

## Configuração do GitHub
Após clonar ou baixar, para contribuir:
1. Crie um repositório no GitHub.
2. Adicione o remote: `git remote add origin https://github.com/seuusuario/nome-do-repo.git`.
3. Push: `git push -u origin master`.