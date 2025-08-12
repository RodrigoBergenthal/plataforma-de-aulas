# Plataforma de Aulas

## Descrição
Esta é uma aplicação desktop em Java usando Swing integrado com JavaFX para gerenciar categorias, itens, reproduzir vídeos internamente e abrir PDFs. Vídeos têm thumbnails gerados com FFmpeg, e PDFs usam ícones do sistema.

## Requisitos
- Java JDK 11 ou superior instalado (para suporte a módulos JavaFX).
- FFmpeg (não incluído; baixe e coloque em `tools/`).
- JavaFX SDK (baixe de https://gluonhq.com/products/javafx/ e copie os arquivos JAR para a pasta `lib/` do projeto).

## Instalação de Dependências
1. **Instale Java JDK**: Baixe e instale do site oficial da Oracle ou use OpenJDK. Adicione ao PATH se necessário.
2. **FFmpeg**: Baixe o executável do site oficial do FFmpeg (versão estática para Windows), extraia `ffmpeg.exe` e coloque em `tools/`.

## Como Rodar a Aplicação
1. Navegue até o diretório do projeto: `cd caminho/para/plataforma de teste`.
2. Compile o código: `javac --enable-preview -source 17 -classpath "lib/*;src" src/ConteudoManager.java src/PlataformaAulas.java`.
3. Execute: `java --enable-preview -classpath "lib/*;src" PlataformaAulas`.

Ou em um comando só (PowerShell): `javac --enable-preview -source 17 -classpath "lib/*;src" src/ConteudoManager.java src/PlataformaAulas.java; if ($LASTEXITCODE -eq 0) { java --enable-preview -classpath "lib/*;src" PlataformaAulas }`.

## Estrutura de Pastas
- `videos/`: Contém subpastas para categorias (ex: `VideoAula/` para vídeos, `conteudo/` para PDFs).
- `thumbnails/`: Armazena thumbnails gerados para vídeos.
- `tools/`: Onde colocar `ffmpeg.exe` (não incluído no repositório).

## Uso
- Use o menu "Gerenciar" para adicionar/remover categorias e itens.
- Selecione uma categoria na lista à esquerda.
- Selecione um item na lista central.
- Clique em "Abrir Item" para reproduzir vídeo internamente ou abrir PDF.

## Configuração do GitHub
Após clonar ou baixar, para contribuir:
1. Crie um repositório no GitHub.
2. Adicione o remote: `git remote add origin https://github.com/seuusuario/nome-do-repo.git`.
3. Push: `git push -u origin master`.